package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.VideoToolbarTabsBaseActivity;
import cc.upedu.online.contextmenu.lib.ContextMenuDialogFragment;
import cc.upedu.online.contextmenu.lib.MenuObject;
import cc.upedu.online.contextmenu.lib.MenuParams;
import cc.upedu.online.contextmenu.lib.OnMenuItemClickListener;
import cc.upedu.online.contextmenu.lib.OnMenuItemLongClickListener;
import cc.upedu.online.domin.NoteUserBean;
import cc.upedu.online.fragment.CourseStudyNote;
import cc.upedu.online.fragment.CourseStudyQuestion;
import cc.upedu.online.fragment.CourseStudySchoolmateFragment;
import cc.upedu.online.fragment.CourseStudySection;
import cc.upedu.online.fragment.CourseStudyVideoAnswer;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;
/**
 * 课程学习界面
 * @author Administrator
 *
 */
public class CourseStudyActivity extends VideoToolbarTabsBaseActivity implements OnMenuItemClickListener,
			OnMenuItemLongClickListener {
	private static final int SAVE_NOTICE = 0;
	private static final int ADD_QUESTION = 1;
	private static final int COLLECT_COURSE = 2;
	private static final int COUSER_PLAYERTIMES = 3;
	private int currentType = -1;
	//课程ID
	private String courseId,teacherName,lessontimes;
	private String kpointId;
	boolean isok;
	//是否已经收藏
	private String iscollected;
	
	private RelativeLayout ll_writenotice,ll_askquestion;
	private LinearLayout back_notice,back_question;
	private TextView save_notice,save_question;
	private EditText ed_write_notice,ed_ask_question;
	private CheckBox cb_choice_notice,cb_choice_question;
	private boolean isShowWriteNotice = false,isShowAskQuestion = false;
	private String newNotice,newQuestion;
	private boolean lastIsIntroduce = true;
	public final static int RESULT_CODE=1;
	private FragmentManager fragmentManager;
    private DialogFragment mMenuDialogFragment;
    private String userId;
    private Dialog getOldNoteDialog;
    private String oldNotice;
	
	private Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (Boolean.valueOf(mNoteUserBean.getSuccess())) {
					if (mNoteUserBean.getEntity() != null) {
						if (!StringUtil.isEmpty(mNoteUserBean.getEntity().getContent())) {
							oldNotice = mNoteUserBean.getEntity().getContent();
							ed_write_notice.setText(oldNotice);
							oldNoticeOpen = mNoteUserBean.getEntity().getStatus();
						}
					}
					newNoticeOpen = oldNoticeOpen;
					cb_choice_notice.setChecked(0 == Integer.valueOf(oldNoticeOpen));
				}else {
					backNotice();
					ShowUtils.showMsg(context, mNoteUserBean.getMessage());
				}
				getOldNoteDialog.dismiss();
				break;
			case 5://笔记
				if (isShowAskQuestion) {
					ll_askquestion.setVisibility(View.GONE);
					isShowAskQuestion = !isShowAskQuestion;
					ed_ask_question.setText("");
				}
				if (!isShowWriteNotice) {
					setFragmentBottomVisibility(View.VISIBLE);
					ll_writenotice.setVisibility(View.VISIBLE);
					currentType = SAVE_NOTICE;
					isShowWriteNotice = !isShowWriteNotice;
					getOldNoteDialog = ShowUtils.createLoadingDialog(context, false);
					getOldNoteDialog.show();
					getOldNoteData();
				}
				break;
			case 6:
				if (isShowWriteNotice) {
					ll_writenotice.setVisibility(View.GONE);
					isShowWriteNotice = !isShowWriteNotice;
					ed_write_notice.setText("");
				}
				if (!isShowAskQuestion) {
					setFragmentBottomVisibility(View.VISIBLE);
					ll_askquestion.setVisibility(View.VISIBLE);
					currentType = ADD_QUESTION;
					isShowAskQuestion = !isShowAskQuestion;
				}
				break;
			case 7:
				//进入到课程代言界面
				if (StringUtil.isEmpty(courseId) || StringUtil.isEmpty(courseName) || StringUtil.isEmpty(courseLogo)) {
					ShowUtils.showMsg(context, "数据获取失败,请检查您的网络!");
				}else {
					//进入到课程代言界面
					Intent intent = new Intent(context, CourseRepresentActivity.class);
					intent.putExtra("courseId", courseId);
					intent.putExtra("courseName", courseName);
					intent.putExtra("courseLogo", courseLogo);
					intent.putExtra("teacherName", teacherName);
					intent.putExtra("lessontimes", lessontimes);
					startActivity(intent);
				}
				break;
			case 8:
				
				break;
			}
		};
	};
	private String courseName;
	private String courseLogo;
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if (isShowWriteNotice) {
				backNotice();
				return true;
			}else if (isShowAskQuestion) {
				backQuestion();
				return true;
			}else {
    			if (lastIsIntroduce) {
    				setResult(RESULT_CODE, new Intent().putExtra("iscollected", iscollected));
    			}
				setResult(RESULT_CODE, new Intent().putExtra("iscollected", iscollected));
				return super.onKeyDown(keyCode, event);
			}
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_notice://写笔记界面中返回按钮
			backNotice();
			break;
		case R.id.save_notice://写笔记界面中保存按钮
			newNotice = ed_write_notice.getText().toString().trim();
			if (!StringUtil.isEmpty(newNotice) && (!newNotice.equals(oldNotice) || !newNoticeOpen.equals(oldNoticeOpen))) {
				kpointId = SharedPreferencesUtil.getInstance().spGetString("kpointId");
				if (!StringUtil.isEmpty(kpointId)) {
					setSaveData(courseId,kpointId,userId,newNotice,newNoticeOpen,currentType);
				}else {
					ShowUtils.showMsg(context, "请选择笔记所对应的章节!");
				}
			}else {
				ShowUtils.showMsg(context, "您的笔记还没完成,还不能保存!");
			}
			break;
		
		case R.id.back_question://提问界面中返回按钮
			backQuestion();
			break;
		case R.id.save_question://提问界面中保存按钮
			newQuestion = ed_ask_question.getText().toString().trim();
			if (StringUtil.isEmpty(newQuestion)) {
				ShowUtils.showMsg(context, "您的问题还没完成,还不能保存!");
			}else {
				kpointId = SharedPreferencesUtil.getInstance().spGetString("kpointId");
				if (!StringUtil.isEmpty(kpointId)) {
					setSaveData(courseId,kpointId,userId,newQuestion,isQuestionOpen,currentType);
				}else {
					ShowUtils.showMsg(context, "请选择笔记所对应的章节!");
				}
			}
			break;
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		back_notice.setOnClickListener(this);
		save_notice.setOnClickListener(this);
		back_question.setOnClickListener(this);
		save_question.setOnClickListener(this);

		cb_choice_notice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					newNoticeOpen = "0";
				}else {
					newNoticeOpen = "1";
				}
			}
		});
		cb_choice_question.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
