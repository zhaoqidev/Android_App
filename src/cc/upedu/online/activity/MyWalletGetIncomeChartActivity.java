package cc.upedu.online.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.MyWalletGetIncomeChartBean;
import cc.upedu.online.domin.MyWalletGetIncomeChartBean.BarList;
import cc.upedu.online.domin.MyWalletGetIncomeChartBean.PieList;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.factory.MyLegendItem;
import cc.upedu.online.view.wheelview.JudgeDate;
import cc.upedu.online.view.wheelview.MyAlertDialog;
import cc.upedu.online.view.wheelview.ScreenInfo;
import cc.upedu.online.view.wheelview.WheelMain;

/**
 * 我的钱包获取收入图表界面
 * 
 * @author Administrator
 * 
 */
public class MyWalletGetIncomeChartActivity extends TitleBaseActivity implements
		OnChartValueSelectedListener {
	LinearLayout ll_nodata;
	LinearLayout ll_default;// 放置图例的布局
	RelativeLayout rl_piechart;// 饼状图的布局，包括明细
	LinearLayout ll_start_end;// 手动选择开始时间和结束时间
	TextView tv_starttime;
	TextView tv_endtime;
	String startTime;// 筛选条件，开始时间
	String endTime;// 筛选条件，结束时间
	Button btn_filter;// 弹出按条件查询的按钮
	Button btn_search;// 按区间查询的确定按钮
	Button btn_condition_scheme;// 切换方案查询还是条件查询
	ImageButton ib_detail;// 详细的收入列表
	boolean isCondition = true;// true:按照条件查询，最近一个月，一年。。。false:按照课程查询，学员消费查询
	String type = "0";// 0:默认（饼状图） 1：课程（柱状图） 2：学员（柱状图）

	TextView bar_legend;// 柱状图 图例

	private RequestVo requestVo;
	private DataCallBack<MyWalletGetIncomeChartBean> dataCallBack;

	String userId;
	MyWalletGetIncomeChartBean bean = new MyWalletGetIncomeChartBean();
	// 饼状图
	private PieChart mPieChart;

	// private Typeface tf;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的收入");
		userId=UserStateUtil.getUserId();
	}
	@Override
	protected View initContentView() {
		contentView = View.inflate(context, R.layout.activity_mywallet_income_chart, null);
		ll_default = (LinearLayout) contentView.findViewById(R.id.ll_default);
		ll_nodata = (LinearLayout) contentView.findViewById(R.id.ll_nodata);

		ll_start_end = (LinearLayout) contentView.findViewById(R.id.ll_start_end);
		rl_piechart = (RelativeLayout) contentView.findViewById(R.id.rl_piechart);
		tv_starttime = (TextView) contentView.findViewById(R.id.tv_starttime);
		tv_endtime = (TextView) contentView.findViewById(R.id.tv_endtime);
		btn_filter = (Button) contentView.findViewById(R.id.btn_filter);
		btn_search = (Button) contentView.findViewById(R.id.btn_search);
		btn_condition_scheme = (Button) contentView.findViewById(R.id.btn_condition_scheme);
		ib_detail = (ImageButton) contentView.findViewById(R.id.ib_detail);// 查看详细

		bar_legend = (TextView) contentView.findViewById(R.id.bar_legend);

		startTime = getFilterTime(1);
		endTime = StringUtil.getStringDateShort();

		tv_starttime.setText(startTime);
		tv_endtime.setText(endTime);
		return contentView;
	}

	/**
	 * 从服务器获取饼状图数据
	 * 
	 * @param dateSection
	 *            时间
	 * @param querytype
	 *            方案查询类型 0，
	 *            区间查询（ym，ymtype有值时，此字段为0）1，最近一个月收入明细（饼状图）2，最近三个月收入明细（饼状图
	 *            ）3，最近半年收入明细（饼状图）4，最近一年收入明细（饼状图）
	 * @param ymtype
	 *            区间查询类型:
	 *            0,默认（饼状图）1,按照课程排序（柱状图，导师，代理商才能查询）2,按照学员消费金额排序（柱状图，导师，代理商才能查询）
	 */
	public void getDataRequest(String dateSection, String querytype,
			final String ymtype) {
		requestVo = new RequestVo(ConstantsOnline.GET_INCOME_CHART, context,
				ParamsMapUtil.getIncomeChart(context, userId, dateSection,
						querytype, ymtype), new MyBaseParser<>(
						MyWalletGetIncomeChartBean.class));

		dataCallBack = new DataCallBack<MyWalletGetIncomeChartBean>() {

			@Override
			public void processData(MyWalletGetIncomeChartBean object) {
				if (object != null) {
					if ("false".equals(object.success)) {
						ShowUtils.showMsg(context, object.message);
					} else {
						bean = object;
						if (isCondition) {
							if (mBarChart != null) {
								mBarChart.setVisibility(View.GONE);
								bar_legend.setVisibility(View.GONE);
							}
							//无数据判断
							if (!StringUtil.isEmpty(object.entity.total)&&(Float.valueOf(object.entity.total)>0)) {
								getpie();
								setPieData(6, 0, object.entity.pielist);
								ll_nodata.setVisibility(View.GONE);
							}else {
								if (mPieChart!=null) {
									mPieChart.setVisibility(View.GONE);
								}
								ll_nodata.setVisibility(View.VISIBLE);
							}
						} else {
							if ("0".equals(ymtype)) {
								if (mBarChart != null) {
									mBarChart.setVisibility(View.GONE);
									bar_legend.setVisibility(View.GONE);
								}
								//无数据判断
								if (!StringUtil.isEmpty(object.entity.total)&&(Float.valueOf(object.entity.total)>0)) {
									getpie();
									setPieData(6, 0, object.entity.pielist);
									ll_nodata.setVisibility(View.GONE);
								}else {
									if (mPieChart!=null) {
										mPieChart.setVisibility(View.GONE);
									}
									ll_nodata.setVisibility(View.VISIBLE);
								}
							} else {
								if (mPieChart != null&&mPieChart!=null) {
									mPieChart.setVisibility(View.GONE);
								}
								//无数据判断
								if (object.entity.barlist.size()>0) {
									getHorizontalBarChart();
									setHorizontalBarChartData(object.entity.barlist);
									ll_nodata.setVisibility(View.GONE);
								}else {
									if (mBarChart!=null) {
										mBarChart.setVisibility(View.GONE);
										bar_legend.setVisibility(View.GONE);
									}
									ll_nodata.setVisibility(View.VISIBLE);
								}
							}
						}
					}
				} else {
					ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
				}
			}
		};

	}

	@Override
	protected void initListener() {
		super.initListener();
		tv_starttime.setOnClickListener(this);
		tv_endtime.setOnClickListener(this);
		btn_filter.setOnClickListener(this);
		btn_search.setOnClickListener(this);
		btn_condition_scheme.setOnClickListener(this);
		ib_detail.setOnClickListener(this);
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
				schemePopWindow();
			}

			break;
		case R.id.btn_search:
			if (!StringUtil.isEmpty(startTime) && !StringUtil.isEmpty(endTime)) {
				if (StringUtil.getLongDate(startTime, "yyyy-MM-dd") < StringUtil
						.getLongDate(endTime, "yyyy-MM-dd")) {
					// ShowUtils.showMsg(context, startTime + ":" + endTime);
					String time = startTime + ":" + endTime;
					ll_default.removeAllViews();
					getDataRequest(time, "0", type);
					getDataServer(requestVo, dataCallBack);
				} else {
					ShowUtils.showMsg(context, "结束时间不能早于起始时间哦");
				}
			} else {
				ShowUtils.showMsg(context, "时间不能为空");
			}

			break;
		case R.id.btn_condition_scheme:

			isCondition = !isCondition;
			if (isCondition) {
				ll_start_end.setVisibility(View.GONE);
				btn_filter.setText("最近一个月");
				upData(getFilterTime(1) + ":" + data, "1");
				btn_condition_scheme.setBackground(getResources().getDrawable(
						R.drawable.mywellet_tiaojian));
			} else {
				ll_start_end.setVisibility(View.VISIBLE);
				btn_filter.setText("默认");
				type = "0";
				btn_condition_scheme.setBackground(getResources().getDrawable(
						R.drawable.mywellet_fangan));
			}

			break;

		case R.id.ib_detail:
			Intent intent = new Intent(this,MyWalletDetailDaoshiAndAgentActivity.class);
			if (isCondition) {
				intent.putExtra("searchTime", ym);
			}else {
				intent.putExtra("searchTime", startTime + ":" + endTime);
			}
			
			startActivity(intent);

			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {
		ym=getFilterTime(1) + ":" + data;
		upData(ym, "1");
	}

	/**
	 * 添加图例布局
	 * 
	 * @param color
	 * @param kinds
	 * @param money
	 */
	private void addView(int color, String kinds, String money) {
		MyLegendItem legendItem = new MyLegendItem();
		legendItem.initView(context, color, kinds, money);
		ll_default.addView(legendItem.getRootView());
	}

	// /////////////////////////////////////////////////////////////////////////////////
	/**
	 * 初始化饼状图
	 */
	public void getpie() {
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mPieChart = (PieChart) findViewById(R.id.pie_chart);
		mPieChart.setUsePercentValues(true);// 显示成百分比
		mPieChart.setDescription("");
		mPieChart.setExtraOffsets(5, 10, 5, 5);

		mPieChart.setDragDecelerationFrictionCoef(0.95f);

		// tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

		// mPieChart.setCenterTextTypeface(Typeface.createFromAsset(getAssets(),
		// "OpenSans-Light.ttf"));
		// mPieChart.setCenterText(generateCenterSpannableText("4866"));

		mPieChart.setDrawHoleEnabled(true);//
		mPieChart.setHoleColorTransparent(true);//

		mPieChart.setTransparentCircleColor(Color.WHITE);
		mPieChart.setTransparentCircleAlpha(110);

		mPieChart.setHoleRadius(58f);// 内侧半径
		mPieChart.setTransparentCircleRadius(61f);// 中间圆半径

		mPieChart.setDrawCenterText(true);// 是否显示饼状图中间的文字

		mPieChart.setRotationAngle(90);// 饼状图载入时的旋转角度

		mPieChart.setRotationEnabled(true);// 手指滑动饼状图，是否能滚动
		mPieChart.setHighlightPerTapEnabled(true);

		// mPieChart.setUnit("");
		// mPieChart.setDrawUnitsInChart(true);

		// add a selection listener
		mPieChart.setOnChartValueSelectedListener(this);// 添加选择监听

		mPieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
		// mPieChart.spin(2000, 0, 360);

		mPieChart.setDrawSliceText(false);

		if (StringUtil.isEmpty(bean.entity.total)) {
			mPieChart.setCenterText(generateCenterSpannableText("0"));
		}else{
			mPieChart.setCenterText(generateCenterSpannableText(bean.entity.total));
		}

		mPieChart.setVisibility(View.VISIBLE);// 设置显示


		Legend l = mPieChart.getLegend();
		l.setPosition(LegendPosition.ABOVE_CHART_LEFT);
		l.setEnabled(false);
		// l.setForm(LegendForm.CIRCLE);
		// l.setXEntrySpace(7f);
		// l.setYEntrySpace(0f);
		// l.setYOffset(0f);

	}

	ArrayList<Entry> yValues;
	ArrayList<String> xVals;
	ArrayList<Integer> colors;

	/**
	 * 为饼状图设置数据
	 * 
	 * @param count
	 * @param range
	 * @param pieList
	 */
	private void setPieData(int count, float range, PieList pieList) {

		yValues = new ArrayList<Entry>();
		xVals = new ArrayList<String>();
		colors = new ArrayList<Integer>();

		// IMPORTANT: In a PieChart, no values (Entry) should have the same
		// xIndex (even if from different DataSets), since no values can be
		// drawn above each other.
		// 饼图数据

		/**
		 * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38 所以 14代表的百分比就是14%
		 */
		float YValue1 = Float.valueOf(pieList.refund);// 退款金额
		float YValue2 = Float.valueOf(pieList.cashload); // 现金充值
		float YValue3 = Float.valueOf(pieList.sales); // 消费金额
		float YValue4 = Float.valueOf(pieList.agentincome); // 代理商收入
		float YValue5 = Float.valueOf(pieList.teacherincome); // 导师收入
		float YValue6 = Float.valueOf(pieList.other); // 其他收入

		getPieData(YValue1, 0, COLORS[0], "退款金额", pieList.refund);
		getPieData(YValue2, 1, COLORS[1], "现金充值", pieList.cashload);
		getPieData(YValue3, 2, COLORS[2], "消费金额", pieList.sales);
		getPieData(YValue4, 3, COLORS[3], "代理商收入", pieList.agentincome);
		getPieData(YValue5, 4, COLORS[4], "课程收入", pieList.teacherincome);
		getPieData(YValue6, 5, COLORS[5], "其他收入", pieList.other);

		// y轴的集合
		PieDataSet pieDataSet = new PieDataSet(yValues,
				"Quarterly Revenue 2014"/* 显示在比例图上 */);
		pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离

		PieDataSet dataSet = new PieDataSet(yValues, "收入图表");
		dataSet.setSliceSpace(2f);
		dataSet.setSelectionShift(5f);

		dataSet.setColors(colors);
		// dataSet.setSelectionShift(0f);

		PieData data = new PieData(xVals, dataSet);
		data.setValueFormatter(new PercentFormatter());
		data.setValueTextSize(11f);
		data.setValueTextColor(Color.BLACK);// 饼状图上的文字的颜色
		// data.setValueTypeface(tf);
		mPieChart.setData(data);

		// undo all highlights
		mPieChart.highlightValues(null);

		mPieChart.invalidate();
	}

	/**
	 * 获取饼状图上的非零有效数据
	 * 
	 * @param YValue
	 *            Y值
	 * @param kind
	 *            哪种金额类型
	 */
	private void getPieData(float YValue, int i, int color, String kind,
			String moneyString) {
		if (YValue != 0) {
			yValues.add(new Entry(YValue, i));
			xVals.add(kind);
			colors.add(color);
			addView(color, kind, moneyString);
		}
	}

	/**
	 * 设置饼状图中间颜色
	 * 
	 * @param money
	 * @return
	 */
	private SpannableString generateCenterSpannableText(String money) {

		SpannableString s = new SpannableString(money);
		s.setSpan(new RelativeSizeSpan(1.5f), 0, s.length(), 0);
		s.setSpan(new StyleSpan(Typeface.ITALIC), 0, s.length(), 0);
		s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 0,
				s.length(), 0);
		return s;
	}

	public static final int[] COLORS = { Color.rgb(243, 216, 103),
			Color.rgb(176, 216, 120), Color.rgb(122, 171, 216),
			Color.rgb(236, 128, 121), Color.rgb(238, 139, 138),
			Color.rgb(242, 190, 126) };

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected() {
		// TODO Auto-generated method stub

	}

	// /////////////////////////////////////////////////////////////////////////////////
	/**
	 * 初始化柱状图
	 */
	protected HorizontalBarChart mBarChart;

	private void getHorizontalBarChart() {
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		mBarChart = (HorizontalBarChart) findViewById(R.id.horizontal_barchart);
		mBarChart.setOnChartValueSelectedListener(this);
		// mBarChart.setHighlightEnabled(false);

		mBarChart.setDrawBarShadow(false);

		mBarChart.setDrawValueAboveBar(true);

		mBarChart.setDescription("");

		// if more than 60 entries are displayed in the chart, no values will be
		// drawn
		mBarChart.setMaxVisibleValueCount(60);

		// scaling can now only be done on x- and y-axis separately
		mBarChart.setPinchZoom(false);

		// draw shadows for each bar that show the maximum value
		// mBarChart.setDrawBarShadow(true);

		// mBarChart.setDrawXLabels(false);

		mBarChart.setDrawGridBackground(false);

		// mBarChart.setDrawYLabels(false);

		mBarChart.setDragEnabled(false);// 是否可以拖拽
		mBarChart.setScaleEnabled(false);// 是否可以缩放

		XAxis xl = mBarChart.getXAxis();
		xl.setPosition(XAxisPosition.BOTTOM);
		xl.setDrawAxisLine(true);
		xl.setDrawGridLines(true);
		xl.setGridLineWidth(0.3f);

		YAxis yl = mBarChart.getAxisLeft();
		yl.setDrawAxisLine(true);
		yl.setDrawGridLines(true);
		yl.setGridLineWidth(0.3f);
		// yl.setInverted(true);

		YAxis yr = mBarChart.getAxisRight();
		yr.setDrawAxisLine(true);
		yr.setDrawGridLines(false);
		// yr.setInverted(true);

		mBarChart.animateY(2500);

		mBarChart.setVisibility(View.VISIBLE);
		bar_legend.setVisibility(View.VISIBLE);

		Legend l = mBarChart.getLegend();
		l.setEnabled(false);
		// l.setPosition(LegendPosition.PIECHART_CENTER);
		// l.setFormSize(8f);
		// l.setXEntrySpace(4f);

		// mBarChart.setDrawLegend(false);

	}

	/**
	 * 为柱状图设置数据
	 * 
	 * @param barlist
	 */
	private void setHorizontalBarChartData(List<BarList> barlist) {

		ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
		ArrayList<String> xVals = new ArrayList<String>();

		if (barlist.size()>10) {
			for (int i = 0; i < 10; i++) {
				// xVals.add(mMonths[i % 10]);
				// yVals1.add(new BarEntry((float) (number[i % 10]), i));
				String courseName;
				if (barlist.get(9 - i).barName.length() > 6) {
					courseName = barlist.get(9 - i).barName.substring(0, 5) + "...";
				} else {
					courseName = barlist.get(9 - i).barName;
				}
				xVals.add(courseName);
				yVals1.add(new BarEntry(Float.valueOf(barlist.get(9 - i).barNum), i));
			}
		}else {
			for (int i = 0; i < barlist.size(); i++) {
				String courseName;
				if (barlist.get(barlist.size()-1 - i).barName.length() > 6) {
					courseName = barlist.get(barlist.size()-1 - i).barName.substring(0, 5) + "...";
				} else {
					courseName = barlist.get(barlist.size()-1 - i).barName;
				}
				xVals.add(courseName);
				yVals1.add(new BarEntry(Float.valueOf(barlist.get(barlist.size()-1 - i).barNum), i));
			}
			
			for (int i = 0; i < 10-barlist.size(); i++) {
				xVals.add("");
				yVals1.add(new BarEntry(0, barlist.size()+i));
			}
		}
		

		BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");

		ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
		dataSets.add(set1);

		BarData data = new BarData(xVals, dataSets);
		data.setValueTextSize(10f);

		mBarChart.setData(data);

		bar_legend.setText("总金额：" + bean.entity.total);

		// 添加图例
		// MyLegendItem legendItem = new MyLegendItem();
		// legendItem.initView(context, 0, "总金额：", bean.entity.total);
		// legendItem.setIconVisible(View.GONE);
		// ll_default.addView(legendItem.getRootView());

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
				.from(MyWalletGetIncomeChartActivity.this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(
				MyWalletGetIncomeChartActivity.this);
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
		dialog = new MyAlertDialog(MyWalletGetIncomeChartActivity.this)
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
					dialog.dismiss();

				} else {
					endTime = wheelMain.getTime();
					tv_endtime.setText(endTime);
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

	private void getListener() {

		// 查询最近一个月
		one_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ym = getFilterTime(1) + ":" + data;
				btn_filter.setText("最近一个月");
				upData(ym, "1");
				popup.dismiss();
			}

		});

		// 查询最近三个月
		three_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ym = getFilterTime(3) + ":" + data;
				btn_filter.setText("最近三个月");
				upData(ym, "2");
				popup.dismiss();
			}
		});

		// 查询最近半年
		six_month.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ym = getFilterTime(6) + ":" + data;
				btn_filter.setText("最近半年");
				upData(ym, "3");
				popup.dismiss();
			}
		});

		// 查询最近一年
		one_year.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ym = getFilterTime(12) + ":" + data;
				btn_filter.setText("最近一年");
				upData(ym, "4");
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
	 * 发送请求更新图表数据
	 * 
	 * @param timeString
	 */
	private void upData(String timeString, String querytype) {
		ll_default.removeAllViews();
		getDataRequest(timeString, querytype, "0");
		getDataServer(requestVo, dataCallBack);
	}

	Button cancel2;// 取消按钮
	LinearLayout ll_blank2;// 点击空白
	Button course;// 课程收入排序
	Button student;// 学员收入排序
	Button defualt;// 默认
	private View contentView;

	/**
	 * 按照方案查询的popUpWindow
	 */
	private void schemePopWindow() {
		popView = View.inflate(context,
				R.layout.layout_popupwindow_mywallet_select, null);

		cancel2 = (Button) popView.findViewById(R.id.cancel_select);
		course = (Button) popView.findViewById(R.id.course);
		student = (Button) popView.findViewById(R.id.student);
		defualt = (Button) popView.findViewById(R.id.defualt);
		ll_blank2 = (LinearLayout) popView.findViewById(R.id.ll_blank_select);
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

		getListener2();// pop内部的点击事件
	}

	private void getListener2() {

		// 默认
		defualt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "0";
				btn_filter.setText("默认");
				popup.dismiss();
			}
		});

		// 按课程查询
		course.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "1";
				btn_filter.setText("按课程显示");
				popup.dismiss();
			}
		});

		// 按学员姓名查询
		student.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type = "2";
				btn_filter.setText("按学员消费显示");
				popup.dismiss();
			}
		});

		// 取消pupupwindow
		cancel2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		});
		// 取消pupupwindow
		ll_blank2.setOnClickListener(new OnClickListener() {

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
