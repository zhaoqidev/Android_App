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
 * 添加/修改价值观界面
 * @author Administrator
 *
 */
public class AddThoughtActivity extends TitleBaseActivity {
	public static final int RESULT_SETTHOUGHT = 24;
	private TextView indexNum;
	private LinearLayout ll_addthought;
	private RelativeLayout rl_default;
	private String oldThoughtItem,newThoughtItem;
	private boolean isNewThought;
	private EditText et_thought;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if (!isNewThought) {
			et_thought.setText(oldThoughtItem);
		}
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("价值观和信念");
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				newThoughtItem = et_thought.getText().toString().trim();
				if (!oldThoughtItem.equals(newThoughtItem))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							AddThoughtActivity.this.finish();
						}
					});
				else
					AddThoughtActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		oldThoughtItem = getIntent().getStringExtra("thoughtItem");
		if (!StringUtil.isEmpty(oldThoughtItem)) {
			oldThoughtItem = "";
			isNewThought = false;
		}else {
			isNewThought = true;
		}
		
		View view = View.inflate(context, R.layout.activity_setthoughtitem, null);
		rl_default = (RelativeLayout) view.findViewById(R.id.rl_default);
		et_thought = (EditText) view.findViewById(R.id.et_thought);
		indexNum = (TextView) view.findViewById(R.id.indexNum);
		ll_addthought = (LinearLayout) view.findViewById(R.id.ll_addthought);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_addthought.setOnClickListener(this);
		rl_default.setOnClickListener(this);
		et_thought.addTextChangedListener(new TextWatcher() {  
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
			indexNum.setText(num+"/100");
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_default:
			if(AddThoughtActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddThoughtActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case R.id.ll_addthought:
			intent = new Intent();
			newThoughtItem = et_thought.getText().toString().trim();
			if (StringUtil.isEmpty(newThoughtItem)) {
				ShowUtils.showMsg(context, "价值观信念不能为空");
			} else {
				if (isNewThought) {
					intent.putExtra("newThoughtItem", newThoughtItem);
				}else {
					intent.putExtra("newThoughtItem", newThoughtItem);
				}
				setResult(RESULT_SETTHOUGHT, intent);
				finish();
			}
			break;
		}
	}

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			newThoughtItem = et_thought.getText().toString().trim();
			if (!oldThoughtItem.equals(newThoughtItem)) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						AddThoughtActivity.this.finish();
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
