package cc.upedu.online.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import cc.upedu.online.OnlineApp;

/**
 * 名为config的SharedPreferences的存取方法工具类
 */
public class SharedPreferencesUtil {
	private static SharedPreferencesUtil mSharedPreferencesUtil;
	private static SharedPreferences sp;
	private static Editor edit;
	public static SharedPreferencesUtil getInstance(){
		if (mSharedPreferencesUtil == null) {
			mSharedPreferencesUtil = new SharedPreferencesUtil();
		}
		if (sp == null || edit == null) {
			sp = OnlineApp.myApp.context.getSharedPreferences("config", Context.MODE_PRIVATE);
			edit = sp.edit();
		}
		return mSharedPreferencesUtil;
	}
	/**
	 * 给sp存String型数据
	 */
	public void editPutString(String key, String value) {
		edit.putString(key, value);
		edit.commit();
	}

	/**
	 * 给sp存boolean型数据
	 */
	public void editPutBoolean(String key, Boolean value) {
		edit.putBoolean(key, value);
		edit.commit();
	}
	
	/**
	 * 根据key获取sp中的字符串,默认字符串为""
	 * @param key
	 * @return
	 */
	public String spGetString(String key){
		return sp.getString(key, "");
	}
	/**
	 * 根据key获取sp中的字符串,自定义默认字符串
	 * @param key
	 * @param value
	 * @return
	 */
	public String spGetString(String key, String value){
		return sp.getString(key, value);
	}
	/**
	 * 根据key获取sp中的布尔值,默认值为false
	 * @param key
	 * @return
	 */
	public Boolean spGetBoolean(String key){
		return sp.getBoolean(key, false);
	}
	/**
	 * 根据key获取sp中的布尔值,自定义默认值
	 * @param key
	 * @param value
	 * @return
	 */
	public Boolean spGetBoolean(String key, Boolean value){
		return sp.getBoolean(key, value);
	}
	/**
	 * 清除sp中内容的方法
	 */
	public void editClear(){
		edit.clear();
		edit.commit();
	}
}
