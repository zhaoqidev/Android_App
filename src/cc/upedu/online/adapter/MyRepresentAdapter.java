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
import cc.upedu.online.domin.MyRepresentBean.CourseItem;
import cc.upedu.online.utils.ImageUtils;

/**
 * 我的代言界面adapter
 * @author Administrator
 *
 */
public class MyRepresentAdapter extends AbsRecyclerViewAdapter {
	
	public MyRepresentAdapter(Context context, List<CourseItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.activity_courser_item);
	}	
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView courseimage_item;
		TextView course_title;
		TextView theacher_Name;
		TextView apply_count;
		

		public MyViewHolder(View view) {
			super(view);
			courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
			course_title = (TextView) view.findViewById(R.id.course_title);
			theacher_Name = (TextView) view.findViewById(R.id.theacher_Name);
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
		ImageUtils.setImage(item.logo, holder.courseimage_item, R.drawable.img_course);
		holder.course_title.setText(item.name);
		holder.theacher_Name.setText(item.teacherName);
		holder.apply_count.setText(item.joinNum+"人");
		
		super.onBindViewHolder(viewHolder, position);	
	}

}
