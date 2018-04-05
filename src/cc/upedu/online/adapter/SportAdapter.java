package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.domin.SportsBean.ActivityItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ScreenUtil;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

public class SportAdapter extends AbsRecyclerViewAdapter{


	public SportAdapter(Context context, List<ActivityItem> list) {
		this.list = list;
		this.context = context;
		setResId(R.layout.pager_sport_item);
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView note_user_image;// 图片
		TextView note_user_name;// 标题：互联网众筹总裁班
		TextView note_time;// 时间
		TextView note_content;// 活动地址
		TextView tv_number;// 报名人数
		TextView tv_name;// 发起人名称
		CircleImageView civ_img;// 发起人照片
		

		public MyViewHolder(View view) {
			super(view);
			note_user_image = (ImageView) view.findViewById(R.id.note_user_image);
			int screenWidth = ScreenUtil.getInstance(context).getScreenWidth();
			LayoutParams para = note_user_image.getLayoutParams();
//			int width = screenWidth - CommonUtil.dip2px(context, 34);
//	        para.width = width;  
//	        para.height = width*494/890;  
//	        holder.note_user_image.setLayoutParams(para);
			note_user_name = (TextView) view.findViewById(R.id.note_user_name);
			note_time = (TextView) view.findViewById(R.id.note_time);
			note_content = (TextView) view.findViewById(R.id.note_content);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_number = (TextView) view.findViewById(R.id.tv_number);
			civ_img = (CircleImageView) view.findViewById(R.id.civ_img);

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
		final ActivityItem item = (ActivityItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		holder.note_user_name.setText(item.title);
		holder.note_time.setText(item.startDt);
		holder.note_content.setText(item.address);
		holder.tv_number.setText(item.joinNum);
		if(StringUtil.isEmpty(item.uname)){
			holder.tv_name.setText("发起人");
		}else {
			holder.tv_name.setText(item.uname);
		}
		if (!StringUtil.isEmpty(item.logo)) {
			
			ImageUtils.setImage(item.logo, holder.note_user_image, R.drawable.img_sportlist);
		}else {
			holder.note_user_image.setImageResource(R.drawable.img_sportlist);
		}
		if (!StringUtil.isEmpty(item.avatar)) {
			
			ImageUtils.setImage(item.avatar, holder.civ_img, R.drawable.teacher_images);
		}else {
			holder.note_user_image.setImageResource(R.drawable.teacher_images);
		}
		holder.civ_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, UserShowActivity.class);
				intent.putExtra("userId", item.uid);
				context.startActivity(intent);
			}
		});
		
		super.onBindViewHolder(viewHolder, position);	
	}

}
