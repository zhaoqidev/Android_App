package cc.upedu.online.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import cc.upedu.online.CCMediaPlay.CCMediaPlayFull;
import cc.upedu.online.CCMediaPlay.CCMediaPlayView;
import cc.upedu.online.LeCloud.LeCloudVideoPlay;
import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.adapter.ViewPagerAdapter;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 带toolbar,视频和tabs的activity
 * 
 * @author Administrator
 *
 */
public abstract class VideoToolbarTabsBaseActivity extends TitleBaseActivity implements
		android.support.design.widget.AppBarLayout.OnOffsetChangedListener {
//		,NestedScrollingChild{
	private static final int REQUEST_CCPOSITION = 2;
	private ImageView iv_introduce;
	private ImageView iv_playbtn;
	// 课程简介视频的布局
	private LinearLayout layout_video,ll_videobottom,ll_bottom,ll_fragment_bottom;
	private AppBarLayout appBarLayout;
//	private CoordinatorLayout coordinatorLayout;
	private CollapsingToolbarLayout collapsingToolbar;//导师名片向上滚动的布局
	private Toolbar toolbar;//标题栏
	private TextView toolbar_title;//标题栏课程名
	private LeCloudVideoPlay mLeCloudVideoPlay;
	public CCMediaPlayView mCcMediaPlay;
	public boolean isCCPlay;
	private int ccPosition;//cc视频播放位置
	private String videoId;//点击后，将要播放视频的ID。
	private String oldVideoId="";//正在播放的视频id
	protected TabLayout tabs;//指针控件
	protected ViewPager viewPager;
	private boolean isCourse = false;
	private int playNode = 0;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_video_toolbar);
		getTitleView();
		iv_introduce = (ImageView) findViewById(R.id.iv_introduce);
		iv_playbtn = (ImageView) findViewById(R.id.iv_playbtn);
		layout_video = (LinearLayout) findViewById(R.id.layout_video);
		ll_videobottom = (LinearLayout) findViewById(R.id.ll_videobottom);
		View videoBottom = initVideoBottom();
		if (videoBottom != null) {
			ll_videobottom.setVisibility(View.VISIBLE);
			ll_videobottom.addView(videoBottom);
		}else {
			ll_videobottom.setVisibility(View.GONE);
		}
		
		ll_bottom = (LinearLayout) findViewById(R.id.ll_bottom);
		View bottomView = initBottomView();
		if (bottomView != null) {
			ll_bottom.setVisibility(View.VISIBLE);
			ll_bottom.addView(bottomView);
		}else {
			ll_bottom.setVisibility(View.GONE);
		}
		ll_fragment_bottom = (LinearLayout) findViewById(R.id.ll_fragment_bottom);
		View fragmentBottomView = initFragmentBottomView();
		if (fragmentBottomView != null) {
			ll_fragment_bottom.setVisibility(View.VISIBLE);
			ll_fragment_bottom.addView(fragmentBottomView);
		}else {
			ll_fragment_bottom.setVisibility(View.GONE);
		}
		
		appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
		tabs = (TabLayout) findViewById(R.id.tabs);
//		coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
		collapsingToolbar.setTitleEnabled(false);//控制是否是漂浮文字
		toolbar = (Toolbar) this.findViewById(R.id.tool_bar);
		toolbar_title=(TextView) findViewById(R.id.toolbar_title);
	    setToolbar();
	}
	
	/**
	 * 得到一个可以kdy为string,value为fragment的map集合,用于关联控件tablayout和viewpager
	 * @return key为tabs中的一项的文本,value为与之相对应的fragment
	 */
	public abstract LinkedHashMap<String, BaseFragment> setupViewPager();
	
	public void initTabsViewPager() {
		LinkedHashMap<String, BaseFragment> map = setupViewPager();
		if (map != null) {
			int size = map.size();
			if (size > 0) {
				ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
				
				Iterator<String> it = map.keySet().iterator();
				while (it.hasNext()) {
					//it.next()得到的是key，tm.get(key)得到obj
					String key = (String) it.next();
					adapter.addFrag(map.get(key), key);
				}
				
				viewPager.setAdapter(adapter);
				tabs.setupWithViewPager(viewPager);
			}
		}
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		return null;
	}
	private void setToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//      actionBar.setHomeAsUpIndicator(android.R.drawable.ic_input_delete);
