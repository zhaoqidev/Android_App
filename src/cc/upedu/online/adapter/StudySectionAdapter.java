package cc.upedu.online.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.CourseStudyActivity;
import cc.upedu.online.domin.CourseSectionListBean.Entity.CatalogItem;
import cc.upedu.online.domin.CourseSectionListBean.Entity.CatalogItem.ChildListItem;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;

public class StudySectionAdapter extends AbsRecyclerViewAdapter {
	private int count;
	private View view;//条目view

	private List<List<CatalogItem>> list;

	//记录课程标题在总列表中的位置
	private List<Integer> courseTitleIndexs = new ArrayList<Integer>();
	//记录课程标题在bean集合中的位置(二位数字字符串来表示)
	private List<String> courseTitleIndex = new ArrayList<String>();
	//记录章标题在总列表中的位置
	private List<Integer> chapterTitleIndexs = new ArrayList<Integer>();
	//记录章标题在bean集合中的位置(二位数字字符串来表示)
	private List<String> chapterTitleIndex = new ArrayList<String>();
	//记录章节中课程的个数
	private List<Integer> sectionSize = new ArrayList<Integer>();
	
	public StudySectionAdapter(Context context, List<List<CatalogItem>> list) {
		this.context = context;
		this.list=list;
		resId=R.layout.layout_studysection_item;
		count = 0;
		for (int i = 0; i < list.size(); i++) {
			count++;//课程标题个数
			courseTitleIndex.add(String.valueOf(i)+String.valueOf(0));
			courseTitleIndexs.add(count-1);
			if (list.get(i).size() == 1) {
				//该课程没有章节信息,待处理
			}else {
				for (int j = 0; j < list.get(i).size(); j++) {
					if (j != 0) {
						count++;//章标题个数
						chapterTitleIndexs.add(count-1);
						chapterTitleIndex.add(String.valueOf(i)+String.valueOf(j));
						int size = list.get(i).get(j).getChildList().size();
						sectionSize.add(size);
						
						//节标题个数
						count += size;
					}
				}
			}
		}
	}

