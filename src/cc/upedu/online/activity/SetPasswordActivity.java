package cc.upedu.online.activity;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.ResetPasswordBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;

/**
 * 设置界面中设置密码页面
 * 
 * @author Administrator
 * 
 */
public class SetPasswordActivity extends TitleBaseActivity {
	//旧密码,新密码,确认新密码
	EditText et_oldpwd, et_newpwd, et_reset_confrimpwd;
	Button btn_resetpwd;// 重置密码按钮
	String userId;//上个页面传递过来的手机号
	
	private RequestVo requestVo;
	private DataCallBack<ResetPasswordBean> dataCallBack;
	//旧密码,新密码,确认新密码
	private String oldPwd,newPwd,confrimPwd;
	private LinearLayout ll_default;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("修改密码");
	}


	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_setpassword, null);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		et_oldpwd = (EditText) view.findViewById(R.id.et_oldpwd);
		et_newpwd = (EditText) view.findViewById(R.id.et_newpwd);
		et_reset_confrimpwd = (EditText) view.findViewById(R.id.et_reset_confrimpwd);
		btn_resetpwd = (Button) view.findViewById(R.id.btn_resetpwd);
		userId=getIntent().getStringExtra("userId");
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_default.setOnClickListener(this);
		btn_resetpwd.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_resetpwd:
			oldPwd = et_oldpwd.getText().toString().trim();
			newPwd = et_newpwd.getText().toString().trim();
			confrimPwd = et_reset_confrimpwd.getText().toString().trim();
			
			if(!(oldPwd.length() >= 6 && oldPwd.length() <= 20)) {
				ShowUtils.showMsg(SetPasswordActivity.this, "密码长度在6至20个字符");
				return;
			}else if(!oldPwd.equals(SharedPreferencesUtil.getInstance().spGetString("passWord"))) {
				ShowUtils.showMsg(SetPasswordActivity.this, "您输入的旧密码错误");
				return;
			}else if(!(newPwd.length() >= 6 && newPwd.length() <= 20)) {
				ShowUtils.showMsg(SetPasswordActivity.this, "新密码长度在6至20个字符");
				return;
			}else if(StringUtil.isEmpty(newPwd) || StringUtil.isEmpty(confrimPwd)){
				ShowUtils.showMsg(SetPasswordActivity.this, "新密码输入内容不能为空");
				return;
			}else if(!(newPwd.equals(confrimPwd))){
				ShowUtils.showMsg(SetPasswordActivity.this, "新密码两次输入不一致，请您重新输入");
				return;
			}else{
				getRequestVo();
				getDataCallBack();
				getDataServer(requestVo, dataCallBack);
			}
			
			break;
		case R.id.ll_default:
			if(SetPasswordActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetPasswordActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		default:
			break;
		}
	}
	
	protected void getRequestVo() {
		requestVo = new RequestVo(ConstantsOnline.RESET_URL, 
				context,
				ParamsMapUtil.alterPassword(context, userId, newPwd),
				new MyBaseParser<>(ResetPasswordBean.class));
	}
	
	protected void getDataCallBack() {
		dataCallBack = new DataCallBack<ResetPasswordBean>() {
			@Override
			public void processData(ResetPasswordBean resetpwd) {
				if (resetpwd != null) {
					// 判断返回值是true还是false
					if ("true".equals(resetpwd.success)) {
						ShowUtils.showMsg(SetPasswordActivity.this,"恭喜您密码修改成功");
						SharedPreferencesUtil.getInstance().editPutString("passWord", newPwd);
//						Intent intent = new Intent(SetPasswordActivity.this,LoginActivity.class);
//						startActivity(intent);
						finish();
					} else {
						ShowUtils.showMsg(SetPasswordActivity.this, resetpwd.message);
					}
				} else {
					ShowUtils.showMsg(SetPasswordActivity.this, "获取数据失败，请检查网络");
				}
			}
		};
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

}
