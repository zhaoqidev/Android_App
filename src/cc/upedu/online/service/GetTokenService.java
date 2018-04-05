package cc.upedu.online.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.ryim.TokenObject;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.ryim.ApiHttpClient;
import cc.upedu.ryim.FormatType;
import cc.upedu.ryim.SdkHttpResult;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;


public class GetTokenService extends Service {

	private final int RONGYUN_CONNECT=1;
	private final int SAVETOKEN=2;
	public SharedPreferences tokenSp;//用于存储token 的sp
	public String userId;//用户Id
//	public String userName;//用户名
//	public String userAvatar;//用户头像
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		tokenSp = getApplicationContext().getSharedPreferences("TokenConfig", MODE_PRIVATE);		
		super.onCreate();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			userId=intent.getStringExtra("userId");
			//先判断用户是否登录
			//已登录:再判断是否是同一用户
			//同一用户:判断token是否为空。
			//不是，直接请求token
			if (UserStateUtil.isLogined()) {
				if(userId.equals(tokenSp.getString("oldUserId", ""))){
					if (StringUtil.isEmpty(tokenSp.getString("token", ""))) {
						new Thread(runnable).start();
						Log.i("token", "同一用户,token不存在，开始请求token");
					}else {
						Message msg = new Message();
						msg.what = SAVETOKEN;
						msg.obj = tokenSp.getString("token", "");
						handler.sendMessage(msg);
						Log.i("token", "同一用户,token已存在");
					}
				}else {
					new Thread(runnable).start();
					Log.i("token", "新用户,开始请求token");
				}
			}
		
		} catch (Exception e) {
		}
		return super.onStartCommand(intent, flags, startId);
	};
	/**
	 * 开启子线程获取token
	 */
	Runnable runnable = new Runnable(){
		 @Override
		 public void run() {
		 //
		 // TODO: http request.
		 //
			 try {
				SdkHttpResult result =ApiHttpClient.getToken("qf3d5gbj3rbbh", "BLQ4GuK0hunQ",  
						UserStateUtil.getUserId(), SharedPreferencesUtil.getInstance().spGetString("name"), 
						ConstantsOnline.SERVER_IMAGEURL+SharedPreferencesUtil.getInstance().spGetString("avatar"), FormatType.json);
				result.getResult();
				Message msg = new Message();
				msg.what = RONGYUN_CONNECT;
				TokenObject token= GsonUtil.jsonToBean(result.getResult(), TokenObject.class);
				msg.obj = token;
				handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		 }
		};
	

	
	// 消息处理器
		private Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				
				
				case RONGYUN_CONNECT:
					
					TokenObject token = (TokenObject) msg.obj;
					tokenSp.edit().putString("token", token.getToken()).commit();
					tokenSp.edit().putString("oldUserId", UserStateUtil.getUserId()).commit();
					connect(token.getToken());
					Log.i("token", "token请求成功,并保存,建立连接");
					break; 
					
				case SAVETOKEN:
					connect((String) msg.obj);
					Log.i("token", "token已正常存在,建立连接");
					break;
				}
				}
			};
			
			/**
			 * 建立与融云服务器的连接
			 *
			 * @param token
			 */
			 private void connect(String token) {

			        if (getApplicationInfo().packageName.equals(OnlineApp.getCurProcessName(getApplicationContext()))) {

			            /**
			             * IMKit SDK调用第二步,建立与服务器的连接
			             */
			            RongIM.connect(token, new RongIMClient.ConnectCallback() {

			                /**
			                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的 Token
			                 */
			                @Override
			                public void onTokenIncorrect() {

			                    Log.d("LoginActivity", "--onTokenIncorrect");
			                }

			                /**
			                 * 连接融云成功
			                 * @param userid 当前 token
			                 */
			                @Override
			                public void onSuccess(String userid) {

			                    Log.d("LoginActivity", "--onSuccess" + userid);
			                }

			                /**
			                 * 连接融云失败
			                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
			                 *                  http://www.rongcloud.cn/docs/android.html#常见错误码
			                 */
			                @Override
			                public void onError(RongIMClient.ErrorCode errorCode) {

			                    Log.d("LoginActivity", "--onError" + errorCode);
			                }
			            });
			        }
			        
			        stopSelf();//获取token、建立连接结束，关闭服务
			    }

}
