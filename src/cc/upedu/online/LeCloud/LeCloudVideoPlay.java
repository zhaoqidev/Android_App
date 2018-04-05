package cc.upedu.online.LeCloud;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lecloud.common.base.util.LogUtils;
import com.lecloud.skin.PlayerStateCallback;
import com.lecloud.skin.vod.VODPlayCenter;

import java.util.HashMap;

import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

public class LeCloudVideoPlay {
	private static Context context;
	private static VODPlayCenter mPlayerView;
	private LinearLayout mLayout;
	private boolean isBackgroud = false;
	private boolean isPlayed = false;
	private ImageView iv_introduce;
	private ImageView iv_playbtn;
	private LinearLayout ll_other;
	/**
	 * 构造
	 */
	private LeCloudVideoPlay() {
        
    }
    /**
     * 得到实例
     * @return
     */
    public static LeCloudVideoPlay getInstance(Context context) {
    	LeCloudVideoPlay.context = context;
        return ManagerHolder.instance;
    }
    /**
     * 初始化实例
     * @author Administrator
     *
     */
    private static class ManagerHolder {
        private static final LeCloudVideoPlay instance = new LeCloudVideoPlay();
    }
    
	/**
	 *  初始化播放器
	 */
	public LinearLayout initPlayer(final ImageView iv_introduce,final ImageView iv_playbtn,final LinearLayout ll_courestime) {
		this.iv_introduce = iv_introduce;
		this.iv_playbtn = iv_playbtn;
		this.ll_other = ll_courestime;
		mPlayerView = new VODPlayCenter(context, true);
		View playerView = mPlayerView.getPlayerView();

		mLayout = new  LinearLayout(context);  
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, 200)));
        mLayout.addView(playerView);

