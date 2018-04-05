package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.ThreePartModelListBaseActivity;
import cc.upedu.online.domin.ShoppingBean;
import cc.upedu.online.domin.ShoppingBean.Entity.ShoppingItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;

/**
 * 我的购物车界面
 * 
 * @author Administrator
 * 
 */
public class MyShoppingTrolleyActivity extends ThreePartModelListBaseActivity<ShoppingItem> {
	private ShoppingBean mShoppingBean;
	private List<ShoppingItem> mShoppingList;// 课程条目list集合

	String userId;// 用户ID
	private CheckBox cb_choose_scope;
	private TextView tv_choose_scope;
	private TextView tv_total;
	private LinearLayout ll_settlement;
	private TextView tv_settlement;
	private int currentNumber = 0;
	private Double currentTotalPrices = 0.00d;
	private LinearLayout ll_choose_scope;
	private boolean isChooseAll = false;
	private List<String> chooseIndexs = new ArrayList<String>();
	//是否正在管理内容
	private boolean isManaging = false;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("购物车");
		setRightText("编辑", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//处理编辑界面的点击事件
				if (mShoppingList != null && mShoppingList.size() > 0) {
					if (!isManaging) {
						setRightText("完成",null);
						isManaging = !isManaging;
						getAdapter().notifyDataSetChanged();
						setBottomLayoutVisibility(View.GONE);
					}else {
						exitManaging();
					}
				}else {
					ShowUtils.showMsg(context, "空购物车不需要管理!");
				}
			}
		});
	}
	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_shoppingtrolley_top, null);
		return view;
	}
	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_shoppingtrolley_bottom, null);
		ll_choose_scope = (LinearLayout) view.findViewById(R.id.ll_choose_scope);
		cb_choose_scope = (CheckBox) view.findViewById(R.id.cb_choose_scope);
		tv_choose_scope = (TextView) view.findViewById(R.id.tv_choose_scope);
		tv_total = (TextView) view.findViewById(R.id.tv_total);
		ll_settlement = (LinearLayout) view.findViewById(R.id.ll_settlement);
		tv_settlement = (TextView) view.findViewById(R.id.tv_settlement);
		
		cb_choose_scope.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				//判断是否是在管理购物车,只有在非管理状态下才能全选/反选
				if (mShoppingList != null && mShoppingList.size() > 0) {
					if (isChecked) {//根据单选框的选择状态进行不同的操作
						tv_choose_scope.setTextColor(Color.RED);
						tv_choose_scope.setText("反选");
						isChooseAll = true;
						currentNumber = mShoppingList.size();
						currentTotalPrices =  0.00d;
						for (ShoppingItem mShoppingItem : mShoppingList) {
							currentTotalPrices += Double.valueOf(mShoppingItem.getPrice());
						}
						chooseIndexs.clear();
						for (int i = 0; i < mShoppingList.size(); i++) {
							chooseIndexs.add(String.valueOf(i));
						}
						getAdapter().notifyDataSetChanged();
						changeSettlement();
					}else {
						tv_choose_scope.setTextColor(Color.WHITE);
						tv_choose_scope.setText("全选");
						if (isChooseAll) {
							isChooseAll = false;
							currentNumber = 0;
							currentTotalPrices = 0.00d;
							chooseIndexs.clear();
							getAdapter().notifyDataSetChanged();
							changeSettlement();
						}
					}
				}else {
					ShowUtils.showMsg(context, "空购物车不能进行选择!");
				}
			}
		});
		changeSettlement();
		return view;
	}

	/**
	 * 根据用户的选择来显示客户选择的商品个数和商品总价
	 */
	private void changeSettlement() {
		tv_total.setText(String.valueOf(new BigDecimal(currentTotalPrices).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()));
		tv_settlement.setText("去结算("+String.valueOf(currentNumber)+")");
	}

	@Override
	protected void initData() {
		userId = UserStateUtil.getUserId();
		Map<String, String> requestDataMap;
		if (StringUtil.isEmpty(SharedPreferencesUtil.getInstance().spGetString("ShoopingCourseId"))) {
			requestDataMap = ParamsMapUtil.getMyShopping(context, String.valueOf(userId));
		}else {
			requestDataMap = ParamsMapUtil.getMyShopping(context, String.valueOf(userId),SharedPreferencesUtil.getInstance().spGetString("ShoopingCourseId"));
		}
		
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_SHOPPING, context,
				requestDataMap, new MyBaseParser<>(ShoppingBean.class));
		DataCallBack<ShoppingBean> shoppingDataCallBack = new DataCallBack<ShoppingBean>() {

			@Override
			public void processData(final ShoppingBean object) {
				if (object == null) {
					ShowUtils.showMsg(context, "获取购物车数据失败请联系客服");
				} else {
					mShoppingBean = object;
					handler.obtainMessage(0).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, shoppingDataCallBack);
	}
	private boolean isFistInitdata = true;
	@Override
	protected void onResume() {
		if (!isFistInitdata) {
			initData();
		}else {
			isFistInitdata = false;
		}
		super.onResume();
	}
	private class ShoppingAdapter extends BaseMyAdapter<ShoppingItem>{

		public ShoppingAdapter(Context context, List<ShoppingItem> list) {
			super(context, list);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			final ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(context, R.layout.layout_shopping_item, null);
				holder = new ViewHolder();
				holder.fl_itemlist = (FrameLayout) view.findViewById(R.id.fl_itemlist);
				holder.cb_choose = (CheckBox) view.findViewById(R.id.cb_choose);
				holder.iv_delete = (ImageView) view.findViewById(R.id.iv_delete);
				holder.iv_courseimage = (ImageView) view.findViewById(R.id.iv_courseimage);
				holder.course_title = (TextView) view.findViewById(R.id.course_title);
				holder.tv_price = (TextView) view.findViewById(R.id.tv_price);
				view.setTag(holder);
			}else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			if (isChooseAll) {
				holder.cb_choose.setChecked(true);
			}else {
				if (chooseIndexs.contains(String.valueOf(position))) {
					holder.cb_choose.setChecked(true);
				}else {
					holder.cb_choose.setChecked(false);
				}
			}
			
			ImageUtils.setImage(list.get(position).getPic(), holder.iv_courseimage, 0);		
			
			holder.course_title.setText(list.get(position).getTitle());
			holder.tv_price.setText(list.get(position).getPrice());
			if (isManaging) {
				holder.iv_delete.setVisibility(View.VISIBLE);
				holder.cb_choose.setVisibility(View.GONE);
				holder.fl_itemlist.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if (isManaging) {
							ShowUtils.showDiaLog(context, "温馨提醒", "确定要删除该课程("+mShoppingList.get(position).getTitle()+")吗?", new ConfirmBackCall() {
								
								@Override
								public void confirmOperation() {
									Map<String, String> map = new HashMap<String, String>();
									map.put("itemId", list.get(position).getItemId());
									map.put("userId", list.get(position).getUserId());
									UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.DEL_SHOPPINGITEM,map , new UploadDataCallBack() {
										
										@Override
										public void onUploadDataSuccess() {
											// TODO Auto-generated method stub
											boolean ischoosed = chooseIndexs.contains(String.valueOf(position));
											if (ischoosed) {//如果删除的是选中的课程:要把总钱数减去该课程的钱,及把选中课程数减一
												currentTotalPrices -= Double.valueOf(mShoppingList.get(position).getPrice());
												currentNumber--;
												chooseIndexs.remove(String.valueOf(position));
												mShoppingList.remove(position);
												changeSettlement();
											}else {//如果删除的是未选中的课程:需要判断剩下的课程是否全部都是选中的课程,若是将下方的全选状态改为反选状态
												mShoppingList.remove(position);
												if (currentNumber != 0 && currentNumber == mShoppingList.size()) {
													tv_choose_scope.setTextColor(Color.RED);
													tv_choose_scope.setText("反选");
													isChooseAll = true;
													cb_choose_scope.setChecked(true);
												}
											}
											ShowUtils.showMsg(context, "删除课程成功");
											getAdapter().notifyDataSetChanged();
										}
										
										@Override
										public void onUploadDataFailure() {
											// TODO Auto-generated method stub
											
										}
									});
								}
							});
						}else {
							if (chooseIndexs.contains(String.valueOf(position))) {
								holder.cb_choose.setChecked(true);
							}else {
								holder.cb_choose.setChecked(false);
							}
						}
					}
				});
			}else {
				holder.iv_delete.setVisibility(View.GONE);
				holder.cb_choose.setVisibility(View.VISIBLE);
				holder.cb_choose.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (!isManaging) {
							if (isChecked) {
								if (!isChooseAll) {
									currentNumber++;
									currentTotalPrices += Double.valueOf(list.get(position).getPrice());
									chooseIndexs.add(String.valueOf(position));
								}
								if (chooseIndexs.size() == list.size()) {
									tv_choose_scope.setTextColor(Color.RED);
									tv_choose_scope.setText("反选");
									cb_choose_scope.setChecked(true);
									isChooseAll = true;
//								mShoppingAdapter.notifyDataSetChanged();
								}
							}else {
								if (currentNumber != 0) {
									currentNumber--;
									BigDecimal f1 = new BigDecimal(currentTotalPrices);
									BigDecimal f2 = new BigDecimal(Double.valueOf(list.get(position).getPrice()));
									currentTotalPrices = f1.subtract(f2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									chooseIndexs.remove(String.valueOf(position));
								}
								if (isChooseAll) {
									isChooseAll = false;
									cb_choose_scope.setChecked(false);
//								mShoppingAdapter.notifyDataSetChanged();
								}
							}
							changeSettlement();
						}
					}
				});
			}
			return view;
		}

		private class ViewHolder{
			FrameLayout fl_itemlist;
			ImageView iv_delete;
			CheckBox cb_choose;
			ImageView iv_courseimage;
			TextView course_title;
			TextView tv_price;
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		ll_choose_scope.setOnClickListener(this);
		ll_settlement.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_settlement://去结算
			if (mShoppingList != null && mShoppingList.size() > 0) {
				//先判断是否在管理购物车的状态
				if (chooseIndexs.size() > 0) {
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
							Intent intent = new Intent(context, PaymentActivity.class);
							List<ShoppingItem> shoppingList = new ArrayList<ShoppingBean.Entity.ShoppingItem>();
							for (int i = 0; i < chooseIndexs.size(); i++) {
								shoppingList.add(mShoppingList.get(Integer.valueOf(chooseIndexs.get(i))));
							}
							intent.putExtra("shoppingList", (Serializable)shoppingList);
							intent.putExtra("totalGcoin", totalGcoin);
							intent.putExtra("totalPrices", String.valueOf(currentTotalPrices));
							startActivity(intent);
						}
					});
				}else {
					ShowUtils.showMsg(context, "您还没有选择要结算的商品!");
				}
			}else {
				ShowUtils.showMsg(context, "您的购物车为空,不能去结算!");
			}
			break;
			
		case R.id.ll_choose_scope:
			if (isChooseAll) {
				cb_choose_scope.setChecked(false);
			}else {
				cb_choose_scope.setChecked(true);
			}
			break;
		}
	}

	/**
	 * 
	 */
	private void exitManaging() {
		setRightText("管理", null);
		isManaging = !isManaging;
		currentNumber = 0;
		currentTotalPrices = 0.00d;
		chooseIndexs.clear();
		if (isChooseAll) {
			tv_choose_scope.setTextColor(Color.WHITE);
			tv_choose_scope.setText("全选");
			isChooseAll = false;
		}
		changeSettlement();
		getAdapter().notifyDataSetChanged();
		setBottomLayoutVisibility(View.VISIBLE);
	}
	/**
	 * 
	 */
	private void setData() {
		if (isAdapterEmpty()) {
			setListView(new ShoppingAdapter(context, mShoppingList),R.drawable.noshopping);
			setOnItemClickListion(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if ("LIVE".equals( mShoppingList.get(position).getCourseType())) {
						Intent intent = new Intent(context, TelecastApplayActivity.class);
						intent.putExtra("courseId", mShoppingList.get(position).getGoodsid());
						context.startActivity(intent);
					}else {
						Intent intent = new Intent(context, CourseIntroduceActivity.class);
						intent.putExtra("courseId",mShoppingList.get(position).getGoodsid());
						context.startActivity(intent);
					}
				}
			});
		}else {
			notifyData();
		}
	};
	//成长币数量
	private String totalGcoin = "0";
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (Boolean.valueOf(mShoppingBean.getSuccess())) {
					mShoppingList = mShoppingBean.getEntity().getShopcartList();
					if (!StringUtil.isEmpty(mShoppingBean.getEntity().getTotalGcoin())) {
						totalGcoin = mShoppingBean.getEntity().getTotalGcoin();
					}
				}else {
					ShowUtils.showMsg(context, mShoppingBean.getMessage());
				}
				setData();
				break;
			}
		}
	};
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (isManaging) {
        		exitManaging();
        		return false;
			}else {
	        	return super.onKeyDown(keyCode, event); 
			}
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
}
