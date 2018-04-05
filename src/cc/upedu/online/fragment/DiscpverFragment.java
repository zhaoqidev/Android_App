package cc.upedu.online.fragment;

import android.content.Intent;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cc.upedu.online.MainActivity;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.TabsBaseFragment;

/**
 * 发现页面
 * 
 * @author Administrator
 * 
 */
public class DiscpverFragment extends TabsBaseFragment {
	private List<String> titleAction = new ArrayList<String>();// 存储4个小标题“智库”“导师”“学友”“活动”的广播action
	List<BaseFragment> discpverFragmentList = new ArrayList<BaseFragment>();
	private Intent intent;
//	private MainFragment mainFragment;
	
	public DiscpverFragment(){
		super();
	}
	
//	public DiscpverFragment(MainFragment mainFragment) {
//		this.mainFragment = mainFragment;
//	}

	private boolean isFirst = true;
	private boolean isFirstSchoolmate = true;
	private boolean isFirstSport = true;
	@Override
	public void initData() {
		super.initData();
		if (isFirst) {
			isFirst = false;
			setViewPagerChangeListener(new OnPageChangeListener() {
	
				@Override
				public void onPageSelected(int arg0) {
					// 发广播修改titlebar上的标题信息
					intent = new Intent(titleAction.get(arg0));
					intent. setPackage("cc.upedu.online");
					context.sendBroadcast(intent);
					// 只有在第一个页面的时候才能进行拖拽出左侧侧拉栏。
					if (arg0 == 0) {
						((MainActivity) context).getSlidingMenu()
								.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
					} else {
						((MainActivity) context).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
					
					}
					if (arg0 == 1 && isFirstSchoolmate) {
						isFirstSchoolmate = false;
						((SchoolmateFragment)discpverFragmentList.get(1)).setExtraData("");
						((SchoolmateFragment)discpverFragmentList.get(1)).switchPager(0, false);
					}else if (arg0 == 2 && isFirstSport) {
						isFirstSport = false;
						((SportFragment)discpverFragmentList.get(2)).setExtraData("");
						((SportFragment)discpverFragmentList.get(2)).switchPager(0, false);
					}
				}
	
				@Override
				public void onPageScrollStateChanged(int arg0) {
					// TODO Auto-generated method stub
	
				}
	
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					// TODO Auto-generated method stub
	
				}
			});
	
			// 设置默认展示的页面
			intent = new Intent("DaoshiPager");
			intent. setPackage("cc.upedu.online");
			context.sendBroadcast(intent);
		}
	}
	
	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// titleAction.add("ZhikuPager");
		titleAction.clear();
		titleAction.add("DaoshiPager");
		titleAction.add("XueyouPager");
		titleAction.add("HuodongPager");
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
//		map.put("智库", new ZhikuPager(context));// 添加指针的条目
		DaoshiFragment daoshiFragment = new DaoshiFragment(context);
		SchoolmateFragment schoolmateFragment = new SchoolmateFragment(context);
		SportFragment sportFragment = new SportFragment(context);
		
		discpverFragmentList.add(daoshiFragment);
		discpverFragmentList.add(schoolmateFragment);
		discpverFragmentList.add(sportFragment);
		
		map.put("导师", daoshiFragment);
		map.put("学友", schoolmateFragment);
		map.put("活动", sportFragment);
		return map;
	}
	public List<BaseFragment> getFragmentList() {
		// TODO Auto-generated method stub
		return discpverFragmentList;
	}
}
