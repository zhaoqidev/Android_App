package cc.upedu.online.activity;

import java.util.LinkedHashMap;

import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.TabsBaseActivity;
import cc.upedu.online.fragment.MyCollectSportFragment;
import cc.upedu.online.fragment.MyJoinSportFragment;
import cc.upedu.online.fragment.MyStartSportFragment;
/**
 * 我的活动Activity
 * @author Administrator
 *
 */
public class MySportActivity extends TabsBaseActivity {

	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
		map.put("收藏的活动", new MyCollectSportFragment(context));
		map.put("参加的活动", new MyJoinSportFragment(context));
		map.put("我的活动", new MyStartSportFragment(context));
		return map;
	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的活动");
	}
}
