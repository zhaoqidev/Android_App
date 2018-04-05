package cc.upedu.online.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.MyOrderListAdapter;
import cc.upedu.online.base.TwoPartModelTopRecyclerViewBaseActivity;
import cc.upedu.online.domin.MyOrderBean;
import cc.upedu.online.domin.MyOrderBean.OrderItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.flakeview.FlakeGoldCoinActivity;

/**
 * 侧拉栏，我的订单的界面
 * 
 * @author Administrator
 * 
 */
public class MyOrderActivity extends TwoPartModelTopRecyclerViewBaseActivity<OrderItem> {

	private MyOrderBean bean = new MyOrderBean();

	String userId;
	boolean showPop;//显示动画

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				showPop();
				break;
			case 1:
				if ("true".equals(bean.success)) {
					if (!isLoadMore()) {
						if (list!=null) {
							list.clear();
						}else {
							list = new ArrayList<OrderItem>();
						}
					}
					setData();
				} else {
					ShowUtils.showMsg(context, bean.message);
				}
				setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
				break;

			default:
				break;
			}
		}
	};
	private String showPop_money;
	private String course_name;

	private void setData() {
		totalPage = bean.entity.totalPageSize;
		//判断是否可以加载下一页
		canLodeNextPage();
		list.addAll(bean.entity.orderList);
		
		if (isAdapterEmpty()) {
			setRecyclerView(new MyOrderListAdapter(context, list,bean.entity.balance));
		}else {
			notifyData();
		}
	}
	
	@Override
	protected void onResume() {

		initData();
		Message msg =new Message();
		msg.what=1;
		handler.sendMessageDelayed(msg, 3000);
		
		super.onResume();
	}


	@Override
	public void initData() {
		userId =UserStateUtil.getUserId();// 获取用户ID
		// 获取订单列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.myOrder(context,userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_ORDER,
				context, requestDataMap, new MyBaseParser<>(
						MyOrderBean.class));
		DataCallBack<MyOrderBean> dataCallBack = new DataCallBack<MyOrderBean>() {
			@Override
			public void processData(MyOrderBean object) {
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


	/**
	 * 显示支付成功的PopUpWindow的动画
	 */
	private void showPop() {
	
		showPop=SharedPreferencesUtil.getInstance().spGetBoolean("showPop");
		if (showPop) {
			course_name = SharedPreferencesUtil.getInstance().spGetString("course_name", "");
			if ("0.00".equals(SharedPreferencesUtil.getInstance().spGetString("showPop_money", ""))) {
				showPop_money = "";
			}else {
				showPop_money=SharedPreferencesUtil.getInstance().spGetString("showPop_money", "");
			}
		
		new FlakeGoldCoinActivity(MyOrderActivity.this).showPopWindows( course_name+"购买成功",showPop_money, false);
		showPop=false;
		SharedPreferencesUtil.getInstance().editPutBoolean("showPop", false);//置空
		}

	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View initTopLayout() {
		
		return View.inflate(context, R.layout.activity_myorder, null);
	}

	@Override
	protected void initTitle() {
		setTitleText("我的订单");
		
	}

}
