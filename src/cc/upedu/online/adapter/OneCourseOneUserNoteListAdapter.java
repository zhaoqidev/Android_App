package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.MyNoteOneCourseDetailsActivity;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.domin.NoteItem;

public class OneCourseOneUserNoteListAdapter extends BaseMyAdapter<NoteItem> {
	String courseId;
	public OneCourseOneUserNoteListAdapter(Context context, List<NoteItem> list, String courseId) {
		super(context, list);
		// TODO Auto-generated constructor stub
		this.courseId = courseId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_onecoursenote_item, null);
			holder = new ViewHolder();
			holder.topview = (View) view.findViewById(R.id.topview);
			holder.note_title = (TextView) view.findViewById(R.id.note_title);
			holder.note_content = (TextView) view.findViewById(R.id.note_content);
			holder.iv_read = (ImageView) view.findViewById(R.id.iv_read);
			holder.iv_edit = (ImageView) view.findViewById(R.id.iv_edit);
			holder.note_time = (TextView) view.findViewById(R.id.note_time);
			holder.line = (View) view.findViewById(R.id.line);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		if (0 == position) {
			holder.topview.setVisibility(View.VISIBLE);
		}else {
			holder.topview.setVisibility(View.GONE);
		}
		if (position == getCount() - 1) {
			holder.line.setVisibility(View.INVISIBLE);
		}else {
			holder.line.setVisibility(View.VISIBLE);
		}
		holder.note_title.setText(list.get(position).getKpointName());
		holder.note_content.setText(list.get(position).getCourseContent());
		holder.note_time.setText(list.get(position).getModifyTime());
		holder.iv_read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MyNoteOneCourseDetailsActivity.class);
				intent.putExtra("noteList", (Serializable)list);
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		holder.iv_edit.setVisibility(View.GONE);
		return view;
	}
	
	private class ViewHolder{
		View topview;
		TextView note_title;
		TextView note_content;
		ImageView iv_read;
		ImageView iv_edit;
		TextView note_time;
		View line;
	}
}
