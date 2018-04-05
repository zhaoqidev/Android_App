package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;

import java.util.LinkedHashMap;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.base.TabsBaseActivity;
import cc.upedu.online.fragment.MyNoteCourseFragment;
import cc.upedu.online.fragment.MyNoteOtherUserFragment;
import cc.upedu.online.fragment.MyNoteSharFragment;
import cc.upedu.online.interfaces.OnClickMyListener;

/**
 * 我的笔记
 * 
 * @author Administrator
 * 
 */
public class MyNoteActivity extends TabsBaseActivity{
	@Override
	public LinkedHashMap<String, BaseFragment> setupViewPager() {
		// TODO Auto-generated method stub
		LinkedHashMap<String, BaseFragment> map = new LinkedHashMap<String, BaseFragment>();
		map.put("课程笔记",new MyNoteCourseFragment(context));
		map.put("他人笔记",new MyNoteOtherUserFragment(context));
		map.put("我的分享",new MyNoteSharFragment(context));
		return map;
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的笔记");
		setRightButton(R.drawable.note_blag_records, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MyNoteBlagRecordsActivity.class);
				startActivity(intent);
			}
		});
	}
}