package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 我的零钱界面
 * @author Administrator
 *
 */
public class MyWalletSmallMoneyActivity extends TitleBaseActivity {
	TextView tv_money;
	TextView tv_pay;
	TextView tv_tixian;
	String userId;
	String userType;//获取用户type；0:普通用户 1:导师 2:代理商
	private String money;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的零钱");
		setRightText("收支明细", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				if("-1".equals(userType)){
					UserStateUtil.ErrorDialog(context, "数据获取有误", "是否登录重试");
				}else if("1".equals(userType)||"2".equals(userType)){
					intent =new Intent(context,MyWalletGetIncomeChartActivity.class);
					startActivity(intent);
				}else {
					intent =new Intent(context,MyWalletDetailUserActivity.class);
					startActivity(intent);
				}
			}
		});
		userId=UserStateUtil.getUserId();//获取用户Id
		userType=UserStateUtil.getUserType();//获取用户type
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_mywallet_smallmoney, null);
		tv_money = (TextView) view.findViewById(R.id.tv_money);
		tv_pay = (TextView) view.findViewById(R.id.tv_pay);
		tv_tixian = (TextView) view.findViewById(R.id.tv_tixian);
		return view;
	}

	@Override
	protected void initListener() {
		super.initListener();
		tv_pay.setOnClickListener(this);
		tv_tixian.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.tv_pay:
			intent =new Intent(this,MyWalletRechargeActivity.class);
			intent.putExtra("userId", userId);
			startActivity(intent);
			finish();
			break;
		case R.id.tv_tixian:

			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {
		Intent intent=getIntent();
		money=intent.getStringExtra("money");
		tv_money.setText(money);
	}

}
