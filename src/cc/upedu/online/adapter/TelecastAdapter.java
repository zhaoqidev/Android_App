package cc.upedu.online.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.TelecastBean.Entity.Live;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CustomDigitalClock;
import cc.upedu.online.view.CustomDigitalClock.ClockListener;

public class TelecastAdapter extends AbsRecyclerViewAdapter {
	
	public TelecastAdapter(Context context, List<Live> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.layout_telecast_item_new);
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		CustomDigitalClock tv_countdown;
		RelativeLayout rl_content;
		TextView course_title;
		TextView theacher_Name;
		ImageView civ_img;
//		TextView apply_count;
		TextView browse_count;
		TextView starttime;
		boolean isTimeEnd;
		

		public MyViewHolder(View view) {
			super(view);
			tv_countdown = (CustomDigitalClock) view.findViewById(R.id.tv_countdown);
			rl_content = (RelativeLayout) view.findViewById(R.id.rl_content);
			course_title = (TextView) view.findViewById(R.id.course_title);
			theacher_Name = (TextView) view.findViewById(R.id.theacher_Name);
			civ_img = (ImageView) view.findViewById(R.id.civ_img);
//			holder.apply_count = (TextView) view.findViewById(R.id.apply_count);
			starttime = (TextView) view.findViewById(R.id.starttime);
			browse_count = (TextView) view.findViewById(R.id.browse_count);

		}

	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}
	@SuppressLint("NewApi")
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, int position) {
		Live item = (Live) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		//倒计时
				long endTime = StringUtil.stringToMillis(item.getStartTime());
				holder.tv_countdown.setEndTime(endTime,0);
				holder.tv_countdown.setClockListener(new ClockListener() {
					@Override
					public void timeEnd() {
//						isTimeEnd = true;
					}
					@Override
					public void remainFiveMinutes() {
					}
				});
				if (!StringUtil.isEmpty(item.getLogo())) {
					ImageUtils.setBackGroundImage(ConstantsOnline.SERVER_IMAGEURL+item.getLogo(), holder.rl_content, R.drawable.img_sportlist);
					
				}else {
					holder.rl_content.setBackgroundResource(R.drawable.img_sportlist);
				}
				if (!StringUtil.isEmpty(item.getPicPath())) {
					ImageUtils.setImage(item.getPicPath(), holder.civ_img, R.drawable.teacher_images);
				}else {
					holder.civ_img.setImageResource(R.drawable.teacher_images);
				}
//				holder.course_title.setText(item.getTitle());
				holder.theacher_Name.setText(item.getTeacherName());
				if (StringUtil.isEmpty(item.getJoinNum())) {
					holder.browse_count.setText(" 0次");
				}else {
					holder.browse_count.setText(" "+item.getJoinNum()+"次");
				}
				if (!StringUtil.isEmpty(item.getStartTime()) && !StringUtil.isEmpty(item.getEndTime())) {
					StringBuffer buffer = new StringBuffer();
					String[] startStrs = item.getStartTime().split(":");
					String[] endStrs = item.getEndTime().split(" ")[1].split(":");
					buffer.append(startStrs[0]).append(":").append(startStrs[1]).append(" ── ").append(endStrs[0]).append(":").append(endStrs[1]);
					
					holder.starttime.setText(buffer.toString());
				}else {
					holder.starttime.setText(item.getStartTime());
				}
		
		super.onBindViewHolder(viewHolder, position);	
	}
	
}
