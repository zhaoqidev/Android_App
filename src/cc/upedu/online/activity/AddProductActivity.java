package cc.upedu.online.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.UserCordBean;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.PicItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.photoselector.PhotoItem.onItemLongClickListener;
import cc.upedu.online.photoselector.PhotoModel;
import cc.upedu.online.photoselector.PhotoSelectorActivity;
import cc.upedu.online.photoselector.PhotoSelectorView;
import cc.upedu.online.photoselector.PicImage;
import cc.upedu.online.utils.HttpMultipartPost;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;

/**
 * 添加/修改产品信息界面
 * @author Administrator
 *
 */
public class AddProductActivity extends TitleBaseActivity implements onItemLongClickListener {
	public static final int RESULT_SETOLDPRODUCTCITY = 21;
	private ImageView iv_addpic;
	private ProductItem oldProductItem;
	private boolean isNewProduct;
	private EditText et_productname,et_productdesc,et_productcustomer,et_productvalue;
	private String productName,productDesc,productCustomer,productValue;
	private ProductItem newProductItem;
	private List<PicItem> picList;
	private List<String> oldUrlList;
	private List<String> newUrlList;
	private List<String> picAddArray;
	private List<String> picDelArray;
	private Map<String, String> picIvMap;
	private LinearLayout ll_default;
	private LinearLayout llPhotos;
	private boolean isChange = false;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("主营产品");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				productName = et_productname.getText().toString().toString().trim();
				productDesc = et_productdesc.getText().toString().toString().trim();
				productCustomer = et_productcustomer.getText().toString().toString().trim();
				productValue = et_productvalue.getText().toString().toString().trim();
				if (StringUtil.isEmpty(productName)) {
					ShowUtils.showMsg(context, "产品名称不能为空");
				} else if (StringUtil.isEmpty(productDesc)) {
					ShowUtils.showMsg(context, "产品描述不能为空");
				} else {
					if (isNewProduct) {
						if (newProductItem == null) {
							newProductItem = new UserCordBean().new Entity().new UserInfo().new CompanyItem().new ProductItem();
						}
						newProductItem.setTitle(productName);
						newProductItem.setDesc(productDesc);
						newProductItem.setCustomer(productCustomer);
						newProductItem.setValue(productValue);
						newProductItem.setPicAddArray(picAddArray);
						intent.putExtra("newProductItem", (Serializable) newProductItem);
					} else {
						oldProductItem.setTitle(productName);
						oldProductItem.setDesc(productDesc);
						oldProductItem.setCustomer(productCustomer);
						oldProductItem.setValue(productValue);
						oldProductItem.setPicAddArray(picAddArray);
						oldProductItem.setPicDelArray(picDelArray);
						intent.putExtra("alterProductItem", (Serializable) oldProductItem);
					}
					setResult(RESULT_SETOLDPRODUCTCITY, intent);
					finish();
				}
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				productName = et_productname.getText().toString().toString().trim();
				productDesc = et_productdesc.getText().toString().toString().trim();
				productCustomer = et_productcustomer.getText().toString().toString().trim();
				productValue = et_productvalue.getText().toString().toString().trim();

				if (isNewProduct){
					if (!StringUtil.isEmpty(productName) || !StringUtil.isEmpty(productDesc)){
						if (!isChange)
							isChange = true;
					}
				}else {
					if ((!StringUtil.isEmpty(productName) && !productName.equals(oldProductItem.getTitle())) || StringUtil.isEmpty(productName)
							|| (!StringUtil.isEmpty(productDesc) && !productDesc.equals(oldProductItem.getDesc())) || StringUtil.isEmpty(productDesc)
							|| (!StringUtil.isEmpty(productCustomer) && !productCustomer.equals(oldProductItem.getCustomer())) || StringUtil.isEmpty(productCustomer)
							|| (!StringUtil.isEmpty(productValue) && !productValue.equals(oldProductItem.getValue())) || StringUtil.isEmpty(productValue)
							|| (picAddArray != null && picAddArray.size() > 0)
							|| (picDelArray != null && picDelArray.size() > 0)){
						if (!isChange)
							isChange = true;
					}
				}
				if (isChange)
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							AddProductActivity.this.finish();
						}
					});
				else
					AddProductActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		oldProductItem = (ProductItem)getIntent().getSerializableExtra("productItem");
