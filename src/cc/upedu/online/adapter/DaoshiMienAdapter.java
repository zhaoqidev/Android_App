package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.ImageActivity;
import cc.upedu.online.utils.ImageUtils;

/**
 * 导师风采的adapter
 * 
 * @author Administrator
 * 
 */
public class DaoshiMienAdapter extends AbsRecyclerViewAdapter {



	public DaoshiMienAdapter(Context context, List<String> list) {
		this.list = list;
		this.context = context;
		setResId(R.layout.pager_daoshimien_item);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int arg1) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}

	// 将数据与界面进行绑定的操作
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {

		final MyViewHolder holder = (MyViewHolder) viewHolder;
		/*		
				viewHolder.ib_zhiku_item.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (bitmap != null) {
							Intent intent = new Intent(context, ImageActivity.class);
							intent.putExtra("image_list", (Serializable) list);
							intent.putExtra("image_position", i);
							context.startActivity(intent);
						} else {
							ShowUtils.showMsg(context, "图片加载失败");
						}

					}
				});*/
		
		ImageUtils.setImageToImageButton((String) list.get(position), holder.ib_zhiku_item, R.drawable.wodeimg_default);
		holder.ib_zhiku_item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

					Intent intent = new Intent(context, ImageActivity.class);
					intent.putExtra("image_list", (Serializable) list);
					intent.putExtra("image_position", position);
					context.startActivity(intent);

			}
		});
				
		super.onBindViewHolder(viewHolder, position);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		ImageButton ib_zhiku_item;

		public MyViewHolder(View arg0) {
			super(arg0);
			ib_zhiku_item=(ImageButton) arg0.findViewById(R.id.ib_zhiku_item);
		}

	}

}
