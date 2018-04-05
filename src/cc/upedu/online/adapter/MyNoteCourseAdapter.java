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
import cc.upedu.online.domin.MyNoteCourseBean.ClassItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;

/**
 * 我的笔记界面adapter
 * @author Administrator
 *
 */
public class MyNoteCourseAdapter extends AbsRecyclerViewAdapter{

	public MyNoteCourseAdapter(Context context, List<ClassItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.activity_mynote_courser_item);
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView courseimage_item;
		TextView tv_note_course;
		TextView tv_note_time;
		TextView tv_note_theacher;
		

		public MyViewHolder(View view) {
			super(view);
			courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
			tv_note_course = (TextView) view.findViewById(R.id.tv_note_course);
			tv_note_time = (TextView) view.findViewById(R.id.tv_note_time);
			tv_note_theacher = (TextView) view.findViewById(R.id.tv_note_theacher);

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
		ClassItem item = (ClassItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		if (!StringUtil.isEmpty(item.getLogo())) {
			ImageUtils.setImage(item.getLogo(), holder.courseimage_item, R.drawable.img_course);
		}else {
			holder.courseimage_item.setImageResource(R.drawable.img_course);
		}
		holder.tv_note_course.setText(item.getCourseName());
		holder.tv_note_time.setText(item.getModifyTime());
		holder.tv_note_theacher.setText(item.getTeacherName());
		
		super.onBindViewHolder(viewHolder, position);	
	}

}
