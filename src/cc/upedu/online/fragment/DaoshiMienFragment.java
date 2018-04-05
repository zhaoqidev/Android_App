package cc.upedu.online.fragment;

import android.content.Context;
import android.view.View;

import java.util.List;

import cc.upedu.online.adapter.DaoshiMienAdapter;
import cc.upedu.online.base.GridBaseFragment;
import cc.upedu.online.utils.PreferencesObjectUtil;

/**
 * 导师名片页面的导师风采
 * 
 * @author Administrator
 * 
 */
public class DaoshiMienFragment extends GridBaseFragment<String> {
	public DaoshiMienFragment() {

	}

	public DaoshiMienFragment(Context context) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public void initData() {
		list = (List<String>) PreferencesObjectUtil.readObject("picArray",
				context);
		if (list.size() > 0) {
			adapter = new DaoshiMienAdapter(context, list);
			gridRecyclerView.setAdapter(adapter);
		} else {
			ll_nodata.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void onItemClick(View view, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemLongClick(View view, int position) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setGridRecyclerView() {
		setGridRecyclerView(3);
		
	}


}
