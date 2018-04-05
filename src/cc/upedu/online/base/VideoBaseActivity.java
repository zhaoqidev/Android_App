package cc.upedu.online.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;

import cc.upedu.online.CCMediaPlay.CCMediaPlayFull;
import cc.upedu.online.CCMediaPlay.CCMediaPlayView;
import cc.upedu.online.LeCloud.LeCloudVideoPlay;
import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;

/**
 * 带视频的activity,分上下两部分,上方是视频窗口,整体滚动
 * 
 * @author Administrator
 *
 */
public abstract class VideoBaseActivity extends TitleBaseActivity {
	private static final int REQUEST_CCPOSITION = 3;
	private ImageView iv_introduce;
	private ImageView iv_playbtn;
	// 课程简介视频的布局
	private LinearLayout layout_video,ll_Content,ll_videobottom;
	private LeCloudVideoPlay mLeCloudVideoPlay;
	private CCMediaPlayView mCcMediaPlay;
	private boolean isCCPlay;
	private int ccPosition;//cc视频播放位置
	protected String videoId;
	
	@Override
	protected View initContentView(){
		View view = View.inflate(context, R.layout.layout_video_base, null);
		iv_introduce = (ImageView) view.findViewById(R.id.iv_introduce);
		iv_playbtn = (ImageView) view.findViewById(R.id.iv_playbtn);
		layout_video = (LinearLayout) view.findViewById(R.id.layout_video);
		ll_videobottom = (LinearLayout) view.findViewById(R.id.ll_videobottom);
		View videoBottom = initVideoBottom();
		if (videoBottom != null) {
			ll_videobottom.setVisibility(View.VISIBLE);
			ll_videobottom.addView(videoBottom);
		}else {
			ll_videobottom.setVisibility(View.GONE);
		}
		
		ll_Content = (LinearLayout) view.findViewById(R.id.ll_Content);
		View twelfthLayout = initTwelfthLayout();
		if (twelfthLayout != null) {
			ll_Content.setVisibility(View.VISIBLE);
			ll_Content.addView(twelfthLayout);
		}
		return view;
	}
	public abstract View initVideoBottom();
	
	@Override
	protected void initListener() {
		// TODO Auto-generated method stub
		super.initListener();
		iv_playbtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch (v.getId()) {
		case R.id.iv_playbtn:
			// 播放的点击事件
			if (StringUtil.isEmpty(videoId)) {
				ShowUtils.showMsg(context, "请求数据失败!");
			}else {
				//判断在设置中是否允许在非wifi下播放视频
				if (!SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay", true)) {
					//判断WiFi是否连接
					if (NetUtil.isWifiConnected(context)) {
						if (isCCPlay) {
							mCcMediaPlay.play(videoId,getNode(),false,null);
							iv_introduce.setVisibility(View.GONE);
							iv_playbtn.setVisibility(View.GONE);
						}else {
							mLeCloudVideoPlay.playVideo(videoId,false,null,getNode());
						}
						if (View.VISIBLE == ll_videobottom.getVisibility()) {
							ll_videobottom.setVisibility(View.GONE);
						}
					}else {
						ShowUtils.showMsg(context, "您的网络状态非WiFi,请检查您的网络!");
					}
				}else {
					//判断是否有网络连接
					if (NetUtil.hasConnectedNetwork(context)) {
						if (isCCPlay) {
							mCcMediaPlay.play(videoId,getNode(),false,null);
							iv_introduce.setVisibility(View.GONE);
							iv_playbtn.setVisibility(View.GONE);
						}else {
							mLeCloudVideoPlay.playVideo(videoId,false,null,getNode());
						}
						if (View.VISIBLE == ll_videobottom.getVisibility()) {
							ll_videobottom.setVisibility(View.GONE);
						}
					}else {
						ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
					}
				}
			}
			break;
		}
		if (R.id.iv_playbtn == v.getId()) {
		}
	}

