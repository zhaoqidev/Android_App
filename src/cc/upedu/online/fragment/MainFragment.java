package cc.upedu.online.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.MainActivity;
import cc.upedu.online.R;
import cc.upedu.online.activity.LoginActivity;
import cc.upedu.online.activity.SchoolmatePeerActivity;
import cc.upedu.online.activity.SportIssueActivity;
import cc.upedu.online.activity.UserLearningRecordsActivity;
import cc.upedu.online.adapter.DownMenuAdapter;
import cc.upedu.online.adapter.SearchRecordAdapter;
import cc.upedu.online.base.BaseFragment;
import cc.upedu.online.domin.DawnMenuBean;
import cc.upedu.online.domin.DawnMenuBean.Entity;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.GetUserInforUtil;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cc.upedu.online.view.SearchListData;
import cc.upedu.online.view.citychoose.CityChooseActity;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongIM.UserInfoProvider;
import io.rong.imlib.model.UserInfo;

/**
 * 主activity中的主fragment
 * @author hui
 *
 */
public class MainFragment extends BaseFragment implements OnClickListener,UserInfoProvider {
	public static final String tag = "HomeFragment";
	/**
	 * title的view
	 */
	private View titleBar;
	/**
	 * title的父布局
	 */
	@ViewInject(R.id.fl_title)
	private LinearLayout fl_title;
	//按钮组控件显示的内容布局
	@ViewInject(R.id.layout_content)
	private FrameLayout layout_content;
	//下方按钮组中文字
//	@ViewInject(R.id.home_textView)
//	private TextView home_textView;
	@ViewInject(R.id.course_textView)
	private TextView course_textView;
	@ViewInject(R.id.discpver_textView)
	private TextView discpver_textView;
	@ViewInject(R.id.telecast_TextView)
	private TextView telecast_TextView;
	//下方按钮组中的图片
//	@ViewInject(R.id.rb_tab1)
//	private RadioButton rb_tab1;
	@ViewInject(R.id.rb_tab2)
	private RadioButton rb_tab2;
	@ViewInject(R.id.rb_tab3)
	private RadioButton rb_tab3;
	@ViewInject(R.id.rb_tab4)
	private RadioButton rb_tab4;
	@ViewInject(R.id.rb_tab5)
	private RadioButton rb_tab5;
	//下方按钮组中的布局
//	@ViewInject(R.id.layout_1)
//	private LinearLayout layout_1;
	@ViewInject(R.id.layout_2)
	private LinearLayout layout_2;
	@ViewInject(R.id.layout_3)
	private LinearLayout layout_3;
	@ViewInject(R.id.layout_4)
	private LinearLayout layout_4;
	@ViewInject(R.id.layout_5)
	private LinearLayout layout_5;
	/** 下拉列表的布局 */
	@ViewInject(R.id.ll_down_menu)
	private LinearLayout ll_down_menu;
	/** 下拉列表中的ListView */
	@ViewInject(R.id.lv_down_menu)
	private ListView lv_down_menu;
	/** 搜索记录的布局 */
	@ViewInject(R.id.ll_search_record)
	private LinearLayout ll_search_record;
	/** 搜索记录中的ListView */
	@ViewInject(R.id.lv_search_record)
	private ListView lv_search_record;
	/**
	 * 下方按钮组控制显示的fragment的集合
	 */
	private List<BaseFragment> fragmentList = new ArrayList<BaseFragment>();
	/**
	 * title显示文本的布局
	 */
	private LinearLayout rl_main_text;
	/**
	 * 标题栏中间得标题
	 */
	private TextView tv_main_title;
	/**
	 * 标题栏中间得标题右侧的箭头
	 */
	private ImageView txt_image;
	/**
	 * 标题左侧显示隐藏侧拉栏的图片按钮
	 */
	private ImageButton imgbtn_menu;
	/**
	 * 标题右侧显示学习记录的图片按钮
	 */
	private ImageButton imgbtn_recode;
	/**
	 * 标题右侧弹出搜索框的图片按钮
	 */
	private ImageButton btn_search;
	/**
	 * 用于子界面切换的fragment管理器
	 */
	private FragmentManager mFragmentMan;
	/**
	 * 当前显示的fragment
	 */
	private BaseFragment currentFragment = null;
	/**
	 * 搜索的布局
	 */
	private RelativeLayout search_bar;
	/**
	 * 搜索框的显示状态
	 */
	private boolean isShowSearchBar = false;
	/**
	 * 搜索栏中搜索的关键字
	 */
	private EditText search_text;
	/**
	 * 搜索功能按钮
	 */
	private ImageButton btn_search_two;
	/**
	 * 关闭搜索框的
	 */
	private LinearLayout backsearch;
	/**
	 * 搜索框的动画
	 */
	private TranslateAnimation mTranslateAnimation;
	/**
	 * 下拉列表中listview的适配器
	 */
	private DownMenuAdapter mDownMenuAdapter;
	/** 搜索记录的集合 */
	private List<String> searchRecordList;
	/** 搜索记录列表的适配器 */
	private SearchRecordAdapter mSearchRecordAdapter;
	/**
	 * 记录课程中显示的是那个分类的课程列表,  默认值"-1"表示默认显示的是所有课程
	 */
	private String currentCourseSubjectId = "-1";
	/**
	 * 记录课程中显示的是那个分类的导师列表,  默认值"-1"表示默认显示的是所有导师
	 */
	private String currentTeacherSubjectId = "-1";
	/**
	 * 弹出的下拉列表的数据
	 */
	private List<Entity> dawnMenuList = new ArrayList<DawnMenuBean.Entity>();
	/**  保存下拉(课程分类)列表对象中对应的ID和分类名称的map集合 */
	private Map<String, String> dawnMenuMap = new HashMap<String, String>();
	/**向上的动画*/
    private Animation mRotateUpAnim;
    /**向下的动画*/
    private Animation mRotateDownAnim;
    /** 旋转动画时间 */
    private static final int ROTATE_ANIM_DURATION = 150;
//	private boolean isSearch;
	private boolean hasPopupWindow = false;
	private SlidingMenu slidingMenu;
	View view;
    public MainFragment() {
    	super();
	}
	
