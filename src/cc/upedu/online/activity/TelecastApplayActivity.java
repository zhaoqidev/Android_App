package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.VideoBaseActivity;
import cc.upedu.online.domin.CourseIntroduceBean;
import cc.upedu.online.domin.LiveFreeRegisiter;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cc.upedu.online.utils.WebViewSettingUtils;
import cc.upedu.online.view.CircleImageView;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 * 直播简介界面
 * @author Administrator
 *
 */
public class TelecastApplayActivity extends VideoBaseActivity {
	private String courseId;
	private CircleImageView teacher_img;
	private TextView teacher_name,teacher_work,teacher_doc;
	private WebView wv;
	private TextView bt_applay;
	private String userId;
	private CourseIntroduceBean mCourseIntroduceBean;
	//是否可以直接观看/是否已经购买过该课程
	private String isok,isfree,courseName,courseLogo,startTime,teacherName,lessontimes;
	private OnekeyShare oks;
//	private String ccVideoId="7D315A2D76FBE2EF9C33DC5901307461";//获取到真实id后可以使用videoId赋值，删除此变量
	private String iscollected;//是否已经收藏
	private Dialog loadingDialog;
	
	@Override
	protected void initData() {
		userId = UserStateUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			userId = String.valueOf(0);
		}
		Map<String, String> requestDataMap = ParamsMapUtil.getCourseIntroduce(context, courseId, userId,"Android");
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_INTRODUCE, context, requestDataMap, new MyBaseParser<>(CourseIntroduceBean.class));
		DataCallBack<CourseIntroduceBean> coursseDataCallBack = new DataCallBack<CourseIntroduceBean>() {

			@Override
			public void processData(CourseIntroduceBean object) {
				if (object==null) {
					ShowUtils.showMsg(context, "获取直播数据失败请联系客服");
				}else {
					mCourseIntroduceBean = object;
					handler.obtainMessage(0).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, coursseDataCallBack);
		
	}
	private Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if ("true".equals(mCourseIntroduceBean.getSuccess())) {
					if (mCourseIntroduceBean.getEntity() != null) {
						isok = mCourseIntroduceBean.getEntity().getIsok();
						isfree = mCourseIntroduceBean.getEntity().getIsfree();
						videoId = mCourseIntroduceBean.getEntity().getCourse().getFreeurl();
						SharedPreferencesUtil.getInstance().editPutString("secretUid", mCourseIntroduceBean.getEntity().getSecretUid());//把分享用户的id存储起来
						if (mCourseIntroduceBean.getEntity().getCourse() != null) {
							courseName = mCourseIntroduceBean.getEntity().getCourse().getName();
							courseLogo = mCourseIntroduceBean.getEntity().getCourse().getCourseLogo();
							startTime = mCourseIntroduceBean.getEntity().getCourse().getLiveBeginTime();
							teacherName = mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getName();
							lessontimes = mCourseIntroduceBean.getEntity().getCourse().getLessontimes();

							String videotype = mCourseIntroduceBean.getEntity().getCourse().getVideotype();
							initVideoData(videoId,courseName,courseLogo,videotype);
							setExtraData(courseName, startTime);
							
							if (Boolean.valueOf(mCourseIntroduceBean.getEntity().getIscollected())) {
								iscollected = "true";
								setRightButton(R.drawable.iconfont_shoucang,null);
							}
							setData2View();
						}
					}
				}else {
					ShowUtils.showMsg(TelecastApplayActivity.this, mCourseIntroduceBean.getMessage());
				}
				break;
			case 3:
				ShowUtils.showMsg(context, "预约成功！");
				isok = "true";
				skipHome();
				break;
			case 4:
				ShowUtils.showMsg(context, "预约失败,请重新预约！");
				isok = "false";
				break;
			}
		}
	};
	private void setData2View() {
		if (mCourseIntroduceBean.getEntity().getCourse().getTeacherList()!= null && mCourseIntroduceBean.getEntity().getCourse().getTeacherList().size() > 0) {
					
			ImageUtils.setImage(mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getPicPath(), teacher_img, 0);
			teacher_name.setText(mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getName());
			teacher_work.setText(mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getEducation());
			teacher_doc.setText(mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getCareer());
		}
		teacher_img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mCourseIntroduceBean.getEntity().getCourse().getTeacherList() != null && mCourseIntroduceBean.getEntity().getCourse().getTeacherList().size() > 0) {
					Intent intent = new Intent(context, TeacherVisitCard.class);
					intent.putExtra("teacherId", mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getId());
					intent.putExtra("teacherPosition", mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getEducation());
					intent.putExtra("teacherName", mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getName());
					intent.putExtra("teacherLogo", mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getPicPath());
					context.startActivity(intent);
				}else {
					ShowUtils.showMsg(context, "请求的数据异常!");
				}
			}
		});
		wv.loadDataWithBaseURL(null, mCourseIntroduceBean.getEntity().getCourse().getContext(), "text/html", "utf-8", null);
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	};

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("直播简介");
		setRightButton(R.drawable.left_collect, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				collectSave();
			}
		});
		setRightButton2(R.drawable.share, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (StringUtil.isEmpty(courseId) || StringUtil.isEmpty(courseName) || StringUtil.isEmpty(courseLogo)) {
					ShowUtils.showMsg(context, "数据获取失败,请检查您的网络!");
				}else {
					oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_TELECAST,courseId,courseName,courseLogo,Boolean.valueOf(isok),null,
							"直播: "+courseName+
									"\n导师: "+teacherName+
									"\n时间: "+lessontimes);
					if (oks != null) {
						oks.show(context);
					}
				}
			}
		});
		courseId = getIntent().getStringExtra("courseId");
		startTime = getIntent().getStringExtra("startTime");
		courseName = getIntent().getStringExtra("courseName");
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
	}

	@Override
	public View initTwelfthLayout() {
		showFullScreen(false);//设置不全屏显示
		View view = View.inflate(this, R.layout.layout_telecastapplay_content, null);
		teacher_img = (CircleImageView) view.findViewById(R.id.teacher_img);
		teacher_name = (TextView) view.findViewById(R.id.teacher_name);
		teacher_work = (TextView) view.findViewById(R.id.teacher_work);
		teacher_doc = (TextView) view.findViewById(R.id.teacher_doc);
		wv = (WebView) view.findViewById(R.id.wv);
		WebViewSettingUtils.setWebViewCommonAttrs(context, wv);
		bt_applay = (TextView) view.findViewById(R.id.bt_applay);

		return view;
	}
	private TextView tv_couresname,tv_livetime;
	@Override
	public View initVideoBottom() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_telecaseapplay_videobottom, null);
		tv_couresname = (TextView) view.findViewById(R.id.tv_couresname);
		tv_livetime = (TextView) view.findViewById(R.id.tv_livetime);
		return view;
	}

	private void setExtraData(String courseName, String startTime) {
		// TODO Auto-generated method stub
		tv_couresname.setText(courseName);
		tv_livetime.setText(startTime);
	}
	/**
	 * 
	 */
	private void collectSave() {
		//判断是否已经登录
		if (!UserStateUtil.isLogined()) {//未登录
			ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录.", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					//跳转到登录界面
					Intent intent = new Intent(context, LoginActivity.class);
					startActivity(intent);
				}
			});
		}else {//已登录
			//判断是否已经收藏
			if (Boolean.valueOf(iscollected)) {
				ShowUtils.showMsg(context, "您已经收藏过此课程!");
			}else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("courseId", courseId);
				map.put("userId", userId);
				UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.COLLECTION_COURSE,map , new UploadDataCallBack() {

					@Override
					public void onUploadDataSuccess() {
						// TODO Auto-generated method stub
						ShowUtils.showMsg(context, "收藏课程成功");
						iscollected = "true";
						setRightButton(R.drawable.iconfont_shoucang,null);
					}

					@Override
					public void onUploadDataFailure() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		bt_applay.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_applay:
			if (UserStateUtil.isLogined()) {
				if (StringUtil.isEmpty(isok) || StringUtil.isEmpty(isfree)) {
					ShowUtils.showMsg(context, "请求数据错误,请检查网络!");
				}else {
					//判断是否已经购买
					if (!Boolean.valueOf(isok)) {//未购买
						//免费课程，未进行免费预约
						if(Boolean.valueOf(isfree)){
							
							handleFreeLiveVideoRegisiter();
							
						
						}else{
							ShowUtils.showDiaLog(context, "温馨提醒", "此课程您还没有购买,请先购买再学习.祝您学有所成!", new ConfirmBackCall() {
								@Override
								public void confirmOperation() {
									//跳转到购买课程界面
									Intent intent = new Intent(context, MyShoppingTrolleyActivity.class);
									SharedPreferencesUtil.getInstance().editPutString("ShoopingCourseId", courseId);
									startActivity(intent);
								}
							});
						}
					}else {//已购买
						skipHome();
					}
				}
			}else {
				UserStateUtil.NotLoginDialog(context);
			}
			break;
		}
	}
	private void skipHome() {
		UserStateUtil.loginInFailuer(context,new FailureCallBack() {
			
			@Override
			public void onFailureCallBack() {
			}
		}, new SuccessCallBack() {
			
			@Override
			public void onSuccessCallBack() {
				//还要加入直播要判断参加人数是否已经满员
				//判断直播是否在进行中
				if (UserStateUtil.isPlaying(mCourseIntroduceBean.getEntity().getCourse().getLiveBeginTime())) {
					Intent intent;
					if (StringUtil.getCpuArchitecture()) {
						intent = new Intent(context, TelecastHomeActivity.class);
					}else {
						intent = new Intent(context, LiveHomeActivity.class);
						intent.putExtra("iscollected", mCourseIntroduceBean.getEntity().getIscollected());
					}
					intent.putExtra("courseItem", (Serializable)mCourseIntroduceBean.getEntity().getCourse());
					startActivity(intent);
				}else {
					ShowUtils.showMsg(context, "直播未开始");
				}
			}
		});
	}
	
	private void handleFreeLiveVideoRegisiter() {
		// TODO Auto-generated method stub
		userId = UserStateUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			userId = String.valueOf(0);
		}
		Map<String, String> requestDataMap = ParamsMapUtil.getLiveFree(context, courseId, userId);
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_FREELIVE, context, requestDataMap, new MyBaseParser<>(LiveFreeRegisiter.class));
		DataCallBack<LiveFreeRegisiter> liveFreeDataCallBack = new DataCallBack<LiveFreeRegisiter>() {

			@Override
			public void processData(LiveFreeRegisiter object) {
				if (object==null) {
					ShowUtils.showMsg(context, "获取直播数据失败请联系客服");
				}else {
					LiveFreeRegisiter mLiveFreeRegisiter = object;
					if(Boolean.valueOf(mLiveFreeRegisiter.getSuccess())){
						handler.obtainMessage(3).sendToTarget();
					}else {
						handler.obtainMessage(4).sendToTarget();
					}
				}
			}
		};
		getDataServer(requestVo, liveFreeDataCallBack);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		System.out.println("*************************************");
//		if (isCCPlay && mCcMediaPlay!=null) {
//			mCcMediaPlay.onTouchEvent(event);
//		}
//		return super.onTouchEvent(event);
//	}
}
