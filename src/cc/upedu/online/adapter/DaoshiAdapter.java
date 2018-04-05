package cc.upedu.online.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.DaoshiBean.TeacherItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.view.RoundedImageView.RoundedImageView;
/**
 * 发现——>导师的adapter
 * @author Administrator
 *
 */
public class DaoshiAdapter extends AbsRecyclerViewAdapter {

	public DaoshiAdapter(Context context, List<TeacherItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.pager_teacher_item);
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		RoundedImageView teacher_image_item;
		TextView teacher_name;
		TextView teacher_work;
		TextView tv_course_number;
		TextView tv_article_number;
		

		public MyViewHolder(View view) {
			super(view);
			teacher_image_item = (RoundedImageView) view.findViewById(R.id.teacher_image_item);
			teacher_name = (TextView) view.findViewById(R.id.teacher_name);
			teacher_work = (TextView) view.findViewById(R.id.teacher_work);
			tv_course_number = (TextView) view.findViewById(R.id.tv_course_number);
			tv_article_number = (TextView) view.findViewById(R.id.tv_article_number);
		}

	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}
	@SuppressLint("NewApi") @Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		TeacherItem item = (TeacherItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		ImageUtils.setImage(item.picPath, holder.teacher_image_item, R.drawable.teacher_images);

		holder.teacher_name.setText(item.name);
		holder.teacher_work.setText(item.intro);
		holder.tv_course_number.setText(item.courseNum);
		holder.tv_article_number.setText(item.articleNum);
		
		super.onBindViewHolder(viewHolder, position);	
	}

}
