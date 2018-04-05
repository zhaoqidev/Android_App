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
import cc.upedu.online.domin.DaoshiCourseBean.CourseItem;
import cc.upedu.online.utils.ImageUtils;

/**
 * 导师名片页面课程列表的adapter界面
 * @author Administrator
 *
 */
public class DaoshiCourseAdapter extends AbsRecyclerViewAdapter{

	public DaoshiCourseAdapter(Context context, List<CourseItem> list) {
		this.list = list;
		this.context = context;
		resId=R.layout.home_courseitem;
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {

		ImageView courseimage_item;
		TextView course_title;
		TextView course_time;//课程时长
		TextView course_count;//课程观看次数

		public MyViewHolder(View arg0) {
			super(arg0);
			courseimage_item=(ImageView) arg0.findViewById(R.id.courseimage_item);
			course_title=(TextView) arg0.findViewById(R.id.course_title);
			course_time=(TextView) arg0.findViewById(R.id.course_time);
			course_count=(TextView) arg0.findViewById(R.id.course_count);
		}
		
	}



	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
		MyViewHolder holder=(MyViewHolder) viewHolder;
		CourseItem item=(CourseItem) list.get(position);
		ImageUtils.setImage(item.logo, holder.courseimage_item, 0);
		holder.course_title.setText(item.name);
		holder.course_time.setText(item.lessonNum);
		holder.course_count.setText(item.viewcount);
		super.onBindViewHolder(viewHolder,position);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup arg0, int i) {
		View view = LayoutInflater.from(arg0.getContext()).inflate(
				resId, arg0, false);
		return new MyViewHolder(view);
	}

}
