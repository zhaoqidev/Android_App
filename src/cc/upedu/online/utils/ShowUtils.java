package cc.upedu.online.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.upedu.online.R;

/**
 * 显示工具类
 * showMsg用来显示Toast
 * showDiaLog用来显示提示框
 * getDisPlay用来显示默认图片
 *
 */
public class ShowUtils {

	private static Dialog dialog;
	private static LayoutInflater inflater;

	/**
	 * 显示Toast的方法
	 */
	public static void showMsg(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	/**
	 * @param context
	 *            上下文对象
	 * @param title
	 *            对话框的标题
	 * @param message
	 *            对话框显示的信息
	 * @param cla
	 *            点击确定想要跳转到的类
	 */
	public static void showDiaLog(final Context context, String title,
			String message, final ConfirmBackCall mConfirmBackCall) {
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(context, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.show();
		Button btnsure = (Button) view.findViewById(R.id.dialogbtnsure);
		TextView textView = (TextView) view.findViewById(R.id.textmessage);
		textView.setText(message);
		TextView titles = (TextView) view.findViewById(R.id.texttitles);
		titles.setText(title);
		btnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConfirmBackCall.confirmOperation();
				dialog.cancel();
			}
		});
		Button btncancle = (Button) view.findViewById(R.id.dialogbtncancle);
		btncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	public interface ConfirmBackCall{
		/**
		 * 自定义对话框的确认按钮所执行的回调方法
		 */
		abstract void confirmOperation();
	}
	
	/**
	 * 弹出dialog，重写确定按钮和取消按钮的回调
	 * @param context
	 * @param title
	 * @param message
	 * @param mConfirmBackCall
	 */
	public static void showDiaLog2(final Context context, String title,
			String message, final ConfirmBackCall2 mConfirmBackCall) {
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(context, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.show();
		Button btnsure = (Button) view.findViewById(R.id.dialogbtnsure);
		TextView textView = (TextView) view.findViewById(R.id.textmessage);
		textView.setText(message);
		TextView titles = (TextView) view.findViewById(R.id.texttitles);
		titles.setText(title);
		btnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConfirmBackCall.confirmOperation();
				dialog.cancel();
			}
		});
		Button btncancle = (Button) view.findViewById(R.id.dialogbtncancle);
		btncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConfirmBackCall.cancleOperation();
				dialog.cancel();
			}
		});
	}

	/**
	 * 弹出dialog，只有确定选项
	 * @param context 上下文对象
	 * @param title dialog 的 title
	 * @param message 要显示的信息
	 * @param mConfirmBackCall 点击确认按钮的回调
	 */
	public static void showDiaLog_sure(final Context context, String title,
								   String message, final ConfirmBackCall mConfirmBackCall) {
		inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_show_sure, null);
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(context, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.show();
		Button btnsure = (Button) view.findViewById(R.id.dialogbtnsure);
		TextView textView = (TextView) view.findViewById(R.id.textmessage);
		textView.setText(message);
		TextView titles = (TextView) view.findViewById(R.id.texttitles);
		titles.setText(title);
		btnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mConfirmBackCall.confirmOperation();
				dialog.cancel();
			}
		});
	}

	public interface ConfirmBackCall2{
		/**
		 * 自定义对话框的确认按钮所执行的回调方法
		 */
		abstract void confirmOperation();
		/**
		 * 自定义对话框的取消按钮所执行的回调方法
		 */
		abstract void cancleOperation();
	}
	
	
	/**
	 * 显示进度条
	 * 
	 * @param progressDialog
	 */
	public static void showProgressDialog(ProgressDialog progressDialog) {
		progressDialog.setMessage("努力加载中...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}

	/**
	 * 隐藏进度条
	 * 
	 * @param progressDialog
	 */
	public static void exitProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	/** 
     * 得到自定义的progressDialog 
     * @param context 
     * @param cancelable 是否能使用返回键
     * @return 
     */  
    public static Dialog createLoadingDialog(Context context,boolean cancelable) {  
  
        LayoutInflater inflater = LayoutInflater.from(context);  
        View v = inflater.inflate(R.layout.layout_progressdealog, null);// 得到加载view  
        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局  
//        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字  
        
//        tipTextView.setText(msg);// 设置加载信息  
  
        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog  
  
        loadingDialog.setCancelable(cancelable);// 可不可以用“返回键”取消  
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(  
                LinearLayout.LayoutParams.FILL_PARENT,  
                LinearLayout.LayoutParams.FILL_PARENT));// 设置布局  
        return loadingDialog;
//        progressDlg = new ProgressDialog(context);
//		//progressDlg.setMessage("数据加载中...");
//		progressDlg.setMessage(this.getResources().getString(R.string.Loading));
//		progressDlg.setCancelable(false);
//		progressDlg.setIndeterminate(true);
    } 
}
