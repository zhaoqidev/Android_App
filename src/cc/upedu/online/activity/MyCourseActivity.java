package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;

import java.util.Map;

import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.MyCourseListAdapter;
import cc.upedu.online.base.RecyclerViewBaseActivity;
import cc.upedu.online.domin.MyCourseBean;
import cc.upedu.online.domin.MyCourseBean.CourseItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 我的课程
 * 
 * @author Administrator
 * 
 */
public class MyCourseActivity extends RecyclerViewBaseActivity<CourseItem>{

		String userId;// 用户ID

		@Override
		protected void initData() {
			userId = getIntent().getStringExtra("userId");//获取用户ID
			// 获取课程列表的数据
			Map<String, String> requestDataMap = ParamsMapUtil.getMyCourse(context, String.valueOf(userId));
			RequestVo requestVo = new RequestVo(ConstantsOnline.MY_COURSE, context,
					requestDataMap, new MyBaseParser<>(MyCourseBean.class));
			DataCallBack<MyCourseBean> coursseDataCallBack = new DataCallBack<MyCourseBean>() {
				
				@Override
				public void processData(final MyCourseBean object) {
					
					if (object == null) {
						objectIsNull();
					} else {
						list = object.entity;
						if (isAdapterEmpty()) {
							setRecyclerView(new MyCourseListAdapter(context, list));
							setOnItemClick(new OnItemClickLitener() {
								
								@Override
								public void onItemClick(View view, int position) {
									// TODO Auto-generated method stub
									if ("LIVE".equals(list.get(position).courseType)) {
										Intent intent = new Intent(context, TelecastApplayActivity.class);
										intent.putExtra("courseId", list.get(position).courseId);
										context.startActivity(intent);
									}else {
										Intent intent = new Intent(context, CourseIntroduceActivity.class);
										intent.putExtra("courseId",list.get(position).courseId);
										context.startActivity(intent);
									}
								}
								
								@Override
								public void onItemLongClick(View view, int position) {
									// TODO Auto-generated method stub
									
								}
							});
						}else {
							notifyData();
						}
					}
					setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
				}
			};

			getDataServer(requestVo, coursseDataCallBack);

		}
		@Override
		public void onLoadMore() {
			ShowUtils.showMsg(context, "没有更多数据");
			setHasMore(false);
			setPullLoadMoreCompleted();
			
		}

		@Override
		protected void initTitle() {
			setTitleText("我的课程");
		}
		
		@Override
		protected void setPullLoadMoreRecyclerView() {
			setItemDecoration(true);
			
		}
}
