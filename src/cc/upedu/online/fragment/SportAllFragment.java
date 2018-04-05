package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.SportDetailActivity;
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
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 全部活动的pager
 * 
 * @author Administrator
 * 2015.9.8 将轮播图隐藏
 * 
 */
public class SportAllFragment extends RecyclerViewBaseFragment<ActivityItem> {

	// 活动列表的Javabean文件
	private SportsBean bean = new SportsBean();

	private String search=null;//搜索活动的关键词

	public SportAllFragment(Context context) {
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
		if (bean.entity.activityList == null) {
//			if (!StringUtil.isEmpty(extraData)){
//				list.clear();
//			}
			notifyData();
		}else {
			list.addAll(bean.entity.activityList);
			
			if (isAdapterEmpty()) {
				setRecyclerView(new SportAdapter(context, list));
				setOnItemClick(new OnItemClickLitener() {
					
					@Override
					public void onItemLongClick(View view, int position) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onItemClick(View view, int position) {
						// TODO Auto-generated method stub
						if (UserStateUtil.isLogined()) {
							Intent intent = new Intent(context,SportDetailActivity.class);
							intent.putExtra("id", list.get(position).id);
							intent.putExtra("image", list.get(position).logo);
							intent.putExtra("startDt",list.get(position).startDt);
							context.startActivity(intent);
						} else {
							UserStateUtil.NotLoginDialog(context);
						}
					}
				});
			}else {
				notifyData();
			}
		}
	}
	
	@Override
	public void initData() {
		// 获取活动列表的数据
		Map<String, String> requestDataMap;
		if (StringUtil.isEmpty(extraData))
			requestDataMap = ParamsMapUtil.AllSport(context,String.valueOf(currentPage), search);
		else
			requestDataMap = ParamsMapUtil.otherCitySport(context,String.valueOf(currentPage),extraData);
		RequestVo requestVo = new RequestVo(ConstantsOnline.SPORT, context,requestDataMap, new MyBaseParser<>(SportsBean.class));
		DataCallBack<SportsBean> coursseDataCallBack = new DataCallBack<SportsBean>() {
			@Override
			public void processData(SportsBean object) {
				if (object == null) {
					objectIsNull();
				} else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, coursseDataCallBack);
	}
	public void getData() {
		currentPage = 1;
		search=null;
		initData();
	}
	public void seacherSport(String searchText){
//		ShowUtils.showMsg(context, "搜索活动");
		extraData = null;
		if ("查找全部".equals(searchText)) {
			search=null;
			currentPage=1;
			list.clear();
			initData();
		}else {
			search=searchText;//搜索内容
			currentPage=1;
			list.clear();
			initData();
		}
	}

	private String extraData;
	public void setExtraData(String extraData) {
		// TODO Auto-generated method stub
		this.extraData = extraData;
	}

//	@Override
//	public void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//		int a = 0;
//		initData();
//	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		
	}
}
