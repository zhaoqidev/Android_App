package cc.upedu.online.activity;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.MyReceivingAddressBean.AddressItem;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.ValidateUtil;
import cc.upedu.online.view.wheelcity.AbstractWheelTextAdapter;
import cc.upedu.online.view.wheelcity.AddressBean;
import cc.upedu.online.view.wheelcity.ArrayWheelAdapter;
import cc.upedu.online.view.wheelcity.AssetsDatabaseManager;
import cc.upedu.online.view.wheelcity.ChooseAddressUtil;
import cc.upedu.online.view.wheelcity.OnWheelChangedListener;
import cc.upedu.online.view.wheelcity.WheelView;
import cc.upedu.online.view.wheelview.MyAlertDialog;
/**
 * 微商城中添加/修改收货地址的界面
 * @author Administrator
 *
 */
public class MicroMallAddressActivity extends TitleBaseActivity {
	private TextView tv_cityaddress;
	private RelativeLayout ll_default,rl_address;
	private EditText et_name,et_phone,et_detailaddress;
	private Button bt_save;
	private boolean isNewAddress;
	private Dialog loadingDialog;
	private int provinceId,cityId,areaId;
	private String provinceText,cityText,areaText;
	private AddressItem oldAddress;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("收货地址");
		oldAddress = (AddressItem)getIntent().getSerializableExtra("oldAddress");
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_micromall_address_add, null);
		ll_default = (RelativeLayout) view.findViewById(R.id.ll_default);
		et_name = (EditText) view.findViewById(R.id.et_name);
		et_phone = (EditText) view.findViewById(R.id.et_phone);
		rl_address = (RelativeLayout) view.findViewById(R.id.rl_address);
		tv_cityaddress = (TextView) view.findViewById(R.id.tv_cityaddress);
		et_detailaddress = (EditText) view.findViewById(R.id.et_detailaddress);
		bt_save = (Button) view.findViewById(R.id.bt_save);
		
		setViewdata();
		return view;
	}
	private void setViewdata() {
		// TODO Auto-generated method stub
		if (oldAddress == null) {
			setTitleText("新增收货地址");
			isNewAddress = true;
//			et_name.setText("");
//			et_phone.setText("");
//			tv_cityaddress.setText("");
//			et_detailaddress.setText("");
		}else {
			setTitleText("修改收货地址");
			isNewAddress = false;
			if (!StringUtil.isEmpty(oldAddress.personName)) {
				et_name.setText(oldAddress.personName);
			}else {
				et_name.setText("");
			}
			if (!StringUtil.isEmpty(oldAddress.mobile)) {
				et_phone.setText(oldAddress.mobile);
			}else {
				et_phone.setText("");
			}
			if (!StringUtil.isEmpty(oldAddress.provinceText) && !StringUtil.isEmpty(oldAddress.cityText) && !StringUtil.isEmpty(oldAddress.areaText)) {
				tv_cityaddress.setText(new StringBuffer().append(oldAddress.provinceText).append(oldAddress.cityText).append(oldAddress.areaText).toString());
			}else {
				tv_cityaddress.setText("");
			}
			if (!StringUtil.isEmpty(oldAddress.address)) {
				et_detailaddress.setText(oldAddress.address);
			}else {
				et_detailaddress.setText("");
			}
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_default.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		bt_save.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.rl_address://选择省市区
			chooseAdress(tv_cityaddress);
//			if (StringUtil.isEmpty(editText.getText().toString().trim())) {
//				ShowUtils.showMsg(context, "用户名不能为空!");
//			} else {
//				if (ValidateUtil.checkNameChese(editText.getText().toString().trim())) {
//					Intent intent=new Intent();
//					intent.putExtra("name", editText.getText().toString().trim());
//					intent.putExtra("sex", newSex);
//					intent.putExtra("married", newMarital);
//					setResult(RESULT_SETNAME, intent);
//					finish();
//				}else {
//					ShowUtils.showMsg(context, "用户名不能为非中文!");
//				}
//			}
			break;
		case R.id.ll_default:
			if(MicroMallAddressActivity.this.getCurrentFocus()!=null){
				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MicroMallAddressActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
			break;
		case R.id.bt_save:
			loadingDialog = ShowUtils.createLoadingDialog(context, true);
			loadingDialog.show();
			if (requestParamsMap == null) {
				requestParamsMap = new HashMap<>();
			}else {
				requestParamsMap.clear();
			}
			//收件人名称
			String newName = et_name.getText().toString().trim();
			if (StringUtil.isEmpty(newName)) {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				ShowUtils.showMsg(context, "收货人不能为空!");
				return;
			}else {
				if (isNewAddress) {
					requestParamsMap.put("personName", newName);
				}else {
					if (!newName.equals(oldAddress.personName)) {
						requestParamsMap.put("personName", newName);
					}
				}
			}
			//联系方式
			String newPhone = et_phone.getText().toString().trim();
			if (StringUtil.isEmpty(newPhone)) {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				ShowUtils.showMsg(context, "联系方式不能为空!");
				return;
			}else {
				// 1. 通过规则判断手机号
				if (!ValidateUtil.isMobile(newPhone)) {
					if (loadingDialog != null && loadingDialog.isShowing()) {
						loadingDialog.dismiss();
					}
					ShowUtils.showMsg(context, "联系方式手机号格式不正确!");
					return;
				}else {
					if (isNewAddress) {
						requestParamsMap.put("mobile", newPhone);
					}else {
						if (!newPhone.equals(oldAddress.mobile)) {
							requestParamsMap.put("mobile", newPhone);
						}
					}
				}
			}
			//所在地区
			String newCityaddress = tv_cityaddress.getText().toString().trim();
			if (StringUtil.isEmpty(newCityaddress) || "省、市、区".equals(newCityaddress)) {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				ShowUtils.showMsg(context, "所在地区不能为空!");
				return;
			}else {
				if (isNewAddress) {
					requestParamsMap.put("province", String.valueOf(provinceId));
					requestParamsMap.put("city", String.valueOf(cityId));
					if (-1 == areaId) {
						requestParamsMap.put("area", String.valueOf(0));
					}else {
						requestParamsMap.put("area", String.valueOf(areaId));
					}
				}else {
					if (StringUtil.isEmpty(provinceText) || StringUtil.isEmpty(cityText)) {
						
					}else {
						if (!provinceText.equals(oldAddress.provinceText)) {
							requestParamsMap.put("province", String.valueOf(provinceId));
						}
						if (!cityText.equals(oldAddress.cityText)) {
							requestParamsMap.put("city", String.valueOf(cityId));
						}
						if (StringUtil.isEmpty(areaText) && !areaText.equals(oldAddress.areaText)) {
							if (-1 == areaId) {
								requestParamsMap.put("area", String.valueOf(0));
							}else {
								requestParamsMap.put("area", String.valueOf(areaId));
							}
						}
					}
				}
			}
			//详细地址
			String newDetailaddress = et_detailaddress.getText().toString().trim();
			if (StringUtil.isEmpty(newDetailaddress)) {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}
				ShowUtils.showMsg(context, "详细地址不能为空!");
				return;
			}else {
				if (isNewAddress) {
					requestParamsMap.put("address", newDetailaddress);
				}else {
					if (!newDetailaddress.equals(oldAddress.address)) {
						requestParamsMap.put("address", newDetailaddress);
					}
				}
			}
			if (requestParamsMap.size() > 0) {
				requestParamsMap.put("userId", UserStateUtil.getUserId());
				//地址id,新地址为0
				if (isNewAddress) {
					requestParamsMap.put("addressId", "0");
				}else {
					requestParamsMap.put("addressId", oldAddress.addressId);
				}
				UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.ADD_RECEIVINGADDRESS,requestParamsMap , new UploadDataCallBack() {

					@Override
					public void onUploadDataSuccess() {
						// TODO Auto-generated method stub
						ShowUtils.showMsg(context, "保存收货地址成功");
						if (loadingDialog != null && loadingDialog.isShowing()) {
							loadingDialog.dismiss();
						}
						MicroMallAddressActivity.this.finish();
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
				ShowUtils.showMsg(context, "您没有修改数据,不能保存!");
			}
			break;
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (isShowDialog) {
        		dialog.dismiss();
			}else {
				if (loadingDialog != null && loadingDialog.isShowing()) {
					loadingDialog.dismiss();
				}else {
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							MicroMallAddressActivity.this.finish();
						}
					});
				}
			}
			return false;
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
	private void chooseAdress(final TextView tv) {
		View view = dialogm();
		dialog = new MyAlertDialog(MicroMallAddressActivity.this)
				.builder()
				.setTitle(tv.getText().toString())
				.setView(view)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						isShowDialog = !isShowDialog;
					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Toast.makeText(getApplicationContext(), cityTxt, 1).show();
				//省的id,,
				provinceId = provinceList.get(country.getCurrentItem()).getId();
				//市的id
				cityId = cityList.get(city.getCurrentItem()).getId();
				//区的id
				if(areaList.size() > 0){
					areaId = areaList.get(ccity.getCurrentItem()).getId();
				}else {
					areaId = -1;
				}

				provinceText = provinceList.get(country.getCurrentItem()).getArea_name();
				cityText = cityList.get(city.getCurrentItem()).getArea_name();
				if (-1 == areaId) {
					tv.setText(new StringBuffer().append(provinceText)
//							.append("、")
							.append(cityText)
//							.append("、")
							.toString());
				}else {
					areaText = areaList.get(ccity.getCurrentItem()).getArea_name();
					tv.setText(new StringBuffer().append(provinceText)
//						.append("、")
							.append(cityText)
//						.append("、")
							.append(areaText).toString());
				}
				
				dialog.dismiss();
				isShowDialog = !isShowDialog;
//				if (provinceId == -1 || cityId == -1 || areaId == -1) {
//					ShowUtils.showMsg(context, "省、市、区三项需要都设置!");
//				}else {
//				}
			}
		});
		Window window = dialog.getWindow();  
	    window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置   
	    window.setWindowAnimations(R.style.mydatadialogstyle);
		isShowDialog = !isShowDialog;
		dialog.show();
	}
	private WheelView country;
	private WheelView city;
	private WheelView ccity;
	private boolean isShowDialog = false;
	private MyAlertDialog dialog;
	private ArrayList<AddressBean> provinceList;
	private ArrayList<AddressBean> cityList;
	private ArrayList<AddressBean> areaList;
	private ChooseAddressUtil mChoosseAddressUtil;

	private View dialogm() {
		if (db == null) {
			// 初始化，只需要调用一次  
			AssetsDatabaseManager.initManager(context);
			// TODO Auto-generated method stub
			// 获取管理对象，因为数据库需要通过管理对象才能够获取  
			AssetsDatabaseManager mg = AssetsDatabaseManager.getManager();  
			db = mg.getDatabase("mycity.db");
		}
		final View contentView = LayoutInflater.from(context).inflate(R.layout.wheelcity_cities_layout, null);
		if (provinceList == null || provinceList.size() < 1) {
			if (mChoosseAddressUtil == null) {
				mChoosseAddressUtil = new ChooseAddressUtil();
			}
			mChoosseAddressUtil.getAddressData(context, db, mChoosseAddressUtil.new SucceedCallBack() {
				
				@Override
				public void onSucceed(ArrayList<AddressBean> list) {
					if (provinceList == null) {
						provinceList = new ArrayList<AddressBean>();
					}
					provinceList.addAll(list);
					setDialogView(contentView);
				}
			},1);
		}else {
			setDialogView(contentView);
		}
		
		return contentView;
	}
	/**
	 * @param contentView
	 */
	private void setDialogView(final View contentView) {
		// TODO Auto-generated method stub
		country = (WheelView) contentView.findViewById(R.id.wheelcity_country);
//	country.setVisibleItems(2);
		country.setViewAdapter(new CountryAdapter(context));
		
		city = (WheelView) contentView
				.findViewById(R.id.wheelcity_city);
//	city.setVisibleItems(0);
		
		ccity = (WheelView) contentView
				.findViewById(R.id.wheelcity_ccity);
//	ccity.setVisibleItems(0);// 不限城市
		
		country.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateCities(city, provinceList.get(newValue).getId(),true);
//				if (0 != newValue) {
//				}
				
			}
		});
		city.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
