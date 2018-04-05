package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.domin.DawnMenuBean.Entity;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.StringUtil;
/**
 * 下拉(课程分类中课程/导师分类)列表listview的填充器
 * @author hui
 *
 */
public class DownMenuAdapter extends BaseMyAdapter<Entity> {
	/**
	 * 标示显示的内容是课程还是导师
	 * 	true 表示课程
	 *  false 表示导师
	 */
	private boolean type;
	public DownMenuAdapter(Context context, List<Entity> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_downmenu_item, null);
			holder = new ViewHolder();
			holder.iv_downmenu = (ImageView) view.findViewById(R.id.iv_downmenu);
			holder.tv_downmenu = (TextView) view.findViewById(R.id.tv_downmenu);
			holder.choose_downmenu = (ImageView) view.findViewById(R.id.choose_downmenu);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		/*
		 * 根据类型表示去获取用户上传选择的分类ID以便于显示
		 * 默认值"-1"表示所有
		 */
		String subject;
		if (type) {
			subject = SharedPreferencesUtil.getInstance().spGetString("downmenu_courseSubjectId", "-1");
		}else {
			subject = SharedPreferencesUtil.getInstance().spGetString("downmenu_teacherSubjectId", "-1");
		}
		
		if (position == 0) {
			//列表的第一个条目表示所有
			if (type) {
				holder.tv_downmenu.setText("所有课程");
			}else {
				holder.tv_downmenu.setText("所有导师");
			}
			if ("-1".equals(subject)) {
				if (isSearch) {
					holder.choose_downmenu.setVisibility(View.GONE);
				}else {
					holder.choose_downmenu.setVisibility(View.VISIBLE);
				}
			}else {
				holder.choose_downmenu.setVisibility(View.GONE);
			}
			holder.iv_downmenu.setImageResource(R.drawable.newkeno);
		}else {
			//其他为请求的数据
			if (!StringUtil.isEmpty(list.get(position-1).getSubjectLogo())) {
				ImageUtils.setImage(list.get(position-1).getSubjectLogo(), holder.iv_downmenu, R.drawable.wodeimg_default);
			}else {
				holder.iv_downmenu.setImageResource(R.drawable.wodeimg_default);
			}
			holder.tv_downmenu.setText(list.get(position-1).getSubjectName());
			
			if (list.get(position-1).getSubjectId().equals(subject)) {
				holder.choose_downmenu.setVisibility(View.VISIBLE);
			}else {
				holder.choose_downmenu.setVisibility(View.GONE);
			}
		}
		return view;
	}
	@Override
	public int getCount() {
		return super.getCount()+1;
	}
	@Override
	public Object getItem(int position) {
		if (position == 0) {
			return null;
		}else {
			return super.getItem(position-1);
		}
	}
	/**
	 * 设置该adapter填充的数据是课程还是导师
	 * @param type true 表示该adapter填充的数据是课程
	 * 				false 表示该adapter填充的数据是导师
	 */
	public void setType(boolean type){
		this.type = type;
	}
	
	private class ViewHolder{
		ImageView iv_downmenu;
		TextView tv_downmenu;
		ImageView choose_downmenu;
	}
	
	private boolean isSearch = false;
	public void setSearch(boolean isSearch) {
		this.isSearch = isSearch;
	}
}
