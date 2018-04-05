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
/**
 * 设置QQ的界面
 * @author Administrator
 *
 */
public class SetQqActivity extends TitleBaseActivity {
	public static final int RESULT_SETQQ = 4;
	private String oldQq;
	private String newQq = "";
	private String oldIsqqopen;
	private String newIsqqopen = "1";
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
		setTitleText("QQ");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("qq", newQq);
				intent.putExtra("isqqopen", newIsqqopen);
				setResult(RESULT_SETQQ, intent);
				finish();
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!oldQq.equals(newQq) || !newIsqqopen.equals(oldIsqqopen))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetQqActivity.this.finish();
						}
					});
				else
					SetQqActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		oldQq = getIntent().getStringExtra("qq");
		oldIsqqopen = getIntent().getStringExtra("isqqopen");
		
		View view = View.inflate(context, R.layout.activity_setqq, null);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		editText = (EditText) view.findViewById(R.id.ed_qq);
		if (!StringUtil.isEmpty(oldQq)) {
			newQq = oldQq;
			editText.setText(oldQq);
		}else {
			oldQq = "";
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
					newQq = temp.toString().trim();
				}else {
					newQq = "";
				}
			}
		});
//		rg_jurisdiction = (RadioGroup) view.findViewById(R.id.rg_jurisdiction);
		rb_friend = (RadioButton) view.findViewById(R.id.rb_friend);
		rb_all = (RadioButton) view.findViewById(R.id.rb_all);
		if (!StringUtil.isEmpty(oldIsqqopen)) {
			newIsqqopen = oldIsqqopen;
			if ("1".equals(oldIsqqopen)) {
				rb_friend.setChecked(true);
				rb_all.setChecked(false);
			}else if ("0".equals(oldIsqqopen)) {
				rb_friend.setChecked(false);
				rb_all.setChecked(true);
			}
		}else {
			newIsqqopen = "1";
			rb_friend.setChecked(true);
			rb_all.setChecked(false);
		}
		rb_friend.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsqqopen = "1";
					rb_all.setChecked(false);
				}
			}
		});
		rb_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newIsqqopen = "0";
					rb_friend.setChecked(false);
				}
			}
		});
//		rg_jurisdiction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
//            @Override  
//            public void onCheckedChanged(RadioGroup group, int checkedId)  
//            {
//            	if (checkedId == rb_friend.getId()) {
//            		newIsqqopen = "1";
//				}else if (checkedId == rb_all.getId()) {
//					newIsqqopen = "0";
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
			if(SetQqActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetQqActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (!oldQq.equals(newQq) || !newIsqqopen.equals(oldIsqqopen)) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetQqActivity.this.finish();
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
