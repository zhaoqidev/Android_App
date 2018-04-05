package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cc.upedu.online.utils.ValidateUtil;

/**
 * 送成长币给盆友界面
 * 
 * @author Administrator
 * 
 */
public class MyWalletCoinToFriendsActivity extends TitleBaseActivity {
	LinearLayout ll_content;
	EditText et_number;// 输入成长币数量
	TextView tv_select_friend;// 选择好友
	TextView tv_confirm;// 确认赠送按钮

	private RequestVo requestVo;
	private DataCallBack<sendCoinBean> dataCallBack;

	String userId;// 用户ID
	String amount;//成长币数量
	String friendId;//好友Id
	String name;
	
	private final static int REQUEST_FRIEND=1;//用于两个activity之间的数据传值
	static int REQUEST_COIN=2;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("送成长币");
		userId = UserStateUtil.getUserId();// 获取到用户ID
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_mywallet_chengzhangbi_tofriend, null);
		ll_content = (LinearLayout) view.findViewById(R.id.ll_content);
		et_number = (EditText) view.findViewById(R.id.et_number);
		tv_select_friend = (TextView) view.findViewById(R.id.tv_select_friend);
		tv_confirm = (TextView) view.findViewById(R.id.tv_confirm);
		return view;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == REQUEST_FRIEND) {
				if (resultCode==SelectFriendActivity.REQUEST_FRIEND){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						name = bundle.getString("userName");
						friendId=bundle.getString("userId");
						tv_select_friend.setText(name);
					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	protected void  getRequestVo() {
		Map<String, String> requestDataMap = ParamsMapUtil.sendCoin(context, userId, amount, friendId);
		requestVo = new RequestVo(ConstantsOnline.SEND_COIN,
				context, requestDataMap, new MyBaseParser<>(
						sendCoinBean.class));
	}
	protected void getDataCallBack() {
		dataCallBack=new DataCallBack<sendCoinBean>() {

			@Override
			public void processData(sendCoinBean object) {
				//请求成功，携带减的成长币的值返回
				if(!StringUtil.isEmpty(object.success)){
					if("true".equals(object.success)){
						Intent intent = new Intent(context,MyWalletCoinActivity.class);
						intent.putExtra("amount", amount);
			            setResult(REQUEST_COIN, intent);  
			            ShowUtils.showMsg(context, object.message);
			            finish();
					}else {
						ShowUtils.showMsg(context, object.message);
					}
				}else {
					ShowUtils.showMsg(context, "数据获取失败，请稍后重试");
				}
				
				
			}
		};
	}

	@Override
	protected void initListener() {
		super.initListener();
		tv_select_friend.setOnClickListener(this);
		tv_confirm.setOnClickListener(this);
		ll_content.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_select_friend:
			Intent intent = new Intent();
			intent.setClass(this, SelectFriendActivity.class);
			startActivityForResult(intent, REQUEST_FRIEND);
			break;
		case R.id.tv_confirm:
			
			UserStateUtil.loginInFailuer(context, null, new SuccessCallBack() {
				
				@Override
				public void onSuccessCallBack() {
					amount=et_number.getText().toString().trim();//获取成长币数量
					if(StringUtil.isEmpty(amount)){
						ShowUtils.showMsg(context, "请输入成长币数量");
						return;
					}else if("0".equals(amount)){
						ShowUtils.showMsg(context, "成长币数量不能为0哦~");
						return;
					}else if (!ValidateUtil.isInt(amount)) {
						ShowUtils.showMsg(context, "成长币数量不能是小数哦~");
						return;
					}else if(Integer.parseInt(amount)<0){
						ShowUtils.showMsg(context, "成长币数量不能是负数哦~");
						return;
					}else if(Integer.parseInt(amount)>Integer.parseInt(coin)){
						ShowUtils.showMsg(context, "成长币数量不足哦~");
						return;
					}else if(StringUtil.isEmpty(friendId)){
						ShowUtils.showMsg(context, "请选择赠送的好友哦~");
						return;
					}else{
						getRequestVo();
						getDataCallBack();
						getDataServer(requestVo, dataCallBack);
					}
					
				}
			});
			
			
			break;
		case R.id.ll_content:
			if(MyWalletCoinToFriendsActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MyWalletCoinToFriendsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		default:
			break;
		}
	}
	String coin;//成长币总数
	
	@Override
	protected void initData() {
		
		Intent intent = getIntent();
		coin = intent.getStringExtra("coin");
		
	}
	/**
	 * 赠送成长币的Javabean对象
	 * @author Administrator
	 *
	 */
	class sendCoinBean{
		String message;
		String success;
	}

}
