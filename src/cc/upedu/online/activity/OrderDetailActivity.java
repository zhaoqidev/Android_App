package cc.upedu.online.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.OrderConfrimCourseListAdapter;
import cc.upedu.online.adapter.OrderCourseListAdapter;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.CheckOrderBean;
import cc.upedu.online.domin.ConfirmOrderBean.ConfrimOrderList;
import cc.upedu.online.domin.MyOrderBean.TrxorderDetailList;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;
import cn.beecloud.BCPay;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;

/**
 * 订单详情页面,点击立即支付进入到支付前检测，检测成功进入支付页面
 * 
 * @author Administrator
 * 
 */
public class OrderDetailActivity extends TitleBaseActivity {

	private TextView tv_order_number;// 订单号
	private TextView paying_type;// 支付类型
	private TextView tv_money;// 商品总额
	private TextView tv_actual_money;// 实际金额
	private TextView tv_time;// 下单时间
	private ListView lv_order;// 课程列表
	private TextView tv_balance_money;// 零钱数

	private TextView tv_cancel_order;// 取消订单
	private TextView tv_pay_order;// 支付订单

	private String userId = UserStateUtil.getUserId();// 用户ID
	private String trxorderId;// 订单号,短的
	private String requestId;// 请求号，长的
	private String payType;// 支付类型
	private String orderAmount;// 订单总额
	private String amount;// 实付金额
	private String createTime;// 订单创建时间
	private String balance;// 我的零钱

	private RequestVo requestVo;
	private DataCallBack<CheckOrderBean> dataCallBack;
	private DataCallBack<PaySuccess> callBack;

	CheckOrderBean bean = new CheckOrderBean();

	private ProgressDialog loadingDialog;// 进度条

