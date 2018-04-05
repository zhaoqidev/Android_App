package cc.upedu.online.CCMediaPlay;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bokecc.sdk.mobile.exception.ErrorCode;
import com.bokecc.sdk.mobile.play.DWMediaPlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cc.upedu.online.CCMediaPlay.PopMenu.OnItemClickListener;
import cc.upedu.online.R;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ScreenUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.VerticalSeekBar;


/**
 * 视频播放小窗口界面
 * 
 * @author CC视频
 * 
 */
public class CCMediaPlayView implements
		DWMediaPlayer.OnBufferingUpdateListener,
		DWMediaPlayer.OnInfoListener,
		DWMediaPlayer.OnPreparedListener, DWMediaPlayer.OnErrorListener,
		MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback {
//	CoordinatorLayout coordinatorLayout;
//	String videoId = "7D315A2D76FBE2EF9C33DC5901307461";
	private Rect mChangeImageBackgroundRect = null;
	Context context;
	String videoId;
//	private 	CollapsingToolbarLayout collapsingToolbar;
	public CCMediaPlayView(Context context){
		this.context=context;
		
		player = new DWMediaPlayer();
		player.reset();
	}
	
	int screenOrientation;// 0，竖屏，1，横屏
	private int screenWidth;// 屏幕宽高
	
	private boolean networkConnected = true;
	private DWMediaPlayer player;
//	private Subtitle subtitle;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private ProgressBar bufferProgressBar;
	private SeekBar skbProgress;
	private ImageView playOp, backPlayList;
	private TextView videoIdText, playDuration, videoDuration;
	private Button screenSizeBtn, definitionBtn, subtitleBtn;
	private PopMenu screenSizeMenu, definitionMenu, subtitleMenu;
//	private LinearLayout playerTopLayout; //volumeLayout
	private LinearLayout volumeLayout;
	private RelativeLayout playerBottomLayout;
	private AudioManager audioManager;
	private VerticalSeekBar volumeSeekBar;
	private int currentVolume;
	private int maxVolume;
//	private TextView subtitleText;
	
	private RelativeLayout playerSurfaceViewRL;

//	private boolean isLocalPlay;
	private boolean isPrepared;
	private Map<String, Integer> definitionMap;

	private Handler playerHandler;
	private Timer timer = new Timer();
	private TimerTask timerTask, networkInfoTimerTask;

	private int currentScreenSizeFlag = 1;
	private int currentDefinition = 0;
	
	private boolean firstInitDefinition = true;
	
	String path;
	
	public static Boolean isPlaying;
	// 当player未准备好，并且当前activity经过onPause()生命周期时，此值为true
	private boolean isFreeze = false;
	private boolean isSurfaceDestroy = false;

	int currentPosition;
	private Dialog dialog;
	
//	int position;//从全屏恢复到小屏播放后的播放位置

	private String[] definitionArray;
	private final String[] screenSizeArray = new String[] { "满屏", "100%",
			"75%", "50%" };
//	private final String[] subtitleSwitchArray = new String[] { "开启", "关闭" };
//	private final String subtitleExampleURL = "http://dev.bokecc.com/static/font/example.utf8.srt";

	private GestureDetector detector;
	private float scrollTotalDistance, scrollCurrentPosition;
	
	public static final int REQUEST_CCPOSITION = 2;

	public int node;//上一次播放节点

	public void play(String videoId,int node,boolean isCouser,String subtitleExampleURL) {

		this.videoId=videoId;
		this.isCourse=isCouser;
		this.node=node;
		
		initPlayHander();

		initPlayInfo(subtitleExampleURL);

		initScreenSizeMenu();

		getSurfaceViewRect();

	}


	private TextView subtitleText;
	private Subtitle subtitle;
	public View initView() {
		detector = new GestureDetector(context,new MyGesture());
		View view = View.inflate(context, R.layout.media_play,null);
		
		//整个视频播放的布局
		playerSurfaceViewRL = (RelativeLayout) view.findViewById(R.id.playerSurfaceViewRL);
		
		surfaceView = (SurfaceView) view.findViewById(R.id.playerSurfaceView);
		playOp = (ImageView) view.findViewById(R.id.btnPlay);
		backPlayList = (ImageView) view.findViewById(R.id.backPlayList);
		bufferProgressBar = (ProgressBar) view.findViewById(R.id.bufferProgressBar);

		videoIdText = (TextView) view.findViewById(R.id.videoIdText);
		playDuration = (TextView) view.findViewById(R.id.playDuration);
		videoDuration = (TextView) view.findViewById(R.id.videoDuration);
		playDuration.setText(ParamsUtil.millsecondsToStr(0));
		videoDuration.setText(ParamsUtil.millsecondsToStr(0));

		screenSizeBtn = (Button) view.findViewById(R.id.playScreenSizeBtn);
		definitionBtn = (Button) view.findViewById(R.id.definitionBtn);
		subtitleBtn = (Button) view.findViewById(R.id.subtitleBtn);

		audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

		volumeSeekBar = (VerticalSeekBar) view.findViewById(R.id.volumeSeekBar);
		volumeSeekBar.setThumbOffset(2);

		volumeSeekBar.setMax(maxVolume);
		volumeSeekBar.setProgress(currentVolume);
		volumeSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);

		skbProgress = (SeekBar) view.findViewById(R.id.skbProgress);
		skbProgress.setOnSeekBarChangeListener(onSeekBarChangeListener);

//		playerTopLayout = (LinearLayout) view.findViewById(R.id.playerTopLayout);
		volumeLayout = (LinearLayout) view.findViewById(R.id.volumeLayout);
		playerBottomLayout = (RelativeLayout) view.findViewById(R.id.playerBottomLayout);

		playOp.setOnClickListener(onClickListener);
		backPlayList.setOnClickListener(onClickListener);
		screenSizeBtn.setOnClickListener(onClickListener);
		definitionBtn.setOnClickListener(onClickListener);
		subtitleBtn.setOnClickListener(onClickListener);

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //2.3及以下使用，不然出现只有声音没有图像的问题
		surfaceHolder.addCallback(this);

//		subtitleText = (TextView) view.findViewById(R.id.subtitleText);
		
		surfaceView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isDisplay) {
					setLayoutVisibility(View.GONE, false);
				}else {
					setLayoutVisibility(View.VISIBLE, true);
				}
			}
		});
		
		mLayout = new  LinearLayout(context);
        mLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, 200)));
        mLayout.setBackgroundColor(context.getResources().getColor(R.color.black));
        mLayout.addView(view);

		subtitleText = (TextView) view.findViewById(R.id.subtitleText);

