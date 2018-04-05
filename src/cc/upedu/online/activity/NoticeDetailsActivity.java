package cc.upedu.online.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelBottomBaseActivity;
import cc.upedu.online.domin.NoticeItem;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.WebViewSettingUtils;
/**
 * 公告详情界面
 * @author Administrator
 *
 */
public class NoticeDetailsActivity extends TwoPartModelBottomBaseActivity {
	private List<NoticeItem> noticeList;
	private LinearLayout bt_up,bt_down;
	private int currentPosition;
	private WebView webview;
	private LinearLayout ll_nodata;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("公告详情");
	}
	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_notice_bottom, null);
		bt_up = (LinearLayout) view.findViewById(R.id.bt_up);
		bt_down = (LinearLayout) view.findViewById(R.id.bt_down);
		return view;
	}
	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_webview, null);
		noticeList = (List<NoticeItem>) getIntent().getSerializableExtra("noticeList");
		currentPosition = getIntent().getIntExtra("position", 0);
		ll_nodata = (LinearLayout) view.findViewById(R.id.ll_nodata);
		webview = (WebView) view.findViewById(R.id.wv);

		WebViewSettingUtils.setWebViewCommonAttrs(context, webview);
		return view;
	}
	@Override
	protected void initData() {
		if (StringUtil.isEmpty(noticeList.get(currentPosition).getId())) {
			ll_nodata.setVisibility(View.VISIBLE);
		}else {
			ll_nodata.setVisibility(View.GONE);
			if (!StringUtil.isEmpty(noticeList.get(currentPosition).getUrl())) {
				webview.loadUrl(noticeList.get(currentPosition).getUrl());
			}else {
				webview.loadUrl(ConstantsOnline.ARTICLE_DETAILS + noticeList.get(currentPosition).getId());
			}
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		bt_up.setOnClickListener(this);
		bt_down.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_up:
			if (currentPosition > 0) {
				currentPosition--;
				bt_down.setEnabled(true);
				webview.removeAllViews();
				initData();
			}else {
				ShowUtils.showMsg(context, "这是第一个公告!");
				bt_up.setEnabled(false);
			}
			break;
		case R.id.bt_down:
			if (currentPosition < noticeList.size() - 1) {
				currentPosition++;
				bt_up.setEnabled(true);
				webview.removeAllViews();
				initData();
			}else {
				ShowUtils.showMsg(context, "已经到最后一个公告了!");
				bt_down.setEnabled(false);
			}
			break;
		}
	}
}
