package cc.upedu.online.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.ExecutorService;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.activity.LoginActivity;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.LogUtils;
import cc.upedu.online.utils.NetUtil;
import cc.upedu.online.utils.NetUtil.NetObject;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;



public abstract class BaseActivity extends AppCompatActivity implements OnClickListener {
	
	/**
	 * 处理分发每个请求从服务器获取obj对象
	 * 
	 * @author zhangp
	 * 
	 */
	@SuppressLint("HandlerLeak")
	protected class BaseHandler extends Handler {
		/**
		 * 多态:表示获取实现类的对象(例如:TopicActivity中的topicCallBack)
		 */
		private DataCallBack dataCallback;

		public BaseHandler(DataCallBack<?> dataCallback) {
			super();
			this.dataCallback = dataCallback;
		}

		public void handleMessage(android.os.Message msg) {
			//关闭等待框
			switch (msg.what) {
			case ConstantsOnline.SUCCESS:// 数据请求成功
				if (dataCallback != null) {
					// 如果获取到了正确的对象后,把这个对象传递给实现类的processData方法(例如:TopicActivity)
					dataCallback.processData(msg.obj);
				}
				break;
			case ConstantsOnline.NET_FAILED:// 网络不正常
				LogUtils.toast(getApplicationContext(), "亲,请检查您的网络连接");
				dataCallback.processData(null);
				break;
			case ConstantsOnline.FAILED:// 数据请求失败
				LogUtils.toast(getApplicationContext(), "亲,服务器数据获取失败");
				dataCallback.processData(null);
				break;
			case ConstantsOnline.TIMEOUT://登录已过期
				ShowUtils.showDiaLog(context, "数据异常，请重新登录", "",
						new ConfirmBackCall() {

							@Override
							public void confirmOperation() {
								Intent intent = new Intent(context, LoginActivity.class);
								context.startActivity(intent);

							}
						});
				dataCallback.processData(null);
				break;
			}
		};
	};

	/**
	 * 获取服务器对象的解析工具类
	 * 
	 * @author zhangp
	 * 
	 * @param <T>
	 */
	protected interface DataCallBack<T> {
		/**
		 * 解析的方法
		 * 
		 * @param object
		 *            从服务器获取的对象(由Parser解析过来的)
		 */
		abstract void processData(T object);
	}

	protected Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = OnlineApp.myApp.instance;
		context = this;
		OnlineApp.myApp.addActivity(this);
		// 去掉标题栏
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 删除窗口背景
		getWindow().setBackgroundDrawable(null);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		// 初始化View
		initView();
		// 初始化监听
		initListener();
		// 请求处理数据
		initData();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		OnlineApp.myApp.removeActivity(this);
	}

	/**
	 * 初始化布局和布局的控件
	 */
	protected abstract void initView();

	/**
	 * 初始化监听
	 */
	protected abstract void initListener();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 从服务器获取数据
	 * 
	 * @param requestVo
	 *            请求的工具类
	 * @param dataCallBack
	 *            处理数据的工具类
	 */
	protected void getDataServer(RequestVo requestVo,
			DataCallBack<?> dataCallBack) {

		BaseHandler baseHander = new BaseHandler(dataCallBack);
		BaseRunnable baseRunnable = new BaseRunnable(requestVo, baseHander);
		if (requestVo.isShowDialog()) {
			//弹出等待框
		}
		// 接收Runnable开线程去自动执行
		instance.submit(baseRunnable);
	}

	/**
	 * 线程池
	 */
	protected ExecutorService instance;

	/**
	 * 从服务器请求数据,并且用BaseHandler传递给主线程
	 * 
	 * @author zhangp
	 * 
	 */
	protected class BaseRunnable implements Runnable {
		protected RequestVo vo;
		protected BaseHandler handler;

		public BaseRunnable(RequestVo vo, BaseHandler handler) {
			super();
			this.vo = vo;
			this.handler = handler;
		}

		@Override
		public void run() {

			Message msg = Message.obtain();
			try {
				if (NetUtil.hasConnectedNetwork(BaseActivity.this)) {// 有网则请求
					if (vo.getType().equals("get")) {
						msg.obj = NetUtil.get(vo);
					} else {
						msg.obj = NetUtil.post(vo);
					}
					if (msg.obj instanceof NetObject) {
						msg.obj = null;
						msg.what = ConstantsOnline.TIMEOUT;
					}else {
						msg.what = ConstantsOnline.SUCCESS;// 数据获取成功
					}
				} else {// 没网
					msg.what = ConstantsOnline.NET_FAILED;
				}
			} catch (Exception e) {
				msg.what = ConstantsOnline.FAILED;// 数据获取失败
				e.printStackTrace();
			}
			handler.sendMessage(msg);

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);//友盟统计
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);//友盟统计
	}
}
