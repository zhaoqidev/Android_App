package cc.upedu.online.ryim;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import cc.upedu.online.R;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GetUserInforUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.UserStateUtil;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

public class ConversationActivity extends FragmentActivity implements OnClickListener, UserInfoProvider {

	TextView tv_title_2;// 标题
	LinearLayout ll_back;

	/**
	 * 目标 Id
	 */
	private String mTargetId;
	/**
	 * 目标姓名
	 */
	private String userName;


	/**
	 * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
	 */
	private String mTargetIds;

	/**
	 * 会话类型
	 */
	private Conversation.ConversationType mConversationType;
	
	public SharedPreferences tokenSp;//用于存储token 的sp

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conversation);
		
		tokenSp = getApplicationContext().getSharedPreferences("TokenConfig", MODE_PRIVATE);
	       
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		tv_title_2=(TextView) findViewById(R.id.tv_title_2);
		
		ll_back.setOnClickListener(this);
		
		/**
		 * 向融云服务器传递用户信息：用户id，用户名，头像
		 */
		RongIM.setUserInfoProvider(this,true);
		
		Intent intent = getIntent();
		getIntentDate(intent);
	}

	/**
	 * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
	 */
	private void getIntentDate(Intent intent) {

		mTargetId = intent.getData().getQueryParameter("targetId");
		userName = intent.getData().getQueryParameter("title");
		
		tv_title_2.setText(userName);
		// mTargetIds = intent.getData().getQueryParameter("targetIds");
		// intent.getData().getLastPathSegment();//获得当前会话类型
		mConversationType = Conversation.ConversationType.PRIVATE;
		// Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
		

		enterFragment(mConversationType, mTargetId);
	}

	/**
	 * 加载会话页面 ConversationFragment
	 * 
	 * @param mConversationType
	 *            会话类型
	 * @param mTargetId
	 *            目标 Id
	 */
	private void enterFragment(Conversation.ConversationType mConversationType,
			String mTargetId) {

		ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager()
				.findFragmentById(R.id.conversation);

		Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName)
				.buildUpon().appendPath("conversation")
				.appendPath(mConversationType.getName().toLowerCase())
				.appendQueryParameter("targetId", mTargetId).build();
		
		//实现陌生人的头像和姓名的显示
		RongIM.getInstance().setCurrentUserInfo(new UserInfo(UserStateUtil.getUserId(),
				SharedPreferencesUtil.getInstance().spGetString("name"),
				Uri.parse(ConstantsOnline.SERVER_IMAGEURL+SharedPreferencesUtil.getInstance().spGetString("avatar"))));
		RongIM.getInstance().setMessageAttachedUserInfo(true); 
		
		fragment.setUri(uri);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_back:
			finish();
			break;

		default:
			break;
		}
		
	}

	
	@Override
	public UserInfo getUserInfo(String arg0) {
		Log.i("token", "用户ID"+arg0);

		cc.upedu.online.domin.UserInfoBean.UserInfo userInfo=GetUserInforUtil.getUserInfo(this, arg0);
		if (userInfo!=null) {
			Log.i("token", "用户信息正确返回");
			Log.i("token", "userId"+arg0);
			Log.i("token", "userName"+userInfo.uname);
			Log.i("token", "userAvatar"+Uri.parse(ConstantsOnline.SERVER_IMAGEURL+userInfo.avatar));
			return new UserInfo(arg0,userInfo.uname,Uri.parse(ConstantsOnline.SERVER_IMAGEURL+userInfo.avatar));
		}else {
			Log.i("token", "用户信息返回空");
			return null;
		}
		
		
	}
	
	

}