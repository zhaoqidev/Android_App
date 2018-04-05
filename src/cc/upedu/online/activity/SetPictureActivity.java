package cc.upedu.online.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelTopBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.PicItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.photoselector.PhotoItem.onItemLongClickListener;
import cc.upedu.online.photoselector.PhotoModel;
import cc.upedu.online.photoselector.PhotoPreviewActivity;
import cc.upedu.online.photoselector.PhotoSelectorActivity;
import cc.upedu.online.photoselector.PhotoSelectorView;
import cc.upedu.online.photoselector.PicImage;
import cc.upedu.online.utils.CreateBmpFactory;
import cc.upedu.online.utils.HttpMultipartPost;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.factory.MyHorizontalIconTextItem;
/**
 * 设置我的图片
 * @author Administrator
 *
 */
public class SetPictureActivity extends TwoPartModelTopBaseActivity implements onItemLongClickListener {
	public static final int RESULT_SETPICTURE = 2;
	private List<PicItem> picList;
	private List<String> oldUrlList;
	private List<String> newUrlList;
	private List<String> picAddArray;
	private List<String> picDelArray;
	private Map<String, String> picIvMap;
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		picList = (List<PicItem>)getIntent().getSerializableExtra("picList");
		//原用户信息中的图片的url集合
		oldUrlList = new ArrayList<String>();
		//展示出来的图片的URL集合(包括)
		newUrlList = new ArrayList<String>();
		picDelArray = (List<String>)getIntent().getSerializableExtra("picDelArray");
		if (picList != null) {
			picIvMap = new HashMap<String, String>();
			if (picDelArray == null) {
				picDelArray = new ArrayList<String>();
				if (picList.size() > 0) {
					for (int i = 0; i < picList.size(); i++) {
						picIvMap.put(picList.get(i).getId(), picList.get(i).getVersion());
						oldUrlList.add(picList.get(i).getPicPath());
					}
				}
				newUrlList.addAll(oldUrlList);
			}else {
				if (picList.size() > 0) {
					for (int i = 0; i < picList.size(); i++) {
						picIvMap.put(picList.get(i).getId(), picList.get(i).getPicPath());
						oldUrlList.add(picList.get(i).getPicPath());
						if (!picDelArray.contains(picList.get(i).getPid()+"_"+picList.get(i).getVersion())) {
							newUrlList.add(picList.get(i).getPicPath());
						}
					}
				}
			}
		}else {
			if (picDelArray == null) {
				picDelArray = new ArrayList<String>();
			}
		}
		picAddArray = (List<String>)getIntent().getSerializableExtra("picAddArray");
		if (picAddArray == null) {
			picAddArray = new ArrayList<String>();
		}else {
			for (String picurl : picAddArray) {
				newUrlList.add(picurl.replace("\"", ""));
			}
		}

