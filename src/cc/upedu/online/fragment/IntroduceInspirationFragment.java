package cc.upedu.online.fragment;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.CourseInspirationAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.CourseAssessListBean;
import cc.upedu.online.domin.CourseAssessListBean.Entity.CourseAssessItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 课程介绍界面中的课程感悟Fragment
 * 
 * @author Administrator
 * 
 */
public class IntroduceInspirationFragment extends RecyclerViewBaseFragment<CourseAssessItem> {
	private String courseId;
	public IntroduceInspirationFragment(Context context, String courseId) {
		super(context);
		this.courseId = courseId;
	}


	private CourseAssessListBean mCourseAssessListBean;


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mCourseAssessListBean.getSuccess())) {
				if (!isLoadMore()) {
					if (list==null) {
						list = new ArrayList<CourseAssessListBean.Entity.CourseAssessItem>();
					}else {
						list.clear();
					}
				}
				setData();
			}else {
				ShowUtils.showMsg(context, mCourseAssessListBean.getMessage());
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};

	private void setData() {
		totalPage = mCourseAssessListBean.getEntity().getTotalPage();

		canLodeNextPage();
		list.addAll(mCourseAssessListBean.getEntity().getCourseAssessList());
		
		if (isAdapterEmpty()) {
			setRecyclerView(new CourseInspirationAdapter(context, list));
		}else {
			notifyData();
		}
	}

	@Override
	public void initData() {
		// 获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getCourseAssess(
				context, courseId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_ASSESS,
				context, requestDataMap, new MyBaseParser<>(
						CourseAssessListBean.class));
		DataCallBack<CourseAssessListBean> coursseAssessDataCallBack = new DataCallBack<CourseAssessListBean>() {

			@Override
			public void processData(CourseAssessListBean object) {
				if (object == null) {
					objectIsNull();
				} else {
					mCourseAssessListBean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, coursseAssessDataCallBack);
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
	}
}
