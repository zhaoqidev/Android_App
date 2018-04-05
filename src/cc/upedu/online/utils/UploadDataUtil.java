package cc.upedu.online.utils;

import android.app.Dialog;
import android.content.Context;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.Map;
import java.util.Set;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.domin.UploadDataMessageBean;
import cc.upedu.online.interfaces.UploadDataCallBack;

/**
 * 上传文本数据功能的工具类
 * 可用于收藏,保存收货地址,删除收藏,选取默认地址,保存笔记提问等等
 * @author Administrator
 *
 */
public class UploadDataUtil {
	/**
	 * 上传文本数据功能的工具类的实例
	 */
	private static UploadDataUtil mUploadDataUtile;
	private static Dialog loadingDialog;
	/**
	 * 获取上传文本数据功能的工具类的实例
	 * @return
	 */
	public static UploadDataUtil getInstance(){
		if (mUploadDataUtile == null) {
			mUploadDataUtile = new UploadDataUtil();
		}
		return mUploadDataUtile;
	}
	/**
	 * 发送http请求网络数据
	 */
	public void onUploadDataData(Context context,String url,Map<String,String> map,final UploadDataCallBack mUploadDataCallBack) {
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
		RequestParams params = new RequestParams();
		Set<String> set = map.keySet();
		for (String key : set) {
			params.addQueryStringParameter(key, map.get(key));
		}
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, url, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						UploadDataMessageBean data = ProcessedData(responseInfo.result);
						if (data != null) {
							if (Boolean.valueOf(data.success)) {
								if (mUploadDataCallBack != null) {
									mUploadDataCallBack.onUploadDataSuccess();
								}
							}else {
								ShowUtils.showMsg(OnlineApp.myApp.context, data.getMessage());
								if (mUploadDataCallBack != null) {
									mUploadDataCallBack.onUploadDataFailure();
								}
							}
						}else {
							ShowUtils.showMsg(OnlineApp.myApp.context, "请求失败,请检查您的网络连接是否正常!");
							if (mUploadDataCallBack != null) {
								mUploadDataCallBack.onUploadDataFailure();
							}
						}
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ShowUtils.showMsg(OnlineApp.myApp.context, "请求网络失败，请稍后重试");
					}
				});
	}
	/**
	 * 利用xutils进行http请求数据
	 * @param httpMethod 请求类型
	 * @param url	请求地址
	 * @param params 参数
	 * @param callBack 请求回调
	 */
	public void getData(HttpMethod httpMethod, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		HttpUtils httpUtils= new HttpUtils();
		httpUtils.send(httpMethod, url,params, callBack);
	}
	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	private UploadDataMessageBean ProcessedData(String result) {
		return GsonUtil.jsonToBean(result, UploadDataMessageBean.class);
	}
}
