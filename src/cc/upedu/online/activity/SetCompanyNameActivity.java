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
 * 设置企业名称的界面
 * @author Administrator
 *
 */
public class SetCompanyNameActivity extends TitleBaseActivity {
	public static final int RESULT_SETCOMPANYNAME = 15;
	private CheckBox cb_choice;
	private EditText ed_companyname;
//	private TextView tv_choose;
	private String isnameopen = "0";//0表示公开,1不表示公开
	private String oldNameOpen;
	private String oldCompanyName;
	private String newCompanyname = "";
	private String currentPosition;
	private LinearLayout ll_default;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub

		setTitleText("企业名称");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				newCompanyname = ed_companyname.getText().toString().trim();
				if (StringUtil.isEmpty(newCompanyname)) {
					ShowUtils.showMsg(context, "企业名称不能为空");
				} else {
					intent.putExtra("companyName", newCompanyname);
					intent.putExtra("isnameopen", isnameopen);
					intent.putExtra("currentPosition", currentPosition);
					setResult(RESULT_SETCOMPANYNAME, intent);
					finish();
				}
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!oldCompanyName.equals(newCompanyname) || !isnameopen.equals(oldNameOpen))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetCompanyNameActivity.this.finish();
						}
					});
				else
					SetCompanyNameActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		oldCompanyName = getIntent().getStringExtra("companyName");
		oldNameOpen = getIntent().getStringExtra("isnameopen");
		currentPosition = getIntent().getStringExtra("currentPosition");
		if (StringUtil.isEmpty(oldNameOpen)) {
			isnameopen = "0";
		}else {
			isnameopen = oldNameOpen;
		}
		
		View view = View.inflate(context, R.layout.activity_setcompanyname, null);
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		ed_companyname = (EditText) view.findViewById(R.id.ed_companyname);
		if (!StringUtil.isEmpty(oldCompanyName)) {
			ed_companyname.setText(oldCompanyName);
		}else {
			oldCompanyName = "";
		}
		cb_choice = (CheckBox) view.findViewById(R.id.cb_choice);
//		tv_choose = (TextView) view.findViewById(R.id.tv_choose);
		if ("0".equals(isnameopen)) {
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
					isnameopen = "0";
				}else {
//					tv_choose.setText("否");
					isnameopen = "1";
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
			if(SetCompanyNameActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SetCompanyNameActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (!oldCompanyName.equals(newCompanyname) || !isnameopen.equals(oldNameOpen)){
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetCompanyNameActivity.this.finish();
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
