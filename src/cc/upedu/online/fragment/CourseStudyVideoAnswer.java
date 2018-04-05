package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.TelecastApplayActivity;
import cc.upedu.online.adapter.TextAnswerAdapter;
import cc.upedu.online.adapter.VideoAnswerAdapter;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.domin.TextAnswerListBean;
import cc.upedu.online.domin.TextAnswerListBean.Entity.TextAnswerItem;
import cc.upedu.online.domin.VideoAnswerListBean;
import cc.upedu.online.domin.VideoAnswerListBean.Entity.Notice;
import cc.upedu.online.domin.VideoAnswerListBean.Entity.VideoAnswerItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.MarqueeText;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView;

/**
 * 课程学习界面--答疑。文字答疑与视频答疑都在该界面中
 * @author Administrator
 *
 */
public class CourseStudyVideoAnswer extends BaseFragment implements OnClickListener {
	View view;
	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;
	
	private VideoAnswerListBean mVideoAnswerListBean;
	private VideoAnswerAdapter mVideoAnswerAdapter;
	private List<VideoAnswerItem> mVideoAnswerList = new ArrayList<VideoAnswerItem>();

	private TextAnswerAdapter mTextAnswerAdapter;
	private TextAnswerListBean mTextAnswerListBean;
	private List<TextAnswerItem> mTextAnswerList = new ArrayList<TextAnswerItem>();

	private LinearLayout ll_nodata;

	//当前数据加载到哪个page
	private int currentPage = 1;
	private String totalPage;
	
	private MarqueeText marqueetext;//公告列表
	private LinearLayout ll_notice;//公告布局
	private Notice notice;
	
	private Button button_answer;
	private boolean isTextAnswer=true;
	
	public CourseStudyVideoAnswer(Context context, String courseId) {
		this.courseId = courseId;
		view = View.inflate(context, R.layout.layout_study_answer, null);
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
	}

	@Override
	protected View initView(LayoutInflater inflater) {
		marqueetext = (MarqueeText) view.findViewById(R.id.marqueetext);
		ll_notice=(LinearLayout) view.findViewById(R.id.ll_notice);
		
		button_answer=(Button) view.findViewById(R.id.button_answer);
		button_answer.setOnClickListener(this);
		mPullLoadMoreRecyclerView.setLinearLayout();
		mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
		mPullLoadMoreRecyclerView.setIsRefresh(true);
//		mPullLoadMoreRecyclerView.setItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));//设置分隔线
		
		ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
		return view;
	}
	
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if ("true".equals(mVideoAnswerListBean.getSuccess())) {
					if (mPullLoadMoreRecyclerView.isLoadMore()) {
						mVideoAnswerList.addAll(mVideoAnswerListBean.getEntity().getQaVideoList());
						if (mVideoAnswerList.size() > 0) {
							if (mVideoAnswerAdapter == null) {
								mVideoAnswerAdapter = new VideoAnswerAdapter(context, mVideoAnswerList);
								mPullLoadMoreRecyclerView.setAdapter(mVideoAnswerAdapter);
							} else {
								mVideoAnswerAdapter.notifyDataSetChanged();
							}

						}
						//判断是否可以加载下一页
						if (currentPage < Integer.valueOf(totalPage)) {
							mPullLoadMoreRecyclerView.setHasMore(true);
						} else {
							mPullLoadMoreRecyclerView.setHasMore(false);
						}

						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载更多
					} else {
						mVideoAnswerList.clear();
						setData();
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束下拉刷新
					}
				} else {
					ShowUtils.showMsg(context, mVideoAnswerListBean.getMessage());
					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
				}
				break;
				
			case 1:
				if ("true".equals(mTextAnswerListBean.getSuccess())) {
					if (mPullLoadMoreRecyclerView.isLoadMore()) {
						mTextAnswerList.addAll(mTextAnswerListBean.getEntity().getQaWordsList());
						if (mTextAnswerList.size() > 0) {
							if (mTextAnswerAdapter == null) {
								mTextAnswerAdapter = new TextAnswerAdapter(context, mTextAnswerList);
								mPullLoadMoreRecyclerView.setAdapter(mTextAnswerAdapter);
							} else {
								mTextAnswerAdapter.notifyDataSetChanged();
							}
						}
						//判断是否可以加载下一页
						if (currentPage < Integer.valueOf(totalPage)) {
							mPullLoadMoreRecyclerView.setHasMore(true);
						} else {
							mPullLoadMoreRecyclerView.setHasMore(false);
						}

						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载更多
					} else {
						mTextAnswerList.clear();
						setData();
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束下拉刷新
					}
				} else {
					ShowUtils.showMsg(context, mTextAnswerListBean.getMessage());
					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
				}
			