	private int getNode(){
		HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
		int node = -1;
		if (map.containsKey("cid"+videoId)){
			node = map.get("cid"+videoId);
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
		}
	}
	/**
	 * 初始化CC的视频播放器
	 */
	private void initVideoCCPlay() {
		if (mCcMediaPlay == null) {
			mCcMediaPlay = new CCMediaPlayView(context);
			this.layout_video.addView(mCcMediaPlay.initView());
			if (!isFullScreen) {
				mCcMediaPlay.showFullScreen(View.GONE);
			}
			
		}
	}
	public void initVideoData(String videoId,String courseName,String courseLogo,String videotype){
		this.videoId = videoId;
		OnlineApp.myApp.imageLoader.displayImage(
      		  ConstantsOnline.SERVER_IMAGEURL+courseLogo, 
      		  iv_introduce, 
      		  OnlineApp.myApp.builder
      			.showImageOnLoading(R.drawable.rollview_default)  
      	        .showImageOnFail(R.drawable.rollview_default)
      	        .build());
		setTitleText(courseName);
		if ("LETV".equals(videotype)) {
			isCCPlay = false;
		}else if ("CC".equals(videotype)) {
			isCCPlay = true;
		}
//		isCCPlay = true;
//		isCCPlay = SharedPreferencesUtil.getInstance().spGetBoolean("isCCPlay");
		if (isCCPlay) {
			initVideoCCPlay();
		}else {
			initVideoLecPlay();
		}
		if (!StringUtil.isEmpty(videoId)) {
			//判断在设置中视频是否自动播放
			if (SharedPreferencesUtil.getInstance().spGetBoolean("isAutoPlay")) {
				//判断在设置中是否允许在非wifi下播放视频
				if (!SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay", true)) {
					//判断WiFi是否连接
					if (NetUtil.isWifiConnected(context)) {
						if (isCCPlay) {
							mCcMediaPlay.play(videoId,getNode(),false,null);
							iv_introduce.setVisibility(View.GONE);
							iv_playbtn.setVisibility(View.GONE);
						}else {
							mLeCloudVideoPlay.playNewVideo(videoId,false,null,getNode());
						}
					}else {
						ShowUtils.showMsg(context, "您的网络状态非WiFi,请检查您的网络!");
					}
				}else {
					//判断是否有网络连接
					if (NetUtil.hasConnectedNetwork(context)) {
						if (isCCPlay) {
							mCcMediaPlay.play(videoId,getNode(),false,null);
							iv_introduce.setVisibility(View.GONE);
							iv_playbtn.setVisibility(View.GONE);
						}else {
							mLeCloudVideoPlay.playNewVideo(videoId,false,null,getNode());
						}
					}else {
						ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
					}
				}
			}
		}
	}
	@SuppressLint("NewApi")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
			//横屏
			setTitleViewVisibility(View.GONE);
			ll_Content.setVisibility(View.GONE);
			setContentViewVisibility(View.GONE);
			
			if (isCCPlay) {
				mCcMediaPlay.changeLayoutParams(true, 0);
			}else {
				mLeCloudVideoPlay.changeLayoutParams(true, 0);
			}
			
		} else if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
			//竖屏
			setTitleViewVisibility(View.VISIBLE);
			ll_Content.setVisibility(View.VISIBLE);
			setContentViewVisibility(View.VISIBLE);
			
			if (isCCPlay) {
				mCcMediaPlay.changeLayoutParams(false, 200);
			}else {
				mLeCloudVideoPlay.changeLayoutParams(false, 200);
			}
		}
	}
	@Override
	protected void onResume() {
		if (isCCPlay) {
			mCcMediaPlay.onResume(ccPosition);
		}else {
//			initVideoLecPlay();
			if (mLeCloudVideoPlay != null) {
				mLeCloudVideoPlay.onResume(videoId,false,null);
			}
		}
		super.onResume();
	}
	@Override
	protected void onPause() {
		if (isCCPlay) {
			ccPosition=mCcMediaPlay.getCcPosition();
			if (mCcMediaPlay != null) {
				mCcMediaPlay.onPause();
			}
		}else {
			if (mLeCloudVideoPlay != null) {
				mLeCloudVideoPlay.onPause();
			}
		}
		
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		removePlayer();
		super.onDestroy();
	}
	/**
	 * 回收播放器
	 */
	private void removePlayer() {
		if (isCCPlay) {
			if (mCcMediaPlay != null) {
				mCcMediaPlay.onDestroy();
				mCcMediaPlay = null;
			}
		}else {
			if (mLeCloudVideoPlay != null) {
				mLeCloudVideoPlay.onDestroy();
				mLeCloudVideoPlay = null;
			}
		}
		layout_video.removeAllViews();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
    			//横屏时,把视频切换成竖屏
        		if (isCCPlay) {
        			mCcMediaPlay.setScreenOrientation(0);
        		}else {
        			mLeCloudVideoPlay.changeOrientation();
        		}
        		return false;
    		} else {
    			return super.onKeyDown(keyCode, event); 
    		}
		}else {
			return super.onKeyDown(keyCode, event); 
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (data != null) {
			if(requestCode==REQUEST_CCPOSITION){ 
				if (resultCode==CCMediaPlayFull.RESULT_CCPOSITION) {
					//mCcMediaPlay.timePlay(data.getExtras().getInt("position"));
					ccPosition=data.getExtras().getInt("position");
				}
			}
		}
	}
	/**
	 * 初始化中间填充父窗体的布局view
	 * @return
	 */
	public abstract View initTwelfthLayout();
	
	/**
	 * 设置是否显示全屏按钮
	 * @param isFullScreen true显示全屏按钮， false不显示全屏按钮
	 */
	private boolean isFullScreen=true;
	public void showFullScreen(boolean isFullScreen){
		this.isFullScreen=isFullScreen;
	}
	}
