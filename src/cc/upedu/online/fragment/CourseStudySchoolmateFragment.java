package cc.upedu.online.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.AskForNoteActivity;
import cc.upedu.online.activity.SetIndustryActivity;
import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.SchoolmateCourseAdapter;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.domin.SchoolmateOfCourseBean;
import cc.upedu.online.domin.SchoolmateOfCourseBean.SchoolmateItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ScreenUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;
import cc.upedu.online.view.pullrefreshRecyclerview.DividerItemDecoration;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView;
import io.rong.imkit.RongIM;

/**
 * 直播在线好友界面
 * 
 * @author Administrator
 * 
 */
public class CourseStudySchoolmateFragment extends BaseFragment implements OnClickListener{
	public static final int REQUEST_CHOOSEINDUSTRY = 41;
	private View contentView;
	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;

	private List<SchoolmateItem> list = new ArrayList<SchoolmateItem>();
	private SchoolmateCourseAdapter adapter;
	private LinearLayout ll_nodata,ll_search; 
	private TextView tv_num;

	private int currentPage = 1;// 当前数据加载到哪个page
	private String totalPage;// 总页数
	private SchoolmateOfCourseBean mSchoolmateBean;
	private String userId;
	private String courseId;

	public CourseStudySchoolmateFragment(){
		super();
	}
	public CourseStudySchoolmateFragment(Context context, String courseId) {
		this.context = context;
		this.courseId = courseId;
		contentView = View.inflate(context,
				R.layout.layout_courseschoolmate_relcerview, null);
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) contentView
				.findViewById(R.id.pullLoadMoreRecyclerView);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mSchoolmateBean.success)) {
				if (mPullLoadMoreRecyclerView.isLoadMore()) {
					list.addAll(mSchoolmateBean.entity.classmateList);
					if (list.size() > 0) {
						if (adapter == null) {
							adapter = new SchoolmateCourseAdapter(context,
									list);
							mPullLoadMoreRecyclerView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}

					}
					// 判断是否可以加载下一页
					if (currentPage < Integer.valueOf(totalPage)) {
						mPullLoadMoreRecyclerView.setHasMore(true);
					} else {
						mPullLoadMoreRecyclerView.setHasMore(false);
					}
					
					onItemClick();

					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载更多
				} else {
					list.clear();
					setData();
					// mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();//
					// 结束下拉刷新
				}
			} else {
				ShowUtils.showMsg(context, mSchoolmateBean.message);
				mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
			}

		}
	};

	private void setData() {
		totalPage = mSchoolmateBean.entity.totalPage;
		if (currentPage < Integer.valueOf(totalPage)) {
			mPullLoadMoreRecyclerView.setHasMore(true);
		}
		if (Integer.parseInt(totalPage) > 0) {
			ll_nodata.setVisibility(View.GONE);
			list.addAll(mSchoolmateBean.entity.classmateList);
		} else {
			// 做无数据时的页面显示操作
			// ShowUtils.showMsg(context, "暂时没有数据哦~");
			ll_nodata.setVisibility(View.VISIBLE);
			list.clear();
		}
		if (list.size() > 0) {
			tv_num.setText(String.valueOf(list.size())+"人");
			adapter = new SchoolmateCourseAdapter(context, list);
			mPullLoadMoreRecyclerView.setAdapter(adapter);
			if (mPullLoadMoreRecyclerView.isRefresh()) {

			} else {
				adapter.notifyDataSetChanged();
			}
			onItemClick();
		}else {
			if (adapter != null){
				adapter.notifyDataSetChanged();
			}
		}
		mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束下拉刷新

	}

	@Override
	protected View initView(LayoutInflater inflater) {
		userId = UserStateUtil.getUserId();
		ll_nodata = (LinearLayout) contentView.findViewById(R.id.ll_nodata);//
		ll_search = (LinearLayout) contentView.findViewById(R.id.ll_search);//
		tv_num = (TextView) contentView.findViewById(R.id.tv_num);// 报名人数
		ll_search.setOnClickListener(this);

		mPullLoadMoreRecyclerView.setLinearLayout();
		mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(new PullLoadMoreListener());
		mPullLoadMoreRecyclerView.setItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));

		return contentView;
	}

	@Override
	public void initData() {
		if (!StringUtil.isEmpty(industry) && Integer.valueOf(type) == 2 && !StringUtil.isEmpty(industry)) {
			otherCode = industry;
		}else if (!StringUtil.isEmpty(industry) && Integer.valueOf(type) == 3 && !StringUtil.isEmpty(othercity)) {
			otherCode = othercity;
		}
		Map<String, String> requestDataMap = ParamsMapUtil.schoolMateCourse(
				context, userId, courseId, String.valueOf(currentPage), "500",type,otherCode,content);
		RequestVo requestVo = new RequestVo(
				ConstantsOnline.SCHOOLMATE_LIST_TELECAST_COURSE, context,
				requestDataMap,
				new MyBaseParser<>(SchoolmateOfCourseBean.class));
		DataCallBack<SchoolmateOfCourseBean> dataCallBack = new DataCallBack<SchoolmateOfCourseBean>() {
			@Override
			public void processData(SchoolmateOfCourseBean object) {
				if (object == null) {
					if (mPullLoadMoreRecyclerView.isLoadMore()
							|| mPullLoadMoreRecyclerView.isRefresh()) {
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
					} else {
						ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
					}
				} else {
					mSchoolmateBean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, dataCallBack);

	}

	class PullLoadMoreListener implements
			PullLoadMoreRecyclerView.PullLoadMoreListener {
		@Override
		public void onRefresh() {
			currentPage = 1;
			initData();
		}

		@Override
		public void onLoadMore() {
			if (!StringUtil.isEmpty(totalPage)) {
				if (currentPage < Integer.parseInt(totalPage)) {
					currentPage++;
					initData();
				} else {
					ShowUtils.showMsg(context, "没有更多数据");
					mPullLoadMoreRecyclerView.setHasMore(false);
					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
				}
			} else {
				onRefresh();
			}
		}
	}

	/**
	 * 列表点击事件的处理
	 */
	private void onItemClick() {
		if (adapter != null) {
			adapter.setOnItemClickLitener(new OnItemClickLitener() {

				@Override
				public void onItemLongClick(View view, int position) {

				}

				@Override
				public void onItemClick(View view, int position) {
					sportPopWindow(position);
				}
			});
		}
	}
	
	/**
	 * 点击活动时弹出的popwindow
	 * 
	 */
	PopupWindow popup;// 活动的PopupWindow对象
	
	View popView;
	Button iv_im_letter;//发消息
	Button look_friend;//查看好友名片
	Button iv_askfor_note;//索要笔记
	Button iv_share_note;//分享笔记
	Button cancel;//取消按钮
	LinearLayout ll_blank;//点击空白
	

	private void sportPopWindow(int position) {
		popView = View.inflate(context,R.layout.layout_popupwindow_coursestudy_schoolmate, null);
		
		iv_im_letter = (Button) popView.findViewById(R.id.iv_im_letter);
		look_friend = (Button) popView.findViewById(R.id.look_friend);
		iv_askfor_note = (Button) popView.findViewById(R.id.iv_askfor_note);
		iv_share_note = (Button) popView.findViewById(R.id.iv_share_note);
		cancel = (Button) popView.findViewById(R.id.cancel);
		ll_blank=(LinearLayout) popView.findViewById(R.id.ll_blank);
		 // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		popup = new PopupWindow(popView,
	        WindowManager.LayoutParams.MATCH_PARENT,
	        WindowManager.LayoutParams.MATCH_PARENT);

	    // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		popup.setFocusable(true);


	    // 实例化一个ColorDrawable颜色为半透明
	    ColorDrawable dw = new ColorDrawable(0x1e000000);
	    popup.setBackgroundDrawable(dw);

	    
	    // 设置popWindow的显示和消失动画
	    popup.setAnimationStyle(R.style.mypopwindow_anim_style);
	    // 在底部显示
	    popup.showAtLocation(((Activity) context).findViewById(R.id.tv_num),Gravity.BOTTOM, 0, 0);
	    
	    getListener(position);//pop内部的点击事件

		/*// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popup.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.loginback));


		// 设置好参数之后再show
		//popup.showAsDropDown(holder.iv_attention);
*/	}
	
	

	private void getListener(final int position) {

		//发送即时消息的点击监听
		iv_im_letter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				/*
				 * 打开即时消息页面
				 */
				RongIM.getInstance().startPrivateChat(context, list.get(position).uid, list.get(position).name); 
			}
		});

		//进入到好友名片页面
		look_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				userCard(position);
			}
		});
		//取消pupupwindow
		cancel.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				popup.dismiss();
				}
			});
		//取消pupupwindow
		ll_blank.setOnClickListener(new OnClickListener() {
						
		@Override
		public void onClick(View v) {
			popup.dismiss();
			}
		});
		
		//索要笔记
		iv_askfor_note.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				Intent intent=new Intent(context,AskForNoteActivity.class);
				intent.putExtra("type", "ask");//页面为赠送笔记还是索要笔记
				intent.putExtra("tuid", list.get(position).uid);
				intent.putExtra("tname", list.get(position).name);
				
				intent.putExtra("course_name", SharedPreferencesUtil.getInstance().spGetString("study_courseName"));
				intent.putExtra("course_image", SharedPreferencesUtil.getInstance().spGetString("study_courseLogo"));
				intent.putExtra("course_id", SharedPreferencesUtil.getInstance().spGetString("study_courseId"));
				
				startActivity(intent);
				
			}
		});
		//分享笔记
		iv_share_note.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				Intent intent=new Intent(context,AskForNoteActivity.class);
				intent.putExtra("type", "share");//页面为赠送笔记还是索要笔记
				intent.putExtra("tuid", list.get(position).uid);
				intent.putExtra("tname", list.get(position).name);
				
				intent.putExtra("course_name", SharedPreferencesUtil.getInstance().spGetString("study_courseName"));
				intent.putExtra("course_image", SharedPreferencesUtil.getInstance().spGetString("study_courseLogo"));
				intent.putExtra("course_id", SharedPreferencesUtil.getInstance().spGetString("study_courseId"));
				startActivity(intent);
				
			}
		});
		
	}
	
	/**
	 * 进入到好友名片页面
	 */
	public void userCard(int position) {
		Intent intent = new Intent(context, UserShowActivity.class);
		intent.putExtra("userId", list.get(position).uid);
//		intent.putExtra("attention", list.get(position).isFriend);
		context.startActivity(intent);
	}

	/**
	 * 设置是否可刷新
	 * 
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mPullLoadMoreRecyclerView.setPullRefreshEnable(enable);
	}
	
	private PopupWindow searchpop;
	private TextView tv_left,tv_right,tv_all,tv_city,tv_industry,tv_man,tv_woman,tv_othercity,tv_clear,name_industry,name_othercity;
	private List<TextView> viewList = new ArrayList<TextView>();
	private EditText et_search;
	private void showSearchPopWin() {
		if (searchpop == null) {
			View view = View.inflate(context, R.layout.layout_searchschoolmate_popwindow, null);
			searchpop = new PopupWindow(view,
			        WindowManager.LayoutParams.WRAP_CONTENT,
			        WindowManager.LayoutParams.WRAP_CONTENT);
			searchpop.setFocusable(true); // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
			searchpop.setTouchable(true);
			// 实例化一个ColorDrawable颜色为半透明
		    ColorDrawable dw = new ColorDrawable(0x50000000);
		    searchpop.setBackgroundDrawable(dw);
		 // 设置popWindow的显示和消失动画
		    searchpop.setAnimationStyle(R.style.mypopwindow_anim_style);
		    // 在底部显示
		    searchpop.setTouchInterceptor(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
					// 这里如果返回true的话，touch事件将被拦截
					// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
				}
			});
			//et_search
			// 点击事件监听
		    et_search = (EditText) view.findViewById(R.id.et_search);
		    tv_left = (TextView) view.findViewById(R.id.tv_left);
		    tv_left.setOnClickListener(this);
		    tv_right = (TextView) view.findViewById(R.id.tv_right);
			tv_right.setOnClickListener(this);
			tv_all = (TextView) view.findViewById(R.id.tv_all);
			tv_all.setOnClickListener(this);
			tv_city = (TextView) view.findViewById(R.id.tv_city);
			tv_city.setOnClickListener(this);
			tv_industry = (TextView) view.findViewById(R.id.tv_industry);
			view.findViewById(R.id.ll_industry).setOnClickListener(this);
			name_industry = (TextView) view.findViewById(R.id.name_industry);
			tv_man = (TextView) view.findViewById(R.id.tv_man);
			tv_man.setOnClickListener(this);
			tv_woman = (TextView) view.findViewById(R.id.tv_woman);
			tv_woman.setOnClickListener(this);
			tv_othercity = (TextView) view.findViewById(R.id.tv_othercity);
			view.findViewById(R.id.ll_othercity).setOnClickListener(this);
			name_othercity = (TextView) view.findViewById(R.id.name_othercity);
			tv_clear = (TextView) view.findViewById(R.id.tv_clear);
			tv_clear.setOnClickListener(this);
			viewList.add(tv_all);
			viewList.add(tv_city);
			viewList.add(tv_industry);
			viewList.add(tv_man);
			viewList.add(tv_woman);
			viewList.add(tv_othercity);
		}
		int[] location = new int[2];  
		contentView.getLocationOnScreen(location);
		x = ScreenUtil.getInstance(context).getScreenWidth()-searchpop.getWidth();
		y = location[1];
		// 设置好参数之后再show
		searchpop.showAtLocation(contentView,Gravity.NO_GRAVITY, x, y);
		
		 // 设置背景颜色变暗
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = 0.7f;
        getActivity().getWindow().setAttributes(lp);
        searchpop.setOnDismissListener(new OnDismissListener() {
 
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
	}
	int x = 0;
	int y = 0;
	private void dismissSearchpop() {
		// TODO Auto-generated method stub
		if (searchpop != null && searchpop.isShowing()) {
//			type = "0";
//			otherCode = "";
//			currentCondition = 0;
//			selectionCondition(0);
			searchpop.dismiss();
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ll_search:
			if (searchpop == null || !searchpop.isShowing()) {
				showSearchPopWin();
			}
			break;
		case R.id.tv_left://取消
			dismissSearchpop();
			break;
		case R.id.tv_right://确定
			content = et_search.getText().toString().trim();
			currentPage = 1;
			initData();
			dismissSearchpop();
			break;
		case R.id.tv_all://全部学友,进入时默认,条件0
			type = "0";
			otherCode = "";
			name_industry.setText("");
			name_othercity.setText("");
			selectionCondition(0);
			break;
		case R.id.tv_city://同城学友,条件1
			type = "1";
			otherCode = "";
			name_industry.setText("");
			name_othercity.setText("");
			selectionCondition(1);
			break;
		case R.id.ll_industry://同行学友,条件2
			type = "2";
			otherCode = "";
			name_othercity.setText("");
			selectionCondition(2);
			intent = new Intent(context, SetIndustryActivity.class);
			intent.putExtra("industry", industry);
			startActivityForResult(intent, REQUEST_CHOOSEINDUSTRY);
			break;
		case R.id.tv_man://男学友,条件3
			type = "4";
			otherCode = "1";
			name_industry.setText("");
			name_othercity.setText("");
			selectionCondition(3);
			break;
		case R.id.tv_woman://女学友,条件4
			type = "4";
			otherCode = "2";
			name_industry.setText("");
			name_othercity.setText("");
			selectionCondition(4);
			break;
		case R.id.ll_othercity://其他城市,条件5
			type = "3";
			otherCode = "";
			name_industry.setText("");
			selectionCondition(5);
			intent = new Intent(context, CityChooseActity.class);
			intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_ONE);
			startActivityForResult(intent, CityChooseActity.CHOOSE_ONE);
			break;
		case R.id.tv_clear://清除筛选条件
			type = "0";
			otherCode = "";
			et_search.setText("");
			name_industry.setText("");
			name_othercity.setText("");
			selectionCondition(0);
			break;
		}
	}
	private String type = "0";
	private String otherCode = "";
	private int currentCondition = 0;
	private String content = "";
	//选择条件处理实际的方法
	private void selectionCondition(int num) {
		// TODO Auto-generated method stub
		if (currentCondition != num) {
			currentCondition = num;
			int size = viewList.size();
			for (int i = 0; i < size; i++) {
				if (i == currentCondition) {
					viewList.get(i).setTextColor(getResources().getColor(R.color.red_fb5252));
				}else {
					viewList.get(i).setTextColor(getResources().getColor(R.color.course_title));
				}
			}
		}
	}
	private String industry = "";
	public void setIndustry(String industryname,String industryid) {
		// TODO Auto-generated method stub
		industry = industryid;
		name_industry.setText(industryname);
	}
	private String othercity = "";
	public void setOthercity(String cityname,String cityid) {
		// TODO Auto-generated method stub
		othercity = cityid;
		name_othercity.setText(cityname);
	}
}
