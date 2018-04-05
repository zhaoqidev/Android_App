package cc.upedu.online;


import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.lecloud.common.cde.LeCloud;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import cc.upedu.online.domin.LoginBean;
import cc.upedu.online.fragment.MainFragment;
import cc.upedu.online.fragment.MenuFragment;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cn.beecloud.BeeCloud;

public class MainActivity extends SlidingFragmentActivity {
    private SlidingMenu slidingMenu;
    private MenuFragment menuFragment;
    private MainFragment mainFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        LeCloud.init(getApplicationContext());
        super.onCreate(savedInstanceState);

        // 推荐在主Activity里的onCreate函数中初始化BeeCloud,初始化支付参数
        BeeCloud.setAppIdAndSecret("a25864ed-e096-4f18-a03a-7d13b8b79bdb", "b64dd061-ea40-4104-a271-f058ed2b3cec");

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // 取消标题栏
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.framelayout_content);
        setBehindContentView(R.layout.menu_frame);
        // 获取侧拉栏目对象
        slidingMenu = getSlidingMenu();
        // 侧拉栏在左侧
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 侧拉栏宽度
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 给侧边栏与内容页之间加条线
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        // 设置线的宽度
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置为全屏可点
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        menuFragment = new MenuFragment();

        getSupportFragmentManager() // 新建FragmentManager
                .beginTransaction() // 开启事务
                .replace(R.id.menu, menuFragment, "MENU")// Fragment替换当前
                .commit(); // 提交事务

        mainFragment = new MainFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mainFragment, "HOME").commit();

        //autoLogin();//执行自动登录操作
        //如果是分享过来的将参数保存
        Uri uri = getIntent().getData();
        if (uri != null) {
            System.out.println("++++++++uri" + uri);
            String courseId = uri.getQueryParameter("courseId");
            String shareUid = uri.getQueryParameter("shareUid");
            if ((!StringUtil.isEmpty(courseId)) && (!StringUtil.isEmpty(shareUid)) && (!shareUid.equals("0"))
                    && (!shareUid.equals(UserStateUtil.getUserId()))&& (!courseId.equals("0"))) {
//                SharedPreferencesUtil.getInstance().editPutString("share_courseId", courseId);//将分享的课程的id进行本地持久化
//                SharedPreferencesUtil.getInstance().editPutString("share_shareUid", shareUid);//将分享人进行本地持久化
                autoLogin(courseId,shareUid);
            }
        }

}

    /**
     * 在MainActivity中获取MenuFrament侧边栏对象
     */
    public MenuFragment switchMenufragment() {
        // 利用tag—>"MENU"获取MenuFragemnt对象
        return (MenuFragment) getSupportFragmentManager().findFragmentByTag(
                "MENU");
    }

    public MainFragment switchHomeFragment() {
        // 利用tag—>"MENU"获取MenuFragemnt对象
        return (MainFragment) getSupportFragmentManager().findFragmentByTag(
                "HOME");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            //判断搜索栏是否是在显示状态
            if (slidingMenu.isMenuShowing()) {
                slidingMenu.toggle();
                return true;
            } else if (mainFragment.isShowSearchBar()) {
                mainFragment.backSearch();
                return true;
            } else if (mainFragment.isDownMenu()) {
                mainFragment.backDownMenu();
                return true;
            } else if (mainFragment.isPopupWindow()) {
                mainFragment.backPopupWindow();
                return true;
            } else {
                ShowUtils.showDiaLog(MainActivity.this, "您确定要退出应用吗?", null, new ConfirmBackCall() {

                    @Override
                    public void confirmOperation() {
                        finish();
                        System.exit(0);
                    }
                });
                return true;
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        mainFragment.backPopupWindow();
        return super.onTouchEvent(event);
    }

    /**
     * 打开App自动登陆
     */
    private void autoLogin(String courseId,String shareUid) {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("account", SharedPreferencesUtil.getInstance().spGetString("mobile"));
        params.addQueryStringParameter("password", SharedPreferencesUtil.getInstance().spGetString("passWord"));
        params.addQueryStringParameter("brand", StringUtil.getPhoneBrand());
        params.addQueryStringParameter("type", StringUtil.getPhoneType());
        params.addQueryStringParameter("size", StringUtil.getScreenSize(MainActivity.this));
        params.addQueryStringParameter("courseId", courseId);
        params.addQueryStringParameter("shareUid", shareUid);
        params.addQueryStringParameter("system", "Android");

//        System.out.println("**************MainActivity:" + params.getQueryStringParams().toString());

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpMethod.GET, ConstantsOnline.LOGIN_URL, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                LoginBean bean = GsonUtil.jsonToBean(responseInfo.result, LoginBean.class);
                if (bean != null) {
                    if ("true".equals(bean.success)) {

                    } else {

                    }
                } else {
                    ShowUtils.showMsg(getApplicationContext(), "获取数据失败，请稍后重试");
                }
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                ShowUtils.showMsg(getApplicationContext(), "自动登录失败，请检查网络");
            }
        });

    }

//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		mainFragment.onResume();
//		super.onResume();
//	}

/*

	@Override
	protected void onResume() {

		//通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
		boolean hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
		boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

		if (!hasMenuKey ) {//&& !hasBackKey
			// 做任何你需要做的,这个设备有一个导航栏
			ShowUtils.showMsg(this,"有系统导航栏");
			Resources resources = this.getResources();
			int resourceId = resources.getIdentifier("navigation_bar_height",
					"dimen", "android");
			//获取NavigationBar的高度
			int height = resources.getDimensionPixelSize(resourceId);
			if (height>0){
				ShowUtils.showMsg(this,"导航栏"+height);
			}
		}else{
			ShowUtils.showMsg(this,"没有系统导航栏。。。");
		}

		super.onResume();
	}
*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LeCloud.destory();
    }

}