package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.SelectFriendAdapter;
import cc.upedu.online.base.RecyclerViewBaseActivity;
import cc.upedu.online.domin.MySchoolmateBean;
import cc.upedu.online.domin.MySchoolmateBean.SchoolmateItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 侧拉栏，向学友赠送成长币，弹出的学友列表
 * 
 * @author Administrator
 * 
 */
public class SelectFriendActivity extends RecyclerViewBaseActivity<SchoolmateItem>{
	private MySchoolmateBean bean = new MySchoolmateBean();
	String type = "4";
	String userId;
	static int REQUEST_FRIEND=1;

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list!=null) {
						list.clear();
					}else {
						list = new ArrayList<SchoolmateItem>();
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
		list.addAll(bean.entity.followList);
		if (isAdapterEmpty()) {
			setRecyclerView(new SelectFriendAdapter(context, list));
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					//选择好友，携带姓名和id返回
					Intent intent = new Intent(context,MyWalletCoinToFriendsActivity.class);
					intent.putExtra("userId", list.get(position).userId);
					intent.putExtra("userName", list.get(position).name);
			        setResult(REQUEST_FRIEND, intent);  
			        finish();
				}
			});
		}else {
			notifyData();
		}
	}

	@Override
	public void initData() {

		userId = UserStateUtil.getUserId();// 获取用户ID

		// 获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.mySchoolMate(context,userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_SCHOOLMATE,
				context, requestDataMap, new MyBaseParser<>(
						MySchoolmateBean.class));
		DataCallBack<MySchoolmateBean> dataCallBack = new DataCallBack<MySchoolmateBean>() {
			@Override
			public void processData(MySchoolmateBean object) {
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

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}

	@Override
	protected void initTitle() {
		setTitleText("选择好友");
		
	}

}
