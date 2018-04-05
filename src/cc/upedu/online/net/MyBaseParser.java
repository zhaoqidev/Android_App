package cc.upedu.online.net;

import com.google.gson.Gson;


/**
 * 解析工具抽象类
 * @author zhangp
 * @param <T>
 *
 * @param <T> 返回的对象类型
 */
public class MyBaseParser<T> extends BaseParser<T> {
	private Class clazz;

	public MyBaseParser(Class clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public T parseJSON(String str) {
		Gson gson = new Gson();
		return (T) gson.fromJson(str, clazz);
	}
	
}
