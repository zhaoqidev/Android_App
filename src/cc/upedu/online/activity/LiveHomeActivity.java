package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;

import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.CourseIntroduceBean.Entity.Course;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;

public class LiveHomeActivity extends TitleBaseActivity {
	private WebView wb_live;
	private String iscollected;
	private Course courseItem;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("直播间");
		setRightButton(R.drawable.left_collect, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
						map.put("courseId", courseItem.getCourseId());
						map.put("userId", UserStateUtil.getUserId());
						UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.COLLECTION_COURSE,map , new UploadDataCallBack() {

							@Override
							public void onUploadDataSuccess() {
								// TODO Auto-generated method stub
								ShowUtils.showMsg(context, "收藏课程成功");
								iscollected = "true";
								setRightButton(R.drawable.iconfont_shoucang,null);
							}

							@Override
							public void onUploadDataFailure() {
								// TODO Auto-generated method stub
								
							}
						});
					}
				}
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_webview, null);
		iscollected = getIntent().getStringExtra("");
		courseItem = (Course)getIntent().getSerializableExtra("courseItem");
		
		wb_live = (WebView) view.findViewById(R.id.wv);
		wb_live.getSettings().setJavaScriptEnabled(true);
    	long tempUid = 1000000000 + Integer.valueOf(UserStateUtil.getUserId());
		wb_live.loadUrl(courseItem.getLiveurl()+"&nickname="+SharedPreferencesUtil.getInstance().spGetString("name") + "&uid=" + tempUid);
		return view;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		wb_live.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		wb_live.onResume();
	}
	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
}
