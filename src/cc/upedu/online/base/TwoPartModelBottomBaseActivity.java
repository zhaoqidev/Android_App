package cc.upedu.online.base;

import android.view.View;
import android.widget.LinearLayout;

import cc.upedu.online.R;
/**
 * 在确定titlebar的基础上把activity的主题内容分成上下两部分的activity
 * 两部分中上方部分高为高为填充父窗体,下方部分包裹内容
 * @author Administrator
 *
 */
public abstract class TwoPartModelBottomBaseActivity extends TitleBaseActivity {

	@Override
	protected View initContentView() {

		View view = View.inflate(context, R.layout.layout_twopartmodebottom_base, null);
		LinearLayout ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
		LinearLayout ll_twelfth = (LinearLayout) view.findViewById(R.id.ll_twelfth);
		
		View bottomLayout = initBottomLayout();
		View twelfthLayout = initTwelfthLayout();
		if (twelfthLayout != null) {
			ll_twelfth.addView(twelfthLayout);
		}
		if (bottomLayout != null) {
			ll_bottom.addView(bottomLayout);
		}else {
			ll_bottom.setVisibility(View.GONE);
		}
		return view;
	}

	/**
	 * 初始化下方包裹内容的布局view
	 * @return
	 */
	public abstract View initBottomLayout();

	/**
	 * 初始化上方填充父窗体的布局view
	 * @return
	 */
	public abstract View initTwelfthLayout();
}
