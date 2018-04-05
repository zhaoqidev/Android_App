package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.SchoolmateCityAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.SchoolmateAllBean;
import cc.upedu.online.domin.SchoolmateAllBean.SchoolmateItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 同城好友界面
 * @author Administrator
 *
 */
public class SchoolmateCityFragment extends RecyclerViewBaseFragment<SchoolmateItem>{
	public SchoolmateCityFragment(Context context) {
		super(context);
		userId=UserStateUtil.getUserId();
	}

	private  SchoolmateAllBean bean = new SchoolmateAllBean();
	
	String type="1";
	String userId;
	

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

	private void setData() {
		totalPage = bean.entity.totalPage;
		//判断是否可以加载下一页
		canLodeNextPage();
		if (bean.entity.userList == null) {
			setNocontentVisibility(View.VISIBLE);
		}else {
			list.addAll(bean.entity.userList);
			
			if (isAdapterEmpty()) {
				setRecyclerView(new SchoolmateCityAdapter(context, list));
				setOnItemClick(new OnItemClickLitener() {
					
					@Override
					public void onItemLongClick(View view, int position) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onItemClick(View view, int position) {
						// TODO Auto-generated method stub
						if (UserStateUtil.isLogined()) {
							Intent intent = new Intent(context, UserShowActivity.class);
							intent.putExtra("userId", list.get(position).userId);
							intent.putExtra("attention", list.get(position).isFriend);
							context.startActivity(intent);
						}else{
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
		// 获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.schoolMate(context, type, userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.SCHOOL_MATE_LIST,
				context, requestDataMap, new MyBaseParser<>(SchoolmateAllBean.class));
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
	public void getData() {
		currentPage = 1;
		initData();
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