package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.domin.OneCourseOtherUserListBean.StudentItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

/**
 * 我的代言界面adapter
 * @author Administrator
 *
 */
public class MyNoteCourseOtherUserListAdapter extends BaseMyAdapter<StudentItem> {

	public MyNoteCourseOtherUserListAdapter(Context context, List<StudentItem> list) {
		super(context, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_mynote_otheruser_item, null);
			holder = new ViewHolder();
			holder.teacher_image = (CircleImageView) view.findViewById(R.id.teacher_image);
			holder.teacher_name = (TextView) view.findViewById(R.id.teacher_name);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		
		if (!StringUtil.isEmpty(list.get(position).getAvatar())) {
			ImageUtils.setImage(list.get(position).getAvatar(), holder.teacher_image, R.drawable.left_menu_head);
		}else {
			holder.teacher_image.setImageResource(R.drawable.left_menu_head);
		}
		holder.teacher_name.setText(list.get(position).getUname());
		return view;
	}
	private class ViewHolder{
		CircleImageView teacher_image;
		TextView teacher_name;
	}
}
