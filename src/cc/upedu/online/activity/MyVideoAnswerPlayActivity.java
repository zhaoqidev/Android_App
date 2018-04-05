package cc.upedu.online.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import cc.upedu.online.CCMediaPlay.CCMediaPlayFull;
import cc.upedu.online.CCMediaPlay.CCMediaPlayView;
import cc.upedu.online.LeCloud.LeCloudVideoPlay;
import cc.upedu.online.R;
import cc.upedu.online.base.BaseActivity;
import cc.upedu.online.domin.VideoAnswerListBean.Entity.VideoAnswerItem;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 我的视频答疑中视频播放界面
 * @author Administrator
 *
 */
public class MyVideoAnswerPlayActivity extends BaseActivity {
	private LinearLayout ll_title;
	private LinearLayout layout_video;
	private ImageView iv_back,iv_introduce,iv_playbtn;
	private TextView tv_answertitle,tv_creattime,tv_answercontent;	
	private String videoId;
//	private String ccVideoId="7D315A2D76FBE2EF9C33DC5901307461";//获取到真实id后可以使用videoId赋值，删除此变量
	private int ccPosition=0;
	private static final int REQUEST_CCPOSITION = 2;
	private LeCloudVideoPlay mLeCloudVideoPlay;
	private CCMediaPlayView mCcMediaPlay;
	private boolean isCCPlay;
	private VideoAnswerItem mVideoAnswerItem;
	
	@Override
	protected void initView() {
		//TODO ADD new view for course title or course live time
		setContentView(R.layout.activity_myvideoanswer_play);
		mVideoAnswerItem = (VideoAnswerItem)getIntent().getSerializableExtra("videoAnswerItem");
		
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		layout_video = (LinearLayout) findViewById(R.id.layout_video);
		iv_introduce = (ImageView) findViewById(R.id.iv_introduce);
		iv_playbtn = (ImageView) findViewById(R.id.iv_playbtn);
		tv_answertitle = (TextView) findViewById(R.id.tv_answertitle);
		tv_creattime = (TextView) findViewById(R.id.tv_creattime);
		tv_answercontent = (TextView) findViewById(R.id.tv_answercontent);
		
		if (mVideoAnswerItem != null) {
			videoId = mVideoAnswerItem.getVideourl();
			if ("LETV".equals(mVideoAnswerItem.getVideoType())) {
				isCCPlay = false;
			}else if ("CC".equals(mVideoAnswerItem.getVideoType())) {
				isCCPlay = true;
			}
//			isCCPlay = true;
//			isCCPlay = sp.getBoolean("isCCPlay", false);
			if (isCCPlay) {
				initVideoCCPlay();
			}else {
				initVideoLecPlay();
			}
		}else {
			ShowUtils.showMsg(context, "数据有误,请反馈信息,谢谢!");
			finish();
		}
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
		}
	}
	@Override
	protected void initData() {
		setData2View();
		if (!StringUtil.isEmpty(videoId)) {
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

	private int getNode(){
		int node = -1;
		if (UserStateUtil.isLogined()){
			String answerId = videoId;
			SharedPreferencesUtil.getInstance().editPutString("kpointId", answerId);
			HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
			if (map.containsKey("aid" + answerId)){
				node = map.get("aid"+answerId);
			}
		}
		return node;
	}
	
	private void setData2View() {
		if (!StringUtil.isEmpty(mVideoAnswerItem.getTitle())) {
			tv_answertitle.setText(mVideoAnswerItem.getTitle());
		}
		if (!StringUtil.isEmpty(mVideoAnswerItem.getCreateTime())) {
			tv_creattime.setText(mVideoAnswerItem.getCreateTime());
		}
		if (!StringUtil.isEmpty(mVideoAnswerItem.getContent())) {
			tv_answercontent.setText(mVideoAnswerItem.getContent());
		}
		
//		if (!StringUtil.isEmpty(mVideoAnswerItem.get)) {
//			Bitmap mbitmap = BitmapUtils.loadImageDefault(context, context.getCacheDir(), ConstantsOnline.SERVER_IMAGEURL+mCourseIntroduceBean.getEntity().getCourse().getCourseLogo(), new ImageCallback() {
//				@Override
//				public void loadImage(Bitmap bitmap, String imagePath) {
//					iv_introduce.setImageBitmap(bitmap);
//				}
//			}, true);
//			if (mbitmap!=null) {
//				iv_introduce.setImageBitmap(mbitmap);
//			}else {
//				iv_introduce.setImageResource(R.drawable.rollview_default);
//			}
//		}else {
//			iv_introduce.setImageResource(R.drawable.rollview_default);
//		}
	};
	@Override
	protected void initListener() {
		iv_back.setOnClickListener(this);
//		ibtn_right.setOnClickListener(this);
//		ibtn_right2.setOnClickListener(this);
		iv_playbtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_playbtn: // 播放的点击事件
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
					}else {
						ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
					}
				}
			}
			break;
		}
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(getScreenState() ==Configuration.ORIENTATION_LANDSCAPE) {
			//横屏
			ll_title.setVisibility(View.GONE);
			if (isCCPlay) {
				mCcMediaPlay.changeLayoutParams(true, 0);
			}else {
				mLeCloudVideoPlay.changeLayoutParams(true, 0);
			}
		} else if(getScreenState() ==Configuration.ORIENTATION_PORTRAIT) {
			//竖屏
			ll_title.setVisibility(View.VISIBLE);
			if (isCCPlay) {
				mCcMediaPlay.changeLayoutParams(false, 200);
			}else {
				mLeCloudVideoPlay.changeLayoutParams(false, 200);
			}
		}
		super.onConfigurationChanged(newConfig);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==REQUEST_CCPOSITION){ 
			if (resultCode==CCMediaPlayFull.RESULT_CCPOSITION) {
				//mCcMediaPlay.timePlay(data.getExtras().getInt("position"));
				ccPosition=data.getExtras().getInt("position");
				}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (isCCPlay) {
			mCcMediaPlay.onResume(ccPosition);
		}else {
//			initVideoLecPlay();
			if (mLeCloudVideoPlay != null) {
				mLeCloudVideoPlay.onResume(videoId,false,null);
			}
		}
	}
	
	@Override
	protected void onPause() {
		if (isCCPlay) {
			ccPosition=mCcMediaPlay.getCcPosition();
			mCcMediaPlay.onPause();
		}else {
			mLeCloudVideoPlay.onPause();
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
	public boolean onTouchEvent(MotionEvent event) {
		System.out.println("*************************************");
		if (isCCPlay && mCcMediaPlay!=null) {
			mCcMediaPlay.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if(getScreenState() ==Configuration.ORIENTATION_LANDSCAPE) {
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
	private int getScreenState(){
		return this.getResources().getConfiguration().orientation;
	}
}
