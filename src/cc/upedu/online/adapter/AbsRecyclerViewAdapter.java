package cc.upedu.online.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;

import java.util.List;

/**
 * RecyclerView的Adapter的父类
 * @author Administrator
 *
 */
@SuppressWarnings("rawtypes")
public class AbsRecyclerViewAdapter extends RecyclerView.Adapter{
	Context context;
	List list;
	int resId;

	//点击事件
	public static interface OnItemClickLitener
    {
		/**
		 * 条目单击事件
		 * @param view
		 * @param position
		 */
        void onItemClick(View view, int position);
        /**
         * 条目长点击事件
         * @param view
         * @param position
         */
        void onItemLongClick(View view , int position);
    }
	
    public OnItemClickLitener mOnItemClickLitener;

	public OnItemClickLitener getmOnItemClickLitener() {
		return mOnItemClickLitener;
	}

	public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

	@Override
	public int getItemCount() {
		// TODO Auto-generated method stub
		if (list == null) {
			return 0;
		}else {
			return list.size();
		}
	}

	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
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

	public int getResId() {
		return resId;
	}

	public void setResId(int resId) {
		this.resId = resId;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewgroup, int i) {
		// TODO Auto-generated method stub
		return null;
	}

}
