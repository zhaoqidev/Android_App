package cc.upedu.online.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.SportDetailBean;
import cc.upedu.online.domin.SportDetailBean.ContactItem;
import cc.upedu.online.domin.SportDetailBean.PicList;
import cc.upedu.online.domin.SportIssueBean;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.photoselector.PhotoItem;
import cc.upedu.online.photoselector.PhotoModel;
import cc.upedu.online.photoselector.PhotoSelectorActivity;
import cc.upedu.online.photoselector.PhotoSelectorView;
import cc.upedu.online.photoselector.PicImage;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.CreateBmpFactory;
import cc.upedu.online.utils.HttpMultipartPost;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.ValidateUtil;
import cc.upedu.online.view.citychoose.CityChooseActity;
import cc.upedu.online.view.factory.MyHorizontalRow4Item;
import cc.upedu.online.view.factory.MyHorizontalRow4Item.IconReightOnClickListener;
import cc.upedu.online.view.factory.MyHorizontalTextIcon3Item;
import cc.upedu.online.view.factory.MyVerticalTitleEdit2Item;
import cc.upedu.online.view.wheelview.JudgeDate;
import cc.upedu.online.view.wheelview.MyAlertDialog;
import cc.upedu.online.view.wheelview.ScreenInfo;
import cc.upedu.online.view.wheelview.WheelMain;

/**
 * 发起活动的页面
 * 
 * @author Administrator
 * 
 */
public class SportIssueActivity extends TitleBaseActivity implements PhotoItem.onItemLongClickListener {
	ScrollView sv;//整个滚动布局

	private MyHorizontalRow4Item sportTitleRow,
		contactsNameRow1,contactsMobileRow1,contactsEmailRow1,
		contactsNameRow2,contactsMobileRow2,contactsEmailRow2,
		contactsNameRow3,contactsMobileRow3,contactsEmailRow3;
	private MyVerticalTitleEdit2Item sportContentRow;
	private MyHorizontalTextIcon3Item startTimeRow,endTimeRow,addressRow,positionRow;
//	private MyVerticalTitleGrid3Item picGridRow;
	private TextView addPerosonView;
	
	int TYPE=0;//0是活动开始时间，1是活动结束时间
	
	String name1,name2,name3,phone1,phone2,phone3,email1,email2,email3;//活动联系人的信息
	// 添加联系人,发起活动按钮
	RelativeLayout rl_issue_sport;
	int NUMBER = 2;

	WheelMain wheelMain;
	String time;
	String address;// 活动位置
	/**
	 * 开始时间
	 */
	private String startTime;
	/**
	 * 结束时间
	 */
	private String endTime;

	private String newCity = "0";
//	private String cityId = "0";
	
	private String userId;
	private String activity;
	
	Intent intent;
	private String sportId;//活动Id

