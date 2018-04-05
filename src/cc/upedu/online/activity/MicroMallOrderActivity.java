package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelBottomBaseActivity;
import cc.upedu.online.domin.MicroMallListFromVdianBean.Item;
import cc.upedu.online.domin.MyReceivingAddressBean.AddressItem;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 微商城中确认订单界面
 * @author Administrator
 *
 */
public class MicroMallOrderActivity extends TwoPartModelBottomBaseActivity {
	public static final int REQUEST_CHOOSEADDRESS = 60;
	private Dialog loadingDialog;
	private Item commodityItem;
	private ImageView iv_commodity_pic;
	private TextView tv_commodity_name,tv_price,tv_balance,tv_czbprice,tv,tv_name,tv_phone,tv_detailed_address;
	private RelativeLayout rl_address,rl_detailed_address;
	private EditText et_remark;
	private Button bt_save;
	private String addressId;
	private AddressItem addressItem;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		setViewdata();
	}
	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_order_bottom, null);
		tv_czbprice = (TextView) view.findViewById(R.id.tv_czbprice);
		bt_save = (Button) view.findViewById(R.id.bt_save);
		return view;
	}
	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		View view = View.inflate(context, R.layout.activity_micromall_order, null);
		iv_commodity_pic = (ImageView) view.findViewById(R.id.iv_commodity_pic);
		tv_commodity_name = (TextView) view.findViewById(R.id.tv_commodity_name);
		tv_price = (TextView) view.findViewById(R.id.tv_price);
		tv_balance = (TextView) view.findViewById(R.id.tv_balance);
		rl_address = (RelativeLayout) view.findViewById(R.id.rl_address);
		tv = (TextView) view.findViewById(R.id.tv);
		tv.setVisibility(View.VISIBLE);
		rl_detailed_address = (RelativeLayout) view.findViewById(R.id.rl_detailed_address);
		rl_detailed_address.setVisibility(View.GONE);
		tv_name = (TextView) view.findViewById(R.id.tv_name);
		tv_phone = (TextView) view.findViewById(R.id.tv_phone);
		tv_detailed_address = (TextView) view.findViewById(R.id.tv_detailed_address);
		et_remark = (EditText) view.findViewById(R.id.et_remark);
		
		return view;
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("订单确认");
//		commodityItem = (Item)getIntent().getSerializableExtra("commodityItem");
		commodityItem=(Item) PreferencesObjectUtil.readObject("commodityItem", context);
		coin = getIntent().getStringExtra("coin");
	}
	private void setViewdata() {
		if (!StringUtil.isEmpty(commodityItem.imgs.get(0))) {
			
			ImageUtils.setImage(commodityItem.imgs.get(0), iv_commodity_pic, 0);
			
		}
		if (!StringUtil.isEmpty(commodityItem.item_name)) {
			tv_commodity_name.setText(commodityItem.item_name);
		}else {
			tv_commodity_name.setText("商品名称");
		}
		if (!StringUtil.isEmpty(commodityItem.price)) {
			tv_price.setText(commodityItem.price+"成长币");
			tv_czbprice.setText(commodityItem.price+"成长币");
		}else {
			tv_price.setText("*成长币");
			tv_czbprice.setText("*成长币");
		}
		if (!StringUtil.isEmpty("coin")) {
			tv_balance.setText(coin);
		}else {
			tv_balance.setText("0");
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
//		ll_default.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		bt_save.setOnClickListener(this);
	}
	private HashMap<String, String> requestParamsMap;
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_address://选择地址
			intent = new Intent(context, MyReceivingAddressActivity.class);
			intent.putExtra("hasResultBack", true);
			startActivityForResult(intent, REQUEST_CHOOSEADDRESS);
			break;
//		case R.id.ll_default:
//			if(MicroMallOrderActivity.this.getCurrentFocus()!=null){
//				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MicroMallOrderActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//			break;
		case R.id.bt_save:
			loadingDialog = ShowUtils.createLoadingDialog(context, true);
			loadingDialog.show();
			if (requestParamsMap == null) {
				requestParamsMap = new HashMap<>();
			}else {
				requestParamsMap.clear();
			}
			
			//商品id
			if (StringUtil.isEmpty(commodityItem.itemid)) {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				ShowUtils.showMsg(context, "货物信息错误,请重新下单!");
				return;
			}else {
				requestParamsMap.put("commodityId", commodityItem.itemid);
			}
			
			//商品名
			if (!StringUtil.isEmpty(commodityItem.item_name)) {
				requestParamsMap.put("commodityName", commodityItem.item_name);
			}
			//图片url
			if (!StringUtil.isEmpty(commodityItem.imgs.get(0))) {
				requestParamsMap.put("imageUrl", commodityItem.imgs.get(0));
			}
			//成长币数量
			if (!StringUtil.isEmpty(commodityItem.price)) {
				String num[]=commodityItem.price.split("\\.");
				requestParamsMap.put("amount", num[0]);
			}
			
			//收货地址id
			if (StringUtil.isEmpty(addressId)) {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				ShowUtils.showMsg(context, "请选择收货地址!");
				return;
			}else {
				requestParamsMap.put("addressId", addressId);
			}
			//备注信息
			String remark = et_remark.getText().toString().trim();
			if (!StringUtil.isEmpty(remark)) {
				requestParamsMap.put("remarks", remark);
			}
			
			if (requestParamsMap.size() > 0) {
				requestParamsMap.put("userId", UserStateUtil.getUserId());
				UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.CREATE_POINTORDER,requestParamsMap , new UploadDataCallBack() {

					@Override
					public void onUploadDataSuccess() {
						// TODO Auto-generated method stub
						ShowUtils.showMsg(context, "订单确定成功");
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
						Intent intent = new Intent(context, MicroMallExchangeRecordActivity.class);
						startActivity(intent);
						MicroMallOrderActivity.this.finish();
					}

					@Override
					public void onUploadDataFailure() {
						// TODO Auto-generated method stub
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
					}
				});
			}else {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				ShowUtils.showMsg(context, "数据错误不能提交,请反馈!");
			}
			
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode==REQUEST_CHOOSEADDRESS){//设置的数据回传
				if (resultCode==MyReceivingAddressActivity.RESULT_CHOOSEADDRESS){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						addressItem = (AddressItem) bundle.getSerializable("addressItem");
						if (addressItem != null) {
							setAddressData();
						}else {
							ShowUtils.showMsg(context, "选择地址错误,请反馈信息,谢谢!");
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 */
	private void setAddressData() {
		tv.setVisibility(View.GONE);
		rl_detailed_address.setVisibility(View.VISIBLE);
		if (!StringUtil.isEmpty(addressItem.personName)) {
			tv_name.setText(addressItem.personName);
		}
		if (!StringUtil.isEmpty(addressItem.mobile)) {
			tv_phone.setText(addressItem.mobile);
		}
		if (!StringUtil.isEmpty(addressItem.provinceText) && !StringUtil.isEmpty(addressItem.address)) {
			if (!StringUtil.isEmpty(addressItem.areaText)) {
				tv_detailed_address.setText(new StringBuffer().append(addressItem.provinceText).append(addressItem.cityText)
						.append(addressItem.areaText).append(addressItem.address));
			}else {
				tv_detailed_address.setText(new StringBuffer().append(addressItem.provinceText).append(addressItem.cityText)
						.append(addressItem.address));
			}
		}
		addressId = addressItem.addressId;
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (loadingDialog != null && loadingDialog.isShowing()) {
        		loadingDialog.dismiss();
        	}else {
        		ShowUtils.showDiaLog(context, "温馨提醒", "您的订单还未提交,是否要取消!", new ConfirmBackCall() {
        			@Override
        			public void confirmOperation() {
        				MicroMallOrderActivity.this.finish();
        			}
        		});
        	}
			return false;
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
	private String coin;
}
