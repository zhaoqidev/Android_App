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
 * 同城活动的pager
 * 
 * @author Administrator
 * 
 */
public class SportCityFragment extends RecyclerViewBaseFragment<ActivityItem> {
	// 活动列表的Javabean文件
	private SportsBean bean = new SportsBean();
	private String city;//城市活动
	private String userId;//用户Id

	public SportCityFragment(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if(list==null){
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
	private String extraData;
	

	private void setData() {
		totalPage = bean.entity.totalPageSize;
		//判断是否可以加载下一页
		canLodeNextPage();
		if (bean.entity.activityList == null) {
			setNocontentVisibility(View.VISIBLE);
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
		userId=UserStateUtil.getUserId();
		//获取活动列表的数据
				Map<String, String> requestDataMap;
				if (StringUtil.isEmpty(extraData)) {
					requestDataMap = ParamsMapUtil.oneCitySport(context, userId,String.valueOf(currentPage));//同城活动
				}else{
					requestDataMap = ParamsMapUtil.otherCitySport(context, extraData,String.valueOf(currentPage));//其他城市的活动
				}

				RequestVo requestVo = new RequestVo(ConstantsOnline.SPORT, context, requestDataMap, new MyBaseParser<>(SportsBean.class));
				DataCallBack<SportsBean> coursseDataCallBack = new DataCallBack<SportsBean>() {
					@Override
					public void processData(SportsBean object) {
						if (object==null) {
							objectIsNull();
						}else {
							bean = object;
							handler.obtainMessage().sendToTarget();
						}
					}
				};
				getDataServer(requestVo, coursseDataCallBack);
	}

	public void getData() {
		currentPage = 1;
		initData();
	}
	public void setExtraData(String extraData) {
		// TODO Auto-generated method stub
		this.extraData = extraData;
	}

//	public void onResume() {
//		// TODO Auto-generated method stub
//		initData();
//	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		
	}
}
