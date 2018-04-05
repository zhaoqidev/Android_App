package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.CourseArchitectureActivity;
import cc.upedu.online.activity.CourseIntroduceActivity;
import cc.upedu.online.activity.NoticeListActivity;
import cc.upedu.online.activity.TelecastApplayActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.domin.CourseBeanNew;
import cc.upedu.online.domin.CourseBeanNew.Entity.CenterBannerItem;
import cc.upedu.online.domin.CourseBeanNew.Entity.SubjectItem;
import cc.upedu.online.domin.CourseItem;
import cc.upedu.online.domin.CourseListBean;
import cc.upedu.online.domin.NoticeItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.MarqueeText;
import cc.upedu.online.view.RollViewPager;
import cc.upedu.online.view.RollViewPager.onPageClick;
import cc.upedu.online.view.pullrefreshRecyclerview.DividerItemDecoration;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView.PullLoadMoreListener;
import cc.upedu.online.view.pullrefreshRecyclerview.RecyclerViewHeader;

/**
 * 首页
 * @author hui
 *
 */
public class CourseFragment extends BaseFragment implements PullLoadMoreListener{
	/**
	 * 自定义的ListView
	 */
	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;//可以下拉刷新，上拉加载的RecyclerView
	/**
	 * 轮播图的view
	 */
	private View rollView;
	/**
	 *  显示轮播点的布局
	 */
	@ViewInject(R.id.dots_ll)
	private LinearLayout dots_ll;
	/**
	 * 无数据时的占位图
	 */
	@ViewInject(R.id.ll_nodata)
	private LinearLayout ll_nodata;
	/**
	 *  显示图片文字的textview
	 */
	@ViewInject(R.id.top_title)
	private TextView top_title;
	/**
	 * 放置轮播图片位置的布局
	 */
	@ViewInject(R.id.top_viewpager)
	private LinearLayout top_viewpager;
	/**
	 *  设置放置点的集合
	 */
	private List<View> viewList = new ArrayList<View>();
	/**
	 *  传递图片对应的url的地址
	 */
	private List<String> urlImgList = new ArrayList<String>();
	/**
	 *  需要传递给viewPager显示图片关联文字的集合
	 */
	private List<String> titleList = new ArrayList<String>();
	/**
	 * 填充课程列表listview的adapter
	 */
	private CourseAdapterNewAdapter mCourseAdapterNewAdapter;

	/**
	 * 当前数据加载到第几页,默认是第一页
	 */
	private int currentPage = 1;
	private RollViewPager rollViewPager;
	private String totalPage;
	private List<CenterBannerItem> centerBannerList;
	private List<SubjectItem> subjectList;
	private List<NoticeItem> noticeList;
	private List<CourseItem> courseList;
	private boolean isFistLoad = true;
	private LayoutInflater inflater;
	
