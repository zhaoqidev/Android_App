package cc.upedu.online.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.RechargeBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.flakeview.FlakeGoldCoinActivity;
import cn.beecloud.BCPay;
import cn.beecloud.async.BCCallback;
import cn.beecloud.async.BCResult;
import cn.beecloud.entity.BCPayResult;

/**
 * 我的钱包--零钱--充值页面
 * 
 * @author Administrator
 * 
 */
public class MyWalletRechargeActivity extends TitleBaseActivity {
	private TextView tv_money_one, tv_money_two, tv_money_three, tv_money_four,
			tv_money_five;
	private List<TextView> moneyArray = new ArrayList<>();
	private EditText et_money;
	private RadioButton rb_aliPay, rb_weixin;// 支付宝支付，还是微信支付
	private RelativeLayout ll_aliPay;
	RelativeLayout ll_weixin;
	private TextView tv_recharge;

	private String money = "0";// 充值的钱数

	// 支付方式:支付宝/微信/银联
	private static final int ALIPAY = 0;
	private static final int WEIXIN = 1;
//	private static final int UNIONPAY = 2;
	// 默认的支付方式
	private int choosePayment = ALIPAY;
	String payType = "";// 支付类型

	private String userId;
	
	private RequestVo requestVo;
	private DataCallBack<RechargeBean> dataCallBack;//支付之前
	private DataCallBack<PaySuccess> callBack;//支付成功
	
	private RechargeBean bean=new RechargeBean();
	
	private ProgressDialog loadingDialog;// 进度条

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		// 如果用到微信支付，比如在用到微信支付的Activity的onCreate函数里调用以下函数.
		// 第二个参数需要换成你自己的微信AppID.
		BCPay.initWechatPay(MyWalletRechargeActivity.this, "wxa5998cbde9406419");
		showProgressDialog();
		
