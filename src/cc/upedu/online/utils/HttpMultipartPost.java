package cc.upedu.online.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

import cc.upedu.online.utils.CustomMultipartEntity.ProgressListener;

/**
 * Multipart上传,用于图片上传
 * @author Jason
 *
 */
public class HttpMultipartPost extends AsyncTask<String, Integer, String> {

	private Context context;
	private List<String> filePathList;
	private UploadCallBack mUploadCallBack;
	private Dialog pd;
	private long totalSize;
	private String responseResult;

	public HttpMultipartPost(Context context, List<String> filePathList,UploadCallBack uploadCallBack) {
		this.context = context;
		this.filePathList = filePathList;
		this.mUploadCallBack = uploadCallBack;
	}

	@Override
	protected void onPreExecute() {
		pd = ShowUtils.createLoadingDialog(context, true);
		pd.setCancelable(false);
		pd.show();
	}

	@Override
	protected String doInBackground(String... params) {
		String serverResponse = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		HttpPost httpPost = new HttpPost(ConstantsOnline.IMAGE_UPLOAD);
		try {
			CustomMultipartEntity multipartContent = new CustomMultipartEntity(
					new ProgressListener() {
						@Override
						public void transferred(long num) {
							publishProgress((int) ((num / (float) totalSize) * 100));
						}
					});

			// We use FileBody to transfer an image
			//把上传内容添加到MultipartEntity
//			for (int i = 0; i < filePathList.size(); i++) {
//				multipartContent.addPart("file", new FileBody(new File(filePathList.get(i))));
//				multipartContent.addPart("data",new StringBody(filePathList.get(i), Charset
//	                                    .forName(org.apache.http.protocol.HTTP.UTF_8)));
//			}
			for (int i = 0; i < filePathList.size(); i++) {
				String picPath = PicCompressUtil.getCompressBitmap(filePathList.get(0), Integer.valueOf(StringUtil.getScreenWidthSize(context)));
				File file = new File(picPath);
				multipartContent.addPart("file", new FileBody(file));
				multipartContent.addPart("data",
						new StringBody(picPath, Charset
								.forName(org.apache.http.protocol.HTTP.UTF_8)));
			}
			
			totalSize = multipartContent.getContentLength();
			
//			Bitmap  bitmap = convertToBitmap(filePathList.get(0),400,400);
//	        //把Bitmap写进流里面
//	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//	        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//	        //把流转化为数组
//			byte[] b = stream.toByteArray();
//			// 将图片流以字符串形式存储下来
//			String pic = new String(Base64Coder.encodeLines(b));
//			
//			// 设置HTTP POST请求参数必须用NameValuePair对象 
//	        List<NameValuePair> param = new ArrayList<NameValuePair>(); 
//	        param.add(new BasicNameValuePair("action", "downloadAndroidApp"));
//	        param.add(new BasicNameValuePair("action", "downloadAndroidApp"));
//			 httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			 
			// Send it
			httpPost.setEntity(multipartContent);
			HttpResponse httpResponse = httpClient.execute(httpPost, httpContext);
			serverResponse = EntityUtils.toString(httpResponse.getEntity());
			responseResult = serverResponse;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return serverResponse;
	}


	@Override
	protected void onPostExecute(String result) {
		mUploadCallBack.onSuccessListener(result);
		pd.dismiss();
	}

	@Override
	protected void onCancelled() {
	}
	
	public static abstract class UploadCallBack{
		public abstract void onSuccessListener(String result);
	}
}
