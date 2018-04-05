package cc.upedu.online.activity;

import android.content.Context;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.NoteItem;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 我的笔记中单个课程中的单个章节的布局的编辑
 * @author Administrator
 *
 */
public class MyNoteOneCourseEditActivity extends TitleBaseActivity {
	private NoteItem noteItem;
	private TextView indexNum;
	private LinearLayout ll_addnote;
	private RelativeLayout rl_default;
	private String oldNoteItem,newNoteItem;
	private EditText et_note;
	//status	0公开1隐藏
	private String isNoticeOpen = "0";
//	private RadioGroup rg_jurisdiction;
	private RadioButton rb_open;
	private RadioButton rb_hide;
	private String courseId;
	
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		et_note.setText(oldNoteItem);
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("编辑笔记");
		noteItem = (NoteItem) getIntent().getSerializableExtra("noteItem");
		courseId = getIntent().getStringExtra("courseId");
		if (noteItem != null) {
			oldNoteItem = noteItem.getCourseContent();
		}else {
			ShowUtils.showMsg(context, "因数据错误关闭界面,请反馈信息,谢谢!");
			this.finish();
		}
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_edit_noteitem, null);
		rl_default = (RelativeLayout) view.findViewById(R.id.rl_default);
		et_note = (EditText) view.findViewById(R.id.et_note);
		indexNum = (TextView) view.findViewById(R.id.indexNum);
		ll_addnote = (LinearLayout) view.findViewById(R.id.ll_addnote);
		
//		rg_jurisdiction = (RadioGroup) view.findViewById(R.id.rg_jurisdiction);
		rb_open = (RadioButton) view.findViewById(R.id.rb_open);
		rb_hide = (RadioButton) view.findViewById(R.id.rb_hide);
		if (!StringUtil.isEmpty(isNoticeOpen)) {
			if ("0".equals(isNoticeOpen)) {
				rb_open.setChecked(true);
				rb_hide.setChecked(false);
			}else if ("1".equals(isNoticeOpen)) {
				rb_open.setChecked(false);
				rb_hide.setChecked(true);
			}
		}else {
			isNoticeOpen = "0";
			rb_open.setChecked(true);
			rb_hide.setChecked(false);
		}
		rb_open.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					isNoticeOpen = "0";
					rb_hide.setChecked(false);
				}
			}
		});
		rb_hide.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					isNoticeOpen = "1";
					rb_open.setChecked(false);
				}
			}
		});
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_addnote.setOnClickListener(this);
		rl_default.setOnClickListener(this);
		et_note.addTextChangedListener(new TextWatcher() {  
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
		switch (v.getId()) {
		case R.id.rl_default:
			if(MyNoteOneCourseEditActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MyNoteOneCourseEditActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case R.id.ll_addnote:
			newNoteItem = et_note.getText().toString().trim();
			if (StringUtil.isEmpty(newNoteItem)) {
				ShowUtils.showMsg(context, "笔记不能为空");
			} else {
//				intent.putExtra("newThoughtItem", newThoughtItem);
//				setResult(RESULT_SETTHOUGHT, intent);
//				finish();
				if (StringUtil.isEmpty(newNoteItem) || oldNoteItem.equals(newNoteItem)) {
					ShowUtils.showMsg(context, "您的笔记还没完成,还不能保存!");
				}else {
					if (!StringUtil.isEmpty(noteItem.getKpointId())) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("courseId", courseId);
						map.put("userId", UserStateUtil.getUserId());
						map.put("kpointId", noteItem.getKpointId());
						map.put("content", newNoteItem);
						map.put("status", isNoticeOpen);
						UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.SAVE_NOTE_TELECAST_COURSE,map , new UploadDataCallBack() {

							@Override
							public void onUploadDataSuccess() {
								// TODO Auto-generated method stub
								ShowUtils.showMsg(context, "保存笔记成功");
								et_note.setText("");
								MyNoteOneCourseEditActivity.this.finish();
								//TODO 保存成功刷新列表
//								if (currentPager == 1) {
//									((CourseStudyNote)pagerList.get(currentPager)).getNewData();
//								}else if (currentPager == 2) {
//									((CourseStudyQuestion)pagerList.get(currentPager)).getNewData();
//								}
							}

							@Override
							public void onUploadDataFailure() {
								// TODO Auto-generated method stub
								
							}
						});
					}else {
						ShowUtils.showMsg(context, "请选择笔记所对应的章节!");
					}
				}
			}
			break;
		}
	}

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
	    	ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					MyNoteOneCourseEditActivity.this.finish();
				}
			});
			return false;
	    }else {
	    	return super.onKeyDown(keyCode, event); 
		}
	}
}
