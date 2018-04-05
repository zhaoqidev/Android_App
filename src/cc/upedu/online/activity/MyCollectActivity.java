package cc.upedu.online.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import cc.upedu.online.R;
import cc.upedu.online.adapter.MyCourseListAdapter;
import cc.upedu.online.base.BaseActivity;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.fragment.CollectArticleFragment;
import cc.upedu.online.fragment.CollectCoureFragment;
import cc.upedu.online.utils.ShowUtils;

/**
 * 我的收藏
 * 
 * @author Administrator
 * 
 */
public class MyCollectActivity extends BaseActivity {
	TextView tv_right;// 右上角文字
	LinearLayout ll_back;
	private boolean isCoureCollect;
	String userId;// 用户ID
	// private MyCourseBean myCourseListBean;
	MyCourseListAdapter myCourseListAdapter;
//	private FrameLayout tabcontent;
	private RadioButton button_coure_collect;
	private RadioButton button_article_collect;
	private ArrayList<BaseFragment> fragmentList;
//	private RadioGroup collect_radiogroup;
	//是否正在管理内容
	private boolean isManaging = false;
	private MyCollectManagingRecive myCollectManagingRecive;
	/**
	 * 用于子界面切换的fragment管理器
	 */
	private FragmentManager mFragmentMan;
	/**
	 * 当前显示的fragment
	 */
	private BaseFragment currentFragment = null;