	private List<PicList> picList;
	private List<String> oldUrlList;
	private List<String> newUrlList;
	private List<String> picAddArray;
	private List<String> picDelArray;
	private Map<String, String> picIvMap;
	private final static int REQUEST_ADDRESS = 1;// 用于两个activity之间的数据传值
	private MyAlertDialog dialog;
	private boolean people1;//判断活动联系人是否为空，空为false，非空为true
	private boolean people2;
	private boolean people3;
	private LinearLayout ll_default,llPhotos;
	private Object cityTxt;
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("发起活动");
		setRightText("发布", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (StringUtil.getLongDate(startTime,"yyyy-MM-dd HH:mm") < StringUtil.getLongDate(endTime,"yyyy-MM-dd HH:mm:ss")) {
					getSportMessage();
				}else {
					ShowUtils.showMsg(context, "活动结束时间不能在开始时间之前!");
				}
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		View view = View.inflate(context, R.layout.activity_sport_issue, null);
		sv=(ScrollView) view.findViewById(R.id.sv);//滚动布局
		ll_default = (LinearLayout) view.findViewById(R.id.ll_default);
		
		userId=UserStateUtil.getUserId();
		
		addView(ll_default);
		
		picList=new ArrayList<SportDetailBean.PicList>();
		oldUrlList = new ArrayList<String>();
		newUrlList = new ArrayList<String>();
		
		intent=getIntent();
		if (!StringUtil.isEmpty(intent.getStringExtra("aid"))) {
			sportId=intent.getStringExtra("aid");
			getActivityData();//aid不空，请求活动详细信息，设置到活动页面上
		}

		// 上传图片相关
//		mCreateBmpFactory = new CreateBmpFactory(this);
		// 增删图片网格业务


		initPhotos();

//		adapter = new MyGridViewAdapter();
//		picGridRow.setAdapter(adapter);
//
//		View picView = LayoutInflater.from(SportIssueActivity.this).inflate(R.layout.gv_item_pic, null);
//		mImageButton = (ImageButton) picView.findViewById(R.id.pic);
		
//		activityItem = (ActivityItem)getIntent().getSerializableExtra("activityItem");
		return view;
	}
	/**
	 * @throws NotFoundException
	 */
	private void addView(LinearLayout ll_default){
		//添加活动标题布局
		sportTitleRow = new MyHorizontalRow4Item(MyHorizontalRow4Item.TEXT_EDIT);
		sportTitleRow.initView(context);
		sportTitleRow.setEditStyle(14, 8, this.getResources().getColor(R.color.dark_grey), this.getResources().getColor(R.color.color_textcolor));
		sportTitleRow.setText("活动标题");
		sportTitleRow.setEditHintText("请输入标题");
		ll_default.addView(sportTitleRow.getRootView());
		//添加活动内容布局
		sportContentRow = new MyVerticalTitleEdit2Item(MyVerticalTitleEdit2Item.TEXT_EDIT_INDEX);
		sportContentRow.initView(context);
		sportContentRow.setText("活动内容");
		sportContentRow.setEditHintText("请详细说明活动的目的、主题、内容、邀请对象、注意事项等.");
		ll_default.addView(sportContentRow.getRootView());
		//添加开始时间布局
		startTimeRow = addTimeOrAddressItem(MyHorizontalTextIcon3Item.TEXT_TEXT,16,0,"开始时间",null,1);
		//添加结束时间布局
		endTimeRow = addTimeOrAddressItem(MyHorizontalTextIcon3Item.TEXT_TEXT,1f,0,"结束时间",null,2);
		//添加活动城市布局
		addressRow = addTimeOrAddressItem(MyHorizontalTextIcon3Item.TEXT_TEXT,1f,0,"活动城市",null,3);
		//添加详细地址布局
		positionRow = addTimeOrAddressItem(MyHorizontalTextIcon3Item.TEXT_TEXT_ICON,1f,R.drawable.sport_position,"详细地址",null,4);
		//添加图片列表布局
//		picGridRow = new MyVerticalTitleGrid3Item(MyVerticalTitleGrid3Item.TEXT_GRID_REMARKS);
//		picGridRow.initView(context);
//		picGridRow.setText("添加照片");
//		ll_default.addView(picGridRow.getRootView());
		llPhotos = (LinearLayout) View.inflate(context,R.layout.layout_photos,null);
		ll_default.addView(llPhotos);
		//添加联系人名称布局
		contactsNameRow1 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT,16,R.drawable.people_2x,0,"请输入姓名");
		contactsNameRow1.setEditText(SharedPreferencesUtil.getInstance().spGetString("name"));
		//添加电话布局
		contactsMobileRow1 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT,1f,R.drawable.pnone2_2x,0,"请输入手机号");
		contactsMobileRow1.setEditText(SharedPreferencesUtil.getInstance().spGetString("mobile"));
		//添加邮箱布局
		contactsEmailRow1 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT,1f,R.drawable.email_2x,0,"请输入邮箱");
		//添加联系人名称布局
		contactsNameRow2 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT_ICON,16,R.drawable.people_2x,R.drawable.delete,"请输入姓名");
		//添加电话布局
		contactsMobileRow2 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT,1f,R.drawable.pnone2_2x,0,"请输入手机号");
		//添加邮箱布局
		contactsEmailRow2 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT,1f,R.drawable.email_2x,0,"请输入邮箱");
		//添加联系人名称布局
		contactsNameRow3 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT_ICON,16,R.drawable.people_2x,R.drawable.delete,"请输入姓名");
		//添加电话布局
		contactsMobileRow3 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT,1f,R.drawable.pnone2_2x,0,"请输入手机号");
		//添加邮箱布局
		contactsEmailRow3 = addContactsItem(MyHorizontalRow4Item.ICON_EDIT,1f,R.drawable.email_2x,0,"请输入邮箱");
		//隐藏第二第三个联系人
		setContactsGone(new int[]{2,3});
		
		addPerosonView = new TextView(context);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, CommonUtil.dip2px(context, 35));
		layoutParams.setMargins(CommonUtil.dip2px(context, 10), CommonUtil.dip2px(context, 35), CommonUtil.dip2px(context, 10), CommonUtil.dip2px(context, 10));
		addPerosonView.setLayoutParams(layoutParams);
		addPerosonView.setSingleLine();
		addPerosonView.setEllipsize(TruncateAt.END);
		addPerosonView.setGravity(Gravity.CENTER);
		addPerosonView.setTextSize(16);
		addPerosonView.setTextColor(getResources().getColor(R.color.white));
