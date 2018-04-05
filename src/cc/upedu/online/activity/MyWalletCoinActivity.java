package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
/**
 * 我的成长币界面，送成长币给盆友，用倬币充值
 * @author Administrator
 *
 */
public class MyWalletCoinActivity extends TitleBaseActivity {
	TextView tv_money;
	TextView tv_pay;
	TextView tv_tixian;
	static int REQUEST_COIN=2;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的成长币");
		setRightText("收支明细", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MyWalletCoinActivity.this, MyWalletDetailCoinActivity.class);
				startActivity(intent);
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_mywallet_chengzhangbi, null);
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
			intent=new Intent();
			intent.setClass(this, MyWalletCoinToFriendsActivity.class);
			intent.putExtra("coin", coin);
			startActivityForResult(intent, REQUEST_COIN);
			break;
		case R.id.tv_tixian:

			break;

		default:
			break;
		}
	}
	String coin;//成长币总量
	private String amount;
	@Override
	protected void initData() {
		
		Intent intent=getIntent();
		coin=intent.getStringExtra("coin");
		tv_money.setText(coin);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == REQUEST_COIN) {
				if (resultCode==MyWalletCoinToFriendsActivity.REQUEST_COIN){
					Bundle bundle=data.getExtras(); 
					if (bundle != null) {
						amount = bundle.getString("amount");
						coin=String.valueOf(Integer.parseInt(coin)-Integer.parseInt(amount));
						tv_money.setText(coin);
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
