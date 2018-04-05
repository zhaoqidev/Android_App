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
import cc.upedu.online.domin.CourseItem;
import cc.upedu.online.utils.ImageUtils;

public class CourseListAdapter extends AbsRecyclerViewAdapter {
	
	public CourseListAdapter(Context context, List<CourseItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.fragment_courser_item);
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView courseimage_item;
		TextView course_title;
		TextView theacher_Name;
		TextView course_doc;
		TextView apply_count;
		

		public MyViewHolder(View view) {
			super(view);
			courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
			course_title = (TextView) view.findViewById(R.id.course_title);
			theacher_Name = (TextView) view.findViewById(R.id.theacher_Name);
			course_doc = (TextView) view.findViewById(R.id.course_doc);
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
		CourseItem item = (CourseItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		ImageUtils.setImage(item.getLogo(), holder.courseimage_item, 0);
		holder.course_title.setText(item.getName());
		holder.theacher_Name.setText(item.getTeacherList().get(0));
		holder.course_doc.setText(item.getTitle());
		
		super.onBindViewHolder(viewHolder, position);	
	}
	
	

}