//					tv_choose.setText("是");
					isQuestionOpen = "0";
				}else {
//					tv_choose.setText("否");
					isQuestionOpen = "1";
				}
			}
		});
	}
    @Override
    public View initFragmentBottomView() {
    	// TODO Auto-generated method stub
    	View view = View.inflate(context, R.layout.layout_coursestudy_notequestion, null);
    	//写笔记界面
		ll_writenotice = (RelativeLayout) view.findViewById(R.id.ll_writenotice);
		back_notice = (LinearLayout) view.findViewById(R.id.back_notice);
		save_notice = (TextView) view.findViewById(R.id.save_notice);
		ed_write_notice = (EditText) view.findViewById(R.id.ed_write_notice);
		cb_choice_notice = (CheckBox) view.findViewById(R.id.cb_choice_notice);
		//写提问的界面
		ll_askquestion = (RelativeLayout) view.findViewById(R.id.ll_askquestion);
		back_question = (LinearLayout) view.findViewById(R.id.back_question);
		save_question = (TextView) view.findViewById(R.id.save_question);
		ed_ask_question = (EditText) view.findViewById(R.id.ed_ask_question);
		cb_choice_question = (CheckBox) view.findViewById(R.id.cb_choice_question);
    	return view;
    }
    @Override
	protected void initTitle() {
		// TODO Auto-generated method stub
    	userId = UserStateUtil.getUserId();
		courseId = getIntent().getStringExtra("courseId");
		courseName = getIntent().getStringExtra("courseName");
		courseLogo = getIntent().getStringExtra("courseLogo");
		teacherName = getIntent().getStringExtra("teacherName");
		lessontimes = getIntent().getStringExtra("lessontimes");
		
		//存到本地，在课程学习学友界面，索要分享笔记时使用
		SharedPreferencesUtil.getInstance().editPutString("study_courseId", courseId);
		SharedPreferencesUtil.getInstance().editPutString("study_courseName", courseName);
		SharedPreferencesUtil.getInstance().editPutString("study_courseLogo", courseLogo);
		
		
		iscollected = getIntent().getStringExtra("iscollected");
		kpointId = getIntent().getStringExtra("kpointId");
		if (!StringUtil.isEmpty(kpointId)) {
			lastIsIntroduce = false;
			SharedPreferencesUtil.getInstance().editPutString("kpointId", kpointId);
		}
		int resid;
		if (Boolean.valueOf(iscollected)) {
			resid = R.drawable.iconfont_shoucang;
		}else {
			resid = R.drawable.left_collect;
		}
		setRightButton2(resid, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//判断是否已经收藏
				if ("true".equals(iscollected)) {
					ShowUtils.showMsg(context, "您已经收藏过此课程!");
				}else {
					setSaveData(courseId,null,userId,null,null,COLLECT_COURSE);
				}
			}
		});
		setRightButton(R.drawable.content_nenu, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//菜单按钮
				if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
					mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
				}
			}
		});
		setLeftButton(new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (lastIsIntroduce) {
					setResult(RESULT_CODE, new Intent().putExtra("iscollected", iscollected));
				}
				finish();
			}
		});
	}
    private CourseStudySection courseStudySectionFragment;
    private CourseStudyNote courseStudyNoteFragment;
    private CourseStudyQuestion courseStudyQuestionFragment;
    private CourseStudyVideoAnswer courseStudyVideoAnswerFragment;
    private CourseStudySchoolmateFragment courseSchoolmateFragment;
	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
		
		courseStudySectionFragment=new CourseStudySection(context, courseId);
		courseStudyNoteFragment=new CourseStudyNote(context, courseId);
		courseStudyQuestionFragment=new CourseStudyQuestion(context, courseId);
		courseStudyVideoAnswerFragment=new CourseStudyVideoAnswer(context, courseId);
		courseSchoolmateFragment=new CourseStudySchoolmateFragment(context, courseId);
		map.put("章节", courseStudySectionFragment);
		map.put("笔记", courseStudyNoteFragment);
		map.put("提问", courseStudyQuestionFragment);
		map.put("答疑", courseStudyVideoAnswerFragment);
		map.put("学友", courseSchoolmateFragment);
		return map;
	}
	
	private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
