package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.FindPasswordBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.ValidateUtil;

/**
 * 找回密码
 * 
 * @author Administrator
 * 
 */
public class FindPasswordActivity extends TitleBaseActivity {
	private EditText et_find_number;// 输入的手机号
	private Button btn_findpassword;// 查询密码按钮
	private EditText et_findpwd_authcode;//找回密码界面的验证码
	private Button btn_findpwd_code;//发送验证码按钮
	private LinearLayout ll_content;

	private String mobile;// 手机号
	private String authcode;//验证码

	private RequestVo requestVo;
	private DataCallBack<FindPasswordBean> dataCallBack;
	
	private Dialog loadingDialog;// 正在加载
	
	int i=60;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("找回密码");
	}


	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_findpassword, null);
		et_find_number = (EditText) view.findViewById(R.id.et_find_number);
		btn_findpassword = (Button) view.findViewById(R.id.btn_findpassword);
		btn_findpwd_code = (Button) view.findViewById(R.id.btn_findpwd_code);
		et_findpwd_authcode = (EditText) view.findViewById(R.id.et_findpwd_authcode);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		
		et_find_number.addTextChangedListener(watcher);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		btn_findpassword.setOnClickListener(this);
		btn_findpwd_code.setOnClickListener(this);
		ll_content.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_findpwd_code:
			mobile = et_find_number.getText().toString().trim();
			// 1.检查网络是否可用
			boolean available = NetUtil.hasConnectedNetwork(getApplicationContext());
			if (!available) {
				ShowUtils.showMsg(FindPasswordActivity.this, "网络不可用,请检查网络");
				return;
			}
			if (StringUtil.isEmpty(mobile)) {
				ShowUtils.showMsg(FindPasswordActivity.this, "输入的手机号码不能为空");
				return;
			}
			// 2.通过规则判断手机号
			if (!ValidateUtil.isMobile(mobile)) {
				Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
				return;
			} else {
				getSMS();
				getDataServer(requestVo, dataCallBack);
				btn_findpwd_code.setClickable(false);
				btn_findpwd_code.setText("重新发送(" + i + ")");
				if(mc==null){
					mc = new MyCount(60000, 1000);  
				}
				mc.start();
			}
			
			break;
		case R.id.btn_findpassword:
			authcode=et_findpwd_authcode.getText().toString().trim();
			if (StringUtil.isEmpty(mobile)){
				ShowUtils.showMsg(FindPasswordActivity.this, "手机号码不能为空");
				return;
				}
			if (StringUtil.isEmpty(authcode)){
				ShowUtils.showMsg(FindPasswordActivity.this, "验证码不能为空");
				return;
			}else{
				getRequestVo();
				getDataCallBack();
				getDataServer(requestVo, dataCallBack);
				loadingDialog = ShowUtils.createLoadingDialog(context, true);
				loadingDialog.show();// 显示正在加载的布局
			}
			break;
		case R.id.ll_content:
			if(FindPasswordActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(FindPasswordActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		default:
			break;
		}

	}

	protected void getRequestVo() {
		requestVo = new RequestVo(ConstantsOnline.FIND_URL, context,
				ParamsMapUtil.getFindPwd(context, mobile,authcode), new MyBaseParser<>(
						FindPasswordBean.class));
	}

	protected void getDataCallBack() {
		dataCallBack = new DataCallBack<FindPasswordBean>() {
			@Override
			public void processData(FindPasswordBean findPwd) {
				if (findPwd != null) {
					// 判断返回值是true还是false
					if ("true".equals(findPwd.success)) {
						Intent intent = new Intent();
						intent.putExtra("mobile", mobile);
						// 从此ctivity传到另一Activity
						intent.setClass(FindPasswordActivity.this,ResetPasswordActivity.class);
						// 启动另一个Activity
						FindPasswordActivity.this.startActivity(intent);
						finish();
					} else {
						ShowUtils.showMsg(FindPasswordActivity.this, findPwd.message);
					}
				} else {
					ShowUtils.showMsg(FindPasswordActivity.this, "获取数据失败，请检查网络");
				}
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
			}
		};
	}
	
	/**
	 * 获取短信验证码的请求和回调
	 */
	protected void getSMS() {
		Map<String, String> getSms = ParamsMapUtil.sendSms(context, mobile,"y");
		requestVo = new RequestVo(ConstantsOnline.SEND_SMS, context, getSms,
				new MyBaseParser<>(FindPasswordBean.class));

		dataCallBack = new DataCallBack<FindPasswordBean>() {

			@Override
			public void processData(FindPasswordBean object) {
				if (object != null) {
					if ("true".equals(object.success)) {
						ShowUtils.showMsg(context, "验证码发送成功");
					} else {
						ShowUtils.showMsg(context, object.message);
					}
				} else {
					ShowUtils.showMsg(context, "获取数据失败，请检查网络");
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
        	btn_findpwd_code.setText("获取验证码");
        	btn_findpwd_code.setClickable(true);
			i=60;
        }     
        @Override     
        public void onTick(long millisUntilFinished) {
        	i--;
        	btn_findpwd_code.setText("重新发送(" + i + ")");
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
				if (mobile!=null&&(!temp.toString().equals(mobile))&&(mc!=null)) {
					mc.cancel();
					btn_findpwd_code.setText("获取验证码");
					btn_findpwd_code.setClickable(true);
					i = 60;
				}
			}
		}
	};
	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
