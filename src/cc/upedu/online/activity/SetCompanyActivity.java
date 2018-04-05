package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.CompanyListAdapter;
import cc.upedu.online.base.DelBaseBackCall;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;
/**
 * 我的企业
 * @author Administrator
 *
 */
public class SetCompanyActivity extends ListBaseActivity<CompanyItem> {
	public static final int RESULT_SETCOMPANY = 13;
	public static final int REQUEST_SETCOMPANYITEM = 14;
	private List<CompanyItem> companyList;
	private List<CompanyItem> alterCompanyList;
	private List<CompanyItem> addCompanyList;
	private List<CompanyItem> newCompanyList;
	private List<String> delCompanyIdList;
//	private ListView lv_company;
//	private CompanyListAdapter mCompanyListAdapter;
	private CompanyItem alterCompanyItem;
	private CompanyItem newCompanyItem;
//	private boolean isNewCompany = false;
	private Map<String, Integer> alterIdPositionList,addIdPositionList;
	private boolean isChange = false;

	@Override
	protected void initData() {
		if (newCompanyList == null) {
			newCompanyList = new ArrayList<CompanyItem>();
		}else {
			newCompanyList.clear();
		}
		if (companyList != null && companyList.size() > 0) {
			newCompanyList.addAll(companyList);
		}
		if (delCompanyIdList == null) {
			delCompanyIdList = new ArrayList<String>();
		}else {
			if (delCompanyIdList.size() > 0) {
				for (int i = 0; i < delCompanyIdList.size(); i++) {
					for (int j = 0; j < newCompanyList.size(); j++) {
						if (delCompanyIdList.get(i).equals(newCompanyList.get(j).getId())) {
							
							newCompanyList.remove(j);
							break;
						}
					}
				}
			}
		}
		if (alterIdPositionList == null) {
			alterIdPositionList = new HashMap<String, Integer>();
		}else {
			alterIdPositionList.clear();
		}
		if (alterCompanyList == null) {
			alterCompanyList = new ArrayList<CompanyItem>();
		}else {
			for (int i = 0; i < alterCompanyList.size(); i++) {
				alterIdPositionList.put(alterCompanyList.get(i).getId(), i);
				for (int j = 0; j < newCompanyList.size(); j++) {
					if (alterCompanyList.get(i).getId().equals(newCompanyList.get(j).getId())) {
						
						newCompanyList.set(j, alterCompanyList.get(i));
						break;
					}
				}
			}
		}
		if (addIdPositionList == null) {
			addIdPositionList = new HashMap<String, Integer>();
		}else {
			addIdPositionList.clear();
		}
		if (addCompanyList == null) {
			addCompanyList = new ArrayList<CompanyItem>();
		}else {
			if (addCompanyList.size() > 0) {
				newCompanyList.addAll(addCompanyList);
				for (int i = 0; i < addCompanyList.size(); i++) {
					addIdPositionList.put(addCompanyList.get(i).getId(), i);
				}
			}
		}
		if (isAdapterEmpty()) {
			setListView(new CompanyListAdapter(context,newCompanyList,new DelBaseBackCall() {
				@Override
				public void delBackCall(int position) {
					//删除功能
					if (newCompanyList.get(position).getId().contains("#")) {
						addCompanyList.remove(addIdPositionList.get(newCompanyList.get(position).getId()));
					}else if (alterIdPositionList.containsKey(newCompanyList.get(position).getId())) {
						alterCompanyList.remove(alterIdPositionList.get(newCompanyList.get(position).getId()));
						delCompanyIdList.add(newCompanyList.get(position).getId());
					}else {
						delCompanyIdList.add(newCompanyList.get(position).getId());
					}
					newCompanyList.remove(position);
					notifyData();
				}
			}));
		}else {
			notifyData();
		}
		
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("所有企业");
		setLeftButton(R.drawable.back, "保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("alterCompanyList", (Serializable) alterCompanyList);
				intent.putExtra("addCompanyList", (Serializable) addCompanyList);
				intent.putExtra("delCompanyIdList", (Serializable) delCompanyIdList);
				setResult(RESULT_SETCOMPANY, intent);
				finish();
			}
		});
		setRightText("添加", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, AddCompanyActivity.class);