	ArrayList<TrxorderDetailList> courseItem = new ArrayList<TrxorderDetailList>();//从订单列表传的值
	ArrayList<ConfrimOrderList> orderItem=new ArrayList<ConfrimOrderList>();//从确认订单页面传递的值
	String course_name="";//掉落钱币动画所需要的课程名
	
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("订单详情");
	}

	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_orderdetail, null);

		// 如果用到微信支付，比如在用到微信支付的Activity的onCreate函数里调用以下函数.
		// 第二个参数需要换成你自己的微信AppID.
		BCPay.initWechatPay(OrderDetailActivity.this, "wxa5998cbde9406419");
		showProgressDialog();

		tv_order_number = (TextView) view.findViewById(R.id.tv_order_number);// 订单号
		paying_type = (TextView) view.findViewById(R.id.paying_type);// 支付类型
		tv_money = (TextView) view.findViewById(R.id.tv_money);// 商品总额
		tv_actual_money = (TextView) view.findViewById(R.id.tv_actual_money);// 实际金额
		tv_time = (TextView) view.findViewById(R.id.tv_time);// 下单时间
		lv_order = (ListView) view.findViewById(R.id.lv_order);
		tv_balance_money = (TextView) view.findViewById(R.id.tv_balance_money);// 零钱金额

		tv_cancel_order = (TextView) view.findViewById(R.id.tv_cancel_order);// 取消订单
		tv_pay_order = (TextView) view.findViewById(R.id.tv_pay_order);// 支付订单

		Intent intent = getIntent();
		trxorderId = intent.getStringExtra("trxorderId");
		requestId = intent.getStringExtra("requestId");
		
		payType = intent.getStringExtra("payType");
		orderAmount = intent.getStringExtra("orderAmount");
		amount = intent.getStringExtra("amount");
		createTime = intent.getStringExtra("createTime");
		balance = intent.getStringExtra("balance");// 我的零钱
		
		courseItem = (ArrayList<TrxorderDetailList>) intent.getSerializableExtra("trxorderDetailList");// 接收传过来的listview
		orderItem=(ArrayList<ConfrimOrderList>) intent.getSerializableExtra("confrimOrderList");// 接收传过来的listview
		
		setData();
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		tv_cancel_order.setOnClickListener(this);
		tv_pay_order.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_cancel_order://取消订单
			getCancelOrderRequestVo();
			getCancelOrderDataCallback();
			getDataServer(requestVo, callBack);
			break;
		case R.id.tv_pay_order://立即支付
			getCheckRequestVo();
			getCheckDataCallBack();
			getDataServer(requestVo, dataCallBack);

			break;

		default:
			break;
		}

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	protected void setData() {
		tv_order_number.setText(requestId);

		paying_type.setText(payType);
		tv_money.setText(amount);
		float actual_money = Float.valueOf(amount) - Float.valueOf(balance);
		if (actual_money > 0) {
			tv_actual_money.setText(String.valueOf(actual_money));
			SharedPreferencesUtil.getInstance().editPutString("showPop_money", String.valueOf(actual_money));//掉钱袋动画上的需要付款的金额
		} else {
			tv_actual_money.setText("0.00");
			SharedPreferencesUtil.getInstance().editPutString("showPop_money", "0.00");//掉钱袋动画上的需要付款的金额
		}

		tv_time.setText(createTime);
		tv_balance_money.setText(balance);
		
		if (courseItem !=null) {
			OrderCourseListAdapter adapter = new OrderCourseListAdapter(context,courseItem);
			lv_order.setAdapter(adapter);
			adapter.setListViewHeight(lv_order);
			//遍历订单列表，获取到订单课程名
			for (int i = 0; i < courseItem.size(); i++) {
				course_name=course_name+"<"+courseItem.get(i).courseName+">";
			}
		}else if (orderItem !=null) {
			OrderConfrimCourseListAdapter adapter = new OrderConfrimCourseListAdapter(context, orderItem);
			lv_order.setAdapter(adapter);
			adapter.setListViewHeight(lv_order);
			//遍历订单列表，获取到订单课程名
			for (int i = 0; i < orderItem.size(); i++) {
				course_name=course_name+"<"+orderItem.get(i).courseName+">";
			}
		}
		//订单中的课程名存储到sp文件中
		SharedPreferencesUtil.getInstance().editPutString("course_name", course_name);//把课程名存到sp中
		System.out.println("course_name"+course_name);

		
	}
	private void getCheckRequestVo() {

		Map<String, String> params = ParamsMapUtil.checkOrder(context, userId,
				trxorderId, payType);
		requestVo = new RequestVo(ConstantsOnline.CHECK_ORDER, context, params,
				new MyBaseParser<>(CheckOrderBean.class));
	}

	private void getCheckDataCallBack() {
		dataCallBack = new DataCallBack<CheckOrderBean>() {

			String money = null;// 传给支付宝应付款的金额
			String outTradeNo = null;// 传给支付宝的订单号

			@Override
			public void processData(CheckOrderBean object) {
				if (object != null) {
					bean = object;
					if ("true".equals(object.success)) {
						ShowUtils.showMsg(context, "恭喜您课程购买成功");
						Intent intent=new Intent(context, MyOrderActivity.class);
						SharedPreferencesUtil.getInstance().editPutBoolean("showPop", true);//是否显示掉钱袋动画

						SharedPreferencesUtil.getInstance().editPutString("ShoopingCourseId", "");//支付成功后，刷新购物车使用
						startActivity(intent);
						finish();
					} else {
						if (object.entity == null) {
							ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
						} else {
							// 调起支付模块
							if ("ALIPAY".equals(object.entity.payType)) {
								money = String
										.valueOf((int) ((Float
												.valueOf(object.entity.bankAmount)) * 100));
								outTradeNo = object.entity.outTradeNo;
								aliPay(money, outTradeNo);

							} else if ("WEIXIN".equals(object.entity.payType)) {
								money = String
										.valueOf((int) ((Float
												.valueOf(object.entity.bankAmount)) * 100));
								outTradeNo = object.entity.outTradeNo;
								weixinPay(money, outTradeNo);
							}
						}
					}
				} else {
					ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
				}

			}
		};
	}

	/**
	 * 微信支付
	 */
	public void weixinPay(String money, String orderNo) {
		// 启动loading
		loadingDialog.show();

		/*
		 * Map<String, String> mapOptional = new HashMap<>(); String optionalKey
		 * = "userID"; String optionalValue = "13033";
		 * mapOptional.put(optionalKey, optionalValue);
		 */

		if (BCPay.isWXAppInstalledAndSupported() && BCPay.isWXPaySupported()) {
			// 订单标题, 订单金额(分), 订单号, 扩展参数(可以null), 支付完成后回调入口
			BCPay.getInstance(OrderDetailActivity.this).reqWXPaymentAsync(
					"购买课程", money, orderNo, null, bcCallback);
		}
	}

	/**
	 * 支付宝支付
	 */
	public void aliPay(String money, String orderNo) {
		// 启动loading
		loadingDialog.show();

		/*
		 * Map<String, String> mapOptional = new HashMap<>(); mapOptional = new
		 * HashMap<>(); mapOptional.put("paymentid", "");
		 * mapOptional.put("consumptioncode", "consumptionCode");
		 * mapOptional.put("money", "2");
		 */
		BCPay.getInstance(OrderDetailActivity.this).reqAliPaymentAsync("购买课程",
				money, orderNo, null, bcCallback);
	}

	/**
	 * 付款结束后的回调
	 */
	final BCCallback bcCallback = new BCCallback() {
		@Override
		public void done(final BCResult bcResult) {
			// 关闭loading动画
			loadingDialog.dismiss();

			final BCPayResult bcPayResult = (BCPayResult) bcResult;

			// 对于支付宝，如果想通过Toast通知用户结果，请使用如下方式，
			// 直接makeText会造成java.lang.RuntimeException: Can't create handler
			// inside thread that has not called Looper.prepare()
			OrderDetailActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					switch (bcPayResult.getResult()) {
					case BCPayResult.RESULT_SUCCESS:
						Toast.makeText(OrderDetailActivity.this, "支付成功",
								Toast.LENGTH_LONG).show();
						getSuccessRequestVo();
						getSuccessDataCallback();
						getDataServer(requestVo, callBack);

						break;
					case BCPayResult.RESULT_CANCEL:
						Toast.makeText(OrderDetailActivity.this, "取消支付",
								Toast.LENGTH_LONG).show();
						break;
					case BCPayResult.RESULT_FAIL:
						Toast.makeText(
								OrderDetailActivity.this,
								"支付失败, 原因: " + bcPayResult.getErrMsg() + ", "
										+ bcPayResult.getDetailInfo(),
								Toast.LENGTH_LONG).show();
						break;
					}
				}
			});
		}
	};

	/**
	 * 付款成功的RequestVo
	 */
	public void getSuccessRequestVo() {
		Map<String, String> params = ParamsMapUtil.paySuccess(context, userId,
				bean.entity.bankAmount, bean.entity.orderNo,
				bean.entity.outTradeNo);
		requestVo = new RequestVo(ConstantsOnline.PAY_SUCCESS, context, params,
				new MyBaseParser<>(PaySuccess.class));
	}

	/**
	 * 付款成功的DataCallback
	 */
	public void getSuccessDataCallback() {
		callBack = new DataCallBack<OrderDetailActivity.PaySuccess>() {

			@Override
			public void processData(PaySuccess object) {
				if (object != null) {
					if ("true".equals(object.success)) {
						ShowUtils.showMsg(context, "恭喜您课程购买成功");
						Intent intent=new Intent(context, MyOrderActivity.class);
						SharedPreferencesUtil.getInstance().editPutBoolean("showPop", true);//是否显示掉钱袋动画
						SharedPreferencesUtil.getInstance().editPutString("ShoopingCourseId", "");//支付成功后，刷新购物车使用
						startActivity(intent);
						finish();
					}
					if ("false".equals(object.success)) {
						ShowUtils.showMsg(context, object.message + ",请联系客服");
					}
				}
			}
		};
	}
	
	
	/**
	 * 取消订单的RequestVo
	 */
	public void getCancelOrderRequestVo() {
		Map<String, String> params = ParamsMapUtil.cancelOrder(context, userId, trxorderId);
		requestVo = new RequestVo(ConstantsOnline.CANCEL_ORDER, context, params,
				new MyBaseParser<>(PaySuccess.class));
	}

	/**
	 * 取消订单的DataCallback
	 */
	public void getCancelOrderDataCallback() {
		callBack = new DataCallBack<OrderDetailActivity.PaySuccess>() {

			@Override
			public void processData(PaySuccess object) {
				if (object != null) {
					if ("true".equals(object.success)) {
						ShowUtils.showMsg(context, object.message);
						finish();
					}
				}
			}
		};
	}

	/**
	 * 支付成功回调的javabean
	 * 
	 * @author Administrator
	 * 
	 */
	class PaySuccess {
		public String message;
		public String success;
	}

	/**
	 * 设置加载支付进度框的样式
	 */
	public void showProgressDialog() {
		loadingDialog = new ProgressDialog(OrderDetailActivity.this);
		loadingDialog.setMessage("正在启动支付控件，请稍候...");
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(true);
	}
}