//		addPerosonView.setPadding(0, CommonUtil.dip2px(context, 14), 0, CommonUtil.dip2px(context, 14));
		addPerosonView.setText("添加联系人");
		addPerosonView.setId(0);
		addPerosonView.setBackgroundResource(R.color.actionsheet_red);
		ll_default.addView(addPerosonView);
	}


	/**
	 * 创建联系人条目的view并加载到界面中
	 * @param showStyle 展示的类型 :图标输入框/图标输入框图标 两种
	 * @param MarginTop 距上方的边距
	 * @param resIdLeft 左侧的图标
	 * @param resIdReight 右侧的图标
	 * @return
	 */
	private MyHorizontalRow4Item addContactsItem(int showStyle,float MarginTop,int resIdLeft,int resIdReight,String hintText) {
		MyHorizontalRow4Item contactsItemRow = new MyHorizontalRow4Item(showStyle);
		contactsItemRow.setMarginTopDefault(MarginTop);
		contactsItemRow.initView(context);
		contactsItemRow.setIconLiftRes(resIdLeft);
		if (MyHorizontalRow4Item.ICON_EDIT_ICON == showStyle) {
			contactsItemRow.setIconReightRes(resIdReight);
		}
		ll_default.addView(contactsItemRow.getRootView());
		contactsItemRow.setEditHintText(hintText);
		return contactsItemRow;
	}
	/**
	 * 创建时间或地点条目的view并加载到界面中
	 * @param showStyle 展示的类型 :文本/文本文本/文本图标/文本文本图标 四种
	 * @param MarginTop 距上方的边距
	 * @param resIdReight 右侧的图标
	 * @param textLeft 左侧文本的内容
	 * @param textReight 右侧文本的内容
	 * @param id 当成布局的
	 * @return
	 */
	private MyHorizontalTextIcon3Item addTimeOrAddressItem(int showStyle,float MarginTop,int resIdReight,String textLeft,String textReight,int id) {
		MyHorizontalTextIcon3Item timeOrAddressRow = new MyHorizontalTextIcon3Item(showStyle);
		timeOrAddressRow.setMarginTopDefault(MarginTop);
		timeOrAddressRow.initView(context);
		timeOrAddressRow.setTextLeft(textLeft);
		if (StringUtil.isEmpty(textReight)) {
			timeOrAddressRow.setTextReight("");
		}else {
			timeOrAddressRow.setTextReight(textReight);
		}
		if (MyHorizontalTextIcon3Item.TEXT_TEXT_ICON == showStyle) {
			timeOrAddressRow.setIconReightRes(resIdReight);
		}
		timeOrAddressRow.setRootView(id);
		ll_default.addView(timeOrAddressRow.getRootView());
		return timeOrAddressRow;
	}
	/**
	 * 设置第二个第三个联系人显示出来
	 * @param indexs 数组参数只能是数字2和3,2表示第二个联系人显示,3表示第三个联系人在第二个显示之后显示
	 */
	private void setContactsVisibility(int[] indexs){
		// TODO Auto-generated method stub
		if (1 == indexs.length) {
			if (2 == indexs[0]) {
				if (View.VISIBLE != contactsMobileRow2.getRootView().getVisibility()) {
					contactsNameRow2.getRootView().setVisibility(View.VISIBLE);
					contactsMobileRow2.getRootView().setVisibility(View.VISIBLE);
					contactsEmailRow2.getRootView().setVisibility(View.VISIBLE);
				}
			}else if (3 == indexs[0]) {
				if (View.VISIBLE == contactsMobileRow2.getRootView().getVisibility()) {
					contactsNameRow3.getRootView().setVisibility(View.VISIBLE);
					contactsMobileRow3.getRootView().setVisibility(View.VISIBLE);
					contactsEmailRow3.getRootView().setVisibility(View.VISIBLE);
				}
			}
		}else if (2 == indexs.length) {
			contactsNameRow2.getRootView().setVisibility(View.VISIBLE);
			contactsMobileRow2.getRootView().setVisibility(View.VISIBLE);
			contactsEmailRow2.getRootView().setVisibility(View.VISIBLE);
			contactsNameRow3.getRootView().setVisibility(View.VISIBLE);
			contactsMobileRow3.getRootView().setVisibility(View.VISIBLE);
			contactsEmailRow3.getRootView().setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 设置第二个第三个联系人隐藏起来
	 * @param indexs 数组参数只能是数字2和3,2表示第二个联系人在第三个联系人隐藏之后隐藏,3表示第三个联系人隐藏
	 */
	private void setContactsGone(int[] indexs) {
		// TODO Auto-generated method stub
		if (1 == indexs.length) {
			if (2 == indexs[0]) {
				if (View.GONE == contactsMobileRow3.getRootView().getVisibility()) {
					contactsNameRow2.getRootView().setVisibility(View.GONE);
					contactsMobileRow2.getRootView().setVisibility(View.GONE);
					contactsEmailRow2.getRootView().setVisibility(View.GONE);
					contactsNameRow2.setEditText("");
					contactsMobileRow2.setEditText("");
					contactsEmailRow2.setEditText("");
					NUMBER = 2;
				}else {
					contactsNameRow2.setEditText(contactsNameRow3.getEditText());
					contactsMobileRow2.setEditText(contactsMobileRow3.getEditText());
					contactsEmailRow2.setEditText(contactsEmailRow3.getEditText());
					contactsNameRow3.getRootView().setVisibility(View.GONE);
					contactsMobileRow3.getRootView().setVisibility(View.GONE);
					contactsEmailRow3.getRootView().setVisibility(View.GONE);
					contactsNameRow3.setEditText("");
					contactsMobileRow3.setEditText("");
					contactsEmailRow3.setEditText("");
					NUMBER = 3;
				}
			}else if (3 == indexs[0]) {
				if (View.GONE != contactsMobileRow3.getRootView().getVisibility()) {
					contactsNameRow3.getRootView().setVisibility(View.GONE);
					contactsMobileRow3.getRootView().setVisibility(View.GONE);
					contactsEmailRow3.getRootView().setVisibility(View.GONE);
					contactsNameRow3.setEditText("");
					contactsMobileRow3.setEditText("");
					contactsEmailRow3.setEditText("");
					NUMBER = 3;
				}
			}
		}else if (2 == indexs.length) {
			contactsNameRow2.getRootView().setVisibility(View.GONE);
			contactsMobileRow2.getRootView().setVisibility(View.GONE);
			contactsEmailRow2.getRootView().setVisibility(View.GONE);
			contactsNameRow3.getRootView().setVisibility(View.GONE);
			contactsMobileRow3.getRootView().setVisibility(View.GONE);
			contactsEmailRow3.getRootView().setVisibility(View.GONE);
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		startTimeRow.getRootView().setOnClickListener(this);
		endTimeRow.getRootView().setOnClickListener(this);
		addressRow.getRootView().setOnClickListener(this);
		positionRow.getRootView().setOnClickListener(this);

		addPerosonView.setOnClickListener(this);
		contactsNameRow2.setIconReightOnClickListener(new IconReightOnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setContactsGone(new int[]{2});
			}
		});
		contactsNameRow3.setIconReightOnClickListener(new IconReightOnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setContactsGone(new int[]{3});
			}
		});
		
//		ll_default.setOnClickListener(this);
	}

//	String startTime;
//	String endTime;
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case 1:// 开始时间 ,,,
			TYPE=0;
			if(!StringUtil.isEmpty(startTime)){
				dateCheck(startTime,TYPE);
			}else{
				dateCheck("",TYPE);
			}
			

			break;
		case 2:// 结束时间
			TYPE=1;
			if(!StringUtil.isEmpty(endTime)){
				dateCheck(endTime,TYPE);
			}else{
				dateCheck("",TYPE);
			}

			break;

		case 3:// 活动城市
			intent = new Intent(context, CityChooseActity.class);
			intent.putExtra("ChooseCode", CityChooseActity.CHOOSE_ONE);
			startActivityForResult(intent, CityChooseActity.CHOOSE_ONE);
			break;

		case 4:// 活动具体位置
//			if (!"未设置".equals(cityTxt)) {
				intent = new Intent();
				intent.putExtra("city", newCity);
				intent.setClass(this, BaiduMapAddress.class);
				startActivityForResult(intent, REQUEST_ADDRESS);
