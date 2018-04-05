package cc.upedu.online.activity;

import java.util.List;

import cc.upedu.online.adapter.OneCourseOneUserNoteListAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.NoteItem;
/**
 * 我的笔记中单个课程中某个学友的笔记列表
 * @author Administrator
 *
 */
public class MyNoteOneCourseOneUserNoteListActivity extends ListBaseActivity<NoteItem> {
	private String courseName;
	private String courseId;
	private List<NoteItem> notelist;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		courseName = getIntent().getStringExtra("courseName");
		courseId = getIntent().getStringExtra("courseId");
		notelist = (List<NoteItem>)getIntent().getSerializableExtra("notelist");
		setTitleText(courseName);
	}
	@Override
	protected void initData() {
		if (isAdapterEmpty()) {
			setListDividerHeight(0);
			setListView(new OneCourseOneUserNoteListAdapter(context,notelist,courseId));
		}else {
			notifyData();
		}
	}
}
