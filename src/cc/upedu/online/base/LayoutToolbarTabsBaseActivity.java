package cc.upedu.online.base;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Iterator;
import java.util.LinkedHashMap;

import cc.upedu.online.R;
import cc.upedu.online.adapter.ViewPagerAdapter;

/**
 * 带toolbar,视频和tabs的activity
 * 
 * @author Administrator
 *
 */
public abstract class LayoutToolbarTabsBaseActivity extends TitleBaseActivity implements
		android.support.design.widget.AppBarLayout.OnOffsetChangedListener {
	// 课程简介视频的布局
	private LinearLayout ll_top,ll_fragment_bottom;
	private CollapsingToolbarLayout collapsingToolbar;//导师名片向上滚动的布局
	private Toolbar toolbar;//标题栏
	private TextView toolbar_title;//标题栏课程名
	private TabLayout tabs;//指针控件
	private ViewPager viewPager;
	private AppBarLayout appBarLayout;
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.layout_toolbar_tabs);
		getTitleView();
		ll_top = (LinearLayout) findViewById(R.id.ll_top);
		View layoutTop = initLayoutTop();
		if (layoutTop != null) {
			ll_top.setVisibility(View.VISIBLE);
			ll_top.addView(layoutTop);
		}else {
			ll_top.setVisibility(View.GONE);
		}
		
		ll_fragment_bottom = (LinearLayout) findViewById(R.id.ll_fragment_bottom);
		View fragmentBottomView = initFragmentBottomView();
		if (fragmentBottomView != null) {
			ll_fragment_bottom.setVisibility(View.VISIBLE);
			ll_fragment_bottom.addView(fragmentBottomView);
		}else {
			ll_fragment_bottom.setVisibility(View.GONE);
		}
		
		appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
		tabs = (TabLayout) findViewById(R.id.tabs);
//		coordinatorLayout= (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		viewPager = (ViewPager) findViewById(R.id.pager);
		
		collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
		collapsingToolbar.setTitleEnabled(false);//控制是否是漂浮文字
		toolbar = (Toolbar) this.findViewById(R.id.tool_bar);
		toolbar_title=(TextView) findViewById(R.id.toolbar_title);
	    setToolbar();
	}

	/**
	 * 得到一个可以kdy为string,value为fragment的map集合,用于关联控件tablayout和viewpager
	 * @return key为tabs中的一项的文本,value为与之相对应的fragment
	 */
	public abstract LinkedHashMap<String, BaseFragment> setupViewPager();
	
	public void initTabsViewPager() {
		LinkedHashMap<String, BaseFragment> map = setupViewPager();
		if (map != null) {
			int size = map.size();
			if (size > 0) {
				ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
				
				Iterator it = map.keySet().iterator();
				while (it.hasNext()) {
					//it.next()得到的是key，tm.get(key)得到obj
					String key = (String) it.next();
					adapter.addFrag(map.get(key), key);
				}
				
				viewPager.setAdapter(adapter);
				tabs.setupWithViewPager(viewPager);
			}
		}
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		return null;
	}
	private void setToolbar(){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
//      actionBar.setHomeAsUpIndicator(android.R.drawable.ic_input_delete);
//      actionBar.setTitle(teacherName);
//        toolbar_title.setText(courseName);
        actionBar.setDisplayHomeAsUpEnabled(false);//是否显示返回箭头

        toolbar.setClickable(false);
        toolbar.setFocusable(false);
	}
	public void SetToolbarText(String toolbarText){
		toolbar_title.setText(toolbarText);
	}
	public abstract View initLayoutTop();
	/**
	 * 初始化界面上方且在底部不随界面滚动的布局
	 * 默认为隐藏
	 * @return
	 */
	public abstract View initFragmentBottomView();
	/**
	 * 设置界面上方且在底部不随界面滚动的布局的显示和隐藏
	 * @param visibility
	 */
	public void setFragmentBottomVisibility(int visibility){
		ll_fragment_bottom.setVisibility(visibility);
	}
	//监听AppBarLayout是收起状态还是展开状态
		@Override
		public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
			if (i == 0) {
				// 展开状态，可刷新
//				if (introduceInspirationFragment != null ) {
//					introduceInspirationFragment.setPullRefreshEnable(true);
//				}
				if (toolbar!=null) {
					toolbar.setVisibility(View.GONE);
				}

			} else {
				// 收起状态，不可刷新
//				if (introduceInspirationFragment != null ) {
//					introduceInspirationFragment.setPullRefreshEnable(false);
//				}
				if (toolbar!=null) {
					toolbar.setVisibility(View.VISIBLE);
				}
			}
			setAppBarLayoutChanged(i);
		}
		/**
		 * 监听AppBarLayout是收起状态还是展开状态时的操作
		 * 可用于设置是否可以刷新
		 * @param i 0表示展开状态,1表示不可收起状态
		 */
		public abstract void setAppBarLayoutChanged(int i);
		@Override
	    protected void onResume() {
	        super.onResume();
	        appBarLayout.addOnOffsetChangedListener(this);//页面显示，监听AppBarLayout是否收起
	    }
	 
	    @Override
	    protected void onPause() {
	        super.onPause();
	        appBarLayout.removeOnOffsetChangedListener(this);//页面消失，不再监听AppBarLayout是否收起
	    }
		
}
