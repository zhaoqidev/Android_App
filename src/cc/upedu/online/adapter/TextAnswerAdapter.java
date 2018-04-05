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
import cc.upedu.online.domin.TextAnswerListBean.Entity.TextAnswerItem;

public class TextAnswerAdapter extends AbsRecyclerViewAdapter {

	
	public TextAnswerAdapter(Context context, List<TextAnswerItem> list) {
		this.list = list;
		this.context = context;
		resId = R.layout.layout_textanswer_item;
	}


	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		MyViewHolder holder=(MyViewHolder) viewHolder;
		TextAnswerItem item =(TextAnswerItem) list.get(position);
		holder.question_doc.setText(item.getTitle());
		holder.answer_time.setText(item.getCreateTime());
		holder.answer_doc.setText(item.getContent());
		super.onBindViewHolder(viewHolder, position);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		TextView question_doc;
		TextView answer_time;
		TextView answer_doc;

		public MyViewHolder(View view) {
			super(view);
			question_doc = (TextView) view.findViewById(R.id.question_doc);
			answer_time = (TextView) view.findViewById(R.id.answer_time);
			answer_doc = (TextView) view.findViewById(R.id.answer_doc);
		}

	}
}