				break;
			}
			
		}
	};
	
	private void setData() {
		if (isTextAnswer) {
			totalPage = mTextAnswerListBean.getEntity().getTotalPage();
			if (Integer.parseInt(totalPage) > 0) {
			    ll_nodata.setVisibility(View.GONE);
				mTextAnswerList.addAll(mTextAnswerListBean.getEntity().getQaWordsList());
			} else {
				// 做无数据时的页面显示操作
				// ShowUtils.showMsg(context, "暂时没有数据哦~");
				   ll_nodata.setVisibility(View.VISIBLE);
			}

			mTextAnswerAdapter = new TextAnswerAdapter(context, mTextAnswerList);
			mPullLoadMoreRecyclerView.setAdapter(mTextAnswerAdapter);
			// 为列表加入从上至下依次显示动画
			// AnimationAdapter animAdapter = new
			// AlphaInAnimationAdapter(mTextAnswerAdapter);
			// animAdapter.setAbsListView(ptrlv);
			// ptrlv.setAdapter(animAdapter);

			mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束下拉刷新
		} else {
			
			totalPage = mVideoAnswerListBean.getEntity().getTotalPage();

			if (Integer.parseInt(totalPage) > 0) {
			    ll_nodata.setVisibility(View.GONE);
				mVideoAnswerList.addAll(mVideoAnswerListBean.getEntity()
						.getQaVideoList());
			} else {
				// 做无数据时的页面显示操作
				// ShowUtils.showMsg(context, "暂时没有数据哦~");
				ll_nodata.setVisibility(View.VISIBLE);
			}

			mVideoAnswerAdapter = new VideoAnswerAdapter(context,mVideoAnswerList);
			mPullLoadMoreRecyclerView.setAdapter(mVideoAnswerAdapter);
			//
			mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束下拉刷新
			
//			ptrlv.setOnItemClickListener(new MyItemClickListener() {
//				
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					// TODO Auto-generated method stub
//					if (!isDelaying) {
//						isDelaying = !isDelaying;
//						MyCount mc = new MyCount(3000, 1000);  
//						mc.start();
//						OnlineApp.myApp.edit.putString("videoType", mVideoAnswerList.get(position-1).getVideoType()).commit();
//						((CourseStudyActivity)context).playCourseVideo(mVideoAnswerList.get(position-1).getVideourl(),false);
//					}else {
//						ShowUtils.showMsg(context, "不能频繁切换视频!");
//					}
//				}
//			});
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
	private String courseId;

	@Override
	public void initData() {
		if (isTextAnswer) {
			ll_notice.setVisibility(View.GONE);
			//获取文字答疑列表的数据
			Map<String, String> requestDataMap = ParamsMapUtil.getTextAnswer(context, courseId, String.valueOf(currentPage));
			RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_TEXTANSWER, context, requestDataMap, new MyBaseParser<>(TextAnswerListBean.class));
			DataCallBack<TextAnswerListBean> textAnswerDataCallBack = new DataCallBack<TextAnswerListBean>() {

				@Override
				public void processData(TextAnswerListBean object) {
					if (object == null) {
						if (mPullLoadMoreRecyclerView.isLoadMore()|| mPullLoadMoreRecyclerView.isRefresh()) {
							mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
						} else {
							ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
						}
					} else {
						mTextAnswerListBean = object;
						handler.obtainMessage(1).sendToTarget();
					}
				}
			};
			getDataServer(requestVo, textAnswerDataCallBack);

		}else {
			ll_notice.setVisibility(View.VISIBLE);
			//获取视频答疑列表的数据
			Map<String, String> requestDataMap = ParamsMapUtil.getTextAnswer(context, courseId, String.valueOf(currentPage));
			RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_VIDEOANSWER, context, requestDataMap, new MyBaseParser<>(VideoAnswerListBean.class));
			DataCallBack<VideoAnswerListBean> videoAnswerDataCallBack = new DataCallBack<VideoAnswerListBean>() {
				@Override
				public void processData(VideoAnswerListBean object) {
					if (object==null) {

						if (mPullLoadMoreRecyclerView.isLoadMore()||mPullLoadMoreRecyclerView.isRefresh()) {
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
					} else {
						ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
						}
					}
					else {
						mVideoAnswerListBean = object;
						handler.obtainMessage(0).sendToTarget();
						getNotice();
					}
				}
			};
			getDataServer(requestVo, videoAnswerDataCallBack);
		}
		
	}

	protected void getNewData() {

			currentPage = 1;
			initData();
	}
	
	/**
	 * 实现刷新和加载逻辑的回调
	 * @author Administrator
	 *
	 */
	class PullLoadMoreListener implements PullLoadMoreRecyclerView.PullLoadMoreListener {
        @Override
        public void onRefresh() {
        	currentPage=1;
    		initData();
         }

        @Override
        public void onLoadMore() {
        	if (!StringUtil.isEmpty(totalPage)) {
				if (currentPage < Integer.parseInt(totalPage)) {
					currentPage++;
					initData();
				}else {
					ShowUtils.showMsg(context, "没有更多数据");
					mPullLoadMoreRecyclerView.setHasMore(false);
					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
				}
			}else {
				onRefresh();
			}
        }
    }
	
	/**
	 * 设置是否可刷新
	 * @param enable
	 */
	 public void setPullRefreshEnable(boolean enable){
		 mPullLoadMoreRecyclerView.setPullRefreshEnable(enable);
	 }
	
	public void getData() {
		currentPage=1;
		initData();
	}
	
	/**
	 * 滚动公告
	 */
	private void getNotice() {
		notice=mVideoAnswerListBean.getEntity().getNotice();
		String noticeString = "";
		if (notice != null && !StringUtil.isEmpty(notice.getTitle())) {
			noticeString += ("[公告]:"+notice.getTitle());
		}else {
			noticeString += "[公告]:暂时没有公告信息,敬请期待!";
		}
		marqueetext.setText(noticeString);
		marqueetextStart();
		marqueetext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (notice != null && !StringUtil.isEmpty(notice.getTitle())) {
					Intent intent = new Intent(context,TelecastApplayActivity.class);
					intent.putExtra("courseId", notice.getLiveId());
					context.startActivity(intent);
				}else {
					ShowUtils.showMsg(context, "没有公告信息,敬请期待!");
				}
			}
		});
	

	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_answer:
			
			if (isTextAnswer) {
				button_answer.setText("视频");
			}else {
				button_answer.setText("文字");
			}
			isTextAnswer = !isTextAnswer;
			getNewData();
		break;
		default:
			break;
		}
		
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

}