//        menuParams.setActionBarSize((int) getResources().getDimension(R.id.rl_title));
        menuParams.setActionBarSize((int)getResources().getDimension(R.dimen.height_top_bar));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(true);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

	
	@Override
	protected void initData() {
		initVideoData(courseName,courseLogo,false);
		initTabsViewPager();
		fragmentManager = getSupportFragmentManager();
		initMenuFragment();
	}
	
	private String oldNoticeOpen = "0";//0表示公开 1表示不公开
	private String newNoticeOpen;
	private String isQuestionOpen = "1";
	
	private NoteUserBean mNoteUserBean;

	private void getOldNoteData() {
		// TODO Auto-generated method stub
		//获取课程笔记列表的数据
		String kpointId = SharedPreferencesUtil.getInstance().spGetString("kpointId");
		if (!StringUtil.isEmpty(kpointId)) {
			Map<String, String> requestDataMap = ParamsMapUtil.getShowCourseNotice(context, courseId, kpointId,userId);
			RequestVo requestVo = new RequestVo(ConstantsOnline.SHOW_NOTE_TELECAST_COURSE, context, requestDataMap, new MyBaseParser<>(NoteUserBean.class));
			DataCallBack<NoteUserBean> noticeDataCallBack = new DataCallBack<NoteUserBean>() {
				@Override
				public void processData(NoteUserBean object) {
					if (object==null) {
						ShowUtils.showMsg(context, "获取笔记数据失败!");
					}else {
						mNoteUserBean = object;
						handler.obtainMessage(0).sendToTarget();
					}
				}
			};
			getDataServer(requestVo, noticeDataCallBack);
		}else {
			ShowUtils.showMsg(context, "写笔记前,请选择课程章节");
		}
	}
	/**
	 * 提问界面中的返回方法
	 */
	private void backQuestion() {
		newQuestion = ed_ask_question.getText().toString().trim();
		if (StringUtil.isEmpty(newQuestion)) {
			ll_askquestion.setVisibility(View.GONE);
			setFragmentBottomVisibility(View.GONE);
			isShowAskQuestion = !isShowAskQuestion;
		}else {
			ShowUtils.showDiaLog(context, "温馨提醒", "您的问题还没保存,是否要返回!", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					setFragmentBottomVisibility(View.GONE);
					ll_askquestion.setVisibility(View.GONE);
					isShowAskQuestion = !isShowAskQuestion;
					ed_ask_question.setText("");
				}
			});
		}
	}
	/**
	 * 写笔记界面中的返回方法
	 */
	private void backNotice() {
		newNotice = ed_write_notice.getText().toString().trim();
		if (!StringUtil.isEmpty(newNotice) && (!newNotice.equals(oldNotice) || !newNoticeOpen.equals(oldNoticeOpen))) {
			ShowUtils.showDiaLog(context, "温馨提醒", "您的笔记设置还没保存,是否要直接返回!", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					setFragmentBottomVisibility(View.GONE);
					ll_writenotice.setVisibility(View.GONE);
					isShowWriteNotice = !isShowWriteNotice;
					ed_write_notice.setText("");
				}
			});
		}else {
			ll_writenotice.setVisibility(View.GONE);
			setFragmentBottomVisibility(View.GONE);
			isShowWriteNotice = !isShowWriteNotice;
		}
	}
	private void savePlayertimes(String userId,String courseId,String kpointId) {
		setSaveData(courseId, kpointId, userId, null, null, COUSER_PLAYERTIMES);
	}
	/**
	 * 发送http请求网络数据保存笔记和提问功能
	 */
	private void setSaveData(String courseId,String kpointId,String userId,String content,String status,int type) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("courseId", courseId);
		map.put("userId", userId);
		if (COLLECT_COURSE == type) {
			UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.COLLECTION_COURSE,map , new UploadDataCallBack() {

				@Override
				public void onUploadDataSuccess() {
					// TODO Auto-generated method stub
					ShowUtils.showMsg(context, "收藏课程成功");
					iscollected = "true";
					setRightButton2(R.drawable.iconfont_shoucang,null);
				}

				@Override
				public void onUploadDataFailure() {
					// TODO Auto-generated method stub
					
				}
			});
		}else if (COUSER_PLAYERTIMES == type) {
			map.put("kpointId", kpointId);
			UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.COUSER_PLAYERTIMES,map , new UploadDataCallBack() {

				@Override
				public void onUploadDataSuccess() {
					// TODO Auto-generated method stub
				}

				@Override
				public void onUploadDataFailure() {
					// TODO Auto-generated method stub
					
				}
			});
		}else {
			map.put("kpointId", kpointId);
			map.put("content", content);
			map.put("status", status);
			String url = null;
			switch (type) {
			case SAVE_NOTICE:
				url = ConstantsOnline.SAVE_NOTE_TELECAST_COURSE;
				break;
			case ADD_QUESTION:
				url = ConstantsOnline.ADD_COURSEQUESTION;
				break;
			}
			UploadDataUtil.getInstance().onUploadDataData(context,url,map , new UploadDataCallBack() {

				@Override
				public void onUploadDataSuccess() {
					// TODO Auto-generated method stub
					if (SAVE_NOTICE == currentType) {
						ShowUtils.showMsg(context, "保存笔记成功");
						ed_write_notice.setText("");
						ll_writenotice.setVisibility(View.GONE);
						setFragmentBottomVisibility(View.GONE);
						isShowWriteNotice = !isShowWriteNotice;
					}else if (ADD_QUESTION == currentType) {
						ShowUtils.showMsg(context, "保存提问成功");
						ed_ask_question.setText("");
						ll_askquestion.setVisibility(View.GONE);
						setFragmentBottomVisibility(View.GONE);
						isShowAskQuestion = !isShowAskQuestion;
					}
					//TODO 保存成功刷新列表
//					if (currentPager == 1) {
//						((CourseStudyNote)pagerList.get(currentPager)).getNewData();
//					}else if (currentPager == 2) {
//						((CourseStudyQuestion)pagerList.get(currentPager)).getNewData();
//					}
				}

				@Override
				public void onUploadDataFailure() {
					// TODO Auto-generated method stub
//					if (SAVE_NOTICE == currentType) {
//						ShowUtils.showMsg(context, "保存笔记失败,请检查您的网络连接是否正常!");
//					}else if (ADD_QUESTION == currentType) {
//						ShowUtils.showMsg(context, "上交提问失败,请检查您的网络连接是否正常!");
//					}
				}
			});
		}
	}
	
