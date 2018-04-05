package cc.upedu.online.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.PrivateLetterActivity;
import cc.upedu.online.activity.UserShowActivity;
import cc.upedu.online.domin.DeleteMessageBean;
import cc.upedu.online.domin.MySchoolmateBean.SchoolmateItem;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.CircleImageView;
import io.rong.imkit.RongIM;

/**
 * 全部学友的adapter
 * 
 * @author Administrator
 * 
 */
public class MySchoolmateAdapter extends AbsRecyclerViewAdapter{

	Button iv_private_letter;// 发站内信按钮
	Button iv_im_letter;// 发送即时消息按钮
	Button iv_stop_tweet;// 取消关注
	Button look_friend;// 查看学友名片
	Button cancel;// 取消按钮
	LinearLayout ll_blank;//空白部分，点击收起PopupWindow
//	ImageView iv_foucuse;// 已经互相关注按钮
	
	private View attentionView;// pop的view
	
	private String uid = UserStateUtil.getUserId();
	
	DeleteMessageBean bean;//取消好友关注后。返回的bean。

	public MySchoolmateAdapter(Context context, List<SchoolmateItem> list) {
		this.list = list;
		this.context = context;
		setResId(R.layout.pager_schoolmatemy_item);
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv_city;
		CircleImageView teacher_image;
		TextView teacher_name;
		TextView tv_company;
		TextView tv_position;
		ImageView iv_attention;
		

		public MyViewHolder(View view) {
			super(view);
			teacher_image = (CircleImageView) view.findViewById(R.id.teacher_image);
			teacher_name = (TextView) view.findViewById(R.id.teacher_name);
			tv_company = (TextView) view.findViewById(R.id.tv_company);
			tv_city = (TextView) view.findViewById(R.id.tv_city);
			tv_position = (TextView) view.findViewById(R.id.tv_position);
			iv_attention = (ImageView) view.findViewById(R.id.iv_attention);

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
		
		if (!StringUtil.isEmpty(item.avatar)) {
			ImageUtils.setImage(item.avatar, holder.teacher_image, R.drawable.left_menu_head);
		}else {
			holder.teacher_image.setImageResource(R.drawable.left_menu_head);
		}
		holder.teacher_name.setText(item.name);
		holder.tv_city.setText(item.city);
		
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
		
		//点击头像进入到好友名片
		/*holder.teacher_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userCard(position);
			}
		});*/
		
		//点击右侧已关注按钮弹出PopupWindow
		holder.iv_attention.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sportPopWindow(holder,position,item) ;
			}
		});
		//单向关注，互相关注所显示的图标不同
		if (!StringUtil.isEmpty(item.mutual)) {
			//0:单向关注 1:已互相关注
			if ("0".equals(item.mutual)) {
				holder.iv_attention.setImageDrawable(context.getResources().getDrawable(R.drawable.attention));
			}else if ("1".equals(item.mutual)) {
				holder.iv_attention.setImageDrawable(context.getResources().getDrawable(R.drawable.attention_other));
			}else {
				holder.iv_attention.setImageDrawable(context.getResources().getDrawable(R.drawable.attention));
			}
		}
		
		super.onBindViewHolder(viewHolder, position);	
	}

	/**
	 * 点击活动时弹出的popwindow
	 * 
	 */
	PopupWindow popup;// 活动的PopupWindow对象

	private void sportPopWindow(ViewHolder holder,int position,SchoolmateItem mateItem) {
		attentionView = View.inflate(context,R.layout.layout_popupwindow_myattention, null);
		
		iv_im_letter = (Button) attentionView.findViewById(R.id.iv_im_letter);
		iv_private_letter = (Button) attentionView.findViewById(R.id.iv_private_letter);
		iv_stop_tweet = (Button) attentionView.findViewById(R.id.iv_stop_tweet);
		look_friend = (Button) attentionView.findViewById(R.id.look_friend);
		cancel = (Button) attentionView.findViewById(R.id.cancel);
		ll_blank=(LinearLayout) attentionView.findViewById(R.id.ll_blank);
		 // 下面是两种方法得到宽度和高度 getWindow().getDecorView().getWidth()

		popup = new PopupWindow(attentionView,
	        WindowManager.LayoutParams.MATCH_PARENT,
	        WindowManager.LayoutParams.MATCH_PARENT);

	    // 设置popWindow弹出窗体可点击，这句话必须添加，并且是true
		popup.setFocusable(true);


	    // 实例化一个ColorDrawable颜色为半透明
	    ColorDrawable dw = new ColorDrawable(0x1e000000);
	    popup.setBackgroundDrawable(dw);

	    
	    // 设置popWindow的显示和消失动画
	    popup.setAnimationStyle(R.style.mypopwindow_anim_style);
	    // 在底部显示
	    popup.showAtLocation(((Activity) context).findViewById(R.id.iv_attention),Gravity.BOTTOM, 0, 0);
	    
	    getListener(position,mateItem);//pop内部的点击事件

		/*// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popup.setBackgroundDrawable(context.getResources().getDrawable(
				R.drawable.loginback));


		// 设置好参数之后再show
		//popup.showAsDropDown(holder.iv_attention);
*/	}
	
	

	private void getListener(final int position,final SchoolmateItem mateItem) {
		
		//发站内信的点击监听
		iv_private_letter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				//	写站内信			
				Intent intent =new Intent(context,PrivateLetterActivity.class);
				intent.putExtra("fid", mateItem.userId);
				intent.putExtra("uid", uid);
				intent.putExtra("name",mateItem.name);
				context.startActivity(intent);
			}
		});
		
		//发送即时消息的点击监听
		iv_im_letter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				/*
				 * 打开即时消息页面
				 */
				RongIM.getInstance().startPrivateChat(context, mateItem.userId, mateItem.name); 
			}
		});
		
		//取消关注的点击监听
		iv_stop_tweet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				getDeleteData(position,mateItem);//发送请求从服务器删除数据
				
			}
		});
		//进入到好友名片页面
		look_friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				popup.dismiss();
				//进入好友名片页面
				Intent intent = new Intent(context, UserShowActivity.class);
				intent.putExtra("userId", mateItem.userId);
				intent.putExtra("attention", mateItem.mutual);
				context.startActivity(intent);
			}
		});
		//取消pupupwindow
		cancel.setOnClickListener(new OnClickListener() {
					
			@Override
			public void onClick(View v) {
				popup.dismiss();
				}
			});
		//取消pupupwindow
		ll_blank.setOnClickListener(new OnClickListener() {
						
		@Override
		public void onClick(View v) {
			popup.dismiss();
			}
		});		
	}
	
	
	/**
	 * 发送http请求网络数据
	 */
	private void getDeleteData(final int position,SchoolmateItem mateItem) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", uid);
		params.addQueryStringParameter("fid", mateItem.userId);
		
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.DELETE_MY_ATTENTION, params,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						//请求成功解析数据
						bean = GsonUtil.jsonToBean(responseInfo.result, DeleteMessageBean.class);
						if (bean!=null) {
							list.remove(position);
							notifyDataSetChanged();
							ShowUtils.showMsg(context, bean.message);
						}else {
							ShowUtils.showMsg(context, "取消关注失败，请稍后重试");
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

	
}
