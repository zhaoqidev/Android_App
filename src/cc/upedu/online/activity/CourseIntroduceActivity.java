package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.VideoToolbarTabsBaseActivity;
import cc.upedu.online.domin.CourseIntroduceBean;
import cc.upedu.online.fragment.IntroduceCourseFragment;
import cc.upedu.online.fragment.IntroduceInspirationFragment;
import cc.upedu.online.fragment.IntroduceTheacherFragment;
import cc.upedu.online.fragment.IntroduceValueFragment;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 * 课程介绍界面
 * @author Administrator
 *
 *
 *
 */
public class CourseIntroduceActivity extends VideoToolbarTabsBaseActivity{
	private static final int REQUEST_CODE = 1;
	private String userId;
	//我要学习按钮
	private Button bt_study;
	//我要代言按钮
	private Button bt_represent;
	// 需要传递给viewPager显示图片关联文字的集合
	private CourseIntroduceBean mCourseIntroduceBean;
	private String courseId;
	//是否已收藏
	private String iscollected;
	//是否可以直接观看/是否已经购买过该课程
	private String isok = null;
	private IntroduceInspirationFragment  introduceInspirationFragment;
//	private String ccVideoId="7D315A2D76FBE2EF9C33DC5901307461";//获取到真实id后可以使用videoId赋值，删除此变量
	private Dialog loadingDialog;
	
