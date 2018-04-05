package cc.upedu.online.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.MainActivity;
import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.base.BaseActivity;
import cc.upedu.online.domin.LoginBean;
import cc.upedu.online.domin.UpdataAppBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.ryim.TokenObject;
import cc.upedu.online.service.DownloadService;
import cc.upedu.online.service.GetTokenService;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedConfig;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall2;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.ryim.ApiHttpClient;
import cc.upedu.ryim.FormatType;
import cc.upedu.ryim.SdkHttpResult;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

public class SplashActivity extends BaseActivity{
	protected static final int SHOW_UPDATE_DIALOG = 1;
	private static final int LOAD_MAINUI = 2;
	private static final int AUTO_LOGIN = 3;
	private static final int RONGYUN_CONNECT = 4;
//	private TextView tv_splash_version;
//	private TextView tv_info;
	
	private PackageManager packageManager;// 包管理器
//	private int clientVersionCode;//版本号
	private String version;//版本名

	
//	private String desc;// 服务器新资源描述信息
	
	private String downloadurl;// 服务器资源的下载路径
	
	//检查是否第一次进入所用参数
	private boolean first;	//判断是否第一次打开软件
	private SharedPreferences shared;
	//

	// 消息处理器
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_MAINUI:
				loadMainUI();
				break;
			case SHOW_UPDATE_DIALOG:
				showDownLodeDialog();
				break;
			case AUTO_LOGIN:
				autoLogin();
				getToken();//获取token
				break;
			case RONGYUN_CONNECT:
				
