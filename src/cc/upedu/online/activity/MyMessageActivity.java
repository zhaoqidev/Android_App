package cc.upedu.online.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.MyMessageAdapter;
import cc.upedu.online.base.RecyclerViewBaseActivity;
import cc.upedu.online.domin.MyMessageBean;
import cc.upedu.online.domin.MyMessageBean.MsgItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 侧拉栏--我的消息
 * 
 * @author Administrator
 * 
 */
public class MyMessageActivity extends RecyclerViewBaseActivity<MsgItem>{
	/**
	 * 判断是都弹出管理的标志，false是隐藏按钮，管理;true是显示按钮，完成
	 */
	boolean DELETE=false;
	private MyMessageBean bean = new MyMessageBean();
	String userId;//用户ID
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list!=null) {
						list.clear();
					}else {
						list = new ArrayList<MsgItem>();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.message);
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}};

	private MyMessageManagingRecive myMessageManagingRecive;
	
	private void setData() {
		totalPage = bean.entity.totalPage;
		//判断是否可以加载下一页
		canLodeNextPage();
		
		list.addAll(bean.entity.msgList);
		if (isAdapterEmpty()) {
			setRecyclerView(new MyMessageAdapter(context, list));
			((MyMessageAdapter)getAdapter()).setDeleteBoolean(false);
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemClick(View view, int position) {
					
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
	
	@Override
	public void initData() {
		
		userId = getIntent().getStringExtra("userId");// 获取用户ID
		
		//获取课程列表的数据
		Map<String, String> requestDataMap;
		requestDataMap = ParamsMapUtil.myMessage(context, userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_MESSAGE, context,requestDataMap, new MyBaseParser<>(MyMessageBean.class));
		DataCallBack<MyMessageBean> dataCallBack = new DataCallBack<MyMessageBean>() {
			@Override
			public void processData(MyMessageBean object) {
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

	/**
	 * 自定义的广播接受者,用于在用户清空收藏时管理功能的自动切换
	 * @author Administrator
	 *
	 */
	class MyMessageManagingRecive extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			setRightText("管理",null);
			DELETE = !DELETE;
			if (!isAdapterEmpty()) {
				((MyMessageAdapter)getAdapter()).setDeleteBoolean(DELETE);
				notifyData();
			}
		}
	}
	@Override
	protected void onDestroy() {
		context.unregisterReceiver(myMessageManagingRecive);
		super.onDestroy();
	}
	@Override
	public void onRefresh() {
		setRightText("管理",null);
		super.onRefresh();
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}

	@Override
	protected void initTitle() {
		setTitleText("我的站内信");
		setRightText("管理", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				if (list != null && list.size() > 0) {
					if(DELETE==false){
						if (!isAdapterEmpty()) {
							setRightText("完成",null);
							//iv_delete.setVisibility(View.VISIBLE);
							DELETE=true;
							((MyMessageAdapter)getAdapter()).setDeleteBoolean(DELETE);
							notifyData();
						}

					}else{
						setRightText("管理",null);
						//iv_delete.setVisibility(View.GONE);
						DELETE=false;
						if (!isAdapterEmpty()) {
							((MyMessageAdapter)getAdapter()).setDeleteBoolean(DELETE);
							notifyData();
						}
					}
				}else {
					ShowUtils.showMsg(context, "暂无数据,不能开启管理功能!");
				}
				
			}
		});
		myMessageManagingRecive = new MyMessageManagingRecive();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("MessageManaging");
		context.registerReceiver(myMessageManagingRecive, intentFilter);
	}
}
