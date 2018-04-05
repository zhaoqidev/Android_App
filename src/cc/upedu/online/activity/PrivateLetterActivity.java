package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
/**
 * 发送消息，发送私信的界面
 * @author Administrator
 *
 */
public class PrivateLetterActivity extends TitleBaseActivity {
//	TextView tv_send;//发送按钮
	EditText et_content;//编辑框
	TextView tv_name;//收信者姓名

	private RequestVo requestVo;
	private DataCallBack<sendMessageBean> dataCallBack;

	String uid;
	String fid;
	String name;
	String content;
	private LinearLayout ll_content;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("发送消息");
		setRightText("发送", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				content=et_content.getText().toString().trim();//获取到输入的内容
				if(!StringUtil.isEmpty(content)){
					getRequestVo();
					getDataCallBack();
					getDataServer(requestVo, dataCallBack);
				}else{
					ShowUtils.showMsg(context, "输入内容不能为空");
				}
			}
		});
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_sendmessage, null);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
//		tv_send = (TextView) view.findViewById(R.id.tv_send);
		et_content=(EditText) view.findViewById(R.id.et_content);
		tv_name=(TextView) view.findViewById(R.id.tv_name);

		Intent intent = this.getIntent();
		uid = intent.getStringExtra("uid");
		fid = intent.getStringExtra("fid");
		name = intent.getStringExtra("name");
		tv_name.setText(name);
		return view;
	}
	@Override
	protected void initListener() {
		ll_content.setOnClickListener(this);
//		tv_send.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_content:
			if(PrivateLetterActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PrivateLetterActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		default:
			break;
		}

	}
	
	public void getRequestVo() {
		Map<String, String> setUserInfo = ParamsMapUtil.sendMessage(context,
				uid, fid, content);
		requestVo = new RequestVo(ConstantsOnline.SEND_MESSAGE, context,
				setUserInfo, new MyBaseParser<>(sendMessageBean.class));
	}
	public void getDataCallBack() {
		dataCallBack=new DataCallBack<sendMessageBean>() {

			@Override
			public void processData(sendMessageBean object) {
				ShowUtils.showMsg(context, object.message);
				if ("true".equals(object.success)) {
					PrivateLetterActivity.this.finish();//发表成功，关闭页面
				}else{
					//不成功保持原有页面不变
				}
				
			}
		};

	}


	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	public class sendMessageBean{
		public String message;
		public String success;
		
	}
}
