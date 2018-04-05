package cc.upedu.online.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.SchoolmateAllBean.SchoolmateItem;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.CircleImageView;

/**
 * 全部学友的adapter
 * @author Administrator
 *
 */
public class SchoolmateAllAdapter extends AbsRecyclerViewAdapter {
	public SchoolmateAllAdapter(Context context, List<SchoolmateItem> list) {
		this.list = list;
		this.context = context;
		setResId(R.layout.pager_schoolmateall_item);
	}
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv_city;
		CircleImageView teacher_image;
		TextView teacher_name;
		TextView tv_company;
		TextView tv_position;
		TextView tv_attention;

		public MyViewHolder(View view) {
			super(view);
			teacher_image = (CircleImageView) view.findViewById(R.id.teacher_image);
			teacher_name = (TextView) view.findViewById(R.id.teacher_name);
			tv_company = (TextView) view.findViewById(R.id.tv_company);
			tv_city = (TextView) view.findViewById(R.id.tv_city);
			tv_position = (TextView) view.findViewById(R.id.tv_position);
			tv_attention =  (TextView) view.findViewById(R.id.tv_attention);

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
		final SchoolmateItem item = (SchoolmateItem) list.get(position);
		final MyViewHolder holder = (MyViewHolder) viewHolder;

        ImageUtils.setImage(item.avatar, holder.teacher_image, R.drawable.left_menu_head);
		holder.teacher_name.setText(item.userName);
		holder.tv_city.setText(item.city);
		//holder.tv_company.setText(item.company);
		//holder.tv_position.setText(item.position);
		if(StringUtil.isEmpty(item.company)){
			holder.tv_company.setText("未公开");
		}else {
			holder.tv_company.setText(item.company);
		}
		
		if(StringUtil.isEmpty(item.position)){
			holder.tv_position.setText("未公开");
		}else {
			holder.tv_position.setText(item.position);
		}
		holder.tv_attention.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!UserStateUtil.isLogined()) {
					UserStateUtil.NotLoginDialog(context);
				}else {
					if(!StringUtil.isEmpty(item.isFriend)){
						if("2".equals(item.isFriend)){
							getData(position,holder,item);
						}else{
							ShowUtils.showMsg(context, "请在我的关注中，取消对其关注");
						}
					}
				}
			}
		});
		
		/*
		 * 点击学友头像的点击事件
		 * holder.teacher_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, UserShowActivity.class);
				intent.putExtra("userId", item.userId);
				context.startActivity(intent);
			}
		});
		*/
		switch (item.isFriend) {
		//0：已关注 1：互相关注2：未关注
		case "0":
			//holder.tv_attention.setText("已关注");
			holder.tv_attention.setVisibility(View.VISIBLE);
			holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_already));
			break;
		case "1":
			holder.tv_attention.setVisibility(View.VISIBLE);
			//holder.tv_attention.setText("互相关注");
			holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_eachother));
			break;
		case "2":
			//holder.tv_attention.setText("+关注");
			if (UserStateUtil.getUserId()!=null) {
				if (UserStateUtil.getUserId().equals(item.userId)) {
					holder.tv_attention.setVisibility(View.GONE);
				}else {
					holder.tv_attention.setVisibility(View.VISIBLE);
					holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_add));
				}
			}else {
				holder.tv_attention.setVisibility(View.VISIBLE);
				holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_add));
			}
			
			break;

		default:
			//holder.tv_attention.setText("+关注");
			holder.tv_attention.setVisibility(View.VISIBLE);
			holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_add));
			break;
		}
		
		
		super.onBindViewHolder(viewHolder, position);	
	}

	/**
	 * 发送http请求网络数据
	 */
	private void getData(final int position,final MyViewHolder holder,final SchoolmateItem item) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", UserStateUtil.getUserId());
		params.addQueryStringParameter("fid", item.userId);
		
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.ATTENTION_OTHERS, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
					
						ProcessedData(responseInfo.result,holder,position,item);
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
	 */
	private void ProcessedData(String result,MyViewHolder holder,int position,SchoolmateItem item) {
		Attention attentionBean = GsonUtil.jsonToBean(result, Attention.class);
		if (attentionBean!=null) {
			ShowUtils.showMsg(context, attentionBean.message);
			if ("true".equals(attentionBean.success)) {
				//holder.tv_attention.setText("已关注");
				holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_already));
				item.isFriend= "0";
			}
		}else {
			ShowUtils.showMsg(context, "关注学友失败，请稍后重试");
		}
		
	}

	//关注请求的javabean
	private class Attention{
		public String message;
		public String success;
	}
	
	
}
