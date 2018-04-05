package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
/**
 * 设置名称/性别/婚否的界面
 * @author Administrator
 *
 */
public class SetNameActivity extends TitleBaseActivity {
	public static final int RESULT_SETNAME = 1;
	private RadioGroup rg_sex;
	private String oldSex;
	private String newSex = "";
	private RadioButton rb_man;
	private RadioButton rb_woman;
	private RadioGroup rg_marital;
	private String oldMarital;
	private String newMarital = "";
	private RadioButton rb_yes;
	private RadioButton rb_no;
	private RadioButton rb_secret;
	private String oldName;
	private EditText editText;
	private LinearLayout ll_default;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("用户名称");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (StringUtil.isEmpty(editText.getText().toString().trim())) {
					ShowUtils.showMsg(context, "用户名不能为空!");
				} else {
					Intent intent = new Intent();
					intent.putExtra("name", editText.getText().toString().trim());
					intent.putExtra("sex", newSex);
					intent.putExtra("married", newMarital);
					setResult(RESULT_SETNAME, intent);
					finish();
//					if (ValidateUtil.checkNameChese(editText.getText().toString().trim())) {
//					}else {
//						ShowUtils.showMsg(context, "用户名不能为非中文!");
//					}
				}
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!newMarital.equals(oldMarital) || !newSex.equals(oldSex) || !editText.getText().toString().trim().equals(oldName))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetNameActivity.this.finish();
						}
					});
				else
					SetNameActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		oldName = getIntent().getStringExtra("name");
		oldSex = getIntent().getStringExtra("sex");
		oldMarital = getIntent().getStringExtra("married");
//		gender	性别（1男 2女）
//		married	婚否（0：未婚 1：已婚 2：就不告诉你）
		View view = View.inflate(context, R.layout.activity_setname, null);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		editText = (EditText) view.findViewById(R.id.ed_name);
		if (!StringUtil.isEmpty(oldName)) {
			editText.setText(oldName);
		}else {
			oldName = "";
		}
		rg_sex = (RadioGroup) view.findViewById(R.id.rg_sex);
		rb_man = (RadioButton) view.findViewById(R.id.rb_man);
		rb_woman = (RadioButton) view.findViewById(R.id.rb_woman);
		if (!StringUtil.isEmpty(oldSex)) {
			if ("1".equals(oldSex)) {
				rb_man.setChecked(true);
			}else if ("2".equals(oldSex)) {
				rb_woman.setChecked(true);
			}else {
				newSex = "1";
			}
		}
		rg_marital = (RadioGroup) view.findViewById(R.id.rg_marital);
		rb_yes = (RadioButton) view.findViewById(R.id.rb_yes);
		rb_no = (RadioButton) view.findViewById(R.id.rb_no);
		rb_secret = (RadioButton) view.findViewById(R.id.rb_secret);
		if (!StringUtil.isEmpty(oldMarital)) {
			if ("1".equals(oldMarital)) {
				rb_yes.setChecked(true);
			}else if ("0".equals(oldMarital)) {
				rb_no.setChecked(true);
			}else if ("2".equals(oldMarital)) {
				rb_secret.setChecked(true);
			}else {
				newMarital = "2";
			}
		}
		
		rg_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId)  
            {
            	if (checkedId == rb_man.getId()) {
            		newSex = "1";
				}else if (checkedId == rb_woman.getId()) {
					newSex = "2";
				}
            }  
        });
		rg_marital.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
            @Override  
            public void onCheckedChanged(RadioGroup group, int checkedId)  
            {
            	if (checkedId == rb_yes.getId()) {
            		newMarital = "1";
				}else if (checkedId == rb_no.getId()) {
					newMarital = "0";
				}else if (checkedId == rb_secret.getId()) {
	            	newMarital = "2";
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
			if(SetNameActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetNameActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (!newMarital.equals(oldMarital) || !newSex.equals(oldSex) || !oldName.equals(editText.getText().toString().trim())) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetNameActivity.this.finish();
					}
				});
				return false;
			}else {
				return false;
			}
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
}
