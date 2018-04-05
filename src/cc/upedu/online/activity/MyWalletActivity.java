package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.MyWalletBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 我的钱包首界面
 * @author Administrator
 *
 */
public class MyWalletActivity extends TitleBaseActivity {
	TextView tv_money;
	TextView tv_corn;
	LinearLayout rl_mywallet1;
	LinearLayout rl_mywallet2;

	private RequestVo requestVo;
	private DataCallBack<MyWalletBean> dataCallBack;

	String userId;
	MyWalletBean bean =new MyWalletBean();

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的钱包");
		userId = getIntent().getStringExtra("userId");// 获取用户ID
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_mywallet, null);
		tv_money = (TextView) view.findViewById(R.id.tv_money);
		tv_corn = (TextView) view.findViewById(R.id.tv_corn);
		rl_mywallet1 = (LinearLayout) view.findViewById(R.id.rl_mywallet1);
		rl_mywallet2 = (LinearLayout) view.findViewById(R.id.rl_mywallet2);
		return view;
	}

	@Override
	protected void initListener() {
		super.initListener();
		rl_mywallet1.setOnClickListener(this);
		rl_mywallet2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_mywallet1:
			if (bean!=null&&bean.entity!=null) {
				intent = new Intent(context,MyWalletSmallMoneyActivity.class);
				intent.putExtra("money",bean.entity.money);
				context.startActivity(intent);
			}
			
			break;
		case R.id.rl_mywallet2:
			if (bean!=null&&bean.entity!=null) {
			intent = new Intent(context,MyWalletCoinActivity.class);
			intent.putExtra("coin",bean.entity.coin);
			context.startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void initData() {
		requestVo = new RequestVo(ConstantsOnline.MY_WALLET, context,
				ParamsMapUtil.myWallet(context, userId), new MyBaseParser<>(
						MyWalletBean.class));
		dataCallBack = new DataCallBack<MyWalletBean>() {

			@Override
			public void processData(MyWalletBean object) {
				if (object != null) {
					if ("false".equals(object.success)) {
						ShowUtils.showMsg(context, object.message);
					} else {
						bean=object;
						tv_money.setText(object.entity.money);
						tv_corn.setText(object.entity.coin);
					}
				}else{
					ShowUtils.showMsg(context,"获取数据失败，请稍后重试");
				}
			}
		};
//		getDataServer(requestVo, dataCallBack);
	}
	
	@Override
	protected void onResume() {
		getDataServer(requestVo, dataCallBack);
		super.onResume();
	}

}
