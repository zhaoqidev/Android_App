package cc.upedu.online.ryim;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import cc.upedu.online.utils.SharedPreferencesUtil;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import android.os.Vibrator;

/**
 * 获取连接状态以及相应的操作
 * @author Administrator
 *
 */
public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

	Context context;

	public MyReceiveMessageListener(Context context){
		this.context=context;
	}
	@Override
	public boolean onReceived(Message message, int i) {

		SharedPreferencesUtil.getInstance().editPutBoolean("newMsg", true);

		Intent intent = new Intent();
		intent.setAction("newMsg");
		intent.putExtra("newMsg", true);

		context.sendBroadcast(intent);//发送 一个无序广播

		/*
		* 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
		*/
		Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启
		vibrator.vibrate(pattern, -1);           //重复两次上面的pattern 如果只想震动一次，index设为-1

		RingtoneManager manager = new RingtoneManager(context);
		manager.setType(RingtoneManager.TYPE_NOTIFICATION);
		Uri defalutUri = manager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		// 通过URI获得系统默认的Ringtone发出声音
		Ringtone defalutRingtone = manager.getRingtone(context, defalutUri);
		defalutRingtone.play();

		return true;
	}

}