	private boolean isDelaying = false;
	/*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
            super(millisInFuture, countDownInterval);     
        }     
        @Override     
        public void onFinish() {
        	isDelaying = !isDelaying;
        }     
        @Override     
        public void onTick(long millisUntilFinished) {
        }    
    }  
    
    class MyViewHolder extends RecyclerView.ViewHolder {

    	ImageView iv_left;
		TextView tv_name;
		TextView tv_right;

		public MyViewHolder(View view) {
			super(view);
			iv_left = (ImageView) view.findViewById(R.id.iv_left);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_right = (TextView) view.findViewById(R.id.tv_right);
		}

	}


    @Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		final MyViewHolder holder = (MyViewHolder) viewHolder;

		if (courseTitleIndexs.contains(position)) {//判断是否是课程标题
//			View courseTitleView = View.inflate(context, R.layout.layout_coursetitle_item, null);
//			TextView tv_coursetitle = (TextView) courseTitleView.findViewById(R.id.tv_coursetitle);
			holder.iv_left.setVisibility(View.GONE);
			holder.tv_right.setVisibility(View.GONE);
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_itemname));
			//根据课程标题在课程标题位置集合中的位置获取其在bean集合中的位置
			String indexs = courseTitleIndex.get(courseTitleIndexs.indexOf(position));
			if (indexs.length() == 2) {
				holder.tv_name.setText(( list.get(Integer.valueOf(indexs.substring(0, 1)))).get(Integer.valueOf(indexs.substring(1))).getCourseName());
			}
			
//			viewHolder.courseTitleView.setFocusable(false);
//			viewHolder.courseTitleView.setClickable(false);
		}else if (chapterTitleIndexs.contains(position)) {//判断是否是章标题
//			View chapterTitleView = View.inflate(context, R.layout.layout_chaptertitle_item, null);
//			TextView tv_chaptertitle = (TextView) chapterTitleView.findViewById(R.id.tv_chaptertitle);
//			ImageView iv = (ImageView) chapterTitleView.findViewById(R.id.iv);
			final String indexs = chapterTitleIndex.get(chapterTitleIndexs.indexOf(position));
			holder.iv_left.setVisibility(View.GONE);
			holder.tv_right.setVisibility(View.GONE);
			holder.tv_name.setText(list.get(Integer.valueOf(indexs.substring(0, 1))).get(Integer.valueOf(indexs.substring(1))).getVideoName());
			//判断是从简介界面进来还是从学习记录界面进来
			if (StringUtil.isEmpty(SharedPreferencesUtil.getInstance().spGetString("kpointId"))) {//简介且之前的条目没有视频
				//是否有视频
				if (StringUtil.isEmpty(list.get(Integer.valueOf(indexs.substring(0, 1))).get(Integer.valueOf(indexs.substring(1))).getVideoUrl())) {//无
//					iv.setVisibility(View.INVISIBLE);
					holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_itemname));
				}else {//有
//					iv.setVisibility(View.VISIBLE);
					holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_chaptertitle));
					CatalogItem catalogItem = list.get(Integer.valueOf(indexs.substring(0, 1))).get(Integer.valueOf(indexs.substring(1)));
							SharedPreferencesUtil.getInstance().editPutString("kpointId", catalogItem.getKpointId());
					SharedPreferencesUtil.getInstance().editPutString("studySectionPosition", String.valueOf(position));
					SharedPreferencesUtil.getInstance().editPutString("srtUrl", catalogItem.getSrturl());
					SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", catalogItem.getVideoUrl());
					SharedPreferencesUtil.getInstance().editPutString("videoType", catalogItem.getVideoType());
					((CourseStudyActivity)context).autoPlay();
				}
			}else {//学习记录或简介且之前的条目有视频
				//判断该章是不是记录的学习点
				CatalogItem catalogItem = list.get(Integer.valueOf(indexs.substring(0, 1))).get(Integer.valueOf(indexs.substring(1)));
				if (SharedPreferencesUtil.getInstance().spGetString("kpointId").equals(catalogItem.getKpointId())) {
					SharedPreferencesUtil.getInstance().editPutString("studySectionPosition", String.valueOf(position));
					SharedPreferencesUtil.getInstance().editPutString("srtUrl", catalogItem.getSrturl());
					SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", catalogItem.getVideoUrl());
					SharedPreferencesUtil.getInstance().editPutString("videoType", catalogItem.getVideoType());
					((CourseStudyActivity)context).autoPlay();
					holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_chaptertitle));
				}else {//不是
					holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_itemname));
				}
			}
			if (!StringUtil.isEmpty(list.get(Integer.valueOf(indexs.substring(0, 1))).get(Integer.valueOf(indexs.substring(1))).getVideoUrl())) {
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (!isDelaying) {
							isDelaying = !isDelaying;
							MyCount mc = new MyCount(3000, 1000);  
							mc.start();
							CatalogItem catalogIte = list.get(Integer.valueOf(indexs.substring(0, 1))).get(Integer.valueOf(indexs.substring(1)));
							String videoUrl = catalogIte.getVideoUrl();
							String kpointId = catalogIte.getKpointId();
							if (!StringUtil.isEmpty(videoUrl)) {
								// TODO Auto-generated method stub
								SharedPreferencesUtil.getInstance().editPutString("kpointId", kpointId);
								SharedPreferencesUtil.getInstance().editPutString("videoType",catalogIte.getVideoType());
								SharedPreferencesUtil.getInstance().editPutString("studySectionPosition", String.valueOf(position));
								SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", videoUrl);
								SharedPreferencesUtil.getInstance().editPutString("srtUrl", catalogIte.getSrturl());
								HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
								int node = -1;
								if (map.containsKey("kid"+kpointId)){
									node = map.get("kid"+kpointId);
								}
								((CourseStudyActivity)context).playCourseVideo(videoUrl, true, node);
								notifyDataSetChanged();
							}else {
								ShowUtils.showMsg(context, "视频数据异常!");
							}
						}else {
							ShowUtils.showMsg(context, "不能频繁切换视频!");
						}
					}
				});
			}
		}else {//剩下的是节条目

			holder.iv_left.setVisibility(View.VISIBLE);
			holder.tv_right.setVisibility(View.VISIBLE);
			holder.tv_name.setTextColor(context.getResources().getColor(R.color.color_textcolor));
			//在课程章集合中判断属于哪个章
			int currentChapter = -1;
			for (int i = 1; i < chapterTitleIndexs.size()+1; i++) {
				if (i != chapterTitleIndexs.size()) {
					if (position < chapterTitleIndexs.get(i)) {
						currentChapter = chapterTitleIndexs.get(i-1);
						break;
					}
				}else {
					currentChapter = chapterTitleIndexs.get(i-1);
				}
			}
			
			final String indexs = chapterTitleIndex.get(chapterTitleIndexs.indexOf(currentChapter));
			final ChildListItem childListItem = list.get(Integer.valueOf(indexs.substring(0, 1))).get(Integer.valueOf(indexs.substring(1))).getChildList().get(position-currentChapter-1);
			holder.tv_name.setText(childListItem.getVideoName());
			holder.tv_right.setText(childListItem.getVidioLength());
			
			if (StringUtil.isEmpty(SharedPreferencesUtil.getInstance().spGetString("kpointId"))) {//判断是从简介界面且之前的章上无视频
				SharedPreferencesUtil.getInstance().editPutString("kpointId", childListItem.getVideoId());
				SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", childListItem.getVideoUrl());
				SharedPreferencesUtil.getInstance().editPutString("srtUrl", childListItem.getSrturl());
				SharedPreferencesUtil.getInstance().editPutString("studySectionPosition", String.valueOf(position));
				SharedPreferencesUtil.getInstance().editPutString("videoType", childListItem.getVideoType());
				((CourseStudyActivity)context).autoPlay();
				holder.iv_left.setImageResource(R.drawable.bofang);
			}else {
				if (SharedPreferencesUtil.getInstance().spGetString("kpointId").equals(childListItem.getVideoId())) {
					SharedPreferencesUtil.getInstance().editPutString("studySectionPosition", String.valueOf(position));
					SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", childListItem.getVideoUrl());
					SharedPreferencesUtil.getInstance().editPutString("srtUrl", childListItem.getSrturl());
					SharedPreferencesUtil.getInstance().editPutString("videoType", childListItem.getVideoType());
					((CourseStudyActivity)context).autoPlay();
					holder.iv_left.setImageResource(R.drawable.bofang);
				}else {
					holder.iv_left.setImageResource(R.drawable.bofang_details);
				}
			}
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!isDelaying) {
			        	isDelaying = !isDelaying;
						MyCount mc = new MyCount(3000, 1000);  
						mc.start();
						String videoId = childListItem.getVideoId();
						if (!StringUtil.isEmpty(videoId)) {
							SharedPreferencesUtil.getInstance().editPutString("kpointId", videoId);
							SharedPreferencesUtil.getInstance().editPutString("studySectionPosition", String.valueOf(position));
							SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", childListItem.getVideoUrl());
							SharedPreferencesUtil.getInstance().editPutString("srtUrl", childListItem.getSrturl());
							SharedPreferencesUtil.getInstance().editPutString("videoType", childListItem.getVideoType());
							HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
							int node = -1;
							if (map.containsKey("kid"+videoId)){
								node = map.get("kid"+videoId);
							}
							((CourseStudyActivity)context).playCourseVideo(childListItem.getVideoUrl(),true,node);
							notifyDataSetChanged();
						}else {
							ShowUtils.showMsg(context, "视频数据异常");
						}
					}else {
			        	ShowUtils.showMsg(context, "不能频繁切换视频!");
					}
				}
			});
		}
	
		super.onBindViewHolder(viewHolder, position);	
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	
	}
	
	@Override
	public int getItemCount() {
		return count;
	}

	
}
