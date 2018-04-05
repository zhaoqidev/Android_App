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
public class AddDreamActivity extends TitleBaseActivity {
	public static final int RESULT_SETDREAM = 23;
	private TextView indexNum;
	private LinearLayout ll_adddream;
	private RelativeLayout rl_default;
	private String oldDreamItem,newDreamItem;
	private boolean isNewDream;
	private EditText et_dream;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if (!isNewDream) {
			setData2View();
		}
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("添加梦想");
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				newDreamItem = et_dream.getText().toString().toString().trim();
				if (!oldDreamItem.equals(newDreamItem))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							AddDreamActivity.this.finish();
						}
					});
				else
					AddDreamActivity.this.finish();
			}
		});
	}
	@Override
	protected View initContentView() {
		setContentBackgroundResource(R.color.backGrond);
		// TODO Auto-generated method stub
		oldDreamItem = getIntent().getStringExtra("dreamItem");
		if (!StringUtil.isEmpty(oldDreamItem)) {
			oldDreamItem = "";
			isNewDream = false;
		}else {
			isNewDream = true;
		}
		
		View view = View.inflate(context, R.layout.activity_setdreamitem, null);
		rl_default = (RelativeLayout) view.findViewById(R.id.rl_default);
		et_dream = (EditText) view.findViewById(R.id.et_dream);
		indexNum = (TextView) view.findViewById(R.id.indexNum);
		ll_adddream = (LinearLayout) view.findViewById(R.id.ll_adddream);
		return view;
	}
	private void setData2View() {
		et_dream.setText(oldDreamItem);
	}
	@Override
	protected void initListener() {
		super.initListener();
		rl_default.setOnClickListener(this);
		ll_adddream.setOnClickListener(this);
		et_dream.addTextChangedListener(new TextWatcher() {  
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
			if(AddDreamActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddDreamActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case R.id.ll_adddream:
			intent = new Intent();
			newDreamItem = et_dream.getText().toString().toString().trim();
			if (StringUtil.isEmpty(newDreamItem)) {
				ShowUtils.showMsg(context, "梦想不能为空");
			} else {
				if (isNewDream) {
					intent.putExtra("newDreamItem", newDreamItem);
				}else {
					intent.putExtra("alterDreamItem", newDreamItem);
				}
				setResult(RESULT_SETDREAM, intent);
				finish();
			}
			break;
		}
	}
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			newDreamItem = et_dream.getText().toString().toString().trim();
			if (!oldDreamItem.equals(newDreamItem)){
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						AddDreamActivity.this.finish();
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
