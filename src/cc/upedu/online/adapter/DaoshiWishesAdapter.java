package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.domin.DaoshiWishesBean.WordsItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

/**
 * 同学寄语的adapter
 * 
 * @author Administrator
 * 
 */
public class DaoshiWishesAdapter extends AbsRecyclerViewAdapter {


	public DaoshiWishesAdapter(Context context, List<WordsItem> list) {
		this.list = list;
		this.context = context;
		resId=R.layout.pager_daoshi_wishes_item;
	}


	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
		MyViewHolder holder=(MyViewHolder) viewHolder;
		final WordsItem item=(WordsItem) list.get(position);
		
		if (!StringUtil.isEmpty(item.avatar)) {
			ImageUtils.setImage(item.avatar, holder.inspiration_image_item, R.drawable.left_menu_head);
		} else {
			holder.inspiration_image_item
					.setImageResource(R.drawable.left_menu_head);
		}

		holder.inspiration_time.setText(item.addtime);
		holder.inspiration_doc.setText(item.words);
		holder.inspiration_name.setText(item.realname);

		// 点击学友头像的点击事件
		holder.inspiration_image_item.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								UserShowActivity.class);
						intent.putExtra("userId", item.userId);
						context.startActivity(intent);
					}
				});
		
		 // 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null){
			viewHolder.itemView.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, pos);
                }
            });

			viewHolder.itemView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(viewHolder.itemView, pos);
                    return false;
                }
            });
        }

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		CircleImageView inspiration_image_item;// 头像图片
		TextView inspiration_time;// 发表时间
		TextView inspiration_name;// 发表人姓名
		TextView inspiration_doc;// 发表寄语内容

		public MyViewHolder(View view) {
			super(view);
			inspiration_image_item = (CircleImageView) view
					.findViewById(R.id.inspiration_image_item);
			inspiration_time = (TextView) view
					.findViewById(R.id.inspiration_time);
			inspiration_doc = (TextView) view
					.findViewById(R.id.inspiration_doc);
			inspiration_name = (TextView) view
					.findViewById(R.id.inspiration_name);
		}

	}
}
