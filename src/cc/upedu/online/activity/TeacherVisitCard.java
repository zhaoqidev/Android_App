package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map;

import cc.upedu.online.CCMediaPlay.CCMediaPlayFull;
import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.LayoutToolbarTabsBaseActivity;
import cc.upedu.online.domin.DaoshiCardBean;
import cc.upedu.online.fragment.DaoshiArticleFragment;
import cc.upedu.online.fragment.DaoshiCourseFragment;
import cc.upedu.online.fragment.DaoshiIntroduceFragment;
import cc.upedu.online.fragment.DaoshiMienFragment;
import cc.upedu.online.fragment.DaoshiWishesFragment;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.CircleImageView;
import cn.sharesdk.onekeyshare.OnekeyShare;
import io.rong.imkit.RongIM;

public class TeacherVisitCard extends LayoutToolbarTabsBaseActivity{
	CircleImageView iv_teacher_image;//导师头像
	TextView tv_teacher_name;//导师姓名
	TextView tv_teacher_position;//导师职位
	LinearLayout ll_zengyan,ll_private_litter;//导师赠言，写私信按钮
//	RelativeLayout ll_content;
	
	private DaoshiCardBean daoshiCardBean;
	
	private RequestVo requestVo;
	private DataCallBack<DaoshiCardBean> dataCallBack;

	Intent intent;
	private String teacherName;
	private String teacherId;
	private String teacherUid;//导师的用户Id
	private String teacherLogo;
	
	private boolean isCCPlay;
	
	DaoshiCourseFragment courseFragment;//课程
	DaoshiArticleFragment articleFragment;//文章
	DaoshiWishesFragment wishesFragment;//寄语
	
