package cc.upedu.online.activity;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.ExchangeRecordAdapter;
import cc.upedu.online.base.TwoPartModelTopRecyclerViewBaseActivity;
import cc.upedu.online.domin.MyPointorderBean;
import cc.upedu.online.domin.MyPointorderBean.Entity.OrderItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 微商城中订单记录界面
 * 
 * @author Administrator
 * 
 */
public class MicroMallExchangeRecordActivity extends TwoPartModelTopRecyclerViewBaseActivity<OrderItem>{
	TextView tv_pointbalance;// 右上角文字
	private MyPointorderBean bean;
	String userId;// 用户ID
	
//	@Override
//	protected void onResume() {
//		initData();
//		super.onResume();
//	}

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("兑换记录");
	}
	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_exchangerecord_top, null);
		tv_pointbalance = (TextView) view.findViewById(R.id.tv_pointbalance);
		return view;
	}
	@Override
	protected void initData() {
		userId = UserStateUtil.getUserId();
		// 获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getExchangeRecord(context, userId,String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_POINTORDER, context,
				requestDataMap, new MyBaseParser<>(MyPointorderBean.class));
		DataCallBack<MyPointorderBean> coursseDataCallBack = new DataCallBack<MyPointorderBean>() {
			
			@Override
			public void processData(final MyPointorderBean object) {
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
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.getSuccess())) {
				tv_pointbalance.setText(bean.getEntity().getPointbalance());
				if (!isLoadMore()) {
					if (list == null) {
						list = new ArrayList<OrderItem>();
					}else {
						list.clear();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.getMessage());
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}};
	private void setData() {
		totalPage = bean.getEntity().getTotalPageSize();
		canLodeNextPage();
		list.addAll(bean.getEntity().getOrderList());
		
		if (isAdapterEmpty()) {
			setRecyclerView(new ExchangeRecordAdapter(context, list));
		}else {
			notifyData();
		}
	}
	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		setItemDecoration(true);
	}
}
