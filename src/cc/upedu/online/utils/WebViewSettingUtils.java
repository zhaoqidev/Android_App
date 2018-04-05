package cc.upedu.online.utils;

import android.content.Context;
import android.os.Build;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewSettingUtils {

	public static void setWebViewCommonAttrs(Context context, WebView view) {
//		webview.getSettings().setDefaultZoom(ZoomDensity.FAR);
		view.getSettings().setJavaScriptEnabled(true);
		view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//		view.getSettings().setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
		view.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		view.getSettings().setLoadWithOverviewMode(true);
		view.getSettings().setUseWideViewPort(true);
		//设置触摸缩放并把缩放工具隐藏
		view.getSettings().setSupportZoom(true);
		view.getSettings().setBuiltInZoomControls(true);
		view.getSettings().setDisplayZoomControls(false);
		if (Build.VERSION.SDK_INT >= 19) {
			view.getSettings().setLoadsImagesAutomatically(true);
		} else {
			view.getSettings().setLoadsImagesAutomatically(false);
		}
		view.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if (!view.getSettings().getLoadsImagesAutomatically()) {
					view.getSettings().setLoadsImagesAutomatically(true);
				}
			}
		});
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//			view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//		}
	}
}
