package cc.upedu.online.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.MyWalletUserAdapter;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.MyWalletDetailBean;
import cc.upedu.online.domin.MyWalletDetailBean.recordItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView.PullLoadMoreListener;
import cc.upedu.online.view.wheelview.JudgeDate;
import cc.upedu.online.view.wheelview.MyAlertDialog;
import cc.upedu.online.view.wheelview.ScreenInfo;
import cc.upedu.online.view.wheelview.WheelMain;
/**
 * 普通用户的零钱界面，，默认展示全部，可以按照月份查询
 * @author Administrator
 *
 */
public class MyWalletDetailUserActivity extends TitleBaseActivity implements PullLoadMoreListener{
	//LinearLayout ll_wise;
	LinearLayout ll_nodata;
	TextView tv_time;
	
	Button btn_filter;// 弹出按条件查询的按钮
	Button btn_condition_scheme;// 切换方案查询还是条件查询
	
	TextView tv_starttime;
	TextView tv_endtime;
	
	String startTime;// 筛选条件，开始时间
	String endTime;// 筛选条件，结束时间
	
	LinearLayout ll_start_end;// 手动选择开始时间和结束时间

	boolean isCondition = true;// true:按照条件查询，最近一个月，一年。。。false:按照课程查询，学员消费查询

	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;//可以下拉刷新，上拉加载的RecyclerView
	private MyWalletUserAdapter adapter;// 普通代言人列表的adaptor
	private MyWalletDetailBean bean = new MyWalletDetailBean();

	boolean isPullDownToRefresh = false;// 记录是否是下拉刷新操作
	boolean isPullUpToRefresh = false;// 记录是否是下拉加载操作

	private int currentPage = 1;// 当前数据加载到哪个page
	private String totalPage;// 总页数

	private List<recordItem> list = new ArrayList<recordItem>();

	String userId;// 用户ID

	DataCallBack<MyWalletDetailBean> dataCallBack;
	RequestVo requestVo;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (mPullLoadMoreRecyclerView.isLoadMore()) {
					list.addAll(bean.entity.recordList);
					if (list.size() > 0) {
						if (adapter == null) {
							adapter = new MyWalletUserAdapter(context, list, null);
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
				ShowUtils.showMsg(context, bean.message);
				mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
			}
			
//			onItemClick();//添加条目点击事件的监听
		}
	};

