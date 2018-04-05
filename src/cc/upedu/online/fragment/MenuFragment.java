package cc.upedu.online.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.lidroid.xutils.ViewUtils;

import java.util.List;

import cc.upedu.online.MainActivity;
import cc.upedu.online.R;
import cc.upedu.online.activity.LoginActivity;
import cc.upedu.online.activity.MicroMallActivity;
import cc.upedu.online.activity.MyAnswerListActivity;
import cc.upedu.online.activity.MyCollectActivity;
import cc.upedu.online.activity.MyCourseActivity;
import cc.upedu.online.activity.MyMessageActivity;
import cc.upedu.online.activity.MyNoteActivity;
import cc.upedu.online.activity.MyOrderActivity;
import cc.upedu.online.activity.MyReceivingAddressActivity;
import cc.upedu.online.activity.MyRepresentActivity;
import cc.upedu.online.activity.MySchoolmateActivity;
import cc.upedu.online.activity.MyShoppingTrolleyActivity;
import cc.upedu.online.activity.MySportActivity;
import cc.upedu.online.activity.MyWalletActivity;
import cc.upedu.online.activity.SettingActivity;
import cc.upedu.online.activity.UserSettingActivity;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cc.upedu.online.view.CircleImageView;

/**
 * 侧拉栏
 * 
 * @author Administrator
 * 
 */
public class MenuFragment extends BaseFragment implements OnClickListener {
	private SlidingMenu slidingMenu;
	List<String> titleList;// 存储侧边栏的集合
	MenuAdapter adapter;
	String[] ownItem;// 侧边栏条目数组
	int[] img ={R.drawable.left_course,R.drawable.left_note,R.drawable.left_collect,R.drawable.left_question,R.drawable.left_represent,
			R.drawable.left_sport,R.drawable.left_mate,R.drawable.left_wallet,
			R.drawable.left_order,R.drawable.left_mall,R.drawable.left_address,R.drawable.left_message};//

	/*@ViewInject(R.id.lv_left_menu)
	public ListView lv_left_menu;
	@ViewInject(R.id.tv_setting)
	public TextView tv_setting;*/
	
	public ListView lv_left_menu;
	public LinearLayout ll_setting;
	public LinearLayout ll_shoppingcart;

	TextView tv_menu_lode;// 立即登录
	TextView tv_menu_name;// 姓名
	TextView tv_menu_zhiwu;// 职务
	TextView tv_menu_qianming;// 个性签名
	CircleImageView iv_menu_head;// 头像
	ImageView imageView1;// 个性签名左侧图片

	String userId = "";
	String userName = "";
	String loginsid = "";// 用户识别码

	boolean ISLOGIN;// 记录是否是登录状态
	private Intent intent;
	FailureCallBack mFailureCallBack;
	View view;
	public Dialog loadingDialog;

	public MenuFragment(){
		
	}

	@Override
	public View initView(LayoutInflater inflater) {
		// 从文件中获取侧拉栏条目文字信息
		Resources res = getResources();
		ownItem = res.getStringArray(R.array.ownitem);

		view = inflater.inflate(R.layout.layout_left_menu, null);
		ViewUtils.inject(this, view);

		if (slidingMenu == null) {
			slidingMenu = ((MainActivity) context).getSlidingMenu();
		}
		
		tv_menu_lode = (TextView) view.findViewById(R.id.tv_menu_lode);
		tv_menu_name = (TextView) view.findViewById(R.id.tv_menu_name);
		tv_menu_zhiwu = (TextView) view.findViewById(R.id.tv_menu_zhiwu);
		tv_menu_qianming = (TextView) view.findViewById(R.id.tv_menu_qianming);
		iv_menu_head = (CircleImageView) view.findViewById(R.id.iv_menu_head);
		
		lv_left_menu=(ListView) view.findViewById(R.id.lv_left_menu);
		ll_setting=(LinearLayout) view.findViewById(R.id.ll_setting);
		ll_shoppingcart=(LinearLayout) view.findViewById(R.id.ll_shoppingcart);
		initListener();
		return view;
	}

