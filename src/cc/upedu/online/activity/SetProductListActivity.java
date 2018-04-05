package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.adapter.ProductListAdapter;
import cc.upedu.online.base.DelBaseBackCall;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
/**
 * 设置主营产品的界面
 * @author Administrator
 *
 */
public class SetProductListActivity extends ListBaseActivity<ProductItem> {
	public static final int RESULT_SETPRODUCTLIST = 18;
	protected static final int REQUEST_SETPRODUCTITEM = 21;
	private List<ProductItem> productList;
	private List<ProductItem> newProductList;
	private List<ProductItem> alterProductList;
	private List<ProductItem> addProductList;
	private List<String> delProductIdList;
	private HashMap<String, Integer> alterIdPositionList,addIdPositionList;
	private boolean isNewProduct = false;
	private String currentPosition;

	@Override
	protected void initData() {
		if (newProductList == null) {
			newProductList = new ArrayList<ProductItem>();
		}else {
			newProductList.clear();
		}
		if (productList != null && productList.size() > 0) {
			newProductList.addAll(productList);
		}
		if (delProductIdList == null) {
			delProductIdList = new ArrayList<String>();
		}else {
			if (delProductIdList.size() > 0) {
				for (int i = 0; i < delProductIdList.size(); i++) {
					for (int j = 0; j < newProductList.size(); j++) {
						if (delProductIdList.get(i).equals(newProductList.get(j).getId())) {
							
							newProductList.remove(j);
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
		if (alterProductList == null) {
			alterProductList = new ArrayList<ProductItem>();
		}else {
			for (int i = 0; i < alterProductList.size(); i++) {
				alterIdPositionList.put(alterProductList.get(i).getId(), i);
				for (int j = 0; j < newProductList.size(); j++) {
					if (alterProductList.get(i).getId().equals(newProductList.get(j).getId())) {
						newProductList.set(j, alterProductList.get(i));
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
		if (addProductList == null) {
			addProductList = new ArrayList<ProductItem>();
		}else {
			if (addProductList.size() > 0) {
				newProductList.addAll(addProductList);
				for (int i = 0; i < addProductList.size(); i++) {
					addIdPositionList.put(addProductList.get(i).getId(), i);
				}
			}
		}
		
		if (isAdapterEmpty()) {
			setListView(new ProductListAdapter(context,newProductList,new DelBaseBackCall() {
				
				@Override
				public void delBackCall(int position) {
					//删除功能
					if (newProductList.get(position).getId().contains("#")) {
						addProductList.remove(addIdPositionList.get(newProductList.get(position).getId()));
					}else if (alterIdPositionList.containsKey(newProductList.get(position).getId())) {
						alterProductList.remove(alterIdPositionList.get(newProductList.get(position).getId()));
						delProductIdList.add(newProductList.get(position).getId());
					}else {
						delProductIdList.add(newProductList.get(position).getId());
					}
					newProductList.remove(position);
					notifyData();
				}
			}));
			setOnItemClickListion(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					isNewProduct = false;
					Intent intent = new Intent(context,AddProductActivity.class);
					intent.putExtra("productItem", (Serializable)newProductList.get(position));
					startActivityForResult(intent, REQUEST_SETPRODUCTITEM);
				}
			});
		}else {
			notifyData();
		}
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("所有产品");
		setLeftButton(R.drawable.back, "保存",new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("alartProductList", (Serializable)alterProductList);
				intent.putExtra("addProductList", (Serializable)addProductList);
				intent.putExtra("delProductIdList", (Serializable)delProductIdList);
				intent.putExtra("currentPosition", currentPosition);
				setResult(RESULT_SETPRODUCTLIST, intent);
				finish();
			}
		});
		setRightText("添加", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isNewProduct = true;
				Intent intent = new Intent(context,AddProductActivity.class);
				startActivityForResult(intent, REQUEST_SETPRODUCTITEM);
			}
		});
		
		productList = (List<ProductItem>)getIntent().getSerializableExtra("productList");
		alterProductList = (List<ProductItem>)getIntent().getSerializableExtra("alterProductList");
		addProductList = (List<ProductItem>)getIntent().getSerializableExtra("addProductList");
		delProductIdList = (List<String>)getIntent().getSerializableExtra("delProductIdList");
		currentPosition = getIntent().getStringExtra("currentPosition");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode==REQUEST_SETPRODUCTITEM){//修改产品的数据回传
				if (resultCode==AddProductActivity.RESULT_SETOLDPRODUCTCITY){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						if (isNewProduct) {//新数据首次修改
							isNewProduct = false;
							ProductItem newProductItem = (ProductItem) bundle.getSerializable("newProductItem");
							if (addProductList == null) {
								addProductList = new ArrayList<ProductItem>();
							}
							addProductList.add(newProductItem);
							newProductList.add(newProductItem);
							if (addIdPositionList == null) {
								addIdPositionList = new HashMap<String, Integer>();
							}
							addIdPositionList.put(newProductItem.getId(), addIdPositionList.size());
						}else {//修改旧数据和新数据多次修改
							ProductItem alterProductItem = (ProductItem) bundle.getSerializable("alterProductItem");
							if (addProductList == null) {
								addProductList = new ArrayList<ProductItem>();
							}
							if (alterProductItem.getId().contains("#")) {//新数据多次修改
								addProductList.remove((int)addIdPositionList.get(alterProductItem.getId()));
								addProductList.add(addIdPositionList.get(alterProductItem.getId()), alterProductItem);
							}else {
								if (alterIdPositionList.containsKey(alterProductItem.getId())) {//旧数据多次修改
									alterProductList.remove((int)alterIdPositionList.get(alterProductItem.getId()));
									alterProductList.add(alterIdPositionList.get(alterProductItem.getId()), alterProductItem);
								}else {//旧数据首次修改
									if (alterIdPositionList == null) {
										alterIdPositionList = new HashMap<String, Integer>();
									}
									alterIdPositionList.put(alterProductItem.getId(), alterIdPositionList.size());
									alterProductList.add(alterProductItem);
								}
							}
						}
						initData();
					}
				}
			}
		}
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					SetProductListActivity.this.finish();
				}
			});
			return false;
	    }else {
	    	return super.onKeyDown(keyCode, event); 
		}
	}
}
