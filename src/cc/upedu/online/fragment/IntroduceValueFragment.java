package cc.upedu.online.fragment;

import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import cc.upedu.online.R;
import cc.upedu.online.base.NSVBaseFragment;
import cc.upedu.online.domin.CourseIntroduceBean.Entity.Course;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.WebViewSettingUtils;
/**
 * 课程介绍界面中的课程价值Fragment
 * @author Administrator
 *
 */
public class IntroduceValueFragment extends NSVBaseFragment {
	private WebView wv;
	private LinearLayout ll_nodata;
	private Course course;
	
	@Override
	public void initData() {
		if (!StringUtil.isEmpty(course.getValue())) {
			if (StringUtil.isEmpty(wv.getUrl())) {
				wv.loadDataWithBaseURL(null, course.getValue(), "text/html", "utf-8", null);
			}
		}else {
			ll_nodata.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		course=(Course) PreferencesObjectUtil.readObject("IntroduceCourse", context);
		View view = View.inflate(context, R.layout.layout_webview, null);
		wv = (WebView) view.findViewById(R.id.wv);
		ll_nodata = (LinearLayout) view.findViewById(R.id.ll_nodata);
		WebViewSettingUtils.setWebViewCommonAttrs(context, wv);
		return view;
	}
}
