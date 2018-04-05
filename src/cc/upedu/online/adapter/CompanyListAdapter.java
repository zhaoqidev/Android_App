package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.SetCompanyActivity;
import cc.upedu.online.activity.SetCompanyNameActivity;
import cc.upedu.online.activity.SetCompanyWebActivity;
import cc.upedu.online.activity.SetIndustryActivity;
import cc.upedu.online.activity.SetPositionActivity;
import cc.upedu.online.activity.SetProductListActivity;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.DelBaseBackCall;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;

public class CompanyListAdapter extends BaseMyAdapter<CompanyItem> {
	private DelBaseBackCall mDelBaseBackCall;
	protected Intent intent;
	
	public CompanyListAdapter(Context context, List<CompanyItem> list,DelBaseBackCall mDelBaseBackCall) {
		super(context, list);
		this.mDelBaseBackCall = mDelBaseBackCall;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_companyitem, null);
			holder = new ViewHolder();
			holder.ll_setcompanyname = (LinearLayout) view.findViewById(R.id.ll_setcompanyname);
			holder.tv_setcompanyname = (TextView) view.findViewById(R.id.tv_setcompanyname);
			holder.ll_setindustry = (LinearLayout) view.findViewById(R.id.ll_setindustry);
			holder.tv_setindustry = (TextView) view.findViewById(R.id.tv_setindustry);
			holder.ll_setcompanycity = (LinearLayout) view.findViewById(R.id.ll_setcompanycity);
			holder.tv_setcompanycity = (TextView) view.findViewById(R.id.tv_setcompanycity);
			holder.ll_setproductlist = (LinearLayout) view.findViewById(R.id.ll_setproductlist);
			holder.tv_setproductlist = (TextView) view.findViewById(R.id.tv_setproductlist);
			holder.ll_setposition = (LinearLayout) view.findViewById(R.id.ll_setposition);
			holder.tv_setposition = (TextView) view.findViewById(R.id.tv_setposition);
			holder.ll_setcompanyweb = (LinearLayout) view.findViewById(R.id.ll_setcompanyweb);
			holder.tv_setcompanyweb = (TextView) view.findViewById(R.id.tv_setcompanyweb);
			holder.ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		//名称
		holder.tv_setcompanyname.setText(list.get(position).getName());
		holder.ll_setcompanyname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, SetCompanyNameActivity.class);
				intent.putExtra("companyName", list.get(position).getName());
				intent.putExtra("isnameopen", list.get(position).getIsnameopen());
				intent.putExtra("currentPosition", String.valueOf(position));
				((SetCompanyActivity) context).startActivityForResult(intent, SetCompanyActivity.REQUEST_SETCOMPANYITEM);
			}
		});
		//行业
		holder.tv_setindustry.setText(list.get(position).getIndustryName());
		holder.ll_setindustry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, SetIndustryActivity.class);
				intent.putExtra("industry", list.get(position).getIndustry());
				intent.putExtra("currentPosition", String.valueOf(position));
				((SetCompanyActivity) context).startActivityForResult(intent, SetCompanyActivity.REQUEST_SETCOMPANYITEM);
			}
		});
		//所在地
		if (!StringUtil.isEmpty(list.get(position).getCityName())) {
			holder.tv_setcompanycity.setText(list.get(position).getCityName());
		}else {
			holder.tv_setcompanycity.setText("未设置");
		}
		holder.ll_setcompanycity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, CityChooseActity.class);
				intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_FOUR);
				intent.putExtra("currentPosition", String.valueOf(position));
				((SetCompanyActivity) context).startActivityForResult(intent, CityChooseActity.CHOOSE_FOUR);
			}
		});
		//产品
		if (list.get(position).getProductList() != null && list.get(position).getProductList().size() > 0) {
			if (StringUtil.isEmpty(list.get(position).getShowProductName())) {
				list.get(position).setShowProductName(list.get(position).getProductList().get(0).getTitle());
			}
			holder.tv_setproductlist.setText(list.get(position).getShowProductName());
		}else {
			if (list.get(position).getAddProductList() != null && list.get(position).getAddProductList().size() > 0) {
				holder.tv_setproductlist.setText(list.get(position).getShowProductName());
			}else {
				holder.tv_setproductlist.setText("未设置");
			}
		}
		holder.ll_setproductlist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, SetProductListActivity.class);
				intent.putExtra("productList", (Serializable)list.get(position).getProductList());
				intent.putExtra("alterProductList", (Serializable)list.get(position).getAlterProductList());
				intent.putExtra("addProductList", (Serializable)list.get(position).getAddProductList());
				intent.putExtra("delProductIdList", (Serializable)list.get(position).getDelProductIdList());
				intent.putExtra("currentPosition", String.valueOf(position));
				((SetCompanyActivity) context).startActivityForResult(intent, SetCompanyActivity.REQUEST_SETCOMPANYITEM);
			}
		});
		//职位
		if (!StringUtil.isEmpty(list.get(position).getPositionName())) {
			holder.tv_setposition.setText(list.get(position).getPositionName());
		}else {
			holder.tv_setposition.setText("未设置");
		}
		holder.ll_setposition.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, SetPositionActivity.class);
				intent.putExtra("positionid", list.get(position).getPosition());
				intent.putExtra("currentPosition", String.valueOf(position));
				((SetCompanyActivity) context).startActivityForResult(intent, SetCompanyActivity.REQUEST_SETCOMPANYITEM);
			}
		});
		//网站
		if (!StringUtil.isEmpty(list.get(position).getWebsite())) {
			holder.tv_setcompanyweb.setText(list.get(position).getWebsite());
		}else {
			holder.tv_setcompanyweb.setText("未设置");
		}
		holder.ll_setcompanyweb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent(context, SetCompanyWebActivity.class);
				intent.putExtra("companyWeb", list.get(position).getWebsite());
				intent.putExtra("iswebsiteopen", list.get(position).getIswebsiteopen());
				intent.putExtra("currentPosition", String.valueOf(position));
				((SetCompanyActivity) context).startActivityForResult(intent, SetCompanyActivity.REQUEST_SETCOMPANYITEM);
			}
		});
		if (mDelBaseBackCall != null) {
			holder.ll_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDelBaseBackCall.delBackCall(position);
				}
			});
		}else {
			holder.ll_del.setVisibility(View.GONE);
		}
		return view;
	}
	private class ViewHolder{
		LinearLayout ll_setcompanyname;
		TextView tv_setcompanyname;
		LinearLayout ll_setindustry;
		TextView tv_setindustry;
		LinearLayout ll_setcompanycity;
		TextView tv_setcompanycity;
		LinearLayout ll_setproductlist;
		TextView tv_setproductlist;
		LinearLayout ll_setposition;
		TextView tv_setposition;
		LinearLayout ll_setcompanyweb;
		TextView tv_setcompanyweb;
		LinearLayout ll_del;
	}
}
