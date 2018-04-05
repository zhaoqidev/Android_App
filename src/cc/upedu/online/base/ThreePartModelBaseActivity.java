package cc.upedu.online.base;

import android.view.View;
import android.widget.LinearLayout;

import cc.upedu.online.R;
/**
 * 在确定titlebar的基础上把activity的主题内容分成上中下三部分的activity
 * 三部分中上方部分高为包裹内容,中间部分高为填充父窗体,下方部分包裹内容
 * @author Administrator
 *
 */
public abstract class ThreePartModelBaseActivity extends TitleBaseActivity {

	private View bottomLayout;

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_threepartmode_base, null);
		LinearLayout ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
		LinearLayout ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
		LinearLayout ll_twelfth = (LinearLayout) view.findViewById(R.id.ll_twelfth);
		
		View topLayout = initTopLayout();
		bottomLayout = initBottomLayout();
		View twelfthLayout = initTwelfthLayout();
		if (topLayout != null) {
			ll_top.addView(topLayout);
		}else {
			ll_top.setVisibility(View.GONE);
		}
		if (bottomLayout != null) {
			ll_bottom.addView(bottomLayout);
		}else {
			ll_bottom.setVisibility(View.GONE);
		}
		if (twelfthLayout != null) {
			ll_twelfth.addView(twelfthLayout);
		}else {
			ll_twelfth.setVisibility(View.GONE);
		}
		return view;
	}
	/**
	 * 初始化上方包裹内容的布局view
	 * @return
	 */
	public abstract View initTopLayout();
	/**
	 * 初始化下方包裹内容的布局view
	 * @return
	 */
	public abstract View initBottomLayout();
	/**
	 * 初始化中间填充父窗体的布局view
	 * @return
	 */
	public abstract View initTwelfthLayout();

	public void setBottomLayoutVisibility(int visibility){
		bottomLayout.setVisibility(visibility);
	}
}
