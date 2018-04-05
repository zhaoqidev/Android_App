package cc.upedu.online.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cc.upedu.online.R;
/**
 * 把fragment的主题内容分成上中下三部分
 * 三部分中上方部分高为包裹内容,中间部分高为填充父窗体,下方部分包裹内容
 * @author Administrator
 *
 */
public abstract class ThreePartModelBaseFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_threepartmode_base, null);
		LinearLayout ll_top = (LinearLayout) view.findViewById(R.id.ll_top);
		LinearLayout ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
		LinearLayout ll_twelfth = (LinearLayout) view.findViewById(R.id.ll_twelfth);
		
		View topLayout = initTopLayout();
		View bottomLayout = initBottomLayout();
		View twelfthLayout = initTwelfthLayout();
		if (topLayout != null) {
			ll_top.addView(topLayout);
		}
		if (bottomLayout != null) {
			ll_bottom.addView(bottomLayout);
		}
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
	 * 初始化下方包裹内容的布局view
	 * @return
	 */
	public abstract View initBottomLayout();
	/**
	 * 初始化中间填充父窗体的布局view
	 * @return
	 */
	public abstract View initTwelfthLayout();
}
