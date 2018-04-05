package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.ResetPasswordBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;

/**
 * 重置密码页面
 * 
 * @author Administrator
 * 
 */
public class ResetPasswordActivity extends TitleBaseActivity {
	EditText et_resetpwd;// 密码
	EditText et_reset_confrimpwd;// 确认密码
	Button btn_resetpwd;// 重置密码按钮
	String mobile;//上个页面传递过来的手机号
	private RequestVo requestVo;
	private DataCallBack<ResetPasswordBean> dataCallBack;
	private String newPwd;
	private String confrimPwd;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("重置密码");
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		mobile=getIntent().getStringExtra("mobile");
		View view = View.inflate(context, R.layout.activity_resetpassword, null);
		et_resetpwd = (EditText) view.findViewById(R.id.et_resetpwd);
		et_reset_confrimpwd = (EditText) view.findViewById(R.id.et_reset_confrimpwd);
		btn_resetpwd = (Button) view.findViewById(R.id.btn_resetpwd);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		btn_resetpwd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btn_resetpwd:
			newPwd = et_resetpwd.getText().toString().trim();
			confrimPwd = et_reset_confrimpwd.getText().toString().trim();
			
			if(!(newPwd.length() >= 6 && newPwd.length() <= 20)) {
				ShowUtils.showMsg(context, "密码长度在6至20个字符");
				return;
			}else if(StringUtil.isEmpty(newPwd) || StringUtil.isEmpty(confrimPwd)){
				ShowUtils.showMsg(context, "输入内容不能为空");
				return;
			}else if(!(newPwd.equals(confrimPwd))){
				ShowUtils.showMsg(context, "两次输入不一致，请您重新输入");
				return;
			}else{
				getRequestVo();
				getDataCallBack();
				getDataServer(requestVo, dataCallBack);
			}
			
			break;

		default:
			break;
		}

	}
	
	protected void getRequestVo() {
		requestVo = new RequestVo(ConstantsOnline.RESET_URL, 
				context,
				ParamsMapUtil.resetPassword(context, mobile, newPwd),
				new MyBaseParser<>(ResetPasswordBean.class));
	}
	
	protected void getDataCallBack() {
		dataCallBack = new DataCallBack<ResetPasswordBean>() {
			@Override
			public void processData(ResetPasswordBean resetpwd) {
				if (resetpwd != null) {
					String success = resetpwd.success;
					String message = resetpwd.message;
					// 判断返回值是true还是false
					if ("true".equals(success)) {
						ShowUtils.showMsg(context,"恭喜您密码修改成功,请重新登陆");
						Intent intent = new Intent(context,LoginActivity.class);
						startActivity(intent);
						finish();

					} else {
						ShowUtils.showMsg(context, message);
					}
				} else {
					ShowUtils.showMsg(context, "获取数据失败，请检查网络");
				}
			}
		};
	}


	@Override
	protected void initData() {
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
	}
}