		notifyView();

//		mCreateBmpFactory = new CreateBmpFactory(this);
//		if (isAdapterEmpty()) {
//			setNumColumns(3);
//			setGridView(new MyGridViewAdapter());
//		}else {
//			notifyData();
//		}
//		View picView = LayoutInflater.from(SetPictureActivity.this).inflate(R.layout.layout_gvitem_userpic, null);
//		mImageButton = (ImageButton) picView.findViewById(R.id.headpic);
	}

	public void notifyView(){
		try {
			pic.clear();
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (String path : newUrlList){
			PicImage picImage = new PicImage();
			picImage.setOriginalPath(path);
			picImage.setPic(true);
			picImage.setIsurl(true);
			picImage.setCanDelete(true);
			pic.add(picImage);
		}
		if (pic.size() < 10) {
			PicImage picImage1 = new PicImage();
			picImage1.setPic(false);
			pic.add(picImage1);
		}
		photoSelectorView.notifyAdapter();
	}
	@Override
	protected void initTitle() {
		setTitleText("我的照片");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("picAddArray", (Serializable) picAddArray);
				intent.putExtra("picDelArray", (Serializable) picDelArray);
				setResult(RESULT_SETPICTURE, intent);
				finish();

			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if ((picAddArray != null && picAddArray.size() > 0) || (picDelArray != null && picDelArray.size() > 0))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetPictureActivity.this.finish();
						}
					});
				else
					SetPictureActivity.this.finish();
			}
		});
	}

	@Override
	public View initTopLayout() {
		MyHorizontalIconTextItem topview = new MyHorizontalIconTextItem(MyHorizontalIconTextItem.TEXT);
		View view = topview.initView(context);
		topview.setBackgroundColor(getResources().getColor(R.color.backGrond));
		topview.setText(R.string.name_picturedoc);
		return view;
	}

	private ArrayList<PicImage> pic;
	private LinearLayout llPhotos;
	private PhotoSelectorView photoSelectorView;
	@Override
	public View initTwelfthLayout() {
		LinearLayout llPhotos = (LinearLayout) View.inflate(context,R.layout.layout_photos,null);
		pic = new ArrayList<PicImage>();
		PicImage picImage = new PicImage();
		picImage.setPic(false);
		pic.add(picImage);
		photoSelectorView = new PhotoSelectorView(this, pic,
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						takeNewPhotoOrChoosePic();
					}
				},3,10){

			@Override
			public void onDeleteClick(PhotoModel photoModel, View view,int position) {
				super.onDeleteClick(photoModel, view,position);
				if (oldUrlList.contains(newUrlList.get(position))) {
					String delId = picList.get(oldUrlList.indexOf(newUrlList.get(position))).getId();
					if (picDelArray == null) {
						picDelArray = new ArrayList<String>();
					}
					picDelArray.add(delId +"_"+picList.get(oldUrlList.indexOf(newUrlList.get(position))).getVersion());
				}else {
					picAddArray.remove(picAddArray.indexOf("\""+newUrlList.get(position)+"\""));
				}
				newUrlList.remove(position);
			}
		};
		llPhotos.addView(photoSelectorView);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, dpToPx(8), 0, dpToPx(8));
		llPhotos.setLayoutParams(lp);
		return llPhotos;
	}

	public int dpToPx(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}

	/**
	 * 拍照,选择图片
	 */
	private void takeNewPhotoOrChoosePic() {
		Intent intent = new Intent(context,
				PhotoSelectorActivity.class);
//		int count  = 0;
//		try {
//			count= pic.size() ;
//		} catch (Exception e) {
//			count  = 0;
//		}
//
		intent.putExtra(PhotoSelectorActivity.KEY_MAX, 10);
		intent.putExtra("selected", pic);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(intent, 1);
	}
	@Override
	public void onItemLongClick(int position) {
		// TODO Auto-generated method stub
//		Bundle bundle = new Bundle();
//		ArrayList<PhotoModel> picselect = new ArrayList<PhotoModel>();
//		PhotoModel photoModel = new PhotoModel(pic.get(position).getOriginalPath(),true);
//		picselect.add(photoModel);
//		bundle.putSerializable("select", picselect);
//		Intent intent = new Intent(this, PhotoPreviewActivity.class);
//		intent.putExtras(bundle);
//		startActivityForResult(intent, 2);
		Intent intent = new Intent(context,ImageActivity.class);
		intent.putExtra("image_list", (Serializable) newUrlList);
		intent.putExtra("image_position", position);
		context.startActivity(intent);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(requestCode);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		switch (requestCode) {
			case 1:
				if (resultCode == Activity.RESULT_OK) {
					try {

						try {
							pic.clear();;
						} catch (Exception e) {
							// TODO: handle exception
						}
						List<PhotoModel> photos = (List<PhotoModel>) data
								.getExtras().getSerializable("photos");

						Message msg = new Message();
						msg.what = 1;
						msg.obj = photos;
						handler.sendMessage(msg);

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				break;
			case 2:
				try {
					PhotoModel photo = (PhotoModel) data.getSerializableExtra("photo");
					//						.getExtras().getSerializable("photos");
					//				for (PhotoModel photoModel : photos) {
					for(PicImage picImage:pic){
						if(picImage.getOriginalPath().equals(photo.getOriginalPath())){
							pic.remove(picImage);
						}
					}
					//				}
					photoSelectorView.notifyAdapter();
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
		}
	}

	private android.os.Handler handler = new android.os.Handler(){
		List<PhotoModel> photos;
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					final int position = (int) msg.obj;
					if (photos != null && photos.size() > position){
						String path = photos.get(position).getOriginalPath();
						List<String> filePathList = new ArrayList<>();
						filePathList.add(path);

						HttpMultipartPost post = new HttpMultipartPost(SetPictureActivity.this, filePathList,new HttpMultipartPost.UploadCallBack() {

							@Override
							public void onSuccessListener(String result) {
								// TODO Auto-generated method stub
								if (!StringUtil.isEmpty(result)) {
									System.out.println("---------------result----------------" + result);
									newUrlList.add(0, result.substring(result.indexOf("/")));
									picAddArray.add("\"" + result.substring(result.indexOf("/")) + "\"");

									if (position < photos.size()) {
										Message msg3 = new Message();
										msg3.what = 0;
										msg3.obj = position+1;
										handler.sendMessage(msg3);
									}

								}else {
									ShowUtils.showMsg(context, "加载图片失败");
								}
							}
						});
						post.execute();
					}else {
						notifyView();
					}
					break;
				case 1:
					photos = (List<PhotoModel>) msg.obj;
					for (int i=0;i < photos.size();i++){
						if (newUrlList.contains(photos.get(i).getOriginalPath())){
							photos.remove(i);
							i--;
						}
					}
					Message msg2 = new Message();
					msg2.what = 0;
					msg2.obj = 0;
					handler.sendMessage(msg2);
//					for (PhotoModel photoModel : photos) {
//
//					}
					break;
			}
		};
	};



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if ((picAddArray != null && picAddArray.size() > 0) || (picDelArray != null && picDelArray.size() > 0)) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetPictureActivity.this.finish();
					}
				});
				return false;
			}else {
				return super.onKeyDown(keyCode, event);
			}
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