	/**
	 * 填充左边侧拉栏条目
	 * 
	 * @param
	 */
	public void initMenu() {
		final MenuAdapter adapter = new MenuAdapter();
		lv_left_menu.setAdapter(adapter);
		//登陆过期的回调
		mFailureCallBack=new FailureCallBack() {
			
			@Override
			public void onFailureCallBack() {
				tv_menu_lode.setText("立即登录");
				iv_menu_head.setImageDrawable(context.getResources().getDrawable(R.drawable.left_menu_head));
				ShowLoadingDialog();
			}
		};

		// 设置侧拉菜单listview 的点击监听
		lv_left_menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// currentPosition = position;
				// adapter.notifyDataSetChanged();
				// Intent intent;

				switch (position) {
				case 0:// 我的课程
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),
										MyCourseActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
							}
						});
						slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
				case 1:// 我的笔记
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),MyNoteActivity.class);
								//intent.putExtra("userId", userId);
								startActivity(intent);

								ShowLoadingDialog();
//								ShowUtils.showMsg(context, "该功能即将开放，敬请期待");
							}
						});
						slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
				case 2:// 我的收藏
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),MyCollectActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
								
							}
						});
						slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
				case 3:// 我的答疑
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),
										MyAnswerListActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
							}
						}) ;
						slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}

					break;
				case 4:// 我的代言
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),
										MyRepresentActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
							}
						});
						slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
				case 5:// 我的活动
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),
										MySportActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
							}
						});
						slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
				case 6:// 我的好友
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),
										MySchoolmateActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
							}
						}) ;
						slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
				
				case 7:// 我的钱包
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),
										MyWalletActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
								
							}
						});
							slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
					
				case 8:// 我的订单
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(),MyOrderActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
								
							}
						});
							slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;
				case 9:// 微商城
					intent = new Intent(getActivity(), MicroMallActivity.class);
					startActivity(intent);
					break;
				case 10:// 我的收货地址界面
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								intent = new Intent(getActivity(), MyReceivingAddressActivity.class);
								intent.putExtra("hasResultBack", false);
								startActivity(intent);
								ShowLoadingDialog();
							}
						});
						slidingMenu.toggle();
						ShowLoadingDialog();
					}else {
						UserStateUtil.NotLoginDialog(context);
					}
					break;

				case 11:// 我的站内信
					if (ISLOGIN) {
						UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
							
							@Override
							public void onSuccessCallBack() {
								//站内信我的消息界面
								intent = new Intent(getActivity(),MyMessageActivity.class);
								intent.putExtra("userId", userId);
								startActivity(intent);
								ShowLoadingDialog();
								/**
								 * 跳转到即时通讯，我的消息列表界面
								 * RongIM.getInstance().startConversationList(context);
								 */
							}
						}); 
							slidingMenu.toggle();
						ShowLoadingDialog();
					} else {
						UserStateUtil.NotLoginDialog(context);
					}

					break;
				default:
					break;
				}

				// 在此处要去调用，NewCenterPager对象中的switchPager（i）方法
				// 1，获取NewCenterPager对象
				// 2，NewCenterPager对象来至于HomeFragment在生成5个页面(向list结合中添加了5个对象)过程中
				// 3，HomeFragment对应对象获取出来？
				// 4，HomeFragment在MainActivity

				/*
				 * ((MainActivity) context).switchHomeFragment()
				 * .switchNewCenterPager().switchPager(position);
				 * slidingMenu.toggle();
				 */
			}
		});
		
		
		try {
			/**
			 * slidingMenu打开的时候的监听
			 */
			slidingMenu.setOnOpenListener(new OnOpenListener() {
				
				@Override
				public void onOpen() {

					ImageUtils.setImage(SharedPreferencesUtil.getInstance().spGetString("avatar"), iv_menu_head, R.drawable.left_menu_head);
					
					if (!StringUtil.isEmpty(SharedPreferencesUtil.getInstance().spGetString("name"))) {
					}
					if (!StringUtil.isEmpty(SharedPreferencesUtil.getInstance().spGetString("userInfo"))) {
						tv_menu_qianming.setText(SharedPreferencesUtil.getInstance().spGetString("userInfo"));
					}else{
						tv_menu_qianming.setText("真实地表达自己，让更多家人了解我...");
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	private void ShowLoadingDialog(){
		if (loadingDialog == null)
			loadingDialog = ShowUtils.createLoadingDialog(context, true);
		if (!loadingDialog.isShowing())
			loadingDialog.show();
		else
			loadingDialog.cancel();
	}
	/**
	 * 显示左侧 侧拉栏的listview的数据适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class MenuAdapter extends BaseAdapter {

		ViewHolder holder = null;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(getActivity(),
						R.layout.layout_left_menu_item, null);
				holder.iv_item = (ImageView) convertView
						.findViewById(R.id.iv_item);
				holder.tv_item = (TextView) convertView
						.findViewById(R.id.tv_item);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.iv_item.setImageResource(img[position]);
			holder.tv_item.setText(ownItem[position]);

			return convertView;

		}

		@Override
		public int getCount() {
			return ownItem.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	class ViewHolder {
		ImageView iv_item;
		TextView tv_item;
	}

	/**
	 * 在Fragment的生命周期onStart（）方法中，进行对用户名头像的设置。
	 */
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {

		ISLOGIN = UserStateUtil.isLogined();
		if (ISLOGIN) {
			// 取到用户名，ID，个性签名，头像
			userId = SharedPreferencesUtil.getInstance().spGetString("userId");
			userName = SharedPreferencesUtil.getInstance().spGetString("name");

			tv_menu_lode.setText(userName);
			tv_menu_qianming.setText(SharedPreferencesUtil.getInstance().spGetString("userInfo"));
			// 设置头像
			
			ImageUtils.setImage(SharedPreferencesUtil.getInstance().spGetString("avatar"), iv_menu_head, R.drawable.left_menu_head);

		} else {
			tv_menu_lode.setText("立即登录");
			tv_menu_qianming.setText("真实的表达自己，让更多的家人了解你");
		}

		super.onResume();
	}

	protected void initListener() {
		ll_setting.setOnClickListener(this);
		ll_shoppingcart.setOnClickListener(this);
		tv_menu_lode.setOnClickListener(this);
		iv_menu_head.setOnClickListener(this);
	}

	@Override
	public void initData() {
		initMenu();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_setting:
			intent = new Intent(getActivity(), SettingActivity.class);
			intent.putExtra("userId", userId);
			intent.putExtra("userName", userName);
			intent.putExtra("avatar", SharedPreferencesUtil.getInstance().spGetString("avatar"));
			startActivity(intent);
			slidingMenu.toggle();
			break;

		case R.id.ll_shoppingcart:
			if (ISLOGIN) {
				UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
					
					@Override
					public void onSuccessCallBack() {
						intent = new Intent(getActivity(),MyShoppingTrolleyActivity.class);
						startActivity(intent);
						ShowLoadingDialog();
					}
				});
				slidingMenu.toggle();
				ShowLoadingDialog();
			} else {
				UserStateUtil.NotLoginDialog(context);
			}
			
			break;
		case R.id.tv_menu_lode:
			if (ISLOGIN) {

			} else {
				intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.iv_menu_head:
			if (ISLOGIN) {
				UserStateUtil.loginInFailuer(context,mFailureCallBack,new SuccessCallBack() {
					
					@Override
					public void onSuccessCallBack() {
						intent = new Intent(getActivity(),UserSettingActivity.class);
						intent.putExtra("userId", userId);
						startActivity(intent);
						ShowLoadingDialog();
					}
				}) ;
				ShowLoadingDialog();
			} else {
				UserStateUtil.NotLoginDialog(context);
			}
			slidingMenu.toggle();
			break;

		default:
			break;
		}

	}

}