//		ViewGroup.LayoutParams params = ll_introduce.getLayoutParams();
//		params.height = layout_video.getMeasuredHeight();
//		ll_introduce.setLayoutParams(params);
		/**
		 * 视频播放状态的监听
		 */
		LeCloudVideoPlay.mPlayerView.setPlayerStateCallback(new PlayerStateCallback() {

			@Override
			public void onStateChange(int state, Object... extra) {
				switch (state) {
				case PlayerStateCallback.PLAYER_VIDEO_PLAY://视频开始播放事件
					if(getScreenState() == Configuration.ORIENTATION_PORTRAIT) {
						iv_introduce.setVisibility(View.GONE);
						iv_playbtn.setVisibility(View.GONE);
						if (ll_other != null) {
							ll_other.setVisibility(View.GONE);
						}
					}
					isPlayed  = true;
					if (node > 0)
						mPlayerView.seekTo(node);
					node = -1;
					break;
				case PlayerStateCallback.PLAYER_VIDEO_PAUSE://视频暂停播放事件
					if(getScreenState() == Configuration.ORIENTATION_PORTRAIT) {
						iv_playbtn.setVisibility(View.VISIBLE);
					}
					isBackgroud = true;
					isPlayed  = false;
					savePlayNode(false);
					break;
				case PlayerStateCallback.PLAYER_VIDEO_RESUME://视频恢复播放事件
					if(getScreenState() == Configuration.ORIENTATION_PORTRAIT) {
						iv_playbtn.setVisibility(View.GONE);
					}
					isBackgroud = false;
					isPlayed  = true;
					break;
				case PlayerStateCallback.PLAYER_STOP://视频停止播放事件
					if(getScreenState() == Configuration.ORIENTATION_PORTRAIT) {
						iv_introduce.setVisibility(View.VISIBLE);
						iv_playbtn.setVisibility(View.VISIBLE);
						if (ll_other != null) {
							ll_other.setVisibility(View.VISIBLE);
						}
					}
					isPlayed  = false;
					savePlayNode(false);
					break;
				case PlayerStateCallback.PLAYER_SEEK_FINISH://视频拖拽完成事件
					break;
				case PlayerStateCallback.PLAYER_VIDEO_COMPLETE://视频播放结束事件
					if(getScreenState() == Configuration.ORIENTATION_PORTRAIT) {
						iv_introduce.setVisibility(View.VISIBLE);
						iv_playbtn.setVisibility(View.VISIBLE);
						if (ll_other != null) {
							ll_other.setVisibility(View.VISIBLE);
						}
					}
					isPlayed  = false;
					savePlayNode(true);
					break;
				case PlayerStateCallback.PLAYER_ERROR://视频播放出错
					if(getScreenState() == Configuration.ORIENTATION_PORTRAIT) {
						iv_introduce.setVisibility(View.VISIBLE);
						iv_playbtn.setVisibility(View.VISIBLE);
						if (ll_other != null) {
							ll_other.setVisibility(View.VISIBLE);
						}
					}
					isPlayed  = false;
					savePlayNode(false);
					break;
				case PlayerStateCallback.PLAYER_IDLE://
					break;
				}
			}
		});
		return mLayout;
	}

	/*
	记录播放节点
	*/
	private void savePlayNode(boolean isEnd){
		if (UserStateUtil.isLogined()){
			HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
			if (isCourse){
				String kpointId = SharedPreferencesUtil.getInstance().spGetString("oldKpointId");
				if (StringUtil.isEmpty(kpointId))
					kpointId = SharedPreferencesUtil.getInstance().spGetString("kpointId");
				if (isEnd && map.containsKey("kid"+kpointId)) {
					map.remove("kid" + kpointId);
				}
				else{
					map.put("kid"+kpointId,mPlayerView.getCurrentPosition());
				}
				SharedPreferencesUtil.getInstance().editPutString("oldKpointId", SharedPreferencesUtil.getInstance().spGetString("kpointId"));
			}else {
				String zengyanId = SharedPreferencesUtil.getInstance().spGetString("zengyanId");
				if (StringUtil.isEmpty(zengyanId)){
					String answerId = SharedPreferencesUtil.getInstance().spGetString("oldAnswerId");
					if (StringUtil.isEmpty(answerId))
						answerId = SharedPreferencesUtil.getInstance().spGetString("answerId");

					if (StringUtil.isEmpty(answerId)){
						String courseId = SharedPreferencesUtil.getInstance().spGetString("courseId");
						if (isEnd && map.containsKey("cid"+courseId))
							map.remove("cid"+courseId);
						else
							map.put("cid" + courseId, mPlayerView.getCurrentPosition());
					}else {
						if (isEnd && map.containsKey("aid"+answerId))
							map.remove("aid"+answerId);
						else
							map.put("aid"+answerId,mPlayerView.getCurrentPosition());
						SharedPreferencesUtil.getInstance().editPutString("oldAnswerId", SharedPreferencesUtil.getInstance().spGetString("answerId"));
					}
				}else {
					if (isEnd && map.containsKey("zid"+zengyanId))
						map.remove("zid"+zengyanId);
					else
						map.put("zid"+zengyanId,mPlayerView.getCurrentPosition());
				}
			}
			PreferencesObjectUtil.saveObject(map, "videoPlayNode", context);

		}
	}
	private int getScreenState(){
		return context.getResources().getConfiguration().orientation;
	}
	public boolean isCourse = true;
	public void playVideo(String videoId,boolean isCourse,CoursePlaySuccess mCoursePlaySuccess,int node) {
		this.isCourse = isCourse;
		if (node > 0)
			this.node = node;
		if (LeCloudVideoPlay.mPlayerView != null) {
			if (isBackgroud) {
				if (mPlayerView.getCurrentPlayState() == PlayerStateCallback.PLAYER_VIDEO_PAUSE) {
					iv_playbtn.setVisibility(View.GONE);
					LeCloudVideoPlay.mPlayerView.resumeVideo();
				} else {
					playNewVideo(videoId,isCourse,mCoursePlaySuccess,node);
				}
			}else {
				playNewVideo(videoId,isCourse,mCoursePlaySuccess,node);
			}
		}
	}
	int node = -1;
	public void playNewVideo(String videoId,boolean isCourse,CoursePlaySuccess mCoursePlaySuccess,int node) {
		this.isCourse = isCourse;
		if (node > 0)
			this.node = node;
		try {
//			if (mPlayerView.getPlayerView() != null) {
//				mPlayerView.destroyVideo();
//			}
//			initPlayer(iv_introduce, iv_playbtn);
			if (isPlayed) {
				mPlayerView.stopVideo();
			}
			
			mPlayerView.playVideo(ConstantsOnline.UU, videoId, ConstantsOnline.KEY, "", "");
			iv_introduce.setVisibility(View.GONE);
			iv_playbtn.setVisibility(View.GONE);
			if (ll_other != null) {
				ll_other.setVisibility(View.GONE);
			}
			
			if (isCourse && mCoursePlaySuccess != null) {
				mCoursePlaySuccess.onCoursePlaySuccessCallBack();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ShowUtils.showMsg(context, "视频播放异常!");
			iv_introduce.setVisibility(View.VISIBLE);
			iv_playbtn.setVisibility(View.VISIBLE);
			if (ll_other != null) {
				ll_other.setVisibility(View.VISIBLE);
			}
			e.printStackTrace();
		}
	}
	public abstract class CoursePlaySuccess{
		public abstract void onCoursePlaySuccessCallBack();
	}
	/**
	 * 改变视频播放窗口的高度是包裹内容还是具体数值(单位dp)
	 * @param isWrap 视频播放窗口是否是包裹内容
	 * @param height 具体的高度值,单位dp
	 */
	public void changeLayoutParams(boolean isWrap,int height) {
		// TODO Auto-generated method stub
		if (isWrap) {
			mLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}else {
			mLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, height)));
		}
	}
	public void onResume(String videoId,boolean isCourse,CoursePlaySuccess mCoursePlaySuccess) {
		this.isCourse = isCourse;
		if (LeCloudVideoPlay.mPlayerView != null) {
			//判断在设置中视频是否自动播放
			if (SharedPreferencesUtil.getInstance().spGetBoolean("isAutoPlay")) {
				//判断在设置中是否允许在非wifi下播放视频
				if (SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay", true)) {
					//判断WiFi是否连接
					if (NetUtil.isWifiConnected(context)) {
//						if (mPlayerView.getCurrentPlayState() == PlayerStateCallback.PLAYER_VIDEO_PAUSE) {
//							//判断在跳转时播放的状态,判断在设置中视频是否自动播放
//							iv_playbtn.setVisibility(View.GONE);
//							this.mPlayerView.resumeVideo();
//						} else {
//							ShowUtils.showMsg(context, "已回收，重新请求播放");
//						}
						playNewVideo(videoId,isCourse,mCoursePlaySuccess,node);
					}else {
						ShowUtils.showMsg(context, "您的网络状态非WiFi,请检查您的网络!");
					}
				}else {
					//判断是否有网络连接
					if (NetUtil.hasConnectedNetwork(context)) {
//						if (mPlayerView.getCurrentPlayState() == PlayerStateCallback.PLAYER_VIDEO_PAUSE) {
//							//判断在跳转时播放的状态,判断在设置中视频是否自动播放
//							iv_playbtn.setVisibility(View.GONE);
//							this.mPlayerView.resumeVideo();
//						} else {
//							ShowUtils.showMsg(context, "已回收，重新请求播放");
//						}
						playNewVideo(videoId,isCourse,mCoursePlaySuccess,node);
					}else {
						ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
					}
				}
			}
		}
	}
	public void onPause() {
		if (mPlayerView != null) {
			if (isPlayed) {
				mPlayerView.pauseVideo();
				iv_playbtn.setVisibility(View.VISIBLE);
				isBackgroud = true;
			}
		}
	}
	public void onDestroy() {
		if (mPlayerView != null) {
			mPlayerView.destroyVideo();
			mPlayerView = null;
		}
		isBackgroud = false;
		LogUtils.clearLog();
	}
	/**
	 * 把视频播放切换成竖屏
	 */
	public void changeOrientation() {
		if (mPlayerView != null) {
			mPlayerView.changeOrientation(Configuration.ORIENTATION_PORTRAIT);
		}
	}
	/**
	 * 把视频播放切换成横屏
	 */
	public void changeOrientationLandscape() {
		if (mPlayerView != null) {
			mPlayerView.changeOrientation(Configuration.ORIENTATION_LANDSCAPE);
			iv_introduce.setVisibility(View.GONE);
		}
	}
}
