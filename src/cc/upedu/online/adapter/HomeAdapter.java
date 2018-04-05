package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.CourseArchitectureActivity;
import cc.upedu.online.activity.CourseIntroduceActivity;
import cc.upedu.online.activity.NoticeListActivity;
import cc.upedu.online.domin.HomeBean.Entity.CourseItem;
import cc.upedu.online.domin.HomeBean.Entity.SubjectItem;
import cc.upedu.online.domin.NoticeItem;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.MarqueeText;

public class HomeAdapter extends BaseAdapter {
	Context context;
	private List<NoticeItem> noticeList;
	private List<SubjectItem> subjectItem;
	private String spacing = "";
	// 推荐课程列表
	List<CourseItem> recommendList = new ArrayList<CourseItem>();
	// 免费课程列表
	List<CourseItem> freeList = new ArrayList<CourseItem>();

	public HomeAdapter(Context context, List<SubjectItem> subjectItem,
			List<NoticeItem> noticeList, List<CourseItem> courseList) {
		this.context = context;
		this.subjectItem = subjectItem;
		this.noticeList = noticeList;
		// 把推荐课程和免费课程分开
		for (int i = 0; i < courseList.size(); i++) {
			if ("1".equals(courseList.get(i).getRecommendId())) {
				recommendList.add(courseList.get(i));
			} else if ("2".equals(courseList.get(i).getRecommendId())) {
				freeList.add(courseList.get(i));
			}
		}
	}

	@Override
	public int getCount() {
		// +2 表示推荐课程列表分两类显示,添加两个分类标题 +1个体系列表 +1个公告栏
		return recommendList.size() + freeList.size() + 2 + 1 + 1;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// private Map<Integer, String> courseidMap = new HashMap<Integer,
	// String>();
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = null;
		final ViewHolder holder;
		if (position == 0) {
			// 体系列表
			view = View.inflate(context, R.layout.home_plate_scroll, null);
			LinearLayout ll_plate = (LinearLayout) view
					.findViewById(R.id.ll_plate);
			if (subjectItem != null && subjectItem.size() > 0) {
				for (int i = 0; i < subjectItem.size(); i++) {
					final int index = i;
					View iv = View.inflate(context,
							R.layout.layout_architecture_icon, null);
					final ImageView architecture_image = (ImageView) iv
							.findViewById(R.id.architecture_image);
					if (!StringUtil.isEmpty(subjectItem.get(index).getSubjectLogo())) {
						
						ImageUtils.setImage(subjectItem.get(index).getSubjectLogo(), architecture_image, R.drawable.wodeimg_default);
					}
					iv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									CourseArchitectureActivity.class);
							intent.putExtra("subjectId",
									subjectItem.get(index).getSubjectId());
							intent.putExtra("subjectLogo",
									subjectItem.get(index).getSubjectLogo());
							intent.putExtra("subjectName",
									subjectItem.get(index).getSubjectName());
							context.startActivity(intent);
						}
					});
					
					ll_plate.addView(iv);
				}
			}
		} else if (position == 1) {
			// 公告
			view = View.inflate(context, R.layout.layout_notice, null);
			marqueetext = (MarqueeText) view.findViewById(R.id.marqueetext);
			String noticeString = "";
			int textWidth = marqueetext.getTextWidth()/12;
			if (!(spacing.length() > 0)) {
				for (int i = 0; i < textWidth*3; i++) {
					spacing += " ";
				}
			}
			if (noticeList != null && noticeList.size() > 0) {
				for (int i = 0; i < noticeList.size(); i++) {
					noticeString += (spacing + "[公告]:"+noticeList.get(i).getTitle());
				}
			}else {
				noticeString += "[公告]:暂时没有公告信息,敬请期待!";
			}
			marqueetext.setText(noticeString);
			marqueetextStart();
			marqueetext.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (noticeList != null && noticeList.size() > 0) {
						Intent intent = new Intent(context,
								NoticeListActivity.class);
						intent.putExtra("noticeList", (Serializable) noticeList);
						context.startActivity(intent);
					}else {
						ShowUtils.showMsg(context, "没有公告信息,敬请期待!");
					}
				}
			});
		} else if (position == 2 || position == recommendList.size() + 3) {
			// 课程分类标题
			view = View.inflate(context, R.layout.layout_course_title, null);
			view.setFocusable(false);
			view.setClickable(false);
			ImageView iv_course_title = (ImageView) view.findViewById(R.id.iv_course_title);
			TextView ctll_text = (TextView) view.findViewById(R.id.ctll_text);
			iv_course_title.setImageResource(R.drawable.course_title);
			if (position == 2) {
				ctll_text.setText("推荐课程");
			} else {
				ctll_text.setText("免费课程");
			}
		} else {
			// 复用
			if (convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(context, R.layout.home_courseitem, null);
				holder = new ViewHolder();
				holder.courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
				holder.course_title = (TextView) view.findViewById(R.id.course_title);
//				holder.course_doc = (TextView) view.findViewById(R.id.course_doc);
				view.setTag(holder);
			}

			final String courseId;
			if (position < recommendList.size() + 3) {
				ImageUtils.setImage(recommendList.get(position - 3).getLogo(), holder.courseimage_item, R.drawable.img_course);
				holder.course_title.setText(recommendList.get(position - 3).getCourseName());
//				holder.course_doc.setText(recommendList.get(position - 3).getintro());
				courseId = recommendList.get(position - 3).getCourseId();
			} else {
				
				ImageUtils.setImage(freeList.get(position - 4 - recommendList.size()).getLogo(), holder.courseimage_item, R.drawable.img_course);
				holder.course_title.setText(freeList.get(position - 4 - recommendList.size()).getCourseName());
//				holder.course_doc.setText(freeList.get(position - 4 - recommendList.size()).getintro());
				courseId = freeList.get(position - 4 - recommendList.size()).getCourseId();
			}
			// courseidMap.put(position, courseId);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context,
							CourseIntroduceActivity.class);
					intent.putExtra("courseId", courseId);
					context.startActivity(intent);
				}
			});
		}
		return view;
	}
	public void marqueetextStart() {
		if (marqueetext != null) {
			marqueetext.startScroll();
		}
	}

	public void marqueetextStop() {
		if (marqueetext != null) {
			marqueetext.stopScroll();
		}
	}
	
	public void marqueetextPause() {
		if (marqueetext != null) {
			marqueetext.pauseScroll();
		}
	}

	public void marqueetextStartFor0() {
		if (marqueetext != null) {
			marqueetext.startFor0();
		}
	}
	private MarqueeText marqueetext;

	private class ViewHolder {
		ImageView courseimage_item;
		TextView course_title;
		//TextView course_doc;
	}
}
