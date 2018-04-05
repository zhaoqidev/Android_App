package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cn.sharesdk.onekeyshare.OnekeyShare;
/**
 * 课程代言的主界面
 * @author Administrator
 *
 */
public class CourseRepresentActivity extends TitleBaseActivity {
	private EditText represent_content;
	private Button represent_share;
//	private LinearLayout ll_top;
	private LinearLayout ll_content;
	private String courseId,courseName,courseLogo,teacherName,lessontimes;
	
	/**
	 * 
	 */
	private void checkoutRepresent() {
		if (!UserStateUtil.isLogined()) {
			ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录.", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					//跳转到登录界面
					Intent intent = new Intent(context, LoginActivity.class);
					startActivity(intent);
				}
			});
		}else {
			String userId = UserStateUtil.getUserId();
			String assess = represent_content.getText().toString().trim();
			if (!StringUtil.isEmpty(assess)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("courseId", courseId);
				map.put("userId", userId);
				map.put("assess", assess);
				UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.SHARE_COURSE,map , new UploadDataCallBack() {

					@Override
					public void onUploadDataSuccess() {
						// TODO Auto-generated method stub
						if (oks != null) {
							oks.show(context);
						}else {
							String doc = "课程: "+courseName+
									"\n导师: "+teacherName+
									"\n时间: "+lessontimes;
//							if (isQRCode) {
//								oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_COURSE,courseId,courseName,courseLogo,true,qrCodePath,doc);
//							}else {
								oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_COURSE,courseId,courseName,courseLogo,true,null,doc);
//							}
							oks.show(context);
						}
						represent_content.setText("");
					}

					@Override
					public void onUploadDataFailure() {
						// TODO Auto-generated method stub
						
					}
				});
			}else {
				ShowUtils.showMsg(context, "您还没有书写你的学习感悟!");
			}
		}
	}
	private OnekeyShare oks;

	/*
	 * 注释掉的代码是在这个界面中可以选择分享二维码和课程详细信息，现在把二维码分享注释掉了。
	 */
//	private boolean isQRCode = false;
//	private String qrCodePath;
//	private PopupWindow pop;
	/*private void showPopWin() {
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
			pop.setTouchInterceptor(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
					// 这里如果返回true的话，touch事件将被拦截
					// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
				}
			});
			
			// 点击事件监听
			view.findViewById(R.id.ll_qrcode).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isQRCode = true;
					checkoutRepresent();
					pop.dismiss();
				}
			});
			view.findViewById(R.id.ll_noqrcode).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					isQRCode = false;
					checkoutRepresent();
					pop.dismiss();
				}
			});
		}
		// 设置好参数之后再show
		pop.showAtLocation(CourseRepresentActivity.this.findViewById(R.id.sv),Gravity.BOTTOM, 0, 0);
	}*/

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("课程分享");
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_course_represent, null);
//		ll_top = (LinearLayout) findViewById(R.id.ll_top);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		represent_content = (EditText) view.findViewById(R.id.represent_content);
		represent_share = (Button) view.findViewById(R.id.represent_share);
		courseId = getIntent().getStringExtra("courseId");
		courseName = getIntent().getStringExtra("courseName");
		courseLogo = getIntent().getStringExtra("courseLogo");
		teacherName = getIntent().getStringExtra("teacherName");
		lessontimes = getIntent().getStringExtra("lessontimes");
//		qrCodePath = new QRCodeUtil().createImage(ConstantsOnline.SHAR_COURSE + courseId + "?shareBy=" + SharedPreferencesUtil.getInstance().spGetString("secretUid"));
//		represent_content.setFocusable(false);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		represent_share.setOnClickListener(this);
		ll_content.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.represent_share:
//			showPopWin();
			checkoutRepresent();
			break;
		case R.id.ll_content:
			if(CourseRepresentActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(CourseRepresentActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
}
