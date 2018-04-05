package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.VideoToolbarTabsBaseActivity;
import cc.upedu.online.domin.CourseShowArchitectureBean;
import cc.upedu.online.fragment.ArchitectureCourseFragment;
import cc.upedu.online.fragment.ArchitectureOrigenFragment;
import cc.upedu.online.fragment.ArchitectureStructureFragment;
import cc.upedu.online.fragment.ArchitectureTheacherFragment;
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

/**
 * 课程体系界面
 * @author Administrator
 *
 */
public class CourseArchitectureActivity extends VideoToolbarTabsBaseActivity{
	private String subjectId;
	private CourseShowArchitectureBean mCourseShowArchitectureBean;
	private String iscollected;
	
	private Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if ("true".equals(mCourseShowArchitectureBean.getSuccess())) {
					String videoId = mCourseShowArchitectureBean.getEntity().getSubject().getVediourl();
					String videotype = mCourseShowArchitectureBean.getEntity().getSubject().getVideoType();
//					iscollected = mCourseShowArchitectureBean.getEntity().getSubject().getIsAppShow();
					SharedPreferencesUtil.getInstance().editPutString("currentStudyUrl", videoId);
					SharedPreferencesUtil.getInstance().editPutString("videoType", videotype);
					initVideoData(subjectName,subjectLogo,false);
					initTabsViewPager();
				}else {
					ShowUtils.showMsg(CourseArchitectureActivity.this, mCourseShowArchitectureBean.getMessage());
				}
				break;
			}
		};
	};

	private String subjectLogo;
	private String subjectName;
	private String userId;
	
	@Override
	protected void initData() {
		userId = UserStateUtil.getUserId();
		if (StringUtil.isEmpty(userId)) {
			userId = String.valueOf(0);
		}
		//获取体系介绍的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getCourseArchitecture(context, subjectId);
		RequestVo requestVo = new RequestVo(ConstantsOnline.HOME_SHOWARCHITECTURE, context, requestDataMap, new MyBaseParser<>(CourseShowArchitectureBean.class));
		DataCallBack<CourseShowArchitectureBean> coursseDataCallBack = new DataCallBack<CourseShowArchitectureBean>() {

			@Override
			public void processData(CourseShowArchitectureBean object) {
				if (object==null) {
					ShowUtils.showMsg(context, "获取体系介绍数据失败请联系客服");
				}else {
					mCourseShowArchitectureBean = object;
					handler.obtainMessage(0).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, coursseDataCallBack);
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("课程体系");
		setRightButton(R.drawable.left_collect, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				collectSave();
			}
		});
		subjectId = getIntent().getStringExtra("subjectId");
		subjectLogo = getIntent().getStringExtra("subjectLogo");
		subjectName = getIntent().getStringExtra("subjectId");
	}

	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
		map.put("缘起", new ArchitectureOrigenFragment(context,mCourseShowArchitectureBean.getEntity().getSubject().getIntroduct()));
		map.put("结构", new ArchitectureStructureFragment(context,mCourseShowArchitectureBean.getEntity().getSubject().getStructure()));
		map.put("课程", new ArchitectureCourseFragment(context,subjectId));
		map.put("导师", new ArchitectureTheacherFragment(context,subjectId));
		return map;
	}

	@Override
	public View initBottomView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View initFragmentBottomView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onMyCoursePlaySuccessCallBack() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAppBarLayoutChanged(int i) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 */
	private void collectSave() {
		//判断是否已经登录
		if (!UserStateUtil.isLogined()) {//未登录
			ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录.", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					//跳转到登录界面
					Intent intent = new Intent(context, LoginActivity.class);
					startActivity(intent);
				}
			});
		}else {//已登录
			//判断是否已经收藏
			if (Boolean.valueOf(iscollected)) {
				ShowUtils.showMsg(context, "您已经收藏过此课程!");
			}else {
				Map<String, String> map = new HashMap<String, String>();
				map.put("subjectId", subjectId);
				map.put("userId", userId);
				UploadDataUtil.getInstance().onUploadDataData(context,null,map , new UploadDataCallBack() {

					@Override
					public void onUploadDataSuccess() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onUploadDataFailure() {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
	}
}
