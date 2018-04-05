package cc.upedu.online.fragment;

import android.content.Context;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelTopNSVBaseFragment;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.WebViewSettingUtils;

/**
 * 课程介绍界面中的课程价值pager
 * 
 * @author Administrator
 * 
 */
public class DaoshiIntroduceFragment extends TwoPartModelTopNSVBaseFragment{
	//导师荣誉
	private WebView wv;
	private TextView tv_introduce;

	public DaoshiIntroduceFragment(Context context) {
		super();
	}

	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.pager_daoshi_introduce, null);
		tv_introduce= (TextView) view.findViewById(R.id.tv_introduce);
		return view;
	}

	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_webview, null);
		wv = (WebView) view.findViewById(R.id.wv);
		WebViewSettingUtils.setWebViewCommonAttrs(context, wv);
		return view;
	}
	@Override
	public void initData() {

		String introduct = SharedPreferencesUtil.getInstance().spGetString("introduct");
		String honors = SharedPreferencesUtil.getInstance().spGetString("honors");
		if (StringUtil.isEmpty(wv.getUrl())) {
			wv.loadDataWithBaseURL(null, honors, "text/html", "utf-8", null);
		}
		tv_introduce.setText(introduct);

		
	}
}
