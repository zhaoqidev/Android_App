package cc.upedu.online.fragment;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.StudyQuestionAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.QuestionListBean;
import cc.upedu.online.domin.QuestionListBean.Entity.QuestionItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 课程学习中的提问页
 * @author Administrator
 *
 */
public class CourseStudyQuestion extends RecyclerViewBaseFragment<QuestionItem> {
	private QuestionListBean mQuestionListBean;
	
	public CourseStudyQuestion(Context context, String courseId) {
		super(context);
		this.courseId = courseId;
	}
	

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mQuestionListBean.getSuccess())) {
				if (!isLoadMore()) {
					if (list==null) {
						list = new ArrayList<QuestionItem>();
					}else {
						list.clear();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, mQuestionListBean.getMessage());
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	private void setData() {
		totalPage = mQuestionListBean.getEntity().getTotalPage();

		//判断是否可以加载下一页
		canLodeNextPage();
		list.addAll(mQuestionListBean.getEntity().getQuestionList());
		if (isAdapterEmpty()) {
			setRecyclerView(new StudyQuestionAdapter(context, list));
		}else {
			notifyData();
		}
	}
	private String courseId;
	@Override
	public void initData() {
		//获取课程列表的数据
		if (!StringUtil.isEmpty(courseId)) {
			Map<String, String> requestDataMap = ParamsMapUtil.getCourseQuestion(context, String.valueOf(currentPage), courseId,UserStateUtil.getUserId());
			RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_QUESTIONS, context, requestDataMap, new MyBaseParser<>(QuestionListBean.class));
			DataCallBack<QuestionListBean> questionDataCallBack = new DataCallBack<QuestionListBean>() {
				
				@Override
				public void processData(QuestionListBean object) {
					if (object==null) {
					objectIsNull();
					}else {
						mQuestionListBean = object;
						handler.obtainMessage().sendToTarget();
					}
				}
			};
			getDataServer(requestVo, questionDataCallBack);
		}else {
			ShowUtils.showMsg(context, "暂时没有数据");
		}
	}
	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}
}
