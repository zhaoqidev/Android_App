package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelBottomBaseActivity;
import cc.upedu.online.domin.ConfirmOrderBean;
import cc.upedu.online.domin.ConfirmOrderBean.ConfrimOrderList;
import cc.upedu.online.domin.ShoppingBean.Entity.ShoppingItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
/**
 * 选择支付界面
 * @author Administrator
 *
 */
public class PaymentActivity extends TwoPartModelBottomBaseActivity {
	//支付方式:支付宝/微信/银联
	private static final int ALIPAY = 0;
	private static final int WEIXIN = 1;
	private static final int UNIONPAY = 2;
	private List<ShoppingItem> shoppingList;
	private TextView tv_total;
	private LinearLayout ll_settlement;
	private RelativeLayout ll_aliPay,ll_weixin,ll_unionpay;
	//成长币数量,总价
	private String totalGcoin,oldTotalPrices,newTotalPrices;
	//默认的支付方式
	private int choosePayment = ALIPAY;
	//默认不使用成长币
	private boolean chooseUse = false;
	//选择支付的RadioGroup,选择是否使用成长币的RadioGroup
	private RadioGroup rg_pay,rg_use;
	private RadioButton rb_aliPay,rb_weixin,rb_unionpay,rb_use,rb_nouse;
	private ImageView iv_jianhao,iv_jiahao;
	private EditText et_count;
	private Integer number = 0;
	private double prices = 0.00d;
	
	//private RequestVo requestVo;
	private DataCallBack<ConfirmOrderBean> dataCallBack;
	