//		viewList = new ArrayList<Bitmap>();
		oldUrlList = new ArrayList<String>();
		newUrlList = new ArrayList<String>();
		
		if (oldProductItem != null) {
			isNewProduct = false;
			picList = oldProductItem.getPicList();
			picDelArray = oldProductItem.getPicDelArray();
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
			picAddArray = oldProductItem.getPicAddArray();
			if (picAddArray == null) {
				picAddArray = new ArrayList<String>();
			}else {
				for (String picurl : picAddArray) {
					newUrlList.add(picurl.substring(1, picurl.length()-1));
				}
			}
		}else {
			isNewProduct = true;
			newProductItem = new UserCordBean().new Entity().new UserInfo().new CompanyItem().new ProductItem();
			newProductItem.setId("#"+System.currentTimeMillis());
		}

		View view = View.inflate(context, R.layout.activity_setproductitem, null);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		et_productname = (EditText) view.findViewById(R.id.et_productname);
		et_productdesc = (EditText) view.findViewById(R.id.et_productdesc);
		et_productcustomer = (EditText) view.findViewById(R.id.et_productcustomer);
		et_productvalue = (EditText) view.findViewById(R.id.et_productvalue);
		iv_addpic = (ImageView) view.findViewById(R.id.iv_addpic);
		llPhotos = (LinearLayout) view.findViewById(R.id.layout_photos);
		if (!isNewProduct) {
			setData2View();
		}

		initPhotos();
		notifyView();

