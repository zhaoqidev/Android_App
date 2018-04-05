package cc.upedu.online.interfaces;
/**
 * 上传文本数据功能的回调接口
 * @author Administrator
 *
 */
public interface UploadDataCallBack {
	/**
	 * 收藏成功的回调方法
	 */
	abstract void onUploadDataSuccess();
	/**
	 * 收藏失败的回调方法
	 */
	abstract void onUploadDataFailure();
}
