package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.domin.CourseAssessListBean.Entity.CourseAssessItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.CircleImageView;

public class CourseInspirationAdapter extends AbsRecyclerViewAdapter {
	
	public CourseInspirationAdapter(Context context, List<CourseAssessItem> list) {
		this.list = list;
		this.context = context;
		resId=R.layout.layout_courseinspiration_item;
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		MyViewHolder holder=(MyViewHolder) viewHolder;
		final CourseAssessItem item=(CourseAssessItem) list.get(position);
		
		if (!StringUtil.isEmpty(item.getAvatar())) {
			ImageUtils.setImage(item.getAvatar(), holder.inspiration_image_item, R.drawable.left_menu_head);
		}else {
			holder.inspiration_image_item.setImageResource(R.drawable.left_menu_head);
		}
		holder.inspiration_name.setText(item.getRealname());
		holder.inspiration_time.setText(item.getCreateTime());
		holder.inspiration_doc.setText(item.getContent());
		holder.inspiration_image_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(UserStateUtil.isLogined()){
					Intent intent = new Intent(context, UserShowActivity.class);
					intent.putExtra("userId", item.getUserId());
					context.startActivity(intent);
				}else {
					UserStateUtil.NotLoginDialog(context);
				}
			}
		});
		
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}

	
	class MyViewHolder extends RecyclerView.ViewHolder {

		CircleImageView inspiration_image_item;
		TextView inspiration_name;
		TextView inspiration_time;
		TextView inspiration_doc;

		public MyViewHolder(View view) {
			super(view);
			inspiration_image_item = (CircleImageView) view.findViewById(R.id.inspiration_image_item);
			inspiration_name = (TextView) view.findViewById(R.id.inspiration_name);
			inspiration_time = (TextView) view.findViewById(R.id.inspiration_time);
			inspiration_doc = (TextView) view.findViewById(R.id.inspiration_doc);
		}

	}
	
	
	
}
