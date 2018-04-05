package cc.upedu.online.base;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedHashMap;

import cc.upedu.online.R;
import cc.upedu.online.adapter.ViewPagerAdapter;

public abstract class TabsBaseFragment extends TwoPartModelTopBaseFragment {
	private TabLayout tabs;//指针控件
	private ViewPager viewPager;

	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		tabs = (TabLayout) View.inflate(context, R.layout.layout_tabs, null);
		return tabs;
	}

	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) View.inflate(context, R.layout.layout_viewpager, null);
		return viewPager;
	}
	/**
	 * 得到一个可以kdy为string,value为fragment的map集合,用于关联控件tablayout和viewpager
	 * @return key为tabs中的一项的文本,value为与之相对应的fragment
	 */
	public abstract LinkedHashMap<String, BaseFragment> setupViewPager();
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = setupViewPager();
		if (map != null) {
			int size = map.size();
			if (size > 0) {
				ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
				
				Iterator it = map.keySet().iterator();
				while (it.hasNext()) {
					//it.next()得到的是key，tm.get(key)得到obj
					String key = (String) it.next();
					adapter.addFrag(map.get(key), key);
				}

				viewPager.setOffscreenPageLimit(size);
				viewPager.setAdapter(adapter);
				tabs.setupWithViewPager(viewPager);
			}
		}
	}
	
	public void setViewPagerChangeListener(OnPageChangeListener listener) {
		viewPager.setOnPageChangeListener(listener);
	}
}
