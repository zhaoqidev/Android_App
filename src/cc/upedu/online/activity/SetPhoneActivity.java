package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;

public class SetPhoneActivity extends TitleBaseActivity {
	public static final int RESULT_SETPHONE = 7;
	private String oldMobile;
//	private String newMobile = "";
	private String oldIsmobileopen;
	private String newIsmobileopen = "1";
	private TextView et_mobile;
//	private RadioGroup rg_jurisdiction;
	private RadioButton rb_friend;
	private RadioButton rb_all;
	private LinearLayout ll_default;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("手机");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent();
//	            intent.putExtra("mobile", newMobile);
				intent.putExtra("ismobileopen", newIsmobileopen);
				setResult(RESULT_SETPHONE, intent);
				finish();
			}
		});
		oldMobile = getIntent().getStringExtra("mobile");
		oldIsmobileopen = getIntent().getStringExtra("ismobileopen");
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (newIsmobileopen.equals(oldIsmobileopen))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetPhoneActivity.this.finish();
						}
					});
				else
					SetPhoneActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_setphone, null);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		et_mobile = (EditText) view.findViewById(R.id.ed_mobile);
		if (!StringUtil.isEmpty(oldMobile)) {
//			newMobile = oldMobile;
			et_mobile.setText(oldMobile);
		}
//		rg_jurisdiction = (RadioGroup) view.findViewById(R.id.rg_jurisdiction);
		rb_friend = (RadioButton) view.findViewById(R.id.rb_friend);
		rb_all = (RadioButton) view.findViewById(R.id.rb_all);
		if (!StringUtil.isEmpty(oldIsmobileopen)) {
			newIsmobileopen = oldIsmobileopen;
			if ("1".equals(oldIsmobileopen)) {
				rb_friend.setChecked(true);
				rb_all.setChecked(false);
			}else if ("0".equals(oldIsmobileopen)) {
				rb_friend.setChecked(false);
				rb_all.setChecked(true);
			}
		}else {
			rb_friend.setChecked(true);
			rb_all.setChecked(false);
		}
		rb_friend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsmobileopen = "1";
					rb_all.setChecked(false);
				}
			}
		});
		rb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsmobileopen = "0";
					rb_friend.setChecked(false);
				}
			}
		});
//		rg_jurisdiction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
//            @Override  
//            public void onCheckedChanged(RadioGroup group, int checkedId)  
//            {
//            	if (checkedId == rb_friend.getId()) {
//            		newIsmobileopen = "1";
//				}else if (checkedId == rb_all.getId()) {
//					newIsmobileopen = "0";
//				}
//            }  
//        });
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
			if(SetPhoneActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetPhoneActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (newIsmobileopen.equals(oldIsmobileopen)) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetPhoneActivity.this.finish();
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
