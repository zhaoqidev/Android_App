package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.SchoolmateAllAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.SchoolmateAllBean;
import cc.upedu.online.domin.SchoolmateAllBean.SchoolmateItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 学友界面，默认显示全部学友
 * 
 * @author Administrator
 * 
 */
public class SchoolmateAllFragment extends RecyclerViewBaseFragment<SchoolmateItem>{
	private  SchoolmateAllBean bean = new SchoolmateAllBean();
	
	String userId;
	
	private String search=null;//搜索内容

	public SchoolmateAllFragment(Context context) {
		super(context);
		userId=UserStateUtil.getUserId();
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list==null) {
						list=new ArrayList<SchoolmateItem>();
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
		totalPage = bean.entity.totalPage;
		//判断是否可以加载下一页
		canLodeNextPage();
		if (bean.entity.userList == null) {
			setNocontentVisibility(View.VISIBLE);
		}else {
			list.addAll(bean.entity.userList);
			
			if (isAdapterEmpty()) {
				setRecyclerView(new SchoolmateAllAdapter(context, list));
				setOnItemClick(new OnItemClickLitener() {
					
					@Override
					public void onItemLongClick(View view, int position) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onItemClick(View view, int position) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, UserShowActivity.class);
						intent.putExtra("userId", list.get(position).userId);
						intent.putExtra("attention", list.get(position).isFriend);
						context.startActivity(intent);
					}
				});
			}else {
				notifyData();
			}
		}
	}


	@Override
	public void initData() {
		Map<String, String> requestDataMap;
		// 获取课程列表的数据	
		/*if (extraData==null) {
			requestDataMap = ParamsMapUtil.schoolMate(context, "4", userId, String.valueOf(currentPage));
		}else {
			if ("peer".equals(extraData[0])) {
				//行业好友
				requestDataMap = ParamsMapUtil.peerSchoolMate(context, "2", userId, String.valueOf(currentPage), extraData[1]);
			}else if ("city".equals(extraData[0])) {
				//其他城市
				requestDataMap = ParamsMapUtil.peerSchoolMate(context, "3", userId, String.valueOf(currentPage),extraData[1]);
				
			}else {
				//全部好友
				requestDataMap = ParamsMapUtil.schoolMate(context, "4", userId, String.valueOf(currentPage));
			}
		}	*/
		if (StringUtil.isEmpty(search)) {
			if (StringUtil.isEmpty(extraData)) {
				requestDataMap = ParamsMapUtil.schoolMate(context, "4", userId, String.valueOf(currentPage));
				
			}else {
				//其他城市
				requestDataMap = ParamsMapUtil.peerSchoolMate(context, "3", userId, String.valueOf(currentPage),extraData);
			}
		}else {
			requestDataMap = ParamsMapUtil.searchSchoolMate(context, "5", userId, String.valueOf(currentPage),search);
		}
		RequestVo requestVo = new RequestVo(ConstantsOnline.SCHOOL_MATE_LIST,context, requestDataMap, new MyBaseParser<>(SchoolmateAllBean.class));
		DataCallBack<SchoolmateAllBean> dataCallBack = new DataCallBack<SchoolmateAllBean>() {
			@Override
			public void processData(SchoolmateAllBean object) {
				if (object == null) {
					objectIsNull();
				} else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, dataCallBack);
	}
	public void setExtraData(String extraData) {
		// TODO Auto-generated method stub
		this.extraData = extraData;
	}
	public void getData() {
		currentPage = 1;
		search=null;
		initData();
	}
	public void seacherSchoolmate(String searchText){
		ShowUtils.showMsg(context, "搜索学友");
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
//	private boolean isFistInitdata = true;
//	public void onResume() {
//		initData();
//		if (!isFistInitdata) {
//		}else {
//			isFistInitdata = false;
//		}
//	};

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}
}
