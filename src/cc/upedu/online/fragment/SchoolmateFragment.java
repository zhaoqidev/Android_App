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
 * 学友页面的pager，这是一个默认的页面，里面嵌套着全部学友，同城学友，等多个页面pager。
 * @author Administrator
 *
 */
public class SchoolmateFragment extends BaseFragment {
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
	
	public SchoolmateFragment() {
		
	}
	
	public SchoolmateFragment(Context context) {
		fragmentList.add(new SchoolmateAllFragment(context));//所有和其他城市
		fragmentList.add(new SchoolmateCityFragment(context));//同城
		fragmentList.add(new SchoolmateIndustryFragment(context));//行业
	}

	@Override
	public View initView(LayoutInflater inflater) {
		mFragmentMan = getFragmentManager();
		View view=inflater.inflate(R.layout.layout_fragment_schoolmate, null);
		layout_schoolmate_content=(FrameLayout) view.findViewById(R.id.layout_schoolmate_content);
		return view;
	}
	private boolean isFirst = true;
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (actionList == null) {
			actionList = new ArrayList<String>();
			actionList.add("QuanbuXueyou");
			actionList.add("TongchengXueyou");
			actionList.add("HangyeXueyou");
			actionList.add("QitaXueyou");
		}
//		if (isFirst) {
//			isFirst = false;
//			switchPager(0, false);
//		}
	};
	private String searchText = null;
	private String extraData = null;
	public void seacherSchoolmate(String searchText){
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
					transaction.hide(from).add(R.id.layout_schoolmate_content, to).commit(); 
				}else {
					transaction.add(R.id.layout_schoolmate_content, to).commit();
				}
			} else {
				 // 隐藏当前的fragment，显示下一个  
				transaction.hide(from).show(to).commit();
			}  
		}else {
			if (i == 0 && !isSeacher) {
				((SchoolmateAllFragment) fragmentList.get(i)).getData();
			}else if (i == 2) {
				((SchoolmateIndustryFragment)fragmentList.get(i)).getData();
			}
		}  
	}
	private boolean isSeacher = false;
	/**
	 * 
	 * @param i 
	 * @param isSeacher
	 */
	public void switchPager(int i,boolean isSeacher) {
		this.isSeacher = isSeacher;
		Intent intent = new Intent(actionList.get(i));
		intent. setPackage("cc.upedu.online");
		context.sendBroadcast(intent);
		if (i == 3) {
			i = 0;
		}
		if (i == 0) {
			if (isSeacher) {
				((SchoolmateAllFragment)fragmentList.get(i)).seacherSchoolmate(searchText);
			}else {
				((SchoolmateAllFragment)fragmentList.get(i)).setExtraData(extraData);
			}
		}else if (i == 2) {
			((SchoolmateIndustryFragment)fragmentList.get(i)).setExtraData(extraData);
		}
		
        switchContent(currentFragment, i);
	}
	public void setExtraData(String extraData) {
		// TODO Auto-generated method stub
		this.extraData = extraData;
	}
//	public void onResume(int currentXueyouChild) {
//		if (currentXueyouChild == 0 || currentXueyouChild == 3) {
//			((SchoolmateAllFragment) fragmentList.get(0)).onResume();
//		}else if (currentXueyouChild == 1) {
//			((SchoolmateCityFragment) fragmentList.get(1)).onResume();
//		}else if (currentXueyouChild == 2) {
//			((SchoolmateIndustryFragment) fragmentList.get(2)).onResume();
//		}
//	}
}
