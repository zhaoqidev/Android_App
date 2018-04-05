package cc.upedu.online.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 自定义Log工具类
 * 
 * @author fada
 * 
 */
public class LogUtils {
	private static final int TOAST = 0;
	private static final int ERROR = 1;
	private static final int WARN = 2;
	private static final int INFO = 3;
	private static final int DEBUG = 4;
	private static final int VERBOSE = 5;
	private static final int DEFEAT = 6;
	private static final int STRICTMODE = 7;
	public static String TAG = "htjx_SDK:";
	/**
	 * 注:如果不要打印某一类log,那么只要把LOG_LEVEL值调到与其相等就可以了.当LOG_LEVEL为1时log全部不打印 1时只打印toast
	 */
	public static int LOG_LEVEL = 6;

	/**
	 * 默认Log,级别最低
	 */
	public static void a() {
		if (LOG_LEVEL > DEFEAT) {

			Log.d(TAG, "--------log打印了---------");
		}
	}

	/**
	 * 级别为5,总共为6个等级,越大表示越不重要,越容易关闭.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void v(String tag, String msg) {
		if (LOG_LEVEL > VERBOSE) {
			if (tag == null)
				tag = TAG;
			Log.v(tag, msg);
		}
	}

	/**
	 * 级别为7,总共为6个等级,越大表示越不重要,越容易关闭.
	 * 
	 * @param msg
	 */
	public static void d(String msg) {
		if (LOG_LEVEL > VERBOSE) {
			Log.v(TAG, msg);
		}
	}

	/**
	 * 级别为4,总共为6个等级,越大表示越不重要,越容易关闭.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void d(String tag, String msg) {
		if (LOG_LEVEL > DEBUG) {
			if (tag == null)
				tag = TAG;
			Log.d(tag, msg);
		}
	}

	/**
	 * 级别为3,总共为6个等级,越大表示越不重要,越容易关闭.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag, String msg) {
		if (LOG_LEVEL > INFO) {
			if (tag == null)
				tag = TAG;
			Log.i(tag, msg);
		}
	}

	/**
	 * 级别为2,总共为6个等级,越大表示越不重要,越容易关闭.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void w(String tag, String msg) {
		if (LOG_LEVEL > WARN) {
			if (tag == null)
				tag = TAG;
			Log.w(tag, msg);
		}
	}

	/**
	 * 级别为1,总共为6个等级,越大表示越不重要,越容易关闭.
	 * 
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag, String msg) {
		if (LOG_LEVEL > ERROR) {
			if (tag == null)
				tag = TAG;
			Log.e(tag, msg);
		}
	}

	/**
	 * 吐丝
	 * 
	 * @param context
	 * @param msg
	 *            吐丝信息
	 */
	public static void toast(Context context, String msg) {
		if (LOG_LEVEL > TOAST) {
			if (msg == null)
				msg = "我是Toast";
			Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		}
	}
	
//	public static void strictMode() {
//		if (LOG_LEVEL > STRICTMODE) {
//			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//			.detectDiskReads().detectDiskWrites().detectNetwork() // 这里可以替换为detectAll()
//			// 就包括了磁盘读写和网络I/O
//			.penaltyLog() // 打印logcat，当然也可以定位到dropbox，通过文件保存相应的log
//			.build());
//			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//			.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
//			.penaltyLog() // 打印logcat
//			.penaltyDeath().build());
//		}
//	}
}