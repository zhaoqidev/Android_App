package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.domin.MyWalletDetailBean.recordItem;
import cc.upedu.online.utils.StringUtil;

/**
 * 我的钱包，导师页面收入明细的adapter
 * @author Administrator
 *
 */
public class MyWalletDaoshiAdapter extends AbsRecyclerViewAdapter {

	public MyWalletDaoshiAdapter(Context context, List<recordItem> list) {
		this.list = list;
		this.context = context;
		setResId(R.layout.activity_mywallet_detail_daoshi_item);
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv_time;
		TextView tv_contect;
		TextView tv_money;
		TextView tv_name;
		TextView tv_price;
		TextView tv_name_title;
		LinearLayout ll_user;
		LinearLayout ll_course_name;
		View view_course_name;
		

		public MyViewHolder(View view) {
			super(view);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_contect = (TextView) view.findViewById(R.id.tv_contect);
			tv_money = (TextView) view.findViewById(R.id.tv_money);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_price = (TextView) view.findViewById(R.id.tv_prise);
			tv_name_title = (TextView) view.findViewById(R.id.tv_name_title);
			ll_user = (LinearLayout) view.findViewById(R.id.ll_user);
			ll_course_name = (LinearLayout) view.findViewById(R.id.ll_course_name);
			view_course_name = (View) view.findViewById(R.id.view_course_name);

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
		final recordItem item = (recordItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		holder.tv_time.setText(item.addTime);
		holder.tv_money.setText(item.amount);//开发奖励
		holder.tv_name.setText(item.userName);//学员姓名
		holder.tv_price.setText(item.description);//课程价格
		
		if (StringUtil.isEmpty(item.userName)) {
			holder.tv_name_title.setVisibility(View.INVISIBLE);
		}else {
			holder.tv_name_title.setVisibility(View.VISIBLE);
		}
		
		if (!StringUtil.isEmpty(item.userName)) {
			holder.ll_course_name.setVisibility(View.VISIBLE);
			holder.view_course_name.setVisibility(View.VISIBLE);
			holder.tv_contect.setText(item.courseName);//课程名称
		}else {
			holder.ll_course_name.setVisibility(View.GONE);
			holder.view_course_name.setVisibility(View.GONE);
		}
		
		holder.ll_user.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, UserShowActivity.class);
				if (!StringUtil.isEmpty(item.userId)) {
					intent.putExtra("userId", item.userId);
					context.startActivity(intent);
				}
				
				
			}
		});
		
		super.onBindViewHolder(viewHolder, position);	
	}

}
