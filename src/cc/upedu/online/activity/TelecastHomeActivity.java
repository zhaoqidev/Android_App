package cc.upedu.online.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.net.RtComp;
import com.gensee.net.RtComp.Callback;
import com.gensee.room.RtSdk;
import com.gensee.room.RtSimpleImpl;
import com.gensee.routine.IRTEvent.IRoomEvent.JoinResult;
import com.gensee.routine.IRTEvent.IRoomEvent.LeaveReason;
import com.gensee.routine.State;
import com.gensee.routine.UserInfo;
import com.gensee.view.GSVideoView;
import com.gensee.view.GSVideoView.RenderMode;

import java.util.LinkedHashMap;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.LayoutTabsBaseActivity;
import cc.upedu.online.domin.CourseIntroduceBean.Entity.Course;
import cc.upedu.online.fragment.CourseStudySchoolmateFragment;
import cc.upedu.online.fragment.TelecastCommentFragment;
import cc.upedu.online.fragment.TelecastNoteFragment;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.telecastchat.ChatResource;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 * 直播间界面
 * @author Administrator
 *
 */
public class TelecastHomeActivity extends LayoutTabsBaseActivity implements Callback{
	private Course courseItem;
	// 课程简介图片
	private ImageView telecast_img,backPlayList;
	private String courseId;
	private RelativeLayout rl_video;
	private GSVideoView videoView;
	private TelecastCommentFragment mTelecastCommentFragment;
	private TelecastNoteFragment mTelecastNoteFragment;
	private CourseStudySchoolmateFragment mTelecastSchoolmateFragment;
	private Dialog loadingDialog;
	private OnekeyShare oks;
	private int currentPagePostion;
	@Override
	protected void initData() {
		setPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentPagePostion = arg0;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		initTabsViewPager();
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("直播间");
		setLeftButton(R.drawable.back, "返回", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leaveCast();
				TelecastHomeActivity.this.finish();
			}
		});
		setRightButton(R.drawable.share, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//分享
				if (oks != null) {
					oks.show(context);
				}else {
					ShowUtils.showMsg(context, "数据错误!");
				}
			}
		});
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
//		iscollected = getIntent().getStringExtra("");
		courseItem = (Course)getIntent().getSerializableExtra("courseItem");
	}
	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_telecasthome_top, null);
		rl_video = (RelativeLayout) view.findViewById(R.id.rl_video);
		videoView = (GSVideoView) view.findViewById(R.id.surface_casting_cus);
		videoView.setRenderMode(RenderMode.RM_FILL_XY);
		telecast_img = (ImageView) view.findViewById(R.id.telecast_img);
		backPlayList = (ImageView) view.findViewById(R.id.backPlayList);
		initPlayer();
		
		 if (courseItem != null) {
				courseId = courseItem.getCourseId();
				
				OnlineApp.myApp.imageLoader.displayImage(
		        		  ConstantsOnline.SERVER_IMAGEURL+courseItem.getCourseLogo(), 
		        		  telecast_img, 
		        		  OnlineApp.myApp.builder
		        			.showImageOnLoading(R.drawable.rollview_default)  
		        	        .showImageOnFail(R.drawable.rollview_default)
		        	        .build());
				
				oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_COURSE,courseId,courseItem.getName(),courseItem.getCourseLogo(),true,null,
						"直播: "+courseItem.getName()+
						"\n导师: "+courseItem.getTeacherList().get(0).getName()+
						"\n时间: "+courseItem.getLessontimes());
				
				joinHome();
			}else {
				ShowUtils.showDiaLog(context, "温馨提示", "数据错误,请反馈信息,谢谢!是否退出直播间?", new ConfirmBackCall() {
					
					@Override
					public void confirmOperation() {
						// TODO Auto-generated method stub
						TelecastHomeActivity.this.finish();
					}
				});
			}
		return view;
	}
