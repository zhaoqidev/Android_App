package cc.upedu.online.activity;

import java.util.LinkedHashMap;

import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.TabsBaseActivity;
import cc.upedu.online.fragment.MyTextAnswerFragment;
import cc.upedu.online.fragment.MyVideoAnswerFragment;

/**
 * 我的课程相关的答疑
 * 
 * @author Administrator
 * 
 */
public class MyStudyAnswerActivity extends TabsBaseActivity {
	private String courseId;

	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
		map.put("导师文字答疑", new MyTextAnswerFragment(context, courseId));
		map.put("导师视频答疑", new MyVideoAnswerFragment(context, courseId));
		return map;
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的答疑");
		courseId = getIntent().getStringExtra("courseId");
	}
}