//	@Override
//	public boolean dispatchKeyEvent(KeyEvent event) {
//		if (isCCPlay) {
//			if (mCcMediaPlay!=null) {
////				mCcMediaPlay.dispatchKeyEvent(event);
//			}
//		}
//		return super.dispatchKeyEvent(event);
//	}
//	
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (isCCPlay) {
//			if (mCcMediaPlay!=null) {
//				mCcMediaPlay.onTouchEvent(event);
//			}
//		}
//		return super.onTouchEvent(event);
//	}

	private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
//        close.setResource(R.drawable.icn_close);
//        close.setBgColor(getResources().getColor(android.R.color.transparent));

        MenuObject note = new MenuObject("笔记");
        note.setResource(R.drawable.meun_note);
        note.setScaleType(ScaleType.FIT_XY);
        note.setBgColor(getResources().getColor(R.color.titlered_80));

        MenuObject asking = new MenuObject("提问");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.meun_asking);
        asking.setBitmap(b);
        asking.setScaleType(ScaleType.FIT_XY);
        asking.setBgColor(getResources().getColor(R.color.titlered_80));

        MenuObject share = new MenuObject("代言");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.meun_share));
        share.setDrawable(bd);
        share.setScaleType(ScaleType.FIT_XY);
        share.setBgColor(getResources().getColor(R.color.titlered_80));

