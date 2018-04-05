package cc.upedu.online.utils;

import android.content.Context;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.util.IOUtils;

import org.apache.http.HttpEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import cc.upedu.online.domin.UserInfoBean;
import cc.upedu.online.domin.UserInfoBean.UserInfo;

/**
 * 用于获取对话好友的信息，头像，姓名
 * 
 * @author Administrator
 * 
 */
public class GetUserInforUtil {
	static Context context;
//	static String userId;
	static UserInfoBean bean;

	public static UserInfo getUserInfo(Context con, String id) {
		context = con;
//		userId = id;
		if (PreferencesObjectUtil.readObject(id, context) != null) {
			Log.i("token", id + "已存在");
			return (UserInfo) PreferencesObjectUtil.readObject(id, context);
		} else {
			getUserData(id);
			return getUserInfoFromMap(id);
		}
	}

	public static UserInfo getUserInfoInList(Context con, String id) {
		context = con;
//		userId = id;
		if (PreferencesObjectUtil.readObject(id, context) != null) {
			Log.i("token", id + "已存在");
			return (UserInfo) PreferencesObjectUtil.readObject(id, context);
		} else {
			return getListData(id);
		}
	}
	private static UserInfo getUserInfoFromMap(String id){
		UserInfo userInfo = mUserInfoMap.get(id);
		mUserInfoMap.remove(id);
		return userInfo;
	}
	private static HashMap<String,UserInfo> mUserInfoMap = new HashMap<>();
	/**
	 * 发送http请求网络数据
	 */
	private static void getUserData(final String id) {

		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", id);

		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.GET_USER_INFO, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						// 请求成功解析数据
						bean = GsonUtil.jsonToBean(responseInfo.result,
								UserInfoBean.class);
						if (bean != null&&!bean.success.equals("false")) {
							Log.i("token", id + "请求成功并保存");
							PreferencesObjectUtil.saveObject(bean.entity,id, context);
							mUserInfoMap.put(id, bean.entity);
						} else {
							ShowUtils.showMsg(context, "获取对话好友信息失败，请稍后重试");
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						ShowUtils.showMsg(context, "请求网络失败，请稍后重试");
					}
				});
	}
	/**
	 * 发送http请求网络数据
	 */
	private static UserInfo getListData(String id) {
		UserInfoBean bean ;
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", id);

		// 调用父类方法，发送请求
		bean = GsonUtil.jsonToBean(getSyncData(HttpMethod.GET, ConstantsOnline.GET_USER_INFO, params),
				UserInfoBean.class);
				
		if (bean != null) {
			return bean.entity;
		} else {
			return null;
		}

	}
	/**
	 * 利用xutils进行http请求数据
	 * 
	 * @param httpMethod
	 *            请求类型
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @param callBack
	 *            请求回调
	 */
	public static void getData(HttpMethod httpMethod, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(httpMethod, url, params, callBack);
	}

	/**
	 * 利用xutils进行http请求数据
	 * 
	 * @param httpMethod
	 *            请求类型
	 * @param url
	 *            请求地址
	 * @param params
	 *            参数
	 * @param callBack
	 *            请求回调
	 * @return 
	 */
	public static String getSyncData(HttpMethod httpMethod, String url,
			RequestParams params) {
		HttpUtils httpUtils = new HttpUtils();
		try {
			ResponseStream mResponseStream = httpUtils.sendSync(httpMethod,
					url, params);
			if (mResponseStream.getStatusCode() == 200) {

				HttpEntity entity = mResponseStream.getBaseResponse()
						.getEntity();
				if (entity != null) {

					return  handleEntity(entity, "UTF-8");
				}
			}

		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String handleEntity(HttpEntity entity, String charset)
			throws IOException {
		if (entity == null)
			return null;
		InputStream inputStream = null;
		StringBuilder sb = new StringBuilder();
		try {
			inputStream = entity.getContent();

		} finally {
			IOUtils.closeQuietly(inputStream);
		}
		return sb.toString().trim();
	}
}