//			} else {
//				ShowUtils.showMsg(context, "请先选择活动所在城市");
//			}

			break;

		case 0:// 添加联系人
			getContact();
			if (NUMBER == 2) {
				if (people1) {
					if (isEmailAddress(email1)) {
						setContactsVisibility(new int[]{2});
						NUMBER++;
						//发送延时消息
						new Handler().postDelayed(new Runnable(){  
						    public void run() {  
						    	sv.fullScroll(ScrollView.FOCUS_DOWN);  
						     }  
						  }, 300); 
						
					}else {
						ShowUtils.showMsg(context, "请输入正确的邮箱地址哦~");
					}
					
				}else {
					ShowUtils.showMsg(context, "先填写完整上面的联系人，再进行添加哦~");
				}
			} else if (NUMBER == 3) {
				if (people1&&people2) {
					if (isEmailAddress(email1)&&isEmailAddress(email2)) {
						setContactsVisibility(new int[]{3});
						NUMBER++;
						//发送延时消息
						new Handler().postDelayed(new Runnable(){  
						    public void run() {  
						    	sv.fullScroll(ScrollView.FOCUS_DOWN);  
						     }  
						  }, 300); 
					}else {
						ShowUtils.showMsg(context, "请输入正确的邮箱地址哦~");
					}
				}else {
					ShowUtils.showMsg(context, "先填写完整上面的联系人，再进行添加哦~");
				}
			} else {
				ShowUtils.showMsg(context, "最多能添加3个联系人");
			}
			break;