	private void setData() {
		
		totalPage = bean.entity.totalPageSize;
		if (currentPage < Integer.valueOf(totalPage)) {
			mPullLoadMoreRecyclerView.setHasMore(true);
		}
		if (Integer.parseInt(totalPage) > 0) {
			ll_nodata.setVisibility(View.GONE);
			list.addAll(bean.entity.recordList);
		}else {
			//做无数据时的页面显示操作
//			ShowUtils.showMsg(context, "暂时没有数据哦~");
			ll_nodata.setVisibility(View.VISIBLE);
		}
		if (list.size() > 0) {
			if (adapter==null) {
				adapter = new MyWalletUserAdapter(context, list,null);
				mPullLoadMoreRecyclerView.setAdapter(adapter);
			}else {		
				adapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的零钱");
	}
	@Override
	protected View initContentView() {
		contentView = View.inflate(context, R.layout.activity_mywallet_detail_user, null);
		ll_nodata = (LinearLayout) contentView.findViewById(R.id.ll_nodata);
		tv_time = (TextView) contentView.findViewById(R.id.tv_time);

		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) contentView.findViewById(R.id.pullLoadMoreRecyclerView);
		mPullLoadMoreRecyclerView.setLinearLayout();
		mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
		mPullLoadMoreRecyclerView.setIsRefresh(true);
//		mPullLoadMoreRecyclerView.setItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));

		userId = UserStateUtil.getUserId();
		
		btn_filter = (Button) contentView.findViewById(R.id.btn_filter);
		btn_condition_scheme = (Button) contentView.findViewById(R.id.btn_condition_scheme);
		
		tv_starttime = (TextView) contentView.findViewById(R.id.tv_starttime);
		tv_endtime = (TextView) contentView.findViewById(R.id.tv_endtime);
		ll_start_end=(LinearLayout) contentView.findViewById(R.id.ll_start_end);
		
		startTime = getFilterTime(1);
		endTime = StringUtil.getStringDateShort();

		tv_starttime.setText(startTime);
		tv_endtime.setText(endTime);
		return contentView;
	}
	@Override
	protected void initData() {
		ym=getFilterTime(1) + ":" + data;
		dataCallBack = new DataCallBack<MyWalletDetailBean>() {
			@Override
			public void processData(MyWalletDetailBean object) {
				if (object == null) {
					if (mPullLoadMoreRecyclerView.isLoadMore()||mPullLoadMoreRecyclerView.isRefresh()) {
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
					} else {
						ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
					}
				} else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataFromServer(ym);
	}
	
	/**
	 * 获取数据
	 */
	private void getDataFromServer(String timeString) {
		Map<String, String> requestDataMap;
		requestDataMap = ParamsMapUtil.myWalletUserTime(context, userId, timeString,
				String.valueOf(currentPage));
		requestVo = new RequestVo(ConstantsOnline.MY_WALLET_USER, context,
				requestDataMap, new MyBaseParser<>(MyWalletDetailBean.class));
		
		getDataServer(requestVo, dataCallBack);
	}
	
	

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		currentPage=1;
		if (isCondition) {
			getDataFromServer(ym);
		}else {
			getDataFromServer(startTime + ":" + endTime);
		}
		
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {

    	if (!StringUtil.isEmpty(totalPage)) {
			if (currentPage < Integer.parseInt(totalPage)) {
				currentPage++;
				if (isCondition) {
					getDataFromServer(ym);
				}else {
					getDataFromServer(startTime + ":" + endTime);
				}
			}else {
				ShowUtils.showMsg(context, "没有更多数据");
				mPullLoadMoreRecyclerView.setHasMore(false);
				mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
			}
		}else {
			onRefresh();
		}
	}

	@Override
	protected void initListener() {
		super.initListener();
		tv_starttime.setOnClickListener(this);
		tv_endtime.setOnClickListener(this);
		btn_filter.setOnClickListener(this);
		btn_condition_scheme.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_starttime:
			getData(startTime, 0);
			break;
		case R.id.tv_endtime:
			getData(endTime, 1);
			break;
		case R.id.btn_filter:
			if (isCondition) {
				conditionPopWindow();
			} else {
				
			}

			break;
			
		case R.id.btn_condition_scheme:

			isCondition = !isCondition;
			if (isCondition) {
				ll_start_end.setVisibility(View.GONE);
				btn_filter.setVisibility(View.VISIBLE);
				btn_filter.setText("最近一个月");
				getDataFromServer(getFilterTime(1) + ":" + data);
				btn_condition_scheme.setBackground(getResources().getDrawable(
						R.drawable.mywellet_tiaojian));
			} else {
				ll_start_end.setVisibility(View.VISIBLE);
				btn_filter.setVisibility(View.GONE);
				btn_condition_scheme.setBackground(getResources().getDrawable(
						R.drawable.mywellet_fangan));
			}

			break;

		default:
			break;
		}

	}
	
	// /////////////////////////////////////////////////////////////////////////////////
	private WheelMain wheelMain;
	private String time;
	private boolean isShowWheel = false;
	private MyAlertDialog dialog;

	/**
	 * 时间选择控件
	 */
	public void getData(String mTime, final int type) {
		LayoutInflater inflater = LayoutInflater
				.from(this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(
				MyWalletDetailUserActivity.this);
		wheelMain = new WheelMain(timepickerview, "true");
		wheelMain.screenheight = screenInfo.getHeight();
		if (StringUtil.isEmpty(mTime)) {
			time = StringUtil.getStringDate();
		} else {
			time = mTime;
		}
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
			try {
				calendar.setTime(StringUtil.mDateFormat.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month, day);
		dialog = new MyAlertDialog(MyWalletDetailUserActivity.this)
				.builder()
				// .setTitle("选择时间")
				.setView(timepickerview)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						isShowWheel = !isShowWheel;
					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (0 == type) {
					startTime = wheelMain.getTime();
					tv_starttime.setText(startTime);

					if (StringUtil.getLongDate(startTime, "yyyy-MM-dd") < StringUtil.getLongDate(endTime, "yyyy-MM-dd")) {
						String time = startTime + ":" + endTime;
						getDataFromServer(time);
					} else {
						ShowUtils.showMsg(context, "结束时间不能早于起始时间哦");
					}
				
					dialog.dismiss();

				} else {
					endTime = wheelMain.getTime();
					tv_endtime.setText(endTime);

					if (StringUtil.getLongDate(startTime, "yyyy-MM-dd") < StringUtil.getLongDate(endTime, "yyyy-MM-dd")) {
						String time = startTime + ":" + endTime;
						getDataFromServer(time);
					} else {
						ShowUtils.showMsg(context, "结束时间不能早于起始时间哦");
					}
				
					dialog.dismiss();

				}
			}
		});
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mydatadialogstyle);
		isShowWheel = !isShowWheel;
		dialog.show();
	}

	
	// /////////////////////////////////////////////////////////////////////////////////
		/**
		 * 点击筛选条件时弹出的popwindow
		 * 
		 */
		PopupWindow popup;// 活动的PopupWindow对象

