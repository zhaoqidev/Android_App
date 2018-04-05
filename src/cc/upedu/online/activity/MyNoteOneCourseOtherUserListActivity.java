package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.adapter.MyNoteCourseOtherUserListAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.OneCourseOtherUserListBean;
import cc.upedu.online.domin.OneCourseOtherUserListBean.StudentItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 我的笔记中单个课程中的有笔记的学友的学友列表
 * @author Administrator
 *
 */
public class MyNoteOneCourseOtherUserListActivity extends ListBaseActivity<StudentItem> {
	private String courseName;
	private String courseId;
	private String type;
	private String userId;
	private OneCourseOtherUserListBean mOneCourseOtherUserListBean;
	private List<StudentItem> studentList;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		courseName = getIntent().getStringExtra("courseName");
		courseId = getIntent().getStringExtra("courseId");
		type = getIntent().getStringExtra("type");
		userId = UserStateUtil.getUserId();//获取用户ID
//		setTitle(courseName);
		setTitleText(courseName);
	}
	/**
	 * 
	 */
	private void setData() {
		if (studentList == null) {
			studentList = new ArrayList<StudentItem>();
		}else {
			studentList.clear();
		}
		if (mOneCourseOtherUserListBean.getEntity().getStudentList() != null && mOneCourseOtherUserListBean.getEntity().getStudentList().size() > 0) {
			studentList.addAll(mOneCourseOtherUserListBean.getEntity().getStudentList());
		}
		if (isAdapterEmpty()) {
			setListView(new MyNoteCourseOtherUserListAdapter(context,studentList));
			setOnItemClickListion(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(context,
							MyNoteOneCourseOneUserNoteListActivity.class);
					intent.putExtra("courseName",courseName);
					intent.putExtra("courseId",courseId);
					intent.putExtra("notelist",(Serializable)studentList.get(position).getNoteList());
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mOneCourseOtherUserListBean.getSuccess())) {
				setData();
			} else {
				ShowUtils.showMsg(context, mOneCourseOtherUserListBean.getMessage());
			}
		}
	};
	@Override
	protected void initData() {
		Map<String, String> requestDataMap = ParamsMapUtil.getMyNoteOneCourse(
				context, userId, courseId,type, "1", "100");
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_NOTEONECOURSE,
				context, requestDataMap, new MyBaseParser<>(
						OneCourseOtherUserListBean.class));
		DataCallBack<OneCourseOtherUserListBean> dataCallBack = new DataCallBack<OneCourseOtherUserListBean>() {
			@Override
			public void processData(OneCourseOtherUserListBean object) {
				if (object == null) {
					ShowUtils.showMsg(context, "获取笔记数据失败请联系客服");
				} else {
					mOneCourseOtherUserListBean = object;
					handler.obtainMessage().sendToTarget();
//					String stringDate = StringUtil.getStringDate();
//					ptrlv.setLastUpdatedLabel(stringDate.subSequence(stringDate.indexOf("-")+1, stringDate.lastIndexOf(":")));
				}
			}
		};

		getDataServer(requestVo, dataCallBack);
	}
}
