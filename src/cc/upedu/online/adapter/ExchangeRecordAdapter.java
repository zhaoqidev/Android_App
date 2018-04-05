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
import cc.upedu.online.domin.MyPointorderBean.Entity.OrderItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;

public class ExchangeRecordAdapter extends AbsRecyclerViewAdapter {

	public ExchangeRecordAdapter(Context context, List<OrderItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.layout_exchangerecord_item);
	}	
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv_commodity_name;
		ImageView iv_commodity_pic;
		TextView tv_pointamount;
		TextView tv_createTime;
		TextView tv_address;
		TextView tv_status;
		

		public MyViewHolder(View view) {
			super(view);
			tv_commodity_name = (TextView) view.findViewById(R.id.tv_commodity_name);
			iv_commodity_pic = (ImageView) view.findViewById(R.id.iv_commodity_pic);
			tv_pointamount = (TextView) view.findViewById(R.id.tv_pointamount);
			tv_createTime = (TextView) view.findViewById(R.id.tv_createTime);
			tv_address = (TextView) view.findViewById(R.id.tv_address);
			tv_status = (TextView) view.findViewById(R.id.tv_status);

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
		OrderItem item = (OrderItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		holder.tv_commodity_name.setText(item.getCommodityName());
		if (!StringUtil.isEmpty(item.getImageUrl())) {
			
			ImageUtils.setImageFullPath(item.getImageUrl(), holder.iv_commodity_pic, R.drawable.wodeimg_default);
		}else {
			holder.iv_commodity_pic.setImageResource(R.drawable.wodeimg_default);
		}
		holder.tv_pointamount.setText(item.getPointamount()+"成长币");
		holder.tv_createTime.setText(item.getCreateTime());
		holder.tv_address.setText(item.getAddress());
		holder.tv_status.setText(item.getStatus());
		
		super.onBindViewHolder(viewHolder, position);	
	}

		
//		holder.inspiration_image_item.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(UserStateUtil.isLogined()){
//					Intent intent = new Intent(context, UserShowActivity.class);
//					intent.putExtra("userId", list.get(position).getUserId());
//					context.startActivity(intent);
//				}else {
//					UserStateUtil.NotLoginDialog(context);
//				}
//			}
//		});

}
