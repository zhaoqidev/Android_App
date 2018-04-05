package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.SportIssueActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.SportAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.SportsBean;
import cc.upedu.online.domin.SportsBean.ActivityItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 侧拉栏，我的活动-发起的活动的pager
 * 
 * @author Administrator
 * 
 */
public class MyStartSportFragment extends RecyclerViewBaseFragment<ActivityItem> {
	private String userId;
	private String type;

	// 活动列表的Javabean文件
	private SportsBean bean;
	public MyStartSportFragment(){

	}
	public MyStartSportFragment(Context context) {
		super(context);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list==null) {
						 list = new ArrayList<ActivityItem>();// 存储活动条目
					}else {
						list.clear();
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
		if(bean.entity.activityList != null){
			list.addAll(bean.entity.activityList);
		}

		if (isAdapterEmpty()) {
			setRecyclerView(new SportAdapter(context, list),R.drawable.nostart_sportdata);
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,SportIssueActivity.class);
					intent.putExtra("aid", list.get(position).id);
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}

	@Override
	public void initData() {

		userId = UserStateUtil.getUserId();
		type = "2";
		// 获取课程列表的数据
		Map<String, String> requestDataMap;
		requestDataMap = ParamsMapUtil.MySport(context, userId, type,
				String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_SPORT, context,
				requestDataMap, new MyBaseParser<>(SportsBean.class));
		DataCallBack<SportsBean> dataCallBack = new DataCallBack<SportsBean>() {
			@Override
			public void processData(SportsBean object) {
				if (object == null) {
					objectIsNull();
				} else {
					bean = object;
					handler.obtainMessage(1).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, dataCallBack);

	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		
	}
}
