package cc.upedu.online.base;

import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import cc.upedu.online.R;
/**
 * 基于TwoPartModelBottomActivity,上方部分为listview下方为包裹内容的布局
 * @author Administrator
 * @param <T>
 *
 */
public abstract class TwoPartModelListBottomBaseActivity<T> extends TwoPartModelBottomBaseActivity {
	private ListView lv_base;
	private LinearLayout ll_nocontent;
	private BaseMyAdapter<T> adapter;
	
	@Override
	public View initTwelfthLayout() {
		View view = View.inflate(context, R.layout.layout_listview, null);
		lv_base = (ListView) view.findViewById(R.id.lv_base);
		ll_nocontent = (LinearLayout) view.findViewById(R.id.ll_nocontent);
		return view;
	}
	public void setListView(BaseMyAdapter<T> adapter) {
		this.adapter = adapter;
		if (adapter != null && adapter.getCount() > 0) {
			setNocontentVisibility(View.GONE);
			lv_base.setAdapter(adapter);
		}else {
			if (adapter != null) {
				lv_base.setAdapter(adapter);
			}
			setNocontentVisibility(View.VISIBLE);
		}
	}
	public void setOnItemClickListion(OnItemClickListener listener){
		if (listener != null) {
			lv_base.setOnItemClickListener(listener);
		}
	}
	public void setNocontentVisibility(int visibility){
		ll_nocontent.setVisibility(visibility);
	}
	public void notifyData() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
		if (adapter.getCount() > 0) {
			setNocontentVisibility(View.GONE);
		}else {
			setNocontentVisibility(View.VISIBLE);
		}
	}
	public boolean isAdapterEmpty() {
		// TODO Auto-generated method stub
		if (adapter == null) {
			return true;
		}else {
			return false;
		}
	}
	public BaseMyAdapter<T> getAdapter(){
		return adapter;
	}
}
