package cc.upedu.online.base;

import android.view.View;
import android.widget.LinearLayout;

import cc.upedu.online.R;
/**
 * 主题内容分成上下两部分的fragment
 * 两部分中上方部分高为高为填充父窗体,下方部分包裹内容
 * @author Administrator
 *
 */
public abstract class TwoPartModelTopNSVBaseFragment extends NSVBaseFragment {

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_twopartmodetop_nsv_base, null);
		LinearLayout ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
		LinearLayout ll_twelfth = (LinearLayout) view.findViewById(R.id.ll_twelfth);
		View topLayout = initTopLayout();
		if (topLayout != null) {
			ll_top.setVisibility(View.VISIBLE);
			ll_top.addView(topLayout);
		}else {
			ll_top.setVisibility(View.GONE);
		}
		View twelfthLayout = initTwelfthLayout();
		if (twelfthLayout != null) {
			ll_twelfth.addView(twelfthLayout);
		}
		return view;
	}
	/**
	 * 初始化上方包裹内容的布局view
	 * @return
	 */
	public abstract View initTopLayout();
	/**
	 * 初始化下方填充父窗体的布局view
	 * @return
	 */
	public abstract View initTwelfthLayout();
}