//				updatecCities(ccity, AddressData.COUNTIES, country.getCurrentItem(), newValue);
				if (-1 != newValue) {
					updateCities(ccity, city.getViewAdapter().getItemId(newValue),false);
				}else {
					areaList.clear();
					ArrayWheelAdapter adapter = new ArrayWheelAdapter(context,areaList);
					adapter.setTextSize(16);
					city.setViewAdapter(adapter);
					city.setCurrentItem(0);
				}
			}
		});
		
		ccity.addChangingListener(new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				
			}
		});
		
		country.setCurrentItem(0);
//		city.setCurrentItem(1);
//		ccity.setCurrentItem(1);
	}
	/**
	 * Updates the city wheel
	 */
	private void updateCities(final WheelView city,int id,boolean iscity) {
		if (iscity) {
			if (cityList == null) {
				cityList = new ArrayList<AddressBean>();
			}
			mChoosseAddressUtil.getAddressData(context, db, mChoosseAddressUtil.new SucceedCallBack() {
				public void onSucceed(java.util.ArrayList<AddressBean> List) {
					cityList.clear();
					cityList.addAll(List);
					ArrayWheelAdapter adapter = new ArrayWheelAdapter(context,cityList);
					adapter.setTextSize(16);
					city.setViewAdapter(adapter);
					city.setCurrentItem(0);
				};
			},id);
		}else {
			if (areaList == null) {
				areaList = new ArrayList<AddressBean>();
			}
			mChoosseAddressUtil.getAddressData(context, db, mChoosseAddressUtil.new SucceedCallBack() {
				public void onSucceed(java.util.ArrayList<AddressBean> List) {
					areaList.clear();
					areaList.addAll(List);
					ArrayWheelAdapter adapter = new ArrayWheelAdapter(context,areaList);
					adapter.setTextSize(16);
					city.setViewAdapter(adapter);
					city.setCurrentItem(0);
				};
			},id);
		}
	}
	/**
	 * Updates the ccity wheel
	 */
//	private void updatecCities(WheelView city, String ccities[][][], int index,
//			int index2) {
//		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(this,
//				ccities[index][index2]);
//		adapter.setTextSize(16);
//		city.setViewAdapter(adapter);
//		city.setCurrentItem(0);
//	}
	/**
	 * Adapter for countries
	 */
	private class CountryAdapter extends AbstractWheelTextAdapter {
		// Countries names
//		private String countries[] = AddressData.PROVINCES;

		/**
		 * Constructor
		 */
		protected CountryAdapter(Context context) {
			super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
			setItemTextResource(R.id.wheelcity_country_name);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return provinceList.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
//			if (0 == index) {
//				return "未设置";
//			}else {
//			}
			return provinceList.get(index).getArea_name();
		}

		@Override
		public int getItemId(int index) {
			// TODO Auto-generated method stub
//			if (0 == index) {
//				return -1;
//			}else {
//			}
			return provinceList.get(index).getId();
		}
	}
	
	private HashMap<String, String> requestParamsMap;
	private SQLiteDatabase db;
}
