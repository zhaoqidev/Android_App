package cc.upedu.online.activity;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.ZoomControls;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.ImageZoomState;
import cc.upedu.online.view.ImageZoomView;
import cc.upedu.online.view.SimpleImageZoomListener;
/**
 * 查看大图的界面
 * @author Administrator
 *
 */
public class ImageActivity extends Activity implements OnPageChangeListener {
	private Context context;
	/**
	 * 用于管理图片的滑动
	 */
	private ViewPager viewPager;

	/**
	 * 显示当前图片的页数
	 */
	private TextView pageText;

	private List<String> picList;
	private ImageZoomState zoomState;
	private SimpleImageZoomListener zoomListener;
	private ZoomControls zoomCtrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image);
		context = OnlineApp.myApp.context;
		picList = (List<String>) getIntent().getSerializableExtra("image_list");
//		for (int i = 0; i < picList.size(); i++) {
//			if (StringUtil.isEmpty(picList.get(i))) {
//				picList.remove(i);
//				i--;
//			}
//		}
		int imagePosition = getIntent().getIntExtra("image_position", 0);
		pageText = (TextView) findViewById(R.id.page_text);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter();
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(imagePosition);
		viewPager.setOnPageChangeListener(this);
		viewPager.setEnabled(false);
		// 设定当前的页数和总页数
		pageText.setText((imagePosition + 1) + "/" + picList.size());
		zoomState = new ImageZoomState();

		zoomListener = new SimpleImageZoomListener();

		zoomListener.setZoomState(zoomState);

		zoomCtrl = new ZoomControls(this, null);
	}

	/**
	 * 
	 * TODO<ViewPager的适配器>
	 * 
	 * @author ZhuZiQiang
	 * @data: 2014-4-4 下午3:54:43
	 * @version: V1.0
	 */
	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
//			String imagePath = picList.get(position).getPicPath();
//			Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//			if (bitmap == null) {
//				bitmap = BitmapFactory.decodeResource(getResources(),
//						R.drawable.ic_launcher);
//			}
			
			View view = LayoutInflater.from(ImageActivity.this).inflate(
					R.layout.image_layout, null);
			final ImageZoomView onlyImageView = (ImageZoomView) view
					.findViewById(R.id.image_view);
			onlyImageView.setImageZoomState(zoomState);
			onlyImageView.setOnTouchListener(zoomListener);
			if (!StringUtil.isEmpty(picList.get(position))) {
				/*Bitmap bitmap = BitmapUtils.loadImageDefault(context,
						context.getCacheDir(), ConstantsOnline.SERVER_IMAGEURL
						+ picList.get(position), new ImageCallback() {
					
					@Override
					public void loadImage(Bitmap bitmap, String imagePath) {
						onlyImageView.setImage(bitmap);
					}
				}, false);*/
				
				Bitmap bitmap=OnlineApp.myApp.imageLoader.loadImageSync(ConstantsOnline.SERVER_IMAGEURL + picList.get(position));
				if (bitmap != null) {
					onlyImageView.setImage(bitmap);
				}else {
					Bitmap decodeStream = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.wodeimg_default));
					onlyImageView.setImage(decodeStream);
					ShowUtils.showMsg(context, "图片请求失败!");
				}
			}else {
//				Bitmap decodeStream = BitmapFactory.decodeStream(context.getResources().openRawResource(R.drawable.wodeimg_default));
				Resources r = context.getResources();
				InputStream is = r.openRawResource(R.drawable.wodeimg_default);
				BitmapDrawable  bmpDraw = new BitmapDrawable(is);
				Bitmap bmp = bmpDraw.getBitmap();
				onlyImageView.setImage(bmp);
				ShowUtils.showMsg(context, "图片不存在!");
			}
			onlyImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getFinish();
				}
			});
			container.addView(view);
			return view;
		}

		@Override
		public int getCount() {
			return picList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			container.removeView(view);
		}

	}
	private void getFinish(){
		finish();
	}
	/**
	 * 获取图片的本地存储路径。
	 * 
	 * @param imageUrl
	 *            图片的URL地址。
	 * @return 图片的本地存储路径。
	 */
	private String getImagePath(String imageUrl) {
		int lastSlashIndex = imageUrl.lastIndexOf("/");
		String imageName = imageUrl.substring(lastSlashIndex + 1);
		String imageDir = Environment.getExternalStorageDirectory().getPath()
				+ "/PhotoWallFalls/";
		File file = new File(imageDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		String imagePath = imageDir + imageName;
		return imagePath;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int currentPage) {
		// 每当页数发生改变时重新设定一遍当前的页数和总页数
		pageText.setText((currentPage + 1) + "/" + picList.size());
	}
}
