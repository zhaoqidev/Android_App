package cc.upedu.online.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.UpdataAppBean;
import cc.upedu.online.interfaces.UploadDataCallBack;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.service.DownloadService;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UploadDataUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cc.upedu.online.view.CircleImageView;
/**
 * 侧拉菜单中的设置功能的界面
 * @author Administrator
 *
 */
public class SettingActivity extends TitleBaseActivity {
	//是否容许非WiFi下播放视频的box,是否自动播放视频的box,changeplay_cbtn
	private CheckBox wifi_cbtn,autoplay_cbtn;
	//用户名,返回,缓存大小
	private TextView user_name, tv_loadnum,tv_changeplay;
	private RelativeLayout ll_usersetting, ll_changepassword, ll_feedback, ll_about,ll_checkversion;
	//用户设置,清理缓存,修改密码,意见反馈,关于成长在线
	private LinearLayout ll_cleanload;
	//登录状态切换的按钮
	private Button bt_login;
	//缓存目录
	private File files;
	//缓存大小(字节)
	private long cacheSize;
	//转换完毕的缓存大小
	private String formetFileSize;
	//非WiFi状态下是否播放,是否自动播放,isCCPlay
	private boolean onlyWifiPlay = true, isAutoPlay;
	private Dialog dialog;
	private LayoutInflater inflater;
	private CircleImageView user_head;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("设置");
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		View view = View.inflate(context, R.layout.activity_setting, null);
		userId = getIntent().getStringExtra("userId");
		userName = getIntent().getStringExtra("userName");
		avatar = getIntent().getStringExtra("avatar");
		
		user_head = (CircleImageView) view.findViewById(R.id.user_head);
		user_name = (TextView) view.findViewById(R.id.user_name);
		tv_loadnum = (TextView) view.findViewById(R.id.tv_loadnum);
		wifi_cbtn = (CheckBox) view.findViewById(R.id.wifi_cbtn);
		autoplay_cbtn = (CheckBox) view.findViewById(R.id.autoplay_cbtn);
		tv_changeplay = (TextView) view.findViewById(R.id.tv_changeplay);
//		changeplay_cbtn = (CheckBox) findViewById(R.id.changeplay_cbtn);
		
		ll_usersetting = (RelativeLayout) view.findViewById(R.id.ll_usersetting);
		ll_cleanload = (LinearLayout) view.findViewById(R.id.ll_cleanload);
		ll_changepassword = (RelativeLayout) view.findViewById(R.id.ll_changepassword);
		ll_feedback = (RelativeLayout) view.findViewById(R.id.ll_feedback);
		ll_about = (RelativeLayout) view.findViewById(R.id.ll_about);
		ll_checkversion= (RelativeLayout) view.findViewById(R.id.ll_checkversion);
		bt_login = (Button) view.findViewById(R.id.bt_login);
		files = this.getCacheDir();
		//设置界面信息
		setData();
		return view;
	}
	private void setData() {
		//判断是否已经登录
		if (!UserStateUtil.isLogined()) {//未登录
			user_name.setText("未登录用户");
			bt_login.setText("立即登录");
			user_head.setImageResource(R.drawable.left_menu_head);
		}else {//已登录
			bt_login.setText("退出登录");
			userName = SharedPreferencesUtil.getInstance().spGetString("name");
			if (StringUtil.isEmpty(userName)) {
				user_name.setText("未设置");
			}else {
				user_name.setText(userName);
			}
			// 设置头像
			avatar = SharedPreferencesUtil.getInstance().spGetString("avatar");
			
			ImageUtils.setImage(SharedPreferencesUtil.getInstance().spGetString("avatar"), user_head, R.drawable.left_menu_head);
		}
	}
	protected void onResume() {
		setData();
		super.onResume();
	}
	
	private String userId;
	private String userName;
	private String avatar;
	@Override
	protected void initListener() {
		super.initListener();
		ll_usersetting.setOnClickListener(this);
		ll_cleanload.setOnClickListener(this);
		ll_changepassword.setOnClickListener(this);
		ll_feedback.setOnClickListener(this);
		ll_about.setOnClickListener(this);
		ll_checkversion.setOnClickListener(this);
		bt_login.setOnClickListener(this);
		
		wifi_cbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (wifi_cbtn.isChecked()) {
					onlyWifiPlay = true;
				} else {
					onlyWifiPlay = false;
				}
				SharedPreferencesUtil.getInstance().editPutBoolean("onlyWifiPlay", onlyWifiPlay);
			}
		});
		autoplay_cbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (autoplay_cbtn.isChecked()) {
					isAutoPlay = true;
				} else {
					isAutoPlay = false;
				}
				SharedPreferencesUtil.getInstance().editPutBoolean("isAutoPlay", isAutoPlay);
			}
		});
