package cc.upedu.online.fragment;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.TelecastNoteAdapter;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.domin.NoteListBean;
import cc.upedu.online.domin.NoteListBean.Entity.NoticeItem;
import cc.upedu.online.domin.NoteUserBean;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.pullrefreshRecyclerview.DividerItemDecoration;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView.PullLoadMoreListener;
/**
 * 直播笔记界面
 * @author Administrator
 *
 */
public class TelecastNoteFragment extends BaseFragment implements OnClickListener,PullLoadMoreListener{
	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;//可以下拉刷新，上拉加载的RecyclerView
	private TelecastNoteAdapter adapter;
	private NoteListBean bean;
//	private LinearLayout ll_nodata;
	private FloatingActionButton fab_writenote;
	private LinearLayout ll_writenotice;
	private FrameLayout fl_content;
	private EditText et_content;
	private Button bt_save;
	private boolean isShowWriteNotice = false;
	//记录是否是下拉刷新操作
	boolean isPullDownToRefresh = false;
	//记录是否是下拉加载操作
	boolean isPullUpToRefresh = false;
	//当前数据加载到哪个page
	private int currentPage = 1;
	private String totalPage;
	private List<NoticeItem> list = new ArrayList<NoticeItem>();
	private String userId;
	private String courseId;

	public TelecastNoteFragment(Context context, String courseId) {
		// TODO Auto-generated constructor stub
		this.courseId = courseId;
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if ("true".equals(bean.getSuccess())) {
					if (mPullLoadMoreRecyclerView.isLoadMore()) {
						list.addAll(bean.getEntity().getNoteList());
						if (list.size() > 0) {
							if (adapter == null) {
								adapter = new TelecastNoteAdapter(context, list);
								mPullLoadMoreRecyclerView.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
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
						list.clear();
						setData();
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束下拉刷新
					}
				} else {
					ShowUtils.showMsg(context, bean.getMessage());
					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
				}
				
//				onItemClick();//添加条目点击事件的监听
			
				break;
			case 1:
				if (Boolean.valueOf(mNoteUserBean.getSuccess())) {
					if (mNoteUserBean.getEntity() != null) {
						if (!StringUtil.isEmpty(mNoteUserBean.getEntity().getContent())) {
							et_content.setText(mNoteUserBean.getEntity().getContent());
						}
					}
				}else {
					ShowUtils.showMsg(context, mNoteUserBean.getMessage());
				}
				break;
			}
		}
	};

	private NoteUserBean mNoteUserBean;
	private int noticeLayoutHeight;
	@Override
	public void initData() {
		//获取个人课程笔记的数据
		if (!StringUtil.isEmpty(courseId)) {
			Map<String, String> requestDataMap = ParamsMapUtil.getCourseNotice(context, String.valueOf(currentPage), courseId,userId);
			RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_NOTICES, context, requestDataMap, new MyBaseParser<>(NoteListBean.class));
			DataCallBack<NoteListBean> noticeDataCallBack = new DataCallBack<NoteListBean>() {
				
				@Override
				public void processData(NoteListBean object) {
					if (object==null) {
						if (mPullLoadMoreRecyclerView.isLoadMore()||mPullLoadMoreRecyclerView.isRefresh()) {
							mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
						} else {
							ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
						}
					}else {
						bean = object;
						handler.obtainMessage(0).sendToTarget();
					}
				}
			};
			getDataServer(requestVo, noticeDataCallBack);
		}else {
			ShowUtils.showMsg(context, "请选择课程章节");
		}
	}

	@Override
	protected View initView(LayoutInflater inflater) {
		View view = View.inflate(context, R.layout.layout_telecast_note, null);
		userId=UserStateUtil.getUserId();
		
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
		mPullLoadMoreRecyclerView.setLinearLayout();
		mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
		mPullLoadMoreRecyclerView.setIsRefresh(true);
		mPullLoadMoreRecyclerView.setItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
		
		ll_writenotice = (LinearLayout) view.findViewById(R.id.ll_writenotice);
		ll_writenotice.measure(0, 0); // 计算子项View 的宽高
		ll_writenotice.setVisibility(View.GONE);
		noticeLayoutHeight = ll_writenotice.getMeasuredHeight();
		fl_content = (FrameLayout) view.findViewById(R.id.fl_content);
		bt_save = (Button) view.findViewById(R.id.bt_save);
		et_content = (EditText) view.findViewById(R.id.et_content);
//		ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
		fab_writenote=(FloatingActionButton) view.findViewById(R.id.fab_writenote);
		
		getOldNoteData();
		bt_save.setOnClickListener(this);
		fab_writenote.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_save:
			String content = et_content.getText().toString().trim();
			String status = "0";//0表示公开,1表示不公开
			if (!StringUtil.isEmpty(content)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("courseId", courseId);
				map.put("userId", userId);
				map.put("kpointId", "0");
				map.put("content", content);
				map.put("status", status);
				UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.SAVE_NOTE_TELECAST_COURSE,map , new UploadDataCallBack() {

					@Override
					public void onUploadDataSuccess() {
						// TODO Auto-generated method stub
						ShowUtils.showMsg(context, "保存笔记成功");
//						et_content.setText("");
						hideWriteNote();
						initData();
					}

					@Override
					public void onUploadDataFailure() {
						// TODO Auto-generated method stub
						
					}
				});
			}else {
				ShowUtils.showMsg(context, "您还没有写笔记,不能保存.");
			}
			break;
		case R.id.fab_writenote:
			showWriteNote();
			break;
		}
	}
	private void showWriteNote() {
		// TODO Auto-generated method stub
		ll_writenotice.setVisibility(View.VISIBLE);
		fab_writenote.setVisibility(View.GONE);
		isShowWriteNotice = !isShowWriteNotice;
		Animation inAnimation = AnimationUtils.loadAnimation(context, R.anim.note_in_right);
		ll_writenotice.startAnimation(inAnimation);
		inAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				animation.cancel();
			}
		});
		TranslateAnimation downAnimation = new TranslateAnimation(0, 0, -noticeLayoutHeight, 0);
		downAnimation.setDuration(500);
		downAnimation.setFillEnabled(true);
		downAnimation.setFillAfter(false);
		fl_content.startAnimation(downAnimation);