	/**
	 * 正在加载布局
	 */
	private Dialog loadingDialog;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("");
		setRightButton(R.drawable.share, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//分享功能
				//进入代言
				showPopWin();

			}
		});
		isCCPlay = SharedPreferencesUtil.getInstance().spGetBoolean("isCCPlay");
	}
	@Override
	public View initLayoutTop() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_teachercard_top, null);
		// 正在加载布局
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
		
		iv_teacher_image=(CircleImageView) view.findViewById(R.id.iv_teacher_image);
		tv_teacher_name=(TextView) view.findViewById(R.id.tv_teacher_name);
		tv_teacher_position=(TextView) view.findViewById(R.id.tv_teacher_position);
		ll_private_litter=(LinearLayout) view.findViewById(R.id.ll_private_litter);
		ll_zengyan=(LinearLayout) view.findViewById(R.id.ll_zengyan);
		intent =getIntent();
		teacherId = intent.getStringExtra("teacherId");
		teacherName = intent.getStringExtra("teacherName");
		teacherLogo = intent.getStringExtra("teacherLogo");
		tv_teacher_name.setText(teacherName);
		tv_teacher_position.setText(intent.getStringExtra("teacherPosition"));

		//如果teacherId为空，就说明是从浏览器跳转过来的
		if (StringUtil.isEmpty(teacherId)){
			Uri uri = getIntent().getData();
			teacherId= uri.getQueryParameter("teacherId");
		}

     
		return view;
	}
	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
		courseFragment=new DaoshiCourseFragment(context);
		articleFragment=new DaoshiArticleFragment(context);
		wishesFragment=new DaoshiWishesFragment(context);
		map.put("简介", new DaoshiIntroduceFragment(context));
		map.put("风采", new DaoshiMienFragment(context));
		map.put("课程", courseFragment);
		map.put("文章", articleFragment);
		map.put("寄语", wishesFragment);
		//清除正在加载的布局
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
		return map;
	}

	@Override
	protected void initListener() {
		super.initListener();
		ll_private_litter.setOnClickListener(this);
		ll_zengyan.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_zengyan://打开视频播放，播放导师赠言
			if (!StringUtil.isEmpty(daoshiCardBean.entity.vidiourl)) {
				if (isCCPlay) {
					Intent intent = new Intent(context, CCMediaPlayFull.class);
					intent.putExtra("videoId", daoshiCardBean.entity.vidiourl);
					intent.putExtra("position", 0);
					startActivity(intent);
				}else {
					Intent intent = new Intent(context, LeCPlayActivity.class);
					intent.putExtra("videoId", daoshiCardBean.entity.vidiourl);
					startActivity(intent);
				}
			}else {
				ShowUtils.showMsg(context, "暂无导师赠言视频，敬请期待");
			}
			
			break;
			
		case R.id.ll_private_litter://发送站内信
			//先检测是否登录
			/*站内信
			 * if (UserStateUtil.isLogined()) {
				intent = new Intent(context, PrivateLetterActivity.class);
				intent.putExtra("uid", UserStateUtil.getUserId());
				intent.putExtra("fid", teacherUid);
				intent.putExtra("name", teacherName);
				startActivity(intent);
			}else{
				UserStateUtil.NotLoginDialog(context);
			}*/
			if (!UserStateUtil.getUserId().equals(teacherUid)) {
				if (UserStateUtil.isLogined()) {
					/*intent = new Intent(context, ConversationActivity.class);
					intent.putExtra("targetId", teacherUid);
					intent.putExtra("userName", teacherName);
					intent.putExtra("userAvatar", teacherLogo);
					startActivity(intent);*/
					
					RongIM.getInstance().startPrivateChat(context, teacherUid, teacherName);
				}else{
					UserStateUtil.NotLoginDialog(context);
				}
			}else {
				ShowUtils.showMsg(context, "尊敬的导师，不能给自己发信哦");
			}
		default:
			break;
		}
	}
	

	@Override
	protected void initData() {
		SetToolbarText(teacherName);
		getRequestVo();
		getDataCallBack();
		getDataServer(requestVo, dataCallBack);
		
	}

	private void getDataCallBack() {
		dataCallBack = new DataCallBack<DaoshiCardBean>() {

			@Override
			public void processData(DaoshiCardBean object) {
				if (object != null) {
					daoshiCardBean = object;
					
					// 判断返回值是true还是false
					if ("true".equals(object.success)) {
						if ("LETV".equals(daoshiCardBean.entity.videotype)) {
							isCCPlay = false;
						}else if ("CC".equals(daoshiCardBean.entity.videotype)) {
							isCCPlay = true;
						}

						
						ImageUtils.setImage(object.entity.picPath, iv_teacher_image, 0);
						
						if (!StringUtil.isEmpty(daoshiCardBean.entity.teacherUid)) {
							teacherUid=daoshiCardBean.entity.teacherUid;
						}

						if (StringUtil.isEmpty(teacherName)){

							teacherLogo = daoshiCardBean.entity.picPath;
							tv_teacher_name.setText(daoshiCardBean.entity.name);
							tv_teacher_position.setText(daoshiCardBean.entity.education);
						}
						
						//把子页面需要的数据存到sp中
						SharedPreferencesUtil.getInstance().editPutString("introduct", object.entity.introduct);//导师介绍
						SharedPreferencesUtil.getInstance().editPutString("honors", object.entity.honors);//导师荣誉
						SharedPreferencesUtil.getInstance().editPutString("teacherId", object.entity.teacherId);//导师Id
						
						PreferencesObjectUtil.saveObject(object.entity.picArray, "picArray", context);
//						switchPager();
						initTabsViewPager();
					   
					} else {
						ShowUtils.showMsg(TeacherVisitCard.this, object.message);
					}
				} else {
					ShowUtils.showMsg(TeacherVisitCard.this, "获取数据失败，请检查网络");
				}
				
			}
		};
	}
	
	private void getRequestVo() {

		if(teacherId==null){
			ShowUtils.showMsg(context, "数据有误，请重试");
		}else {
			Map<String, String> setUserInfo = ParamsMapUtil.DaoshiCard(context, teacherId);
			requestVo = new RequestVo(ConstantsOnline.DAOSHI_CARD, context,
					setUserInfo, new MyBaseParser<>(DaoshiCardBean.class));
		}
		
	}
	@Override
	public void setAppBarLayoutChanged(int i) {
		// TODO Auto-generated method stub
		if (i == 0) {
			// 展开状态，可刷新
			if (courseFragment != null && articleFragment != null&& wishesFragment != null) {
				courseFragment.setPullRefreshEnable(true);
				articleFragment.setPullRefreshEnable(true);
				wishesFragment.setPullRefreshEnable(true);
			}
		} else {
			// 收起状态，不可刷新
			if (courseFragment != null && articleFragment != null&& wishesFragment != null) {
				courseFragment.setPullRefreshEnable(false);
				articleFragment.setPullRefreshEnable(false);
				wishesFragment.setPullRefreshEnable(false);
			}
		}
	}
	@Override
	public View initFragmentBottomView() {
		// TODO Auto-generated method stub
		return null;
	}

	private OnekeyShare oks;
	private PopupWindow pop;
	private void showPopWin() {
		if (pop == null) {
			View view = View.inflate(context, R.layout.layout_represent_popwindow, null);
			TextView tv_share_qr= (TextView) view.findViewById(R.id.tv_share_qr);
			tv_share_qr.setText("分享导师二维码名片");
			TextView tv_share= (TextView) view.findViewById(R.id.tv_share);
			tv_share.setText("直接分享导师名片");
			pop = new PopupWindow(view,
					WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.WRAP_CONTENT);
			pop.setFocusable(true); // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
			pop.setTouchable(true);
			// 实例化一个ColorDrawable颜色为半透明
			ColorDrawable dw = new ColorDrawable(0x1e000000);
			pop.setBackgroundDrawable(dw);
			// 设置popWindow的显示和消失动画
			pop.setAnimationStyle(R.style.mypopwindow_anim_style);
			// 在底部显示
			pop.setTouchInterceptor(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return false;
					// 这里如果返回true的话，touch事件将被拦截
					// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
				}
			});

			// 点击事件监听
			view.findViewById(R.id.ll_qrcode).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转到分享二维码界面
					Intent intent = new Intent(context, ShareQRCodeActivity.class);
					intent.putExtra("teacherId", teacherId);
					intent.putExtra("teacherImage", teacherLogo);
					intent.putExtra("type", 2);
					startActivity(intent);
					pop.dismiss();
				}
			});
			view.findViewById(R.id.ll_noqrcode).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转到直接分享导师名片
					if (StringUtil.isEmpty(teacherId) || StringUtil.isEmpty(teacherName) || StringUtil.isEmpty(teacherLogo)) {
						ShowUtils.showMsg(context, "数据获取失败,请检查您的网络!");
					}else {
						if (oks == null) {
							oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_TEACHER,teacherId,teacherName,teacherLogo,false,null,teacherName+"的导师名片");
						}
						oks.show(context);
					}
					pop.dismiss();
				}
			});
		}
		// 设置好参数之后再show
		pop.showAtLocation(this.findViewById(R.id.tv_teacher_position), Gravity.BOTTOM, 0, 0);
	}

}
