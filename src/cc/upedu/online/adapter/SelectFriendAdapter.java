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
import cc.upedu.online.domin.MySchoolmateBean.SchoolmateItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

/**
 * 赠送成长币，选择学友的列表的adaptor
 * 
 * @author Administrator
 * 
 */
public class SelectFriendAdapter extends AbsRecyclerViewAdapter{

	public SelectFriendAdapter(Context context, List<SchoolmateItem> list) {
		this.list = list;
		this.context = context;
		setResId(R.layout.pager_schoolmatemy_item);
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv_city;
		CircleImageView teacher_image;
		TextView teacher_name;
		TextView tv_company;
		TextView tv_position;
		ImageView iv_attention;
		

		public MyViewHolder(View view) {
			super(view);
			teacher_image = (CircleImageView) view.findViewById(R.id.teacher_image);
			teacher_name = (TextView) view.findViewById(R.id.teacher_name);
			tv_company = (TextView) view.findViewById(R.id.tv_company);
			tv_city = (TextView) view.findViewById(R.id.tv_city);
			tv_position = (TextView) view.findViewById(R.id.tv_position);
			iv_attention = (ImageView) view.findViewById(R.id.iv_attention);
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
		SchoolmateItem item = (SchoolmateItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		if (!StringUtil.isEmpty(item.avatar)) {
			ImageUtils.setImage(item.avatar, holder.teacher_image, R.drawable.left_menu_head);
		}else {
			holder.teacher_image.setImageResource(R.drawable.left_menu_head);
		}
		holder.teacher_name.setText(item.name);
		holder.tv_city.setText(item.city);
		if (!StringUtil.isEmpty(item.company)) {
			holder.tv_company.setText(item.company);
		}else {
			holder.tv_company.setText("未公开");
		}
		holder.tv_position.setText(item.position);
		holder.iv_attention.setVisibility(View.GONE);
		
		super.onBindViewHolder(viewHolder, position);	
	}

}
