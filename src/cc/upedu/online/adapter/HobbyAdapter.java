package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.domin.HobbyListBean.HobbyItem;
import cc.upedu.online.utils.ConstantsOnline;
/**
 * 智库的adapter
 * @author Administrator
 *
 */
public class HobbyAdapter extends BaseAdapter {

	private List<HobbyItem> hobbyList;// 存储
	private Context context;
	private List<String> mHobbyList;
	
	public HobbyAdapter(Context context, List<HobbyItem> hobbyList,
			List<String> mHobbyList) {
		this.hobbyList = hobbyList;
		this.context = context;
		this.mHobbyList = mHobbyList;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;// 风采条目
		ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_hobbyitem, null);
			holder = new ViewHolder();
			holder.iv_hobby_pic= (ImageView) view.findViewById(R.id.iv_hobby_pic);
			holder.tv_hobby_name=(TextView) view.findViewById(R.id.tv_hobby_name);
			holder.iv_choice= (ImageView) view.findViewById(R.id.iv_choice);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		OnlineApp.myApp.imageLoader.displayImage(ConstantsOnline.IP_IMAGEURL+hobbyList.get(position).getApplogo(),
				holder.iv_hobby_pic, 
				OnlineApp.myApp.builder.showImageOnFail(R.drawable.wodeimg_default)
				.showImageOnLoading(R.drawable.wodeimg_default).build());
		
		holder.tv_hobby_name.setText(hobbyList.get(position).getContent());
		if (mHobbyList.contains(hobbyList.get(position).getId())) {
			holder.iv_choice.setVisibility(View.VISIBLE);
		}else {
			holder.iv_choice.setVisibility(View.INVISIBLE);
		}
		return view;
	}
	private class ViewHolder{
		
		ImageView iv_hobby_pic;
		TextView tv_hobby_name;
		ImageView iv_choice;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return hobbyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Integer.valueOf(hobbyList.get(position).getId());
	}

}