//		changeplay_cbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (changeplay_cbtn.isChecked()) {
//					isCCPlay = true;
//					tv_changeplay.setText("CC视频播放");
//				} else {
//					isCCPlay = false;
//					tv_changeplay.setText("乐视视频播放");
//				}
//				SharedPreferences.Editor editor = sp.edit();
//				editor.putBoolean("isCCPlay", isCCPlay);
//				editor.commit();
//			}
//		});
	}

	@Override
	protected void onStart() {
		setChockBoxDate();
		//设置缓存信息
		try {
			cacheSize = StringUtil.getFolderSize(files);
			formetFileSize = StringUtil.FormetFileSize(cacheSize);
			if (StringUtil.isEmpty(formetFileSize)) {
				tv_loadnum.setText("0K");
			}else {
				tv_loadnum.setText(formetFileSize);
			}
		} catch (Exception e) {
			ShowUtils.showMsg(context, "获取缓存信息失败!");
		}
		super.onStart();
	}
	/**
	 * 设置选择按钮的状态
	 */
	private void setChockBoxDate() {
		onlyWifiPlay = SharedPreferencesUtil.getInstance().spGetBoolean("onlyWifiPlay", true);
		isAutoPlay = SharedPreferencesUtil.getInstance().spGetBoolean("isAutoPlay");
//		isCCPlay = sp.getBoolean("isCCPlay", false);
		if (onlyWifiPlay) {
			wifi_cbtn.setChecked(true);
		} else {
			wifi_cbtn.setChecked(false);
		}
		if (isAutoPlay) {
			autoplay_cbtn.setChecked(true);
		} else {
			autoplay_cbtn.setChecked(false);
		}
//		if (isCCPlay) {
//			changeplay_cbtn.setChecked(true);
//		} else {
//			changeplay_cbtn.setChecked(false);
//		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub , , , , 
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_usersetting://用户设置
			//判断是否已经登录
			if (!UserStateUtil.isLogined()) {//未登录
				ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录.", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SharedPreferencesUtil.getInstance().editClear();
						user_name.setText("未登录用户");
						bt_login.setText("立即登录");
						user_head.setImageResource(R.drawable.left_menu_head);
						//跳转到登录界面
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
					}
				});
			}else {//已登录
				UserStateUtil.loginInFailuer(context,new FailureCallBack() {
					
					@Override
					public void onFailureCallBack() {
						ShowUtils.showDiaLog(context, "温馨提示", "用户登录已经过期,需要您重新登录", new ConfirmBackCall() {
							
							@Override
							public void confirmOperation() {
								//跳转到登录界面
								Intent intent = new Intent(context, LoginActivity.class);
								context.startActivity(intent);
							}
						});
					}
				}, new SuccessCallBack() {
					
					@Override
					public void onSuccessCallBack() {
						Intent userIntent=new Intent(context, UserSettingActivity.class);
						userIntent.putExtra("userId", userId);
						startActivity(userIntent);
					}
				});
			}
			break;
		case R.id.ll_cleanload://清理缓存
			if (StringUtil.isEmpty(formetFileSize)) {
				Toast.makeText(this, "无缓存数据", Toast.LENGTH_SHORT).show();
				return;
			} else {
				showDialog();
			}
			break;
		case R.id.ll_changepassword://修改密码
			//判断是否已经登录
			if (!UserStateUtil.isLogined()) {//未登录
				ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						//跳转到登录界面
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
					}
				});
			}else {//已登录
				UserStateUtil.loginInFailuer(context,new FailureCallBack() {
					
					@Override
					public void onFailureCallBack() {
						ShowUtils.showDiaLog(context, "温馨提示", "用户登录已经过期,需要您重新登录", new ConfirmBackCall() {
							
							@Override
							public void confirmOperation() {
								//跳转到登录界面
								Intent intent = new Intent(context, LoginActivity.class);
								context.startActivity(intent);
							}
						});
					}
				}, new SuccessCallBack() {
					
					@Override
					public void onSuccessCallBack() {
						Intent setPasswordIntent = new Intent(context, SetPasswordActivity.class);
						setPasswordIntent.putExtra("userId", userId);
						startActivity(setPasswordIntent);
					}
				});
			}
			break;
		case R.id.ll_feedback://意见反馈
			//进入到意见反馈界面
			startActivity(new Intent(this, FeedbackActivity.class));
			break;
		
		case R.id.ll_about://关于成长在线
			//进入到关于成长在线界面
			startActivity(new Intent(this, AboutActivity.class));
			break;

		case R.id.ll_checkversion://检查版本更新
			checkVersion();
				break;
		case R.id.bt_login://登录状态改变的button
			//判断是否已经登录
			if (!UserStateUtil.isLogined()) {//未登录
				//跳转到登录界面
				Intent intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
			}else {//已登录
				//退出登录
				Map<String, String> map = new HashMap<String, String>();
				map.put("userId", SharedPreferencesUtil.getInstance().spGetString("userId"));
				map.put("loginsid", SharedPreferencesUtil.getInstance().spGetString("loginsid"));
				map.put("brand", SharedPreferencesUtil.getInstance().spGetString("brand"));//手机品牌
				map.put("type", SharedPreferencesUtil.getInstance().spGetString("type"));//手机型号
				map.put("size", SharedPreferencesUtil.getInstance().spGetString("size"));//手机尺寸
				UploadDataUtil.getInstance().onUploadDataData(context,ConstantsOnline.APPLOG_OUT,map , new UploadDataCallBack() {

					@Override
					public void onUploadDataSuccess() {
						// TODO Auto-generated method stub
						ShowUtils.showMsg(context, "用户已成功退出");
						user_name.setText("未登录用户");
						bt_login.setText("立即登录");
						user_head.setImageResource(R.drawable.left_menu_head);
						SharedPreferencesUtil.getInstance().editClear();
					}

					@Override
					public void onUploadDataFailure() {
						// TODO Auto-generated method stub
						
					}
				});
			}
			break;
		}
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showDialog() {
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(this, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.show();
		Button btnsure = (Button) view.findViewById(R.id.dialogbtnsure);
		TextView textView = (TextView) view.findViewById(R.id.textmessage);
		textView.setText("缓存数据:"+formetFileSize);
		TextView titles = (TextView) view.findViewById(R.id.texttitles);
		titles.setText("确定清空?");
		btnsure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Runnable runnable = new Runnable() {

					@Override
					public void run() {
						try {
							Dialog pd = ShowUtils.createLoadingDialog(context, true);
							pd.setCancelable(false);
							pd.show();
							StringUtil.deleteFolderFile(files.getAbsolutePath(), true);
							// 执行线程
							try {
								cacheSize = StringUtil.getFolderSize(files);
								formetFileSize = StringUtil.FormetFileSize(cacheSize);
							} catch (Exception e) {
								ShowUtils.showMsg(context, "获取缓存信息失败!");
							}
							if (StringUtil.isEmpty(formetFileSize)) {
								tv_loadnum.setText("0K");
							}else {
								tv_loadnum.setText(formetFileSize);
							}
							StringUtil.buildDir(context, "upedu.cc");
							pd.dismiss();
						} catch (Exception e) {
							Log.e(BitmapUtils.class.getName(), "清除缓存出错！");
							e.printStackTrace();
						}
					}

				};
				runnable.run();
				dialog.cancel();
			}
		});
		Button btncancle = (Button) view.findViewById(R.id.dialogbtncancle);
		btncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}
	@Override
	protected void initData() {
	}



	private String downloadurl;//下载链接
	private String version;//版本名
	/**
	 * 连接服务器 检查版本号 是否有更新
	 */
	private void checkVersion() {
		//Message msg = Message.obtain();
		Map<String, String> requestDataMap = ParamsMapUtil.updataApp(context, "android");
		RequestVo requestVo=new RequestVo(ConstantsOnline.UPDATA_APP, this, requestDataMap, new MyBaseParser<>(UpdataAppBean.class));
		try {
			version = getPackageManager().getPackageInfo(getPackageName(),0).versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		DataCallBack<UpdataAppBean> callBack=new DataCallBack<UpdataAppBean>() {

			@Override
			public void processData(UpdataAppBean object) {
				if (object!=null) {
					if (object.entity!=null) {
						if ("android".equals(object.entity.kType)) {
							if (!StringUtil.isEmpty(version)){
								if (version.equals(object.entity.versionNo)) {
									ShowUtils.showMsg(context,"您的成长吧已经是最新版本");
								}else {
									downloadurl=object.entity.downloadUrl;
									ShowUtils.showDiaLog_sure(context, "温馨提醒", "发现新版本，请点击确定按钮开始更新", new ConfirmBackCall() {
										@Override
										public void confirmOperation() {
											download(downloadurl);
										}
									});
								}
							}else{
								ShowUtils.showMsg(context, "数据解析失败，请稍后重试");
							}
						}
					}else {
						ShowUtils.showMsg(context, "数据解析失败，请将该问题反馈给客服");
					}
				}else {
					ShowUtils.showMsg(context, "数据请求失败，请检查您的网络");
				}

			}
		};

		getDataServer(requestVo, callBack);

	}

	/**
	 * 多线程的下载器
	 *
	 * @param downloadurl
	 */
	private void download(String downloadurl) {
		//从服务器下载APP更新
		ShowUtils.showMsg(context, "开始下载");
		Intent intent = new Intent(context,DownloadService.class);		// 开启更新服务UpdateService
		String appname ="成长吧"+version;
		intent.putExtra("appname", appname);
		intent.putExtra("url", downloadurl);
		context.startService(intent);
		//loadMainUI();
	}

}
