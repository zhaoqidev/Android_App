package cc.upedu.online.utils;

import com.google.gson.Gson;

public class GsonUtil {
	public static<T> T jsonToBean(String json,Class<T> clazz){
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, clazz);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			System.out.println("Json解析异常");
			return null;
		}
	}
}
