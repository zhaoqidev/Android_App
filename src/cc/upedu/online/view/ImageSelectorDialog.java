package cc.upedu.online.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import cc.upedu.online.R;
import cc.upedu.online.utils.ScreenUtil;

/**
 * 用于图片选择的dialog,可选相机和相册
 */
public class ImageSelectorDialog extends AlertDialog implements OnClickListener {
	private DialogCallBack mDialogCallBack;
	private ImageButton sendPic;
	private ImageButton sendCamera;
	private Context context;

	public ImageSelectorDialog(Context context) {
		super(context);
		this.context = context;
		this.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = View.inflate(context, R.layout.addpic, null);
		LinearLayout ll_sendPic = (LinearLayout) view.findViewById(R.id.ll_sendPic);
		LinearLayout ll_sendCamera = (LinearLayout) view.findViewById(R.id.ll_sendCamera);
//		ll_sendPic.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil.getInstance(context).getScreenWidth()/2,  LinearLayout.LayoutParams.WRAP_CONTENT));
//		ll_sendCamera.setLayoutParams(new LinearLayout.LayoutParams(ScreenUtil.getInstance(context).getScreenWidth()/2,  LinearLayout.LayoutParams.WRAP_CONTENT));
		setContentView(view,new LinearLayout.LayoutParams(ScreenUtil.getInstance(context).getScreenWidth(),  LinearLayout.LayoutParams.WRAP_CONTENT));
		findUiById();
		addListener();
	}

	private void findUiById() {
		sendPic = (ImageButton) this.findViewById(R.id.sendPic);
		sendCamera = (ImageButton) this.findViewById(R.id.sendCamera);
	}

	public void addListener() {
		sendPic.setOnClickListener(this);
		sendCamera.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sendPic:
			if (mDialogCallBack != null) {
				mDialogCallBack.sendPic();
			}
			dismiss();
			break;
		case R.id.sendCamera:
			if (mDialogCallBack != null) {
				mDialogCallBack.sendCamera();
			}
			dismiss();
			break;
		}
	}
	public void setDialogCallBack (DialogCallBack mDialogCallBack){
		this.mDialogCallBack = mDialogCallBack;
	}
	public abstract class DialogCallBack{
		public abstract void sendPic();
		public abstract void sendCamera();
	}
}