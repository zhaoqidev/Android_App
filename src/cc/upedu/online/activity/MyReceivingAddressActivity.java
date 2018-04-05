package cc.upedu.online.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.adapter.MyReceivingAddressAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.MyReceivingAddressBean;
import cc.upedu.online.domin.MyReceivingAddressBean.AddressItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 我的收货地址
 * 
 * @author Administrator
 * 
 */
public class MyReceivingAddressActivity extends ListBaseActivity<AddressItem> {
	public static final int RESULT_CHOOSEADDRESS = 61;
	// 是否正在管理内容
	String userId;// 用户ID
	private MyRecive myRecive;
	private boolean hasResultBack;
	private RequestVo requestVo;
	private List<AddressItem> addressList;
	private DataCallBack<MyReceivingAddressBean> dataCallBack;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的收货地址");
		setRightText("添加", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, MicroMallAddressActivity.class);
				startActivity(intent);
			}
		});
		
		hasResultBack = getIntent().getBooleanExtra("hasResultBack", false);
		userId = UserStateUtil.getUserId();// 获取用户ID
		myRecive = new MyRecive();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("AddressManaging");
		context.registerReceiver(myRecive, intentFilter);
	}
	@Override
	protected void onResume() {
		getDataServer(requestVo, dataCallBack);
		super.onResume();
	}

	@Override
	protected void initData() {
		Map<String, String> requestDataMap = ParamsMapUtil.getMyReceivingAddress(context, userId);

		requestVo = new RequestVo(ConstantsOnline.MY_RECEIVE_ADDRESS, context,
				requestDataMap,
				new MyBaseParser<>(MyReceivingAddressBean.class));
		dataCallBack = new DataCallBack<MyReceivingAddressBean>() {

			@Override
			public void processData(final MyReceivingAddressBean object) {
				if (object != null) {
					if ("true".equals(object.success)) {
						if (addressList == null) {
							addressList = new ArrayList<>();
						}else {
							addressList.clear();
						}
						if (object.entity != null && object.entity.addressList != null) {
							addressList.addAll(object.entity.addressList);
						}
						if (isAdapterEmpty()) {
							setListView(new MyReceivingAddressAdapter(context, addressList));
							if (hasResultBack) {
								setOnItemClickListion(new OnItemClickListener() {

									@Override
									public void onItemClick(AdapterView<?> parent,
											View view, int position, long id) {
										// TODO Auto-generated method stub
										Intent intent = new Intent();
										intent.putExtra("addressItem", (Serializable)object.entity.addressList.get(position));
										setResult(RESULT_CHOOSEADDRESS, intent);
										MyReceivingAddressActivity.this.finish();
									}
								});
							}
						}else {
							notifyData();
						}
					} else {
						ShowUtils.showMsg(context, object.message);
					}
				} else {
					ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
				}

			}
		};

	}
	
	/**
	 * 自定义的广播接受者,用于在用户清空收藏时管理功能的自动切换
	 * @author Administrator
	 *
	 */
	class MyRecive extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			getDataServer(requestVo, dataCallBack);

		}
	}
	
	@Override
	protected void onDestroy() {
		context.unregisterReceiver(myRecive);
		super.onDestroy();
	}

}
