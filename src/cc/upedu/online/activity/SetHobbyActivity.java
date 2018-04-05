package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.HobbyAdapter;
import cc.upedu.online.base.TwoPartModelGridViewBottomBaseActivity;
import cc.upedu.online.domin.HobbyListBean.HobbyItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
/**
 * 设置我的爱好
 * @author Administrator
 *
 */
public class SetHobbyActivity extends TwoPartModelGridViewBottomBaseActivity {
	public static final int RESULT_SETHOBBY = 12;
	private static final int REQUEST_SETMYHOBBY = 25;
	private List<HobbyItem> hobbyList;
	private List<String> mHobbyIdList;
	private List<String> mHobbyList;
	private TextView tv_myhobby;
	private Map<String, String> idTextMap;
	private Button bt_addhobby;
	private String userHobby = "";

	@Override
	protected void initData() {
		hobbyList = (List<HobbyItem>) PreferencesObjectUtil.readObject("hobbyList", context);
		idTextMap = new HashMap<String, String>();
		if (hobbyList != null) {
			for (HobbyItem hobbyItem : hobbyList) {
				idTextMap.put(hobbyItem.getId(), hobbyItem.getContent());
			}
		}
		String hobbyId = getIntent().getStringExtra("hobbyId");
		mHobbyIdList = new ArrayList<String>();
		mHobbyList = new ArrayList<String>();
		if (!StringUtil.isEmpty(hobbyId)) {
			if (hobbyId.contains(",")) {
				String[] mHobbyIds = hobbyId.split(",");
				List<String> asList = Arrays.asList(mHobbyIds);
				mHobbyIdList.addAll(asList);
				for (String str : mHobbyIdList) {
					if (str.contains("otherhobby")) {
						userHobby = str.substring(str.indexOf("otherhobby")+"otherhobby".length(), str.length());
						mHobbyIdList.remove(str);
					}else {
						mHobbyList.add(idTextMap.get(str));
					}
				}
			}else {
				if (hobbyId.contains("otherhobby")) {
					userHobby = hobbyId.substring(hobbyId.indexOf("otherhobby")+"otherhobby".length(), hobbyId.length());
				}else {
					mHobbyList.add(idTextMap.get(hobbyId));
					mHobbyIdList.add(hobbyId);
				}
			}
		}

		if (!StringUtil.isEmpty(userHobby)) {
			tv_myhobby.setText(userHobby);
		}
		if (isAdapterEmpty()) {
			setNumColumns(4);
			setGridView(new HobbyAdapter(context,hobbyList,mHobbyIdList));
			setOnItemClickListion(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (mHobbyIdList.contains(hobbyList.get(position).getId())) {
						mHobbyIdList.remove(hobbyList.get(position).getId());
						mHobbyList.remove(hobbyList.get(position).getContent());
					}else {
						mHobbyIdList.add(hobbyList.get(position).getId());
						mHobbyList.add(hobbyList.get(position).getContent());
					}
					notifyData();
					if (!isChange)
						isChange = true;
				}
			});
		}else {
			notifyData();
		}
	}
	@Override
	public View initBottomLayout() {
		View view = View.inflate(context, R.layout.layout_sethobby_bottom, null);
		bt_addhobby = (Button) view.findViewById(R.id.bt_addhobby);
		tv_myhobby = (TextView) view.findViewById(R.id.tv_myhobby);
		return view;
	}

	@Override
	protected void initTitle() {
		setTitleText("我的爱好");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				String newHobbyId = "";
				String newHobby = "";
				String myHobby = userHobby;
				if (mHobbyIdList.size() > 0) {
					for (String mHobbyId : mHobbyIdList) {
						newHobbyId += (mHobbyId + ",");
					}
					for (String mHobby : mHobbyList) {
						newHobby += (mHobby + "/");
					}
					if (StringUtil.isEmpty(myHobby)) {
						newHobbyId = newHobbyId.substring(0, newHobbyId.length() - 1);
						newHobby = newHobby.substring(0, newHobby.length() - 1);
					} else {
						myHobby = myHobby.replace(",", " ").replace("，", " ");
						newHobbyId += ("otherhobby" + myHobby);
						newHobby += myHobby;
					}
				} else {
					if (!StringUtil.isEmpty(myHobby)) {
						newHobbyId += ("otherhobby" + myHobby);
						newHobby += myHobby;
					}
				}
				Intent intent = new Intent();
				intent.putExtra("hobbyId", newHobbyId);
				intent.putExtra("hobby", newHobby);
				setResult(RESULT_SETHOBBY, intent);
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
							SetHobbyActivity.this.finish();
						}
					});
				else
					SetHobbyActivity.this.finish();
			}
		});
	}
	private boolean isChange = false;
	@Override
	protected void initListener() {
		super.initListener();
		bt_addhobby.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.bt_addhobby:
			Intent intent = new Intent(context,AddHobbyActivity.class);
			intent.putExtra("hobbyItem", userHobby);
			startActivityForResult(intent, REQUEST_SETMYHOBBY);
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode==REQUEST_SETMYHOBBY){//设置的数据回传
				if (resultCode==AddHobbyActivity.RESULT_SETMYHOBBY){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						String newHobbyItem = bundle.getString("newHobbyItem");
						if (StringUtil.isEmpty(newHobbyItem)) {
							if (!StringUtil.isEmpty(userHobby)) {
								userHobby = "";
								tv_myhobby.setText(userHobby);
								if (!isChange)
									isChange = true;
							}
						}else {
							if (!newHobbyItem.equals(userHobby)) {
								userHobby = newHobbyItem;
								tv_myhobby.setText(userHobby);
								if (!isChange)
									isChange = true;
							}
						}
					}
				}
			}
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	if (mHobbyIdList.size() < 1 && StringUtil.isEmpty(userHobby)) {
        		SetHobbyActivity.this.finish();
			}else {
				if (isChange) {
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetHobbyActivity.this.finish();
						}
					});
				}else {
					return super.onKeyDown(keyCode, event);
				}
			}
			return false;
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
}
