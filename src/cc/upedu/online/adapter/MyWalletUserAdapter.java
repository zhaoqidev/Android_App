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
import cc.upedu.online.domin.MyWalletDetailBean.recordItem;
import cc.upedu.online.utils.StringUtil;

/**
 * 我的钱包普通用户页面的用户adapter
 * @author Administrator
 *
 */
public class MyWalletUserAdapter extends AbsRecyclerViewAdapter {

	private String coin;
	
	public MyWalletUserAdapter(Context context, List<recordItem> list,String coin) {
		this.list = list;
		this.context = context;
		this.coin=coin;
		setResId(R.layout.activity_mywallet_detail_user_item);
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv_time;
		TextView tv_contect;
		TextView tv_money;
		TextView tv_money_title;
		TextView tv_username;
		TextView tv_usertitle;//用户名：
		

		public MyViewHolder(View view) {
			super(view);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			tv_contect = (TextView) view.findViewById(R.id.tv_contect);
			tv_money = (TextView) view.findViewById(R.id.tv_money);
			tv_money_title=(TextView) view.findViewById(R.id.tv_money_title);
			tv_username=(TextView) view.findViewById(R.id.tv_username);
			tv_usertitle=(TextView) view.findViewById(R.id.tv_usertitle);

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
		recordItem item = (recordItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		holder.tv_time.setText(item.addTime);
		holder.tv_contect.setText(item.description);
		
		if (StringUtil.isEmpty(item.userName)) {
			holder.tv_usertitle.setVisibility(View.INVISIBLE);
		}else {
			holder.tv_usertitle.setVisibility(View.VISIBLE);
			holder.tv_username.setText(item.userName);
		}
		
		if ("收入".equals(item.type)) {
			holder.tv_money.setText("+"+item.amount);
		}else {
			holder.tv_money.setText("-"+item.amount);
		}
		
		if ("coin".equals(coin)) {
			holder.tv_money_title.setText("币值：");
		}
		
		super.onBindViewHolder(viewHolder, position);	
	}
	
}
