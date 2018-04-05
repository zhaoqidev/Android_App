package cc.upedu.online.activity;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.MyWalletUserAdapter;
import cc.upedu.online.base.TwoPartModelTopRecyclerViewBaseActivity;
import cc.upedu.online.domin.MyWalletDetailBean;
import cc.upedu.online.domin.MyWalletDetailBean.recordItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.wheelview.JudgeDate;
import cc.upedu.online.view.wheelview.MyAlertDialog;
import cc.upedu.online.view.wheelview.ScreenInfo;
import cc.upedu.online.view.wheelview.WheelMain;

/**
 * 成长币查询，默认展示全部，可以按照月份查询
 * 
 * @author Administrator
 * 
 */
public class MyWalletDetailCoinActivity extends TwoPartModelTopRecyclerViewBaseActivity<recordItem> {
	LinearLayout ll_coin_all;
	LinearLayout ll_coin_time;

	TextView coin_all;
	TextView coin_time;

	ImageView iv_down1, iv_down2;// 两个向下的箭头，down1全部，down2按时间

	WheelMain wheelMain;
	String time;
	private MyAlertDialog dialog;
	private MyWalletDetailBean bean = new MyWalletDetailBean();
	String userId;// 用户ID
	String queryCondition = "ALL";// 标志，all查询全部，time按照时间查询

	String COIN = "coin";// 当做参数传到Adapter中，如果为coin即是成长币，为空则为零钱

	private String mTime;
	private String nTime;

	DataCallBack<MyWalletDetailBean> dataCallBack;
	RequestVo requestVo;
	
	TextView tv_time;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list!=null) {
						list.clear();
					}else {
						list = new ArrayList<recordItem>();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.message);
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};

	private void setData() {
		totalPage = bean.entity.totalPageSize;
		//判断是否可以加载下一页
		canLodeNextPage();
		list.addAll(bean.entity.recordList);
		if (isAdapterEmpty()) {
			setRecyclerView(new MyWalletUserAdapter(context, list, COIN));
		}else {
			notifyData();
		}
	}

	@Override
	public View initTopLayout() {
		View view =View.inflate(context, R.layout.activity_mywallet_detail_coin, null);
		tv_time = (TextView) view.findViewById(R.id.tv_time);

		ll_coin_all = (LinearLayout) view.findViewById(R.id.ll_coin_all);// 点击切换筛选条件，查询全部
		ll_coin_time = (LinearLayout) view.findViewById(R.id.ll_coin_time);// 按时间查询

		coin_all = (TextView) view.findViewById(R.id.coin_all);
		coin_time = (TextView) view.findViewById(R.id.coin_time);

		iv_down1 = (ImageView) view.findViewById(R.id.iv_down1);
		iv_down2 = (ImageView) view.findViewById(R.id.iv_down2);
		
		userId = UserStateUtil.getUserId();// 获取到用户ID
		return view;
	}

	@Override
	protected void initData() {
		Map<String, String> requestDataMap;
		//style  0全部,1月份
		if (style == 0) {//全部
			requestDataMap = ParamsMapUtil.myWalletCoinAll(context, userId,
					String.valueOf(currentPage));
			requestVo = new RequestVo(ConstantsOnline.MY_WALLET_COIN, context,
					requestDataMap, new MyBaseParser<>(MyWalletDetailBean.class));
		}else if (style == 1) {//月份
			requestDataMap = ParamsMapUtil.myWalletCoinTime(context, userId, nTime,
					String.valueOf(currentPage));
			requestVo = new RequestVo(ConstantsOnline.MY_WALLET_COIN, context,
					requestDataMap, new MyBaseParser<>(MyWalletDetailBean.class));
		}
		
		dataCallBack = new DataCallBack<MyWalletDetailBean>() {
			@Override
			public void processData(MyWalletDetailBean object) {
				if (object == null) {

					objectIsNull();
				
				} else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, dataCallBack);
	}


	/**
	 * 获取月份查询
	 */
	protected void getRequestVo() {
		

	}
	@Override
	protected void initListener() {
	super.initListener();;
		ll_coin_all.setOnClickListener(this);
		ll_coin_time.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		
		switch (v.getId()) {
		/*
		 * case R.id.ll_wise: showPopWin(); break;
		 */
		/*
		 * case R.id.rl_date: pop.dismiss(); dateCheck(); break;
		 */
		/*
		 * case R.id.rl_all: pop.dismiss(); initRequestVo(); initDataCallBack();
		 * initData(); tv_time.setText("全部"); break;
		 */

		case R.id.ll_coin_time:// 按时间查询
			queryCondition = "TIME";
			coin_time.setTextColor(getResources().getColor(R.color.red_fe3d35));
			coin_all.setTextColor(getResources().getColor(R.color.white));

			ll_coin_time
					.setBackgroundResource(R.drawable.button_bg_write_right);
			ll_coin_all.setBackgroundResource(R.drawable.button_bg_red_left);

			iv_down1.setBackgroundResource(R.drawable.othmenu_2x);
			iv_down2.setBackgroundResource(R.drawable.srmenu_2x);

			dateCheck();
			break;
		case R.id.ll_coin_all:// 查询全部
			queryCondition = "ALL";
			coin_time.setTextColor(getResources().getColor(R.color.white));
			coin_all.setTextColor(getResources().getColor(R.color.red_fe3d35));

			ll_coin_time.setBackgroundResource(R.drawable.button_bg_red_right);
			ll_coin_all.setBackgroundResource(R.drawable.button_bg_write_left);

			iv_down1.setBackgroundResource(R.drawable.srmenu_2x);
			iv_down2.setBackgroundResource(R.drawable.othmenu_2x);
			
			currentPage=1;
			style = 0;
			initData();
			tv_time.setText("全部");
			break;

		default:
			break;
		}

	}


	/**
	 * 弹出时间选择控件，并获取到用户选择的时间
	 */
	private void dateCheck() {
		LayoutInflater inflater = LayoutInflater
				.from(MyWalletDetailCoinActivity.this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(MyWalletDetailCoinActivity.this);
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		String[] strs = StringUtil.getStringDateShort().split("-");
		time = strs[0] + "-" + strs[1];
		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM")) {
			try {
				calendar.setTime(StringUtil.mDateFormat.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year, month);
		dialog = new MyAlertDialog(MyWalletDetailCoinActivity.this).builder()
				// .setTitle("选择时间")
				.setView(timepickerview)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// isShowWheel = !isShowWheel;
					}
				});
		dialog.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				mTime = wheelMain.getTime();
				String[] strs = mTime.split("-");
				mTime = strs[0] + "年" + strs[1] + "月"; // 用于界面显示

				nTime = strs[0] + "-" + strs[1];// 用于请求服务器

				tv_time.setText(mTime);

				style = 1;
				initData();
				dialog.dismiss();
			}
		});
		Window window = dialog.getWindow();
		window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
		window.setWindowAnimations(R.style.mydatadialogstyle);
		dialog.show();
	}
	private int style = 0;
	

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		currentPage=1;
		if ("ALL".equals(queryCondition)) {
			style = 0;
			initData();
		} else {
			style = 1;
			initData();
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
				if ("ALL".equals(queryCondition)) {
					style = 0;
					initData();
				} else {
					style = 1;
					initData();
				}
			}else {
				ShowUtils.showMsg(context, "没有更多数据");
				setHasMore(false);
				setPullLoadMoreCompleted();
			}
		}else {
			onRefresh();
		}
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	protected void initTitle() {
	setTitleText("我的成长币");
		
	}


}
