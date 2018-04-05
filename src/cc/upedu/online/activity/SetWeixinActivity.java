package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;

public class SetWeixinActivity extends TitleBaseActivity {
	public static final int RESULT_SETWEIXIN = 5;
	private String oldWeixin;
	private String newWeixin = "";
	private String oldIsweixinopen;
	private String newIsweixinopen = "1";
	private EditText editText;
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
		setTitleText("微信");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("weixin", newWeixin);
				intent.putExtra("isweixinopen", newIsweixinopen);
				setResult(RESULT_SETWEIXIN, intent);
				finish();
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!oldWeixin.equals(newWeixin) || !newIsweixinopen.equals(oldIsweixinopen))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetWeixinActivity.this.finish();
						}
					});
				else
					SetWeixinActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		oldWeixin = getIntent().getStringExtra("weixin");
		oldIsweixinopen = getIntent().getStringExtra("isweixinopen");
		
		View view = View.inflate(context, R.layout.activity_setweixin, null);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		editText = (EditText) view.findViewById(R.id.ed_weixin);
		if (!StringUtil.isEmpty(oldWeixin)) {
			newWeixin = oldWeixin;
			editText.setText(oldWeixin);
		}else {
			oldWeixin = "";
		}
		editText.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				temp = s;
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtil.isEmpty(temp.toString().trim())) {
					newWeixin = temp.toString().trim();
				}else {
					newWeixin = "";
				}
			}
		});
//		rg_jurisdiction = (RadioGroup) view.findViewById(R.id.rg_jurisdiction);
		rb_friend = (RadioButton) view.findViewById(R.id.rb_friend);
		rb_all = (RadioButton) view.findViewById(R.id.rb_all);
		if (!StringUtil.isEmpty(oldIsweixinopen)) {
			newIsweixinopen = oldIsweixinopen;
			if ("1".equals(oldIsweixinopen)) {
				rb_friend.setChecked(true);
				rb_all.setChecked(false);
			}else if ("0".equals(oldIsweixinopen)) {
				rb_all.setChecked(false);
				rb_friend.setChecked(true);
			}
		}else {
			newIsweixinopen = "1";
			rb_friend.setChecked(true);
			rb_all.setChecked(false);
		}
		rb_friend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsweixinopen = "1";
					rb_all.setChecked(false);
				}
			}
		});
		rb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsweixinopen = "0";
					rb_friend.setChecked(false);
				}
			}
		});
//		rg_jurisdiction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
//            @Override  
//            public void onCheckedChanged(RadioGroup group, int checkedId)  
//            {
//            	if (checkedId == rb_friend.getId()) {
//            		newIsweixinopen = "1";
//				}else if (checkedId == rb_all.getId()) {
//					newIsweixinopen = "0";
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
			if(SetWeixinActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetWeixinActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (!oldWeixin.equals(newWeixin) || !newIsweixinopen.equals(oldIsweixinopen)) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetWeixinActivity.this.finish();
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