//      actionBar.setTitle(teacherName);
//        toolbar_title.setText(courseName);
        actionBar.setDisplayHomeAsUpEnabled(false);//是否显示返回箭头

        toolbar.setClickable(false);
        toolbar.setFocusable(false);
	}
	public void SetToolbarText(String toolbarText){
		toolbar_title.setText(toolbarText);
	}
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
		if (R.id.iv_playbtn == v.getId()) {
			// 播放的点击事件
			if (mLeCloudVideoPlay == null && mCcMediaPlay == null) {
				ShowUtils.showMsg(context, "数据加载中,请稍等片刻!");
			}else {
				//判断在设置中是否允许在非wifi下播放视频
				videoId = SharedPreferencesUtil.getInstance().spGetString("currentStudyUrl");
				int node = -1;
				if (UserStateUtil.isLogined()){
					HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
					if (isCourse){
						String kpointId = SharedPreferencesUtil.getInstance().spGetString("kpointId");
						if (map.containsKey("cid"+kpointId)){
							node = map.get("cid"+kpointId);
						}
					}else {
						String answerId = SharedPreferencesUtil.getInstance().spGetString("answerId");
						if (StringUtil.isEmpty(answerId)){
							String courseId = SharedPreferencesUtil.getInstance().spGetString("courseId");
							if (map.containsKey("cid"+courseId)){
								node = map.get("cid"+courseId);
							}
						}else {
							if (map.containsKey("aid"+answerId)){
								node = map.get("aid"+answerId);
							}
						}
					}
				}
				playCourseVideo(videoId, isCourse,node);
			}
		}
	}
	public View initVideoBottom(){
		return null;
	};
	/**
	 * 初始化界面主体底部不随界面滚动的布局
	 * 默认为隐藏
	 * @return
	 */
	public abstract View initBottomView();
	/**
	 * 初始化界面上方且在底部不随界面滚动的布局
	 * 默认为隐藏
	 * @return
	 */
	public abstract View initFragmentBottomView();
	/**
	 * 设置界面上方且在底部不随界面滚动的布局的显示和隐藏
	 * @param visibility
	 */
	public void setFragmentBottomVisibility(int visibility){
		ll_fragment_bottom.setVisibility(visibility);
	}
	/**
	 * 初始化乐视的视频播放器
	 */
	private void initVideoLecPlay() {
		if (mLeCloudVideoPlay == null) {
			mLeCloudVideoPlay = LeCloudVideoPlay.getInstance(context);
			this.layout_video.addView(mLeCloudVideoPlay.initPlayer(iv_introduce, iv_playbtn, null));
		}
	}
	/**
	 * 初始化CC的视频播放器
	 */
	private void initVideoCCPlay() {
		if (mCcMediaPlay == null) {
			mCcMediaPlay = new CCMediaPlayView(context);
//			mCcMediaPlay.setCoordinatorLayout(coordinatorLayout,collapsingToolbar);
			this.layout_video.addView(mCcMediaPlay.initView());
		}
	}
	
	public void initVideoData(String courseName,String courseLogo,boolean isCourse){
		this.isCourse = isCourse;
		OnlineApp.myApp.imageLoader.displayImage(
      		  ConstantsOnline.SERVER_IMAGEURL+courseLogo, 
      		  iv_introduce, 
      		  OnlineApp.myApp.builder
      			.showImageOnLoading(R.drawable.rollview_default)  
      	        .showImageOnFail(R.drawable.rollview_default)
      	        .build());
		setTitleText(courseName);
		SetToolbarText(courseName);
	}
	public void autoPlay(){
		if (mLeCloudVideoPlay == null && mCcMediaPlay == null) {
			initPlayer(SharedPreferencesUtil.getInstance().spGetString("videoType"));
		}
		if (isVideoType()) {
			videoId = SharedPreferencesUtil.getInstance().spGetString("currentStudyUrl");
			//判断在设置中视频是否自动播放
			if (SharedPreferencesUtil.getInstance().spGetBoolean("isAutoPlay")) {
				//办法记录功能
				int node = -1;
				if (UserStateUtil.isLogined()){
					HashMap<String,Integer> map = (HashMap<String, Integer>) PreferencesObjectUtil.readObject("videoPlayNode", context);
					if (isCourse){
						String kpointId = SharedPreferencesUtil.getInstance().spGetString("kpointId");
						if (map.containsKey("cid"+kpointId)){
							node = map.get("cid"+kpointId);
						}
					}else {
						String answerId = SharedPreferencesUtil.getInstance().spGetString("answerId");
						if (StringUtil.isEmpty(answerId)){
							String courseId = SharedPreferencesUtil.getInstance().spGetString("courseId");
							if (map.containsKey("cid"+courseId)){
								node = map.get("cid"+courseId);
							}
						}else {
							if (map.containsKey("aid"+answerId)){
								node = map.get("aid"+answerId);
							}
						}
					}
				}
				//判断在设置中是否允许在非wifi下播放视频
				if (!SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay", true)) {
					//判断WiFi是否连接
					if (NetUtil.isWifiConnected(context)) {
						if (isCCPlay) {
							playNewCCVideo(videoId, isCourse,node);
						}else {
							playNewCourseVideo(videoId,isCourse,node);
						}
					}else {
						ShowUtils.showMsg(context, "您的网络状态非WiFi,请检查您的网络!");
					}
				}else {
					//判断是否有网络连接
					if (NetUtil.hasConnectedNetwork(context)) {
						if (isCCPlay) {
							playNewCCVideo(videoId, isCourse,node);
						}else {
							playNewCourseVideo(videoId,isCourse,node);
						}
					}else {
						ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
					}
				}
			}
		}else {
			ShowUtils.showMsg(context, "视频类型错误，请反馈客服，谢谢！");
		}
	}
	public void initPlayer(String videoType){
		if ("LETV".equals(videoType)) {
			isCCPlay = false;
		}else if ("CC".equals(videoType)) {
			isCCPlay = true;
		}
		if (isCCPlay) {
			initVideoCCPlay();
		}else {
			initVideoLecPlay();
		}
	}
	public void playCourseVideo(String videoId,boolean isCourse,int node) {
		if (isVideoType()) {
			if (StringUtil.isEmpty(videoId)) {
				ShowUtils.showMsg(context, "请求数据失败或没有选择章节!");
			}else {
				if (!SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay", true)) {
					//判断WiFi是否连接
					if (NetUtil.isWifiConnected(context)) {
						if (isCCPlay) {
							playNewCCVideo(videoId, isCourse,node);
						}else {
							playNewCourseVideo(videoId,isCourse,node);
						}
					}else {
						ShowUtils.showMsg(context, "您的网络状态非WiFi,请检查您的网络!");
					}
				}else {
					//判断是否有网络连接
					if (NetUtil.hasConnectedNetwork(context)) {
						if (isCCPlay) {
							playNewCCVideo(videoId, isCourse,node);
						}else {
							playNewCourseVideo(videoId,isCourse,node);
						}
					}else {
						ShowUtils.showMsg(context, "网络不可用,请先检查您的网络!");
					}
				}
			}
		}else {
			ShowUtils.showMsg(context, "视频类型错误，请反馈客服，谢谢！");
		}
	}
	private boolean isVideoType() {
		String videoType = SharedPreferencesUtil.getInstance().spGetString("videoType");
		boolean isPlay = true;
		if (StringUtil.isEmpty(videoType)) {
			isPlay = true;
		}else {
			if (isCCPlay == "CC".equals(videoType)) {
				isPlay = true;
			}else {
				isPlay = false;
			}
		}
		return isPlay;
	}
	/**
	 * 乐视视频播放新的视频
	 */
	public void playNewCourseVideo(String videoId,final boolean isCourse,int node) {
		this.isCourse = isCourse;
		if (mLeCloudVideoPlay != null && !StringUtil.isEmpty(videoId)) {
			mLeCloudVideoPlay.playVideo(videoId, isCourse, mLeCloudVideoPlay.new CoursePlaySuccess() {

				@Override
				public void onCoursePlaySuccessCallBack() {
					// TODO Auto-generated method stub
					if (isCourse) {
						onMyCoursePlaySuccessCallBack();
					}
				}
			}, node);
		}
	}
	public abstract void onMyCoursePlaySuccessCallBack();
	/**
	 * CC视频播放
	 * @param videoId 课程视频ID
	 * @param isCourse 是否是课程
	 */
	public void playNewCCVideo(String videoId,boolean isCourse,int node){
		this.isCourse = isCourse;
		if (oldVideoId.equals(videoId)) {
			ShowUtils.showMsg(context, "该课程正在播放");
		}else {
			if (mCcMediaPlay.nodePosition>0){
				mCcMediaPlay.videoPause();
			}

			mCcMediaPlay.CcReset();
			String srtUrl = SharedPreferencesUtil.getInstance().spGetString("srtUrl");
//			String srtUrl = "http://dev.bokecc.com/static/font/example.utf8.srt";
//			String srtUrl = "";
			mCcMediaPlay.play(videoId,node,isCourse,srtUrl);
			SharedPreferencesUtil.getInstance().editPutString("srtUrl","");
			
			iv_introduce.setVisibility(View.GONE);
			iv_playbtn.setVisibility(View.GONE);
			if (!StringUtil.isEmpty(oldVideoId)) {
				ShowUtils.showMsg(context, "视频切换中，请稍后");
			}
			oldVideoId=videoId;
		}
		if (isCourse) {
			onMyCoursePlaySuccessCallBack();
		}
	}
	@SuppressLint("NewApi")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
			//横屏
			setTitleViewVisibility(View.GONE);
			ll_bottom.setVisibility(View.GONE);
			setContentViewVisibility(View.GONE);