	@Override
	protected void initView() {
		setContentView(R.layout.activity_mycollect);
		mFragmentMan = getSupportFragmentManager();
		userId = getIntent().getStringExtra("userId");
		
		ll_back = (LinearLayout)findViewById(R.id.ll_back);
		tv_right = (TextView) findViewById(R.id.tv_right);
//		collect_radiogroup = (RadioGroup) findViewById(R.id.collect_radiogroup);
		button_coure_collect = (RadioButton) findViewById(R.id.button_coure_collect);
		button_article_collect = (RadioButton) findViewById(R.id.button_article_collect);
//		tabcontent = (FrameLayout) findViewById(R.id.tabcontent);
		
		fragmentList = new ArrayList<BaseFragment>();
		fragmentList.add(new CollectCoureFragment(context, userId));
		fragmentList.add(new CollectArticleFragment(context, userId));
		
		//初始化内容显示
		//设置默认展示的页面
		switchContent(currentFragment, fragmentList.get(0));
		isCoureCollect = true;
		
		myCollectManagingRecive = new MyCollectManagingRecive();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("CollectManaging");
		context.registerReceiver(myCollectManagingRecive, intentFilter);
	}
	@Override
	protected void initListener() {
		ll_back.setOnClickListener(this);//返回
		tv_right.setOnClickListener(this);//管理
		button_coure_collect.setOnClickListener(this);
		button_article_collect.setOnClickListener(this);
	}
//	@Override
//	protected void onResume() {
//		if (isCoureCollect) {
//			((CollectCourePager)fragmentList.get(0)).initData();
//		}else {
//			((CollectArticlePager)fragmentList.get(1)).initData();
//		}
//		super.onResume();
//	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_back:
			finish();
			break;
		case R.id.tv_right://处理管理界面的点击事件
			if (!isManaging) {
				//判断当前界面显示的是收藏课程还是收藏文章
				if (isCoureCollect) {
					if (((CollectCoureFragment)fragmentList.get(0)).isCanManagingCoure()) {
						tv_right.setText("完成");
						isManaging = !isManaging;
						//更新收藏课程界面把删除的图片显示出来
						((CollectCoureFragment)fragmentList.get(0)).refreshDataSetChanged(true);
					}
				}else {
					if (((CollectArticleFragment)fragmentList.get(1)).isCanManagingArticle()) {
						tv_right.setText("完成");
						isManaging = !isManaging;
						//更新收藏文章界面把删除的图片显示出来
						((CollectArticleFragment)fragmentList.get(1)).refreshDataSetChanged(true);
					}
				}
			}else {
				exitManaging();
			}
			break;
		case R.id.button_coure_collect:
			if (!isCoureCollect) {
//				tabcontent.removeAllViews();
				button_coure_collect.setBackgroundResource(R.drawable.button_bg_write_left);
				button_coure_collect.setTextColor(getResources().getColor(R.color.red_fe3d35));
				button_article_collect.setBackgroundResource(R.drawable.button_bg_red_right);
				button_article_collect.setTextColor(getResources().getColor(R.color.white));
				switchContent(currentFragment, fragmentList.get(0));
				isCoureCollect = !isCoureCollect;
				if (isManaging) {
					tv_right.setText("管理");
					isManaging = !isManaging;
					((CollectArticleFragment)fragmentList.get(1)).refreshDataSetChanged(false);
				}
			}
			break;
		case R.id.button_article_collect:
			if (isCoureCollect) {
//				tabcontent.removeAllViews();
				button_coure_collect.setBackgroundResource(R.drawable.button_bg_red_left);
				button_coure_collect.setTextColor(getResources().getColor(R.color.white));
				button_article_collect.setBackgroundResource(R.drawable.button_bg_write_right);
				button_article_collect.setTextColor(getResources().getColor(R.color.red_fe3d35));
				switchContent(currentFragment, fragmentList.get(1));
				isCoureCollect = !isCoureCollect;
				if (isManaging) {
					tv_right.setText("管理");
					isManaging = !isManaging;
					((CollectCoureFragment)fragmentList.get(0)).refreshDataSetChanged(false);
				}
			}
			break;
		}
	}
	/**
	 * 
	 */
	private void exitManaging() {
		//判断当前界面显示的是收藏课程还是收藏文章
		if (isCoureCollect) {
			if (((CollectCoureFragment)fragmentList.get(0)).isCanManagingCoure()) {
				tv_right.setText("管理");
				isManaging = !isManaging;
				//更新收藏课程界面把删除的图片隐藏
				((CollectCoureFragment)fragmentList.get(0)).refreshDataSetChanged(false);
			}else {
				ShowUtils.showMsg(context, "暂无数据,不能开启管理功能!");
			}
		}else {
			if (((CollectArticleFragment)fragmentList.get(1)).isCanManagingArticle()) {
				tv_right.setText("管理");
				isManaging = !isManaging;
				//更新收藏文章界面把删除的图片隐藏
				((CollectArticleFragment)fragmentList.get(1)).refreshDataSetChanged(false);
			}else {
				ShowUtils.showMsg(context, "暂无数据,不能开启管理功能!");
			}
		}
	}
	/**
	 * 切换显示数据的fragment
	 * @param from 切换时被覆盖的fragment
	 * @param to 切换时要加载的fragment
	 */
	public void switchContent(BaseFragment from, BaseFragment to) {
		if (currentFragment != to) {  
			currentFragment = to;
			FragmentTransaction transaction = mFragmentMan.beginTransaction().setCustomAnimations(  
					android.R.anim.fade_in, android.R.anim.fade_out);  
			if (!to.isAdded()) {    // 先判断是否被add过  
				if (from != null) {
					// 隐藏当前的fragment，add下一个到Activity中  
					transaction.hide(from).add(R.id.tabcontent, to).commit(); 
				}else {
					transaction.add(R.id.tabcontent, to).commit();
				}
			} else {
				 // 隐藏当前的fragment，显示下一个  
				transaction.hide(from).show(to).commit();
			}  
		}  
	}
	/**
	 * 自定义的广播接受者,用于在用户清空收藏时管理功能的自动切换
	 * @author Administrator
	 *
	 */
	class MyCollectManagingRecive extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			tv_right.setText("管理");
			isManaging = !isManaging;
			if (isCoureCollect) {
				((CollectCoureFragment)fragmentList.get(0)).refreshDataSetChanged(false);
			}else {
				((CollectArticleFragment)fragmentList.get(1)).refreshDataSetChanged(false);
			}
		}
	}
	@Override
	protected void onDestroy() {
		context.unregisterReceiver(myCollectManagingRecive);
		super.onDestroy();
	}
	@Override
	protected void initData() {
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (isManaging) {
        		exitManaging();
        		return false;
			}else {
	        	return super.onKeyDown(keyCode, event); 
			}
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
}