				TokenObject token = (TokenObject) msg.obj;
				connect(token.getToken());
				break;
			}
			}
		};

	
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_splash);
//		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
//		tv_info = (TextView) findViewById(R.id.tv_info);
		packageManager = getPackageManager();
		AlphaAnimation alphaA = new AlphaAnimation(0.0f, 1.0f);
		alphaA.setDuration(2000);
		findViewById(R.id.rl_splash_root).startAnimation(alphaA);
		
		shared = new SharedConfig(context).GetConfig(); 
		first=shared.getBoolean("First", true);
		if (first) {
			//如果第一次，则进入引导页WelcomeActivity
			Intent intent = new Intent(this,WelcomeActivity.class);			
			startActivity(intent);
			// 设置Activity的切换效果
			overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
//			SplashActivity.this.finish();
			
		}else{

			try {
				PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(), 0);
				version = packInfo.versionName;
//				clientVersionCode = packInfo.versionCode;
//				tv_splash_version.setText(version);
				
				checkVersion();//检查版本更新
				

			} catch (NameNotFoundException e) {
				e.printStackTrace();
				// 不会发生 can't reach
			}
		}
		
		
	
	
	}
	Runnable runnable = new Runnable(){
		 @Override
		 public void run() {
		 //
		 // TODO: http request.
		 //
			 try {
				SdkHttpResult result =ApiHttpClient.getToken("qf3d5gbj3rbbh", "BLQ4GuK0hunQ",
						UserStateUtil.getUserId()==null?"0":UserStateUtil.getUserId(),
						SharedPreferencesUtil.getInstance().spGetString("mobile"), "", FormatType.json);
				result.getResult();
				Message msg = new Message();
				msg.what = RONGYUN_CONNECT;
//				TokenObject token = new TokenObject();
				TokenObject token= GsonUtil.jsonToBean(result.getResult(), TokenObject.class);
				msg.obj = token;
				handler.sendMessage(msg);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		 }
		};

	/**
	 * 连接服务器 检查版本号 是否有更新
	 */
	private void checkVersion() {
		//Message msg = Message.obtain();
		Map<String, String> requestDataMap = ParamsMapUtil.updataApp(context, "android");
		RequestVo requestVo=new RequestVo(ConstantsOnline.UPDATA_APP, this, requestDataMap, new MyBaseParser<>(UpdataAppBean.class));
		
		DataCallBack<UpdataAppBean> callBack=new DataCallBack<UpdataAppBean>() {

			@Override
			public void processData(UpdataAppBean object) {
				if (object!=null) {
					if (object.entity!=null) {
							if ("android".equals(object.entity.kType)) {
								if (version.equals(object.entity.versionNo)) {
									handler.obtainMessage(AUTO_LOGIN).sendToTarget();
								}else {
									downloadurl=object.entity.downloadUrl;
									handler.obtainMessage(SHOW_UPDATE_DIALOG).sendToTarget();
								}
							}
					}else {
						handler.obtainMessage(LOAD_MAINUI).sendToTarget();
					}
				}else {
					ShowUtils.showMsg(context, "数据请求失败，请检查您的网络");
				}
				
			}
		};
		
		getDataServer(requestVo, callBack);
		
	}

	/**
	 * 打开主页面
	 */
	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
		
	}
	/**
	 * 弹出提示下载的dialog
	 */
	private void showDownLodeDialog() {	
		ShowUtils.showDiaLog2(SplashActivity.this, "更新提醒", "发现新版本，是否立即更新", new ConfirmBackCall2() {
			
			@Override
			public void confirmOperation() {
//				dialog.cancel();
				download(downloadurl);
				handler.obtainMessage(AUTO_LOGIN).sendToTarget();
			}
			
			@Override
			public void cancleOperation() {
//				dialog.cancel();
				handler.obtainMessage(AUTO_LOGIN).sendToTarget();
			}
		});
	}
	
	/**
	 * 多线程的下载器
	 * 
	 * @param downloadurl
	 */
	private void download(String downloadurl) {
		//从服务器下载APP更新
		ShowUtils.showMsg(context, "开始下载");
		Intent intent = new Intent(context,DownloadService.class);		// 开启更新服务UpdateService
		String appname ="成长吧"+version;
		intent.putExtra("appname", appname);
		intent.putExtra("url", downloadurl);
		context.startService(intent);
		//loadMainUI();
	}
	
	/**
	 * 打开App自动登陆
	 */
	private  void  autoLogin() {

			RequestParams params = new RequestParams();
			params.addQueryStringParameter("account", SharedPreferencesUtil.getInstance().spGetString("mobile"));
			params.addQueryStringParameter("password", SharedPreferencesUtil.getInstance().spGetString("passWord"));
			params.addQueryStringParameter("brand", StringUtil.getPhoneBrand());
			params.addQueryStringParameter("type", StringUtil.getPhoneType());
			params.addQueryStringParameter("size", StringUtil.getScreenSize(SplashActivity.this));
			params.addQueryStringParameter("courseId", SharedPreferencesUtil.getInstance().spGetString("share_courseId"));
			params.addQueryStringParameter("shareUid", SharedPreferencesUtil.getInstance().spGetString("share_shareUid"));
			params.addQueryStringParameter("system", "Android");


			System.out.println("**************"+params.getQueryStringParams().toString());

			HttpUtils httpUtils= new HttpUtils();
			httpUtils.send(HttpMethod.GET, ConstantsOnline.LOGIN_URL,params, new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {
					LoginBean bean = GsonUtil.jsonToBean(responseInfo.result, LoginBean.class);
					if (bean!=null) {
						if ("true".equals(bean.success)) {
							SharedPreferencesUtil.getInstance().editPutString("name", bean.entity.name);
							SharedPreferencesUtil.getInstance().editPutString("avatar", bean.entity.avatar);
							SharedPreferencesUtil.getInstance().editPutString("mobile", bean.entity.mobile);
							SharedPreferencesUtil.getInstance().editPutString("userId", bean.entity.id);
							SharedPreferencesUtil.getInstance().editPutString("userType", bean.entity.userType);
							SharedPreferencesUtil.getInstance().editPutString("userInfo", bean.entity.userInfo);// 个性签名
							SharedPreferencesUtil.getInstance().editPutString("loginsid", bean.entity.loginsid);// 用户识别码
							ShowUtils.showMsg(getApplicationContext(), "自动登录成功！");
							SharedPreferencesUtil.getInstance().editPutString("share_courseId", null);
							SharedPreferencesUtil.getInstance().editPutString("share_shareUid", null);

							//用于视频播放节点的记录
							if (bean.entity.id.equals(PreferencesObjectUtil.spGetString("oldUserId", context))){
								if (!PreferencesObjectUtil.containsObject("videoPlayNode",context))
									PreferencesObjectUtil.saveObject(new HashMap<String,Integer>(),"videoPlayNode",context);
							}else {
								PreferencesObjectUtil.editPutString("oldUserId",bean.entity.id,context);
								PreferencesObjectUtil.saveObject(new HashMap<String,Integer>(),"videoPlayNode",context);
							}
							
						}else {
							SharedPreferencesUtil.getInstance().editClear();
						}
					}else {
						ShowUtils.showMsg(getApplicationContext(), "获取数据失败，请尝试手动登录");
					}
					
					handler.obtainMessage(LOAD_MAINUI).sendToTarget();
				}

				@Override
				public void onFailure(HttpException error, String msg) {
					ShowUtils.showMsg(getApplicationContext(), "自动登录失败，请检查网络");
					handler.obtainMessage(LOAD_MAINUI).sendToTarget();
					SharedPreferencesUtil.getInstance().editClear();
				}
			});
		
	}
	
	/**
	 * 开启获取token的服务
	 */
	private void getToken(){
		Intent intent =new Intent(context, GetTokenService.class);
		intent.putExtra("userId", UserStateUtil.getUserId());
		context.startService(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
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
//	                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
//	                    finish();
	    				loadMainUI();
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
	    }
}
