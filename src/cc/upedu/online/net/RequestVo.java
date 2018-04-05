package cc.upedu.online.net;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
/**
 * 请求工具类
 * @author zhangp
 *
 */
public class RequestVo {
	public boolean isSaveLocal=false;//是否要本地缓存
	public String requestUrl=null;//默认地址头 sp.getString("requestUrl", "");
	public Context context;
	private boolean isShowDialog=false;//是否弹出进度框
	private String type="get";//以什么方式提交 get或post 默认是get
	public Map<String,String> requestDataMap;//请求工具
	public BaseParser<?> jsonParser;
	
	public RequestVo() {
		super();
	}
	/**
	 * 以什么方式提交 get或post 默认是get
	 */
	public String getType() {
		return type;
	}
	/**
	 * 以什么方式提交 get或post 默认是post
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 是否要弹Dialog
	 */
	public boolean isShowDialog() {
		return isShowDialog;
	}
	/**
	 * 是否要弹Dialog 默认是true,如果不弹,则改成false;
	 */
	public void setShowDialog(boolean isShowDialog) {
		this.isShowDialog = isShowDialog;
	}
/**
 * 
 * @param context 上下文
 * @param requestDataMap 请求参数
 * @param jsonParser 解析工具类
 */
	public RequestVo(Context context,Map<String, String> requestDataMap, BaseParser<?> jsonParser) {
		super();
		this.context = context;
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		requestUrl = sp.getString("requestUrl", "");
		this.requestDataMap = requestDataMap;
		this.jsonParser = jsonParser;
	}
	/**
	 * 
	 * @param requestUrl 请求根url
	 * @param context 上下文
	 * @param requestDataMap 请求参数
	 * @param jsonParser 解析工具类
	 */
	public RequestVo(String requestUrl, Context context,
		Map<String, String> requestDataMap,BaseParser<?> jsonParser) {
	super();
	this.requestUrl = requestUrl;
	this.context = context;
	this.requestDataMap = requestDataMap;
	this.jsonParser = jsonParser;
}
	public void setUrl(String url){
		this.requestUrl=url;
	}
}
