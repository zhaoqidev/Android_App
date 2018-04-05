package cc.upedu.online.utils;

import android.content.Context;
import android.content.Intent;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import cc.upedu.online.activity.LoginActivity;
import cc.upedu.online.domin.LoginInFailuerBean;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;

/**
 * 判断用户状态的工具类 状态有: 是否登录 ,指定课程是否购买
 * 
 * @author Administrator
 * 
 */
public class UserStateUtil {
	static boolean isLoginInFailuer;

	/**
	 * 判断直播的课程是否正在直播
	 * 
	 * @param courseId
	 *            直播课程的id
	 * @return true 正在进行中 false 直播未开始或直播已结束
	 */
	public static boolean isPlaying(String beginTime) {
		long time = StringUtil.getLongDate(beginTime, "yyyy-MM-dd HH:mm:ss");
		long currentTimeMillis = System.currentTimeMillis();
		if (time - currentTimeMillis > 0) {
			return false;
		}else {
			return true;
		}
	}

	/**
	 * 获取用户是否已经登录
	 * 
	 * @return true已经是登陆状态 false未登录状态
	 */
	public static boolean isLogined() {

		return SharedPreferencesUtil.getInstance().spGetBoolean("isLogin");

	}

	/**
	 * 获取用户ID
	 * 
	 * @return 用户ID
	 */
	public static String getUserId() {
		return SharedPreferencesUtil.getInstance().spGetString("userId","0");
	}

	/**
	 * 获取用户Type
	 * 
	 * @return 0:普通用户 1:导师 2:代理商
	 */
	public static String getUserType() {
		return SharedPreferencesUtil.getInstance().spGetString("userType","-1");
	}
	
	/**
	 * 获取用户手机号码
	 * 
	 * @return 用户手机号码
	 */
	public static String getUserMobile() {
		return SharedPreferencesUtil.getInstance().spGetString("mobile","0");

	}

	/* ........................................................................ */
	/**
	 * 判断用户登录是否失效。 true,当前登陆有效；false，当前登陆失效
	 */
	public static void loginInFailuer(final Context context,
			final FailureCallBack mFailureCallBack,
			final SuccessCallBack mSuccessCallBack) {
		String userId = getUserId();
		String passWord = SharedPreferencesUtil.getInstance().spGetString("passWord");
		String loginsid = SharedPreferencesUtil.getInstance().spGetString("loginsid");
		if (userId.isEmpty() && passWord.isEmpty() && loginsid.isEmpty()) {

			ShowUtils.showDiaLog(context, "数据异常，请重新登录", "",
					new ConfirmBackCall() {

						@Override
						public void confirmOperation() {
							Intent intent = new Intent(context,
									LoginActivity.class);
							context.startActivity(intent);

						}
					});

		} else {
			RequestParams params = new RequestParams();
			params.addQueryStringParameter("userId", userId);
			params.addQueryStringParameter("password", passWord);
			params.addQueryStringParameter("loginsid", loginsid);

			RequestCallBack<String> callBack = new RequestCallBack<String>() {

				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {// 请求成功
					ProcessedData(responseInfo.result, context,
							mFailureCallBack, mSuccessCallBack);
				}

				@Override
				public void onFailure(HttpException error, String msg) {// 请求失败
					ShowUtils.showMsg(context, "请求网络失败，请稍后重试");
				}
			};

			// 调用方法，发送请求
			getData(HttpMethod.GET, ConstantsOnline.LOGIN_IN_FAILURE, params,
					callBack);

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
	 * 解析数据
	 * 
	 * @param result
	 */
	private static void ProcessedData(String result, final Context context,
			FailureCallBack mFailureCallBack, SuccessCallBack mSuccessCallBack) {
		LoginInFailuerBean bean = GsonUtil.jsonToBean(result,LoginInFailuerBean.class);
		ConfirmBackCall mConfirmBackCall = new ConfirmBackCall() {

			@Override
			public void confirmOperation() {

				SharedPreferencesUtil.getInstance().editClear();
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
			}
		};

		if (bean !=null){
			if ("false".equals(bean.success)) {
				if (mFailureCallBack != null) {
					mFailureCallBack.onFailureCallBack();
					ShowUtils.showDiaLog(context, bean.message, null,
							mConfirmBackCall);
				}
			} else {
				if (mSuccessCallBack != null) {
					mSuccessCallBack.onSuccessCallBack();// 登陆未过期执行的操作
				}
			}
		}
	}

	/**
	 * 用户登录已过期的回调
	 * 
	 * @author Administrator
	 * 
	 */
	public static abstract class FailureCallBack {
		public abstract void onFailureCallBack();
	}

	/**
	 * 用户登录未过期的回调
	 * 
	 * @author Administrator
	 * 
	 */
	public static abstract class SuccessCallBack {
		public abstract void onSuccessCallBack();
	}

	/* ........................................................................ */

	/**
	 * 用户未登录时弹出提示框
	 * 
	 * @param context
	 */
	public static void NotLoginDialog(final Context context) {

		ShowUtils.showDiaLog(context, "您还未登陆", "是否前往登陆页面",
				new ConfirmBackCall() {

					@Override
					public void confirmOperation() {
						Intent intent = new Intent(context, LoginActivity.class);
						context.startActivity(intent);

					}
				});

	}

	/**
	 * 数据错误时候执行的方法，跳转到登陆界面
	 * 
	 * @param context
	 * @param strTitle
	 * @param strContent
	 */
	public static void ErrorDialog(final Context context, String strTitle,
			String strContent) {

		ShowUtils.showDiaLog(context, strTitle, strContent,
				new ConfirmBackCall() {

					@Override
					public void confirmOperation() {
						Intent intent = new Intent(context, LoginActivity.class);
						context.startActivity(intent);

					}
				});

	}
}