//		mCreateBmpFactory = new CreateBmpFactory(this);
//		// 增删图片网格业务
//		gv = (GridView) view.findViewById(R.id.gridView);
////		viewList.add(null);
//		adapter = new MyGridViewAdapter(context,newUrlList);
//		gv.setAdapter(adapter);
//
//		View picView = LayoutInflater.from(AddProductActivity.this).inflate(R.layout.gv_item_pic, null);
//		mImageButton = (ImageButton) picView.findViewById(R.id.pic);
		return view;
	}
	private void setData2View() {
		if (oldProductItem != null) {
			et_productname.setText(oldProductItem.getTitle());
			et_productdesc.setText(oldProductItem.getDesc());
			et_productcustomer.setText(oldProductItem.getCustomer());
			et_productvalue.setText(oldProductItem.getValue());
		}
	}

	@Override
	protected void initListener() {
		super.initListener();
		ll_default.setOnClickListener(this);
		iv_addpic.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_default:
			if(AddProductActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddProductActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case R.id.iv_addpic:
			if (newUrlList.size() == 10) {
				ShowUtils.showMsg(context, "已达到最多图片数量");
				return;
			} else {
//				ImageSelectorDialog dialog = new ImageSelectorDialog(context);
//				dialog.setDialogCallBack(dialog.new DialogCallBack() {
//					@Override
//					public void sendPic() {
//						mCreateBmpFactory.OpenGallery();
//					}
//					@Override
//					public void sendCamera() {
//						mCreateBmpFactory.OpenCamera();
//					}
//				});
//				Window window = dialog.getWindow();
//				window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//				window.setWindowAnimations(R.style.style_imageselectordialog); // 添加动画
//				window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//				window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//				dialog.show();
				takeNewPhotoOrChoosePic();
			}
			break;
		}
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
	private ArrayList<PicImage> pic;
	private PhotoSelectorView photoSelectorView;
	private void initPhotos(){
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
				},4,10){

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
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, dpToPx(8), 0, dpToPx(8));
		llPhotos.setLayoutParams(lp);
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

						HttpMultipartPost post = new HttpMultipartPost(AddProductActivity.this, filePathList,new HttpMultipartPost.UploadCallBack() {

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



//	private Bitmap bmp;
//	private String picPath;
//	/** 网络，用于动态显示添加删除图片 */
//	private GridView gv;
//	/** 适配器 */
//	private MyGridViewAdapter adapter;
//	/** BMP制造工厂，用于获得来自图库或者照相机拍照所生成的图片。并可以剪切 */
//	private CreateBmpFactory mCreateBmpFactory;
//	private ImageButton mImageButton;
//	public class MyGridViewAdapter extends BaseMyAdapter<String> {
//		/** 布局填充器 */
//		private LayoutInflater inflater;
//		public MyGridViewAdapter(Context context, List<String> list) {
//			super(context, list);
//			// TODO Auto-generated constructor stub
//			inflater = LayoutInflater.from(AddProductActivity.this);
//		}
//
//		@Override
//		public View getView(final int position, View convertView,
//							ViewGroup parent) {
//			View picView = inflater.inflate(R.layout.gv_item_pic, null);
//			final ImageButton picIBtn = (ImageButton) picView.findViewById(R.id.pic);
//
//			//显示图片的配置
//			if (newUrlList.size() > position) {
//				ImageUtils.setImageToImageButton(newUrlList.get(position), picIBtn, R.drawable.wodeimg_default);
//				picIBtn.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//
//						Intent intent = new Intent(context,ImageActivity.class);
//						intent.putExtra("image_list", (Serializable) newUrlList);
//						intent.putExtra("image_position", position);
//						context.startActivity(intent);
//
//					}
//				});
//
//				picView.findViewById(R.id.delete).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
////								viewList.remove(position);
//								if (isNewProduct) {
//									picAddArray.remove(picAddArray.indexOf("\""+newUrlList.get(position)+"\""));
//								}else {
//									if (oldUrlList.contains(newUrlList.get(position))) {
//										String delId = picList.get(oldUrlList.indexOf(newUrlList.get(position))).getId();
//										if (picDelArray == null) {
//											picDelArray = new ArrayList<String>();
//										}
//										picDelArray.add(delId +"_"+picIvMap.get(delId));
//									}else {
//										picAddArray.remove(picAddArray.indexOf("\""+newUrlList.get(position)+"\""));
//									}
//								}
//								newUrlList.remove(position);
//								adapter.notifyDataSetChanged();
//								setGridViewHeightBasedOnChildren(gv);
//							}
//						});
//			}
//			return picView;
//		}
//	}
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
//				File file = new File(picPath);
//				String name = file.getName();
//
//				ArrayList<String> filePathList = new ArrayList<String>();
//				filePathList.add(picPath);
//				HttpMultipartPost post = new HttpMultipartPost(AddProductActivity.this, filePathList,new UploadCallBack() {
//
//					@Override
//					public void onSuccessListener(String result) {
//						// TODO Auto-generated method stub
//						if (!StringUtil.isEmpty(result)) {
//							newUrlList.add(0, result.substring(result.indexOf("/")));
//							if (picAddArray == null) {
//								picAddArray = new ArrayList<String>();
//							}
//							picAddArray.add("\"" + result.substring(result.indexOf("/")) + "\"");
////						viewList.add(0, bmp);
//							adapter.notifyDataSetChanged();
//							setGridViewHeightBasedOnChildren(gv);
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
//	public void setGridViewHeightBasedOnChildren(GridView gridView) {
//
//        ListAdapter gridAdapter = gridView.getAdapter();
//
//        if (gridAdapter == null) {
//            // pre-condition
//            return;
//        }
//
//        int totalHeight = 0;
//
//        for (int i = 0; i < (gridAdapter.getCount()-1)/4+1; i++) {
//        	View gridItem = gridAdapter.getView(i, null, gridView);
//        	gridItem.measure(0, 0); // 计算子项View 的宽高
//			totalHeight += gridItem.getMeasuredHeight();
////    		int reqWidth = ((LinearLayout)addView).getMeasuredHeight();
////            totalHeight += reqWidth;
//        }
//        ViewGroup.LayoutParams params = gridView.getLayoutParams();
//
//        params.height = totalHeight + CommonUtil.dip2px(context, 20);
//
//        gridView.setLayoutParams(params);
//
//    }
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
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
	    	ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					AddProductActivity.this.finish();
				}
			});
			return false;
	    }else {
	    	return super.onKeyDown(keyCode, event); 
		}
	}
}