//	private ImageButton mImageButton;
//	private CreateBmpFactory mCreateBmpFactory;
//	public class MyGridViewAdapter extends BaseAdapter {
//		private LayoutInflater inflater;
//
//		@Override
//		public int getCount() {
////			if (viewList == null) {
////				return 0;
////			}
////			return viewList.size();
//			return newUrlList.size() + 1;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			if (position == newUrlList.size()) {
//				return newUrlList.get(position);
//			}else {
//				return null;
//			}
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//		@Override
//		public View getView(final int position, View convertView,
//				ViewGroup parent) {
//			inflater = LayoutInflater.from(SetPictureActivity.this);
//			if (position == newUrlList.size()) {
//				View addView = inflater.inflate(R.layout.layout_gvitem_useradd, null);
//				addView.findViewById(R.id.add).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								if (newUrlList.size() == 10) {
//									ShowUtils.showMsg(context, "已达到最多图片数量");
//									return;
//								} else {
//									ImageSelectorDialog dialog = new ImageSelectorDialog(context);
//									dialog.setDialogCallBack(dialog.new DialogCallBack() {
//										@Override
//										public void sendPic() {
//											mCreateBmpFactory.OpenGallery();
//										}
//										@Override
//										public void sendCamera() {
//											mCreateBmpFactory.OpenCamera();
//										}
//									});
//									Window window = dialog.getWindow();
//									window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//									window.setWindowAnimations(R.style.style_imageselectordialog); // 添加动画
//									window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//									window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//									dialog.show();
//								}
//							}
//						});
//				return addView;
//			} else {
//				View picView = inflater.inflate(R.layout.layout_gvitem_userpic, null);
//				final ImageButton picIBtn = (ImageButton) picView
//						.findViewById(R.id.headpic);
//
//				ImageUtils.setImageToImageButton(newUrlList.get(position), picIBtn, R.drawable.wodeimg_default);
//
//				picIBtn.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//
//							Intent intent = new Intent(context,ImageActivity.class);
//							intent.putExtra("image_list", (Serializable) newUrlList);
//							intent.putExtra("image_position", position);
//							context.startActivity(intent);
//
//					}
//				});
//				picView.findViewById(R.id.delete).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
////								viewList.remove(position);
//								if (oldUrlList.contains(newUrlList.get(position))) {
//									String delId = picList.get(oldUrlList.indexOf(newUrlList.get(position))).getId();
//									if (picDelArray == null) {
//										picDelArray = new ArrayList<String>();
//									}
//									picDelArray.add(delId +"_"+picIvMap.get(delId));
//								}else {
//									picAddArray.remove(picAddArray.indexOf("\""+newUrlList.get(position)+"\""));
//								}
//								newUrlList.remove(position);
//								notifyData();
//							}
//						});
//				return picView;
//			}
//		}
//	}
//
//	private Bitmap bmp;
//	private String picPath;
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		picPath = mCreateBmpFactory.getBitmapFilePath(requestCode,
//				resultCode, data);
//		ImageSize imageSize = getImageButtonWidth(mImageButton);
//
//		int reqWidth = imageSize.width;
//		int reqHeight = imageSize.height;
//
//		if(picPath!=null){
//			bmp = mCreateBmpFactory.getBitmapByOpt(picPath, reqWidth, reqHeight);
//			if (bmp != null) {
////				File file = new File(picPath);
////				String name = file.getName();
//
//				ArrayList<String> filePathList = new ArrayList<String>();
//				filePathList.add(picPath);
//				HttpMultipartPost post = new HttpMultipartPost(SetPictureActivity.this, filePathList,new UploadCallBack() {
//
//					@Override
//					public void onSuccessListener(String result) {
//						// TODO Auto-generated method stub
//						if (!StringUtil.isEmpty(result)) {
//							newUrlList.add(0, result.substring(result.indexOf("/")));
//							picAddArray.add("\""+result.substring(result.indexOf("/"))+"\"");
////						viewList.add(0, bmp);
//							notifyData();
//						}else {
//							ShowUtils.showMsg(context, "加载图片失败");
//						}
//					}
//				});
//				post.execute();
//			}else{
//				ShowUtils.showMsg(context, "上传的文件路径出错");
//			}
//		}else{
//			ShowUtils.showMsg(context, "上传的文件路径出错");
//		}
//		super.onActivityResult(requestCode, resultCode, data);
//	}
//
//	private class ImageSize
//	{
//		int width;
//		int height;
//	}
//	/**
//	 * 根据ImageButton获得适当的压缩的宽和高
//	 *
//	 * @param imageButton
//	 * @return
//	 */
//	private ImageSize getImageButtonWidth(ImageButton imageButton)
//	{
//		ImageSize imageSize = new ImageSize();
//		final DisplayMetrics displayMetrics = imageButton.getContext()
//				.getResources().getDisplayMetrics();
//		final LayoutParams params = imageButton.getLayoutParams();
//
//		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageButton
//				.getWidth(); // Get actual image width
//		if (width <= 0)
//			width = params.width; // Get layout width parameter
//		if (width <= 0)
//			width = getImageButtonFieldValue(imageButton, "mMaxWidth"); // Check
//																	// maxWidth
//																	// parameter
//		if (width <= 0)
//			width = displayMetrics.widthPixels;
//		int height = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageButton
//				.getHeight(); // Get actual image height
//		if (height <= 0)
//			height = params.height; // Get layout height parameter
//		if (height <= 0)
//			height = getImageButtonFieldValue(imageButton, "mMaxHeight"); // Check
//																		// maxHeight
//																		// parameter
//		if (height <= 0)
//			height = displayMetrics.heightPixels;
//		imageSize.width = width;
//		imageSize.height = height;
//		return imageSize;
//
//	}
//	/**
//	 * 反射获得ImageButton设置的最大宽度和高度
//	 *
//	 * @param object
//	 * @param fieldName
//	 * @return
//	 */
//	private static int getImageButtonFieldValue(Object object, String fieldName)
//	{
//		int value = 0;
//		try
//		{
//			Field field = ImageButton.class.getDeclaredField(fieldName);
//			field.setAccessible(true);
//			int fieldValue = (Integer) field.get(object);
//			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE)
//			{
//				value = fieldValue;
//
//				Log.e("TAG", value + "");
//			}
//		} catch (Exception e)
//		{
//		}
//		return value;
//	}
}
