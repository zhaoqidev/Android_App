package cc.upedu.online.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.domin.SportMateBean.JoinUserItem;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.CircleImageView;

/**
 * 活动页面已经参加学友的adapter
 * 
 * @author Administrator
 * 
 */
public class SportMateAdapter extends BaseAdapter {
	private List<JoinUserItem> list;
	private Context context;

	public SportMateAdapter(Context context, List<JoinUserItem> list) {
		this.list = list;
		this.context = context;
		if(this.getCount() == 0 && list.size()==0){
			list.add(null);
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view;
		final ViewHolder holder;
		if(null==list.get(position)&& position == 0){
			view = new TextView(context);
			view.setVisibility(View.INVISIBLE);
			return view;
		}else{
			// 复用
			if (convertView == null) {
				view = View.inflate(context, R.layout.pager_schoolmate_item, null);
				holder = new ViewHolder();
				holder.teacher_image = (CircleImageView) view
						.findViewById(R.id.teacher_image);
				holder.teacher_name = (TextView) view
						.findViewById(R.id.teacher_name);
				holder.tv_company = (TextView) view.findViewById(R.id.tv_company);
				holder.tv_position = (TextView) view.findViewById(R.id.tv_position);
				holder.tv_attention = (TextView) view.findViewById(R.id.tv_attention);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
//			view.setBackgroundColor(context.getResources().getColor(R.color.backGrond));
			
			if (!StringUtil.isEmpty(list.get(position).avatar)) {
				
				ImageUtils.setImage(list.get(position).avatar, holder.teacher_image, R.drawable.left_menu_head);
			}else {
				holder.teacher_image.setImageResource(R.drawable.left_menu_head);
			}
			holder.teacher_name.setText(list.get(position).uname);
			// holder.tv_city.setText(list.get(position).city);
			if(StringUtil.isEmpty(list.get(position).ucompany)){
				holder.tv_company.setText("未公开");
			}else {
				holder.tv_company.setText(list.get(position).ucompany);
			}
			
			if(StringUtil.isEmpty(list.get(position).uposition)){
				holder.tv_position.setText("未公开");
			}else {
				holder.tv_position.setText(list.get(position).uposition);
			}

			holder.tv_attention.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!UserStateUtil.isLogined()) {
						UserStateUtil.NotLoginDialog(context);
					} else {
						if (!StringUtil.isEmpty(list.get(position).isFriend)) {
							if ("0".equals(list.get(position).isFriend)) {
								getData(position, holder);
							} else {
								ShowUtils.showMsg(context, "请在我的关注中，取消对其关注");
							}
						}
					}
				}
			});

			holder.teacher_image.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, UserShowActivity.class);
					intent.putExtra("userId", list.get(position).uid);
					if ("0".equals(list.get(position).isFriend)) {
						intent.putExtra("attention", "2");
					}else if ("1".equals(list.get(position).isFriend)) {
						intent.putExtra("attention", "0");
					}else if ("2".equals(list.get(position).isFriend)) {
						intent.putExtra("attention", "1");
					}
					intent.putExtra("attention", list.get(position).isFriend);
					context.startActivity(intent);
				}
			});

			switch (list.get(position).isFriend) {
			// 0：未关注 1：已关注 2：互相关注
			case "1":
				//holder.tv_attention.setText("已关注");
				holder.tv_attention.setVisibility(View.VISIBLE);
				holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_already));
				break;
			case "2":
				//holder.tv_attention.setText("互相关注");
				holder.tv_attention.setVisibility(View.VISIBLE);
				holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_eachother));
				break;
			case "0":
				//holder.tv_attention.setText("+关注");
				if (UserStateUtil.getUserId()!=null) {
					if (UserStateUtil.getUserId().equals(list.get(position).uid)) {
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
			
			return view;
		}
	
	}
	
	
	/**
	 * 发送http请求网络数据
	 */
	private void getData(final int position,final ViewHolder holder) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", UserStateUtil.getUserId());
		params.addQueryStringParameter("fid", list.get(position).uid);
		
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.ATTENTION_OTHERS, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
					
						ProcessedData(responseInfo.result,holder,position);
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
	private void ProcessedData(String result,ViewHolder holder,int position) {
		Attention attentionBean = GsonUtil.jsonToBean(result, Attention.class);
		if (attentionBean!=null) {
			ShowUtils.showMsg(context, attentionBean.message);
			if ("true".equals(attentionBean.success)) {
				//holder.tv_attention.setText("已关注");
				holder.tv_attention.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.attention_already));
				list.get(position).isFriend= "1";
			}
		}else{
			ShowUtils.showMsg(context, "关注学友失败，请稍后重试");
		}
			
	}

	private class ViewHolder {
		// TextView tv_city;
		CircleImageView teacher_image;
		TextView teacher_name;
		TextView tv_company;
		TextView tv_position;
		TextView tv_attention;
	}
	
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	//关注请求的javabean
		private class Attention{
				public String message;
				public String success;
		}

}
