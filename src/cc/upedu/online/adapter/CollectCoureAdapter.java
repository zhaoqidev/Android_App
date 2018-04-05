package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
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
import cc.upedu.online.domin.CollectCoureListBean.Entity.CollectCoureItem;
import cc.upedu.online.domin.DeleteMessageBean;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;

public class CollectCoureAdapter extends AbsRecyclerViewAdapter {
	private boolean isManaging;
	
	public void setManaging(boolean isManaging) {
		this.isManaging = isManaging;
	}
	public CollectCoureAdapter(Context context, List<CollectCoureItem> list, boolean isManaging) {
		this.context=context;
		this.list=list;
		this.isManaging = isManaging;
		setResId(R.layout.activity_mycollect_courseitem);
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		ImageView courseimage_item;
		TextView course_title;//课程标题
		ImageView iv_delete;//删除按钮
		TextView tv_teacherName;//导师姓名
		TextView tv_apply_count;//报名人数
		

		public MyViewHolder(View view) {
			super(view);
			courseimage_item = (ImageView) view.findViewById(R.id.courseimage_item);
			course_title = (TextView) view.findViewById(R.id.course_title);
			tv_teacherName = (TextView) view.findViewById(R.id.tv_teacherName);
			tv_apply_count = (TextView) view.findViewById(R.id.tv_apply_count);
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
		final CollectCoureItem item = (CollectCoureItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;
		
		ImageUtils.setImage(item.getLogo(), holder.courseimage_item, 0);
		holder.course_title.setText(item.getName());
		holder.tv_teacherName.setText(item.getTeacherName());
		holder.tv_apply_count.setText(item.getJoinNum());
		if(!isManaging){
			holder.iv_delete.setVisibility(View.GONE);
		}else {
			holder.iv_delete.setVisibility(View.VISIBLE);
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//提醒用户
					ShowUtils.showDiaLog(context, "温馨提示", "确定要删除吗?", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							// TODO Auto-generated method stub
							//发送请求从服务器删除数据
							getDeleteData(position,item);
						}
					});
				}
			});
		}
		
		final String courseId = item.getCourseId();
		final String courseType = item.getCourseId();
//		view.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if ("LIVE".equals(courseType)) {
//					Intent intent = new Intent(context, TelecastApplayActivity.class);
//					intent.putExtra("courseId", courseId);
//					context.startActivity(intent);
//				}else {
//					Intent intent = new Intent(context,
//							CourseIntroduceActivity.class);
//					intent.putExtra("courseId", courseId);
//					context.startActivity(intent);
//				}
//			}
//		});

		
		super.onBindViewHolder(viewHolder, position);	
	}
	
	
	
	
	

	private DeleteMessageBean deleteMessageBean;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				ShowUtils.showMsg(context, "删除成功");
				list.remove((int)msg.obj);
				refreshData();
				if (list.size() == 0) {
					Intent intent = new Intent("CollectManaging");
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
	private void getDeleteData(final int position,CollectCoureItem item) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("favIds", item.getFavouriteId());
		params.addQueryStringParameter("type", "1");
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.DELETE_MY_COLLECT, params,
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
						handler.obtainMessage(1).sendToTarget();
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
