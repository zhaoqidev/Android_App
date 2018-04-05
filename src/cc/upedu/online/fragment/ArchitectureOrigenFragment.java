package cc.upedu.online.fragment;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.WebViewSettingUtils;
/**
 * 课程体系中的缘起界面
 * @author Administrator
 *
 */
public class ArchitectureOrigenFragment extends BaseFragment {
	private String url;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				break;
			}
		}

	};

	private WebView wv;
	public ArchitectureOrigenFragment(Context context,String url) {
		super();
		this.url = url;
	}

	@Override
	public void initData() {
		//待修改
		if (StringUtil.isEmpty(wv.getUrl())) {
			wv.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);
		}
//		scrollview.fullScroll(ScrollView.FOCUS_UP);
	}

	@Override
	public View initView(LayoutInflater inflater) {
		View view = View.inflate(context, R.layout.layout_webview, null);
		wv = (WebView) view.findViewById(R.id.wv);
		WebViewSettingUtils.setWebViewCommonAttrs(context, wv);
		return view;
	}
}