//		downAnimation.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				// TODO Auto-generated method stub
//				animation.cancel();
//			}
//		});
	}
	private void hideWriteNote() {
		// TODO Auto-generated method stub
		fab_writenote.setVisibility(View.VISIBLE);
		isShowWriteNotice = !isShowWriteNotice;
		
		TranslateAnimation upAnimation = new TranslateAnimation(0, 0, 0, -noticeLayoutHeight);
		upAnimation.setDuration(500);
		upAnimation.setFillAfter(true);
		fl_content.startAnimation(upAnimation);
		upAnimation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				Animation outAnimation = AnimationUtils.loadAnimation(context, R.anim.note_out_right);
				ll_writenotice.startAnimation(outAnimation);
				outAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						animation.cancel();
						ll_writenotice.setVisibility(View.GONE);
					}
				});
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				animation.cancel();
			}
		});
	}
	public void onKeyDown() {
		// TODO Auto-generated method stub
		if (isShowWriteNotice) {
			hideWriteNote();
		}
	}
	private void setData() {
		totalPage = bean.getEntity().getTotalPage();
		if (currentPage < Integer.valueOf(totalPage)) {
			mPullLoadMoreRecyclerView.setHasMore(true);
		}
		
		if (Integer.parseInt(totalPage) > 0) {
			list.addAll(bean.getEntity().getNoteList());
		}else {
			//做无数据时的页面显示操作
//			ShowUtils.showMsg(context, "暂时没有数据哦~");
//			ll_nodata.setVisibility(View.VISIBLE);
		}
		if (list.size() > 0) {
			if (adapter==null) {
				adapter = new TelecastNoteAdapter(context, list);
				mPullLoadMoreRecyclerView.setAdapter(adapter);
			}else {		
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		currentPage=1;
		initData();
		
	}

	/**
	 * 上拉加载
	 */
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
	
	/**
	 * 列表条目点击事件的处理
	 */
	/*private void onItemClick(){
		if (adapter!=null) {
			adapter.setOnItemClickLitener(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					
				}
			});
		}
	}*/

	private void getOldNoteData() {
		// TODO Auto-generated method stub
		//获取课程笔记列表的数据
		if (!StringUtil.isEmpty(courseId)) {
			Map<String, String> requestDataMap = ParamsMapUtil.getShowCourseNotice(context, courseId, null,userId);
			RequestVo requestVo = new RequestVo(ConstantsOnline.SHOW_NOTE_TELECAST_COURSE, context, requestDataMap, new MyBaseParser<>(NoteUserBean.class));
			DataCallBack<NoteUserBean> noticeDataCallBack = new DataCallBack<NoteUserBean>() {
				@Override
				public void processData(NoteUserBean object) {
					if (object==null) {
//						ShowUtils.showMsg(context, "获取笔记数据失败!");
					}else {
						mNoteUserBean = object;
						handler.obtainMessage(1).sendToTarget();
					}
				}
			};
			getDataServer(requestVo, noticeDataCallBack);
		}else {
			ShowUtils.showMsg(context, "课程数据有误,请反馈信息,谢谢!");
		}
	}
}
