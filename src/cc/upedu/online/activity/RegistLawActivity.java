package cc.upedu.online.activity;

import android.view.View;
import android.webkit.WebView;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.utils.WebViewSettingUtils;

/**
 * 注册协议页面
 * 
 * @author Administrator
 * 
 */
public class RegistLawActivity extends TitleBaseActivity {
	private WebView wv;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("成长吧协议");
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_webview, null);		
		wv = (WebView) view.findViewById(R.id.wv);
		WebViewSettingUtils.setWebViewCommonAttrs(context, wv);
		return view;
	}

	@Override
	protected void initData() {
		wv.loadUrl("http://www.upedu.cc/static/register_contract.html");
	}
}
