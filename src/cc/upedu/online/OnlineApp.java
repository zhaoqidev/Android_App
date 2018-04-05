package cc.upedu.online;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.upedu.online.ryim.MyConversationBehaviorListener;
import cc.upedu.online.ryim.MyReceiveMessageListener;
import io.rong.imkit.RongIM;

public class OnlineApp extends Application {
	public Context context;
	public static OnlineApp myApp;
	public Object object;
	public ExecutorService instance;
	public LinkedList<Activity> mActivitys=new LinkedList<Activity>();

	//显示图片的配置  
    public Builder builder;
    //ImageLoader对象
    public ImageLoader imageLoader;

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		myApp = this;
		context = getApplicationContext();
		instance=Executors.newFixedThreadPool(10);
//		CrashReport.initCrashReport(context, "900014942", false);
//		CrashReport.testJavaCrash();

//		MobclickAgent.setDebugMode(true);
		
		//显示图片的配置  
        builder = new DisplayImageOptions.Builder()  
                .cacheInMemory(true)  
                .cacheOnDisc(true)  
                .bitmapConfig(Bitmap.Config.RGB_565) ; 
		File cacheDir = new File(Environment.getExternalStorageDirectory().getPath()+"/Upedu/picCahce2");
		imageLoader=ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)  
		        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions  
//		        .diskCacheExtraOptions(480, 800, null)  
		        .threadPoolSize(3) // default  
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default  
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default  
		        .denyCacheImageMultipleSizesInMemory()  
		        .memoryCache(new LruMemoryCache(2 * 1024 * 1024))  
		        .memoryCacheSize(2 * 1024 * 1024)  
		        .memoryCacheSizePercentage(13) // default  
		        .discCache(new UnlimitedDiscCache(cacheDir)) // default  
		        .discCacheSize(200 * 1024 * 1024)  
		        .discCacheFileCount(500)  
		        .discCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default  
		        .imageDownloader(new BaseImageDownloader(context)) // default  
//		        .imageDecoder(new BaseImageDecoder()) // default  
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default  
		        .writeDebugLogs().defaultDisplayImageOptions(builder.build())  
		        .build();
		imageLoader.init(config);//全局初始化此配置  

//		CrashHandler crashHandler = CrashHandler.getInstance() ;
//		crashHandler.init(this) ;
		
		 if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
	                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

	            /**
	             * IMKit SDK调用第一步 初始化
	             */
	            RongIM.init(this);
	            /**
	            * 设置会话界面操作的监听器。
	            */
	            RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
	            /**
    		     * 设置连接状态变化的监听器.
    		     */
//	            if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
//	    		    RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener(context));
//	    		}
			 /**
			  *  设置接收消息的监听器。
			  */
			 RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener(context));
		 }
	}
	public static boolean hasMess = false;
	/**
     * 获得当前进程的名字
     *
     * @param context
     * @return 进程号
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
	
	
	
	
	/**
	 * Activity开启时添加Activity到集合
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		mActivitys.addFirst(activity);
	}
	/**
	 * Activity退出时清除集合中的Activity.
	 * @param activity
	 */
	public void removeActivity(Activity activity){
		mActivitys.remove(activity);
	}
	public void exit() {
		for (Activity activity : mActivitys) {
			activity.finish();
		}
		//杀死进程,推出整个应用
		//threadPoolManager.stopAllTask();
//		int  sdk=Integer.parseInt(android.os.Build.VERSION.SDK);  
//		if(sdk<=10){
		RongIM.getInstance().disconnect();
			System.exit(0);
//		}else{
//			android.os.Process.killProcess(android.os.Process.myPid());
			
//		}
	}
	
}
