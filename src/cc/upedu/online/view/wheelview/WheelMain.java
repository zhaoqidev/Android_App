
package cc.upedu.online.view.wheelview;

import android.view.View;

import java.util.Arrays;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.utils.StringUtil;

public class WheelMain {

	private View view;
	private WheelView wv_year;
	private WheelView wv_month;
	private WheelView wv_day;
	private WheelView wv_hours;
	private WheelView wv_mins;
	public int screenheight;
	private boolean hasSelectTime, hasSelectDay;
	private static int START_YEAR, END_YEAR;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public static int getSTART_YEAR() {
		return START_YEAR;
	}

	public static void setSTART_YEAR(int sTART_YEAR) {
		START_YEAR = sTART_YEAR;
	}

	public static int getEND_YEAR() {
		return END_YEAR;
	}

	public static void setEND_YEAR(int eND_YEAR) {
		END_YEAR = eND_YEAR;
	}
	/**
	 * 生成的时间选择器中的时间范围是2015年--当前年之间的如果年,只能选择年月
	 * 用于过去项目的查询
	 * @param view
	 */
	public WheelMain(View view) {
		super();
		this.view = view;
		hasSelectTime = false;
		hasSelectDay = false;
		String currentYear = StringUtil.getStringDateShort().split("-")[0];
		START_YEAR = Integer.valueOf("2015");
		END_YEAR = Integer.valueOf(currentYear);
		setView(view);
	}
	
	/**
	 * 生成的时间选择器中的时间范围是2015年--当前年之间的如果年,只能选择年月
	 * 用于过去项目的查询
	 * @param view
	 */
	public WheelMain(View view,String hasSelectDay) {
		super();
		this.view = view;
		this.hasSelectTime = false;
		this.hasSelectDay = true;
		String currentYear = StringUtil.getStringDateShort().split("-")[0];
		START_YEAR = Integer.valueOf("2015");
		END_YEAR = Integer.valueOf(currentYear);
		setView(view);
	}
	
	/**
	 * 生成的时间选择器中的时间范围是110年前--10年前之间的100年 可控制是否显示日,不能选择时间
	 * 用于年龄的选择
	 * @param view
	 * @param hasSelectDay true表示可以选择年月日,false表示可以选择年月
	 */
	public WheelMain(View view, boolean hasSelectDay) {
		super();
		this.view = view;
		this.hasSelectDay = hasSelectDay;
		this.hasSelectTime = false;
		String currentYear = StringUtil.getStringDateShort().split("-")[0];
		START_YEAR = Integer.valueOf(currentYear) - 110;
		END_YEAR = Integer.valueOf(currentYear) - 10;
		setView(view);
	}
	/**
	 * 生成的时间选择器中的时间范围是现在--1年后之间的2年,可以控制显示日和时分
	 * 用于未来(近期)项目的标识
	 * @param view
	 * @param hasSelectDay true表示可以选择日,false表示不可以选择日
	 * @param hasSelectTime true表示可以选择时分,false表示不可以选择时分
	 */
	public WheelMain(View view, boolean hasSelectDay,boolean hasSelectTime) {
		super();
		this.view = view;
		this.hasSelectDay = hasSelectDay;
		this.hasSelectTime = hasSelectTime;
		String currentYear = StringUtil.getStringDateShort().split("-")[0];
		START_YEAR = Integer.valueOf(currentYear);
		END_YEAR = Integer.valueOf(currentYear) + 1;
		setView(view);
	}
	
	public void initDateTimePicker(int year, int month) {
		this.initDateTimePicker(year, month, 0, 0, 0);
	}
	
	public void initDateTimePicker(int year, int month, int day) {
		this.initDateTimePicker(year, month, day, 0, 0);
	}

	/**
	 * @Description: TODO 弹出日期时间选择器
	 */
	public void initDateTimePicker(int year, int month, int day, int hour, int minute) {
		// int year = calendar.get(Calendar.YEAR);
		// int month = calendar.get(Calendar.MONTH);
		// int day = calendar.get(Calendar.DATE);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };

		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);

		// 年
		wv_year = (WheelView) view.findViewById(R.id.year);
		wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
		wv_year.setCyclic(!(wv_year.getAdapter().getItemsCount() < 5));// 可循环滚动
		wv_year.setLabel("年");// 添加文字
		wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据

		// 月
		wv_month = (WheelView) view.findViewById(R.id.month);
		wv_month.setAdapter(new NumericWheelAdapter(1, 12));
		wv_month.setCyclic(true);
		wv_month.setLabel("月");
		wv_month.setCurrentItem(month);
		