	private Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0://下拉列表中数据请求json成功转换成了对象
				//判断对象中的数据是否正确
				if ("true".equals(mDawnMenu.getSuccess()) ) {//正确
					//清除原有数据,加载新数据
					dawnMenuList.clear();
					dawnMenuList.addAll(mDawnMenu.getEntity());
					
					getDawnMenuMap();
					
					if (mDownMenuAdapter == null) {
						mDownMenuAdapter = new DownMenuAdapter(context,dawnMenuList);
						//判断当前页面显示的内容是子界面中的课程还是发现
						if (current == COURSE) {//课程
							mDownMenuAdapter.setType(true);
						}else if (current == DISCPVER) {//发现
							mDownMenuAdapter.setType(false);
							boolean isSearch = false;
							switch (currentDiscpverChild) {
							case 0:
								isSearch = isSearchTeacher;
								break;
							case 1:
								isSearch = isSearchSchoolmate;
								break;
							case 2:
								isSearch = isSearchSport;
								break;
							}
							mDownMenuAdapter.setSearch(isSearch);
						}
						lv_down_menu.setAdapter(mDownMenuAdapter);
					} else {
						if (current == COURSE) {//课程
							mDownMenuAdapter.setType(true);
						}else if (current == DISCPVER) {//发现
							mDownMenuAdapter.setType(false);
							boolean isSearch = false;
							switch (currentDiscpverChild) {
							case 0:
								isSearch = isSearchTeacher;
								break;
							case 1:
								isSearch = isSearchSchoolmate;
								break;
							case 2:
								isSearch = isSearchSport;
								break;
							}
							mDownMenuAdapter.setSearch(isSearch);
						}
						mDownMenuAdapter.notifyDataSetChanged();
					}
					//设置条目的监听数据
					lv_down_menu.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							backDownMenu();
							
							//判断当前页是显示的那个子界面fragment
							if (current == COURSE) {//课程
								//根据条目位置获取当前分类ID
								if (position == 0) {
									currentCourseSubjectId = "-1";
									tv_main_title.setText("所有课程");
								}else {
									currentCourseSubjectId = dawnMenuList.get(position-1).getSubjectId();
								}
								//把分类Id传给子界面并调用initNewData请求数据
								((CourseFragment)fragmentList.get(current)).setSubjectId(currentCourseSubjectId);
								((CourseFragment)fragmentList.get(current)).initNewData("downmenu_courseSubjectId");
								
							}else if (current == DISCPVER) {//发现
								isSearchTeacher = false;
								if (position == 0) {
									currentTeacherSubjectId = "-1";
									tv_main_title.setText("所有导师");
								}else {
									currentTeacherSubjectId = dawnMenuList.get(position-1).getSubjectId();
									if (dawnMenuMap.get(currentTeacherSubjectId) != null) {
										tv_main_title.setText(dawnMenuMap.get(currentTeacherSubjectId));
									}else {
										tv_main_title.setText("所有导师");
									}
								}
								SharedPreferencesUtil.getInstance().editPutString("downmenu_teacherSubjectId", currentTeacherSubjectId);
								((DaoshiFragment)(getFragmentList().get(0))).setSubjectId(currentTeacherSubjectId);
								((DaoshiFragment)(getFragmentList().get(0))).initNewData("downmenu_teacherSubjectId");
							}
						}
					});
				}else {//错误,显示错误信息
					ShowUtils.showMsg(context, mDawnMenu.getMessage());
				}
				break;
			}
		}
	};
	/**
	 * 把下拉(课程分类)列表对象中对应的ID和分类名称保存到map集合(dawnMenuMap)中
	 */
	private void getDawnMenuMap() {
		dawnMenuMap.clear();
		instance.submit(new Runnable() {
			@Override
			public void run() {
				for (Entity entity : dawnMenuList) {
					dawnMenuMap.put(entity.getSubjectId(), entity.getSubjectName());
				}
			}
		});
	}
	@Override
	public View initView(LayoutInflater inflater) {
		
		view = inflater.inflate(R.layout.activity_main, null);
		ViewUtils.inject(this, view); // xutil包的api，从指定的view上找控件
		//初始化title,并添加到主布局中
		
		/**
		 * 向融云服务器传递用户信息：用户id，用户名，头像
		 */
		RongIM.setUserInfoProvider(this,true);
		Log.i("token", "开始监听");
		
		titleBar = inflater.inflate(R.layout.layout_title_bar, null);
		imgbtn_menu = (ImageButton) titleBar.findViewById(R.id.imgbtn_left);
		rl_main_text = (LinearLayout) titleBar.findViewById(R.id.rl_main_text);
		tv_main_title = (TextView) titleBar.findViewById(R.id.tv_main_title);
		txt_image = (ImageView) titleBar.findViewById(R.id.txt_image);
		imgbtn_recode = (ImageButton) titleBar.findViewById(R.id.imgbtn_recode);
		btn_search = (ImageButton) titleBar.findViewById(R.id.btn_search);
		//搜索栏
		search_bar = (RelativeLayout) titleBar.findViewById(R.id.search_bar);
		backsearch = (LinearLayout) titleBar.findViewById(R.id.backsearch);
		search_text = (EditText) titleBar.findViewById(R.id.search_text);
		onSearchtext();
		btn_search.setVisibility(View.GONE);
		btn_search_two = (ImageButton) titleBar.findViewById(R.id.btn_search_two);
		//创建动画
		mTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		mTranslateAnimation.setDuration(300);
		float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
        float toDegree = -180f;     // SUPPRESS CHECKSTYLE
        // 初始化旋转动�?
        mRotateUpAnim = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(toDegree, 0.0f, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
		
		fl_title.addView(titleBar);
		
//		fragmentList.add(new HomeFragment());//首页
//		fragmentList.add(new CourseFragment());课程
		fragmentList.add(new CourseFragment(MainFragment.this));//新课程
		fragmentList.add(new DiscpverFragment());//发现
		fragmentList.add(new TelecastFragment(context));//直播
		
		//注册广播接受者
		myTitleRecive = new MyTitleRecive();
		IntentFilter intentFilter = new IntentFilter();
//		intentFilter.addAction("ZhikuPager");
		intentFilter.addAction("DaoshiPager");
		intentFilter.addAction("XueyouPager");
		intentFilter.addAction("HuodongPager");
		intentFilter.addAction("QuanbuXueyou");
		intentFilter.addAction("TongchengXueyou");
		intentFilter.addAction("HangyeXueyou");
		intentFilter.addAction("QitaXueyou");
		intentFilter.addAction("SuoyouHuodong");
		intentFilter.addAction("TongchengHuodong");
		intentFilter.addAction("QitaHuodong");
		intentFilter.addAction("newMsg");//监听是否有新的融云消息
		context.registerReceiver(myTitleRecive, intentFilter);
//		context.registerReceiver(myTitleRecive, intentFilter, "cc.upedu.online.titlename", null);
		
		if (slidingMenu == null) {
			slidingMenu = ((MainActivity) context).getSlidingMenu();
		}
		
		initListener();
		
		//初始化内容显示
		switchContent(currentFragment, fragmentList.get(0));
		
		//显示箭头图片
		txt_image.clearAnimation();
		txt_image.setVisibility(View.VISIBLE);
		tv_main_title.setText("所有课程");
		SharedPreferencesUtil.getInstance().editPutString("downmenu_courseSubjectId", "-1");
		
		return view;
	}
	private String currentCondition;
	/**
	 * 
	 */
	private void onSearchtext() {
		TextWatcher textWatcher = new TextWatcher() {

		    @Override
		    public void afterTextChanged(Editable s) {
		      Log.d("TAG", "afterTextChanged--------------->");
		    }

		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		      Log.d("TAG", "beforeTextChanged--------------->");
		    }

		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		    	Log.d("TAG", "onTextChanged--------------->");
		    	String str = search_text.getText().toString().trim();
//				if (str != null && str.length() > 0)
//					cancelBtn.setVisibility(View.VISIBLE);
//				else
//					cancelBtn.setVisibility(View.GONE);
			}
		  };
		  search_text.addTextChangedListener(textWatcher);
		  lv_search_record.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
				// 如果是全部
				if (position == 0) {
					currentCondition = "查找全部";
				}else {
					currentCondition = searchRecordList.get(position-1);
				}
				  search_text.setText(currentCondition);
				  // 隐藏键盘
				  InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				  inputMethodManager.hideSoftInputFromWindow(
						  search_text.getWindowToken(), 0);
			  }
		});
		// 添加触摸事件
		  search_text.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ll_search_record.setVisibility(View.VISIBLE);
				if (mSearchRecordAdapter == null) {
	        		mSearchRecordAdapter = new SearchRecordAdapter(context, searchRecordList);
	        		lv_search_record.setAdapter(mSearchRecordAdapter);
				}else {
					mSearchRecordAdapter.notifyDataSetChanged();
				}
				return false;
			}
		});
		// 添加编辑事件
		  search_text.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					if(actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_UNSPECIFIED|| actionId == EditorInfo.IME_ACTION_NONE || actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_SEND || actionId == EditorInfo.IME_ACTION_NEXT || actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_PREVIOUS){
						searchData();
						
						return true;
					}else{
						return false;
					}
				}
			});
