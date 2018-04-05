package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.TwoPartModelTopBaseActivity;
import cc.upedu.online.domin.UserCordBean;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;
import cc.upedu.online.view.factory.MyHorizontalIconTextItem;

public class AddCompanyActivity extends TwoPartModelTopBaseActivity {
	public static final int RESULT_SETCOMPANYITEM = 14;
	public static final int REQUEST_SETCOMPANYNAME = 15;
	public static final int REQUEST_SETINDUSTRY = 16;
	public static final int REQUEST_SETCOMPANYCITY = 17;
	public static final int REQUEST_SETPRODUCTLIST = 18;
	public static final int REQUEST_SETPOSITION = 19;
	public static final int REQUEST_SETCOMPANYWEB = 20;
	private TextView tv_setcompanyname,tv_setindustry,tv_setcompanycity,tv_setproductlist,tv_setposition,tv_setcompanyweb;
	private CompanyItem oldCompanyItem,alterCompanyItem,newCompanyItem;
	private LinearLayout ll_setcompanyname,ll_setindustry,ll_setcompanycity,ll_setproductlist,ll_setposition,ll_setcompanyweb;
	private boolean isNewCompany = false;
	private List<ProductItem> alterProductList,addProductList;
	private List<String> delProductIdList;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的企业");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveCompany();
			}
		});
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (isChange)
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							AddCompanyActivity.this.finish();
						}
					});
				else
					AddCompanyActivity.this.finish();

			}
		});
	}

	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		MyHorizontalIconTextItem topview = new MyHorizontalIconTextItem(MyHorizontalIconTextItem.TEXT);
		View view = topview.initView(context);
		topview.setBackgroundColor(getResources().getColor(R.color.backGrond));
		topview.setText(R.string.name_companyitem_doc);
		return view;
	}

	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		oldCompanyItem = (CompanyItem)getIntent().getSerializableExtra("companyItem");
		
		if (oldCompanyItem != null) {
			isNewCompany = false;
			alterCompanyItem = (CompanyItem)getIntent().getSerializableExtra("alterCompanyItem");
			if (alterCompanyItem == null) {
				alterCompanyItem = oldCompanyItem;
			}
			alterProductList = alterCompanyItem.getAlterProductList();
			addProductList = alterCompanyItem.getAddProductList();
		}else {
			isNewCompany = true;
			newCompanyItem = new UserCordBean().new Entity().new UserInfo().new CompanyItem();
			newCompanyItem.setId("#"+System.currentTimeMillis());
		}
		
		View view = View.inflate(context, R.layout.activity_setcompanyitem, null);
		ll_setcompanyname = (LinearLayout) view.findViewById(R.id.ll_setcompanyname);
		tv_setcompanyname = (TextView) view.findViewById(R.id.tv_setcompanyname);
		ll_setindustry = (LinearLayout) view.findViewById(R.id.ll_setindustry);
		tv_setindustry = (TextView) view.findViewById(R.id.tv_setindustry);
		ll_setcompanycity = (LinearLayout) view.findViewById(R.id.ll_setcompanycity);
		tv_setcompanycity = (TextView) view.findViewById(R.id.tv_setcompanycity);
		ll_setproductlist = (LinearLayout) view.findViewById(R.id.ll_setproductlist);
		tv_setproductlist = (TextView) view.findViewById(R.id.tv_setproductlist);
		ll_setposition = (LinearLayout) view.findViewById(R.id.ll_setposition);
		tv_setposition = (TextView) view.findViewById(R.id.tv_setposition);
		ll_setcompanyweb = (LinearLayout) view.findViewById(R.id.ll_setcompanyweb);
		tv_setcompanyweb = (TextView) view.findViewById(R.id.tv_setcompanyweb);
		
		setData();
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_setcompanyname.setOnClickListener(this);
		ll_setindustry.setOnClickListener(this);
		ll_setcompanycity.setOnClickListener(this);
		ll_setproductlist.setOnClickListener(this);
		ll_setposition.setOnClickListener(this);
		ll_setcompanyweb.setOnClickListener(this);
	}
	/**
	 * 
	 */
	private void setData() {
		//设置企业名称
		if (!isNewCompany && !StringUtil.isEmpty(alterCompanyItem.getName())) {
			tv_setcompanyname.setText(alterCompanyItem.getName());
		}else if (isNewCompany && !StringUtil.isEmpty(newCompanyItem.getName())) {
			tv_setcompanyname.setText(newCompanyItem.getName());
		}else {
			tv_setcompanyname.setText("未设置");
		}
		//设置所在行业
		if (!isNewCompany && !StringUtil.isEmpty(alterCompanyItem.getIndustryName())) {
			tv_setindustry.setText(alterCompanyItem.getIndustryName());
		}else if (isNewCompany && !StringUtil.isEmpty(newCompanyItem.getIndustryName())) {
			tv_setindustry.setText(newCompanyItem.getIndustryName());
		}else {
			tv_setindustry.setText("未设置");
		}
		//设置所在城市
		if (!isNewCompany && !StringUtil.isEmpty(alterCompanyItem.getCityName())) {
			tv_setcompanycity.setText(alterCompanyItem.getCityName());
		}else if (isNewCompany && !StringUtil.isEmpty(newCompanyItem.getCityName())) {
			tv_setcompanycity.setText(newCompanyItem.getCityName());
		}else {
			tv_setcompanycity.setText("未设置");
		}
		//设置产品信息
		if (!isNewCompany && alterCompanyItem.getProductList() != null && alterCompanyItem.getProductList().size() > 0) {
			tv_setproductlist.setText(alterCompanyItem.getProductList().get(0).getTitle());
		}else  if (isNewCompany && newCompanyItem.getProductList() != null && newCompanyItem.getProductList().size() > 0) {
			tv_setproductlist.setText(newCompanyItem.getProductList().get(0).getTitle());
		}else {
			if (alterProductList != null && alterProductList.size() > 0) {
				tv_setproductlist.setText(alterProductList.get(0).getTitle());
			}else if (addProductList != null && addProductList.size() > 0) {
				tv_setproductlist.setText(addProductList.get(0).getTitle());
			}else {
				tv_setproductlist.setText("未设置");
			}
			
		}
		//设置职位信息
		if (!isNewCompany && !StringUtil.isEmpty(alterCompanyItem.getPositionName())) {
			tv_setposition.setText(alterCompanyItem.getPositionName());
		}else if (isNewCompany && !StringUtil.isEmpty(newCompanyItem.getPositionName())) {
			tv_setposition.setText(newCompanyItem.getPositionName());
		}else {
			tv_setposition.setText("未设置");
		}
		//设置公司网址
		if (!isNewCompany && !StringUtil.isEmpty(alterCompanyItem.getWebsite())) {
			tv_setcompanyweb.setText(alterCompanyItem.getWebsite());
		}else if (isNewCompany && !StringUtil.isEmpty(newCompanyItem.getWebsite())) {
			tv_setcompanyweb.setText(newCompanyItem.getWebsite());
		}else {
			tv_setcompanyweb.setText("未设置");
		}
	}
	private boolean isChange = false;
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode==REQUEST_SETCOMPANYNAME){//设置企业名称的数据回传
				if (resultCode==SetCompanyNameActivity.RESULT_SETCOMPANYNAME){
					Bundle bundle=data.getExtras();
					if (bundle != null) {
						String companyName=bundle.getString("companyName");
						String isnameopen=bundle.getString("isnameopen");
						if (isNewCompany) {
							if (!StringUtil.isEmpty(isnameopen)) {
								newCompanyItem.setIsnameopen(isnameopen);
								if (!isChange)
									isChange = true;
							}
							if (!StringUtil.isEmpty(companyName)) {
								tv_setcompanyname.setText(companyName);
								newCompanyItem.setName(companyName);
								if (!isChange)
									isChange = true;
							}
						}else {
							if (companyName.equals(oldCompanyItem.getName())) {
							}else {
								tv_setcompanyname.setText(companyName);
								alterCompanyItem.setName(companyName);
								if (!isChange)
									isChange = true;
							}
							if (isnameopen.equals(oldCompanyItem.getIsnameopen())) {
							}else {
								alterCompanyItem.setIsnameopen(isnameopen);
								if (!isChange)
									isChange = true;
							}
						}
					}
				}
			}
			if (requestCode==REQUEST_SETINDUSTRY){//设置所属行业的数据回传
				if (resultCode==SetIndustryActivity.RESULT_SETINDUSTRY){
					Bundle bundle=data.getExtras(); 
					if (bundle != null) {
						String industry=bundle.getString("industry");
						String industryname=bundle.getString("industryname");
						if (isNewCompany) {
							if (!StringUtil.isEmpty(industry)) {
								tv_setindustry.setText(industryname);
								newCompanyItem.setIndustry(industry);
								newCompanyItem.setIndustryName(industryname);
								if (!isChange)
									isChange = true;
							}
						}else {
							if (industry.equals(oldCompanyItem.getIndustryName())) {
							}else {
								tv_setindustry.setText(industryname);
								alterCompanyItem.setIndustry(industry);
								alterCompanyItem.setIndustryName(industryname);
								if (!isChange)
									isChange = true;
							}
						}
					}
				}
			}
			if (requestCode==REQUEST_SETPRODUCTLIST){//设置主营产品的数据回传
				if (resultCode==SetProductListActivity.RESULT_SETPRODUCTLIST){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						addProductList =(List<ProductItem>) bundle.getSerializable("addProductList");
						if (isNewCompany) {
							if (addProductList != null && addProductList.size() > 0) {
								newCompanyItem.setAddProductList(addProductList);
								tv_setproductlist.setText(addProductList.get(0).getTitle());
								if (!isChange)
									isChange = true;
							}
						}else {
							alterProductList =(List<ProductItem>) bundle.getSerializable("alartProductList");
							if (alterProductList != null && alterProductList.size() > 0) {
								alterCompanyItem.setAlterProductList(alterProductList);
								tv_setproductlist.setText(alterProductList.get(0).getTitle());
								if (!isChange)
									isChange = true;
							}
							if (addProductList != null && addProductList.size() > 0) {
								alterCompanyItem.setAddProductList(addProductList);
								tv_setproductlist.setText(addProductList.get(0).getTitle());
								if (!isChange)
									isChange = true;
							}
							delProductIdList = (List<String>) bundle.getSerializable("delProductIdList");
							if (delProductIdList != null && delProductIdList.size() > 0) {
								alterCompanyItem.setDelProductIdList(delProductIdList);
								if (delProductIdList.size() < alterCompanyItem.getProductList().size()) {
									for (ProductItem productItem : alterCompanyItem.getProductList()) {
										if (!delProductIdList.contains(productItem.getId())) {
											tv_setproductlist.setText(productItem.getTitle());
											break;
										}
									}
								}else {
									tv_setproductlist.setText("未设置");
								}
								if (!isChange)
									isChange = true;
							}
						}
					}
				}
			}
			if (requestCode==REQUEST_SETPOSITION){//设置我在企业中的职位的数据回传
				if (resultCode==SetPositionActivity.RESULT_SETPOSITION){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						String positionid=bundle.getString("positionid");
						String positionName=bundle.getString("positionName");
						if (isNewCompany) {
							if (!StringUtil.isEmpty(positionid)) {
								tv_setposition.setText(positionName);
								newCompanyItem.setPosition(positionid);
								newCompanyItem.setPositionName(positionName);
								if (!isChange)
									isChange = true;
							}
						}else {
							if (positionid.equals(oldCompanyItem.getPosition())) {
							}else {
								tv_setposition.setText(positionName);
								alterCompanyItem.setPosition(positionid);
								alterCompanyItem.setPositionName(positionName);
								if (!isChange)
									isChange = true;
							}
						}
					}
				}
			}
			if (requestCode==REQUEST_SETCOMPANYWEB){//设置企业网站的数据回传
				if (resultCode==SetCompanyWebActivity.RESULT_SETCOMPANYWEB){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						String companyWeb=bundle.getString("companyWeb");
						String iswebsiteopen=bundle.getString("iswebsiteopen");
						if (isNewCompany) {
							if (!StringUtil.isEmpty(companyWeb)) {
								tv_setcompanyweb.setText(companyWeb);
								newCompanyItem.setWebsite(companyWeb);
								if (!isChange)
									isChange = true;
							}
							if (!StringUtil.isEmpty(iswebsiteopen)) {
								newCompanyItem.setIswebsiteopen(iswebsiteopen);
								if (!isChange)
									isChange = true;
							}
						}else {
							if (companyWeb.equals(oldCompanyItem.getWebsite())) {
							}else {
								tv_setcompanyweb.setText(companyWeb);
								alterCompanyItem.setWebsite(companyWeb);
								if (!isChange)
									isChange = true;
							}
							if (iswebsiteopen.equals(oldCompanyItem.getIswebsiteopen())) {
							}else {
								alterCompanyItem.setIswebsiteopen(iswebsiteopen);
								if (!isChange)
									isChange = true;
							}
						}
					}
				}
			}
			if (requestCode == CityChooseActity.CHOOSE_ONE) {// 设置企业所在地的回传
				if (resultCode == CityChooseActity.CHOOSE_ONE) {
					Bundle bundle=data.getExtras();
					if (bundle != null) {
						String newCompanyCity = bundle.getString("text");
						String cityId = bundle.getString("id");
						if (isNewCompany) {
							if (!StringUtil.isEmpty(cityId)) {
								tv_setcompanycity.setText(newCompanyCity);
								newCompanyItem.setCity(cityId);
								newCompanyItem.setCityName(newCompanyCity);
								if (!isChange)
									isChange = true;
							}
						}else {
							if (cityId.equals(oldCompanyItem.getCity())) {
							}else {
								tv_setcompanycity.setText(newCompanyCity);
								alterCompanyItem.setCityName(newCompanyCity);
								alterCompanyItem.setCity(cityId);
								if (!isChange)
									isChange = true;
							}
						}
					}
				}
			}
		}
	}

	protected void saveCompany() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		if (isNewCompany) {
			if (checkoutCompanyItem(newCompanyItem)) {
				intent.putExtra("newCompanyItem", (Serializable)newCompanyItem);
				setResult(RESULT_SETCOMPANYITEM, intent);
				finish();
			}
		}else {
			if (checkoutCompanyItem(alterCompanyItem)) {
				intent.putExtra("alterCompanyItem", (Serializable)alterCompanyItem);
				setResult(RESULT_SETCOMPANYITEM, intent);
				finish();
			}
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_setcompanyname://设置企业名称
			intent = new Intent(context, SetCompanyNameActivity.class);
			if (!isNewCompany) {
				intent.putExtra("companyName", oldCompanyItem.getName());
				intent.putExtra("isnameopen", oldCompanyItem.getIsnameopen());
			}else {
				intent.putExtra("companyName", newCompanyItem.getName());
				intent.putExtra("isnameopen", newCompanyItem.getIsnameopen());
			}
			startActivityForResult(intent, REQUEST_SETCOMPANYNAME);
			break;
		case R.id.ll_setindustry://设置所属行业
			intent = new Intent(context, SetIndustryActivity.class);
			if (!isNewCompany) {
				intent.putExtra("industry", oldCompanyItem.getIndustry());
			}else {
				intent.putExtra("industry", newCompanyItem.getIndustry());
			}
			startActivityForResult(intent, REQUEST_SETINDUSTRY);
			break;
		case R.id.ll_setcompanycity://设置企业所在地
			intent = new Intent(context, CityChooseActity.class);
			intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_ONE);
			startActivityForResult(intent, CityChooseActity.CHOOSE_ONE);
			break;
		case R.id.ll_setproductlist://设置主营产品
			intent = new Intent(context, SetProductListActivity.class);
			if (!isNewCompany) {
				intent.putExtra("productList", (Serializable)alterCompanyItem.getProductList());
				intent.putExtra("alterProductList", (Serializable)alterCompanyItem.getAlterProductList());
				intent.putExtra("addProductList", (Serializable)alterCompanyItem.getAddProductList());
				intent.putExtra("delProductIdList", (Serializable)alterCompanyItem.getDelProductIdList());
			}else {
				if (newCompanyItem != null) {
					intent.putExtra("productList", (Serializable)newCompanyItem.getProductList());
				}
			}
			startActivityForResult(intent, REQUEST_SETPRODUCTLIST);
			break;
		case R.id.ll_setposition://设置我 在企业中的职位
			intent = new Intent(context, SetPositionActivity.class);
			if (!isNewCompany) {
				intent.putExtra("positionid", oldCompanyItem.getPosition());
			}else {
				intent.putExtra("positionid", newCompanyItem.getPosition());
			}
			startActivityForResult(intent, REQUEST_SETPOSITION);
			break;
		case R.id.ll_setcompanyweb://设置企业网站
			intent = new Intent(context, SetCompanyWebActivity.class);
			if (!isNewCompany) {
				intent.putExtra("companyWeb", oldCompanyItem.getWebsite());
				intent.putExtra("iswebsiteopen", oldCompanyItem.getIswebsiteopen());
			}else {
				intent.putExtra("companyWeb", newCompanyItem.getWebsite());
				intent.putExtra("iswebsiteopen", newCompanyItem.getIswebsiteopen());
			}
			startActivityForResult(intent, REQUEST_SETCOMPANYWEB);
			break;
		}
	}
	/**
	 * 对企业的名称,城市,用户所在职位,所属行业进行非空判断
	 * @param companyItem
	 */
	private boolean checkoutCompanyItem(CompanyItem companyItem){
		if (StringUtil.isEmpty(companyItem.getName())) {
			ShowUtils.showMsg(context, "企业名称不能为空");
			return false;
		}else if (StringUtil.isEmpty(companyItem.getCity())) {
			ShowUtils.showMsg(context, "企业所在地不能为空");
			return false;
		}else if (StringUtil.isEmpty(companyItem.getPosition())) {
			ShowUtils.showMsg(context, "用户担任职位不能为空");
			return false;
		}else if (StringUtil.isEmpty(companyItem.getIndustry())) {
			ShowUtils.showMsg(context, "所属行业不能为空");
			return false;
		}else {
			return true;
		}
	}

	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (isChange) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						AddCompanyActivity.this.finish();
					}
				});
				return false;
			}else {
				return super.onKeyDown(keyCode, event);
			}
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}
}