		// 日
		wv_day = (WheelView) view.findViewById(R.id.day);
		if (hasSelectDay) {
			wv_day.setVisibility(View.VISIBLE);
			
			wv_day.setCyclic(true);
			// 判断大小月及是否闰年,用来确定"日"的数据
			if (list_big.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 31));
			} else if (list_little.contains(String.valueOf(month + 1))) {
				wv_day.setAdapter(new NumericWheelAdapter(1, 30));
			} else {
				// 闰年
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
					wv_day.setAdapter(new NumericWheelAdapter(1, 29));
				else
					wv_day.setAdapter(new NumericWheelAdapter(1, 28));
			}
			wv_day.setLabel("日");
			wv_day.setCurrentItem(day - 1);
		}else {
			wv_day.setVisibility(View.GONE);
		}

		wv_hours = (WheelView) view.findViewById(R.id.hour);
		wv_mins = (WheelView) view.findViewById(R.id.min);
		if (hasSelectTime) {
			wv_hours.setVisibility(View.VISIBLE);
			wv_mins.setVisibility(View.VISIBLE);

			wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
			wv_hours.setCyclic(true);// 可循环滚动
			wv_hours.setLabel("时");// 添加文字
			wv_hours.setCurrentItem(hour);

			wv_mins.setAdapter(new NumericWheelAdapter(0, 59));
			wv_mins.setCyclic(true);// 可循环滚动
			wv_mins.setLabel("分");// 添加文字
			wv_mins.setCurrentItem(minute);
			
		} else {
			wv_hours.setVisibility(View.GONE);
			wv_mins.setVisibility(View.GONE);
		}

		// 添加"年"监听
		OnWheelChangedListener wheelListener_year = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + START_YEAR;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big
						.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(wv_month
						.getCurrentItem() + 1))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if ((year_num % 4 == 0 && year_num % 100 != 0)
							|| year_num % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		// 添加"月"监听
		OnWheelChangedListener wheelListener_month = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					wv_day.setAdapter(new NumericWheelAdapter(1, 30));
				} else {
					if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year
							.getCurrentItem() + START_YEAR) % 100 != 0)
							|| (wv_year.getCurrentItem() + START_YEAR) % 400 == 0)
						wv_day.setAdapter(new NumericWheelAdapter(1, 29));
					else
						wv_day.setAdapter(new NumericWheelAdapter(1, 28));
				}
			}
		};
		wv_year.addChangingListener(wheelListener_year);
		wv_month.addChangingListener(wheelListener_month);

		// 根据屏幕密度来指定选择器字体的大小(不同屏幕可能不同)
		int textSize = 0;
		if (hasSelectTime)
			textSize = (int) ((screenheight / 100) * 2.8f);
		else
			textSize = (screenheight / 100) * 3;
		wv_day.TEXT_SIZE = textSize;
		wv_month.TEXT_SIZE = textSize;
		wv_year.TEXT_SIZE = textSize;
		wv_hours.TEXT_SIZE = textSize;
		wv_mins.TEXT_SIZE = textSize;

	}

	public String getTime() {
		StringBuffer sb = new StringBuffer();
		String str_month;
		String str_day;
		String str_hours;
		String str_mins;
		if (!hasSelectDay && !hasSelectTime) {
			if (wv_month.getCurrentItem() + 1 < 10) {
				str_month = "0"+(wv_month.getCurrentItem() + 1);
			}else {
				str_month = String.valueOf(wv_month.getCurrentItem() + 1);
			}
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
			.append(str_month);
		}else if (hasSelectDay && !hasSelectTime){
			if (wv_month.getCurrentItem() + 1 < 10) {
				str_month = "0"+(wv_month.getCurrentItem() + 1);
			}else {
				str_month = String.valueOf(wv_month.getCurrentItem() + 1);
			}
			if (wv_day.getCurrentItem() + 1 < 10) {
				str_day = "0"+(wv_day.getCurrentItem() + 1);
			}else {
				str_day = ""+(wv_day.getCurrentItem() + 1);
			}
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
			.append(str_month).append("-")
			.append(str_day);
		}else if (hasSelectDay && hasSelectTime) {
			
			if (wv_month.getCurrentItem() + 1 < 10) {
				str_month = "0"+(wv_month.getCurrentItem() + 1);
			}else {
				str_month = String.valueOf(wv_month.getCurrentItem() + 1);
			}
			if (wv_day.getCurrentItem() + 1 < 10) {
				str_day = "0"+(wv_day.getCurrentItem() + 1);
			}else {
				str_day = ""+(wv_day.getCurrentItem() + 1);
			}
			if (wv_hours.getCurrentItem()  < 10) {
				str_hours="0"+(wv_hours.getCurrentItem() );
			}else {
				str_hours=""+(wv_hours.getCurrentItem() );
			}
			if (wv_mins.getCurrentItem()  < 10) {
				str_mins="0"+(wv_mins.getCurrentItem() );
			}else {
				str_mins=""+(wv_mins.getCurrentItem() );
			}
			sb.append((wv_year.getCurrentItem() + START_YEAR)).append("-")
			.append((str_month)).append("-")
			.append((str_day)).append(" ")
			.append(str_hours).append(":")
			.append(str_mins);
		}
		return sb.toString();
	}
}
