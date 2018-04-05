package cc.upedu.online.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.NoteItem;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
/**
 * 我的笔记中单个课程中的单个章节的布局的详情
 * @author Administrator
 *
 */
public class MyNoteOneCourseDetailsActivity extends TitleBaseActivity {
	private List<NoteItem> noteList;
	private LinearLayout bt_up,bt_down;
	private int currentPosition;
	private TextView note_content;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		noteList = (List<NoteItem>) getIntent().getSerializableExtra("noteList");
		currentPosition = getIntent().getIntExtra("position", 0);
		if (StringUtil.isEmpty(noteList.get(currentPosition).getKpointName())) {
			setTitleText("章节标题");
		}else {
			setTitleText(noteList.get(currentPosition).getKpointName());
		}
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		View view = View.inflate(context, R.layout.activity_onecoursenote_itemdetails, null);
		note_content = (TextView) view.findViewById(R.id.note_content);
		bt_up = (LinearLayout) view.findViewById(R.id.bt_up);
		bt_down = (LinearLayout) view.findViewById(R.id.bt_down);
		return view;
	}
	@Override
	protected void initData() {
		if (!StringUtil.isEmpty(noteList.get(currentPosition).getCourseContent())) {
			note_content.setText(noteList.get(currentPosition).getCourseContent());
		}else {
			note_content.setText("");
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		bt_up.setOnClickListener(this);
		bt_down.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_up:
			if (currentPosition > 0) {
				currentPosition--;
				bt_down.setEnabled(true);
				initData();
			}else {
				ShowUtils.showMsg(context, "这是第一个笔记!");
				bt_up.setEnabled(false);
			}
			break;
		case R.id.bt_down:
			if (currentPosition < noteList.size() - 1) {
				currentPosition++;
				bt_up.setEnabled(true);
				initData();
			}else {
				ShowUtils.showMsg(context, "已经到最后一个笔记了!");
				bt_down.setEnabled(false);
			}
			break;
		}
	}
}
