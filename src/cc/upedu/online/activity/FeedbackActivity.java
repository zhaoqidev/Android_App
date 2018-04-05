package cc.upedu.online.activity;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;

public class FeedbackActivity extends TitleBaseActivity {
	private EditText test_components;
	private EditText contact_information;
	private Button bt_save;
	private String components;
	private String contact;
	private String userId;
	private LinearLayout ll_content;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("意见反馈");
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		userId = UserStateUtil.getUserId();

		View view = View.inflate(context, R.layout.activity_feedback, null);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		test_components = (EditText) view.findViewById(R.id.test_components);
		contact_information = (EditText) view.findViewById(R.id.contact_information);
		bt_save = (Button) view.findViewById(R.id.bt_save);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		bt_save.setOnClickListener(this);
		ll_content.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_save:
			components = test_components.getText().toString();
			contact = contact_information.getText().toString();
			if (components.equals("")) {
				Toast.makeText(this, "输入不能为空...", Toast.LENGTH_SHORT).show();
				return;
			} else {
				//判断是否有网络连接
				if(NetUtil.hasConnectedNetwork(context)){
					Map<String, String> map = new HashMap<String, String>();
					if (StringUtil.isEmpty(userId)) {
						if (StringUtil.isEmpty(contact)) {
							map.put("content", components);
						}else {
							map.put("content", components);
							map.put("contact", contact);
						}
					}else {
						if (StringUtil.isEmpty(contact)) {
							map.put("userId", userId);
							map.put("content", components);
						}else {
							map.put("userId", userId);
							map.put("content", components);
							map.put("contact", contact);
						}
					}
					UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.SAVE_FEEDBACK,map , new UploadDataCallBack() {

						@Override
						public void onUploadDataSuccess() {
							// TODO Auto-generated method stub
							ShowUtils.showMsg(context, "提交成功,感谢您的宝贵意见!");
							test_components.setText("");
							contact_information.setText("");
						}

						@Override
						public void onUploadDataFailure() {
							// TODO Auto-generated method stub
							
						}
					});
				}else{
					ShowUtils.showMsg(getApplicationContext(), "网络不可用!");
					return;
				}
			}
			break;
		case R.id.ll_content:
			if(FeedbackActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(FeedbackActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
}
