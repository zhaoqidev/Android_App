package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.OrderDetailActivity;
import cc.upedu.online.domin.MyOrderBean.OrderItem;
import cc.upedu.online.domin.MyOrderBean.TrxorderDetailList;

/**
 * 侧拉栏--我的订单页面adapter
 * 
 * @author Administrator
 * 
 */
public class MyOrderListAdapter extends AbsRecyclerViewAdapter {

	private OrderCourseListAdapter adapter;
	
	private String balance;
	
	public MyOrderListAdapter(Context context, List<OrderItem> list,String balance) {
		this.list = list;
		this.context = context;
		this.balance=balance;
		setResId(R.layout.activity_myorder_item);
	}
	

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv_order_number;// 订单编号
		TextView tv_pay;// 订单金额
		TextView tv_goto_pay;// 立即付款按钮
		ListView lv_course_item;// 课程条目
		

		public MyViewHolder(View view) {
			super(view);
			tv_order_number = (TextView) view.findViewById(R.id.tv_order_number);// 订单编号
			tv_pay = (TextView) view.findViewById(R.id.tv_pay);// 订单金额
			tv_goto_pay = (TextView) view.findViewById(R.id.tv_goto_pay);// 立即付款按钮
			lv_course_item = (ListView) view.findViewById(R.id.lv_course_item);// 课程条目

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
		final OrderItem item = (OrderItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		

		holder.tv_order_number.setText(item.requestId);
		holder.tv_pay.setText(item.amount);// 实付金额
		holder.tv_goto_pay.setText(item.trxStatus);// 支付状态
		
		holder.tv_goto_pay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(context, OrderDetailActivity.class);
				intent.putExtra("trxorderId", item.trxorderId);//订单信息
				intent.putExtra("requestId", item.requestId);
				intent.putExtra("payType", item.payType);//支付类型
				intent.putExtra("orderAmount", item.orderAmount);//订单总额
				intent.putExtra("amount", item.amount);//实付金额
				intent.putExtra("createTime", item.createTime);//下单时间
				intent.putExtra("balance", balance);//零钱余额
				
				intent.putExtra("trxorderDetailList",(Serializable)(ArrayList<TrxorderDetailList>)item.trxorderDetailList);//课程条目
				
				context.startActivity(intent);
				
			}
		});
		
		if ("未支付".equals(item.trxStatus)) {
			holder.tv_goto_pay.setTextColor(context.getResources().getColor(R.color.light_red));
			holder.tv_goto_pay.setClickable(true);
			holder.tv_goto_pay.setBackgroundResource(R.drawable.weifukuan);
			holder.tv_goto_pay.setText("立即支付");
		} else {
			holder.tv_goto_pay.setTextColor(context.getResources().getColor(R.color.text_color_three));
			holder.tv_goto_pay.setClickable(false);
			holder.tv_goto_pay.setBackgroundResource(R.drawable.yifukuan);
		}

		adapter=new OrderCourseListAdapter(context, item.trxorderDetailList);

		holder.lv_course_item.setAdapter(adapter);
		adapter.setListViewHeight(holder.lv_course_item);
		
		super.onBindViewHolder(viewHolder, position);	
	}



}
