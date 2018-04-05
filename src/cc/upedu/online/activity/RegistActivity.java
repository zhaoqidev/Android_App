package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.MainActivity;
import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.RegistBean;
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
 * 注册页面
 * 
 * @author Administrator
 * 
 */
public class RegistActivity extends TitleBaseActivity {
	private EditText et_regist_name, et_regist_number, et_regist_authcode;// 输入的用户名，手机号，验证码
	private EditText et_pwd, et_regist_confirmpwd;// 输入的密码，确认密码
	private TextView tv_law;// 返回和成长在线协议
	private Button btn_regist, btn_authcode;// 注册
	private CheckBox cb_agree;// 勾选同意协议
	private String username;// 用户名
	private String pwd;// 密码
	private String repwd;// 确认密码
	private String number;// 手机号
	private String authCode;// 验证码

	public String brand;// 品牌
	public String type;// 型号
	public String size;// 屏幕尺寸
	public String courseId, shareUid;// 分享的课程Id和分享人的Id

	int i = 60;// 注册码发送后的倒计时,60秒后重发

	private RequestVo requestVo;
	private DataCallBack<RegistBean> dataCallBack;

	private Dialog loadingDialog;// 正在加载

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("用户注册");
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_regist, null);
		et_regist_name = (EditText) view.findViewById(R.id.et_regist_name);
		et_regist_number = (EditText) view.findViewById(R.id.et_regist_number);
		et_regist_authcode = (EditText) view.findViewById(R.id.et_regist_authcode);
		et_pwd = (EditText) view.findViewById(R.id.et_pwd);
		et_regist_confirmpwd = (EditText) view.findViewById(R.id.et_regist_confirmpwd);
		tv_law = (TextView) view.findViewById(R.id.tv_law);
		btn_regist = (Button) view.findViewById(R.id.btn_regist);
		btn_authcode = (Button) view.findViewById(R.id.btn_authcode);
		cb_agree = (CheckBox) view.findViewById(R.id.cb_agree);

		brand = StringUtil.getPhoneBrand();
		type = StringUtil.getPhoneType();
		size = StringUtil.getScreenSize(RegistActivity.this);
		courseId = SharedPreferencesUtil.getInstance().spGetString("share_courseId");
		shareUid = SharedPreferencesUtil.getInstance().spGetString("share_shareUid");

		et_regist_number.addTextChangedListener(watcher);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		btn_authcode.setOnClickListener(this);
		tv_law.setOnClickListener(this);
		btn_regist.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_authcode:// 发送验证码
			number = et_regist_number.getText().toString().trim();// 拿到用户输入的手机号

			// 1. 通过规则判断手机号
			if (!ValidateUtil.isMobile(number)) {
				Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
				return;
			}
			// 2. 通过sdk发送短信验证
			getSMS();
			getDataServer(requestVo, dataCallBack);

			// 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
			btn_authcode.setClickable(false);
			btn_authcode.setText("重新发送(" + i + ")");
			if(mc==null){
				mc = new MyCount(60000, 1000);  
			}
			mc.start();
			break;
		case R.id.tv_law:// 打开成长在线协议
			Intent intent = new Intent(RegistActivity.this,
					RegistLawActivity.class);
			startActivity(intent);

			break;
		case R.id.btn_regist:// 注册按钮
			// 1.检查网络是否可用
			boolean available = NetUtil.hasConnectedNetwork(getApplicationContext());
			if (!available) {
				ShowUtils.showMsg(RegistActivity.this, "网络不可用,请检查网络");
				return;
			}

			// 2.获取用户输入的数据
			username = et_regist_name.getText().toString().trim();
			number = et_regist_number.getText().toString().trim();
			authCode = et_regist_authcode.getText().toString().trim();
			pwd = et_pwd.getText().toString().trim();
			repwd = et_regist_confirmpwd.getText().toString().trim();

			// 3.对手机号码以及用户名校验
			boolean isphone = ValidateUtil.isMobile(number);
			boolean isChinese = ValidateUtil.checkNameChese(username);

			// 4.对输入数据进行判断
			if (StringUtil.isEmpty(number) || StringUtil.isEmpty(pwd)
					|| StringUtil.isEmpty(repwd)
					|| StringUtil.isEmpty(username)
					|| StringUtil.isEmpty(authCode)) {
				if (StringUtil.isEmpty(username)) {
					ShowUtils.showMsg(RegistActivity.this, "输入的真实姓名不能为空");
				} else if (StringUtil.isEmpty(number)) {
					ShowUtils.showMsg(RegistActivity.this, "输入的手机号码不能为空");
				} else if (StringUtil.isEmpty(pwd)) {
					ShowUtils.showMsg(RegistActivity.this, "输入的密码不能为空");
				} else if (StringUtil.isEmpty(repwd)) {
					ShowUtils.showMsg(RegistActivity.this, "输入的确认密码不能为空");
				} else {
					ShowUtils.showMsg(RegistActivity.this, "验证码不能为空");
				}
				return;
			}

			else if (!isChinese || !isphone || number.length() != 11) {
				if (!isChinese) {
					ShowUtils.showMsg(RegistActivity.this, "请输入真实的中文姓名");
				} else {
					ShowUtils.showMsg(RegistActivity.this, "不是正确的手机号");
				}
				return;
			} else if (!(pwd.length() >= 6 && pwd.length() <= 20)) {
				ShowUtils.showMsg(RegistActivity.this, "密码长度在6至20个字符");
				return;
			} else if (!pwd.equals(repwd)) {
				ShowUtils.showMsg(RegistActivity.this, "两次输入的密码不一致,请重新输入");
				return;
			} else if (!cb_agree.isChecked()) {
				ShowUtils.showMsg(RegistActivity.this, "不接受服务协议不能完成注册哦");
				return;
			} else {
				loadingDialog = ShowUtils.createLoadingDialog(context, true);
				loadingDialog.show();// 显示正在加载的布局

				getRequestVo();
				getDataCallBack();
				getDataServer(requestVo, dataCallBack);
			}
			break;
		default:
			break;
		}

	}


	private void getDataCallBack() {
		dataCallBack = new DataCallBack<RegistBean>() {

			@Override
			public void processData(RegistBean regist) {
				if (regist != null) {
					String message = regist.message;
					// 判断返回值是true还是false
					if ("true".equals(regist.success)) {
						// 把服务器返回信息添加到sharedPerference中。
						SharedPreferencesUtil.getInstance().editPutBoolean("isLogin", true);// 设置登陆状态为true
						SharedPreferencesUtil.getInstance().editPutString("name", regist.entity.name);
						SharedPreferencesUtil.getInstance().editPutString("mobile", regist.entity.mobile);
						SharedPreferencesUtil.getInstance().editPutString("userType", "0");
						SharedPreferencesUtil.getInstance().editPutString("userId", regist.entity.id);
						SharedPreferencesUtil.getInstance().editPutString("passWord", pwd);
						SharedPreferencesUtil.getInstance().editPutString("avatar", regist.entity.avatar);
						SharedPreferencesUtil.getInstance().editPutString("userInfo", regist.entity.userInfo)
								;// 个性签名
						SharedPreferencesUtil.getInstance().editPutString("loginsid", regist.entity.loginsid)
								;// 用户识别码

						PreferencesObjectUtil
								.saveObject(regist.entity.bannerList,
										"bannerList", context);
						PreferencesObjectUtil.saveObject(
								regist.entity.hobbyList, "hobbyList", context);
						PreferencesObjectUtil.saveObject(
								regist.entity.industryList, "industryList",
								context);
						PreferencesObjectUtil.saveObject(
								regist.entity.positionList, "positionList",
								context);
						//用于视频播放节点的记录
//						if (regist.entity.id.equals(PreferencesObjectUtil.spGetString("oldUserId",context))){
//							if (!PreferencesObjectUtil.containsObject("videoPlayNode",context))
//								PreferencesObjectUtil.saveObject(new HashMap<String,Integer>(),"videoPlayNode",context);
//						}else {
//						}
						PreferencesObjectUtil.editPutString("oldUserId",regist.entity.id,context);
						PreferencesObjectUtil.saveObject(new HashMap<String,Integer>(),"videoPlayNode",context);

						ShowUtils.showMsg(RegistActivity.this,
								"恭喜您注册成功,已经为您自动登录");

						getToken();// 登录成功，获取token

						if (StringUtil.isEmpty(courseId)) {
							Intent intent = new Intent(RegistActivity.this,
									MainActivity.class);
							startActivity(intent);
						} else {
							Intent intent = new Intent(RegistActivity.this,
									CourseIntroduceActivity.class);
							startActivity(intent);
							SharedPreferencesUtil.getInstance().editPutString("share_courseId", null);
							SharedPreferencesUtil.getInstance().editPutString("share_shareUid", null);
						}

						finish();
					} else {
						ShowUtils.showMsg(RegistActivity.this, message);
					}
				} else {
					ShowUtils.showMsg(RegistActivity.this, "获取数据失败，请检查网络");
				}

				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
			}

		};
	}

	/**
	 * 开启获取token的服务
	 */
	private void getToken() {
		Intent intent = new Intent(context, GetTokenService.class);
		intent.putExtra("userId", UserStateUtil.getUserId());
		context.startService(intent);
	}

	/**
	 * 注册请求
	 */
	protected void getRequestVo() {
		Map<String, String> setUserInfo = ParamsMapUtil.setUserInfo(context,
				number, username, pwd, brand, type, size, "Android", courseId,
				shareUid, authCode);
		requestVo = new RequestVo(ConstantsOnline.REGIST_URL, context,
				setUserInfo, new MyBaseParser<>(RegistBean.class));
	}

	/**
	 * 获取短信验证码的请求和回调
	 */
	protected void getSMS() {
		Map<String, String> getSms = ParamsMapUtil.sendSms(context, number,null);
		requestVo = new RequestVo(ConstantsOnline.SEND_SMS, context, getSms,
				new MyBaseParser<>(RegistBean.class));

		dataCallBack = new DataCallBack<RegistBean>() {

			@Override
			public void processData(RegistBean object) {
				if (object != null) {
					if ("true".equals(object.success)) {
						ShowUtils.showMsg(RegistActivity.this, "验证码发送成功");
					} else {
						ShowUtils.showMsg(RegistActivity.this, object.message);
						
					}
				} else {
					ShowUtils.showMsg(RegistActivity.this, "获取数据失败，请检查网络");
				}

			}
		};
	}

	MyCount mc;
	/*定义一个倒计时的内部类*/
    class MyCount extends CountDownTimer {     
        public MyCount(long millisInFuture, long countDownInterval) {     
            super(millisInFuture, countDownInterval);     
        }     
        @Override     
        public void onFinish() {
        	btn_authcode.setText("获取验证码");
			btn_authcode.setClickable(true);
			i=60;
        }     
        @Override     
        public void onTick(long millisUntilFinished) {
        	i--;
        	btn_authcode.setText("重新发送(" + i + ")");
        }    
    } 
    
    
	TextWatcher watcher = new TextWatcher() {
		private CharSequence temp;

		@Override
		public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
			temp=charsequence;

		}

		@Override
		public void beforeTextChanged(CharSequence charsequence, int i, int j,
				int k) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable editable) {
			if (temp.length()==11) {
				if (number!=null) {
					if (!temp.toString().equals(number)) {
						if (mc!=null) {
							mc.cancel();
							btn_authcode.setText("获取验证码");
							btn_authcode.setClickable(true);
							i = 60;
						}
					}
				}
			}
		}
	};

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}


}
