package cc.upedu.online.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CommonUtil {
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
		int picWidth = bitmap.getWidth();
		int picHeight = bitmap.getHeight();
		int picScreenHeight = 0;
		picScreenHeight = (screenWidth * picHeight) / picWidth;
		return picScreenHeight;
	}

	/**
	 * 
	 * @param context
	 * @param button
	 * @param nornalImageFileName
	 * @param pressedImageFileName
	 * @throws Exception
	 */
	public static void bindViewSelector(Context context, final View view,
			String nornalImageurl, final String pressedImageUrl) {
		final StateListDrawable stateListDrawable = new StateListDrawable();
		final BitmapUtils utils = new BitmapUtils(context);
		utils.display(view, nornalImageurl, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View container, String uri,
					Bitmap bitmap, BitmapDisplayConfig config,
					BitmapLoadFrom from) {
				Drawable normalDrawable = new BitmapDrawable(bitmap);
				stateListDrawable.addState(
						new int[] { android.R.attr.state_active },
						normalDrawable);
				stateListDrawable.addState(new int[] {
						android.R.attr.state_focused,
						android.R.attr.state_enabled }, normalDrawable);
				stateListDrawable.addState(
						new int[] { android.R.attr.state_enabled },
						normalDrawable);
				utils.display(container, pressedImageUrl,
						new BitmapLoadCallBack<View>() {

							@Override
							public void onLoadCompleted(View container,
									String uri, Bitmap bitmap,
									BitmapDisplayConfig config,
									BitmapLoadFrom from) {
								stateListDrawable.addState(new int[] {
										android.R.attr.state_pressed,
										android.R.attr.state_enabled },
										new BitmapDrawable(bitmap));

								view.setBackgroundDrawable(stateListDrawable);

							}

							@Override
							public void onLoadFailed(View container,
									String uri, Drawable drawable) {
								// TODO Auto-generated method stub

							}
						});
			}

			@Override
			public void onLoadFailed(View container, String uri,
					Drawable drawable) {

			}
		});

	}

	private static Drawable createDrawable(Drawable d, Paint p) {

		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

		return new BitmapDrawable(bitmap);
	}

	/** 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果 */
	public static StateListDrawable createSLD(Context context, Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		int brightness = 50 - 127;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, paint);
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed);
		bg.addState(new int[] { android.R.attr.state_focused, }, pressed);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	/**
	 * 从assets目录中获取图片
	 * @param ct
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
		Bitmap image = null;
		AssetManager am = ct.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}

	// public static <Params, Progress, Result> void executeAsyncTask(
	// AsyncTask<Params, Progress, Result> task, Params... params) {
	// if (Build.VERSION.SDK_INT >= 11) {
	// task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	// } else {
	// task.execute(params);
	// }
	// }

	// public static boolean hasToken(Context ct) {
	// String token = SharePrefUtil.getString(ct, "token", "");
	// if (StringUtil.isEmpty(token)) {
	// return false;
	// } else {
	// return true;
	// }
	// }

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	public static void setGridViewHeightBasedOnChildren(Context context,GridView gridView,int numColumns) { 

        ListAdapter gridAdapter = gridView.getAdapter();  

        if (gridAdapter == null) { 
            // pre-condition 
            return; 
        } 

        View gridItem = gridAdapter.getView(0, null, gridView);
        gridItem.measure(0, 0);
        int totalHeight = gridItem.getMeasuredHeight()*((gridAdapter.getCount()-1)/numColumns+1);
        
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + CommonUtil.dip2px(context, 20); 
        gridView.setLayoutParams(params); 

    }

	public static void saveMyBitmap(Bitmap mBitmap,String picPath)  {
        File f = new File(picPath);
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
                e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
                fOut.flush();
        } catch (IOException e) {
                e.printStackTrace();
        }
        try {
                fOut.close();
        } catch (IOException e) {
                e.printStackTrace();
        }
}
}
