package cc.upedu.online.activity;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.adapter.OneCourseListAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.NoteItem;
import cc.upedu.online.domin.OneCourseNoteBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 我的笔记中单个课程中的章节笔记列表
 * @author Administrator
 *
 */
public class MyNoteOneCourseListActivity extends ListBaseActivity<NoteItem> {
	private String courseName;
	private String courseId;
	private String type;
	private String userId;
	private OneCourseNoteBean mOneCourseNoteBean;
	private List<NoteItem> noteList;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		courseName = getIntent().getStringExtra("courseName");
		courseId = getIntent().getStringExtra("courseId");
		type = getIntent().getStringExtra("type");
		userId = UserStateUtil.getUserId();//获取用户ID
		setTitleText(courseName);
	}

	/**
	 * 
	 */
	private void setData() {
		if (noteList == null) {
			noteList = new ArrayList<NoteItem>();
		}else {
			noteList.clear();
		}
		if (mOneCourseNoteBean.getEntity().getNoteList() != null && mOneCourseNoteBean.getEntity().getNoteList().size() > 0) {
			noteList.addAll(mOneCourseNoteBean.getEntity().getNoteList());
		}
		if (isAdapterEmpty()) {
			setListDividerHeight(0);
			setListView(new OneCourseListAdapter(context,noteList,courseId,type));
		}else {
			notifyData();
		}
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mOneCourseNoteBean.getSuccess())) {
				setData();
			} else {
				ShowUtils.showMsg(context, mOneCourseNoteBean.getMessage());
			}
		}
	};
	@Override
	protected void initData() {
		Map<String, String> requestDataMap = ParamsMapUtil.getMyNoteOneCourse(
				context, userId, courseId,type, "1", "100");
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_NOTEONECOURSE,
				context, requestDataMap, new MyBaseParser<>(
						OneCourseNoteBean.class));
		DataCallBack<OneCourseNoteBean> dataCallBack = new DataCallBack<OneCourseNoteBean>() {
			@Override
			public void processData(OneCourseNoteBean object) {
				if (object == null) {
					ShowUtils.showMsg(context, "获取笔记数据失败请联系客服");
				} else {
					mOneCourseNoteBean = object;
					handler.obtainMessage().sendToTarget();
//					String stringDate = StringUtil.getStringDate();
//					ptrlv.setLastUpdatedLabel(stringDate.subSequence(stringDate.indexOf("-")+1, stringDate.lastIndexOf(":")));
				}
			}
		};

		getDataServer(requestVo, dataCallBack);
	}
}