		View popView;
		Button one_month;// 最近一个月
		Button three_month;// 最近三个月
		Button six_month;// 最近半年
		Button one_year;// 最近一年
		Button cancel;// 取消按钮
		LinearLayout ll_blank;// 点击空白

		private void conditionPopWindow() {
			popView = View.inflate(context,
					R.layout.layout_popupwindow_mywallet_selectchart, null);

			one_month = (Button) popView.findViewById(R.id.one_month);
			three_month = (Button) popView.findViewById(R.id.three_month);
			six_month = (Button) popView.findViewById(R.id.six_month);
			one_year = (Button) popView.findViewById(R.id.one_year);
			cancel = (Button) popView.findViewById(R.id.cancel);
			ll_blank = (LinearLayout) popView.findViewById(R.id.ll_blank);
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
			popup.showAtLocation(contentView,
					Gravity.NO_GRAVITY, 0, 0);

			getListener();// pop内部的点击事件

		}

		String data = StringUtil.getStringDateShort();// 获取当前时间 2015-12-12
		String ym;
		private View contentView;

		private void getListener() {

			// 查询最近一个月
			one_month.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ym = getFilterTime(1) + ":" + data;
					btn_filter.setText("最近一个月");
					getDataFromServer(ym);
					popup.dismiss();
				}

			});

			// 查询最近三个月
			three_month.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ym = getFilterTime(3) + ":" + data;
					btn_filter.setText("最近三个月");
					getDataFromServer(ym);
					popup.dismiss();
				}
			});

			// 查询最近半年
			six_month.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ym = getFilterTime(6) + ":" + data;
					btn_filter.setText("最近半年");
					getDataFromServer(ym);
					popup.dismiss();
				}
			});

			// 查询最近一年
			one_year.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ym = getFilterTime(12) + ":" + data;
					btn_filter.setText("最近一年");
					getDataFromServer(ym);
					popup.dismiss();
				}
			});

			// 取消pupupwindow
			cancel.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					popup.dismiss();
				}
			});
			// 取消pupupwindow
			ll_blank.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					popup.dismiss();
				}
			});
		}

		/**
		 * 获取筛选时间，按一月，三个月，半年。。。
		 * 
		 * @param number
		 *            传入参数，按照1个月、3个月、6个月、12个月筛选
		 * @return
		 */
		private String getFilterTime(int number) {
			String[] strs = data.split("-");
			int year = Integer.valueOf(strs[0]);
			int month = Integer.valueOf(strs[1]);

			int m = (month + 12 - number) % 12;
			if (m == 0) {
				year = year - 1;
				month = 12;
			} else if (m > month) {
				year = year - 1;
				month = m;
			} else if (m < month) {
				month = m;
			} else if (m == month) {
				year = year - 1;
				month = m;
			} else {

			}
			
			String mon;
			if (month<10) {
				mon="0"+month;
			}else {
				mon=""+month;
			}

			return String.valueOf(year) + "-" + mon + "-"
					+ strs[2];

		}


}
