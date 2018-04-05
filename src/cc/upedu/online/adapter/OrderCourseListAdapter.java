package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.CourseIntroduceActivity;
import cc.upedu.online.activity.TelecastApplayActivity;
import cc.upedu.online.domin.MyOrderBean.TrxorderDetailList;
import cc.upedu.online.utils.ImageUtils;

/**
 * 我的订单页面课程列表adapter
 * 
 * @author Administrator
 * 
 */
public class OrderCourseListAdapter extends BaseAdapter {
	private List<TrxorderDetailList> list;
	private Context context;

	public OrderCourseListAdapter(Context context, List<TrxorderDetailList> list) {
		this.list = list;
		this.context = context;
	}


	private class ItemViewHolder {
		ImageView courseimage_item;// 课程图片
		TextView course_title;// 课程名称
		TextView course_price;// 课程价格

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
	
	ItemViewHolder courseHolder;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View courseView;
		
		// 复用
		if (convertView == null) {
			courseView = View.inflate(context,R.layout.activity_orderdetail_item, null);
			courseHolder = new ItemViewHolder();
			courseHolder.courseimage_item = (ImageView) courseView
					.findViewById(R.id.courseimage_item);// 课程图片
			courseHolder.course_title = (TextView) courseView
					.findViewById(R.id.course_title);// 课程标题
			courseHolder.course_price = (TextView) courseView
					.findViewById(R.id.course_price);// 课程价格
			courseView.setTag(courseHolder);
		} else {
			courseView = convertView;
			courseHolder = (ItemViewHolder) courseView.getTag();
		}
		ImageUtils.setImage(list.get(position).courseImgUrl, courseHolder.courseimage_item, R.drawable.img_course);
		courseHolder.course_title.setText(list.get(position).courseName);
		courseHolder.course_price.setText(list.get(position).currentPirce);
		
		courseView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ("COURSE".equals(list.get(position).type)) {
					Intent intent = new Intent(context,CourseIntroduceActivity.class);
					intent.putExtra("courseId", list.get(position).courseId);
					context.startActivity(intent);
				}else if("LIVE".equals(list.get(position).type)){
					Intent intent = new Intent(context, TelecastApplayActivity.class);
					intent.putExtra("courseId", list.get(position).courseId);
					context.startActivity(intent);
				}else {
					
				}
				
				
			}
		});
		
		return courseView;
	}
	
	/**
	 * 设置Listview的高度
	 */
	public void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}

