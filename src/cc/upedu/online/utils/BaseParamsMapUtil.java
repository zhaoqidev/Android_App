package cc.upedu.online.utils;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
/**
 * 请求参数工具类
 * @author zhangp
 *
 */
public class BaseParamsMapUtil {
	/**
	 * 通用标准参数
	 * @return 返回请求键值对Map
	 */
	public static Map<String, String> getParamsMap(Context context) {
		Map<String, String> paramsMap = new HashMap<String, String>();

		return paramsMap;
	}
	/**
	 * 通用int参数
	 * @return 返回请求键值对Map
	 */
	public static Map<String, Integer> getIntParamsMap(Context context) {
		Map<String, Integer> paramsMap = new HashMap<String, Integer>();
		return paramsMap;
	}
	public static Map<String, String> submitCheatCid(Context context,
			String string, String string2) {
		// TODO Auto-generated method stub
		return  new HashMap<String, String>();
	}
}
