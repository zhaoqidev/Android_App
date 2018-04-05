package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
/**
 * 设置企业网址的界面
 * @author Administrator
 *
 */
public class SetCompanyWebActivity extends TitleBaseActivity {
	public static final int RESULT_SETCOMPANYWEB = 20;
	private String oldCompanyWeb;
	private String newCompanyWeb = "";
	private String iswebsiteopen = "0";//0表示公开,1表示未公开
	private String oldWebseteOpen;
	private EditText ed_companyweb;
	private CheckBox cb_choice;
//	private TextView tv_choose;
	private LinearLayout ll_default;
	private String currentPosition;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("企业网站");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				newCompanyWeb = ed_companyweb.getText().toString().trim();
				if (!StringUtil.isEmpty(newCompanyWeb)) {
					intent.putExtra("companyWeb", newCompanyWeb);
					intent.putExtra("iswebsiteopen", iswebsiteopen);
					intent.putExtra("currentPosition", currentPosition);
					setResult(RESULT_SETCOMPANYWEB, intent);
					finish();
				}
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!oldCompanyWeb.equals(newCompanyWeb) || !iswebsiteopen.equals(oldWebseteOpen))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetCompanyWebActivity.this.finish();
						}
					});
				else
				SetCompanyWebActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		oldCompanyWeb = getIntent().getStringExtra("companyWeb");
		currentPosition = getIntent().getStringExtra("currentPosition");
		oldWebseteOpen = getIntent().getStringExtra("iswebsiteopen");
		if (StringUtil.isEmpty(oldWebseteOpen)) {
			iswebsiteopen = "0";
		}else {
			iswebsiteopen = oldWebseteOpen;
		}
		
		View view = View.inflate(context, R.layout.activity_setcompanyweb, null);
		ed_companyweb = (EditText) view.findViewById(R.id.ed_companyweb);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		if (!StringUtil.isEmpty(oldCompanyWeb)) {
			ed_companyweb.setText(oldCompanyWeb);
		}else {
			oldCompanyWeb = "";
		}
		cb_choice = (CheckBox) view.findViewById(R.id.cb_choice);
//		tv_choose = (TextView) view.findViewById(R.id.tv_choose);
		if ("0".equals(iswebsiteopen)) {
			cb_choice.setChecked(true);
//			tv_choose.setText("是");
		}else {
			cb_choice.setChecked(false);
//			tv_choose.setText("否");
		}
		cb_choice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
//					tv_choose.setText("是");
					iswebsiteopen = "0";
				}else {
//					tv_choose.setText("否");
					iswebsiteopen = "1";
				}
			}
		});
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_default.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_default:
			if(SetCompanyWebActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetCompanyWebActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (!oldCompanyWeb.equals(newCompanyWeb) || !iswebsiteopen.equals(oldWebseteOpen)){
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetCompanyWebActivity.this.finish();
					}
				});
				return false;
			}else {
				return super.onKeyDown(keyCode, event);
			}
	    }else {
	    	return super.onKeyDown(keyCode, event); 
		}
	}
}
