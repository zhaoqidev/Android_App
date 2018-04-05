package cc.upedu.online.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import cc.upedu.online.OnlineApp;

/**
 * 加载图片工具类
 * 
 * @author Administrator
 * 
 */
public class ImageUtils {
	/**
	 * 加载图片并设置到ImageView上
	 * 
	 * @param picPath
	 *            ConstantsOnline.SERVER_IMAGEURL+picPath 图片路径
	 * @param imageView
	 *            显示图片的ImageView
	 * @param defaultImageId
	 *            加载中和加载失败的默认图片ID，如果没有则传入0
	 */
	public static void setImage(String picPath, ImageView imageView,
			int defaultImageId) {

		if (defaultImageId != 0) {
			OnlineApp.myApp.imageLoader.displayImage(
					ConstantsOnline.SERVER_IMAGEURL + picPath, imageView,
					OnlineApp.myApp.builder.showImageOnLoading(defaultImageId)
							.showImageOnFail(defaultImageId).build());

		} else {
			OnlineApp.myApp.imageLoader.displayImage(
					ConstantsOnline.SERVER_IMAGEURL + picPath, imageView,
					OnlineApp.myApp.builder.build());
		}
	}
	
	/**
	 * 加载图片并设置到ImageButton上
	 * 
	 * @param picPath
	 *            ConstantsOnline.SERVER_IMAGEURL+picPath 图片路径
	 * @param imageView
	 *            显示图片的ImageView
	 * @param defaultImageId
	 *            加载中和加载失败的默认图片ID，如果没有则传入0
	 */
	public static void setImageToImageButton(String picPath, ImageButton imageButton,
			int defaultImageId) {

		if (defaultImageId != 0) {
			OnlineApp.myApp.imageLoader.displayImage(
					ConstantsOnline.SERVER_IMAGEURL + picPath, imageButton,
					OnlineApp.myApp.builder.showImageOnLoading(defaultImageId)
							.showImageOnFail(defaultImageId).build());

		} else {
			OnlineApp.myApp.imageLoader.displayImage(
					ConstantsOnline.SERVER_IMAGEURL + picPath, imageButton,
					OnlineApp.myApp.builder.build());
		}
	}
	
	
	public static void setBackGroundImage(String picPath, final View view,
			final int defaultImageId) {
		ImageLoadingListener listener=new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
				view.setBackgroundResource(defaultImageId);
				
			}
			
			@SuppressLint("NewApi")
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bitmap) {
				view.setBackground(new BitmapDrawable(bitmap));
				
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		}; 
		
		OnlineApp.myApp.imageLoader.loadImage(picPath, listener);
	}
	
	/**
	 * 加载图片并设置到ImageView上,需要传入图片全路径
	 * 
	 * @param picPath
	 *           picPath 图片路径
	 * @param imageView
	 *            显示图片的ImageView
	 * @param defaultImageId
	 *            加载中和加载失败的默认图片ID，如果没有则传入0
	 */
	public static void setImageFullPath(String picPath, ImageView imageView,
			int defaultImageId) {

		if (defaultImageId != 0) {
			OnlineApp.myApp.imageLoader.displayImage( picPath, imageView,
					OnlineApp.myApp.builder.showImageOnLoading(defaultImageId)
							.showImageOnFail(defaultImageId).build());

		} else {
			OnlineApp.myApp.imageLoader.displayImage( picPath, imageView,
					OnlineApp.myApp.builder.build());
		}
	}
	

	/**
	 * 
	 * @param picPath
	 * @param imageView
	 * @param defaultImageId
	 
	@SuppressLint("NewApi")
	public static void setBackGroundImage(String picPath, ImageView imageView,
			int defaultImageId) {
		if (defaultImageId != 0) {
			Bitmap bitmap = OnlineApp.myApp.imageLoader
					.loadImageSync(ConstantsOnline.SERVER_IMAGEURL + picPath);
			if (bitmap != null) {
				imageView.setBackground(new BitmapDrawable(bitmap));
			} else {
				imageView.setBackgroundResource(defaultImageId);
			}

		} else {
			Bitmap bitmap = OnlineApp.myApp.imageLoader
					.loadImageSync(ConstantsOnline.SERVER_IMAGEURL + picPath);
			if (bitmap != null) {
				imageView.setBackground(new BitmapDrawable(bitmap));
			}
		}
	}*/
}
