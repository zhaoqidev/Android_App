package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.CourseIntroduceActivity;
import cc.upedu.online.activity.TelecastApplayActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.DaoshiCourseAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.DaoshiCourseBean;
import cc.upedu.online.domin.DaoshiCourseBean.CourseItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 导师名片页面的课程列表
 * @author Administrator
 *
 */
public class DaoshiCourseFragment extends RecyclerViewBaseFragment<CourseItem>/* implements PullLoadMoreListener*/{
	private  DaoshiCourseBean mDaoshiCourseBean = new DaoshiCourseBean();
	
	String teacherId;
	

	public DaoshiCourseFragment(Context context) {
		super(context);
	}
	public DaoshiCourseFragment() {
		super();
	}


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mDaoshiCourseBean.success)) {
				if (!isLoadMore()) {
					if (list==null) {
						list=new ArrayList<CourseItem>();
					}else {
						list.clear();
					}
				}
				setData();
			}else {
				ShowUtils.showMsg(context, mDaoshiCourseBean.message);
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	
	private void setData() {
		totalPage = mDaoshiCourseBean.entity.totalPage;

		canLodeNextPage();
		list.addAll(mDaoshiCourseBean.entity.courseList);
		
		if (isAdapterEmpty()) {
			setRecyclerView(new DaoshiCourseAdapter(context, list));
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					if ("LIVE".equals(list.get(position).courseType)) {
						Intent intent = new Intent(context, TelecastApplayActivity.class);
						intent.putExtra("courseId", list.get(position).courseId);
						context.startActivity(intent);
					}else {
						Intent intent = new Intent(context, CourseIntroduceActivity.class);
						intent.putExtra("courseId", list.get(position).courseId);
						context.startActivity(intent);
					}
				}
			});
		}else {
			notifyData();
		}
	}

	@Override
	public void initData() {
		teacherId=SharedPreferencesUtil.getInstance().spGetString("teacherId", "0");
		// 获取课程列表的数据
				Map<String, String> requestDataMap = ParamsMapUtil.DaoshiCouse(context, teacherId, String.valueOf(currentPage));
				RequestVo requestVo = new RequestVo(ConstantsOnline.DAOSHI_CAUSRE,
						context, requestDataMap, new MyBaseParser<>(DaoshiCourseBean.class));
				DataCallBack<DaoshiCourseBean> dataCallBack = new DataCallBack<DaoshiCourseBean>() {
					@Override
					public void processData(DaoshiCourseBean object) {
						if (object == null) {
							objectIsNull();
						} else {
							mDaoshiCourseBean = object;
							handler.obtainMessage().sendToTarget();
						}
					}
				};
				getDataServer(requestVo, dataCallBack);
		
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}
}
