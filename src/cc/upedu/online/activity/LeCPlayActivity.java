package cc.upedu.online.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;

import cc.upedu.online.LeCloud.LeCloudVideoPlay;
import cc.upedu.online.R;
import cc.upedu.online.base.BaseActivity;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 课程体系界面
 * @author Administrator
 *
 */
public class LeCPlayActivity extends BaseActivity implements OnClickListener {
	private FrameLayout fl_margin;
	// 课程简介视频的布局
	private LinearLayout layout_video;
	//视频没播放时显示的介绍图片iv_introduce_landscape横屏,iv_introduce竖屏
//	private ImageView iv_introduce_landscape;
	private ImageView iv_introduce;
	//视频窗口中间得播放键
	private ImageView iv_playbtn;
	private String videoId;
//	private boolean isCCPlay;
	private LeCloudVideoPlay mLeCloudVideoPlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		initVideoLecPlay();
		if (mLeCloudVideoPlay != null) {
			mLeCloudVideoPlay.onResume(videoId,false,null);
		}
	}
	@Override
	protected void onPause() {
		removePlayer();
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		removePlayer();
		super.onDestroy();
		SharedPreferencesUtil.getInstance().editPutString("answerId", "");
	}
	/**
	 * 回收播放器
	 */
	private void removePlayer() {
		if (mLeCloudVideoPlay != null) {
			mLeCloudVideoPlay.onDestroy();
			mLeCloudVideoPlay = null;
		}
		layout_video.removeAllViews();
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
			//横屏
			mLeCloudVideoPlay.changeLayoutParams(true, 0);
		} else if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
			//竖屏
			mLeCloudVideoPlay.changeLayoutParams(false, 200);
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
        		//横屏时,把视频切换成竖屏
        		mLeCloudVideoPlay.changeOrientation();
    			return false; 
    		} else {
    			return super.onKeyDown(keyCode, event);
    		}
        }else {
            return super.onKeyDown(keyCode, event); 
        } 
    } 
	
	@Override
	protected void initView() {
		setContentView(R.layout.activity_lecplay);
		videoId = getIntent().getStringExtra("videoId");
		SharedPreferencesUtil.getInstance().editPutString("answerId", videoId);

		fl_margin = (FrameLayout) findViewById(R.id.fl_margin);
		layout_video = (LinearLayout) findViewById(R.id.layout_video);
		iv_introduce = (ImageView) findViewById(R.id.iv_introduce);
//		iv_introduce_landscape = (ImageView) findViewById(R.id.iv_introduce_landscape);
		iv_playbtn = (ImageView) findViewById(R.id.iv_playbtn);
		
		initVideoLecPlay();
		//判断在设置中是否允许在非wifi下播放视频
		if (!SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay")) {
			//判断WiFi是否连接
			if (NetUtil.isWifiConnected(context)) {
				//判断在设置中视频是否自动播放
				if (SharedPreferencesUtil.getInstance().spGetBoolean("isAutoPlay")) {

					mLeCloudVideoPlay.playVideo(videoId,false,null,getNode());
				}
			}else {
				ShowUtils.showMsg(context, "您的网络状态非WiFi,请检查您的网络!");
			}
		}else {
			//判断是否有网络连接
			if (NetUtil.hasConnectedNetwork(context)) {
				//判断在设置中视频是否自动播放
				if (SharedPreferencesUtil.getInstance().spGetBoolean("isAutoPlay")) {
					//判断是否有网络连接
					if (NetUtil.hasConnectedNetwork(context)) {
						mLeCloudVideoPlay.playVideo(videoId,false,null,getNode());
					}else {
						ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
					}
				}
			}else {
				ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
			}
		}
	}
	private int getNode(){
		HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
		int node = -1;
		if (map.containsKey("zid"+videoId)){
			node = map.get("zid"+videoId);
		}
		return node;
	}
	/**
	 * 初始化乐视的视频播放器
	 */
	private void initVideoLecPlay() {
		if (mLeCloudVideoPlay == null) {
			mLeCloudVideoPlay = LeCloudVideoPlay.getInstance(context);
			this.layout_video.addView(mLeCloudVideoPlay.initPlayer(iv_introduce, iv_playbtn,null));
			mLeCloudVideoPlay.changeOrientationLandscape();
		}
	}
	@Override
	protected void initListener() {
		iv_playbtn.setOnClickListener(this);
		fl_margin.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_playbtn: // 播放的点击事件
			playVideo();
			break;
		case R.id.fl_margin:
			finish();
			break;
		}
	}
	private void playVideo() {
		//判断在设置中是否允许在非wifi下播放视频
		if (!SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay", true)) {
			//判断WiFi是否连接
			if (NetUtil.isWifiConnected(context)) {
				mLeCloudVideoPlay.playVideo(videoId,false,null,getNode());
				mLeCloudVideoPlay.changeOrientationLandscape();
			}else {
				ShowUtils.showMsg(context, "您的网络状态非WiFi,请检查您的网络!");
			}
		}else {
			//判断是否有网络连接
			if (NetUtil.hasConnectedNetwork(context)) {
				mLeCloudVideoPlay.playVideo(videoId,false,null,getNode());
				mLeCloudVideoPlay.changeOrientationLandscape();
			}else {
				ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
			}
		}
	}
	@Override
	protected void initData() {
	}
}
