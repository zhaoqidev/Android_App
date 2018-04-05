package cc.upedu.online.photoselector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.utils.ImageUtils;

/**
 * com.photoselector.ui.PhotoItem
 * 
 * @author guowla@yonyou create at 2015年6月3日 上午10:18:07
 */
public class PhotoItem extends LinearLayout implements OnCheckedChangeListener,
		OnLongClickListener {
	private static final int REQUEST_CAMERA = 1;
	private ImageView ivPhoto;
	private CheckBox cbPhoto;
	private onPhotoItemCheckedListener listener;
	private onPhotoItemDeleteListener delistener;
	private PhotoModel photo;
	private boolean isCheckAll;
	private onItemLongClickListener itemLongListenter;
	private int position;
	private TextView takePhoto;
	private ImageView deleteBtn;
	private OnClickListener takePhotoClick;
	private PhotoSelectorAdapter adapter;
	private Context context;
	private PhotoItem(Context context) {
		super(context);
	}

	public PhotoItem(final Context context,
			onPhotoItemCheckedListener listener, final PhotoSelectorAdapter adapter,final onPhotoItemDeleteListener delistener) {
		this(context);
		LayoutInflater.from(context).inflate(R.layout.layout_photoitem, this,
				true);
		this.listener = listener;
		this.delistener = delistener;
		this.adapter = adapter;
		this.context = context;
		setOnLongClickListener(this);
		ivPhoto = (ImageView) findViewById(R.id.iv_photo_lpsi);
		cbPhoto = (CheckBox) findViewById(R.id.cb_photo_lpsi);
		takePhoto = (TextView) findViewById(R.id.tv_camera_vc);
		deleteBtn = (ImageView) findViewById(R.id.delete_photo_btn);
		deleteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 delistener.onDeleteClick(photo, arg0,position);
			}
		});
		cbPhoto.setOnCheckedChangeListener(this); 
	}

	/**
	 * 创建文件名字
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private String createFileName() {

		String fileName = "";
		Date date = new Date(System.currentTimeMillis()); // 系统当前时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		fileName = dateFormat.format(date);
		return fileName + ".jpg";
	}

	public CheckBox getCbPhoto() {
		return cbPhoto;
	}

	public void setCbPhoto(CheckBox cbPhoto) {
		this.cbPhoto = cbPhoto;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (!isCheckAll) {
			listener.onCheckedChanged(photo, buttonView, isChecked);
		}
		if (buttonView.isChecked()) {
			setDrawingable();
			ivPhoto.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
		} else {
			ivPhoto.clearColorFilter();
		}
		photo.setChecked(buttonView.isChecked());
	}

	public void setImageDrawable(final PhotoModel photo) {
		this.photo = photo;
		if(this.photo.isCanDelete()){
			deleteBtn.setVisibility(View.VISIBLE);
		}
		// You may need this setting form some custom ROM(s)
		/*
		 * new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { ImageLoader.getInstance().displayImage(
		 * "file://" + photo.getOriginalPath(), ivPhoto); } }, new
		 * Random().nextInt(10));
		 */
		if (photo.isCamera()) {
			ivPhoto.setVisibility(View.GONE);
			cbPhoto.setVisibility(View.GONE);
			takePhoto.setVisibility(View.VISIBLE);
			takePhotoClick = new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String filePath = Environment.getExternalStorageDirectory()
							+ "/myImage/";
					File dir = new File(filePath);

					if (!dir.exists()) {
						dir.mkdirs();
					}

					Intent intent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					String fileName = createFileName();
					File file = new File(dir, fileName);
					Uri r = Uri.fromFile(file);
					adapter.setUri(r);
					intent.putExtra(ImageColumns.ORIENTATION, 0);
					intent.putExtra(MediaStore.EXTRA_OUTPUT, r);
					((Activity) context).startActivityForResult(intent,
							REQUEST_CAMERA);
					//
					// CommonUtils.launchActivityForResult((Activity)context, new
					// Intent(
					// MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);

				}
			};
			takePhoto.setOnClickListener(takePhotoClick);
		} else {
			String loadStr = photo.getOriginalPath();

			if(!photo.isIsurl()){
				loadStr ="file://"+loadStr;
				ivPhoto.setBackgroundResource(0);
				OnlineApp.myApp.imageLoader.displayImage(loadStr, ivPhoto);
			}else {
				ImageUtils.setImage(loadStr,ivPhoto,R.drawable.wodeimg_default);
			}


			takePhoto.setOnClickListener(null);
		}
	}

	public void setBtnImageDrawable(final PhotoModel photo) {
		this.photo = photo;
		// You may need this setting form some custom ROM(s)
		/*
		 * new Handler().postDelayed(new Runnable() {
		 * 
		 * @Override public void run() { ImageLoader.getInstance().displayImage(
		 * "file://" + photo.getOriginalPath(), ivPhoto); } }, new
		 * Random().nextInt(10));
		 */
		ivPhoto.setBackgroundResource(R.drawable.add_value);
	}

	private void setDrawingable() {
		ivPhoto.setDrawingCacheEnabled(true);
		ivPhoto.buildDrawingCache();
	}

	@Override
	public void setSelected(boolean selected) {
		if (photo == null) {
			return;
		}
		isCheckAll = true;
		cbPhoto.setChecked(selected);
		isCheckAll = false;
	}

	public void setOnClickListener(onItemLongClickListener l, int position) {
		this.itemLongListenter = l;
		this.position = position;
	}

	// @Override
	// public void
	// onClick(View v) {
	// if (l != null)
	// l.onItemClick(position);
	// }

	/** ͼƬItemѡ���¼������� */
	public static interface onPhotoItemCheckedListener {
		public void onCheckedChanged(PhotoModel photoModel,
									 CompoundButton buttonView, boolean isChecked);
	}
	/** ͼƬItemѡ���¼������� */
	public static interface onPhotoItemDeleteListener {
		public void onDeleteClick(PhotoModel photoModel, View view,int position);
	}

	/** ͼƬ����¼� */
	public interface onItemLongClickListener {
		public void onItemLongClick(int position);
	}

	@Override
	public boolean onLongClick(View v) {
		if (itemLongListenter != null)
			itemLongListenter.onItemLongClick(position);
		return true;
	}

	public void setUriOfPic(Uri uri) {
		// TODO Auto-generated method stub

	}

}
