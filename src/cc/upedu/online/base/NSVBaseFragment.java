package cc.upedu.online.base;

import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;

import cc.upedu.online.R;

public abstract class NSVBaseFragment extends BaseFragment {

	@Override
	protected View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_nestedscrollview, null);
		NestedScrollView ll_nsv = (NestedScrollView) view.findViewById(R.id.ll_nsv);
		View contentView = initContentView();
		if (contentView != null) {
			ll_nsv.addView(contentView);
		}
		return view;
	}
	/**
	 * 初始title下方的内容view
	 */
	protected abstract View initContentView();
}
