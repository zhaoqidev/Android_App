package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.domin.IndustryListBean.IndustryItem;

public class IndustryListAdapter extends BaseMyAdapter<IndustryItem> {
	private String industry;
	public IndustryListAdapter(Context context, List<IndustryItem> list, String industry) {
		super(context, list);
		this.industry = industry;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_industry_item, null);
			holder = new ViewHolder();
			holder.tv_industry = (TextView) view.findViewById(R.id.tv_industry);
			holder.iv_choose = (ImageView) view.findViewById(R.id.iv_choose);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		holder.tv_industry.setText(list.get(position).getContent());
		if (list.get(position).getId().equals(industry)) {
			holder.iv_choose.setVisibility(View.VISIBLE);
		}else {
			holder.iv_choose.setVisibility(View.GONE);
		}
		return view;
	}
	private class ViewHolder{
		TextView tv_industry;
		ImageView iv_choose;
	}
}
