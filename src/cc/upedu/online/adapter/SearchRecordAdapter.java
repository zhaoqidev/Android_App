package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;

public class SearchRecordAdapter extends BaseMyAdapter<String> {

	public SearchRecordAdapter(Context context, List<String> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_searchrecord_item, null);
			holder = new ViewHolder();
			holder.tv_searchrecord = (TextView) view.findViewById(R.id.tv_searchrecord);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		if (0 == position) {
			holder.tv_searchrecord.setText("查找全部");
		}else {
			holder.tv_searchrecord.setText(list.get(position-1));
		}
		return view;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size() + 1;
	}
	private class ViewHolder{
		TextView tv_searchrecord;
	}
}
