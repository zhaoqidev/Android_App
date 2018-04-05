package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.AskForNoteActivity;
import cc.upedu.online.activity.AwardGCoinActivity;
import cc.upedu.online.domin.MyNoteRecordsBean.RecordItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;

/**
 * 我的索要笔记记录页面adapter
 * @author Administrator
 *
 */
public class MyNoteBlagRecordsAdapter extends AbsRecyclerViewAdapter {

	public MyNoteBlagRecordsAdapter(Context context, List<RecordItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.activity_note_blagrecords_item);
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView iv_user;
		TextView tv_username;
		TextView tv_notecontent;
		ImageView courseimage_item;
		TextView course_title;
		TextView tv_time;
		LinearLayout ll_reward;
		ImageView iv_reward;
		TextView tv_reward;
		

		public MyViewHolder(View view) {
			super(view);
			iv_user = (ImageView) view.findViewById(R.id.iv_user);
			tv_username = (TextView) view.findViewById(R.id.tv_username);
			tv_notecontent = (TextView) view.findViewById(R.id.tv_notecontent);
			courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
			course_title = (TextView) view.findViewById(R.id.course_title);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			ll_reward = (LinearLayout) view.findViewById(R.id.ll_reward);
			iv_reward = (ImageView) view.findViewById(R.id.iv_reward);
			tv_reward = (TextView) view.findViewById(R.id.tv_reward);

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
		final RecordItem item = (RecordItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		if (!StringUtil.isEmpty(item.getAvatar())) {
			ImageUtils.setImage(item.getAvatar(), holder.iv_user, R.drawable.wodeimg_default);
		}else {
			holder.iv_user.setImageResource(R.drawable.wodeimg_default);
		}
		holder.tv_username.setText(item.getUname());
		if (!StringUtil.isEmpty(item.getRecordContent())) {
			holder.tv_notecontent.setText(item.getRecordContent());
			holder.tv_notecontent.setVisibility(View.VISIBLE);
		}else {
			holder.tv_notecontent.setVisibility(View.GONE);
		}
		if (!StringUtil.isEmpty(item.getLogo())) {
			ImageUtils.setImage(item.getLogo(), holder.courseimage_item, R.drawable.img_course);
		}else {
			holder.courseimage_item.setImageResource(R.drawable.img_course);
		}
		holder.course_title.setText(item.getCourseName());
		holder.tv_time.setText(item.getRecordtime());
		
		if (!StringUtil.isEmpty(item.gettype())) {
			switch (Integer.valueOf(item.gettype())) {
			case 1://我向他人索要，他人未回复 （可再次索要）	显示索要		
				holder.iv_reward.setVisibility(View.VISIBLE);
				holder.iv_reward.setImageResource(R.drawable.note_suoyao);
				holder.tv_reward.setText("索要");
				holder.ll_reward.setClickable(true);
				holder.ll_reward.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//跳转到索要
						AskOrShare(item,"ask");
					}

				});
				break;
			case 2://我向他人索要，他人已经分享给我（打赏）	显示打赏
				holder.iv_reward.setVisibility(View.VISIBLE);
				holder.iv_reward.setImageResource(R.drawable.note_dashang);
				holder.tv_reward.setText("打赏");
				holder.ll_reward.setClickable(true);
				holder.ll_reward.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						aWardGCoin(item);
					}
				});
				break;
			case 3://他人向我索要，我已经分享，他人未打赏	显示未打赏
				holder.iv_reward.setVisibility(View.GONE);
				holder.tv_reward.setText("未打赏");
				holder.ll_reward.setClickable(false);
				break;
			case 4://他人向我索要，我已经分享，他人已打赏	显示已打赏
				holder.iv_reward.setVisibility(View.GONE);
				holder.tv_reward.setText("已打赏");
				holder.ll_reward.setClickable(false);
				break;
			case 5://他人向我索要，我未分享（分享笔记）		显示分享笔记	跳转到分享
				holder.iv_reward.setVisibility(View.VISIBLE);
				holder.iv_reward.setImageResource(R.drawable.note_fenxiang);
				holder.tv_reward.setText("分享笔记");
				holder.ll_reward.setClickable(true);
				holder.ll_reward.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//跳转到分享
						AskOrShare(item,"share");
					}
				});
				break;
			case 6://我分享给他人，他人未打赏				显示未打赏
				holder.iv_reward.setVisibility(View.GONE);
				holder.tv_reward.setText("未打赏");
				holder.ll_reward.setClickable(false);
				break;
			case 7://我分享给他人，他人已打赏				显示已打赏
				holder.iv_reward.setVisibility(View.GONE);
				holder.tv_reward.setText("已打赏");
				holder.ll_reward.setClickable(false);
				break;
			case 8://他人分享给我，我未打赏				显示打赏
				holder.iv_reward.setVisibility(View.VISIBLE);
				holder.iv_reward.setImageResource(R.drawable.note_dashang);
				holder.tv_reward.setText("打赏");
				holder.ll_reward.setClickable(true);
				holder.ll_reward.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						aWardGCoin(item);
					}
					
				});
				break;
			case 9://他人分享给我，我已打赏				显示再打赏
				holder.iv_reward.setVisibility(View.VISIBLE);
				holder.iv_reward.setImageResource(R.drawable.note_dashang);
				holder.tv_reward.setText("再赏");
				holder.ll_reward.setClickable(true);
				holder.ll_reward.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						aWardGCoin(item);
					}
				});
				break;
			}
		}else {
			holder.iv_reward.setVisibility(View.GONE);
			holder.tv_reward.setText("");
			holder.ll_reward.setClickable(false);
		}
		
		super.onBindViewHolder(viewHolder, position);	
	}
	
	/**
	 * 请求或者分享笔记
	 * @param item
	 */
	public void AskOrShare(RecordItem item,String type) {
		Intent intent=new Intent(context,AskForNoteActivity.class);
		intent.putExtra("type", type);//页面为赠送笔记还是索要笔记
		intent.putExtra("tuid", item.getTuid());
		intent.putExtra("tname", item.getUname());
		intent.putExtra("course_name", item.getCourseName());
		intent.putExtra("course_image", item.getLogo());
		intent.putExtra("course_id", item.getCourseId());
		context.startActivity(intent);
	}
	
	/**
	 * 打赏笔记
	 * @param item
	 */
	private void aWardGCoin(RecordItem item) {
		Intent intent = new Intent(context, AwardGCoinActivity.class);
		intent.putExtra("tuid", item.getTuid());
		intent.putExtra("uname", item.getUname());
		intent.putExtra("course_name", item.getCourseName());
		intent.putExtra("course_image", item.getLogo());
		intent.putExtra("recordId", item.getRecordId());
		context.startActivity(intent);
	}
}