//      getSurfaceViewRect();
		return mLayout;
	}
	private void getSurfaceViewRect() {
		setLayoutVisibility(View.VISIBLE, true);
		if (null == mChangeImageBackgroundRect) {
            mChangeImageBackgroundRect = new Rect();
        }
		mLayout.getDrawingRect(mChangeImageBackgroundRect);
        int[] location = new int[2];
        mLayout.getLocationOnScreen(location);


        mChangeImageBackgroundRect.left = location[0];
        mChangeImageBackgroundRect.top = location[1];
        mChangeImageBackgroundRect.right = mChangeImageBackgroundRect.right + location[0];
        mChangeImageBackgroundRect.bottom = mChangeImageBackgroundRect.bottom + location[1];
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
	private void initPlayHander() {
		playerHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (player == null) {
					return;
				}
				try{
					// 刷新字幕
					subtitleText.setText(subtitle.getSubtitleByTime(player.getCurrentPosition()));

				}catch (Exception e){
					Log.d("ccmediaplayer","subtitle Error");
				}

				// 更新播放进度
				int position = player.getCurrentPosition();
				int duration = player.getDuration();

				if (duration > 0) {
					long pos = skbProgress.getMax() * position / duration;
					playDuration.setText(ParamsUtil.millsecondsToStr(player.getCurrentPosition()));
					skbProgress.setProgress((int) pos);

					if (0<=position&&position<duration){
						nodePosition=position;
					}

				}
			}
		};

		// 通过定时器和Handler来更新进度
		timerTask = new TimerTask() {
			@Override
			public void run() {

				if (!isPrepared) {
					return;
				}

				playerHandler.sendEmptyMessage(0);
			}
		};

	}

	private void initPlayInfo(String subtitleExampleURL) {
		timer.schedule(timerTask, 0, 1000);
		isPrepared = false;

		player.setOnErrorListener(this);
		player.setOnVideoSizeChangedListener(this);
		player.setOnInfoListener(this);

//		String videoId = getIntent().getStringExtra("videoId");

		videoIdText.setText(videoId);
		try {

				player.setVideoPlayInfo(videoId, ConstantsOnline.CC_USERID,
						ConstantsOnline.CC_API_KEY, context);
				player.setDefaultDefinition(DWMediaPlayer.NORMAL_DEFINITION);

			player.prepareAsync();

		} catch (IllegalArgumentException e) {
			Log.e("player error", e.getMessage());
		} catch (SecurityException e) {
			Log.e("player error", e.getMessage());
		} catch (IllegalStateException e) {
			Log.e("player error", e + "");
		}

		// 设置视频字幕
		if (!StringUtil.isEmpty(subtitleExampleURL)){
			subtitle = new Subtitle(new Subtitle.OnSubtitleInitedListener() {
				@Override
				public void onInited(Subtitle subtitle) {
					// 初始化字幕控制菜单
//				initSubtitleSwitchpMenu(subtitle);
				}

			});
			subtitle.initSubtitleResource(subtitleExampleURL);
		}else
			subtitleText.setVisibility(View.GONE);

	}

	private void initScreenSizeMenu() {
		screenSizeMenu = new PopMenu(context, R.drawable.popdown,
				currentScreenSizeFlag);
		screenSizeMenu.addItems(screenSizeArray);
		screenSizeMenu.setOnItemClickListener(new PopMenu.OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				// 提示已选择的屏幕尺寸
				Toast.makeText(context.getApplicationContext(),
						screenSizeArray[position], Toast.LENGTH_SHORT).show();

				LayoutParams params = getScreenSizeParams(position);
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				surfaceView.setLayoutParams(params);

			}
		});

	}

	private LayoutParams getScreenSizeParams(int position) {
		currentScreenSizeFlag = position;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();

		int vWidth = player.getVideoWidth();
		if (vWidth == 0) {
			vWidth = 600;
		}

		int vHeight = player.getVideoHeight();
		if (vHeight == 0) {
			vHeight = 400;
		}

		if (vWidth > width || vHeight > height) {
			float wRatio = (float) vWidth / (float) width;
			float hRatio = (float) vHeight / (float) height;
			float ratio = Math.max(wRatio, hRatio);

			width = (int) Math.ceil((float) vWidth / ratio);
			height = (int) Math.ceil((float) vHeight / ratio);
		} else {
			float wRatio = (float) width / (float) vWidth;
			float hRatio = (float) height / (float) vHeight;
			float ratio = Math.min(wRatio, hRatio);

			width = (int) Math.ceil((float) vWidth * ratio);
			height = (int) Math.ceil((float) vHeight * ratio);
		}

		String screenSizeStr = screenSizeArray[position];
		if (screenSizeStr.indexOf("%") > 0) {// 按比例缩放
			int screenSize = ParamsUtil.getInt(screenSizeStr.substring(0,
					screenSizeStr.indexOf("%")));
			width = (width * screenSize) / 100;
			height = (height * screenSize) / 100;

		} else { // 拉伸至全屏
			width = wm.getDefaultDisplay().getWidth();
			height = wm.getDefaultDisplay().getHeight();
		}

		LayoutParams params = new LayoutParams(width, height);
		return params;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			player.setAudioStreamType(AudioManager.STREAM_MUSIC);
			player.setOnBufferingUpdateListener(this);
			player.setOnPreparedListener(this);
			player.setDisplay(holder);
			if (isSurfaceDestroy) {
				player.prepareAsync();
			}
		} catch (Exception e) {
			Log.e("videoPlayer", "error", e);
		}
		Log.i("videoPlayer", "surface created");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		holder.setFixedSize(width, height);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (player == null) {
			return;
		}
		if (isPrepared) {
//			currentPosition = player.getCurrentPosition();
			if (nodePosition>0) {
				savePlayNode(getPlayIsEnd());
			}
		}

		isPrepared = false;
		isSurfaceDestroy = true;

		player.stop();
		player.reset();

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		isPrepared = true;
		if (!isFreeze) {
			if(isPlaying == null || isPlaying.booleanValue()){
				videoStart();
				playOp.setImageResource(R.drawable.btn_pause);
			}
		}

		definitionMap = player.getDefinitions();
			initDefinitionPopMenu();

		bufferProgressBar.setVisibility(View.GONE);
		LayoutParams params = getScreenSizeParams(currentScreenSizeFlag);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		surfaceView.setLayoutParams(params);
		videoDuration.setText(ParamsUtil.millsecondsToStr(player.getDuration()));
	}

	private void initDefinitionPopMenu() {
		definitionBtn.setVisibility(View.VISIBLE);
		
		if(definitionMap.size() > 1 && firstInitDefinition){
			currentDefinition = 1;
			firstInitDefinition = false;
		}
		
		definitionMenu = new PopMenu(context, R.drawable.popup, currentDefinition);
		// 设置清晰度列表
		definitionArray = new String[] {};
		definitionArray = definitionMap.keySet().toArray(definitionArray);

		definitionMenu.addItems(definitionArray);
		definitionMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				try {

					currentDefinition = position;
					int definitionCode = definitionMap
							.get(definitionArray[position]);

					if (isPrepared) {
						currentPosition = player.getCurrentPosition();
						if (player.isPlaying()) {
							isPlaying = true;
						} else {
							isPlaying = false;
						}
					}

					setLayoutVisibility(View.GONE, false);
					bufferProgressBar.setVisibility(View.VISIBLE);
					
					player.reset();
					player.setDefinition(context.getApplicationContext(),
							definitionCode);

				} catch (IOException e) {
					Log.e("player error", e.getMessage());
				}

			}
		});
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		skbProgress.setSecondaryProgress(percent);
	}

	OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnPlay:
				if (!isPrepared) {
					return;
				}

				changePlayStatus();
				break;

			case R.id.backPlayList:
			//	finish();
				