	@Override
	protected void initData() {
		//获取体系介绍的数据
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
					ShowUtils.showMsg(context, "获取体系介绍数据失败请联系客服");
				}else {
					mCourseIntroduceBean = object;
					//把子页面需要的数据存到sp中
					if (object.getEntity()!=null) {
						PreferencesObjectUtil.saveObject(object.getEntity().getCourse(), "IntroduceCourse", context);//课程信息					
					}
					handler.obtainMessage(0).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, coursseDataCallBack);
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
		//从分享页面打开课程页面
		courseId = getIntent().getStringExtra("courseId");
		//判断一下courseId有没有值，有值即为分享页面打开，没有值就是从app内部跳转过来的
		if (StringUtil.isEmpty(courseId)) {
			Uri uri = getIntent().getData();  
			courseId= uri.getQueryParameter("courseId");  
			String shareUid= uri.getQueryParameter("shareUid");
			if ((!StringUtil.isEmpty(courseId))&&(!StringUtil.isEmpty(shareUid))&&(!shareUid.equals("0"))&&(!shareUid.equals(UserStateUtil.getUserId()))) {
				SharedPreferencesUtil.getInstance().editPutString("share_courseId", courseId);//将分享的课程的id进行本地持久化
				SharedPreferencesUtil.getInstance().editPutString("share_shareUid", shareUid);//将分享人进行本地持久化
			}
		}
		setTitleText("课程简介");
		setRightButton(R.drawable.left_collect, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				collectSave();
			}
		});
	}
	@Override
	public View initBottomView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_courseintroduce_bottom, null);
		bt_study = (Button) view.findViewById(R.id.bt_study);
		bt_represent = (Button) view.findViewById(R.id.bt_represent);
		return view;
	}
	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<>();
		introduceInspirationFragment=new IntroduceInspirationFragment(context, courseId);
		map.put("课程简介", new IntroduceCourseFragment());
		map.put("课程导师", new IntroduceTheacherFragment());
		map.put("课程价值", new IntroduceValueFragment());
		map.put("课程感悟", introduceInspirationFragment);
		return map;
	}
	@Override
	public void setAppBarLayoutChanged(int i) {
		// TODO Auto-generated method stub
		if (i == 0) {
			// 展开状态，可刷新
			if (introduceInspirationFragment != null ) {
				introduceInspirationFragment.setPullRefreshEnable(true);
			}
		}else {
			// 收起状态，不可刷新
			if (introduceInspirationFragment != null ) {
				introduceInspirationFragment.setPullRefreshEnable(false);
			}
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		bt_study.setOnClickListener(this);
		bt_represent.setOnClickListener(this);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode==REQUEST_CODE){  
				if (resultCode==CourseStudyActivity.RESULT_CODE){  
					iscollected = data.getExtras().getString("iscollected");
					if (Boolean.valueOf(iscollected)) {
						setRightButton(R.drawable.iconfont_shoucang,null);
					}else {
						setRightButton(R.drawable.left_collect,null);
					}
				}  
			}
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_study://我要学习
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
				if (StringUtil.isEmpty(isok)) {
					ShowUtils.showMsg(context, "请求数据错误,请检查网络!");
				}else {
					//判断是否已经购买
					if (!Boolean.valueOf(isok)) {//未购买
						ShowUtils.showDiaLog(context, "温馨提醒", "此课程您还没有购买,请先购买再学习.祝您学有所成!", new ConfirmBackCall() {
							@Override
							public void confirmOperation() {
								//跳转到购买课程界面
								Intent intent = new Intent(context, MyShoppingTrolleyActivity.class);
								SharedPreferencesUtil.getInstance().editPutString("ShoopingCourseId", courseId);
								startActivity(intent);
							}
						});
					}else {//已购买
						UserStateUtil.loginInFailuer(context,new FailureCallBack() {
							
							@Override
							public void onFailureCallBack() {
								ShowUtils.showDiaLog(context, "温馨提示", "用户登录已经过期,需要您重新登录", new ConfirmBackCall() {
									
									@Override
									public void confirmOperation() {
										//跳转到登录界面
										Intent intent = new Intent(context, LoginActivity.class);
										context.startActivity(intent);
									}
								});
							}
						}, new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								//进入课程学习界面
								Intent intent = new Intent(context, CourseStudyActivity.class);
								intent.putExtra("courseId", courseId);
								intent.putExtra("courseName", courseName);
								intent.putExtra("courseLogo", courseLogo);
								intent.putExtra("iscollected", iscollected);
								intent.putExtra("teacherName", teacherName);
								intent.putExtra("lessontimes", lessontimes);
								startActivityForResult(intent, REQUEST_CODE);
							}
						});
					}
				}
			}
			break;
		case R.id.bt_represent://我要代言
			//判断是否已经登录
			if (!UserStateUtil.isLogined()) {//未登录
				if (oks != null) {
					oks.show(context);
				}
			}else {//已登录
				//判断是否已经购买
				if (!Boolean.valueOf(isok)) {//未购买
					if (oks != null) {
						oks.show(context);
					}
				}else {//已购买

					showPopWin();//弹出选项：分享二维码，直接分享课程

				}
			}
			break;
		}
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

	private Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if ("true".equals(mCourseIntroduceBean.getSuccess())) {
					
					String videoId = mCourseIntroduceBean.getEntity().getCourse().getFreeurl();
					courseName = mCourseIntroduceBean.getEntity().getCourse().getName();
					courseLogo = mCourseIntroduceBean.getEntity().getCourse().getCourseLogo();
					teacherName = mCourseIntroduceBean.getEntity().getCourse().getTeacherList().get(0).getName();
					lessontimes = mCourseIntroduceBean.getEntity().getCourse().getLessontimes();
					String videotype = mCourseIntroduceBean.getEntity().getCourse().getVideotype();
					SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", videoId);
					SharedPreferencesUtil.getInstance().editPutString("kpointId", "");
					SharedPreferencesUtil.getInstance().editPutString("courseId", courseId);
					SharedPreferencesUtil.getInstance().editPutString("videoType", videotype);
					SharedPreferencesUtil.getInstance().editPutString("secretUid", mCourseIntroduceBean.getEntity().getSecretUid());//把分享用户的id存储起来
					initVideoData(courseName,courseLogo,false);
					autoPlay();
					iscollected = mCourseIntroduceBean.getEntity().getIscollected();
					if (Boolean.valueOf(iscollected)) {
						setRightButton(R.drawable.iconfont_shoucang,null);
					}else {
						setRightButton(R.drawable.left_collect,null);
					}
					isok = mCourseIntroduceBean.getEntity().getIsok();
					oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_COURSE,courseId,courseName,courseLogo,Boolean.valueOf(isok),null,"课程: "+courseName+
							"\n导师: "+teacherName+
							"\n时间: "+lessontimes);
					
					initTabsViewPager();

					if (loadingDialog != null && loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
				}else {
					ShowUtils.showMsg(CourseIntroduceActivity.this, mCourseIntroduceBean.getMessage());
				}
				break;
			}
		};
	};

	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferencesUtil.getInstance().editPutString("kpointId", "");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferencesUtil.getInstance().editPutString("courseId", "");
	}

	private String courseName,courseLogo,teacherName,lessontimes;
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (isCCPlay) {
//			if (mCcMediaPlay!=null) {
//				mCcMediaPlay.dispatchKeyEvent(event);
//			}
//		}
//		return super.dispatchKeyEvent(event);
//	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (isCCPlay && mCcMediaPlay!=null) {
//			mCcMediaPlay.onTouchEvent(event);
//			return true;
//		}
//		return super.onTouchEvent(event);
//	}

	
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if (loadingDialog != null && loadingDialog.isShowing()) {
				loadingDialog.dismiss();
				return false;
			} else {
    			return super.onKeyDown(keyCode, event); 
    		}
        }else {
            return super.onKeyDown(keyCode, event); 
        } 
    } 
	@Override
	public void onMyCoursePlaySuccessCallBack() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public View initFragmentBottomView() {
		// TODO Auto-generated method stub
		return null;
	}

	private OnekeyShare oks;
	private PopupWindow pop;
	private void showPopWin() {
		if (pop == null) {
			View view = View.inflate(context, R.layout.layout_represent_popwindow, null);
			pop = new PopupWindow(view,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			pop.setFocusable(true); // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
			pop.setTouchable(true);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0x1e000000);
			pop.setBackgroundDrawable(dw);
			// 设置popWindow的显示和消失动画
			pop.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 在底部显示
			pop.setTouchInterceptor(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
					// 这里如果返回true的话，touch事件将被拦截
					// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
				}
			});

			// 点击事件监听
			view.findViewById(R.id.ll_qrcode).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转到分享二维码界面

					Intent intent = new Intent(context, ShareQRCodeActivity.class);
					intent.putExtra("courseId", courseId);
					intent.putExtra("type", 1);
					intent.putExtra("courseImg", courseLogo);
					startActivity(intent);
					pop.dismiss();
				}
			});
			view.findViewById(R.id.ll_noqrcode).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转到直接分享课程
					if (StringUtil.isEmpty(courseId) || StringUtil.isEmpty(courseName) || StringUtil.isEmpty(courseLogo)) {
						ShowUtils.showMsg(context, "数据获取失败,请检查您的网络!");
					}else {
						//进入到课程代言界面
						Intent intent = new Intent(context, CourseRepresentActivity.class);
						intent.putExtra("courseId", courseId);
						intent.putExtra("courseName", courseName);
						intent.putExtra("courseLogo", courseLogo);
						intent.putExtra("teacherName", teacherName);
						intent.putExtra("lessontimes", lessontimes);
						startActivity(intent);
					}
					pop.dismiss();
				}
			});
		}
		// 设置好参数之后再show
		pop.showAtLocation(this.findViewById(R.id.bt_study), Gravity.BOTTOM, 0, 0);
	}

}