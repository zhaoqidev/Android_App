package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.domin.PositionListBean.PositionItem;

public class PositionListAdapter extends BaseMyAdapter<PositionItem> {
	private String mPosition;
	public PositionListAdapter(Context context, List<PositionItem> list, String mPosition) {
		super(context, list);
		this.setPosition(mPosition);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_position_item, null);
			holder = new ViewHolder();
			holder.tv_position = (TextView) view.findViewById(R.id.tv_position);
			holder.iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_position.setText(list.get(position).getContent());
		if (list.get(position).getId().equals(mPosition)) {
			holder.iv_choose.setVisibility(View.VISIBLE);
		}else {
			holder.iv_choose.setVisibility(View.GONE);
		}
		return view;
	}
	public void setPosition(String mPosition) {
		this.mPosition = mPosition;
	}
	private class ViewHolder{
		TextView tv_position;
		ImageView iv_choose;
	}
}