	boolean isAdd=false;//用来判断是否添加过头布局。
	/**
	 * 父界面
	 */
	private MainFragment mainFragment;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				setData();
				break;
			case 1:
				//判断请求的数据是否正确
				if ("true".equals(mCourseListBean.getSuccess())) {
					//判断请求的分页数据是否有第一页
					if (Integer.valueOf(mCourseListBean.getEntity().gettotalPage()) > 0) {
						//有数据时调用父类中修改title中标题的方法
						mainFragment.setTitleText(subjectId);
						if (courseList == null) {
							courseList = new ArrayList<CourseItem>();
						}
						//判断是否是上拉加载
						if (mPullLoadMoreRecyclerView.isLoadMore()) {//上拉加载
							courseList.addAll(mCourseListBean.getEntity().getCourseList());
							if (mCourseAdapterNewAdapter == null) {
								mCourseAdapterNewAdapter = new CourseAdapterNewAdapter(context);
								mPullLoadMoreRecyclerView.setAdapter(mCourseAdapterNewAdapter);
							} else {
								mCourseAdapterNewAdapter.notifyDataSetChanged();
							}
							mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
							
						}else {//不是上拉加载
							courseList.clear();
							courseList.addAll(mCourseListBean.getEntity().getCourseList());
							if (rollViewPager != null) {
								rollViewPager.stopRoll();
							}
							if (urlImgList != null) {
								urlImgList.clear();
							}
							if (titleList != null) {
								titleList.clear();
							}
							initRollView();
						}
					}else {
						ShowUtils.showMsg(context, "所选分类暂无课程,敬请期待!");
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
					}
				}else {
					ShowUtils.showMsg(context, mCourseBeanNew.getMessage());
					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
				}
				break;
			};
		}
	};

	public CourseFragment(MainFragment mainFragment) {
		this.mainFragment = mainFragment;
	}
	public CourseFragment() {
		// TODO Auto-generated constructor stub
	}
	private void setData(){
		if ("true".equals(mCourseBeanNew.getSuccess())) {
			totalPage = mCourseBeanNew.getEntity().getCourseTotalPage();
			//判断是否可以加载下一页
			if (currentPage < Integer.valueOf(totalPage)) {
				mPullLoadMoreRecyclerView.setHasMore(true);
			} else {
				mPullLoadMoreRecyclerView.setHasMore(false);
			}
			centerBannerList = mCourseBeanNew.getEntity().getCourseCenterBanner();
			if (centerBannerList == null) {
				centerBannerList = new ArrayList<CourseBeanNew.Entity.CenterBannerItem>();
			}
			subjectList = mCourseBeanNew.getEntity().getSubjectList();
			if (subjectList == null) {
				subjectList = new ArrayList<CourseBeanNew.Entity.SubjectItem>();
			}
			noticeList = mCourseBeanNew.getEntity().getNoticeList();
			if (noticeList == null) {
				noticeList = new ArrayList<NoticeItem>();
			}
			courseList = mCourseBeanNew.getEntity().getCourseList();
			if (courseList == null) {
				courseList = new ArrayList<CourseItem>();
			}
			isFistLoad = false;
			initRollView();
		}else {
			ShowUtils.showMsg(context, mCourseBeanNew.getMessage());
		}
	}
	private void initRollView() {	
		if (centerBannerList != null && centerBannerList.size() > 0) {
			for (int i = 0; i < centerBannerList.size(); i++) {
				urlImgList.add(centerBannerList.get(i).getImagesUrl());
				titleList.add(centerBannerList.get(i).getTitle());
			}
			
			initDot();
			rollViewPager = new RollViewPager(context,viewList,new onPageClick(){
				@Override
				public void onclick(int i) {
					String courseId = centerBannerList.get(i).getCourseId();
					if ("0".equals(courseId)) {
						Intent intent = new Intent(context, TelecastApplayActivity.class);
						intent.putExtra("courseId", "115");
						context.startActivity(intent);
					}else {
						if (!StringUtil.isEmpty(courseId)) {
							Intent intent = new Intent(context, CourseIntroduceActivity.class);
							intent.putExtra("courseId", courseId);
							context.startActivity(intent);
						}
					}
					
				}
			});
			rollViewPager.initTitleList(top_title, titleList);
			rollViewPager.initImgUrlList(urlImgList);
			rollViewPager.startRoll();
			
			// 先把轮播图中的控件清空，在把rollViewPager设置上去。
			top_viewpager.removeAllViews();
			top_viewpager.addView(rollViewPager);
			
			
			// 需要添加到listView上面去
//			if (ptrlv.getRefreshableView().getHeaderViewsCount() < 1) {
//				ptrlv.getRefreshableView().addHeaderView(rollView);
//				
//			}
			if (!isAdd) {
				isAdd=true;
				RecyclerViewHeader header= RecyclerViewHeader.getRecyclerViewHeader(context, rollView);
				header.attachTo(mPullLoadMoreRecyclerView.mRecyclerView);
			}
			
			
		}
		
		if (courseList.size() > 0) {
			if (mCourseAdapterNewAdapter == null) {
				mCourseAdapterNewAdapter = new CourseAdapterNewAdapter(context);
				mPullLoadMoreRecyclerView.setAdapter(mCourseAdapterNewAdapter);
				
			} else {
					mCourseAdapterNewAdapter.notifyDataSetChanged();
			}
		}
		if (mPullLoadMoreRecyclerView.isRefresh()) {
			mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
		}
	}
	@Override
	public View initView(LayoutInflater inflater) {
		//主布局的view
		View view = inflater.inflate(R.layout.refresh_loadmore_recyclerview, null);
		ViewUtils.inject(this, view);
		//轮播图的view
		rollView = View.inflate(context, R.layout.layout_roll_view, null);
		ViewUtils.inject(this, rollView);
		
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
		mPullLoadMoreRecyclerView.setLinearLayout();
		mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
		mPullLoadMoreRecyclerView.setIsRefresh(true);
		mPullLoadMoreRecyclerView.setItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
		return view;
	}
	private CourseBeanNew mCourseBeanNew;
	@Override
	public void initData() {
		//获取首页的数据
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_INDEXPAGE, context, ParamsMapUtil.getHomeCourse(context), new MyBaseParser<>(CourseBeanNew.class));
		DataCallBack<CourseBeanNew> homeCallBack = new DataCallBack<CourseBeanNew>() {

			@Override
			public void processData(CourseBeanNew object) {
				if (object==null) {
					if (mPullLoadMoreRecyclerView.isLoadMore()||mPullLoadMoreRecyclerView.isRefresh()) {
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
					} else {
						ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
					}
				}else {
					mCourseBeanNew = object;
					handler.obtainMessage(0).sendToTarget();
//					String stringDate = StringUtil.getStringDate();
//					ptrlv.setLastUpdatedLabel(stringDate.subSequence(stringDate.indexOf("-")+1, stringDate.lastIndexOf(":")));
				}
			}
		};
		getDataServer(requestVo, homeCallBack);
	}
	private CourseListBean mCourseListBean;
	private void initListData() {
		//获取课程列表的数据
		Map<String, String> requestDataMap;
		if ("-1".equals(subjectId)) {
			requestDataMap = ParamsMapUtil.getAllCourse(context, String.valueOf(currentPage));
		}else {
			requestDataMap = ParamsMapUtil.getSubjectCourse(context, subjectId, String.valueOf(currentPage));
		}
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE, context, requestDataMap, new MyBaseParser<>(CourseListBean.class));
		DataCallBack<CourseListBean> coursseDataCallBack = new DataCallBack<CourseListBean>() {

			@Override
			public void processData(CourseListBean object) {
				if (object==null) {
					if (mPullLoadMoreRecyclerView.isLoadMore()||mPullLoadMoreRecyclerView.isRefresh()) {
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
					} else {
						ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
					}
				}else {
					mCourseListBean = object;
					
					//当列表条目少于10条时，屏蔽下拉加载
					if (mCourseListBean.getEntity().getCourseList().size()<10) {
						mPullLoadMoreRecyclerView.setHasMore(false);
					}else {
						mPullLoadMoreRecyclerView.setHasMore(true);
					}
					
					handler.obtainMessage(1).sendToTarget();
//					String stringDate = StringUtil.getStringDate();
//					ptrlv.setLastUpdatedLabel(stringDate.subSequence(stringDate.indexOf("-")+1, stringDate.lastIndexOf(":")));
				}
			}
		};
		getDataServer(requestVo, coursseDataCallBack);
	}
	public void seacherCourse(String searchText){
		ShowUtils.showMsg(context, "搜索课程");
	}
	/**
	 * 填充课程列表listview的adapter
	 * @author hui
	 *
	 */
	public class CourseAdapterNewAdapter extends AbsRecyclerViewAdapter {
		Context context;
		private String spacing = "";

		public CourseAdapterNewAdapter(Context context) {
			this.context = context;
			if (inflater == null) {
				inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			}
		}
		
		class MyViewHolder extends RecyclerView.ViewHolder {
			RelativeLayout rl_course_item;//课程条目布局
			ImageView courseimage_item;
			TextView course_title;
			TextView theacher_Name;
			TextView course_count;
			TextView apply_count;
			
			LinearLayout ll_notice;//公告条目布局
			MarqueeText marqueetext;//跑马灯文字
			
			HorizontalScrollView hs_plate;//五大课程体系的布局
			LinearLayout ll_plate;

			public MyViewHolder(View view) {
				super(view);
				rl_course_item=(RelativeLayout) view.findViewById(R.id.rl_course_item);
				courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
				course_title = (TextView) view.findViewById(R.id.course_title);
				theacher_Name = (TextView) view.findViewById(R.id.theacher_Name);
				course_count = (TextView) view.findViewById(R.id.course_count);
				apply_count = (TextView) view.findViewById(R.id.apply_count);
				
				ll_notice=(LinearLayout) view.findViewById(R.id.ll_notice);
				marqueetext=(MarqueeText) view.findViewById(R.id.marqueetext);
				
				hs_plate=(HorizontalScrollView) view.findViewById(R.id.hs_plate);
				ll_plate=(LinearLayout) view.findViewById(R.id.ll_plate);
			}

		}
		@Override
		public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
			View view = LayoutInflater.from(viewGroup.getContext()).inflate(
					R.layout.layout_courser_notice_item, viewGroup, false);
			return new MyViewHolder(view);
		}
		@Override
		public void onBindViewHolder(ViewHolder viewHolder, int position) {
//			ArticleItem item = (ArticleItem) list.get(position);
			final MyViewHolder holder = (MyViewHolder) viewHolder;
			
			if (subjectList != null && subjectList.size() >0) {
				if (position == 0) {
					// 体系列表
					holder.hs_plate.setVisibility(View.VISIBLE);
					holder.rl_course_item.setVisibility(View.GONE);
					holder.ll_notice.setVisibility(View.GONE);
				
					for (int i = 0; i < subjectList.size(); i++) {
						final int index = i;
						View iv = View.inflate(context,
								R.layout.layout_architecture_icon, null);
						final ImageView architecture_image = (ImageView) iv
								.findViewById(R.id.architecture_image);
						
						ImageUtils.setImage(subjectList.get(index)
									.getSubjectLogo(), architecture_image, R.drawable.wodeimg_default);
						iv.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(context,CourseArchitectureActivity.class);
								intent.putExtra("subjectId",subjectList.get(index).getSubjectId());
								intent.putExtra("subjectLogo",subjectList.get(index).getSubjectLogo());
								intent.putExtra("subjectName",subjectList.get(index).getSubjectName());
								context.startActivity(intent);
							}
						});
						
						holder.ll_plate.addView(iv);
					}
				
				} else if (position == 1) {
					// 公告
					holder.ll_notice.setVisibility(View.VISIBLE);
					holder.hs_plate.setVisibility(View.GONE);
					holder.rl_course_item.setVisibility(View.GONE);
					String noticeString = "";
					if (!(spacing.length() > 0)) {
						spacing = "    ";
					}
					if (noticeList != null && noticeList.size() > 0) {
						for (int i = 0; i < noticeList.size(); i++) {
							if (i == 0) {
								noticeString += ("[公告]:"+noticeList.get(i).getTitle());
							}else {
								noticeString += (spacing + "[公告]:"+noticeList.get(i).getTitle());
							}
						}
					}else {
						noticeString += "[公告]:暂时没有公告信息,敬请期待!";
					}
					holder.marqueetext.setText(noticeString);
					marqueetextStart(holder.marqueetext);
					holder.ll_notice.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									NoticeListActivity.class);
							intent.putExtra("noticeList", (Serializable) noticeList);
							context.startActivity(intent);
						}
					});
				}else {
					//课程列表
					holder.rl_course_item.setVisibility(View.VISIBLE);
					holder.ll_notice.setVisibility(View.GONE);
					holder.hs_plate.setVisibility(View.GONE);
					
					ImageUtils.setImage(courseList.get(position-2).getLogo(), holder.courseimage_item, R.drawable.img_course);
					holder.course_title.setText(courseList.get(position- 2).getName());
					holder.theacher_Name.setText(courseList.get(position- 2).getTeacherList().get(0));
					holder.course_count.setText(courseList.get(position-2).getViewCount()+"人");
					
					final String courseId = courseList.get(position - 2).getCourseId();
					holder.rl_course_item.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									CourseIntroduceActivity.class);
							intent.putExtra("courseId", courseId);
							context.startActivity(intent);
						}
					});
				}
			}else {
				if (position == 0) {
					// 公告
					holder.ll_notice.setVisibility(View.VISIBLE);
					holder.hs_plate.setVisibility(View.GONE);
					holder.rl_course_item.setVisibility(View.GONE);
					String noticeString = "";
					if (!(spacing.length() > 0)) {
						spacing = "    ";
					}
					if (noticeList != null && noticeList.size() > 0) {
						for (int i = 0; i < noticeList.size(); i++) {
							if (i == 0) {
								noticeString += ("[公告]:"+noticeList.get(i).getTitle());
							}else {
								noticeString += (spacing + "[公告]:"+noticeList.get(i).getTitle());
							}
						}
					}else {
						noticeString += "[公告]:暂时没有公告信息,敬请期待!";
					}
					holder.marqueetext.setText(noticeString);
					marqueetextStart(holder.marqueetext);
					holder.ll_notice.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									NoticeListActivity.class);
							intent.putExtra("noticeList", (Serializable) noticeList);
							context.startActivity(intent);
						}
					});
				}else {
					//课程列表
					holder.rl_course_item.setVisibility(View.VISIBLE);
					holder.ll_notice.setVisibility(View.GONE);
					holder.hs_plate.setVisibility(View.GONE);
					
					ImageUtils.setImage(courseList.get(position-1).getLogo(), holder.courseimage_item, R.drawable.img_course);
					holder.course_title.setText(courseList.get(position-1).getName());
					holder.theacher_Name.setText(courseList.get(position-1).getTeacherList().get(0));
					holder.course_count.setText(courseList.get(position-1).getViewCount()+"人");
					
					final String courseId = courseList.get(position - 1).getCourseId();
					holder.rl_course_item.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context,
									CourseIntroduceActivity.class);
							intent.putExtra("courseId", courseId);
							context.startActivity(intent);
						}
					});
				}
			}
			
			super.onBindViewHolder(viewHolder, position);	
		}
		
		@Override
		public int getItemCount() {
			if (subjectList != null && subjectList.size() >0) {
				// +2 表示推荐课程列表分两类显示,添加1个体系列表 +1个公告栏
				return courseList.size() + 1 + 1;
			}else {
				// +2 表示推荐课程列表分两类显示,添加1个公告栏
				return courseList.size() + 1;
			}
		}

		public void marqueetextStart(MarqueeText marqueetext) {
			if (marqueetext != null) {
				marqueetext.startScroll();
			}
		}

		public void marqueetextStop(MarqueeText marqueetext) {
			if (marqueetext != null) {
				marqueetext.stopScroll();
			}
		}
		
		public void marqueetextPause(MarqueeText marqueetext) {
			if (marqueetext != null) {
				marqueetext.pauseScroll();
			}
		}

		public void marqueetextStartFor0(MarqueeText marqueetext) {
			if (marqueetext != null) {
				marqueetext.startFor0();
			}
		}

	}
	@Override
	public void onStart() {
		if (rollViewPager != null) {
			rollViewPager.startRoll();
		}
		if (mCourseAdapterNewAdapter != null) {
//			mCourseAdapterNewAdapter.marqueetextStart(mCourseAdapterNewAdapter.);
		}
		super.onStart();
	}
	@Override
	public void onPause() {
		if (rollViewPager != null) {
			rollViewPager.stopRoll();
		}
		if (mCourseAdapterNewAdapter != null) {
//			mCourseAdapterNewAdapter.marqueetextPause();
		}
		super.onPause();
	}
	@Override
	public void onStop() {
		
		super.onStop();
	}
	/**
	 * 实例化点操作
	 */
	public void initDot() {
		if (dots_ll != null) {
			dots_ll.removeAllViews();
		}
		if (viewList != null) {
			viewList.clear();
		}

		for (int i = 0; i < urlImgList.size(); i++) {
			View view = new View(context);
			if (i == 0) {
				view.setBackgroundResource(R.drawable.dot_focus);
			} else {
				view.setBackgroundResource(R.drawable.dot_normal);
			}

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					CommonUtil.dip2px(context, 6),
					CommonUtil.dip2px(context, 6));
			view.setLayoutParams(layoutParams);
			layoutParams.setMargins(5, 0, 5, 0);
			dots_ll.addView(view);
			viewList.add(view);
		}
	}
	/**
	 * 下拉列表中课程分类的分类id
	 */
	public String subjectId = "-1";

	public String getSubjectId() {
		return subjectId;
	}
	/**
	 * 设置当前界面中要显示的数据是属于哪个分类的分类id
	 * @param subjectId 分类id
	 */
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public void onRefresh() {
		currentPage = 1;
		if (isFistLoad) {
			initData();
		}else {
			initListData();
		}
		
	}
	@Override
	public void onLoadMore() {
    	if (!StringUtil.isEmpty(totalPage)) {
			if (currentPage < Integer.parseInt(totalPage)) {
				currentPage++;
				initListData();
			}else {
				ShowUtils.showMsg(context, "没有更多数据");
				mPullLoadMoreRecyclerView.setHasMore(false);
				mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
			}
		}else {
			onRefresh();
		}
	}
	/**
	 * 根据特定的key,重新加载数据
	 * 
	 * @param key
	 */
	public void initNewData(String key) {
		// TODO Auto-generated method stub
		onRefresh();
	}
}
