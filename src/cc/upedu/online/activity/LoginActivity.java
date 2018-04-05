package cc.upedu.online.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;

import cc.upedu.online.MainActivity;
import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.LoginBean;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.service.GetTokenService;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.ValidateUtil;

/**
 * 用户登陆界面
 * 
 * @author Administrator
 * 
 */
public class LoginActivity extends TitleBaseActivity {

	private EditText etuser, etpassword;// 输入的用户名，密码
	private TextView tv_findpwd;// 返回和注册按钮,找回密码
	private Button btn_login;// 登陆
	private CheckBox cb_autologin;// 勾选自动登陆
	private String nameString;// 用户名
	private String paswString;// 密码
	private ProgressDialog dialog;// 进度框
	private LinearLayout ll_content;
	private RequestVo requestVo;
	private DataCallBack<LoginBean> dataCallBack;

	public String brand;// 品牌
	public String type;// 型号
	public String size;// 屏幕尺寸
	
	public String courseId,shareUid;//分享的课程Id和分享人的Id
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("登录");
		setRightText("注册", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 注册
				startActivity(new Intent(context, RegistActivity.class));
				// LoginActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_login, null);
		etuser = (EditText) view.findViewById(R.id.etuser);
		etpassword = (EditText) view.findViewById(R.id.etpassword);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		tv_findpwd = (TextView) view.findViewById(R.id.tv_findpwd);
		cb_autologin = (CheckBox) view.findViewById(R.id.cb_autologin);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		dialog = new ProgressDialog(LoginActivity.this);

		brand = StringUtil.getPhoneBrand();
		type = StringUtil.getPhoneType();
		size =StringUtil.getScreenSize(LoginActivity.this);
		
		courseId=SharedPreferencesUtil.getInstance().spGetString("share_courseId");
		shareUid=SharedPreferencesUtil.getInstance().spGetString("share_shareUid");

		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		btn_login.setOnClickListener(this);
		tv_findpwd.setOnClickListener(this);
		ll_content.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_login:// 登陆
			isNetwork();
			nameString = etuser.getText().toString();
			paswString = etpassword.getText().toString();
			if (StringUtil.isEmpty(nameString)) {
				ShowUtils.showMsg(LoginActivity.this, "用户名不能为空");
				return;
			}
			if (StringUtil.isEmpty(paswString)) {
				ShowUtils.showMsg(LoginActivity.this, "密码不能为空");
				return;
			}
			if (6 > paswString.length() && paswString.length() > 20) {
				ShowUtils.showMsg(LoginActivity.this, "密码长度只能为6至20位之间");
				return;
			}
			if (ValidateUtil.isMobile(nameString) == false) {
				ShowUtils.showMsg(LoginActivity.this, "请输入正确的手机号");
				return;
			}

			boolean available = NetUtil.hasConnectedNetwork(LoginActivity.this);// 判断网络是否可用
			if (available) {
				ShowUtils.showProgressDialog(dialog);// 显示进度条
				try {

					getRequestVo();
					getDataCallBack();
					getDataServer(requestVo, dataCallBack);

					ShowUtils.exitProgressDialog(dialog);// 隐藏进度条
				} catch (Exception e) {
				}
			} else {
				ShowUtils.exitProgressDialog(dialog);
				ShowUtils.showMsg(getApplicationContext(), "网络不可用,请检查网络");
			}
			break;
		case R.id.tv_findpwd:
			startActivity(new Intent(LoginActivity.this,
					FindPasswordActivity.class));
			// LoginActivity.this.finish();
			break;
		case R.id.ll_content:
			if(LoginActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(LoginActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;

		default:
			break;
		}
	}

	public void isNetwork() {
		boolean flag = NetUtil.hasConnectedNetwork(LoginActivity.this);
		if (flag == false) {
			ShowUtils.showMsg(LoginActivity.this, "网络不可用，请检查网络设置..");
			return;
		}
	}

	protected void getRequestVo() {
		requestVo = new RequestVo(ConstantsOnline.LOGIN_URL, context,
				ParamsMapUtil.getLoginInfo(context, nameString, paswString,
						brand, type, size,"Android",courseId,shareUid), new MyBaseParser<>(LoginBean.class));
	}

	protected void getDataCallBack() {
		dataCallBack = new DataCallBack<LoginBean>() {

			@Override
			public void processData(LoginBean login) {
				if (login!=null) {
					String message = login.message;
					// 判断返回值是true还是false
					if ("true".equals(login.success)) {
						// 把服务器返回信息添加到sharedPerference中。
						SharedPreferencesUtil.getInstance().editPutBoolean("isLogin", true);// 设置登陆状态为true
						SharedPreferencesUtil.getInstance().editPutString("name", login.entity.name);
						SharedPreferencesUtil.getInstance().editPutString("mobile", login.entity.mobile);
						SharedPreferencesUtil.getInstance().editPutString("userType", login.entity.userType);
						SharedPreferencesUtil.getInstance().editPutString("userId", login.entity.id);
						SharedPreferencesUtil.getInstance().editPutString("passWord", paswString);
						SharedPreferencesUtil.getInstance().editPutString("avatar", login.entity.avatar);
						SharedPreferencesUtil.getInstance().editPutString("userInfo", login.entity.userInfo);// 个性签名
						SharedPreferencesUtil.getInstance().editPutString("loginsid", login.entity.loginsid);// 用户识别码
						
						
						PreferencesObjectUtil.saveObject(login.entity.bannerList, "bannerList", context);
						PreferencesObjectUtil.saveObject(login.entity.hobbyList, "hobbyList", context);
						PreferencesObjectUtil.saveObject(login.entity.industryList, "industryList", context);
						PreferencesObjectUtil.saveObject(login.entity.positionList, "positionList", context);
						//用于视频播放节点的记录
						if (login.entity.id.equals(PreferencesObjectUtil.spGetString("oldUserId",context))){
							if (!PreferencesObjectUtil.containsObject("videoPlayNode",context))
								PreferencesObjectUtil.saveObject(new HashMap<String,Integer>(),"videoPlayNode",context);
						}else {
							PreferencesObjectUtil.editPutString("oldUserId",login.entity.id,context);
							PreferencesObjectUtil.saveObject(new HashMap<String,Integer>(),"videoPlayNode",context);
						}

						ShowUtils.showMsg(LoginActivity.this, "登录成功");
						
						getToken();//登录成功，获取token

						// 跳转到主页面
						if (StringUtil.isEmpty(courseId)) {
							Intent intent = new Intent(LoginActivity.this,MainActivity.class);
							startActivity(intent);
						}else {
							Intent intent = new Intent(LoginActivity.this,CourseIntroduceActivity.class);
							intent.putExtra("courseId", courseId);
							startActivity(intent);
							SharedPreferencesUtil.getInstance().editPutString("share_courseId", null);
							SharedPreferencesUtil.getInstance().editPutString("share_shareUid", null);
						}
						
						finish();

					} else {
						SharedPreferencesUtil.getInstance().editPutBoolean("isLogin", false);// 设置登陆状态为false
						ShowUtils.showMsg(LoginActivity.this, message);
					}
				}else {
					ShowUtils.showMsg(LoginActivity.this, "获取数据失败，请稍后重试");
				}
				
				// 判断是否自动登陆
				if (cb_autologin.isChecked()) {
					SharedPreferencesUtil.getInstance().editPutBoolean("isAutoLogin", true);
				} else {
					SharedPreferencesUtil.getInstance().editPutBoolean("isAutoLogin", false);
				}
			}
		};
	}
	
	/**
	 * 开启获取token的服务
	 */
	private void getToken(){
		Intent intent =new Intent(context, GetTokenService.class);
		intent.putExtra("userId", UserStateUtil.getUserId());
		context.startService(intent);
	}

	/**
	 * 获取手机屏幕的宽高
	 */
	public String getScreen() {
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		return String.valueOf(width) + "*" + String.valueOf(height);
	}

	/**
	 * 获取厂商 device factory name, e.g: Samsung
	 * 
	 * @return the vENDOR
	 */
	public String getVendor() {
		return Build.BRAND;
	}

	/**
	 * 获取手机型号 device model name, e.g: GT-I9100
	 * 
	 * @return the user_Agent
	 */
	public String getDevice() {
		return Build.MODEL;
	}
	@Override
	protected void initData() {
		SharedPreferencesUtil.getInstance().editPutString("brand", brand);// 品牌
		SharedPreferencesUtil.getInstance().editPutString("type", type);// 型号
		SharedPreferencesUtil.getInstance().editPutString("size", size);// 屏幕尺寸

	}
}