		setTitleText("充值");
		userId = getIntent().getStringExtra("userId");
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_mywallet_recharge, null);
		tv_money_one = (TextView) view.findViewById(R.id.tv_money_one);
		tv_money_two = (TextView) view.findViewById(R.id.tv_money_two);
		tv_money_three = (TextView) view.findViewById(R.id.tv_money_three);
		tv_money_four = (TextView) view.findViewById(R.id.tv_money_four);
		tv_money_five = (TextView) view.findViewById(R.id.tv_money_five);
		moneyArray.add(tv_money_one);
		moneyArray.add(tv_money_two);
		moneyArray.add(tv_money_three);
		moneyArray.add(tv_money_four);
		moneyArray.add(tv_money_five);

		et_money = (EditText) view.findViewById(R.id.et_money);

		ll_aliPay = (RelativeLayout) view.findViewById(R.id.ll_aliPay);
		ll_weixin = (RelativeLayout) view.findViewById(R.id.ll_weixin);
		rb_aliPay = (RadioButton) view.findViewById(R.id.rb_aliPay);
		rb_weixin = (RadioButton) view.findViewById(R.id.rb_weixin);
		tv_recharge = (TextView) view.findViewById(R.id.tv_recharge);

		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);


		et_money.addTextChangedListener(new TextWatcher() {// 输入框的点击监听
			private CharSequence temp;

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				temp = s;

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!StringUtil.isEmpty(temp.toString().trim())) {
					onlickState(0);
					amount = temp.toString().trim();
				}

			}
		});
		return view;
	}

	@Override
	protected void initListener() {
		super.initListener();
		tv_money_one.setOnClickListener(this);
		tv_money_two.setOnClickListener(this);
		tv_money_three.setOnClickListener(this);
		tv_money_four.setOnClickListener(this);
		tv_money_five.setOnClickListener(this);

		ll_aliPay.setOnClickListener(this);
		rb_aliPay.setOnClickListener(this);
		ll_weixin.setOnClickListener(this);
		rb_weixin.setOnClickListener(this);

		tv_recharge.setOnClickListener(this);
		ll_content.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_money_one:
			onlickState(1);

			amount = "500";
			break;
		case R.id.tv_money_two:
			onlickState(2);

			amount = "1000";
			break;
		case R.id.tv_money_three:
			onlickState(3);
			amount = "5000";
			break;

		case R.id.tv_money_four:
			onlickState(4);
			amount = "10000";
			break;
		case R.id.tv_money_five:
			onlickState(5);

			amount = "30000";
			break;

		case R.id.ll_aliPay:
		case R.id.rb_aliPay:
			choosePayment = ALIPAY;
			rb_aliPay.setChecked(true);
			rb_weixin.setChecked(false);
			break;

		case R.id.ll_weixin:
		case R.id.rb_weixin:
			choosePayment = WEIXIN;
			rb_weixin.setChecked(true);
			rb_aliPay.setChecked(false);
			break;
		case R.id.tv_recharge:
			getRequestVo();
			if (requestVo!=null&&!(0 == Integer.parseInt(amount))&&!moneyISNull()) {
				getDataServer(requestVo, dataCallBack);
			}
			
			break;
		case R.id.ll_content:
			if(MyWalletRechargeActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MyWalletRechargeActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		default:
			break;
		}

	}

	protected void getRequestVo() {

		// 选择支付方式
		switch (choosePayment) {
		case ALIPAY:
			payType = "ALIPAY";
			break;
		case WEIXIN:
			payType = "WEIXIN";
			break;
		default:
			break;
		}

		if (!moneyISNull()) {
			
			if (!(0 == Integer.parseInt(amount))) {
				Map<String, String> params = ParamsMapUtil.recharge(context,userId, amount, payType);
				requestVo = new RequestVo(
						ConstantsOnline.RECHARGE, context, params,
						new MyBaseParser<>(RechargeBean.class));
			} else {
				ShowUtils.showMsg(context, "充值金额不能为0哦~");
			}
		} else {
			ShowUtils.showMsg(context, "充值金额不能为空哦~");
		}

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
			BCPay.getInstance(MyWalletRechargeActivity.this).reqWXPaymentAsync(
					"零钱充值", money, orderNo, null, bcCallback);
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
		BCPay.getInstance(MyWalletRechargeActivity.this).reqAliPaymentAsync("零钱充值",
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
			MyWalletRechargeActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					switch (bcPayResult.getResult()) {
					case BCPayResult.RESULT_SUCCESS:
						Toast.makeText(MyWalletRechargeActivity.this, "支付成功",
								Toast.LENGTH_LONG).show();
						getSuccessRequestVo();
						getSuccessDataCallback();
						getDataServer(requestVo, callBack);
						break;
					case BCPayResult.RESULT_CANCEL:
						Toast.makeText(MyWalletRechargeActivity.this, "取消支付",
								Toast.LENGTH_LONG).show();
						break;
					case BCPayResult.RESULT_FAIL:
						Toast.makeText(
								MyWalletRechargeActivity.this,
								"支付失败, 原因: " + bcPayResult.getErrMsg() + ", "
										+ bcPayResult.getDetailInfo(),
								Toast.LENGTH_LONG).show();
						break;
					}
				}
			});
		}
	};
	private LinearLayout ll_content;
	private String amount="500";//充值金额

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
		callBack = new DataCallBack<MyWalletRechargeActivity.PaySuccess>() {

			@Override
			public void processData(PaySuccess object) {
				if (object != null) {
					if ("true".equals(object.success)) {
						new FlakeGoldCoinActivity(MyWalletRechargeActivity.this).showPopWindows( "充值成功",amount, false);
					}
					else {
						ShowUtils.showMsg(context, object.message + ",请联系客服");
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
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setMessage("正在启动支付控件，请稍候...");
		loadingDialog.setIndeterminate(true);
		loadingDialog.setCancelable(true);
	}
	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		dataCallBack=new DataCallBack<RechargeBean>() {

			@Override
			public void processData(RechargeBean object) {
				if (object!=null) {
					if("true".equals(object.success)){
						if(object.entity!=null){
							bean=object;
							// 调起支付模块
							if ("ALIPAY".equals(object.entity.payType)) {
								money = String.valueOf((int) ((Float.valueOf(object.entity.bankAmount)) * 100));
								
								aliPay(money, object.entity.orderNo);

							} else if ("WEIXIN".equals(object.entity.payType)) {
								money = String.valueOf((int) ((Float.valueOf(object.entity.bankAmount)) * 100));
								
								weixinPay(money, object.entity.orderNo);
							}
						}else {
							ShowUtils.showMsg(context, "确认订单失败，请稍后重试");
						}
					}else{
						ShowUtils.showMsg(context, "数据获取失败，请稍后重试");
					}
				}else {
					ShowUtils.showMsg(context, "数据获取失败，请稍后重试");
				}
				
			}
		};
	}
	/**
	 * 返回输入金额是否为空
	 * @return true 空 false 非空
	 */
	public boolean moneyISNull(){
		if (amount=="500"||amount=="1000"||amount=="5000"||amount=="10000"||amount=="30000") {
			return false;
		}else if (amount.equals(et_money.getText().toString())) {
			return false;
		}else {
			return true;
		}
	}

	/**
	 * 点击输入框为0,点击1-5个数字文本为1-5
	 * @param index 取值范围为0~5,其他值默认与0相同
	 */
	public void onlickState(int index) {
		if (index < 0 || index > 5) {
			index = 0;
		}
		if (index != 0) {
			for (int i = 0; i < 5; i++) {
				if (index == i+1) {
					moneyArray.get(i).setTextColor(context.getResources().getColor(
							R.color.text_color_nine));
					moneyArray.get(i).setBackgroundResource(R.drawable.bluebox);
				}else {
					moneyArray.get(i).setTextColor(context.getResources().getColor(
							R.color.text_color_one));
					moneyArray.get(i).setBackgroundResource(R.drawable.graybox);
				}
				et_money.setText("");
			}
		}else {
			for (int i = 0; i < 5; i++) {
				moneyArray.get(i).setTextColor(context.getResources().getColor(
						R.color.text_color_one));
				moneyArray.get(i).setBackgroundResource(R.drawable.graybox);
			}
		}
	}
}
