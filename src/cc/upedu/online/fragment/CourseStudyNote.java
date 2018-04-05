package cc.upedu.online.fragment;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.StudyNoteAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.NoteListBean;
import cc.upedu.online.domin.NoteListBean.Entity.NoticeItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 课程学习中的笔记页
 * @author Administrator
 *
 */
public class CourseStudyNote extends RecyclerViewBaseFragment<NoticeItem> {
	private NoteListBean mNoticeListBean = new NoteListBean();

	public CourseStudyNote(Context context, String courseId) {
		super(context);
		this.courseId = courseId;
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mNoticeListBean.getSuccess())) {
				if (!isLoadMore()) {
					if (list==null) {
						list=new ArrayList<NoticeItem>();
					}else {
						list.clear();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, mNoticeListBean.getMessage());
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	private void setData() {
		totalPage = mNoticeListBean.getEntity().getTotalPage();

		//判断是否可以加载下一页
		canLodeNextPage();
		list.addAll(mNoticeListBean.getEntity().getNoteList());
		if (isAdapterEmpty()) {
			setRecyclerView(new StudyNoteAdapter(context,list));
		}else {
			notifyData();
		}
	}
	
	private String courseId;
	@Override
	public void initData() {
		//获取课程笔记列表的数据
		if (!StringUtil.isEmpty(courseId)) {
			Map<String, String> requestDataMap = ParamsMapUtil.getCourseNotice(context, String.valueOf(currentPage), courseId,UserStateUtil.getUserId());
			RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_NOTICES, context, requestDataMap, new MyBaseParser<>(NoteListBean.class));
			DataCallBack<NoteListBean> noticeDataCallBack = new DataCallBack<NoteListBean>() {
				
				@Override
				public void processData(NoteListBean object) {
					if (object==null) {
						objectIsNull();
					}else {
						mNoticeListBean = object;
						handler.obtainMessage().sendToTarget();
					}
				}
			};
			getDataServer(requestVo, noticeDataCallBack);
		}else {
			ShowUtils.showMsg(context, "请选择课程章节");
		}
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		 setItemDecoration(true);
		
	}
}