//				同一页面进行横竖屏切换
				// 0，竖屏，1，横屏
				if (screenOrientation == 0) {
					setScreenOrientation(1);
//					if(null!=coordinatorLayout){
//						AppBarLayout.LayoutParams lp =(android.support.design.widget.AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
//						lp.setScrollFlags(0);
//						}
//					
				} else if (screenOrientation == 1) {
					setScreenOrientation(0);
//					if(null!=coordinatorLayout){
//						coordinatorLayout.setScrollEnabled(true);
//						surfaceView.requestFocusFromTouch();

//						AppBarLayout.LayoutParams lp =(android.support.design.widget.AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
//						lp.setScrollFlags(3);
						
//					}
				}
				/*
				//切换到横屏播放
				Intent intent = new Intent(context, CCMediaPlayFull.class);
				intent.putExtra("videoId", videoId);
				intent.putExtra("position", player.getCurrentPosition());
				
				((Activity)context).startActivityForResult(intent, REQUEST_CCPOSITION);
				*/
				break;
			case R.id.playScreenSizeBtn:
				screenSizeMenu.showAsDropDown(v);
				break;
			case R.id.subtitleBtn:
				subtitleMenu.showAsDropDown(v);
				break;
			case R.id.definitionBtn:
				definitionMenu.showAsDropDown(v);
				break;
			}
		}
	};
	
	
	/**
	 * 设置屏幕方向
	 * 
	 * @param Orientation
	 *            0，竖屏；1横屏
	 */
	public void setScreenOrientation(int Orientation) {
		screenOrientation = Orientation;
		if (Orientation == 0) {
			backPlayList.setImageResource(R.drawable.iv_media_quanping);
			((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setPlayViewSize(0);
		} else if (Orientation == 1) {
			backPlayList.setImageResource(R.drawable.iv_media_esc);
			((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			setPlayViewSize(1);
		}
	}
	
	/**
	 * 设置播放界面的大小
	 * 
	 * @param type
	 *            0，竖屏播放；1横屏播放
	 */

	private void setPlayViewSize(int type) {
		ViewGroup.LayoutParams layoutParams = playerSurfaceViewRL.getLayoutParams();
		if (screenWidth == 0) {
			screenWidth = ScreenUtil.getInstance(context).getScreenWidth();
			ScreenUtil.getInstance(context).getScreenHeight();
		}
		if (type == 0) {
			full(false);
			layoutParams.width = screenWidth;
		//	layoutParams.height = layoutParams.width * 2 / 3;//视频高度为宽度的三分之二
			layoutParams.height = CommonUtil.dip2px(context, 200);//视频高度为200dp
		} else if (type == 1) {
			full(true);
			layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
			layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
			LayoutParams params = getScreenSizeParams(1);
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			surfaceView.setLayoutParams(params);
		}

		playerSurfaceViewRL.setLayoutParams(layoutParams);
	}
	
	/**
	 * @description 实现状态栏的显示与隐藏
	 * @param enable
	 */
	private void full(boolean enable) {
		if (enable) {
			WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
			lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			((Activity) context).getWindow().setAttributes(lp);
			((Activity) context).getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			WindowManager.LayoutParams attr = ((Activity) context).getWindow().getAttributes();
			attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			((Activity) context).getWindow().setAttributes(attr);
			((Activity) context).getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
		int progress = 0;

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (networkConnected ) {
				player.seekTo(progress);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (networkConnected ) {
				this.progress = progress * player.getDuration() / seekBar.getMax();
			}
		}
	};

	VerticalSeekBar.OnSeekBarChangeListener seekBarChangeListener = new VerticalSeekBar.OnSeekBarChangeListener() {
		@Override
		public void onStopTrackingTouch(VerticalSeekBar verticalseekbar) {

		}

		@Override
		public void onStartTrackingTouch(VerticalSeekBar verticalseekbar) {

		}

		@Override
		public void onProgressChanged(VerticalSeekBar verticalseekbar, int i,
				boolean flag) {
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
			currentVolume = i;
			volumeSeekBar.setProgress(i);
		}
	};

	// 控制播放器面板显示
	private boolean isDisplay = false;

/*	private void initSubtitleSwitchpMenu(Subtitle subtitle) {
		this.subtitle = subtitle;
		subtitleBtn.setVisibility(View.VISIBLE);
		subtitleMenu = new PopMenu(context, R.drawable.popup,
				currrentSubtitleSwitchFlag);
		subtitleMenu.addItems(subtitleSwitchArray);
		subtitleMenu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				switch (position) {
				case 0:// 开启字幕
					currentScreenSizeFlag = 0;
					subtitleText.setVisibility(View.VISIBLE);
					break;
				case 1:// 关闭字幕
					currentScreenSizeFlag = 1;
					subtitleText.setVisibility(View.GONE);
					break;
				}
			}
		});
	}
*/

//	public boolean dispatchKeyEvent(KeyEvent event) {
//		// 监测音量变化
//		if (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN
//				|| event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP) {
//
//			int volume = audioManager
//					.getStreamVolume(AudioManager.STREAM_MUSIC);
//			if (currentVolume != volume) {
//				currentVolume = volume;
//				volumeSeekBar.setProgress(currentVolume);
//			}
//
//			if (isPrepared) {
//				setLayoutVisibility(View.VISIBLE, true);
//			}
//		}
//		return dispatchKeyEvent(event);
//	}


	private void setLayoutVisibility(int visibility, boolean isDisplay) {
		if (player == null) {
			return;
		}

		if (player.getDuration() <= 0) {
			return;
		}

		this.isDisplay = isDisplay;
//		playerTopLayout.setVisibility(visibility);
		playerBottomLayout.setVisibility(visibility);
//		volumeLayout.setVisibility(visibility);
	}

	private Handler alertHandler = new Handler() {

		AlertDialog.Builder builder;
		AlertDialog.OnClickListener onClickListener = new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			//	finish();
			}

		};

		@Override
		public void handleMessage(Message msg) {

			String message = "";
			boolean isSystemError = false;
			if (ErrorCode.INVALID_REQUEST.Value() == msg.what) {
				message = "无法播放此视频，请检查视频状态";
			} else if (ErrorCode.NETWORK_ERROR.Value() == msg.what) {
				message = "无法播放此视频，请检查网络状态";
			} else if (ErrorCode.PROCESS_FAIL.Value() == msg.what) {
				message = "无法播放此视频，请检查帐户信息";
			} else {
				isSystemError = true;
			}

			if (!isSystemError) {
				builder = new AlertDialog.Builder(context);
				dialog = builder.setTitle("提示").setMessage(message)
						.setPositiveButton("OK", onClickListener)
						.setCancelable(false).show();
			}

			super.handleMessage(msg);
		}

	};
	private LinearLayout mLayout;

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		Message msg = new Message();
		msg.what = what;
		if (alertHandler != null) {
			alertHandler.sendMessage(msg);
		}
		return false;
	}

	public void onResume(int position) {
		
	//	player.start();//强制播放
		this.currentPosition=position;
		
		if (isFreeze) {
			isFreeze = false;
			if (isPrepared) {
				player.start();
			}
		} else {
			if (isPlaying != null && isPlaying.booleanValue() && isPrepared) {
				player.start();
			}
		}
	}

	public void onPause() {
		if (isPrepared) {
			// 如果播放器prepare完成，则对播放器进行暂停操作，并记录状态
			if (player.isPlaying()) {
				isPlaying = true;
			} else {
				isPlaying = false;
			}
			player.pause();
		} else {
			// 如果播放器没有prepare完成，则设置isFreeze为true
			isFreeze = true;
		}

	}

	public void onDestroy() {
		if (timerTask!=null) {
			timerTask.cancel();
		}
		alertHandler.removeCallbacksAndMessages(null);
		alertHandler = null;
		
		if (player != null) {
//			videoPause();
			player.reset();
			player.release();
			player = null;
		}
		if (dialog != null) {
			dialog.dismiss();
		}
		//	networkInfoTimerTask.cancel();
	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		LayoutParams params = getScreenSizeParams(currentScreenSizeFlag);
		params.addRule(RelativeLayout.CENTER_IN_PARENT);
		surfaceView.setLayoutParams(params);
	}


	public boolean onTouchEvent(MotionEvent event) {
//		if(screenOrientation == 0){
//			setLayoutVisibility(View.VISIBLE, true);
//			return false;	
//		}else{
			getSurfaceViewRect();
			if (mChangeImageBackgroundRect!=null) {
				if(mChangeImageBackgroundRect.contains((int)event.getX(), (int)event.getY())){
					
					if (!isPrepared) {
						return true;
					}
					// 事件监听交给手势类来处理
					return detector.onTouchEvent(event);
				}
			}
			return false;	
			
//		}
		
	
	}

	/**
	 * 手势监听器类
 	 */
	private class MyGesture extends SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return super.onSingleTapUp(e);
		}

		@Override
		public void onLongPress(MotionEvent e) {
			super.onLongPress(e);
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if (!isDisplay) {
				setLayoutVisibility(View.VISIBLE, true);
			}
			scrollTotalDistance += distanceX;

			float duration = (float) player.getDuration();

			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

			float width = wm.getDefaultDisplay().getWidth() * 0.75f; // 设定总长度是多少，此处根据实际调整

			float currentPosition = scrollCurrentPosition - (float) duration
					* scrollTotalDistance / width;

			if (currentPosition < 0) {
				currentPosition = 0;
			} else if (currentPosition > duration) {
				currentPosition = duration;
			}

			player.seekTo((int) currentPosition);

			playDuration.setText(ParamsUtil
					.millsecondsToStr((int) currentPosition));
			int pos = (int) (skbProgress.getMax() * currentPosition / duration);
			skbProgress.setProgress(pos);

			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			return super.onFling(e1, e2, velocityX, velocityY);
		}

		@Override
		public void onShowPress(MotionEvent e) {
			super.onShowPress(e);
		}

		@Override
		public boolean onDown(MotionEvent e) {

			scrollTotalDistance = 0f;

			scrollCurrentPosition = (float) player.getCurrentPosition();

			return super.onDown(e);
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			if (!isDisplay) {
				setLayoutVisibility(View.VISIBLE, true);
			}
			changePlayStatus();
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			return super.onDoubleTapEvent(e);
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
				if (isDisplay) {
					setLayoutVisibility(View.GONE, false);
				} else {
					setLayoutVisibility(View.VISIBLE, true);
				}
			return super.onSingleTapConfirmed(e);
		}
	}

	/**
	 * 更改视频播放状态
	 */
	public void changePlayStatus() {
		if (player.isPlaying()) {
			player.pause();
			playOp.setImageResource(R.drawable.btn_play);

		} else {
			player.start();
			playOp.setImageResource(R.drawable.btn_pause);
		}
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		switch(what) {
		case DWMediaPlayer.MEDIA_INFO_BUFFERING_START:
			if (player.isPlaying()) {
				bufferProgressBar.setVisibility(View.VISIBLE);
			}
			break;
		case DWMediaPlayer.MEDIA_INFO_BUFFERING_END:
			bufferProgressBar.setVisibility(View.GONE);
			break;
		}
		return false;
	}
	/**
	 * 根据传入的时间更改视频的播放进度
	 */
	public int getCcPosition(){
		return player.getCurrentPosition();
	}
	/**
	 * 返回视频是否正在播放
	 * @return
	 */
	public boolean CcIsPlaying(){
		return player.isPlaying();
	}
	
	public void CcReset(){
		player.reset();
	}

	/**
	 * 隐藏全屏按钮，不能全屏
	 */
	public void showFullScreen(int value){
		if (backPlayList!=null) {
			backPlayList.setVisibility(value);
		}
	}


	public boolean isCourse = true;

	/**
	 * 播放暂停
	 */
	public void videoPause(){
		player.pause();
		savePlayNode(getPlayIsEnd());
	}

	/**
	 * 开始播放
	 */
	public void videoStart(){
		player.start();
		if (node==-1){
			player.seekTo(0);
		}else{
			player.seekTo(node);
		}

	}

	/**
	 * 判断视频是否播放完成
	 * @return true播完了 false 没播完
	 */
	public boolean getPlayIsEnd(){
		if (player.getCurrentPosition()==player.getDuration()){
			return true;
		}else {
			return false;
		}
	}


	/**
	 * 记录视频播放位置
	 */
	public int nodePosition;
	/**
	 * 记录播放节点
	 * @param isEnd 是不是播放到视频结束
	 */
	public void savePlayNode(boolean isEnd ){
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
					map.put("kid"+kpointId,nodePosition);
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
							map.put("cid" + courseId, nodePosition);
					}else {
						if (isEnd && map.containsKey("aid"+answerId))
							map.remove("aid"+answerId);
						else
							map.put("aid"+answerId,nodePosition);
						SharedPreferencesUtil.getInstance().editPutString("oldAnswerId", SharedPreferencesUtil.getInstance().spGetString("answerId"));
					}
				}else {
					if (isEnd && map.containsKey("zid"+zengyanId))
						map.remove("zid"+zengyanId);
					else
						map.put("zid"+zengyanId,nodePosition);
				}
			}
			PreferencesObjectUtil.saveObject(map, "videoPlayNode", context);

		}
	}


}
