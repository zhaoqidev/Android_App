package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.MicroMallAddressActivity;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.domin.MyReceivingAddressBean.AddressItem;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 我的收货地址的adapter
 * @author Administrator
 *
 */
public class MyReceivingAddressAdapter extends BaseMyAdapter<AddressItem> {
	private String userId;
	
	final int DEFUALT=1;//设置为默认收货地址
	final int DELETE=2;//删除收货地址
	
	
	public MyReceivingAddressAdapter(Context context, List<AddressItem> list) {
		super(context, list);
		userId = UserStateUtil.getUserId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder holder;
		//复用
		if (convertView == null) {
			view = View.inflate(context, R.layout.activity_micromall_address_item, null);
			holder = new ViewHolder();
			holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			holder.tv_phone = (TextView) view.findViewById(R.id.tv_phone);
			holder.tv_address = (TextView) view.findViewById(R.id.tv_address);
			holder.tv_edit = (TextView) view.findViewById(R.id.tv_edit);
			holder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
			holder.select_default = (Button) view.findViewById(R.id.select_default);
			view.setTag(holder);
		}else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
//		holder.select_default.setTag(1000+position);
		holder.tv_name.setText(list.get(position).personName);
		holder.tv_phone.setText(list.get(position).mobile);
		holder.tv_address.setText(list.get(position).provinceText+list.get(position).cityText+list.get(position).areaText+list.get(position).address);
		if ("Y".equals(list.get(position).isdefault)) {
			Log.i("address", "getView + " + position+ "   id:"+ list.get(position).addressId);
			holder.select_default.setBackgroundResource(R.drawable.checkbox_true);
		}else{
			holder.select_default.setBackgroundResource(R.drawable.checkbox_false);	
		}
		
		//编辑
		holder.tv_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new Intent(context, MicroMallAddressActivity.class);
				intent.putExtra("oldAddress", list.get(position));
				context.startActivity(intent);
			}
		});
		//删除
		holder.tv_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog(holder,position,DELETE);
				
			}
		});
		holder.select_default.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!"Y".equals(list.get(position).isdefault)){
					showDialog(holder,position,DEFUALT);
				}
			}
		});
//		holder.select_default.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				// TODO Auto-generated method stub
//				if (isChecked) {
//					showdialog(holder,buttonView.getTag());
//				}
//			}
//		});
		
		
		
		return view;
	}
	

	private class ViewHolder{
		TextView tv_name;
		TextView tv_phone;
		TextView tv_address;
		Button select_default;
		TextView tv_edit;
		TextView tv_delete;
	}


	/**
	 * 弹出提示框，提醒用户是否要设置成默认地址,还是删除地址
	 */
	public void showDialog(final ViewHolder holder, final Object object,final int type) {
		String content = "";
		if (type == DEFUALT) {
			content = "是否设为默认收货地址？";
		} else if (type == DELETE) {
			content = "是否确定删除该收货地址？";
		}

		ShowUtils.showDiaLog(context, "温馨提示", content, new ConfirmBackCall() {

			@Override
			public void confirmOperation() {
				setAddress((Integer) object, type);

			}
		});

	}
	

	private Handler handler = new Handler(){
		Intent intent;
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ShowUtils.showMsg(context, "设置成功");

					intent = new Intent("AddressManaging");
					intent. setPackage("cc.upedu.online");
					context.sendBroadcast(intent);
					
				break;
			case 1:
				ShowUtils.showMsg(context, "设置失败");
			case 2:
				ShowUtils.showMsg(context, "删除成功");
				list.remove((int)msg.obj);
				refreshData();
				if (list.size() == 0) {
					intent = new Intent("AddressManaging");
					intent. setPackage("cc.upedu.online");
					context.sendBroadcast(intent);
				}
				break;
			case 3:
				ShowUtils.showMsg(context, "删除失败");
				break;
			}
		}
	};
	private void refreshData() {
		notifyDataSetChanged();
	}
	/**
	 * 发送http请求网络数据，设置默认收货地址
	 */
	private void setAddress(final int position,int type) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("userId", userId);
		params.addQueryStringParameter("addressId", list.get(position).addressId);

		final Message msg = new Message();
		
		if (type==DEFUALT) {
			getData(HttpMethod.GET, ConstantsOnline.MY_RECEIVE_ADDRESS_DEFAULT, params,
					new RequestCallBack<String>() {
						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							if (ProcessedData(responseInfo.result)) {
								
								msg.what = 0;
								handler.sendMessage(msg);
							}else {
								handler.obtainMessage(1).sendToTarget();
							}
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							ShowUtils.showMsg(context, "请求网络失败，请稍后重试");
						}
					});
		}else if(type==DELETE) {
			getData(HttpMethod.GET, ConstantsOnline.MY_RECEIVE_ADDRESS_DELETE, params,
					new RequestCallBack<String>() {
						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							if (ProcessedData(responseInfo.result)) {
								msg.obj = position;
								msg.what = 2;
								handler.sendMessage(msg);
							}else {
								handler.obtainMessage(1).sendToTarget();
							}
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							ShowUtils.showMsg(context, "请求网络失败，请稍后重试");
						}
					});
		}
	}
	

	/**
	 * 利用xutils进行http请求数据
	 * @param httpMethod 请求类型
	 * @param url	请求地址
	 * @param params 参数
	 * @param callBack 请求回调
	 */
	public void getData(HttpMethod httpMethod, String url,
			RequestParams params, RequestCallBack<String> callBack) {
		HttpUtils httpUtils= new HttpUtils();
		httpUtils.configHttpCacheSize(0);
		httpUtils.send(httpMethod, url,params, callBack);
	}
	/**
	 * 解析数据
	 * 
	 * @param result
	 * @return 
	 */
	private boolean ProcessedData(String result) {
		BackMessage bean = GsonUtil.jsonToBean(result, BackMessage.class);
		if (bean != null) {
			return Boolean.valueOf(bean.success);
		}else {
			return false;
		}
	}
	
	/**
	 * 删除收货地址条目以及设置默认地址返回值的javabean
	 * @author Administrator
	 *
	 */
	class BackMessage{
		public String message;
		public String success;
		
		@Override
		public String toString() {
			return "BackMessage [message=" + message + ", success=" + success
					+ "]";
		}
	}
}
