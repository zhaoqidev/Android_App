package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.DeleteMessageBean;
import cc.upedu.online.domin.MyMessageBean.MsgItem;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

/**
 * 侧拉栏——>我的消息的adapter
 * @author Administrator
 *
 */
public class MyMessageAdapter extends AbsRecyclerViewAdapter {

	private Boolean deleteBoolean ;
	public Boolean getDeleteBoolean() {
		return deleteBoolean;
	}
	public void setDeleteBoolean(Boolean deleteBoolean) {
		this.deleteBoolean = deleteBoolean;
	}

	private DeleteMessageBean deleteMessageBean;
	
	
	protected static final String tag = "MyMessageAdapter";
	

	
	public MyMessageAdapter(Context context, List<MsgItem> list) {
		this.context=context;
		this.list=list;
		setResId(R.layout.activity_mymessage_item);
	}	
	class MyViewHolder extends RecyclerView.ViewHolder {
		CircleImageView iv_menu_head;
		TextView tv_name;
		TextView tv_content;
		TextView tv_time;
		ImageView iv_delete;
		

		public MyViewHolder(View view) {
			super(view);
			iv_menu_head =  (CircleImageView) view.findViewById(R.id.iv_menu_head);
			tv_name = (TextView) view.findViewById(R.id.tv_name);
			tv_content = (TextView) view.findViewById(R.id.tv_content);
			tv_time = (TextView) view.findViewById(R.id.tv_time);
			iv_delete=(ImageView) view.findViewById(R.id.iv_delete);

		}

	}
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
		View view = LayoutInflater.from(viewGroup.getContext()).inflate(
				resId, viewGroup, false);
		return new MyViewHolder(view);
	}
	@Override
	public void onBindViewHolder(ViewHolder viewHolder, final int position) {
		final MsgItem item = (MsgItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		if (!StringUtil.isEmpty(item.avatar)) {
			ImageUtils.setImage(item.avatar, holder.iv_menu_head, R.drawable.left_menu_head);
		}else{
			holder.iv_menu_head.setImageResource(R.drawable.left_menu_head);
		}
		
		holder.tv_name.setText(item.name);
		holder.tv_content.setText(item.content);
		holder.tv_time.setText(item.sendTime);
		
		if(deleteBoolean==false){
			holder.iv_delete.setVisibility(View.GONE);
		}else {
			holder.iv_delete.setVisibility(View.VISIBLE);
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					getDeleteData(position,item);//发送请求从服务器删除数据
				}
			});
		}
		
		super.onBindViewHolder(viewHolder, position);	
	}
	
	
	
	
	

	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ShowUtils.showMsg(context, "删除成功");
				list.remove((int)msg.obj);
				refreshData();
				if (list.size() == 0) {
					Intent intent = new Intent("MessageManaging");
					intent. setPackage("cc.upedu.online");
					context.sendBroadcast(intent);
				}
				break;
			case 1:
				ShowUtils.showMsg(context, "删除失败");
				break;
			}
		}
	};
	private void refreshData() {
		notifyDataSetChanged();
	}
	/**
	 * 发送http请求网络数据
	 */
	private void getDeleteData(final int position,MsgItem msgItem) {
		
		//Map<String, String> requestDataMap = ParamsMapUtil.deleteMySchoolMate(context, item.msgId, item.type);
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("msgId", msgItem.msgId);
		params.addQueryStringParameter("type", msgItem.type);
		// params.addHeader("msgId", item.msgId);
		// params.addHeader("type", item.type);
		Log.i("msg", params.getQueryStringParams().toString());
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.DELETE_MY_MESSAGE, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (ProcessedData(responseInfo.result)) {
							Message msg = new Message();
							msg.obj = position;
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
		httpUtils.send(httpMethod, url,params, callBack);
	}
	/**
	 * 解析数据
	 * 
	 * @param result
	 * @return 
	 */
	private boolean ProcessedData(String result) {
		deleteMessageBean = GsonUtil.jsonToBean(result, DeleteMessageBean.class);
		if (deleteMessageBean != null) {
			return Boolean.valueOf(deleteMessageBean.success);
		}else {
			return false;
		}
	}
	

}