//	ImageCallback callback=new ImageCallback() {
//		@Override
//		public void loadImage(Bitmap bitmap, String imagePath) {
//			telecast_img.setImageBitmap(bitmap);
//		}
//	};
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.backPlayList://切换横竖屏
			if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
    			//横屏时,把视频切换成竖屏
				TelecastHomeActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    		} else {
    			TelecastHomeActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    		}
			break;
		}
	}

	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
		mTelecastSchoolmateFragment = new CourseStudySchoolmateFragment(context,courseId);
		mTelecastNoteFragment = new TelecastNoteFragment(context,courseId);
		mTelecastCommentFragment = new TelecastCommentFragment(context);
		map.put("成员", mTelecastSchoolmateFragment);
		map.put("笔记", mTelecastNoteFragment);
		map.put("群聊", mTelecastCommentFragment);
		return map;
	}
	
	@Override
	protected void initListener() {
		super.initListener();
		backPlayList.setOnClickListener(this);
	}

	private RtSimpleImpl simpleImpl;
	private UserInfo self;
	private void initPlayer(){
		simpleImpl = new RtSimpleImpl() {
			
			@Override
			public Context onGetContext() {
				return getBaseContext();
			}
			
			@Override
			protected void onVideoStart() {
//				ShowUtils.showMsg(context, "onVideoStart");
			}
			
			@Override
			protected void onVideoEnd() {
//				ShowUtils.showMsg(context, "onVideoStart");
			}
			
			/**
			 * result 0 表示加入房间（直播间、会议室、课堂）成功  其他代表加入失败  失败后最好以对话框通知用户本次操作失败了
			 */
			@Override
			public void onRoomJoin(final int result, UserInfo self) {
				super.onRoomJoin(result, self);
				TelecastHomeActivity.this.self = self;
				runOnUiThread(new Runnable() {
					public void run() {
						
						String resultDesc = null;
						switch (result) {
						//加入成功  除了成功其他均需要正常提示给用户
						case JoinResult.JR_OK:
							telecast_img.setVisibility(View.GONE);
							if (loadingDialog != null && loadingDialog.isShowing()) {
								loadingDialog.dismiss();
							}
							ShowUtils.showMsg(context, "您已成功加入直播间");
							break;
							//加入错误
						case JoinResult.JR_ERROR:
							resultDesc = "加入失败，重试或联系管理员";
							break;
							//课堂被锁定
						case JoinResult.JR_ERROR_LOCKED:
							resultDesc = "直播间已被锁定";
							
							break;
							//老师（组织者已经加入）
						case JoinResult.JR_ERROR_HOST:
							resultDesc = "老师已经加入，请以其他身份加入";
							break;
							//加入人数已满
						case JoinResult.JR_ERROR_LICENSE:
							resultDesc = "人数已满，联系管理员";
							
							break;
							//音视频编码不匹配
						case JoinResult.JR_ERROR_CODEC:
							resultDesc = "编码不匹配";
							break;
							//超时
						case JoinResult.JR_ERROR_TIMESUP:
							resultDesc = "已经超过直播结束时间";
							break;
							
						default:
							resultDesc = "其他结果码：" + result + "联系管理员";
							break;
						}
						if(resultDesc != null){
							ShowUtils.showDiaLog(context, "提示", resultDesc, new ConfirmBackCall() {
								
								@Override
								public void confirmOperation() {
									// TODO Auto-generated method stub
									leave(false);if (loadingDialog != null && loadingDialog.isShowing()) {
										loadingDialog.dismiss();
									}
								}
							});
						}
					}
				});
				
			}
			
			/**
			 * 直播状态 s.getValue()   0 默认直播未开始 1、直播中， 2、直播停止，3、直播暂停
			 */
			@Override
			public void onRoomPublish(State s) {
				super.onRoomPublish(s);
				
				//TODO 此逻辑是控制视频要在直播开始后才准许看的逻辑
				/*	byte castState = s.getValue();
				    RtSdk rtSdk = getRtSdk();
				    
					switch (castState) {
					case 1:
						setVideoView(videoView);
				        rtSdk.audioOpenSpeaker(null);
						break;
					case 0:
					case 2:
					case 3:
						setVideoView(null);
				        rtSdk.audioCloseSpeaker(null);
					default:
						break;
					}*/
				
			}

			@Override
			public void onJoin(boolean result) {
				// TODO Auto-generated method stub
				
			}
			
			//退出完成 关闭界面
			@Override
			protected void onRelease(final int reason) {
				//reason 退出原因
				runOnUiThread(new Runnable() {
					
					@SuppressLint("NewApi")
					@Override
					public void run() {
						String msg = "已退出";
						switch (reason) {
						//用户自行退出  正常退出
						case LeaveReason.LR_NORMAL:
							msg = "您已经成功退出";
							break;
					    //LR_EJECTED = LR_NORMAL + 1; //被踢出
						case LeaveReason.LR_EJECTED:
							msg = "您已被踢出"; 
							break;
						//LR_TIMESUP = LR_NORMAL + 2; //时间到
						case LeaveReason.LR_TIMESUP:
							msg = "时间已过"; 
							break;
						//LR_CLOSED = LR_NORMAL + 3; //直播（课堂）已经结束（被组织者结束）
						case LeaveReason.LR_CLOSED:
							msg = "直播间已经被关闭"; 
							break;

						default:
							break;
						}
						if (isDestroyed()) {
							return;
						}
						//这里可以弹出对话框，确定后在关闭界面
						ShowUtils.showDiaLog(context, "提示", msg, new ConfirmBackCall() {
							
							@Override
							public void confirmOperation() {
								// TODO Auto-generated method stub
								//确认关闭界面
								finish();
							}
						});
					}
				});
			}
		};

		/**
		 * 设置视频View
		 */
		simpleImpl.setVideoView(videoView);
		videoView.setRenderMode(RenderMode.RM_ADPT_XY);
		// 这里可以获得Rtsdk的实例，因此可以注册其他回调进行其他功能"问答、聊天、投票"的开发
		/*
		 * RtSdk rtSdk = simpleImpl.getRtSdk(); 
		 * rtSdk.setQACallback(qaCallback); //问答功能
		 * rtSdk.setVoteCallback(voteCallBack);//投票
		 * rtSdk.setChatCallback(chatCallBack);//聊天
		 */
		
		/*
		 * 设置文档View
		 */
        //simpleImpl.setDocView(docView);
		rtSdk = simpleImpl.getRtSdk();
		mRTSdk2 = rtSdk;
//		rtSdk.setQACallback(null);
		// 初始化聊天表情资源，可以更加app自身需求，如果没有聊天或有聊天不需要显示表情则忽略
		ChatResource.initChatResource(TelecastHomeActivity.this);
	}
	private static RtSdk rtSdk;
	public static RtSdk mRTSdk2;
	private void joinHome() {
		// TODO Auto-generated method stub
//		edtDomain.setText("cintv.gensee.com");
//		edtNum.setText("93857580");
//		edtAccount.setText("");
//		edtPwd.setText("888888");
//		edtNick.setText("Android");
//		edtJoinPwd.setText("123456");
		
//		 	http://upedu.gensee.com/training/site/r/22996197
//		wb_live.loadUrl("http://upedu.gensee.com/training/site/s/22996197?apptoken=&token=&nickname=test");
		String liveurl = courseItem.getLiveurl();
		String[] split = liveurl.split("\\?");
		String liveId = "";
		String joinPwd = "";
		if (split.length > 0) {
			liveId = split[0].substring(split[0].lastIndexOf("/")+1);
			if (split.length == 2) {
				String[] params = split[1].split("\\&");
				for (String param : params) {
					int indexOf = param.indexOf("apptoken=");
					if (indexOf != -1) {
						String[] split2 = param.split("=");
						if (split2.length == 2) {
							joinPwd = split2[1];
						}else {
							joinPwd = "";
						}
						break;
					}
				}
			}
		}else {
			ShowUtils.showMsg(context, "直播数据错误,请反馈信息,谢谢!");
			TelecastHomeActivity.this.finish();
		}
		InitParam p = new InitParam();
		//domain域名
		p.setDomain("upedu.gensee.com");
		//编号（直播间号）,如果没有编号却有直播id的情况请使用setLiveId("此处直播id或课堂id");
		p.setNumber(liveId);
//		p.setNumber("22996197");
//		p.setLiveId("79647849");
		//站点认证帐号，根据情况可以填""
		p.setLoginAccount("admin@upedu.com");
		//站点认证密码，根据情况可以填""
		p.setLoginPwd("upedu123");
		//昵称，供显示用
		p.setNickName(SharedPreferencesUtil.getInstance().spGetString("name"));
		p.setUserId(Long.valueOf(UserStateUtil.getUserId())+1000000000);
		//加入口令，没有则填""333333
//		p.setJoinPwd("333333");
		if (StringUtil.isEmpty(joinPwd)) {
			p.setJoinPwd("");
		}else {
			p.setJoinPwd(joinPwd);
		}
		//站点类型ServiceType.ST_CASTLINE 直播webcast，
		//ServiceType.ST_MEETING 会议meeting，
		//ServiceType.ST_TRAINING 培训
		p.setServiceType(ServiceType.ST_TRAINING);
		
		RtComp comp = new RtComp(getApplicationContext(),
				TelecastHomeActivity.this);
		comp.setbAttendeeOnly(true);
		comp.initWithGensee(p);
	}
	@Override
	public void onErr(final int errCode) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ShowUtils.showDiaLog(context, "提示", "初始化错误:错误码" + errCode+ "，请反馈信息谢谢!", new ConfirmBackCall() {
					
					@Override
					public void confirmOperation() {
						// TODO Auto-generated method stub
					}
				});
			}
		});
	}
	@Override
	public void onInited(String rtParam) {
		simpleImpl.joinWithParam("", rtParam);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ShowUtils.showDiaLog(context, "提示", "您确定要退出直播间吗!", new ConfirmBackCall() {
			
			@Override
			public void confirmOperation() {
				// TODO Auto-generated method stub
				leaveCast();
			}
		});
		super.onDestroy();
	}
	/**
	 * 退出的时候请调用
	 */
	private void leaveCast(){
		//TODO 显示进度框
		simpleImpl.leave(false);
	}

	
	@Override
	public void onBackPressed() {
		if (self == null) {
			super.onBackPressed();
			return;
		}
		leaveCast();
	}
	
	public void packUpKeyboard() {
		// TODO Auto-generated method stub
		if(TelecastHomeActivity.this.getCurrentFocus()!=null){
			((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(TelecastHomeActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	@SuppressLint("NewApi")
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
			//横屏
			setTitleViewVisibility(View.GONE);
			setTwelfthLayoutVisibility(View.GONE);
//			coordinatorLayout.setNestedScrollingEnabled(false);
			backPlayList.setImageResource(R.drawable.iv_media_esc);
			rl_video.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
		} else if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_PORTRAIT) {
			//竖屏
			setTitleViewVisibility(View.VISIBLE);
			setTwelfthLayoutVisibility(View.VISIBLE);
			backPlayList.setImageResource(R.drawable.iv_media_quanping);
			rl_video.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, 200)));
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if(this.getResources().getConfiguration().orientation ==Configuration.ORIENTATION_LANDSCAPE) {
    			//横屏时,把视频切换成竖屏
				TelecastHomeActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				return false; 
    		} else {
    			if (1 == currentPagePostion) {
    				mTelecastNoteFragment.onKeyDown();
    				return false; 
    			}else { 
    				return super.onKeyDown(keyCode, event); 
    			}
    		}
       }else { 
           return super.onKeyDown(keyCode, event); 
       }
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
//			if (requestCode==CourseStudySchoolmateFragment.REQUEST_CHOOSEINDUSTRY){//设置所属行业的数据回传
//			}
			if (resultCode==SetIndustryActivity.RESULT_SETINDUSTRY){
				Bundle bundle=data.getExtras(); 
				if (bundle != null) {
					String industry=bundle.getString("industry");
					String industryname=bundle.getString("industryname");
					mTelecastSchoolmateFragment.setIndustry(industryname, industry);
				}
			}

//			if (requestCode == CityChooseActity.CHOOSE_ONE) {// 设置所在
//			}
			if (resultCode == CityChooseActity.CHOOSE_ONE) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String text = bundle.getString("text");
					String id = bundle.getString("id");
					mTelecastSchoolmateFragment.setOthercity(text, id);
				}
			}
		}
	}
}
