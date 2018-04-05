package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
/**
 * 发现页面的活动的pager，这是一个默认的页面，里面嵌套着全部活动，同城活动，其他城市的三个页面pager。
 * @author Administrator
 *
 */
public class SportFragment extends BaseFragment {
	FrameLayout layout_schoolmate_content;//用来被填充的布局
	List<BaseFragment> fragmentList=new ArrayList<BaseFragment>();//存储Fragment的List。
	/**
	 * 用于子界面切换的fragment管理器
	 */
	private FragmentManager mFragmentMan;
	/**
	 * 当前显示的fragment
	 */
	private BaseFragment currentFragment = null;
	private List<String> actionList;

	public SportFragment(Context context) {
		// TODO Auto-generated constructor stub
		fragmentList.add(new SportAllFragment(context));//所有和其他
		fragmentList.add(new SportCityFragment(context));//同城
		//pagerList.add(new SportCityPager(context));
		//pagerList.add(new SchoolmateCityPager(context));
		
	}

	@Override
	public View initView(LayoutInflater inflater) {
		mFragmentMan = getFragmentManager();
		View view=inflater.inflate(R.layout.layout_fragment_sport, null);
		layout_schoolmate_content=(FrameLayout) view.findViewById(R.id.layout_sport_content);
		return view;
	}
	private boolean isFirst = true;
	@Override
	public void initData() {
		if (actionList == null) {
			actionList = new ArrayList<String>();
			actionList.add("SuoyouHuodong");
			actionList.add("TongchengHuodong");
			actionList.add("QitaHuodong");
		}
//		if (isFirst) {
//			isFirst = false;
//			switchPager(0, false);
//		}
	}
	private String searchText;
	private String extraData;
	public void seacherSport(String searchText){
		this.searchText = searchText;
		switchPager(0,true);
	}
	/**
	 * 切换显示数据的fragment
	 * @param from 切换时被覆盖的fragment
	 * @param to 切换时要加载的fragment
	 */
	public void switchContent(BaseFragment from, int i) {
		BaseFragment to = fragmentList.get(i);
		if (currentFragment != to) {
			currentFragment = to;
			FragmentTransaction transaction = mFragmentMan.beginTransaction().setCustomAnimations(  
					android.R.anim.fade_in, android.R.anim.fade_out);  
			if (!to.isAdded()) {    // 先判断是否被add过  
				if (from != null) {
					// 隐藏当前的fragment，add下一个到Activity中  
					transaction.hide(from).add(R.id.layout_sport_content, to).commit(); 
				}else {
					transaction.add(R.id.layout_sport_content, to).commit();
				}
			} else {
				 // 隐藏当前的fragment，显示下一个  
				transaction.hide(from).show(to).commit();
			}  
		}else {
			if (i == 0) {
				((SportAllFragment) fragmentList.get(i)).getData();
			}
		}
	}
	private boolean isSeacher = false;
	/**
	 * 
	 * @param i 取值0-2,0表示所有,1表示同城,2表示其他城市活动
	 * @param isSeacher
	 */
	public void switchPager(int i,boolean isSeacher) {
		this.isSeacher = isSeacher;
		Intent intent = new Intent(actionList.get(i));
		intent. setPackage("cc.upedu.online");
		context.sendBroadcast(intent);
		if (i == 2) {
			i = 0;
		}

		if (i == 0) {
			if (isSeacher) {
				((SportAllFragment) fragmentList.get(i)).seacherSport(searchText);
			}else {
				((SportAllFragment)fragmentList.get(i)).setExtraData(extraData);
			}
		}else if (i == 1) {
			((SportCityFragment) fragmentList.get(i)).setExtraData(extraData);
		}
		switchContent(currentFragment, i);
	}
	public void setExtraData(String extraData) {
		// TODO Auto-generated method stub
		this.extraData = extraData;
	}
//	public void onResume(int currentHuodongChild) {
//		// TODO Auto-generated method stub
//		if (currentHuodongChild == 0 || currentHuodongChild == 2) {
//			((SportAllFragment) fragmentList.get(0)).onResume();
//		}else if (currentHuodongChild == 1) {
//			((SportCityFragment) fragmentList.get(1)).onResume();
//		}
//	}
}
