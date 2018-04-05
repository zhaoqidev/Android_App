package cc.upedu.online.ryim;

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
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


public class ConversationListActivity extends FragmentActivity implements OnClickListener{
	TextView tv_title_2;// 标题
	LinearLayout ll_back;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversationlist);
       
		ll_back = (LinearLayout) findViewById(R.id.ll_back);
		tv_title_2=(TextView) findViewById(R.id.tv_title_2);
		tv_title_2.setText("我的消息");
		
        
		ll_back.setOnClickListener(this);

        /**
         * 向融云服务器传递用户信息：用户id，用户名，头像
         */
        RongIM.setUserInfoProvider(new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String arg0) {
                Log.i("token", "用户ID" + arg0);

                cc.upedu.online.domin.UserInfoBean.UserInfo userInfo= GetUserInforUtil.getUserInfo(ConversationListActivity.this, arg0);
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
        }, true);
        enterFragment();
    }
    /**
     * 加载 会话列表 ConversationListFragment
     */
      private void enterFragment() {
          ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);
          Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                  .appendPath("conversationlist")
                  .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                  .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//设置群组会话聚合显示
                  .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                  .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                  .build();

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
  }