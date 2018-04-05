package cc.upedu.online.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.gensee.callback.IChatCallBack;
import com.gensee.room.RTRoom;
import com.gensee.room.RtSdk;
import com.gensee.routine.UserInfo;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.ChatEditText;

import java.util.Calendar;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.activity.TelecastHomeActivity;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.telecastchat.GridViewAvatarAdapter;
import cc.upedu.online.telecastchat.GridViewAvatarAdapter.SelectAvatarInterface;
import cc.upedu.online.telecastchat.OnChatAdapter;
import cc.upedu.online.telecastchat.PrivateChatManager;
import cc.upedu.online.telecastchat.PrivateChatMessage;
import cc.upedu.online.telecastchat.PublicChatManager;
import cc.upedu.online.telecastchat.PublicChatMessage;
import cc.upedu.online.utils.StringUtil;
/**
 * 直播界面中的聊天界面
 * @author Administrator
 *
 */
public class TelecastCommentFragment extends BaseFragment implements OnClickListener,
		IChatCallBack, SelectAvatarInterface, OnTaskRet {
	private Button mSendmsgButton;
	private ImageView mExpressionButton;
//	private Spinner mSpinner;
	private RtSdk mRtSdk;
//	private String[] mNameString;
	private GridView mGridView;
	private GridViewAvatarAdapter mGridViewAvatarAdapter;
	private ChatEditText mChatEditText;
	private OnChatAdapter mOnChatAdapter;
	private ListView mContextListView;
	private List<UserInfo> mlList;
	private long mUserID = -1000;
	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			mOnChatAdapter.init(mUserID);

		}

	};
	
	public TelecastCommentFragment(Context context) {
	}

	@Override
	protected View initView(LayoutInflater inflater) {
		View view = View.inflate(context, R.layout.layout_telecast_comment, null);
		mSendmsgButton = (Button) view.findViewById(R.id.chat_sendmsg);
		mExpressionButton = (ImageView) view.findViewById(R.id.chat_expression);
//		mSpinner = (Spinner) view.findViewById(R.id.chat_spininer);
		mGridView = (GridView) view.findViewById(R.id.chat_grid_view);
		mChatEditText = (ChatEditText) view.findViewById(R.id.chat_edittext);
		mContextListView = (ListView) view.findViewById(R.id.chat_context_listview);
		mOnChatAdapter = new OnChatAdapter(context);

		mGridViewAvatarAdapter = new GridViewAvatarAdapter(mGridView.getContext(), this);
		mGridView.setAdapter(mGridViewAvatarAdapter);
		
		mContextListView.setAdapter(mOnChatAdapter);

		mRtSdk = TelecastHomeActivity.mRTSdk2;
		
//		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
//
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view,
//					int position, long id) {
//				// TODO Auto-generated method stub
//				if (position == 0) {
//					mUserID = -1000;
//				} else {
//					mUserID = mlList.get(position - 1).getId();
//				}
//				mHandler.sendEmptyMessage(0);
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//
//			}
//		});
		mSendmsgButton.setOnClickListener(this);
		mExpressionButton.setOnClickListener(this);
		// mSpinner.setOnClickListener(this);
//		initSpinner();
		mRtSdk.setChatCallback(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.chat_sendmsg:
			String chatText = mChatEditText.getChatText();
			String richText = mChatEditText.getRichText();
			if (!StringUtil.isEmpty(chatText.trim()) || !StringUtil.isEmpty(richText.trim())) {
				mRtSdk.chatWithPublic(chatText,richText, this);
//			if (mUserID == -1000) {
//				mRtSdk.chatWithPublic(chatText,richText, this);
//			} else {
//				mRtSdk.chatWithPersion(chatText,richText, mUserID, this);
//			}
				
				((TelecastHomeActivity)context).packUpKeyboard();
				if (mGridView.getVisibility() == View.VISIBLE) {
					mGridView.setVisibility(View.GONE);
				}
				mChatEditText.setText("");
			}
			break;
		case R.id.chat_expression:
			((TelecastHomeActivity)context).packUpKeyboard();
			if (mGridView.getVisibility() == View.GONE) {
				mGridView.setVisibility(View.VISIBLE);
			} else if (mGridView.getVisibility() == View.VISIBLE) {
				mGridView.setVisibility(View.GONE);
			}
			if (mGridViewAvatarAdapter == null) {
				mGridViewAvatarAdapter = new GridViewAvatarAdapter(
						mGridView.getContext(), this);
				mGridView.setAdapter(mGridViewAvatarAdapter);
			} else {
				mGridViewAvatarAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
		
	}
	/**
	 * 聊天模块加载成功与否通知
	 */
	@Override
	public void onChatJoinConfirm(boolean bRet) {
		// TODO Auto-generated method stub

	}
	/**
	 * 收到私聊消息
	 */
	@Override
	public void onChatWithPersion(UserInfo userInfo, String msg, String rich) {

		PrivateChatMessage message = new PrivateChatMessage();
		message.setText(msg);
		message.setTime(Calendar.getInstance().getTimeInMillis());
		message.setSendUserId(userInfo.getId());
		message.setRich(rich);
		message.setSendUserName(userInfo.getName());
		PrivateChatManager.getIns().addMsg(userInfo.getId(), message);
			mHandler.sendEmptyMessage(0);
//		if (mUserID == userInfo.getId()) {
//			mHandler.sendEmptyMessage(0);
//		}
	}
	/**
	 * 收到公聊消息
	 */
	@Override
	public void onChatWithPublic(UserInfo userInfo, String msg, String rich) {

		PublicChatMessage mPublicChatMessage = new PublicChatMessage();
		mPublicChatMessage.setText(msg);
		mPublicChatMessage.setRich(rich);
		mPublicChatMessage.setSendUserName(userInfo.getName());
		mPublicChatMessage.setTime(System.currentTimeMillis());
		mPublicChatMessage.setSendUserId(userInfo.getId());
		PublicChatManager.getIns().addMsg(mPublicChatMessage);
//		if (mUserID == -1000) {
//			mHandler.sendEmptyMessage(0);
//		}
			mHandler.sendEmptyMessage(0);
	}
	/**
	 * 发送私聊消息的回送
	 */
	@Override
	public void onChatToPersion(long userId, String msg, String rich) {
		// TODO Auto-generated method stub
		PrivateChatMessage message = new PrivateChatMessage();
		message.setText(msg);
		message.setTime(Calendar.getInstance().getTimeInMillis());
		message.setSendUserId(RTRoom.getIns().getUserId());
		message.setRich(rich);
		message.setReceiveUserId(userId);
		message.setSendUserName(mRtSdk.getSelfUserInfo().getName());
		PrivateChatManager.getIns().addMsg(userId, message);
		mHandler.sendEmptyMessage(0);
	}
	/**
	 * 聊天禁聊通知
	 */
	@Override
	public void onChatEnable(boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAvatar(String sAvatar, Drawable resId) {
		// TODO Auto-generated method stub
//		mChatEditText.getText().insert(mChatEditText.getSelectionStart(),
//				SpanResource.convetToSpan(sAvatar.toString(),
//						this));
		mChatEditText.insertAvatar(sAvatar,0);
	}

	@Override
	public void onTaskRet(boolean ret, int id, String desc) {
		// TODO Auto-generated method stub

		mHandler.sendEmptyMessage(0);
	}

	@Override
	public void initData() {

	}
}