//			search_text.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
//	
//				@Override  
//			    public void onFocusChange(View v, boolean hasFocus) {  
//			        if(hasFocus) {
//			        	ll_search_record.setVisibility(View.VISIBLE);
//						if (mSearchRecordAdapter == null) {
//			        		mSearchRecordAdapter = new SearchRecordAdapter(context, searchRecordList);
//			        		lv_search_record.setAdapter(mSearchRecordAdapter);
//						}else {
//							
//						}
//					} else {
//					}
//			    }
//			});
	}
	/**
	 * 选择课程分类之后修改title的方法
	 * @param subjectId 所选择的分类id
	 */
	public void setTitleText(String subjectId) {
		if ("-1".equals(subjectId)) {
			tv_main_title.setText("所有课程");
		}else {
			tv_main_title.setText(dawnMenuMap.get(subjectId));
			SharedPreferencesUtil.getInstance().editPutString("downmenu_courseSubjectId", subjectId);
		}
	}
	/**
	 * 判断搜索栏是否是显示的
	 * @return	true 表示搜索栏是显示状态
	 * 			false 表示搜索栏是隐藏状态
	 */
	public boolean isShowSearchBar() {
		return isShowSearchBar;
	}
	/**
	 * 设置搜索栏的显示状态
	 * @param isShowSearchBar 	true 表示搜索栏是显示状态
	 * 							false 表示搜索栏是隐藏状态
	 */
	public void setShowSearchBar(boolean isShowSearchBar) {
		this.isShowSearchBar = isShowSearchBar;
	}
	/**
	 * 判断下拉列表是否是显示的
	 * @return	true 表示下拉列表是显示状态
	 * 			false 表示下拉列表是隐藏状态
	 */
	public boolean isDownMenu() {
		return isDownMenu;
	}
	/**
	 * 判断下拉列表是否是显示的
	 * @return	true 表示下拉列表是显示状态
	 * 			false 表示下拉列表是隐藏状态
	 */
	public boolean isPopupWindow() {
		if ((matePop != null && matePop.isShowing()) || (sportPop != null && sportPop.isShowing())) {
			return true;
		}else {
			return false;
		}
	}
	public void backPopupWindow() {
		// TODO Auto-generated method stub
		if (matePop != null && matePop.isShowing()) {
			matePop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
		}
		if (sportPop != null && sportPop.isShowing()) {
			sportPop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
		}
	}
	/**
	 * 设置下拉列表的显示状态
	 * @param isDownMenu 	true 表示下拉列表是显示状态
	 * 							false 表示下拉列表是隐藏状态
	 */
	public void setDownMenu(boolean isDownMenu) {
		this.isDownMenu = isDownMenu;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取fragment的管理器
		mFragmentMan = getFragmentManager();
	}
	//课程和发现界面title文本点击显示的下拉列表的显示状态
	private boolean isDownMenu = false;
	/**
	 * 当前页面的位置 HONE 主页子界面
	 */
