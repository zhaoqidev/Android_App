package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.MyRepresentAdapter;
import cc.upedu.online.base.RecyclerViewBaseActivity;
import cc.upedu.online.domin.MyRepresentBean;
import cc.upedu.online.domin.MyRepresentBean.CourseItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 我代言的课程
 * 
 * @author Administrator
 * 
 */
public class MyRepresentActivity extends RecyclerViewBaseActivity<CourseItem>{
	private MyRepresentBean bean = new MyRepresentBean();
	String userId;// 用户ID

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list!=null) {
						list.clear();
					}else {
						list = new ArrayList<CourseItem>();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.message);
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	
	private void setData() {
		totalPage = bean.entity.totalPageSize;
		//判断是否可以加载下一页
		canLodeNextPage();
		
		list.addAll(bean.entity.courseList);
		if (isAdapterEmpty()) {
			setRecyclerView(new MyRepresentAdapter(context, list));
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,CourseIntroduceActivity.class);
					intent.putExtra("courseId",list.get(position).courseId);
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}

	@Override
	protected void initData() {
		userId = getIntent().getStringExtra("userId");// 获取用户ID
		// 获取我的代言课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getMyRepresent(
				context, userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_REPREAENT,
				context, requestDataMap, new MyBaseParser<>(
						MyRepresentBean.class));
		DataCallBack<MyRepresentBean> dataCallBack = new DataCallBack<MyRepresentBean>() {
			@Override
			public void processData(MyRepresentBean object) {
				if (object == null) {
					objectIsNull();
				} else {
					list=new ArrayList<CourseItem>();
					bean = object;
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

	@Override
	protected void initTitle() {
		setTitleText("我的代言");
		
	}

}
