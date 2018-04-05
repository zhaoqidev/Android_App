package cc.upedu.online.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.UserLearningRecordsBean.Entity.RecordsItem;
import cc.upedu.online.utils.ImageUtils;

public class MyRecordsListAdapter extends AbsRecyclerViewAdapter {
	
	public MyRecordsListAdapter(Context context, List<RecordsItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.layout_records_item);
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView courseimage_item;
		TextView course_title;
		TextView section_Name;
		TextView theacher_Name;
		TextView course_count;
		TextView apply_count;
		

		public MyViewHolder(View view) {
			super(view);
			courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
			course_title = (TextView) view.findViewById(R.id.course_title);
			section_Name = (TextView) view.findViewById(R.id.section_Name);
			theacher_Name = (TextView) view.findViewById(R.id.theacher_Name);
			course_count = (TextView) view.findViewById(R.id.course_count);
			apply_count = (TextView) view.findViewById(R.id.apply_count);

		}

	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		final RecordsItem item = (RecordsItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		ImageUtils.setImage(item.getLogo(), holder.courseimage_item, R.drawable.img_course);
		holder.course_title.setText(item.getCourseName());
		holder.section_Name.setText(item.getKpointName());
		holder.theacher_Name.setText(item.getTeacherName());
		holder.course_count.setText(item.getPlayercount());
		
		super.onBindViewHolder(viewHolder, position);	
	}
}