//	private final int HONE = 0;
	/**
	 * 当前页面的位置 COURSE 课程子界面
	 */
	private final int COURSE = 0;
	/**
	 * 当前页面的位置 DISCPVER 发现子界面
	 */
	private final int DISCPVER = 1;
	/**
	 * 当前页面的位置 TELECAST 直播子界面
	 */
	private final int TELECAST = 2;
	/**
	 * 记录当前页面所显示的子界面的位置
	 */
	private int current = 0;
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imgbtn_left://控制侧拉菜单
			//点击按钮拖拽，隐藏侧拉菜单
			slidingMenu.toggle();
			break;
		case R.id.rl_main_text://title文本的点击事件
			txt_image.clearAnimation();
			if (!hasPopupWindow) {
				if (isDownMenu) {
					ll_down_menu.setVisibility(View.GONE);
					txt_image.startAnimation(mRotateDownAnim);
				}else {
					ll_down_menu.setVisibility(View.VISIBLE);
					txt_image.startAnimation(mRotateUpAnim);
					initDownMenu();
				}
				isDownMenu = !isDownMenu;
			}else {
				if (currentDiscpverChild == 1) {
					if (matePop != null && matePop.isShowing()) {
						txt_image.startAnimation(mRotateDownAnim);
						matePop.dismiss();
					}else {
						txt_image.startAnimation(mRotateUpAnim);
						//弹出学友的PopupWindow
						shoolmatePopupWindow();
					}
				}else {
					if (sportPop != null && sportPop.isShowing()) {
						txt_image.startAnimation(mRotateDownAnim);
						sportPop.dismiss();
					}else {
						txt_image.startAnimation(mRotateUpAnim);
						//弹出活动的PopupWindow
						sportPopWindow();
					}
				}
			}
			break;
		case R.id.imgbtn_recode://历史记录
			//判断是否已经登录
			if (!UserStateUtil.isLogined()) {//未登录
				ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录.", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						//跳转到登录界面
						Intent intent = new Intent(context, LoginActivity.class);
						startActivity(intent);
					}
				});
			}else {//已登录
				//判断登录是否过期kpointId
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
						Intent intent = new Intent(context, UserLearningRecordsActivity.class);
						startActivity(intent);
					}
				});
			}
			break;
		case R.id.btn_search://弹出搜索框的按钮
			search_bar.setVisibility(View.VISIBLE);
			search_text.setText("");
			search_bar.startAnimation(mTranslateAnimation);
			//判断当前界面显示的是mainactivity中子界面中的哪一个,以此来决定搜索的内容
			switch (current) {
			case COURSE://课程子界面
				//搜索课程
				search_text.setHint("查找您感兴趣的课程");
				initSearchRecord("CourseSearchRecordList");
				break;
			case DISCPVER://发现子界面
				//判断发现子界面中的二级子界面显示的是哪一个,以此来决定搜索的内容
				switch (currentDiscpverChild) {
				case 0:
					//搜索导师
					search_text.setHint("查找您感兴趣的导师");
					initSearchRecord("TeacherSearchRecordList");
					break;
				case 1:
					//搜索学友
					search_text.setHint("查找您感兴趣的学友");
					initSearchRecord("SchoolmateSearchRecordList");
					break;
				case 2:
					//搜索活动
					search_text.setHint("查找您感兴趣的活动");
					initSearchRecord("SportSearchRecordList");
					break;
				}
				break;
			case TELECAST://直播子界面
				//搜索直播课程
				search_text.setHint("查找您感兴趣的直播课程");
				initSearchRecord("LiveCourseSearchRecordList");
				break;
			}
			isShowSearchBar = !isShowSearchBar;
			backDownMenu();
			break;
		case R.id.backsearch://关闭搜索框的功能按钮
			// 隐藏键盘
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager != null) {
				inputMethodManager.hideSoftInputFromWindow(
						search_text.getWindowToken(), 0);
			}
			ll_search_record.setVisibility(View.GONE);
			backSearch();
			break;
		case R.id.btn_search_two://搜索功能按钮
			searchData();
			break;
//		case R.id.layout_1://显示首页
//		case R.id.rb_tab1:
//			backSearch();
//			backDownMenu();
//			if (current != HONE) {
//				initHome();
//				current = HONE;
//			}
//			break;
		case R.id.layout_2://显示课程
		case R.id.rb_tab2:
			backSearch();
			backDownMenu();
			if (current != COURSE) {
				hasPopupWindow = false;
				initCourse();
				showSearchbotton(false);
				current = COURSE;
			}
			break;
		case R.id.layout_3://显示发现
		case R.id.rb_tab3:
			backSearch();
			backDownMenu();
			if (current != DISCPVER) {
				initDiscpver();
				showSearchbotton(true);
				current = DISCPVER;
				if (currentDiscpverChild != 0) {
					hasPopupWindow = true;
				}
			}
			break;
		case R.id.layout_4://显示直播
		case R.id.rb_tab4:
			backSearch();
			backDownMenu();
			if (current != TELECAST) {
				hasPopupWindow = false;
				initTelecast();
				showSearchbotton(false);
				current = TELECAST;
			}
			break;
		case R.id.layout_5://跳转到即时通讯对话列表页面
		case R.id.rb_tab5:
			backSearch();
			backDownMenu();
			RongIM.getInstance().startConversationList(context);
			SharedPreferencesUtil.getInstance().editPutBoolean("newMsg", false);
			rb_tab5.setBackgroundResource(R.drawable.iconfont_message_hui);
			break;
