package cc.upedu.online.base;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import cc.upedu.online.R;
/**
 * 
 * @author Administrator
 *
 */
public abstract class TwoPartModelBottomBaseFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub

		View view = View.inflate(context, R.layout.layout_twopartmodebottom_base, null);
		LinearLayout ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
		LinearLayout ll_twelfth = (LinearLayout) view.findViewById(R.id.ll_twelfth);
		
		View bottomLayout = initBottomLayout();
		View twelfthLayout = initTwelfthLayout();
		if (bottomLayout != null) {
			ll_bottom.addView(bottomLayout);
		}
		if (twelfthLayout != null) {
			ll_twelfth.addView(twelfthLayout);
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
