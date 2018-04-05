package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.domin.NoticeItem;

public class NoticeAdapter extends BaseMyAdapter<NoticeItem> {
	
	public NoticeAdapter(Context context, List<NoticeItem> noticeList) {
		super(context, noticeList);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_notice_item, null);
			holder = new ViewHolder();
			holder.notice_name = (TextView) view.findViewById(R.id.notice_name);
			holder.notice_time = (TextView) view.findViewById(R.id.notice_time);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.notice_name.setText(list.get(position).getTitle());
		holder.notice_time.setText(list.get(position).getUpdateTime());
		return view;
	}
	
	private class ViewHolder{
		TextView notice_name;
		TextView notice_time;
	}
}
