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

public class BgAdapter extends BaseMyAdapter<String> {
	private String bannerUrl;
	public BgAdapter(Context context, List list, String bannerUrl) {
		super(context, list);
		this.bannerUrl = bannerUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public void setBannerUrl(String bannerUrl) {
		this.bannerUrl = bannerUrl;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder holder;
		if (convertView == null) {
			view = View.inflate(context, R.layout.layout_userbg_item, null);
			holder = new ViewHolder();
			holder.iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		if (!StringUtil.isEmpty(list.get(position))) {
			
			ImageUtils.setImage(list.get(position), holder.iv_bg, R.drawable.img_sportlist);
		}else {
			holder.iv_bg.setImageResource(R.drawable.img_sportlist);
		}
		return view;
	}
//	ImageCallback callback=new ImageCallback() {
//		@Override
//		public void loadImage(Bitmap bitmap, String imagePath) {
//			notifyDataSetChanged();
//		}
//	};
	private class ViewHolder{
		ImageView iv_bg;
	}
}