//        MenuObject my = new MenuObject("我的");
//        my.setResource(R.drawable.meun_my);
//        my.setScaleType(ScaleType.FIT_XY);
//        my.setBgColor(getResources().getColor(R.color.titlered_80));

//        MenuObject block = new MenuObject("Block user");
//        block.setResource(R.drawable.icn_5);
//        close.setBgColor(getResources().getColor(R.color.light_red));

        menuObjects.add(close);
        menuObjects.add(note);
        menuObjects.add(asking);
        menuObjects.add(share);
//        menuObjects.add(my);
//        menuObjects.add(block);
        return menuObjects;
    }
	
	@Override
    public void onBackPressed() {
        if (mMenuDialogFragment != null && mMenuDialogFragment.isAdded()) {
            mMenuDialogFragment.dismiss();
        } else{
            finish();
        }
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        handler.sendEmptyMessage(position+4);
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
    	handler.sendEmptyMessage(position+4);
    }

	@Override
	public View initBottomView() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onMyCoursePlaySuccessCallBack() {
		// TODO Auto-generated method stub
		kpointId = SharedPreferencesUtil.getInstance().spGetString("kpointId");
		savePlayertimes(userId, courseId, kpointId);
	}
	@Override
	public void setAppBarLayoutChanged(int i) {
		// TODO Auto-generated method stub
		if (i == 0) {
			if (courseStudySectionFragment != null && courseStudyNoteFragment != null&& courseStudyQuestionFragment != null&&courseStudyVideoAnswerFragment!=null&&courseSchoolmateFragment!=null) {
				courseStudySectionFragment.setPullRefreshEnable(true);
				courseStudyNoteFragment.setPullRefreshEnable(true);
				courseStudyQuestionFragment.setPullRefreshEnable(true);
				courseStudyVideoAnswerFragment.setPullRefreshEnable(true);
				courseSchoolmateFragment.setPullRefreshEnable(true);
			}
		} else {
			if (courseStudySectionFragment != null && courseStudyNoteFragment != null&& courseStudyQuestionFragment != null&&courseStudyVideoAnswerFragment!=null&&courseSchoolmateFragment!=null) {
				courseStudySectionFragment.setPullRefreshEnable(false);
				courseStudyNoteFragment.setPullRefreshEnable(false);
				courseStudyQuestionFragment.setPullRefreshEnable(false);
				courseStudyVideoAnswerFragment.setPullRefreshEnable(false);
				courseSchoolmateFragment.setPullRefreshEnable(false);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
//			if (requestCode==CourseStudySchoolmateFragment.REQUEST_CHOOSEINDUSTRY){//设置所属行业的数据回传
//			}
			if (resultCode==SetIndustryActivity.RESULT_SETINDUSTRY){
				Bundle bundle=data.getExtras(); 
				if (bundle != null) {
					String industry=bundle.getString("industry");
					String industryname=bundle.getString("industryname");
					courseSchoolmateFragment.setIndustry(industryname, industry);
				}
			}

//			if (requestCode == CityChooseActity.CHOOSE_ONE) {// 设置所在
//			}
			if (resultCode == CityChooseActity.CHOOSE_ONE) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					String text = bundle.getString("text");
					String id = bundle.getString("id");
					courseSchoolmateFragment.setOthercity(text, id);
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharedPreferencesUtil.getInstance().editPutString("answerId", "");
	}
}
