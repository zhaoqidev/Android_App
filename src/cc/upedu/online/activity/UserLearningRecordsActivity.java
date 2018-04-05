package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.MyRecordsListAdapter;
import cc.upedu.online.base.RecyclerViewBaseActivity;
import cc.upedu.online.domin.UserLearningRecordsBean;
import cc.upedu.online.domin.UserLearningRecordsBean.Entity.RecordsItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 用户学习记录界面
 * @author Administrator
 *
 */
public class UserLearningRecordsActivity extends RecyclerViewBaseActivity<RecordsItem>{

	private String userId;// 用户ID
	private UserLearningRecordsBean bean;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				if ("true".equals(bean.getSuccess())) {
					if (!isLoadMore()) {
						if (list==null) {
							list = new ArrayList<RecordsItem>();
						}else {
							list.clear();
						}
					}
					setData();
				} else {
					ShowUtils.showMsg(context, bean.getMessage());
				}
				setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
				break;
			}
		}
	};
	private void setData() {
		totalPage = bean.getEntity().getTotalPageSize();
		//判断是否可以加载下一页
		canLodeNextPage();
		list.addAll(bean.getEntity().getStudylist());
		if (isAdapterEmpty()) {
			setRecyclerView(new MyRecordsListAdapter(context, list));
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					//进入课程学习界面
					Intent intent = new Intent(context, CourseStudyActivity.class);
					intent.putExtra("courseId", list.get(position).getCourseId());
					intent.putExtra("courseName", list.get(position).getCourseName());
					intent.putExtra("courseLogo", list.get(position).getLogo());
					intent.putExtra("iscollected", list.get(position).getIscollected());
					intent.putExtra("kpointId", list.get(position).getKpointId());
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}
	
	private boolean isFistInitdata = true;
	@Override
	protected void onResume() {
		if (!isFistInitdata) {
			initData();
		}else {
			isFistInitdata = false;
		}
		super.onResume();
	}

	@Override
	protected void initData() {
		userId = UserStateUtil.getUserId();
		if (!StringUtil.isEmpty(userId)) {
			Map<String, String> requestDataMap = ParamsMapUtil.getCourseUserLearningRecords(context, userId, String.valueOf(currentPage));
			RequestVo requestVo = new RequestVo(ConstantsOnline.USERLEARNING_RECORDS, context, requestDataMap, new MyBaseParser<>(UserLearningRecordsBean.class));
			DataCallBack<UserLearningRecordsBean> userLearningRecordsDataCallBack = new DataCallBack<UserLearningRecordsBean>() {
				
				@Override
				public void processData(UserLearningRecordsBean object) {
					if (object==null) {
						objectIsNull();
					}else {
						bean = object;
						handler.obtainMessage(0).sendToTarget();
					}
				}
			};
			getDataServer(requestVo, userLearningRecordsDataCallBack);
		}else {
			ShowUtils.showMsg(context, "用户登录异常!");
		}
	}
	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}
	@Override
	protected void initTitle() {
		setTitleText("学习记录");
		
	}
}