//			ll_indicator.setVisibility(View.GONE);
//			coordinatorLayout.setNestedScrollingEnabled(false);
			
			if (isCCPlay) {
				mCcMediaPlay.changeLayoutParams(true, 0);
			}else {
				mLeCloudVideoPlay.changeLayoutParams(true, 0);
			}
			AppBarLayout.LayoutParams lp =(android.support.design.widget.AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
			lp.setScrollFlags(0);
			
		} else if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
			//竖屏
			setTitleViewVisibility(View.VISIBLE);
			ll_bottom.setVisibility(View.VISIBLE);
			setContentViewVisibility(View.VISIBLE);
//			ll_indicator.setVisibility(View.VISIBLE);
			if (isCCPlay) {
				mCcMediaPlay.changeLayoutParams(false, 200);
			}else {
				mLeCloudVideoPlay.changeLayoutParams(false, 200);
			}
			AppBarLayout.LayoutParams lp =(android.support.design.widget.AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
			lp.setScrollFlags(3);
		}
	}
	@Override
	protected void onResume() {
		appBarLayout.addOnOffsetChangedListener(this);//页面显示，监听AppBarLayout是否收起
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
		appBarLayout.removeOnOffsetChangedListener(this);//页面消失，不再监听AppBarLayout是否收起
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
		SharedPreferencesUtil.getInstance().editPutString("studySectionPosition", "");
		SharedPreferencesUtil.getInstance().editPutString("kpointId", "");
		SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", "");
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
	//监听AppBarLayout是收起状态还是展开状态
	@Override
	public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
		if (i == 0) {
			// 展开状态，可刷新
//			if (introduceInspirationFragment != null ) {
//				introduceInspirationFragment.setPullRefreshEnable(true);
//			}
			if (toolbar!=null) {
				toolbar.setVisibility(View.GONE);
			}

		} else {
			// 收起状态，不可刷新
//			if (introduceInspirationFragment != null ) {
//				introduceInspirationFragment.setPullRefreshEnable(false);
//			}
			if (toolbar!=null) {
				toolbar.setVisibility(View.VISIBLE);
			}
		}
		setAppBarLayoutChanged(i);
	}
	/**
	 * 监听AppBarLayout是收起状态还是展开状态时的操作
	 * 可用于设置是否可以刷新
	 * @param i 0表示展开状态,1表示不可收起状态
	 */
	public abstract void setAppBarLayoutChanged(int i);
	
//	private NestedScrollingChildHelper mChildHelper;
		
//	@Override
//    public void setNestedScrollingEnabled(boolean enabled) {
//        mChildHelper.setNestedScrollingEnabled(enabled);
//    }
// 
//    @Override
//    public boolean isNestedScrollingEnabled() {
//        return mChildHelper.isNestedScrollingEnabled();
//    }
// 
//    @Override
//    public boolean startNestedScroll(int axes) {
//        return mChildHelper.startNestedScroll(axes);
//    }
// 
//    @Override
//    public void stopNestedScroll() {
//        mChildHelper.stopNestedScroll();
//    }
// 
//    @Override
//    public boolean hasNestedScrollingParent() {
//        return mChildHelper.hasNestedScrollingParent();
//    }
// 
//    @Override
//    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
//                                        int dyUnconsumed, int[] offsetInWindow) {
//        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
//                offsetInWindow);
//    }
// 
//    @Override
//    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
//        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
//    }
// 
//    @Override
//    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
//        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
//    }
// 
//    @Override
//    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
//        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
//    }
}
