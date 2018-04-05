package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.TwoPartModelTopBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;
import cc.upedu.online.view.factory.MyHorizontalIconTextItem;
/**
 * 我的位置
 * @author Administrator
 *
 */
public class SetAdressActivity extends TwoPartModelTopBaseActivity {
	public static final int RESULT_SETADRESS = 10;
	private LinearLayout ll_setcurrentcity;
	private TextView tv_setcurrentcity;
	private LinearLayout ll_sethometown;
	private TextView tv_sethometown;
	private RelativeLayout ll_settravelcity;
	private TextView tv_settravelcity;
	private ListView lv_settravelcity;
	private String oldCity;
	private String newCity = "";
	private String cityId = "0";
	private String oldHometown;
	private String newHometown = "";
	private String hometownId = "0";
	private String oldTravelIdcity;
	private String oldTravelcityText;
//	private String newTravelcity = "";
//	private String travelcityId = "0";
	private TravelcityAdapter travelcityAdapter;
	private List<String> travelcityList;
	private List<String> travelcityIdList;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}
	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		MyHorizontalIconTextItem topview = new MyHorizontalIconTextItem(MyHorizontalIconTextItem.TEXT);
		View view = topview.initView(context);
		topview.setBackgroundColor(getResources().getColor(R.color.backGrond));
		topview.setText(R.string.remarks_city);
		return view;
	}
	@Override
	public View initTwelfthLayout() {
		// TODO Auto-generated method stub
		oldCity = getIntent().getStringExtra("city");
		oldHometown = getIntent().getStringExtra("hometown");
		oldTravelIdcity = getIntent().getStringExtra("travelcity");
		oldTravelcityText = getIntent().getStringExtra("travelcityText");
		travelcityList = new ArrayList<String>();
		travelcityIdList = new ArrayList<String>();
		if (!StringUtil.isEmpty(oldTravelIdcity)) {
			if (oldTravelIdcity.contains(",")) {
				String[] oldTravelcityIdStrs = oldTravelIdcity.split(",");
				for (String oldTravelcityIdStr : oldTravelcityIdStrs) {
					travelcityIdList.add(oldTravelcityIdStr);
				}
				String[] oldTravelcityStrs = oldTravelcityText.split(",");
				for (String oldTravelcityStr : oldTravelcityStrs) {
					travelcityList.add(oldTravelcityStr);
				}
			}else {
				travelcityList.add(oldTravelcityText);
				travelcityIdList.add(oldTravelIdcity);
				
			}
		}
		
		View view = View.inflate(context, R.layout.activity_setadress, null);
		ll_setcurrentcity = (LinearLayout) view.findViewById(R.id.ll_setcurrentcity);
		tv_setcurrentcity = (TextView) view.findViewById(R.id.tv_setcurrentcity);
		if (!StringUtil.isEmpty(oldCity)) {
			tv_setcurrentcity.setText(oldCity);
			newCity = oldCity;
		}else {
			tv_setcurrentcity.setText("未设置");
		}
		ll_sethometown = (LinearLayout) view.findViewById(R.id.ll_sethometown);
		tv_sethometown = (TextView) view.findViewById(R.id.tv_sethometown);
		if (!StringUtil.isEmpty(oldHometown)) {
			tv_sethometown.setText(oldHometown);
			newHometown = oldHometown;
		}else {
			tv_sethometown.setText("未设置");
		}
		ll_settravelcity = (RelativeLayout) view.findViewById(R.id.ll_settravelcity);
		tv_settravelcity = (TextView) view.findViewById(R.id.tv_settravelcity);
		lv_settravelcity = (ListView) view.findViewById(R.id.lv_settravelcity);
		
		setTravelcityAdapter(travelcityList, lv_settravelcity);
		return view;
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的位置");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("city", newCity);
				intent.putExtra("cityId", cityId);
				intent.putExtra("hometown", newHometown);
				intent.putExtra("hometownId", hometownId);
				String newTravelcity = "";
				for (String travelcity : travelcityList) {
					newTravelcity += (travelcity + ",");
				}
				if (newTravelcity.contains(",")) {
					newTravelcity = newTravelcity.substring(0, newTravelcity.length() - 1);
				}
				String newTravelcityId = "";
				for (String travelcityId : travelcityIdList) {
					newTravelcityId += (travelcityId + ",");
				}
				if (newTravelcityId.contains(",")) {
					newTravelcityId = newTravelcityId.substring(0, newTravelcityId.length() - 1);
				}
				intent.putExtra("travelcity", newTravelcity);
				intent.putExtra("travelcityId", newTravelcityId);
				setResult(RESULT_SETADRESS, intent);
				finish();
			}
		});

		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (isChange)
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetAdressActivity.this.finish();
						}
					});
				else
					SetAdressActivity.this.finish();
			}
		});
	}

	@Override
	protected void initListener() {
		super.initListener();
		ll_setcurrentcity.setOnClickListener(this);
		ll_sethometown.setOnClickListener(this);
		ll_settravelcity.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_setcurrentcity:
			intent = new Intent(context, CityChooseActity.class);
			intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_ONE);
			startActivityForResult(intent, CityChooseActity.CHOOSE_ONE);
			break;
		case R.id.ll_sethometown:
			intent = new Intent(context, CityChooseActity.class);
			intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_TWO);
			startActivityForResult(intent, CityChooseActity.CHOOSE_TWO);
			break;
		case R.id.ll_settravelcity:
			if (travelcityList.size() < 10) {
				intent = new Intent(context, CityChooseActity.class);
				intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_THREE);
				startActivityForResult(intent, CityChooseActity.CHOOSE_THREE);
			}else {
				ShowUtils.showMsg(context, "您最多只能添加10个往来城市!");
			}
			break;
		}
	}
	private boolean isChange = false;
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				String text = bundle.getString("text");
				String id = bundle.getString("id");
				if (requestCode == CityChooseActity.CHOOSE_ONE) {// 设置所在
					if (resultCode == CityChooseActity.CHOOSE_ONE) {
						newCity = text;
						cityId = String.valueOf(id);
						tv_setcurrentcity.setText(newCity);
						if(!newCity.equals(oldCity) && !isChange)
							isChange = true;
					}
				}
				if (requestCode == CityChooseActity.CHOOSE_TWO) {// 设置家乡
					if (resultCode == CityChooseActity.CHOOSE_TWO) {
						newHometown = text;
						hometownId = String.valueOf(id);
						tv_sethometown.setText(newHometown);
						if(!newHometown.equals(newHometown) && !isChange)
							isChange = true;
					}
				}
				if (requestCode == CityChooseActity.CHOOSE_THREE) {// 设置往来
					if (resultCode == CityChooseActity.CHOOSE_THREE) {
						if (!travelcityIdList.contains(id)) {
							travelcityList.add(text);
							travelcityIdList.add(id);
							setTravelcityAdapter(travelcityList, lv_settravelcity);
							CommonUtil.setListViewHeightBasedOnChildren(lv_settravelcity);
							if (!isChange)
								isChange = true;
						}else {
							ShowUtils.showMsg(context, "您已经添加过该城市");
						}
					}
				}
			}
		}
	}

	private void setTravelcityAdapter(List<String> list,ListView listView) {
		if (list.size() > 0) {
			tv_settravelcity.setVisibility(View.GONE);
			lv_settravelcity.setVisibility(View.VISIBLE);
			if (travelcityAdapter == null) {
				travelcityAdapter = new TravelcityAdapter(context, list);
				listView.setAdapter(travelcityAdapter);
			}else {
				travelcityAdapter.notifyDataSetChanged();
			}
			CommonUtil.setListViewHeightBasedOnChildren(listView);
		} else {
			lv_settravelcity.setVisibility(View.GONE);
			tv_settravelcity.setVisibility(View.VISIBLE);
			tv_settravelcity.setText("未设置");
		}
	}
	private class TravelcityAdapter extends BaseMyAdapter<String>{

		public TravelcityAdapter(Context context, List<String> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(context, R.layout.layout_showuser_item, null);
				holder = new ViewHolder();
				holder.tv_show_item = (TextView) view.findViewById(R.id.tv_show_item);
				holder.ib_del = (ImageView) view.findViewById(R.id.ib_del);
//				holder.line = (View) view.findViewById(R.id.line);
				view.setTag(holder);
			}else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_show_item.setText(list.get(position));
			holder.ib_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					travelcityList.remove(position);
					travelcityIdList.remove(position);
					if (travelcityAdapter != null) {
						travelcityAdapter.notifyDataSetChanged();
						CommonUtil.setListViewHeightBasedOnChildren(lv_settravelcity);
					}
				}
			});
			return view;
		}
	}
	private class ViewHolder{
		TextView tv_show_item;
		ImageView ib_del;
//		View line;
	}

	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
        		@Override
        		public void confirmOperation() {
        			SetAdressActivity.this.finish();
        		}
        	});
			return false;
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
}
