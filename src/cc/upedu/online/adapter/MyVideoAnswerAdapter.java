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
import cc.upedu.online.domin.VideoAnswerListBean.Entity.VideoAnswerItem;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.StringUtil;


public class MyVideoAnswerAdapter extends AbsRecyclerViewAdapter{
	private List<VideoAnswerItem> answerVideoList;

	
	public MyVideoAnswerAdapter(Context context, List<VideoAnswerItem> answerVideoList){
		this.context=context;
		this.answerVideoList=answerVideoList;
		resId=R.layout.layout_videoanswer_item;
	}
	@Override
	public int getItemCount() {
		// +1个公告栏
		return answerVideoList.size();
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView iv_doc;
		TextView tv_title;
		TextView tv_content;
		TextView tv_createTime;
		

		public MyViewHolder(View view) {
			super(view);
			iv_doc = (ImageView) view.findViewById(R.id.iv_doc);
			tv_title = (TextView) view.findViewById(R.id.tv_title);
			tv_content = (TextView) view.findViewById(R.id.tv_content);
			tv_createTime = (TextView) view.findViewById(R.id.tv_createTime);
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
		MyViewHolder holder=(MyViewHolder) viewHolder;
		
			holder.tv_title.setText(answerVideoList.get(position).getTitle());
			holder.tv_createTime.setText(answerVideoList.get(position).getCreateTime());
			if (!StringUtil.isEmpty(answerVideoList.get(position).getContent())) {
				holder.tv_content.setText(answerVideoList.get(position).getContent());
			}
			if (!StringUtil.isEmpty(answerVideoList.get(0).getVideourl())) {
				SharedPreferencesUtil.getInstance().editPutString("currentMyVideoAnswerUrl", answerVideoList.get(0).getVideourl());
	
			}
//			holder.iv_doc.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					((MyStudyAnswerActivity)context).getMyVideoAnswerPager().playNewVideo(answerVideoList.get(position-1).getVideourl());
//				}
//			});
		
		super.onBindViewHolder(viewHolder, position);
	}


	
}
