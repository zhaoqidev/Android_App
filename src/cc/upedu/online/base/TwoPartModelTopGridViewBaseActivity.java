package cc.upedu.online.base;

import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import cc.upedu.online.R;
/**
 * 基于TwoPartModelTopActivity,下方部分为gridview上方为包裹内容的布局
 * @author Administrator
 *
 */
public abstract class TwoPartModelTopGridViewBaseActivity extends TwoPartModelTopBaseActivity{
	/**
	 * gridview对象
	 */
	private GridView gv;
	/**
	 * gridview的adapter
	 */
	private BaseAdapter adapter;
	
	
	@Override
	public View initTwelfthLayout() {
		View view = View.inflate(context, R.layout.layout_gridview, null);
		gv = (GridView) view.findViewById(R.id.gridView);
		return view;
	}
	
	public void setGridView(BaseAdapter adapter){
		this.adapter = adapter;
		gv.setAdapter(adapter);
	}
	public void setOnItemClickListion(OnItemClickListener listener){
		if (listener != null) {
			gv.setOnItemClickListener(listener);
		}
	}
	/**
	 * 设置GridView的每一行个数
	 * @param num
	 */
	public void setNumColumns(int num) {
		gv.setNumColumns(num);
	}
	public void notifyData() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}
	public boolean isAdapterEmpty() {
		// TODO Auto-generated method stub
		if (adapter == null) {
			return true;
		}else {
			return false;
		}
	}
}