//		case R.id.ll_default:
//			if(SportIssueActivity.this.getCurrentFocus()!=null){
//				((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(SportIssueActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//			}
//			break;
		}

	}
	String warningString = "";//提示信息
	/**
	 * 向服务器发送活动信息
	 */
	protected void getSportMessage() {
		warningString = "";
		String title=sportTitleRow.getEditText();//标题
		String content= sportContentRow.getEditText();//活动内容
		String picUrl = null;//图片URL
		String delPicUrl=null;//被删除的图片的url

		//开始时间：StartTime
		//结束时间： endTime
		//活动城市：newCity
		//活动地址：address
		
		
		boolean email=isEmailAddress(email1)&&isEmailAddress(email2)&&isEmailAddress(email3);//校验邮箱地址

		getContact();
		
		List<Contact> contactList =new ArrayList<Contact>();
		
		if (View.VISIBLE == contactsNameRow1.getRootView().getVisibility()) {
			Contact contact =new Contact();
			contact.name=name1;
			contact.mobile=phone1;
			contact.email=email1;
			contactList.add(contact);
		}
		if (View.VISIBLE == contactsNameRow2.getRootView().getVisibility()) {
			Contact contact =new Contact();
			contact.name=name2;
			contact.mobile=phone2;
			contact.email=email2;
			contactList.add(contact);
		}
		if (View.VISIBLE == contactsNameRow3.getRootView().getVisibility()) {
			Contact contact =new Contact();
			contact.name=name3;
			contact.mobile=phone3;
			contact.email=email3;
			contactList.add(contact);
		}
		String contactString =contactListToString(contactList);
		if (picAddArray!=null) {
			picUrl=picAddArray.toString();
		}
		if (picDelArray!=null) {
			delPicUrl=picDelArray.toString();
		}
		
		/**
		 * 抽取出上传字段的公共部分
		 */
		String eventString="uid:"+userId+",title:"+title+",content:"+content+",startTime:"+"'"+startTime+"'"+",endTime:"+"'"+endTime+"'"+",city:"+newCity+",address:"+address+",contactList:"+contactString;

		validateStr(title,"活动标题");
		validateStr(content,"活动内容");
		validateStr(startTime,"开始时间");
		validateStr(endTime,"结束时间");
		validateStr(newCity,"活动城市");
		validateStr(address,"活动地址");
	
		if ((people1 || people2 || people3)&&StringUtil.isEmpty(warningString)) {
			if (email) {
				if (!StringUtil.isEmpty(picUrl)) {
					picUrl = ",picAddList:" + picUrl;
					handlePicArray(picUrl, delPicUrl, eventString);
				} else {
					picUrl = "";
					handlePicArray(picUrl, delPicUrl, eventString);
//					if (!StringUtil.isEmpty(sportId)) {
//						if (!StringUtil.isEmpty(delPicUrl)) {
//							activity = "{" + eventString + ",id:" + sportId+ ",picDelList:" + picDelArray + "}";
//						} else {
//							activity = "{" + eventString + ",id:" + sportId+ "}";
//						}
//
//					} else {
//						if (picDelArray != null) {
//							activity = "{" + eventString + ",picDelList:"+ picDelArray + "}";
//						} else {
//							activity = "{" + eventString + "}";
//						}
//					}
//					getData();
//					System.out.println("activity2***"+activity);
				}
			} else {
				ShowUtils.showMsg(context, "请输入正确的邮箱地址哦~");
			}
		} else {
			ShowUtils.showMsg(context, "至少添加一位联系人，联系人信息要填写完整哦~");
		}
	
	}

	private void handlePicArray(String picUrl, String delPicUrl,String eventString) {
		if (!StringUtil.isEmpty(sportId)) {
			if (!StringUtil.isEmpty(delPicUrl)) {
				activity = "{" + eventString + ",id:" + sportId+ picUrl + ",picDelList:"+ picDelArray + "}";
//				activity = "{" + eventString + ",id:" + sportId+ ",picDelList:"+ picDelArray + getPicUrl(picUrl)+"}";
			} else {
				activity = "{" + eventString + ",id:" + sportId+ picUrl + "}";
			}

		} else {
			if (picDelArray != null) {
				activity = "{" + eventString + picUrl + ",picDelList:" + picDelArray+ "}";
			} else {
				activity = "{" + eventString + picUrl + "}";
			}
		}

		getData();
		
		System.out.println("activity1***"+activity);
	}
	
	
	/**
	private String getPicUrl(String picUrl) {
		if (!StringUtil.isEmpty(picUrl)) {
			return  ","+"picAddList:" + picUrl;
		
		} else {
			return "";
		}
	}
*/
	private void validateStr(String contentString,String warnStr) {
//		if(!(StringUtil.isEmpty(contentString))&&(StringUtil.isEmpty(warningString))){
//			
//		}else
		if((StringUtil.isEmpty(contentString))&&StringUtil.isEmpty(warningString)) {
			warningString=warnStr;
			ShowUtils.showMsg(context,warningString+"不能为空哦~");
		}

	}
	/**
	 * 判断邮箱地址是否正确
	 * @param email 邮箱地址
	 */
	private boolean isEmailAddress(String email) {
		if (!(StringUtil.isEmpty(email))) {
			if (!ValidateUtil.isEmail(email)) {
				return false;
			}else {
				return true;
			}
		}else {
			return true;
		}
	}
	
	/**
	 * 获取活动联系人的信息，否是填写完整
	 */
	private void getContact() {
		name1=contactsNameRow1.getEditText();//联系人1
		name2=contactsNameRow2.getEditText();//联系人2
		name3=contactsNameRow3.getEditText();//联系人3
		phone1=contactsMobileRow1.getEditText();//联系人1
		phone2=contactsMobileRow2.getEditText();//联系人2
		phone3=contactsMobileRow3.getEditText();//联系人3
		email1=contactsEmailRow1.getEditText();//联系人1
		email2=contactsEmailRow2.getEditText();//联系人2
		email3=contactsEmailRow3.getEditText();//联系人3
		
		people1 = (!(StringUtil.isEmpty(name1)))&&(!(StringUtil.isEmpty(phone1)))&&(!(StringUtil.isEmpty(email1)));//判断联系人是否为空的Boolean值
		people2 = (!(StringUtil.isEmpty(name2)))&&(!(StringUtil.isEmpty(phone2)))&&(!(StringUtil.isEmpty(email2)));
		people3 = (!(StringUtil.isEmpty(name3)))&&(!(StringUtil.isEmpty(phone3)))&&(!(StringUtil.isEmpty(email3)));
	}
	
	
	/**
	 * 活动联系人
	 * @author Administrator
	 *
	 */
	class Contact{
		public String name;
		public String mobile;
		public String email;
	}
	
	/**
	 * 联系人list装换成String字符串
	 * @param contact
	 * @return
	 */
	private String contactListToString(List<Contact> contact) {
		String contactString = null;
		for (int i = 0; i < contact.size(); i++) {
			if (i == 0) {
				contactString = "[";
			}
			String contactItemString = "{";
			contactItemString +=("name:"+contact.get(i).name+",");
			contactItemString +=("mobile:"+contact.get(i).mobile+",");
			contactItemString +=("email:"+contact.get(i).email+",");
			
			if (contactItemString.lastIndexOf(",") == contactItemString.length() - 1) {
				contactItemString = contactItemString.substring(0, contactItemString.length() - 1);
			}
			contactItemString += "}";
			contactString += contactItemString;
			if (i == contact.size() - 1) {
				contactString += "]";
			}else {
				contactString += ",";
			}
		}
		return contactString;
	}
	/**
	 * 发送数据到服务器
	 */
	protected void getData() {
		Map<String, String> requestDataMap = ParamsMapUtil.SportIssue(context, activity);
		RequestVo requestVo = new RequestVo(ConstantsOnline.ISSUE_SPORT, context,
				requestDataMap, new MyBaseParser<>(SportIssueBean.class));
		DataCallBack<SportIssueBean> dataCallBack = new DataCallBack<SportIssueBean>() {

			@Override
			public void processData(SportIssueBean object) {
				// 发布成功，打开活动详情页面，把活动id传过去
				if (object!=null) {
					if ("true".equals(object.success)) {
						if (!StringUtil.isEmpty(object.entity.activityInfo.aid)) {
							intent =new Intent(context, SportDetailActivity.class);
							intent.putExtra("id", object.entity.activityInfo.aid);
							intent.putExtra("startDt", object.entity.activityInfo.startDt);
							startActivity(intent);
							finish();
							ShowUtils.showMsg(context, "活动发布成功");
						}
						
					}else {
						ShowUtils.showMsg(context, "活动发布失败");
					}
				}
				
				
			}
		};
		getDataServer(requestVo, dataCallBack);
	}

	/**
	 * 获取整个活动信息，修改活动信息请求并显示信息，发布新活动不请求
	 */
	protected void getActivityData() {
		//填充活动信息的数据
		Map<String, String> requestDataMap = ParamsMapUtil.SportDetail(context,sportId, userId);
		RequestVo requestVo = new RequestVo(ConstantsOnline.SPORT_DETAIL,context, requestDataMap, new MyBaseParser<>(
								SportDetailBean.class));
		DataCallBack<SportDetailBean> dataCallBack = new DataCallBack<SportDetailBean>() {
					@Override
					public void processData(SportDetailBean object) {
						if (object != null) {
							if ("true".equals(object.success)) {
								if (object.entity.activityInfo!=null) {
									cc.upedu.online.domin.SportDetailBean.ActivityInfo info=object.entity.activityInfo;
								
									sportTitleRow.setEditText(info.title);
									sportContentRow.setEditText(info.content);
									startTimeRow.setTextReight(info.startDt);
									startTime=info.startDt;//开始时间
									if(!StringUtil.isEmpty(info.endDt)){
										endTimeRow.setTextReight(info.endDt);
										endTime=info.endDt;//结束时间
									}
									positionRow.setTextReight(info.city);
									newCity=info.city;//给城市赋值
									addressRow.setTextReight(info.address);
									address=info.address;//给address赋值
									
									setContactsData(info.contactList);
									
									picList=info.picList;
									if (picList!=null && picList.size() > 0) {
										if (picIvMap == null) {
											picIvMap = new HashMap<String, String>();
										}
										for (int j = 0; j < picList.size(); j++) {
											oldUrlList.add(picList.get(j).picPath);
											picIvMap.put(picList.get(j).id, picList.get(j).version);
										}
										newUrlList.addAll(oldUrlList);
//										picGridRow.notifyDataGridView();

										notifyView();
									}else {
										
									}

									}
								}
							}
						}

			};
		getDataServer(requestVo, dataCallBack);
	}
	/**
	 * 设置联系人列表数据
	 * @param list 列表数据的集合
	 */
	private void setContactsData(List<ContactItem> list) {
		if (0 < list.size()) {
			contactsNameRow1.setEditText(list.get(0).contact);
			contactsMobileRow1.setEditText(list.get(0).mobile);
			contactsEmailRow1.setEditText(list.get(0).email);
		}
		if (1 < list.size()) {
			contactsNameRow2.setEditText(list.get(1).contact);
			contactsMobileRow2.setEditText(list.get(1).mobile);
			contactsEmailRow2.setEditText(list.get(1).email);
			setContactsVisibility(new int[]{2});
			NUMBER=3;
		}
		if (2 < list.size()) {
			contactsNameRow3.setEditText(list.get(2).contact);
			contactsMobileRow3.setEditText(list.get(2).mobile);
			contactsEmailRow3.setEditText(list.get(2).email);
			setContactsVisibility(new int[]{2,3});
			NUMBER=4;
		}
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == REQUEST_ADDRESS) {
				if (resultCode == BaiduMapAddress.RESULT_ADDRESS) {
					Bundle bundle = data.getExtras();
					if (bundle != null) {
						address = bundle.getString("address");
						positionRow.setTextReight(address);
					}
				}

		}else if(requestCode == CityChooseActity.CHOOSE_ONE) {// 设置活动城市的回传
			if (resultCode == CityChooseActity.CHOOSE_ONE) {
				if (data != null) {
				}
				Bundle bundle=data.getExtras();
				if (bundle != null) {
					newCity = bundle.getString("text");
//					cityId = bundle.getString("id");
					addressRow.setTextReight(newCity);
				}
			}
			}else {
//				picPath = mCreateBmpFactory.getBitmapFilePath(requestCode,
//						resultCode, data);
//				ImageSize imageSize = getImageButtonWidth(mImageButton);
//
//				int reqWidth = imageSize.width;
//				int reqHeight = imageSize.height;
//				if (picPath != null) {
//					bmp = mCreateBmpFactory.getBitmapByOpt(picPath, reqWidth,reqHeight);
//
//					if (bmp != null) {
//						File file = new File(picPath);
//						String name = file.getName();
//
//						ArrayList<String> filePathList = new ArrayList<String>();
//						filePathList.add(picPath);
//						HttpMultipartPost post = new HttpMultipartPost(SportIssueActivity.this, filePathList,new UploadCallBack() {
//
//							@Override
//							public void onSuccessListener(String result) {
//								if (!StringUtil.isEmpty(result)) {
//									newUrlList.add(0, result.substring(result.indexOf("/")));
//									if (picAddArray == null) {
//										picAddArray = new ArrayList<String>();
//									}
//									picAddArray.add("\""+result.substring(result.indexOf("/"))+"\"");
////									viewList.add(0, bmp);
//									picGridRow.notifyDataGridView();
//								}else {
//									ShowUtils.showMsg(context, "加载图片失败");
//								}
//
//							}
//
//						});
//						post.execute();
//					} else {
//						ShowUtils.showMsg(context, "上传的文件路径出错");
//					}
//				}else {
//					ShowUtils.showMsg(context, "上传的文件路径出错");
//				}
				super.onActivityResult(requestCode, resultCode, data);

				System.out.println(requestCode);
				if (resultCode == RESULT_CANCELED) {
					return;
				}
				switch (requestCode) {
					case 1:
						if (resultCode == Activity.RESULT_OK) {
							try {

								try {
									pic.clear();;
								} catch (Exception e) {
									// TODO: handle exception
								}
								List<PhotoModel> photos = (List<PhotoModel>) data
										.getExtras().getSerializable("photos");

								Message msg = new Message();
								msg.what = 1;
								msg.obj = photos;
								handler.sendMessage(msg);

							} catch (Exception e) {
								// TODO: handle exception
							}
						}
						break;
					case 2:
						try {
							PhotoModel photo = (PhotoModel) data.getSerializableExtra("photo");
							//						.getExtras().getSerializable("photos");
							//				for (PhotoModel photoModel : photos) {
							for(PicImage picImage:pic){
								if(picImage.getOriginalPath().equals(photo.getOriginalPath())){
									pic.remove(picImage);
								}
							}
							//				}
							photoSelectorView.notifyAdapter();
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
				}
			}
		}else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void notifyView(){
		try {
			pic.clear();
		} catch (Exception e) {
			// TODO: handle exception
		}
		for (String path : newUrlList){
			PicImage picImage = new PicImage();
			picImage.setOriginalPath(path);
			picImage.setPic(true);
			picImage.setIsurl(true);
			picImage.setCanDelete(true);
			pic.add(picImage);
		}
		if (pic.size() < 10) {
			PicImage picImage1 = new PicImage();
			picImage1.setPic(false);
			pic.add(picImage1);
		}
		photoSelectorView.notifyAdapter();
	}
	private ArrayList<PicImage> pic;
	private PhotoSelectorView photoSelectorView;
	private void initPhotos(){
		pic = new ArrayList<PicImage>();
		PicImage picImage = new PicImage();
		picImage.setPic(false);
		pic.add(picImage);
		photoSelectorView = new PhotoSelectorView(this, pic,
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						takeNewPhotoOrChoosePic();
					}
				},4,10){

			@Override
			public void onDeleteClick(PhotoModel photoModel, View view,int position) {
				super.onDeleteClick(photoModel, view,position);
				if (oldUrlList.contains(newUrlList.get(position))) {
					String delId = picList.get(oldUrlList.indexOf(newUrlList.get(position))).id;
					if (picDelArray == null) {
						picDelArray = new ArrayList<String>();
					}
					picDelArray.add(delId +"_"+picList.get(oldUrlList.indexOf(newUrlList.get(position))).version);
				}else {
					picAddArray.remove(picAddArray.indexOf("\""+newUrlList.get(position)+"\""));
				}
				newUrlList.remove(position);
			}
		};
		llPhotos.addView(photoSelectorView);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, dpToPx(8), 0, dpToPx(8));
		llPhotos.setLayoutParams(lp);
	}
	public int dpToPx(int dp) {
		return (int) (dp * getResources().getDisplayMetrics().density);
	}

	/**
	 * 拍照,选择图片
	 */
	private void takeNewPhotoOrChoosePic() {
		Intent intent = new Intent(context,
				PhotoSelectorActivity.class);
//		int count  = 0;
//		try {
//			count= pic.size() ;
//		} catch (Exception e) {
//			count  = 0;
//		}
//
		intent.putExtra(PhotoSelectorActivity.KEY_MAX, 10);
		intent.putExtra("selected", pic);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(intent, 1);
	}
	@Override
	public void onItemLongClick(int position) {
		// TODO Auto-generated method stub
//		Bundle bundle = new Bundle();
//		ArrayList<PhotoModel> picselect = new ArrayList<PhotoModel>();
//		PhotoModel photoModel = new PhotoModel(pic.get(position).getOriginalPath(),true);
//		picselect.add(photoModel);
//		bundle.putSerializable("select", picselect);
//		Intent intent = new Intent(this, PhotoPreviewActivity.class);
//		intent.putExtras(bundle);
//		startActivityForResult(intent, 2);
		Intent intent = new Intent(context,ImageActivity.class);
		intent.putExtra("image_list", (Serializable) newUrlList);
		intent.putExtra("image_position", position);
		context.startActivity(intent);
	}
	private android.os.Handler handler = new android.os.Handler(){
		List<PhotoModel> photos;
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					final int position = (int) msg.obj;
					if (photos != null && photos.size() > position){
						String path = photos.get(position).getOriginalPath();
						List<String> filePathList = new ArrayList<>();
						filePathList.add(path);

						HttpMultipartPost post = new HttpMultipartPost(SportIssueActivity.this, filePathList,new HttpMultipartPost.UploadCallBack() {

							@Override
							public void onSuccessListener(String result) {
								// TODO Auto-generated method stub
								if (!StringUtil.isEmpty(result)) {
									System.out.println("---------------result----------------" + result);
									newUrlList.add(0, result.substring(result.indexOf("/")));
									picAddArray.add("\"" + result.substring(result.indexOf("/")) + "\"");

									if (position < photos.size()) {
										Message msg3 = new Message();
										msg3.what = 0;
										msg3.obj = position+1;
										handler.sendMessage(msg3);
									}

								}else {
									ShowUtils.showMsg(context, "加载图片失败");
								}
							}
						});
						post.execute();
					}else {
						notifyView();
					}
					break;
				case 1:
					photos = (List<PhotoModel>) msg.obj;
					for (int i=0;i < photos.size();i++){
						if (newUrlList.contains(photos.get(i).getOriginalPath())){
							photos.remove(i);
							i--;
						}
					}
					Message msg2 = new Message();
					msg2.what = 0;
					msg2.obj = 0;
					handler.sendMessage(msg2);
//					for (PhotoModel photoModel : photos) {
//
//					}
					break;
			}
		};
	};



