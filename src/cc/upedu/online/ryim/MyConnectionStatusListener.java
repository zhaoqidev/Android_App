package cc.upedu.online.ryim;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cc.upedu.online.service.GetTokenService;
import cc.upedu.online.utils.UserStateUtil;
import io.rong.imlib.RongIMClient;
/**
 * 获取连接状态以及相应的操作
 * @author Administrator
 *
 */
public class MyConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

	Context context;
	public MyConnectionStatusListener(Context context){
		this.context=context;
	}
	@SuppressWarnings("incomplete-switch")
	@Override
	  public void onChanged(ConnectionStatus connectionStatus) {

	      switch (connectionStatus){

	          case CONNECTED://连接成功。
	        	  Log.i("conne", "成功");
	              break;
	          case DISCONNECTED://断开连接。
	        	  Log.i("conne", "断开");
	        	  getToken();
	              break;
	          case CONNECTING://连接中。
	        	  Log.i("conne", "连接中");

	              break;
	          case NETWORK_UNAVAILABLE://网络不可用。

	              break;
	          case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线

	              break;
	      }
	  }
	
	/**
	 * 开启获取token的服务
	 */
	private void getToken(){
		Intent intent =new Intent(context, GetTokenService.class);
		intent.putExtra("userId", UserStateUtil.getUserId());
		context.startService(intent);
	}
	}
