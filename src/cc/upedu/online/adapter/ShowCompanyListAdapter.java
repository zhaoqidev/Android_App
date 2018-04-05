package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.DelBaseBackCall;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem;
import cc.upedu.online.utils.StringUtil;

public class ShowCompanyListAdapter extends BaseMyAdapter<CompanyItem> {
	private DelBaseBackCall mDelBaseBackCall;
	
	public ShowCompanyListAdapter(Context context, List list,DelBaseBackCall mDelBaseBackCall) {
		super(context, list);
		this.mDelBaseBackCall = mDelBaseBackCall;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_showcompanyitem, null);
			holder = new ViewHolder();
			holder.tv_companyname = (TextView) view.findViewById(R.id.tv_companyname);
			holder.tv_companyaddress = (TextView) view.findViewById(R.id.tv_companyaddress);
			holder.tv_position = (TextView) view.findViewById(R.id.tv_position);
			holder.ll_del = (LinearLayout) view.findViewById(R.id.ll_del);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_companyname.setText(list.get(position).getName());
		if (!StringUtil.isEmpty(list.get(position).getCityName())) {
			holder.tv_companyaddress.setText(list.get(position).getCityName());
		}else {
			holder.tv_companyaddress.setText("未设置城市所在地");
		}
		if (!StringUtil.isEmpty(list.get(position).getPositionName())) {
			holder.tv_position.setText(list.get(position).getPositionName());
		}else {
			holder.tv_position.setText("未设置职位");
		}
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
		TextView tv_companyname;
		TextView tv_companyaddress;
		TextView tv_position;
		LinearLayout ll_del;
	}
}
