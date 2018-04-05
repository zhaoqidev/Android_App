package cc.upedu.online.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.CourseStudyActivity;
import cc.upedu.online.domin.VideoAnswerListBean.Entity.VideoAnswerItem;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

public class VideoAnswerAdapter extends AbsRecyclerViewAdapter {
	View view;
	public VideoAnswerAdapter(Context context,List<VideoAnswerItem> list) {
		this.context = context;
		this.list = list;
		resId=R.layout.layout_videoanswer_item;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

		view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);

		return new MyViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		MyViewHolder holder=(MyViewHolder) viewHolder;
		final VideoAnswerItem item=(VideoAnswerItem) list.get(position);
		
		holder.tv_title.setText(item.getTitle());
		holder.tv_createTime.setText(item.getCreateTime());
		if (!StringUtil.isEmpty(item.getContent())) {
			holder.tv_content.setText(item.getContent().replace("<br />", ""));
		}
		view.setOnClickListener(new OnClickListener() {
			 @Override
			 public void onClick(View v) {
				 int node = -1;
				 if (UserStateUtil.isLogined()){
					 String answerId = item.getVideourl();
					 SharedPreferencesUtil.getInstance().editPutString("kpointId", answerId);
					 HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
					 if (map.containsKey("aid" + answerId)){
						 node = map.get("aid"+answerId);
					 }
				 }
				 ((CourseStudyActivity)context).playCourseVideo(item.getVideourl(),false,node);
			 }
		 });
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
}