//				isNewCompany = true;
				startActivityForResult(intent, REQUEST_SETCOMPANYITEM);
			}
		});

		companyList = (List<CompanyItem>)getIntent().getSerializableExtra("companyList");
		alterCompanyList = (List<CompanyItem>)getIntent().getSerializableExtra("alterCompanyList");
		addCompanyList = (List<CompanyItem>)getIntent().getSerializableExtra("addCompanyList");
		delCompanyIdList = (List<String>)getIntent().getSerializableExtra("delCompanyIdList");
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode==REQUEST_SETCOMPANYITEM){//设置的数据回传
				if (resultCode==AddCompanyActivity.RESULT_SETCOMPANYITEM){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						newCompanyItem = (CompanyItem) bundle.getSerializable("newCompanyItem");
						addCompanyList.add(newCompanyItem);
						newCompanyList.add(newCompanyItem);
						addIdPositionList.put(newCompanyItem.getId(), addIdPositionList.size());
//						if (isNewCompany) {//新数据首次修改
//							isNewCompany = false;
//						}else {//修改旧数据和新数据多次修改
//							alterCompanyItem = (CompanyItem) bundle.getSerializable("alterCompanyItem");
//							if (alterCompanyItem.getId().contains("#")) {//新数据多次修改
//								addCompanyList.remove((int)addIdPositionList.get(alterCompanyItem.getId()));
//								addCompanyList.add(addIdPositionList.get(alterCompanyItem.getId()), alterCompanyItem);
//							}else {
//								if (alterIdPositionList.containsKey(alterCompanyItem.getId())) {//旧数据多次修改
//									alterCompanyList.remove((int)alterIdPositionList.get(alterCompanyItem.getId()));
//									alterCompanyList.add(alterIdPositionList.get(alterCompanyItem.getId()), alterCompanyItem);
//								}else {//旧数据首次修改
//									alterIdPositionList.put(alterCompanyItem.getId(), alterIdPositionList.size());
//									alterCompanyList.add(alterCompanyItem);
//								}
//							}
//						}
						initData();
					}
				}
				//设置企业名称的数据回传
				if (resultCode==SetCompanyNameActivity.RESULT_SETCOMPANYNAME){
					Bundle bundle=data.getExtras();
					if (bundle != null) {
						String companyName=bundle.getString("companyName");
						String currentPosition=bundle.getString("currentPosition");
						alterCompanyItem = newCompanyList.get(Integer.valueOf(currentPosition));
						if (StringUtil.isEmpty(companyName) ) {
							ShowUtils.showMsg(context, "企业名称不能为空!");
						}else {
							if (companyName.equals(alterCompanyItem.getName())) {
							}else {
								alterCompanyItem.setName(companyName);
							}
							String isnameopen=bundle.getString("isnameopen");
							if (isnameopen.equals(alterCompanyItem.getIsnameopen())) {
							}else {
								alterCompanyItem.setIsnameopen(isnameopen);
							}
							saveAlterData();
						}
					}
				}
				//设置所属行业的数据回传
				if (resultCode==SetIndustryActivity.RESULT_SETINDUSTRY){
					Bundle bundle=data.getExtras(); 
					if (bundle != null) {
						String industry=bundle.getString("industry");
						String industryname=bundle.getString("industryname");
						String currentPosition=bundle.getString("currentPosition");
						alterCompanyItem = newCompanyList.get(Integer.valueOf(currentPosition));
						if (StringUtil.isEmpty(industry) ) {
							ShowUtils.showMsg(context, "行业不能为空!");
						}else {
							if (industry.equals(alterCompanyItem.getIndustry())) {
							}else {
								alterCompanyItem.setIndustry(industry);
								alterCompanyItem.setIndustryName(industryname);
								saveAlterData();
							}
						}
					}
				}
				//设置主营产品的数据回传
				if (resultCode==SetProductListActivity.RESULT_SETPRODUCTLIST){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						String currentPosition=bundle.getString("currentPosition");
						alterCompanyItem = newCompanyList.get(Integer.valueOf(currentPosition));
						boolean isChange = false;
						List<ProductItem> addProductList = (List<ProductItem>) bundle.getSerializable("addProductList");
						if (addProductList != null && addProductList.size() > 0) {
							isChange = true;
							alterCompanyItem.setAddProductList(addProductList);
							alterCompanyItem.setShowProductName(addProductList.get(0).getTitle());
						}
						List<ProductItem> alterProductList = (List<ProductItem>) bundle.getSerializable("alartProductList");
						if (alterProductList != null && alterProductList.size() > 0) {
							isChange = true;
							alterCompanyItem.setAlterProductList(alterProductList);
							alterCompanyItem.setShowProductName(alterProductList.get(0).getTitle());
						}
						List<String> delProductIdList = (List<String>) bundle.getSerializable("delProductIdList");
						if (delProductIdList != null && delProductIdList.size() > 0) {
							isChange = true;
							alterCompanyItem.setDelProductIdList(delProductIdList);
							if (delProductIdList.size() < alterCompanyItem.getProductList().size()) {
								for (ProductItem productItem : alterCompanyItem.getProductList()) {
									if (!delProductIdList.contains(productItem.getId())) {
										break;
									}
								}
							}
						}
						if (isChange) {
							saveAlterData();
						}
					}
				}
				//设置我在企业中的职位的数据回传
				if (resultCode==SetPositionActivity.RESULT_SETPOSITION){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						String positionid=bundle.getString("positionid");
						String positionName=bundle.getString("positionName");
						String currentPosition=bundle.getString("currentPosition");
						alterCompanyItem = newCompanyList.get(Integer.valueOf(currentPosition));
						if (StringUtil.isEmpty(positionid) ) {
							ShowUtils.showMsg(context, "职位不能为空!");
						}else {
							if (positionid.equals(alterCompanyItem.getPosition())) {
							}else {
								alterCompanyItem.setPosition(positionid);
								alterCompanyItem.setPositionName(positionName);
								saveAlterData();
							}
						}
					}
				}
				//设置企业网站的数据回传
				if (resultCode==SetCompanyWebActivity.RESULT_SETCOMPANYWEB){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						String companyWeb=bundle.getString("companyWeb");
						String currentPosition=bundle.getString("currentPosition");
						alterCompanyItem = newCompanyList.get(Integer.valueOf(currentPosition));
						if (StringUtil.isEmpty(companyWeb) ) {
							alterCompanyItem.setWebsite("");
						}else {
							if (companyWeb.equals(alterCompanyItem.getWebsite())) {
							}else {
								alterCompanyItem.setWebsite(companyWeb);
							}
							String iswebsiteopen=bundle.getString("iswebsiteopen");
							if (iswebsiteopen.equals(alterCompanyItem.getIswebsiteopen())) {
							}else {
								alterCompanyItem.setIswebsiteopen(iswebsiteopen);
							}
						}
						saveAlterData();
					}
				}
			}
			if (requestCode == CityChooseActity.CHOOSE_FOUR) {// 设置企业所在地的回传
				if (resultCode == CityChooseActity.CHOOSE_FOUR) {
					Bundle bundle=data.getExtras();
					if (bundle != null) {
						String cityId = bundle.getString("id");
						String currentPosition=bundle.getString("currentPosition");
						alterCompanyItem = newCompanyList.get(Integer.valueOf(currentPosition));
						if (StringUtil.isEmpty(cityId) ) {
							ShowUtils.showMsg(context, "职位不能为空!");
						}else {
							if (cityId.equals(alterCompanyItem.getCity())) {
							}else {
								String newCompanyCity = bundle.getString("text");
								alterCompanyItem.setCity(cityId);
								alterCompanyItem.setCityName(newCompanyCity);
								saveAlterData();
							}
						}
					}
				}
			}
		}
	}
	/**
	 * 
	 */
	private void saveAlterData() {
		if (alterCompanyItem.getId().contains("#")) {//新数据多次修改
			addCompanyList.remove((int)addIdPositionList.get(alterCompanyItem.getId()));
			addCompanyList.add(addIdPositionList.get(alterCompanyItem.getId()), alterCompanyItem);
		}else {
			if (alterIdPositionList.containsKey(alterCompanyItem.getId())) {//旧数据多次修改
				alterCompanyList.remove((int)alterIdPositionList.get(alterCompanyItem.getId()));
				alterCompanyList.add(alterIdPositionList.get(alterCompanyItem.getId()), alterCompanyItem);
			}else {//旧数据首次修改
				alterIdPositionList.put(alterCompanyItem.getId(), alterIdPositionList.size());
				alterCompanyList.add(alterCompanyItem);
			}
		}
		alterCompanyItem = null;
		notifyData();
		if (!isChange)
			isChange = true;
	}
	
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (isChange) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetCompanyActivity.this.finish();
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
}
