package cc.upedu.online.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.NoteListBean.Entity.NoticeItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

public class TelecastNoteAdapter extends AbsRecyclerViewAdapter {

	public TelecastNoteAdapter(Context context, List<NoticeItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.layout_studynote_item);
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		CircleImageView note_user_image;
		TextView note_user_name;
		TextView note_user_work;
		TextView praise_count;
		TextView note_time;
		TextView note_content;
		

		public MyViewHolder(View view) {
			super(view);
			note_user_image = (CircleImageView) view.findViewById(R.id.note_user_image);
			note_user_name = (TextView) view.findViewById(R.id.note_user_name);
			note_user_work = (TextView) view.findViewById(R.id.note_user_work);
			praise_count = (TextView) view.findViewById(R.id.praise_count);
			note_time = (TextView) view.findViewById(R.id.note_time);
			note_content = (TextView) view.findViewById(R.id.note_content);

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
		NoticeItem item = (NoticeItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		if (!StringUtil.isEmpty(item.getAvatar())) {
			ImageUtils.setImage(item.getAvatar(), holder.note_user_image, R.drawable.left_menu_head);
		}
		if (item.getRealName() == null) {
			holder.note_user_name.setText("未设置");
		}else {
			holder.note_user_name.setText(item.getRealName());
		}
		if (item.getPosition() == null) {
			holder.note_user_work.setText("未设置");
		}else {
			holder.note_user_work.setText(item.getPosition());
		}
//		if (item.getRealName() == null) {
//			holder.praise_count.setText("0");
//		}else {
//			holder.praise_count.setText(item.getUpdateTime());
//		}
		if (item.getUpdateTime() == null) {
			holder.note_time.setText("未知");
		}else {
			holder.note_time.setText(item.getUpdateTime());
		}

		holder.note_content.setText(item.getContent());
		holder.note_content.setMaxLines(2);
		holder.note_content.requestLayout();
		holder.note_content.setOnClickListener(new OnClickListener() {
			private boolean mState = false;//默认收起状态
			@Override
			public void onClick(View v) {
				if (mState == false) {  
					holder.note_content.setMaxLines(Integer.MAX_VALUE);  
				} else if (mState == true) {  
					holder.note_content.setMaxLines(2);  
				} 
				holder.note_content.requestLayout();
				mState = !mState;  
			}
		});
		
		super.onBindViewHolder(viewHolder, position);	
	}

	}