//////////////////////////////////////////////////////////////////////发现页面的点击事件			
		case R.id.ll_mate_one:
			// 切换到全部学友
			matePop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			
			((SchoolmateFragment) getFragmentList().get(1)).setExtraData("");
			((SchoolmateFragment) getFragmentList().get(1)).switchPager(0,false);
			
			isSearchSchoolmate = false;
			break;
		case R.id.ll_mate_two:
			// 切换到同城学友
			matePop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			if (UserStateUtil.isLogined()) {
				((SchoolmateFragment) getFragmentList().get(1)).switchPager(1,false);
			}else {
				UserStateUtil.NotLoginDialog(context);
			}
			isSearchSchoolmate = false;
			break;
			
		case R.id.ll_mate_three:
			// 打开同行学友的筛选页面
			matePop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			if (UserStateUtil.isLogined()) {
				startActivityForResult(new Intent(getActivity(),SchoolmatePeerActivity.class), INUDSTRY);
//				schoolMate[0] = "peer";
//				schoolMate[1] = inudstry;
				
			}else {
				UserStateUtil.NotLoginDialog(context);
			}
			
			break;
			
		case R.id.ll_mate_four:
			// 打开其他学友的筛选页面
			matePop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			if (UserStateUtil.isLogined()) {
				intent = new Intent(context, CityChooseActity.class);
				intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_ONE);
				startActivityForResult(intent, CityChooseActity.CHOOSE_ONE);
			}else {
				UserStateUtil.NotLoginDialog(context);
			}
			
			break;

		case R.id.ll_one://全部活动
			sportPop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			((SportFragment) getFragmentList().get(2)).setExtraData("");
			((SportFragment) getFragmentList().get(2)).switchPager(0,false);
			
			isSearchSport = false;
			break;
		case R.id.ll_two://同城活动
			sportPop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			if (UserStateUtil.isLogined()) {
				((SportFragment) getFragmentList().get(2)).setExtraData("");
				((SportFragment) getFragmentList().get(2)).switchPager(1,false);
				isSearchSport = false;
			}else {
				UserStateUtil.NotLoginDialog(context);
			}
			break;
		case R.id.ll_three://按地区筛选
			sportPop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			if (UserStateUtil.isLogined()) {
				intent = new Intent(context, CityChooseActity.class);
				intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_THREE);
				startActivityForResult(intent, CityChooseActity.CHOOSE_THREE);
				//((SportPager) pagerList.get(2)).switchPager(1);
			}else {
				UserStateUtil.NotLoginDialog(context);
			}
			break;

		case R.id.ll_four://发起活动
			sportPop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			if (UserStateUtil.isLogined()) {
				Intent intent =new Intent(context,SportIssueActivity.class);
				context.startActivity(intent);
			}else {
				UserStateUtil.NotLoginDialog(context);
			}
			break;
		case R.id.ll_schoolmate_blank:
			matePop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
			break;
		case R.id.ll_sport_blank:
			sportPop.dismiss();
			txt_image.startAnimation(mRotateDownAnim);
		}
	}
	private List<BaseFragment> getFragmentList(){
		if (discpverFragmentList == null) {
			discpverFragmentList = ((DiscpverFragment)fragmentList.get(current)).getFragmentList();
		}
		return discpverFragmentList;
	}
	private boolean isSearchCourse = false;
	private boolean isSearchTeacher = false;
	private boolean isSearchSchoolmate = false;
	private boolean isSearchSport = false;
	private boolean isSearchTelecast = false;
	/**
	 * 
	 */
	private void searchData() {
		//获取搜索的内容,并做非空判断
		String searchText = search_text.getText().toString().toString().trim();
		if (!StringUtil.isEmpty(searchText)) {
			//去除中间的空格
			searchText = searchText.replaceAll("\\s*", "");
			//判断当前界面显示的是mainactivity中子界面中的哪一个,以此来决定搜索的内容
			switch (current) {
			case COURSE://课程子界面
				//搜索课程
				saveSearchText(searchText,"CourseSearchRecordList");
				((CourseFragment)(fragmentList.get(current))).seacherCourse(searchText);
				isSearchCourse = true;
				break;
			case DISCPVER://发现子界面
				//判断发现子界面中的二级子界面显示的是哪一个,以此来决定搜索的内容
				switch (currentDiscpverChild) {
				case 0:
					//搜索导师
					saveSearchText(searchText,"TeacherSearchRecordList");
					((DaoshiFragment)(getFragmentList().get(currentDiscpverChild))).seacherDaoshi(searchText);
					if ("查找全部".equals(searchText)) {
						isSearchTeacher = false;
						currentTeacherSubjectId = "-1";
						tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
					}else {
						tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
						isSearchTeacher = true;
					}
					break;
				case 1:
					//搜索学友
					saveSearchText(searchText,"SchoolmateSearchRecordList");
					((SchoolmateFragment)(getFragmentList().get(currentDiscpverChild))).seacherSchoolmate(searchText);
					if ("查找全部".equals(searchText)) {
						isSearchSchoolmate = false;
						tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
					}else {
						tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
						isSearchSchoolmate = true;
					}
					break;
				case 2:
					//搜索活动
					saveSearchText(searchText,"SportSearchRecordList");
					((SportFragment)(getFragmentList().get(currentDiscpverChild))).seacherSport(searchText);
					if ("查找全部".equals(searchText)) {
						isSearchSport = false;
						tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
					}else {
						tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
						isSearchSport = true;
					}
					break;
				}
				break;
			case TELECAST://直播子界面
				//搜索直播课程
				saveSearchText(searchText,"LiveCourseSearchRecordList");
				((TelecastFragment)(fragmentList.get(current))).seacherTelecast(searchText);
				isSearchTelecast = true;
				break;
			}
			// 隐藏键盘
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					search_text.getWindowToken(), 0);
			backSearch();
		}else {
			ShowUtils.showMsg(context, "搜索内容不能为空!");
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		backSearch();
		backDownMenu();
	}
	/**
	 * 退出搜索栏的方法
	 */
	public void backSearch(){
		if (isShowSearchBar) {
			search_bar.setVisibility(View.GONE);
			isShowSearchBar = !isShowSearchBar;
			ll_search_record.setVisibility(View.GONE);
		}
	}
	/**
	 * 收起下拉菜单的方法
	 */
	public void backDownMenu(){
		if (isDownMenu) {
			ll_down_menu.setVisibility(View.GONE);
			txt_image.clearAnimation();
//			txt_image.setImageResource(R.drawable.xiajiantou);
			isDownMenu = !isDownMenu;
		}
	}
	/**
	 * 下拉(分类)列表的Javabean对象
	 */
	private DawnMenuBean mDawnMenu;
	/**
	 * 接受发现fragment中pager切换的广播
	 */
	private MyTitleRecive myTitleRecive;
	/**
	 * 发现子界面的title名称集合
	 */
	private List<String> discpverChildTitles;
	/**
	 * 发现子界面搜索状态的title名称集合
	 */
	private List<String> discpverChildSearchTitles;
	/**
	 * 学友子界面的title名称集合
	 */
	private List<String> xueyouChildTitles;
	/**
	 * 活动子界面的title名称集合
	 */
	private List<String> huodongChildTitles;
	/**
	 * 初始化下拉列表中请求数据的方法
	 */
	private void initDownMenu() {
		Map<String, String> requestDataMap = ParamsMapUtil.getDownMenu(context, "3", "1");
		RequestVo requestVo = new RequestVo(ConstantsOnline.HOME_ARCHITECTURE, context, requestDataMap, new MyBaseParser<>(DawnMenuBean.class));
		DataCallBack<DawnMenuBean> dawnMenuCallBack = new DataCallBack<DawnMenuBean>() {

			@Override
			public void processData(DawnMenuBean object) {
				if (object==null) {
					ShowUtils.showMsg(context, "获取下拉列表数据失败请检查网络");
				}else {
					mDawnMenu = object;
					handler.obtainMessage(0).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, dawnMenuCallBack);
	}
	/**
	 * 初始化搜索记录列表中请求数据的方法
	 */
	private void initSearchRecord(String searchRecordListString){
		List<String> temlist = (List<String>) PreferencesObjectUtil.readObject(searchRecordListString, context);
		if (searchRecordList == null) {
			searchRecordList = new ArrayList<String>();
		}else {
			searchRecordList.clear();
		}
		if (temlist != null && temlist.size() > 0) {
			searchRecordList.addAll(temlist);
		}
	}
	/**
	 * 把新的搜索记录保存到集合中
	 * @param searchText
	 */
	private void saveSearchText(String searchText,String searchRecordListString){
		if (!"查找全部".equals(searchText)) {
			if (taskSearchListData == null) {
				taskSearchListData = new SearchListData<String>();
			}else {
				taskSearchListData.clearAll();
			}
			taskSearchListData.addAllObject(searchRecordList);
			taskSearchListData.addElement(searchText);
			searchRecordList.clear();
			searchRecordList.addAll(taskSearchListData.getStringList());
			PreferencesObjectUtil.saveObject(searchRecordList, searchRecordListString, context);
		}
	}
	/**
	 * 初始化直播界面,并完成状态改变
	 */
	private void initTelecast() {
		setTitleClickState(false,false,View.GONE);
		tv_main_title.setText("直播");
		setunderButtonState(false,false,false,true);
		//切换子界面
		switchContent(currentFragment, fragmentList.get(2));
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	/**
	 * @throws NotFoundException
	 */
	private void setunderButtonState(boolean tab1,boolean tab2,boolean tab3,boolean tab4){
		//下方按钮组的状态切换
//		rb_tab1.setChecked(false);
		rb_tab2.setChecked(tab2);
		rb_tab3.setChecked(tab3);
		rb_tab4.setChecked(tab4);
//		if (tab1) {
//			home_textView.setTextColor(getResources().getColor(
//					R.color.tab_choice_color));
//		}else {
//			home_textView.setTextColor(getResources().getColor(
//					R.color.tab_color));
//		}
		if (tab2) {
			course_textView.setTextColor(getResources().getColor(
					R.color.tab_choice_color));
		}else {
			course_textView.setTextColor(getResources().getColor(
					R.color.tab_color));
		}
		if (tab3) {
			discpver_textView.setTextColor(getResources().getColor(
					R.color.tab_choice_color));
		}else {
			discpver_textView.setTextColor(getResources().getColor(
					R.color.tab_color));
		}
		if (tab4) {
			telecast_TextView.setTextColor(getResources().getColor(
					R.color.tab_choice_color));
		}else {
			telecast_TextView.setTextColor(getResources().getColor(
					R.color.tab_color));
		}
	}
	/**
	 * 
	 */
	private void setTitleClickState(boolean focusable,boolean clickable,int imgVisibility) {
		//设置titlebar中的标题是否能点击.及标题旁的图片是否显示
		rl_main_text.setFocusable(focusable);
		rl_main_text.setClickable(clickable);
		txt_image.clearAnimation();
		txt_image.setVisibility(imgVisibility);
	}
	@Override
	public void initData() {
		//创建发现界面中子界面对应的title的文本集合
		if (discpverChildTitles == null) {
			discpverChildTitles = new ArrayList<String>();
		}else {
			discpverChildTitles.clear();
		}
//		discpverChildTitles.add("智库");
		discpverChildTitles.add("所有导师");
		discpverChildTitles.add("全部学友");
		discpverChildTitles.add("所有活动");
		//创建发现界面中子界面搜索状态对应的title的文本集合
		if (discpverChildSearchTitles == null) {
			discpverChildSearchTitles = new ArrayList<String>();
		}else {
			discpverChildSearchTitles.clear();
		}
//		discpverChildTitles.add("智库");
		discpverChildSearchTitles.add("导师查询");
		discpverChildSearchTitles.add("学友查询");
		discpverChildSearchTitles.add("活动查询");
		//创建发现界面中子界面学友界面中二级子界面对应的title的文本集合
		if (xueyouChildTitles == null) {
			xueyouChildTitles = new ArrayList<String>();
		}else {
			xueyouChildTitles.clear();
		}
		xueyouChildTitles.add("全部学友");
		xueyouChildTitles.add("同城学友");
		xueyouChildTitles.add("行业学友");
		xueyouChildTitles.add("其他城市");
		//创建发现界面中子界面活动界面中二级子界面对应的title的文本集合
		if (huodongChildTitles == null) {
			huodongChildTitles = new ArrayList<String>();
		}else {
			huodongChildTitles.clear();
		}
		huodongChildTitles.add("所有活动");
		huodongChildTitles.add("同城活动");
		huodongChildTitles.add("其他城市");

		
//		initDownMenu();
	}
	/**
	 * 当前显示的发现界面的子界面的位置(与discpverChildTitles中的标题位置相对应)
	 */
	private int currentDiscpverChild = 0;
	/**
	 * 当前显示的发现界面的子界面学友界面中二级子界面的位置(与xueyouChildTitles中的标题位置相对应)
	 */
	private int currentXueyouChild = 0;
	/**
	 * 当前显示的发现界面的子界面活动界面中二级子界面的位置(与huodongChildTitles中的标题位置相对应)
	 */
	private int currentHuodongChild = 0;
	private SearchListData<String> taskSearchListData;
	/**
	 * 自定义的广播接受者,用于发现界面头布局中文字跟随内容中按钮组的切换而显示不同的标题
	 * @author Administrator
	 *
	 */
	class MyTitleRecive extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
//			if (action.equals("ZhikuPager")) {
//				currentDiscpverChild = 0;
//				ll_down_menu.setVisibility(View.GONE);
//				setTitleClickState(false,false,View.GONE);
//			} else 
			if ("DaoshiPager".equals(action)) {
				currentDiscpverChild = 0;
			}else if ("XueyouPager".equals(action)) {
				currentDiscpverChild = 1;
			}else if ("HuodongPager".equals(action)) {
				currentDiscpverChild = 2;
			}else if ("QuanbuXueyou".equals(action)) {
				currentDiscpverChild = 1;
				currentXueyouChild = 0;
			}else if ("TongchengXueyou".equals(action)) {
				currentDiscpverChild = 1;
				currentXueyouChild = 1;
			}else if ("HangyeXueyou".equals(action)) {
				currentDiscpverChild = 1;
				currentXueyouChild = 2;
			}else if ("QitaXueyou".equals(action)) {
				currentDiscpverChild = 1;
				currentXueyouChild = 3;
			}else if ("SuoyouHuodong".equals(action)) {
				currentDiscpverChild = 2;
				currentHuodongChild = 0;
			}else if ("TongchengHuodong".equals(action)) {
				currentDiscpverChild = 2;
				currentHuodongChild = 1;
			}else if ("QitaHuodong".equals(action)) {
				currentDiscpverChild = 2;
				currentHuodongChild = 2;
			}else if ("newMsg".equals(action)) {
				boolean haveNewMsg = intent.getBooleanExtra("newMsg", false);
				if (haveNewMsg){
					rb_tab5.setBackgroundResource(R.drawable.iconfont_message_hui_new);
				}

			}
			showSearchbotton(true);
			backSearch();
			
			if (currentDiscpverChild == 0) {
				ll_down_menu.setVisibility(View.GONE);
				setTitleClickState(true,true,View.VISIBLE);
				hasPopupWindow = false;
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				//切换到导师界面,根据分类id去设置title的标题
				currentTeacherSubjectId = SharedPreferencesUtil.getInstance().spGetString("downmenu_teacherSubjectId", "-1");
				if ("-1".equals(currentTeacherSubjectId)) {
					if (isSearchTeacher) {
						tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
					}else {
						tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
					}
				}else {
					if (dawnMenuMap.get(currentTeacherSubjectId) != null) {
						tv_main_title.setText(dawnMenuMap.get(currentTeacherSubjectId));
					}else {
						if (isSearchTeacher) {
							tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
						}else {
							tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
						}
					}
				}
			}else if (currentDiscpverChild == 1) {
				ll_down_menu.setVisibility(View.GONE);
				setTitleClickState(true,true,View.VISIBLE);
				hasPopupWindow = true;
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				if (currentXueyouChild == 0) {
					if (isSearchSchoolmate) {
						tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
					}else {
						tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
					}
				}else {
					tv_main_title.setText(xueyouChildTitles.get(currentXueyouChild));
				}
			}else if (currentDiscpverChild == 2) {
				ll_down_menu.setVisibility(View.GONE);
				setTitleClickState(true,true,View.VISIBLE);
				hasPopupWindow = true;
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				if (currentHuodongChild == 0) {
					if (isSearchSport) {
						tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
					}else {
						tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
					}
				}else {
					tv_main_title.setText(huodongChildTitles.get(currentHuodongChild));
				}
			}
		}
	}
	/**
	 * 弹出搜索框的按钮是否显示
	 * true 显示
	 * false 隐藏
	 */
	private void showSearchbotton(boolean isShow) {
//		isShow = false;
		if (isShow) {
			if (View.GONE == btn_search.getVisibility()) {
				btn_search.setVisibility(View.VISIBLE);
			}
		}else {
			if (View.VISIBLE == btn_search.getVisibility()) {
				btn_search.setVisibility(View.GONE);
			}
		}
	}
	@Override
	public void onDestroy() {
		//取消注册的广播接受者
		context.unregisterReceiver(myTitleRecive);
		super.onDestroy();
	}
	/**
	 * 初始化发现界面,并完成状态改变
	 */
	private void initDiscpver() {
		//判断当前发现界面中显示的是否是导师界面,以此来设置title上标题的显示内容及点击状态
		setTitleClickState(true,true,View.VISIBLE);
		switch (currentDiscpverChild) {
		case 0:
			currentTeacherSubjectId = SharedPreferencesUtil.getInstance().spGetString("downmenu_teacherSubjectId", "-1");
			
			if ("-1".equals(currentTeacherSubjectId)) {
				tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
			}else {
				if (dawnMenuMap.get(currentTeacherSubjectId) != null) {
					tv_main_title.setText(dawnMenuMap.get(currentTeacherSubjectId));
				}else {
					if (isSearchTeacher) {
						tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
					}else {
						tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
					}
				}
			}
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			break;
		case 1:
			if (isSearchSchoolmate) {
				tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
			}else {
				tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
			}
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			break;
		case 2:
			if (isSearchSport) {
				tv_main_title.setText(discpverChildSearchTitles.get(currentDiscpverChild));
			}else {
				tv_main_title.setText(discpverChildTitles.get(currentDiscpverChild));
			}
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
			break;
		}

		setunderButtonState(false,false,true,false);
		switchContent(currentFragment, fragmentList.get(1));
	}
	/**
	 * 初始化课程界面,并完成状态改变
	 */
	private void initCourse() {
		setTitleClickState(true,true,View.VISIBLE);
		currentCourseSubjectId = SharedPreferencesUtil.getInstance().spGetString("downmenu_courseSubjectId", "-1");
		if ("-1".equals(currentCourseSubjectId)) {
			tv_main_title.setText("所有课程");
		}else {
			if (dawnMenuMap.get(currentCourseSubjectId) != null) {
				tv_main_title.setText(dawnMenuMap.get(currentCourseSubjectId));
			}else {
				tv_main_title.setText("所有课程");
			}
		}
		setunderButtonState(false,true,false,false);
		((CourseFragment)fragmentList.get(0)).setSubjectId(currentCourseSubjectId);
		switchContent(currentFragment, fragmentList.get(0));
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		
	}
	/**
	 * 初始化首页界面,并完成状态改变
	 */
	private void initHome() {
		setTitleClickState(false,false,View.GONE);
		setunderButtonState(true,false,false,false);
		//设置对应文本
		tv_main_title.setText("首页");	
		switchContent(currentFragment, fragmentList.get(0));
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
	}
	/**
	 * 切换显示数据的fragment
	 * @param from 切换时被覆盖的fragment
	 * @param to 切换时要加载的fragment
	 */
	public void switchContent(BaseFragment from, BaseFragment to) {
		if (currentFragment != to) {
			currentFragment = to;
			FragmentTransaction transaction = mFragmentMan.beginTransaction().setCustomAnimations(  
					android.R.anim.fade_in, android.R.anim.fade_out);  
			if (!to.isAdded()) {    // 先判断是否被add过  
				if (from != null) {
					// 隐藏当前的fragment，add下一个到Activity中  
					transaction.hide(from).add(R.id.layout_content, to).commit(); 
				}else {
					transaction.add(R.id.layout_content, to).commit();
				}
			} else {
				 // 隐藏当前的fragment，显示下一个  
				transaction.hide(from).show(to).commit();
			}  
		}  
	}
	private void initListener() {
		imgbtn_menu.setOnClickListener(this);
		rl_main_text.setOnClickListener(this);
		rl_main_text.setFocusable(true);
		rl_main_text.setClickable(true);
		imgbtn_recode.setOnClickListener(this);
		btn_search.setOnClickListener(this);
//		rb_tab1.setOnClickListener(this);
		rb_tab2.setOnClickListener(this);
		rb_tab3.setOnClickListener(this);
		rb_tab4.setOnClickListener(this);
		rb_tab5.setOnClickListener(this);
//		layout_1.setOnClickListener(this);
		layout_2.setOnClickListener(this);
		layout_3.setOnClickListener(this);
		layout_4.setOnClickListener(this);
		layout_5.setOnClickListener(this);
		backsearch.setOnClickListener(this);
		btn_search_two.setOnClickListener(this);
		ll_down_menu.setOnClickListener(this);
	}
	
	/*****************发现页面弹出的popupwindow***********************/
	
	/**
	 * 学友的PopupWindow对象
	 */
	private PopupWindow matePop; 
	/**
	 * 活动的PopupWindow对象
	 */
	private PopupWindow sportPop; 
	/**
	 * 学友的view
	 */
	private View mateView;
	/**
	 * 活动的View
	 */
	private View sportView;
	
	private Intent intent;
	
	private int INUDSTRY = 1;// 行业
	
	private String inudstry;
	private List<BaseFragment> discpverFragmentList;
	
	/**
	 * 学友的popUpWindow
	 */
	private void shoolmatePopupWindow() {

		mateView = View.inflate(context,R.layout.layout_popupwindow_schoolmate, null);

		matePop = new PopupWindow(mateView, ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT);
		matePop.setFocusable(false); // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
		matePop.setTouchable(true);
		matePop.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		matePop.setBackgroundDrawable(new BitmapDrawable());
		// 点击事件监听
		getMateListener();
		// 设置好参数之后再show
		matePop.showAsDropDown(fl_title);

	}

	/**
	 * 点击活动时弹出的popwindow
	 * 
	 */
	private void sportPopWindow() {
		sportView = View.inflate(context, R.layout.layout_popupwindow_sport,
				null);
		sportPop = new PopupWindow(sportView, ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		sportPop.setFocusable(false); // 设定 PopupWindow 取的焦点，创建出来的 PopupWindow 默认无焦点
		sportPop.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		sportPop.setBackgroundDrawable(new BitmapDrawable());
		getSportListener();
		// 设置好参数之后再show
		sportPop.showAsDropDown(fl_title);
	}

	/**
	 * 学友PopupWindow的点击监听
	 */
	public void getMateListener() {
		// 学友界面
		LinearLayout ll_mate_one = (LinearLayout) mateView.findViewById(R.id.ll_mate_one);
		LinearLayout ll_mate_two = (LinearLayout) mateView.findViewById(R.id.ll_mate_two);
		LinearLayout ll_mate_three = (LinearLayout) mateView.findViewById(R.id.ll_mate_three);
		LinearLayout ll_mate_four = (LinearLayout) mateView.findViewById(R.id.ll_mate_four);
		LinearLayout ll_schoolmate_blank = (LinearLayout) mateView.findViewById(R.id.ll_schoolmate_blank);//PopupWindow的空白部分

		ll_mate_one.setOnClickListener(this);
		ll_mate_two.setOnClickListener(this);
		ll_mate_three.setOnClickListener(this);
		ll_mate_four.setOnClickListener(this);
		ll_schoolmate_blank.setOnClickListener(this);
	}

	/**
	 * 学友PopupWindow的点击监听
	 */
	public void getSportListener() {
		// 活动界面的控件
		LinearLayout ll_one = (LinearLayout) sportView.findViewById(R.id.ll_one);
		LinearLayout ll_two = (LinearLayout) sportView.findViewById(R.id.ll_two);
		LinearLayout ll_three = (LinearLayout) sportView.findViewById(R.id.ll_three);
		LinearLayout ll_four = (LinearLayout) sportView.findViewById(R.id.ll_four);
		LinearLayout ll_sport_blank= (LinearLayout) sportView.findViewById(R.id.ll_sport_blank);//PopupWindow的空白部分

		ll_one.setOnClickListener(this);
		ll_two.setOnClickListener(this);
		ll_three.setOnClickListener(this);
		ll_four.setOnClickListener(this);
		ll_sport_blank.setOnClickListener(this);

	}
	
	/**
	 * 从打开页面获取到的返回结果
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data!=null) {
			if (1 == resultCode) {
				if (!StringUtil.isEmpty(data.getStringExtra("id"))) {
					inudstry = data.getStringExtra("id");
					((SchoolmateFragment) getFragmentList().get(1)).setExtraData(inudstry);
					((SchoolmateFragment) getFragmentList().get(1)).switchPager(2,false);
					isSearchSchoolmate = false;
				}else {
					//TODO
				}
				
			}
			if (requestCode == CityChooseActity.CHOOSE_ONE) {// 设置其他学友的城市筛选的回传
				if (resultCode == CityChooseActity.CHOOSE_ONE) {
					Bundle bundle=data.getExtras();
					if (bundle != null) {
						String newCity = bundle.getString("text");
						String cityId = bundle.getString("id");
						
						((SchoolmateFragment) getFragmentList().get(1)).setExtraData(cityId);
						((SchoolmateFragment) getFragmentList().get(1)).switchPager(3,false);
						isSearchSchoolmate = false;
					}
				}
			}
			if (requestCode == CityChooseActity.CHOOSE_THREE) {// 设置活动的地区筛选的回调
				if (resultCode == CityChooseActity.CHOOSE_THREE) {
					Bundle bundle=data.getExtras();
					if (bundle != null) {
						String newCity = bundle.getString("text");
						String cityId = bundle.getString("id");
						
						((SportFragment) getFragmentList().get(2)).setExtraData(newCity);
						((SportFragment) getFragmentList().get(2)).switchPager(2,false);
						isSearchSport = false;
					}
				}
			}
		}else {
			//TODO
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
//	public void onResume() {
//		if (current == DISCPVER) {
//			if (currentDiscpverChild == 1) {
//				((SchoolmatePager) getPagerList().get(1)).onResume(currentXueyouChild);
//			}else if (currentDiscpverChild == 2) {
//				((SportPager) getPagerList().get(2)).onResume(currentHuodongChild);
//			}
//		}
//		super.onResume();
//	}


	@Override
	public void onResume() {
		if (SharedPreferencesUtil.getInstance().spGetBoolean("newMsg")){
			rb_tab5.setBackgroundResource(R.drawable.iconfont_message_hui_new);
		}
		super.onResume();
	}

	/**
	 * 融云的用户信息提供者
	 */
	@Override
	public UserInfo getUserInfo(String arg0) {
		Log.i("token", "用户ID"+arg0);
		

		cc.upedu.online.domin.UserInfoBean.UserInfo userInfo=GetUserInforUtil.getUserInfo(context, arg0);
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
	};

}
