package cc.upedu.online.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.Uri;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import cc.upedu.online.net.RequestVo;


/**
 * 网络请求工具类
 * @author zhangp
 * 
 */
public class NetUtil {
	private static int mConTimeout = 20000;
	private static int maxSize = 1048576;
	/** 网络类型 0表示其他网络 1表示移动联通 2代表电信 */
	public static int NetType = 0;
	/** 中国移动联通wap代理网关 */
	public static final String proxyMobile = "10.0.0.172";
	/** 中国电信wap代理网关 */
	public static final String proxyTel = "10.0.0.200";
	public static final String CTWAP = "ctwap";
	public static final String CMWAP = "cmwap";
	public static final String WAP_3G = "3gwap";
	public static final String UNIWAP = "uniwap";
	public static final int TYPE_CM_CU_WAP = 4;// 移动联通wap10.0.0.172
	public static final int TYPE_CT_WAP = 5;// 电信wap 10.0.0.200
	public static final int TYPE_OTHER_NET = 6;// 电信,移动,联通,wifi 等net网络
	private static final String TAG = "NetUtil";
	public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

	private static HttpClient getHttpClient(Context context) {
		int port = Proxy.getDefaultPort();
		BasicHttpParams basicHttpParams = new BasicHttpParams();
		HttpConnectionParams.setStaleCheckingEnabled(basicHttpParams, false);
//		int status = checkNetworkType(context);
		int status = TYPE_OTHER_NET;
		switch (status) {
		case TYPE_CM_CU_WAP:// 移动联通wap10.0.0.172
			LogUtils.v(TAG, "移动联通wap代理模式");
			NetType = TYPE_CM_CU_WAP;
			HttpHost httpHost = new HttpHost(proxyMobile, port);
			basicHttpParams.setParameter(ConnRouteParams.DEFAULT_PROXY,
					httpHost);
			HttpConnectionParams.setConnectionTimeout(basicHttpParams,
					mConTimeout);
			HttpConnectionParams.setSoTimeout(basicHttpParams, mConTimeout);
			HttpConnectionParams.setSocketBufferSize(basicHttpParams, maxSize);
			return new DefaultHttpClient(basicHttpParams);
		case TYPE_OTHER_NET:// 电信,移动,联通,wifi 等net网络
			LogUtils.v(TAG, "wifi 等net网络无代理");
			NetType = TYPE_OTHER_NET;
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, mConTimeout);
			HttpConnectionParams.setSoTimeout(httpParams, mConTimeout);
			HttpConnectionParams.setSocketBufferSize(httpParams, maxSize);
			return new DefaultHttpClient(httpParams);
		case TYPE_CT_WAP:// 电信wap 10.0.0.200
			LogUtils.v(TAG, "电信wap代理模式");
			NetType = TYPE_CT_WAP;
			HttpHost host = new HttpHost(proxyTel, port);
			basicHttpParams.setParameter(ConnRouteParams.DEFAULT_PROXY, host);
			HttpConnectionParams.setConnectionTimeout(basicHttpParams,
					mConTimeout);
			HttpConnectionParams.setSoTimeout(basicHttpParams, mConTimeout);
			HttpConnectionParams.setSocketBufferSize(basicHttpParams, maxSize);
			return new DefaultHttpClient(basicHttpParams);
		}
		return new DefaultHttpClient(basicHttpParams);
	}

	public static HttpParams setHttpParams(Context context,
			int connectionTimeout) throws Exception {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, mConTimeout);
		HttpConnectionParams.setSoTimeout(httpParams,
				connectionTimeout == 0 ? mConTimeout : connectionTimeout);
		HttpConnectionParams.setSocketBufferSize(httpParams, maxSize);
		return httpParams;
	}

	/**
	 * wifi是否连接
	 * 
	 * @param context
	 * @return wifi是否连接
	 */
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 是否能上网
	 * 
	 * @param context
	 * @return  是否能上网
	 */
	public static boolean hasConnectedNetwork(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * get方法
	 * @param vo
	 *            请求工具类
	 * @return 返回对象
	 * @throws Exception
	 */
	public static Object get(RequestVo vo) throws Exception {
		CodeResult response = null;
		String encodedParams = "";
		if (vo.requestDataMap!=null&&!vo.requestDataMap.isEmpty()) {
			encodedParams = encodeParameters(vo.requestDataMap);
		}
		if (encodedParams.length() > 0) {
			if (-1 == vo.requestUrl.indexOf("?"))
				vo.requestUrl = vo.requestUrl + "?" + encodedParams;
			else {
//				vo.requestUrl = vo.requestUrl + "&" + encodedParams;
			}
		}
		LogUtils.d("NetUtil:get:" + "url==" + vo.requestUrl);
		// 判断本地是否有数据,有的话取本地数据
		String md5Url = AppInfoUtil.md5(vo.requestUrl);
		String path = new File(vo.context.getCacheDir(), URLEncoder.encode(md5Url)
				+ ".json").getAbsolutePath();
		if (vo.isSaveLocal) {
			File file = new File(path);
			if (vo.isSaveLocal && file.exists()) {
				// 是否超时
				long savetime = System.currentTimeMillis()
						- file.lastModified();
				LogUtils.d("本地缓存时间:=" + savetime);
				if (savetime <= 300000L) {
					LogUtils.d("取本地缓存");
					BufferedReader reader = new BufferedReader(new FileReader(
							file));
					String s = "";
					StringBuffer sb = new StringBuffer();
					while ((s = reader.readLine()) != null) {
						sb.append(s);
					}
					return vo.jsonParser.parseJSON(sb.toString());
				} else {
					file.delete();// 超时删除本地数据
				}
			}
		}
		HttpGet method = new HttpGet(vo.requestUrl);
		response = httpRequest(vo.context, method, path, vo.isSaveLocal);
		if (response.code == 5000) {
			return new NetObject();
		}else {
			if (response.result != null) {
				return vo.jsonParser.parseJSON(response.result);
			}
		}
		return vo.jsonParser.parseJSON(null);
	}

	/**
	 * 方法描述：post方式提交请求
	 */
	public static Object post(RequestVo vo) throws Exception {

		HttpPost method = new HttpPost(vo.requestUrl);
		LogUtils.d("NetUtil:post:" + "url==" + vo.requestUrl);
		List<BasicNameValuePair> keyParams = new ArrayList<BasicNameValuePair>();
		if(vo.requestDataMap!=null){
			Set<String> set = vo.requestDataMap.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) vo.requestDataMap.get(key);
				if ((value != null) && (!"".equals(value))) {
					keyParams.add(new BasicNameValuePair(key, value));
				}
			}
		}
		// 判断本地是否有数据,有的话取本地数据
		String md5Url = AppInfoUtil.md5(vo.requestUrl);
		String path = new File(vo.context.getCacheDir(), URLEncoder.encode(md5Url)
				+ ".json").getAbsolutePath();
		if (vo.isSaveLocal) {
			File file = new File(path);
			if (vo.isSaveLocal && file.exists()) {
				// 是否超时
				long savetime = System.currentTimeMillis()
						- file.lastModified();
				LogUtils.d("本地缓存时间:=" + savetime);
				if (savetime <= 300000L) {
					LogUtils.d("取本地缓存");
					BufferedReader reader = new BufferedReader(new FileReader(
							file));
					String s = "";
					StringBuffer sb = new StringBuffer();
					while ((s = reader.readLine()) != null) {
						sb.append(s);
					}
					return vo.jsonParser.parseJSON(sb.toString());
				} else {
					file.delete();// 超时删除本地数据
				}
			}
		}

		try {
			method.setEntity(new UrlEncodedFormEntity(keyParams, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
		}
		CodeResult response = httpRequest(vo.context, method, path, true);
		if (response.code == 5000) {
			return new NetObject();
		}else {
			if (response.result != null) {
				return vo.jsonParser.parseJSON(response.result);
			} else {
				return vo.jsonParser.parseJSON(null);
			}
		}
	}

	/**
	 * 
	 * @param method
	 * @param path
	 *            本地缓存路径
	 * @param isSaveLocal
	 *            是否要本地缓存
	 * @return 返回的结果字符串
	 * @throws Exception
	 */
	private static CodeResult httpRequest(Context context, HttpRequestBase method,
			String path, boolean isSaveLocal) throws Exception {
		int code = -1;
		CodeResult result = new CodeResult();
		String Uri = method.getRequestLine().getUri();
		try {
//			method.setHeader("ts", StringUtil.getStringDate());
//			method.setHeader("userid", OnlineApp.myApp.sp.getString("userId", "-1"));
			HttpResponse httpResponse = getHttpClient(context).execute(method);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			LogUtils.d("NetUtil:httpRequest:statusCode=" + statusCode + "-----url----"+Uri);
			Header[] headers = httpResponse.getHeaders("timeout");
			if (headers != null && headers.length > 0) {
				String value = headers[0].getValue();
				if (!StringUtil.isEmpty(value) && "y".equals(value)) {
					statusCode = 5000;//自定义的状态码,表示登录已经失效
				}
			}
			result.code = statusCode;
			if (200 == statusCode) {
				result.result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				LogUtils.d("-----url----"+Uri + "NetUtil:httpRequest:result=" + result.result);
				if (result == null||result.result.equals("")) {
					return result;
				}
				JSONObject resBody = AppInfoUtil.stringToJSONObject(result.result);
				if (resBody == null) {
					return result;
				}
				code = resBody.optInt("code");
				if (code == 0) {
					// 如果需要本地缓存则写入本地
					if (isSaveLocal && path != null) {
						wirteJsonToLocal(path, result.result);
					}
				}

			}

			return result;
		} catch (Exception ioe) {
			ioe.printStackTrace();
			LogUtils.d("NetUtil:Response" + "htjx httpRequest exception:"
					+ ioe.getMessage());
			return result;
		} finally {
			method.abort();
		}

	}
	public static class CodeResult{
		public int code;
		public String result;
	}
	public static class NetObject{
		
	}
	/**
	 * 获取网络类型
	 * @param mContext
	 * @return 网络类型
	 */
	public static int checkNetworkType(Context mContext) {
		try {
			final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo mobNetInfoActivity = connectivityManager
					.getActiveNetworkInfo();
			if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {

				// 注意一：
				// NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
				// 但是有些电信机器，仍可以正常联网，
				// 所以当成net网络处理依然尝试连接网络。
				// （然后在socket中捕捉异常，进行二次判断与用户提示）。
				Log.i("", "=====================>无网络");
				return TYPE_OTHER_NET;
			} else {
				// NetworkInfo不为null开始判断是网络类型
				int netType = mobNetInfoActivity.getType();
				if (netType == ConnectivityManager.TYPE_WIFI) {
					// wifi net处理
					Log.i("", "=====================>wifi网络");
					return TYPE_OTHER_NET;
				} else if (netType == ConnectivityManager.TYPE_MOBILE) {

					// 注意二：
					// 判断是否电信wap:
					// 不要通过getExtraInfo获取接入点名称来判断类型，
					// 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
					// 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
					// 所以可以通过这个进行判断！

					final Cursor c = mContext.getContentResolver().query(
							PREFERRED_APN_URI, null, null, null, null);
					if (c != null) {
						c.moveToFirst();
						final String user = c.getString(c
								.getColumnIndex("user"));
						if (!StringUtil.isEmpty(user)) {
							Log.i("",
									"=====================>代理："
											+ c.getString(c
													.getColumnIndex("proxy")));
							if (user.startsWith(CTWAP)) {
								Log.i("", "=====================>电信wap网络");
								return TYPE_CT_WAP;
							}
						}
					}
					c.close();

					// 注意三：
					// 判断是移动联通wap:
					// 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
					// 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
					// 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
					// 所以采用getExtraInfo获取接入点名字进行判断

					String netMode = mobNetInfoActivity.getExtraInfo();
					Log.i("", "netMode ================== " + netMode);
					if (netMode != null) {
						// 通过apn名称判断是否是联通和移动wap
						netMode = netMode.toLowerCase(Locale.CHINESE);
						if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
								|| netMode.equals(UNIWAP)) {
							Log.i("", "=====================>移动联通wap网络");
							return TYPE_CM_CU_WAP;
						}

					}

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return TYPE_OTHER_NET;
		}

		return TYPE_OTHER_NET;

	}


	/**
	 * 写入本地字符串进文件
	 * @param path 路径
	 * @param content 内容
	 * @throws IOException
	 */
	private static void wirteJsonToLocal(String path, String content)
			throws IOException {
		File file = new File(path);
		if (file.exists()) {
			file.delete();
		}
		FileOutputStream out = null;
		try {
			LogUtils.d("path=" + path);
			out = new FileOutputStream(file);
			byte[] bytes = content.getBytes("UTF-8");
			out.write(bytes);
		} catch (Exception e) {
			LogUtils.d(e.toString());
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 方法描述：对请求参数进行编码(get方式)
	 */
	private static String encodeParameters(Map<String, String> map) {
		StringBuffer buf = new StringBuffer();
		Set<String> set = map.keySet();
		Iterator<String> iterator = set.iterator();
		int i = 0;
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			String value = (String) map.get(key);

			if ((key == null) || ("".equals(key)) || (value == null)
					|| ("".equals(value))) {
				continue;
			}
			if (i != 0)
				buf.append("&");
			try {
				buf.append(URLEncoder.encode(key, "UTF-8")).append("=")
						.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			i++;
		}
		return buf.toString();
	}
}