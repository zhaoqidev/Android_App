package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.DelBaseBackCall;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;

public class ProductListAdapter extends BaseMyAdapter<ProductItem> {
	private DelBaseBackCall mDelBaseBackCall;
	
	public ProductListAdapter(Context context, List<ProductItem> list,DelBaseBackCall mDelBaseBackCall) {
		super(context, list);
		this.mDelBaseBackCall = mDelBaseBackCall;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_productitem, null);
			holder = new ViewHolder();
			holder.iv_product = (ImageView) view.findViewById(R.id.iv_product);
			holder.tv_productname = (TextView) view.findViewById(R.id.tv_productname);
			holder.tv_productdesc = (TextView) view.findViewById(R.id.tv_productdesc);
			holder.ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		if (list.get(position).getPicAddArray() != null && list.get(position).getPicAddArray().size() > 0 && !StringUtil.isEmpty(list.get(position).getPicAddArray().get(0))) {
			ImageUtils.setImage(list.get(position).getPicAddArray().get(0).substring(1, list.get(position).getPicAddArray().get(0).length()-1), 
					holder.iv_product, R.drawable.wodeimg_default);
		}else {
			if (list.get(position).getPicList() != null && list.get(position).getPicList().size() >0 && !StringUtil.isEmpty(list.get(position).getPicList().get(0).getPicPath())) {
				ImageUtils.setImage(list.get(position).getPicList().get(0).getPicPath(), 
						holder.iv_product, R.drawable.wodeimg_default);
			}else {
				holder.iv_product.setImageResource(R.drawable.wodeimg_default);
			}
		}
		holder.tv_productname.setText(list.get(position).getTitle());
		holder.tv_productdesc.setText(list.get(position).getDesc());
		if (mDelBaseBackCall != null) {
			holder.ll_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mDelBaseBackCall.delBackCall(position);
				}
			});
		}else {
			holder.ll_del.setVisibility(View.GONE);
		}
		return view;
	}
	private class ViewHolder{
		ImageView iv_product;
		TextView tv_productname;
		TextView tv_productdesc;
		LinearLayout ll_del;
	}
}
