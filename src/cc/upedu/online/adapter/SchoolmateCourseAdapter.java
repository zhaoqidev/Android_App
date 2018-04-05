package cc.upedu.online.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.SchoolmateOfCourseBean.SchoolmateItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

/**
 * 全部学友的adapter
 * @author Administrator
 *
 */
public class SchoolmateCourseAdapter extends AbsRecyclerViewAdapter {
	
	public SchoolmateCourseAdapter(Context context, List<SchoolmateItem> list) {
		this.context=context;
		this.list=list;
		resId=R.layout.pager_schoolmatecourse_item;
	}

	
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		final SchoolmateItem item = (SchoolmateItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		if (!StringUtil.isEmpty(item.avatar)) {
			ImageUtils.setImage(item.avatar, holder.teacher_image, R.drawable.left_menu_head);
		}else {
			holder.teacher_image.setImageResource(R.drawable.left_menu_head);
		}
		holder.teacher_name.setText(item.name);
		if(StringUtil.isEmpty(item.company)){
			holder.tv_company.setText("未公开");
		}else {
			holder.tv_company.setText(item.company);
		}
		
		if(StringUtil.isEmpty(item.city)){
			holder.tv_position.setText("未公开");
		}else {
			holder.tv_position.setText(item.city);
		}
		super.onBindViewHolder(viewHolder, position);	
	}
	
	

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		CircleImageView teacher_image;
		TextView teacher_name;
		TextView tv_company;
		TextView tv_position;
		
		public MyViewHolder(View view) {
			super(view);
			teacher_image = (CircleImageView) view.findViewById(R.id.teacher_image);
			teacher_name = (TextView) view.findViewById(R.id.teacher_name);
			tv_company = (TextView) view.findViewById(R.id.tv_company);
			tv_position = (TextView) view.findViewById(R.id.tv_position);
		}

	}
}
