package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cc.upedu.online.R;

public class CourseValueAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
	private BitmapUtils bitmapUtils;
	
	public CourseValueAdapter(Context context, List<String> list) {
		this.list = list;
		this.context = context;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_coursevalue_item, null);
			holder = new ViewHolder();
			holder.value_image_item = (ImageView) view.findViewById(R.id.value_image_item);
			holder.value_doc = (TextView) view.findViewById(R.id.value_doc);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
//		if (!StringUtil.isEmpty(list.get(position))) {
//		}
//		bitmapUtils.display(holder.teacher_image_item,ConstantsOnline.SERVER_IMAGEURL+list.get(position).getLogo());
		holder.value_doc.setText(list.get(position));
		return view;
	}
	
	private class ViewHolder{
		ImageView value_image_item;
		TextView value_doc;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
