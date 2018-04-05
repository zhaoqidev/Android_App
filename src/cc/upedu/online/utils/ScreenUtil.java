package cc.upedu.online.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class ScreenUtil {

	private static DisplayMetrics mMetrics = new DisplayMetrics();
	private static ScreenUtil instance;
	private Context context;

	public static ScreenUtil getInstance(Context context) {
		if (instance == null)
			instance = new ScreenUtil(context);
		return instance;
	}

	private ScreenUtil(Context _context) {
		context = _context;
		mMetrics = context.getResources().getDisplayMetrics();
	}

	public int getScreenHeight() {
		return mMetrics.heightPixels;
	}

	public int getScreenWidth() {
		return mMetrics.widthPixels;
	}
}
