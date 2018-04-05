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

public class ArchitectureSystemAdapter extends BaseAdapter {
	private List<String> list;
	private Context context;
	private BitmapUtils bitmapUtils;
	private List<String> docList;
	
	public ArchitectureSystemAdapter(Context context,List<String> list,List<String> docList) {
		this.list = list;
		this.docList = docList;
		this.context = context;
		bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_architecture_system_item, null);
			holder = new ViewHolder();
			holder.systemimage_item = (ImageView) view.findViewById(R.id.systemimage_item);
			holder.system_item_name = (TextView) view.findViewById(R.id.system_item_name);
			holder.system_item_doc = (TextView) view.findViewById(R.id.system_item_doc);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
//		if (!StringUtil.isEmpty(list.get(position))) {
//		}
//		bitmapUtils.display(holder.courseimage_item,ConstantsOnline.SERVER_IMAGEURL+list.get(position).getLogo());
		holder.system_item_name.setText(list.get(position));
		holder.system_item_doc.setText(docList.get(position));
		return view;
	}
	
	private class ViewHolder{
		ImageView systemimage_item;
		TextView system_item_name;
		TextView system_item_doc;
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
