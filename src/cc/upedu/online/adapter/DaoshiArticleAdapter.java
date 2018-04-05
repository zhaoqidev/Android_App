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
import cc.upedu.online.domin.DaoshiArticleBean.ArticleItem;
import cc.upedu.online.utils.ImageUtils;

/**
 * 导师名片页面的文章的Adapter
 * @author Administrator
 *
 */
public class DaoshiArticleAdapter extends AbsRecyclerViewAdapter{
	
	public DaoshiArticleAdapter(Context context, List<ArticleItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.pager_daoshi_article_item);
	}	
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView note_user_image;//条目图片
		TextView note_user_name;//条目标题
		TextView note_content;//条目简介
		TextView note_time;//发表时间
		TextView tv_browse_number;//浏览次数
		ImageView iv_delete;

		public MyViewHolder(View view) {
			super(view);
			note_user_image=(ImageView) view.findViewById(R.id.note_user_image);
			note_user_name=(TextView) view.findViewById(R.id.note_user_name);
			note_content=(TextView) view.findViewById(R.id.note_content);
			note_time=(TextView) view.findViewById(R.id.note_time);
			tv_browse_number=(TextView) view.findViewById(R.id.tv_browse_number);//浏览次数
			iv_delete=(ImageView) view.findViewById(R.id.iv_delete);
		}

	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}
	@Override
	public void onBindViewHolder(ViewHolder arg0, int position) {
		ArticleItem iArticleItem = (ArticleItem) list.get(position);
		final MyViewHolder viewHolder = (MyViewHolder) arg0;
		ImageUtils.setImage(iArticleItem.picPath, viewHolder.note_user_image, 0);
		viewHolder.note_user_name.setText(iArticleItem.title);
		viewHolder.note_time.setText(iArticleItem.addtime);
		viewHolder.tv_browse_number.setText(iArticleItem.viewNum);
		super.onBindViewHolder(viewHolder, position);	
	}
}
