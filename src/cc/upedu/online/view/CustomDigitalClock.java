package cc.upedu.online.view;

/*
 * Copyright (C) 2012 The * Project
 * All right reserved.
 * Version 1.00 2012-2-11
 * Author veally@foxmail.com
 */

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.DigitalClock;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Custom digital clock
 *
 * @author veally@foxmail.com
 */
public class CustomDigitalClock extends DigitalClock {
	Calendar mCalendar;
	private final static String m12 = "h:mm aa";
	private final static String m24 = "k:mm";
	private FormatChangeObserver mFormatChangeObserver;
	private Runnable mTicker;
	private Handler mHandler;
	private long endTime;
	public static long distanceTime;
	private ClockListener mClockListener;
	private static boolean isFirst;
	private boolean mTickerStopped;
	private int type = 0;
	
	@SuppressWarnings("unused")
	private String mFormat;
	
	
	public CustomDigitalClock(Context context) {
		super(context);
		initClock(context);
	}
	
	public CustomDigitalClock(Context context, AttributeSet attrs) {
		super(context, attrs);
		initClock(context);
	}
	
	private void initClock(Context context) {
		if (mCalendar == null) {
			mCalendar = Calendar.getInstance();
		}
		
		mFormatChangeObserver = new FormatChangeObserver();
		getContext().getContentResolver().registerContentObserver(
			Settings.System.CONTENT_URI, true, mFormatChangeObserver);
			setFormat();
	}
	
	@Override
	protected void onAttachedToWindow() {
		mTickerStopped = false;
		super.onAttachedToWindow();
		mHandler = new Handler();
		/**
		* requests a tick on the next hard-second boundary
		*/
		mTicker = new Runnable() {
			public void run() {
			
				if (mTickerStopped)
				return;
				
				long currentTime = System.currentTimeMillis();
				if (currentTime / 1000 == endTime / 1000 - 5 * 60) {
					if (mClockListener != null) {
						mClockListener.remainFiveMinutes();
					}
				}
				distanceTime = endTime - currentTime;
				distanceTime /= 1000;
				if (distanceTime == 0) {
					if (type == 0) {
						setText("已播出!");
					}else if (type == 1) {
						setText("已开始!");
					}
					
					onDetachedFromWindow();
					if (mClockListener != null) {
						mClockListener.timeEnd();
					}
				} else if (distanceTime < 0) {
					if (type == 0) {
						setText("已播出!");
					}else if (type == 1) {
						setText("已开始!");
					}
					onDetachedFromWindow();
					if (mClockListener != null) {
						mClockListener.timeEnd();
					}
				} else {
					setText(dealTime(distanceTime));
				}
				invalidate();
				long now = SystemClock.uptimeMillis();
				long next = now + (1000 - now % 1000);
				mHandler.postAtTime(mTicker, next);
			}
		};
		mTicker.run();
	}

	/**
	* deal time string
	*
	* @param time
	* @return
	*/
	public static Spanned dealTime(long time) {
		Spanned str;
		StringBuffer returnString = new StringBuffer();
		long day = time / (24 * 60 * 60);
		long hours = (time % (24 * 60 * 60)) / (60 * 60);
		long minutes = ((time % (24 * 60 * 60)) % (60 * 60)) / 60;
		long second = ((time % (24 * 60 * 60)) % (60 * 60)) % 60;
		String dayStr = String.valueOf(day);
		String hoursStr = timeStrFormat(String.valueOf(hours));
		String minutesStr = timeStrFormat(String.valueOf(minutes));
		String secondStr = timeStrFormat(String.valueOf(second));
	
	
		returnString.append(dayStr).append("天").append(hoursStr).append("小时")
		.append(minutesStr).append("分钟").append(secondStr).append("秒");
		str = Html.fromHtml(returnString.toString());
//		if (day >= 10) {
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 2, 3,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 5, 7,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 9, 11,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 13, 14,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		} else {
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 1, 2,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 4, 6,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 8, 10,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			((Spannable) str).setSpan(new AbsoluteSizeSpan(16), 12, 13,
//			Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//		}
	
	
	// return returnString.toString();
		return str;
	}
	
	/**
	  * 两个时间之间的天数
	  * 
	  * @param futureDate 未来的时间
	  * @param currentData 当前时间（被减）
	  * @return
	  */
	public static long getDays(String futureDate, String currentData) {
	  if (futureDate == null || futureDate.equals(""))
	   return 0;
	  if (currentData == null || currentData.equals(""))
	   return 0;
	  // 转换为标准时间
	  SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINESE);
	  java.util.Date date = null;//未来时间
	  java.util.Date mydate = null;//当前、
	  try {
	   date = myFormatter.parse(futureDate);
	   mydate = myFormatter.parse(currentData);
	  } catch (Exception e) {
	  }
	  //判断活动开始时间是不是晚于当前时间，如果晚于当前时间，则返回0
	  if(date.getTime() - mydate.getTime()<=0){
		  return 0;
	  }else{
		  long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
		  return day;
	  }
	}
	
	
	/**
	* format time
	*
	* @param timeStr
	* @return
	*/
	private static String timeStrFormat(String timeStr) {
		switch (timeStr.length()) {
		case 1:
			timeStr = "0" + timeStr;
			break;
		}
		return timeStr;
	}
	
	/**
	* Clock end time from now on.
	*
	* @param endTime
	* 				0表示是在直播模块使用
	* 				1表示是在活动模块使用
	*/
	public void setEndTime(long endTime,int type) {
		this.endTime = endTime;
		this.type = type;
		mTickerStopped = false;
	}
	
	
	/**
	* Pulls 12/24 mode from system settings
	*/
	private boolean get24HourMode() {
		return android.text.format.DateFormat.is24HourFormat(getContext());
	}
	
	
	private void setFormat() {
		if (get24HourMode()) {
			mFormat = m24;
		} else {
			mFormat = m12;
		}
	}
	
	
	private class FormatChangeObserver extends ContentObserver {
		public FormatChangeObserver() {
			super(new Handler());
		}
	
	
		@Override
		public void onChange(boolean selfChange) {
			setFormat();
		}
	}
	
	
	public void setClockListener(ClockListener clockListener) {
		this.mClockListener = clockListener;
	}
	
	
	public interface ClockListener {
		void timeEnd();
		void remainFiveMinutes();
	}
}