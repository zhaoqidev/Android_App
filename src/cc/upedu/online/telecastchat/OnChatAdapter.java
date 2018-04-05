package cc.upedu.online.telecastchat;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gensee.view.MyTextViewEx;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.domin.UserInfoDetailBean;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GsonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.MyThreadPoolManagerUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.CircleImageView;

public class OnChatAdapter extends AbstractAdapter<AbsChatMessage> {
	private SparseArray<String> mSparseArray;
	private List<AbsChatMessage> mList = new ArrayList<AbsChatMessage>();
	private Context mContext;
	private TextView mSendNameText;
	private TextView mTimetext;
	private ImageView chat_image;
	private MyTextViewEx mViewContextText;
	private long mUserId;

	public OnChatAdapter(Context context) {
		super(context);
		mList = new ArrayList<AbsChatMessage>();
		mContext = context;

	}

	public void init(long mUserID) {
		this.mUserId = mUserID;
		if (mUserID == -1000) {
			mList = new ArrayList<AbsChatMessage>();
			mList = PublicChatManager.getIns().getMsgList();
		} else {
			mList = new ArrayList<AbsChatMessage>();
			mList = PrivateChatManager.getIns().getMsgListByUserId(mUserId);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AbstractViewHolder mAbstractViewHolder = null;

		convertView = createView();
		mAbstractViewHolder = createViewHolder(convertView);
		convertView.setTag(mAbstractViewHolder);

		mAbstractViewHolder.init(position);

		return convertView;
	}

	@Override
	protected View createView() {
		
		View v;
		LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
		v = mLayoutInflater.inflate(R.layout.chat_listitem_layout, null);
		return v;
	}

	@Override
	protected AbstractViewHolder createViewHolder(View view) {
		
		ViewHolder mViewHolder = new ViewHolder(view);
		return mViewHolder;

	}

	protected class ViewHolder extends AbstractViewHolder {

		public ViewHolder(View currView) {
			mViewContextText = (MyTextViewEx) currView
					.findViewById(R.id.chatcontexttextview);
			mTimetext = (TextView) currView.findViewById(R.id.chattimetext);
			mSendNameText = (TextView) currView.findViewById(R.id.chatnametext);
			chat_image = (CircleImageView) currView.findViewById(R.id.chat_image);

		}

		@Override
		public void init(int position) {
			
			long mSendTime = mList.get(position).getTime();
			mSendNameText.setText(mList.get(position).getSendUserName());
//			mTimetext.setText(String.format("%02d",
//					(mSendTime / 3600 % 24 + 8) % 24)
//					+ ":"
//					+ String.format("%02d", mSendTime % 3600 / 60)
//					+ ":"
//					+ String.format("%02d", mSendTime % 3600 % 60));
			mTimetext.setText(StringUtil.getTimeShort(mSendTime));
			mViewContextText.setRichText(mList.get(position).getRich());
			final int userId = (int) (mList.get(position).getSendUserId() - 1000000000);
			if (mSparseArray == null) {
				mSparseArray = new SparseArray<String>();
			}
			if (mSparseArray.size() > 0 && !StringUtil.isEmpty(mSparseArray.get(userId))) {
				setImage(chat_image, mSparseArray.get(userId));
			}else {
				Runnable runnable = new Runnable() {
					@Override
					public void run() {
						setCollectData(chat_image, String.valueOf(userId));
					}
				};
				// 执行线程
				MyThreadPoolManagerUtil.getInstance().submit(runnable);
			}
		}
	}
	/**
	 * 发送http请求网络数据
	 */
	private void setCollectData(final ImageView chat_image,final String userId) {
		
		RequestParams params = new RequestParams();
		params.addQueryStringParameter("uid", userId);
		// 调用父类方法，发送请求
		getData(HttpMethod.GET, ConstantsOnline.GET_USERINFO, params,
				new RequestCallBack<String>() {
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						UserInfoDetailBean userInfoDetailBean = ProcessedData(responseInfo.result);
						if (null != userInfoDetailBean && Boolean.valueOf(userInfoDetailBean.getSuccess()) && userInfoDetailBean.getEntity() != null && !StringUtil.isEmpty(userInfoDetailBean.getEntity().getAvatar())) {
							mSparseArray.put(Integer.valueOf(userId), userInfoDetailBean.getEntity().getAvatar());
//							setImage(chat_image, userInfoDetailBean.getEntity().getAvatar());
							OnChatAdapter.this.notifyDataSetChanged();
						}
//						else{
//							chat_image.setImageResource(R.drawable.left_menu_head);
//						}
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
	private UserInfoDetailBean ProcessedData(String result) {
		return GsonUtil.jsonToBean(result, UserInfoDetailBean.class);
	}
	/**
	 * @param chat_image
	 * @param avatar
	 */
	private void setImage(final ImageView chat_image,
			String avatar) {
		
		ImageUtils.setImage(avatar, chat_image, R.drawable.left_menu_head);
	}
}