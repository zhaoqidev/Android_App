package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
/**
 * 添加/修改梦想界面
 * @author Administrator
 *
 */
public class AddHobbyActivity extends TitleBaseActivity {
	public static final int RESULT_SETMYHOBBY = 25;
	private TextView indexNum;
	private LinearLayout ll_addhobby;
	private RelativeLayout rl_default;
	private String oldHobbyItem,newHobbyItem = "";
	private EditText et_hobby;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		et_hobby.setText(oldHobbyItem);
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("添加爱好");
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				newHobbyItem = et_hobby.getText().toString().toString().trim();
				if (!oldHobbyItem.equals(newHobbyItem))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							AddHobbyActivity.this.finish();
						}
					});
				else
					AddHobbyActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		setContentBackgroundResource(R.color.backGrond);
		oldHobbyItem = getIntent().getStringExtra("hobbyItem");
		if (StringUtil.isEmpty(oldHobbyItem))
			oldHobbyItem = "";
		View view = View.inflate(context, R.layout.activity_sethobbyitem, null);
		rl_default = (RelativeLayout) view.findViewById(R.id.rl_default);
		et_hobby = (EditText) view.findViewById(R.id.et_hobby);
		indexNum = (TextView) view.findViewById(R.id.indexNum);
		ll_addhobby = (LinearLayout) view.findViewById(R.id.ll_addhobby);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		rl_default.setOnClickListener(this);
		ll_addhobby.setOnClickListener(this);
		et_hobby.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
				// TODO Auto-generated method stub
//	          mTextView.setText(s);//将输入的内容实时显示  
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
//	            editText.setText("您输入了" + temp.length() + "个字符");  
				setIndexNum(temp.length());
			}
		});
	}
	private void setIndexNum(int num) {
		// TODO Auto-generated method stub
		if (View.VISIBLE == indexNum.getVisibility()) {
			indexNum.setText(num+"/150");
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.rl_default:
			if(AddHobbyActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddHobbyActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case R.id.ll_addhobby:
			intent = new Intent();
			newHobbyItem = et_hobby.getText().toString().toString().trim();
			if (StringUtil.isEmpty(newHobbyItem)) {
				newHobbyItem = "";
			}
			intent.putExtra("newHobbyItem", newHobbyItem);
			setResult(RESULT_SETMYHOBBY, intent);
			finish();
			break;
		}
	}
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			newHobbyItem = et_hobby.getText().toString().toString().trim();
			if (!oldHobbyItem.equals(newHobbyItem)){
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						AddHobbyActivity.this.finish();
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
