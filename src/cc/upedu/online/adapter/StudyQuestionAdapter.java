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
import cc.upedu.online.domin.QuestionListBean.Entity.QuestionItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

public class StudyQuestionAdapter extends AbsRecyclerViewAdapter {
	
	
	public StudyQuestionAdapter(Context context, List<QuestionItem> list) {
		this.list = list;
		this.context = context;
		setResId(R.layout.layout_studyquestiion_item);
	}
	
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		final QuestionItem item = (QuestionItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		if (!StringUtil.isEmpty(item.getAvatar())) {
			ImageUtils.setImage(item.getAvatar(), holder.inspiration_image_item, R.drawable.left_menu_head);
		}
		holder.inspiration_image_item.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserShowActivity.class);
				intent.putExtra("userId", item.getUserId());
				context.startActivity(intent);
			}
		});
		holder.inspiration_name.setText(item.getName());
		holder.inspiration_time.setText(item.getCreateTime());
		holder.inspiration_doc.setText(item.getContent());
		
	
		super.onBindViewHolder(viewHolder, position);	
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
