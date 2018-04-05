package cc.upedu.online.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class StringUtil {

	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd",Locale.CHINESE);
	
	public static SimpleDateFormat nDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss",Locale.CHINESE);
	/**
	  * 获取现在时间
	  * 
	  * @return 返回短时间字符串格式yyyy-MM-dd
	  */
	public static String getStringDateShort() {
	  Date currentTime = new Date();
	  String dateString = mDateFormat.format(currentTime);
	  return dateString;
	}
	/**
	  * 获取现在时间
	  * 
	  * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
	  */
	public static String getStringDate() {
	  Date currentTime = new Date();
	  String dateString = nDateFormat.format(currentTime);
	  return dateString;
	}
	/**
	  * 获取时间 小时:分;秒 HH:mm:ss
	  * 
	  * @return
	  */
	public static String getTimeShort(long milliseconds) {
	  SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss",Locale.CHINESE);
	  Date currentTime = new Date(milliseconds);
	  String dateString = formatter.format(currentTime);
	  return dateString;
	}
	/**
	 * 获取指定时间的时间毫秒值
	 * @param stringData 字符串格式 "yyyy-MM-dd HH:mm"
	 * @return 时间毫秒值
	 */
	public static long getLongDate(String stringData,String styleData) {
		// TODO Auto-generated method stub
		long longDate = 0;
		try {
			//先把字符串转成Date类型
			SimpleDateFormat sdf = new SimpleDateFormat(styleData);
			//此处会抛异常
			Date date = sdf.parse(stringData);
			//获取毫秒数
			longDate = date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return longDate;
	}
	/**
	 * 将字符串数据转化为毫秒数
	 *
	 * @param dateTime
	 *            格式:yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static long stringToMillis(String dateTime) {
		if (StringUtil.isEmpty(dateTime)) {
			return -1;
		}
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(nDateFormat.parse(dateTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return c.getTimeInMillis();
	}
	/**
	 * 将毫秒数转化为时间
	 *
	 * @param millis
	 *            毫秒值
	 * @return 格式:"yyyy-MM-dd HH:mm:ss"
	 */
	public static String millisToString(long millis) {
		Date date = new Date(millis);
		return nDateFormat.format(date);
	}

	/**
	 * 根据回复的时间字符串,返回显示到布局的回复的时间,
	 *
	 * @param dateTime
	 *            格式:"yyyy-MM-dd HH:mm:ss"
	 * @return 格式:当天的回复显示具体时间 ,昨天,前天,发表于3天前...
	 */
	public static String replyTimeStr(String dateTime) {
		long replyTimeMillis = stringToMillis(dateTime);
		long currentTimeMillis = System.currentTimeMillis();
		long mTime = currentTimeMillis - replyTimeMillis;
		long time = 1000 * 24 * 60 * 60;
		if (mTime < 60 * 1000) {// 一分钟之内
			return "发表于" + (int) (mTime / 1000) + "秒前";
		} else if (mTime < time) {
			return "发表于" + getTimeShort(replyTimeMillis);
		} else if (mTime < time * 2) {
			return "发表于昨天";
		} else if (mTime < time * 3) {
			return "发表于前天";
		} else if (mTime < time * 30) {
			return "发表于" + (int) (mTime / time) + "天前";
		} else if (mTime < time * 365) {
			return "发表于" + (int) (mTime / (time * 30)) + "月前";
		} else {
			return "发表于" + (int) (mTime / (time * 365)) + "年前";
		}
	}
	public static boolean isEmpty(CharSequence input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotEmpty(String s) {
		return (s != null && s.trim().length() > 0);
	}


	// 获取app缓存文件大小
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			java.io.File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	// 转换文件大小
	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS==0) {
			fileSizeString = "";
		}else if (fileS < 1024 && fileS > 0) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!StringUtil.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) {// 如果下面还有文件
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
//							file.delete();
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	// 存储路径
	public static String getFilePath(String vid, Context context, String drm) {
		// 判断sd卡是否存在
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			File dir = new File(sdDir + "/xuepaiVideo");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String path = dir + "/" + vid;
			if (drm != null && drm.equals("1")) {
				path += ".pcm";
			} else {
				path += ".mp4";
			}
			return path;
		} else {
			Toast.makeText(context, "sd卡不存在！", Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	/**
	 * 获取手机的型号
	 */
	public static String getPhoneType(){
		return android.os.Build.MODEL;
	}
	
 	
	/**
	 * 获取手机的品牌
	 */
	public static String getPhoneBrand(){
		return android.os.Build.BRAND;
	}
	
	/**
	 * 获取手机屏幕的宽高
	 */
	public static String getScreenSize(Activity activity) {
		DisplayMetrics metric = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		return String.valueOf(width) + "*" + String.valueOf(height);
	}
	/**
	 * 获取手机屏幕的宽高
	 */
	public static String getScreenWidthSize(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
//		int height = metric.heightPixels; // 屏幕高度（像素）
		return String.valueOf(width);
	}
	/**
	 * 获取手机屏幕的宽高
	 */
	public static String getScreenHeightSize(Context context) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
//		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		return String.valueOf(height);
	}
	
	/**
	 * 检查缓存目录下是否有缓存文件夹，没有：新建，有：清空
	 */
	public static void buildDir(final Context context,final String dir) {
			
			new Thread(){
				public void run() {
					String savePath = context.getCacheDir().getAbsolutePath() + "/" + dir + "/" ; 
//					String savePath = Environment.getExternalStorageDirectory(). + "/" + dir + "/" ; 
					File upedu = new File ( savePath ); 
					if (upedu.exists()) { 
						StringUtil.deleteFolderFile(savePath+upedu, true);
					}				
					upedu . mkdirs (); 
				};
			}.start();
			
	
		}
	/**
	 *  
	 * [获取cpu类型和架构] 
	 *  
	 * @return  
	 * 三个参数类型的数组，第一个参数标识是不是ARM架构，第二个参数标识是V6还是V7架构，第三个参数标识是不是neon指令集 
	 */  
	public static boolean getCpuArchitecture() {  
		boolean isArmCPU = false;
	    try {  
	        InputStream is = new FileInputStream("/proc/cpuinfo");  
	        InputStreamReader ir = new InputStreamReader(is);  
	        BufferedReader br = new BufferedReader(ir);  
	        try {
	        	char[] data = new char[2048];
	        	br.read(data);
	        	String valueOf = String.valueOf(data);
	        	if (!StringUtil.isEmpty(valueOf) && valueOf.contains("ARM")) {
	        		isArmCPU = true;
				}else {
					isArmCPU = false;
				}
	        } finally {  
	            br.close();  
	            ir.close();  
	            is.close();  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	  
	    return isArmCPU;  
	}  

	/**
	 * 把小写数字转换成大小数字字符串
	 * @param num 范围1~
	 * @return
	 */
	public static String numberLower2Capital(int num){
		if (num > 0) {
			String numLower = String.valueOf(num);
			ArrayList<String> arrayList = new ArrayList<>();
			int length = numLower.length();
			int n = 1;
			for (int i = length-n; i > -1; i--) {
				arrayList.add(0, numberCapital[(Integer.valueOf(String.valueOf(numLower.charAt(i))))]);
				n++;
			}
			StringBuffer stringBuffer = new StringBuffer();
			for (String str : arrayList) {
				stringBuffer.append(str);
			}
			return stringBuffer.toString();
		}else {
			return "";
		}
	}
	
	public static String[] numberCapital = {"零","一","二","三","四","五","六","七","八","九"};
}