//	private View addView;
//	private LayoutInflater inflater;
//	/** BMP制造工厂，用于获得来自图库或者照相机拍照所生成的图片。并可以剪切 */
//	private CreateBmpFactory mCreateBmpFactory;
//	private ImageButton mImageButton;
//	/**
//	 * 上传图片的gridview
//	 *
//	 * @author Administrator
//	 *
//	 */
//	public class MyGridViewAdapter extends BaseAdapter {
//
//
//		@Override
//		public int getCount() {
////			if (viewList == null) {
////				return 0;
////			}
//			return newUrlList.size()+1;
//		}
//
//		@Override
//		public Object getItem(int position) {
//			if (position == newUrlList.size()) {
//				return newUrlList.get(position);
//			}else {
//				return null;
//			}
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return position;
//		}
//
//		@Override
//		public View getView(final int position, View convertView,
//							ViewGroup parent) {
//			inflater = LayoutInflater.from(SportIssueActivity.this);
//			if (position == newUrlList.size()) {
//				addView = inflater.inflate(R.layout.gv_sportitem_add, null);
//				addView.findViewById(R.id.add).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//								if (newUrlList.size() == 5) {
//									ShowUtils.showMsg(context, "已达到最多图片数量");
//									return;
//								} else {
//									ImageSelectorDialog dialog = new ImageSelectorDialog(context);
//									dialog.setDialogCallBack(dialog.new DialogCallBack() {
//										@Override
//										public void sendPic() {
//											mCreateBmpFactory.OpenGallery();
//										}
//										@Override
//										public void sendCamera() {
//											mCreateBmpFactory.OpenCamera();
//										}
//									});
//									Window window = dialog.getWindow();
//									window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//									window.setWindowAnimations(R.style.style_imageselectordialog); // 添加动画
//									window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//									window.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//									dialog.show();
//								}
//							}
//						});
//				return addView;
//			} else {
//				View picView = inflater.inflate(R.layout.gv_sportitem_pic, null);
//				ImageButton picIBtn = (ImageButton) picView
//						.findViewById(R.id.pic);
//
//				ImageUtils.setImageToImageButton(newUrlList.get(position), picIBtn, R.drawable.wodeimg_default);
//
//				picIBtn.setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
////						if (loadImageSuccess) {
//						Intent intent = new Intent(context,ImageActivity.class);
//						intent.putExtra("image_list", (Serializable) newUrlList);
//						intent.putExtra("image_position", position);
//						context.startActivity(intent);
////						}else {
////							ShowUtils.showMsg(context, "图片加载失败!");
////						}
//					}
//				});
//				picView.findViewById(R.id.delete).setOnClickListener(
//						new OnClickListener() {
//
//							@Override
//							public void onClick(View v) {
//
//								if (oldUrlList.contains(newUrlList.get(position))) {
//									String delId = picList.get(oldUrlList.indexOf(newUrlList.get(position))).id;
//									if (picDelArray == null) {
//										picDelArray = new ArrayList<String>();
//									}
//									picDelArray.add(delId +"_"+picIvMap.get(delId));
//								}else {
//									picAddArray.remove(picAddArray.indexOf("\""+newUrlList.get(position)+"\""));
//								}
//
//
//								newUrlList.remove(position);
//								picGridRow.notifyDataGridView();
//							}
//						});
//				return picView;
//			}
//		}
//	}
//
//	private String picPath;
//	private Bitmap bmp;
//	private MyGridViewAdapter adapter;
//	private class ImageSize {
//		int width;
//		int height;
//	}
//
//	/**
//	 * 根据ImageButton获得适当的压缩的宽和高
//	 *
//	 * @param imageButton
//	 * @return
//	 */
//	private ImageSize getImageButtonWidth(ImageButton imageButton) {
//		ImageSize imageSize = new ImageSize();
//		final DisplayMetrics displayMetrics = imageButton.getContext()
//				.getResources().getDisplayMetrics();
//		final LayoutParams params = imageButton.getLayoutParams();
//
//		int width = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageButton
//				.getWidth(); // Get actual image width
//		if (width <= 0)
//			width = params.width; // Get layout width parameter
//		if (width <= 0)
//			width = getImageButtonFieldValue(imageButton, "mMaxWidth"); // Check
//		// maxWidth
//		// parameter
//		if (width <= 0)
//			width = displayMetrics.widthPixels;
//		int height = params.height == LayoutParams.WRAP_CONTENT ? 0
//				: imageButton.getHeight(); // Get actual image height
//		if (height <= 0)
//			height = params.height; // Get layout height parameter
//		if (height <= 0)
//			height = getImageButtonFieldValue(imageButton, "mMaxHeight"); // Check
//																			// maxHeight
//																			// parameter
//		if (height <= 0)
//			height = displayMetrics.heightPixels;
//		imageSize.width = width;
//		imageSize.height = height;
//		return imageSize;
//
//	}
//
//	/**
//	 * 反射获得ImageButton设置的最大宽度和高度
//	 *
//	 * @param object
//	 * @param fieldName
//	 * @return
//	 */
//	private static int getImageButtonFieldValue(Object object, String fieldName) {
//		int value = 0;
//		try {
//			Field field = ImageButton.class.getDeclaredField(fieldName);
//			field.setAccessible(true);
//			int fieldValue = (Integer) field.get(object);
//			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
//				value = fieldValue;
//
//				Log.e("TAG", value + "");
//			}
//		} catch (Exception e) {
//		}
//		return value;
//	}

	/**
	 * 弹出时间选择控件，并获取到用户选择的时间
	 * 
	 * 活动开始的时间
	 */
	private void dateCheck(String mTime,final int type) {
		LayoutInflater inflater = LayoutInflater.from(SportIssueActivity.this);
		final View timepickerview = inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(SportIssueActivity.this);
		wheelMain = new WheelMain(timepickerview, true, true);
		wheelMain.screenheight = screenInfo.getHeight();
		if (StringUtil.isEmpty(mTime)) {

			time=StringUtil.getStringDate();
		}else {

			time=mTime;
		}

		Calendar calendar = Calendar.getInstance();
		if (JudgeDate.isDate(time, "yyyy-MM-dd HH:mm:ss")) {
			try {
				calendar.setTime(StringUtil.nDateFormat.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = 8;
		int minute = 0;
		if (!StringUtil.isEmpty(mTime)) {
			hour = calendar.get(Calendar.HOUR_OF_DAY);
			minute = calendar.get(Calendar.MINUTE);
		}
		wheelMain.initDateTimePicker(year, month,day,hour,minute);
		dialog = new MyAlertDialog(SportIssueActivity.this).builder()
//				.setTitle("选择时间")
				.setView(timepickerview)
				.setNegativeButton("取消", new OnClickListener() {
					@Override
					public void onClick(View v) {
						// isShowWheel = !isShowWheel;
					}
				});
		dialog.setPositiveButton("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				String backTime;
				if (0==type) {
					backTime = wheelMain.getTime();
					long longDate = StringUtil.getLongDate(backTime,"yyyy-MM-dd HH:mm");
					if (longDate > System.currentTimeMillis()) {
						startTime=backTime+":00";
						startTimeRow.setTextReight(startTime);
						dialog.dismiss();
					}else {
						ShowUtils.showMsg(context, "您选择的时间已过期!");
					}
				}else {
					backTime = wheelMain.getTime();
					long longDate = StringUtil.getLongDate(backTime,"yyyy-MM-dd HH:mm");
					if (longDate > System.currentTimeMillis()) {
						endTime=backTime+":00";
						endTimeRow.setTextReight(endTime);
						dialog.dismiss();
					}else {
						ShowUtils.showMsg(context, "您选择的时间已过期!");
					}
				}
			}
		});
		Window window = dialog.getWindow();  
	    window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置   
	    window.setWindowAnimations(R.style.mydatadialogstyle);
		dialog.show();
	}

}
