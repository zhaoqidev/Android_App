package cc.upedu.online.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;

/**
 * 商品详情页面，产品展示列表的adapter的
 * 
 * @author Administrator
 * 
 */
public class MicroMallGoodsPicListAdapter extends BaseMyAdapter<String> {

	public MicroMallGoodsPicListAdapter(Context context, List<String> list) {
		super(context, list);
	}


	private class ItemViewHolder {
		ImageView iv;//展示图片
	}

	ItemViewHolder holder;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		
		// 复用
		if (convertView == null) {
			view = View.inflate(context,
					R.layout.layout_imageview, null);
			holder = new ItemViewHolder();
			holder.iv = (ImageView) view.findViewById(R.id.iv);
			
			view.setTag(holder);
		} else {
			view = convertView;
			holder = (ItemViewHolder) view.getTag();
		}
		if (!StringUtil.isEmpty(list.get(position))) {

//			ImageUtils.setBackGroundImage(list.get(position), holder.iv, R.drawable.img_sportlist);
			ImageUtils.setImageFullPath(list.get(position), holder.iv, R.drawable.img_sportlist);
		}

		return view;
	}
	

}

