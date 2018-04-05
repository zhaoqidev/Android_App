
package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.CourseIntroduceActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.CourseListAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.CourseItem;
import cc.upedu.online.domin.CourseListBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 课程体系中的课程界面
 * @author Administrator
 *
 */
public class ArchitectureCourseFragment extends RecyclerViewBaseFragment<CourseItem>{
	private CourseListBean bean;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.getSuccess())) {
				if (!isLoadMore()) {
					if (list==null) {
						list = new ArrayList<CourseItem>();
					}else {
						list.clear();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.getMessage());
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	private String subjectId;
	private void setData() {
		totalPage = bean.getEntity().gettotalPage();
		//判断是否可以加载下一页
		canLodeNextPage();
		list.addAll(bean.getEntity().getCourseList());
		if (isAdapterEmpty()) {
			setRecyclerView(new CourseListAdapter(context, list));
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, CourseIntroduceActivity.class);
					intent.putExtra("courseId", list.get(position).getCourseId());
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}
	
	public ArchitectureCourseFragment(Context context,String subjectId) {
		super(context);
		this.subjectId = subjectId;
	}

	@Override
	public void initData() {
		//获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getSubjectCourse(context, subjectId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_SUBJECT, context, requestDataMap, new MyBaseParser<>(CourseListBean.class));
		DataCallBack<CourseListBean> caursseDataCallBack = new DataCallBack<CourseListBean>() {
			@Override
			public void processData(CourseListBean object) {
				if (object==null) {
					objectIsNull();
				}else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, caursseDataCallBack);
	}
	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}

}