	String userId=UserStateUtil.getUserId();
	String courseId = "";
	String payType = "";//支付类型
	String gcoin="";//使用成长币的数量
	private LinearLayout ll_content;
	
	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_payment_bottom, null);
		tv_total = (TextView) view.findViewById(R.id.tv_total);
		ll_settlement = (LinearLayout) view.findViewById(R.id.ll_settlement);
		return view;
	}
	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_payment, null);
		shoppingList = (List<ShoppingItem>)getIntent().getSerializableExtra("shoppingList");
		totalGcoin = getIntent().getStringExtra("totalGcoin");
		oldTotalPrices = getIntent().getStringExtra("totalPrices");
		newTotalPrices = oldTotalPrices;
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		rg_pay = (RadioGroup) view.findViewById(R.id.rg_pay);
		ll_aliPay = (RelativeLayout) view.findViewById(R.id.ll_aliPay);
		rb_aliPay = (RadioButton) view.findViewById(R.id.rb_aliPay);
		ll_weixin = (RelativeLayout) view.findViewById(R.id.ll_weixin);
		rb_weixin = (RadioButton) view.findViewById(R.id.rb_weixin);
		ll_unionpay = (RelativeLayout) view.findViewById(R.id.ll_unionpay);
		rb_unionpay = (RadioButton) view.findViewById(R.id.rb_unionpay);
		
		iv_jianhao = (ImageView) view.findViewById(R.id.iv_jianhao);
		et_count = (EditText) view.findViewById(R.id.et_count);
		iv_jiahao = (ImageView) view.findViewById(R.id.iv_jiahao);
		et_count.addTextChangedListener(myTextWatcher);
		rg_use = (RadioGroup) view.findViewById(R.id.rg_use);
		rb_use = (RadioButton) view.findViewById(R.id.rb_use);
		rb_nouse = (RadioButton) view.findViewById(R.id.rb_nouse);
		rg_use.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_use:
					chooseUse = true;
					prices = (Double.valueOf(oldTotalPrices)*10 - number)/10d;
					if (prices > 0) {
					}else {
						int num = (int)(Double.valueOf(oldTotalPrices)*10);
						if (num<Double.valueOf(oldTotalPrices)*10) {
							prices = Double.valueOf(oldTotalPrices)-num/10;
						}else {
							prices = 0.00d;
						}
						if (!(!StringUtil.isEmpty(et_count.getText().toString().trim()) && et_count.getText().toString().trim().equals(String.valueOf(num)))) {
							et_count.setText(String.valueOf(num));
						}
						iv_jiahao.setImageResource(R.drawable.pay_jiahao_hui);
					}
					newTotalPrices = String.valueOf(prices);
					showTotalPrices();
					break;
				case R.id.rb_nouse:
					chooseUse = false;
					if (!(Double.valueOf(newTotalPrices) > 0)) {
						rb_aliPay.setChecked(true);
						rb_weixin.setChecked(false);
						rb_unionpay.setChecked(false);
					}
					if (StringUtil.isEmpty(et_count.getText().toString().trim())) {
						number = 0;
					}else {
						number = Integer.valueOf(et_count.getText().toString().trim());
					}
					newTotalPrices = oldTotalPrices;
					showTotalPrices();
					break;
				}
				
			}
		});
		
		return view;
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("支付方式");
	}
	/**
	 * 
	 */
	private void showTotalPrices() {
		tv_total.setText(newTotalPrices);
		if (!(Double.valueOf(newTotalPrices) > 0)) {
			rb_aliPay.setChecked(false);
			rb_weixin.setChecked(false);
			rb_unionpay.setChecked(false);
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_content.setOnClickListener(this);
		iv_jianhao.setOnClickListener(this);
		iv_jiahao.setOnClickListener(this);
		ll_settlement.setOnClickListener(this);
		ll_aliPay.setOnClickListener(this);
		ll_weixin.setOnClickListener(this);
		ll_unionpay.setOnClickListener(this);
		rb_aliPay.setOnClickListener(this);
		rb_weixin.setOnClickListener(this);
		rb_unionpay.setOnClickListener(this);
	}

	@Override
	protected void initData() {

		dataCallBack=new DataCallBack<ConfirmOrderBean>() {

			@Override
			public void processData(ConfirmOrderBean object) {
				Intent intent;
				if("true".equals(object.success)){
					if(object.entity!=null){
						if ("true".equals(object.entity.isPayed)) {//判断成长币是否能够全部抵扣
							ShowUtils.showMsg(context, "恭喜您课程购买成功");
							intent=new Intent(context, MyOrderActivity.class);
							
							SharedPreferencesUtil.getInstance().editPutBoolean("showPop", true);//是否显示掉钱袋动画
							SharedPreferencesUtil.getInstance().editPutString("showPop_money", "0.00");//掉钱袋动画上的需要付款的金额
							String course_name="";
							for (int i = 0; i < object.entity.orderInfo.trxorderDetailList.size(); i++) {
								course_name=course_name+"<"+object.entity.orderInfo.trxorderDetailList.get(i).courseName+">";
							}
							SharedPreferencesUtil.getInstance().editPutString("course_name", course_name);//掉钱袋动画上的课程名
							
							SharedPreferencesUtil.getInstance().editPutString("ShoopingCourseId", "");//支付成功后，刷新购物车使用
							startActivity(intent);
							finish();
						}else {
							intent=new Intent(context, OrderDetailActivity.class);
							intent.putExtra("trxorderId", object.entity.orderInfo.trxorderId);//订单信息
							intent.putExtra("requestId", object.entity.orderInfo.requestId);
							intent.putExtra("payType", object.entity.orderInfo.payType);//支付类型
							intent.putExtra("orderAmount", object.entity.orderInfo.orderAmount);//订单总额
							intent.putExtra("amount", object.entity.orderInfo.amount);//实付金额
							intent.putExtra("createTime", object.entity.orderInfo.createTime);//下单时间
							intent.putExtra("balance", object.entity.balance);//零钱余额
							intent.putExtra("confrimOrderList",(Serializable)(ArrayList<ConfrimOrderList>) object.entity.orderInfo.trxorderDetailList);//课程条目
							
							context.startActivity(intent);
							PaymentActivity.this.finish();
						}
						
						
					}else {
						ShowUtils.showMsg(context, "确认订单失败，请稍后重试");
					}
				}else{
					ShowUtils.showMsg(context, object.message);
				}
			}
		};

		showTotalPrices();
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_settlement://确认订单
			UserStateUtil.loginInFailuer(context, new FailureCallBack() {
				
				@Override
				public void onFailureCallBack() {
					ShowUtils.showDiaLog(context, "温馨提醒", "您登录的用户已过期,请重新登录.", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							//跳转到登录界面
							Intent intent = new Intent(context, LoginActivity.class);
							startActivity(intent);
						}
					});
				}
			}, new SuccessCallBack() {
				
				@Override
				public void onSuccessCallBack() {
					//提交订单后,支付
					//choosePayment 支付方式
					//chooseUse 是否使用成长币
					//number 使用地成长币数量
					getRequestVo();
					Map<String, String> params = ParamsMapUtil.confrimOrder(context, userId, courseId, payType, gcoin);
					RequestVo requestVo = new RequestVo(ConstantsOnline.CONFIRM_ORDER, context,
							params, new MyBaseParser<>(ConfirmOrderBean.class));
					getDataServer(requestVo, dataCallBack);
				}
			});
			break;
		case R.id.iv_jianhao:
			if (StringUtil.isEmpty(et_count.getText().toString().trim())) {
				number = 0;
			}else {
				number = Integer.valueOf(et_count.getText().toString().trim());
			}
			if (0 != number) {
				if (Integer.valueOf(totalGcoin) == number) {
					iv_jiahao.setImageResource(R.drawable.pay_jiahao);
				}
				number--;
				et_count.setText(String.valueOf(number));
				if (0 == number) {
					iv_jianhao.setImageResource(R.drawable.pay_jianhao_hui);
				}
			}
			break;
		case R.id.iv_jiahao:
			if (StringUtil.isEmpty(et_count.getText().toString().trim())) {
				number = 0;
			}else {
				number = Integer.valueOf(et_count.getText().toString().trim());
			}
			if (Integer.valueOf(totalGcoin) > number) {
				if (0 == number) {
					iv_jianhao.setImageResource(R.drawable.pay_jianhao);
				}
				number++;
				et_count.setText(String.valueOf(number));
				if (Integer.valueOf(totalGcoin) == number) {
					iv_jiahao.setImageResource(R.drawable.pay_jiahao_hui);
				}
			}
			break;
		case R.id.ll_aliPay://
		case R.id.rb_aliPay:
			choosePayment = ALIPAY;
			rb_aliPay.setChecked(true);
			rb_weixin.setChecked(false);
			rb_unionpay.setChecked(false);
			break;
		case R.id.ll_weixin://
		case R.id.rb_weixin:
			choosePayment = WEIXIN;
			rb_weixin.setChecked(true);
			rb_aliPay.setChecked(false);
			rb_unionpay.setChecked(false);
			break;
		case R.id.ll_unionpay://
		case R.id.rb_unionpay:
			choosePayment = UNIONPAY;
			rb_unionpay.setChecked(true);
			rb_aliPay.setChecked(false);
			rb_weixin.setChecked(false);
			break;
		case R.id.ll_content:
			if(PaymentActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(PaymentActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		}
	}
	public TextWatcher myTextWatcher = new TextWatcher() {
		private CharSequence temp;
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			temp = s;
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if (!StringUtil.isEmpty(temp.toString().trim())) {
				if (Integer.valueOf(temp.toString().trim()) > Integer.valueOf(totalGcoin)) {
					ShowUtils.showMsg(context, "您的成长币只有"+totalGcoin+"余额不足!");
					et_count.setText(totalGcoin);
					number = Integer.valueOf(totalGcoin);
				}else {
					number = Integer.valueOf(temp.toString().trim());
				}
			}else {
				number = 0;
			}
			if (chooseUse) {
				prices = (Double.valueOf(oldTotalPrices)*10 - number)/10d;
				if (prices > 0) {
					iv_jiahao.setImageResource(R.drawable.pay_jiahao);
				}else {
					int num = (int)(Double.valueOf(oldTotalPrices)*10);
					if (num<Double.valueOf(oldTotalPrices)*10) {
						prices = Double.valueOf(oldTotalPrices)-num/10;
					}else {
						prices = 0.00d;
					}
					iv_jiahao.setImageResource(R.drawable.pay_jiahao_hui);
					if (!(!StringUtil.isEmpty(et_count.getText().toString().trim()) && et_count.getText().toString().trim().equals(String.valueOf(num)))) {
						et_count.setText(String.valueOf(num));
					}
				}
				newTotalPrices = String.valueOf(prices);
				showTotalPrices();
			}
		}
	};
	
	protected void getRequestVo() {
		
		for(int i=0;i<shoppingList.size();i++){
			if(i<shoppingList.size()-1){
			courseId=courseId+shoppingList.get(i).getGoodsid()+",";}
			else{
				courseId=courseId+shoppingList.get(i).getGoodsid();
			}
		}
		
		//选择支付方式
			switch (choosePayment) {
			case ALIPAY:
				payType="ALIPAY";
				break;
			case WEIXIN:
				payType="WEIXIN";
				break;
			default:
				break;
				}
				
		//是否使用成长币
		
		if (chooseUse) {
			gcoin=String.valueOf(number);
		}else {
			gcoin="";
		}
		
	}
}
