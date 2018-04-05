package cc.upedu.online.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.OnlineApp;
import cc.upedu.online.R;
import cc.upedu.online.adapter.SportMateAdapter;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.SportDetailBean;
import cc.upedu.online.domin.SportDetailBean.ContactItem;
import cc.upedu.online.domin.SportDetailBean.PicList;
import cc.upedu.online.domin.SportMateBean;
import cc.upedu.online.domin.SportMateBean.JoinUserItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShareUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.CircleImageView;
import cc.upedu.online.view.CustomDigitalClock;
import cc.upedu.online.view.CustomDigitalClock.ClockListener;
import cc.upedu.online.view.GrapeGridview;
import cc.upedu.online.view.pullrefreshview.PullToRefreshBase;
import cc.upedu.online.view.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import cc.upedu.online.view.pullrefreshview.PullToRefreshListView;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 活动详情页面
 * 
 * @author Administrator
 * 
 */
public class SportDetailActivity extends TitleBaseActivity {
	TextView tv_sport_title;// 活动标题
	TextView tv_browse;// 浏览量
	TextView tv_share;// 分享数
	TextView tv_apply;// 报名数
	TextView tv_sport_content;// 活动内容
	GrapeGridview gv_photo;// 活动图片的GradView

	TextView tv_sport_time;// 活动时间
	TextView tv_end_time;// 活动时间
	
	TextView tv_sport_address;// 活动地点
	TextView tv_sport_linkmen;// 活动联系人
	TextView tv_sport_phone;// 活动联系人手机
	TextView tv_sport_email;// 活动联系人邮箱
	ListView lv_contact;

	TextView note_user_name;// 发起者姓名
	TextView note_user_position;// 发起者职位
	TextView note_content;// 发起者公司名
	CustomDigitalClock tv_countdown;// 倒计时时间
	CircleImageView note_user_image;// 发起者头像

	ScrollView scrollView;
	View headView;// 填充显示内容
	//View leaderView;// 填充活动发起人
	LinearLayout ll_loading;
	LinearLayout ll_faqiren;
	RelativeLayout rl_bg_sport;//活动背景

	LinearLayout ll_text;
	
	TextView tv_baoming;//我要报名按钮
	
	Bitmap bitmap=null;
	int joinNumber=0;//加入活动的学友人数
	String leaderId;

	// .........................................

	String aid;// 活动ID
	String uid;// 用户ID
	private String sportTitle,sportAddress,sportStartTimes;//活动标题
	private String image;//活动图片
	
	SportDetailBean mBean = new SportDetailBean();
	SportMateBean mateBean = new SportMateBean();
	List<PicList> picList = new ArrayList<PicList>();// 存储图片的集合
	List<ContactItem> contactList = new ArrayList<ContactItem>();

	GridViewAdapter gvAdapter;

	// 记录是否是下拉刷新操作
	boolean isPullDownToRefresh = false;
	// 记录是否是下拉加载操作
	boolean isPullUpToRefresh = false;
	// 当前数据加载到哪个page
	private int currentPage = 1;
	private String totalPage;// 总页数

	PullToRefreshListView ptrlv;// 下拉刷新的ListView
	private List<JoinUserItem> joinList = new ArrayList<JoinUserItem>();// 存储活动条目
	SportMateAdapter adapter;
	

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (isPullUpToRefresh) {
					if ("true".equals(mateBean.success)) {
						if (joinList == null) {
							joinList = new ArrayList<SportMateBean.JoinUserItem>();
						}
						joinList.addAll(mateBean.entity.joinUserList);
						if (adapter == null) {
							adapter = new SportMateAdapter(context, joinList);
							ptrlv.getRefreshableView().setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
						isPullUpToRefresh = false;
					}else{
						ShowUtils.showMsg(context, mateBean.message);
					}
				} else {

					if ("true".equals(mateBean.success)) {
						
						totalPage = mateBean.entity.totalPage;
						if (currentPage < Integer.valueOf(totalPage)) {
							ptrlv.setScrollLoadEnabled(true);
						}else {
							ptrlv.setScrollLoadEnabled(false);
						}

						if(mateBean.entity.joinUserList!=null){
							joinList.clear();
							joinList.addAll(mateBean.entity.joinUserList);
							
						}
						adapter = new SportMateAdapter(context, joinList);
						ptrlv.getRefreshableView().setAdapter(adapter);

					}else{
						ShowUtils.showMsg(context, "获取数据失误，请稍后重试");
					}
				}
				break;
			}
		}
	};

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("成长活动");
		setRightButton(R.drawable.left_collect, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击收藏按钮后
				Map<String, String> requestDataMap = ParamsMapUtil.SportDetail(context, aid, uid);
				RequestVo requestVo = new RequestVo(ConstantsOnline.COLLECT_SPORT,context, requestDataMap,
						new MyBaseParser<>(JoinSportBean.class));
				DataCallBack<JoinSportBean> coursseDataCallBack = new DataCallBack<JoinSportBean>() {

					@Override
					public void processData(JoinSportBean object) {
						if (object!=null) {
							if ("true".equals(object.success)) {
								ShowUtils.showMsg(context, object.message);
								setRightButton(R.drawable.iconfont_shoucang,null);
							}
						}else {
							ShowUtils.showMsg(context, "数据请求失败，请稍后重试");
						}
					}
				};
				getDataServer(requestVo, coursseDataCallBack);
			}
		});
		setRightButton2(R.drawable.share, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//点击分享按钮后
				
				//进入分享页面
				if (StringUtil.isEmpty(aid) || StringUtil.isEmpty(sportTitle) || StringUtil.isEmpty(image)) {
					ShowUtils.showMsg(context, "数据获取失败,请检查您的网络!");
				}else {
					if (oks == null) {
						oks = ShareUtil.getInstance().showShare(ShareUtil.STYE_SPORT,aid,sportTitle,image,false,null,"活动: "+sportTitle+
								"\n地址: "+sportAddress+
								"\n时间: "+sportStartTimes);
					}
					oks.show(context);
				}
			}
		});
	}
	@Override
	protected View initContentView() {
		contentView = View.inflate(context, R.layout.activity_sport_listview, null);
		headView = View.inflate(context, R.layout.activity_sport, null);

		ll_loading = (LinearLayout) contentView.findViewById(R.id.ll_loading);
		ll_faqiren = (LinearLayout) headView.findViewById(R.id.ll_faqiren);
		
		rl_bg_sport=(RelativeLayout) headView.findViewById(R.id.rl_bg_sport);//活动背景

		lv_contact = (ListView) headView.findViewById(R.id.lv_contact);

		tv_sport_title = (TextView) headView.findViewById(R.id.tv_sport_title);
		tv_browse = (TextView) headView.findViewById(R.id.tv_browse);
		tv_share = (TextView) headView.findViewById(R.id.tv_share);
		tv_apply = (TextView) headView.findViewById(R.id.tv_apply);
		tv_sport_content = (TextView) headView
				.findViewById(R.id.tv_sport_content);
		gv_photo = (GrapeGridview) headView.findViewById(R.id.gv_photo);

		tv_sport_time = (TextView) headView.findViewById(R.id.tv_sport_time);
		tv_end_time = (TextView) headView.findViewById(R.id.tv_end_time);
		tv_sport_address = (TextView) headView.findViewById(R.id.tv_sport_address);
		// tv_sport_linkmen = (TextView)
		// headView.findViewById(R.id.tv_sport_linkmen);
		// tv_sport_phone = (TextView)
		// headView.findViewById(R.id.tv_sport_phone);
		// tv_sport_email = (TextView)
		// headView.findViewById(R.id.tv_sport_email);
		// 活动联系人的listview。

//		leaderView = View.inflate(context,R.layout.activity_sport_faqiren_item, null);
//		note_user_name = (TextView) leaderView
//				.findViewById(R.id.note_user_name);
//		note_user_position = (TextView) leaderView
//				.findViewById(R.id.note_user_position);
//		note_content = (TextView) leaderView.findViewById(R.id.note_content);
//		tv_countdown = (CustomDigitalClock) leaderView.findViewById(R.id.tv_countdown);
//		note_user_image = (CircleImageView) leaderView
//				.findViewById(R.id.note_user_image);
//		ll_faqiren.addView(leaderView);
		
		// 发起人介绍
		note_user_name = (TextView) headView.findViewById(R.id.note_user_name);//发起人姓名
		note_user_position = (TextView) headView.findViewById(R.id.note_user_position);//发起人职位
		note_content = (TextView) headView.findViewById(R.id.note_content);//发起人公司
		tv_countdown = (CustomDigitalClock) headView.findViewById(R.id.tv_countdown);//倒计时控件
		note_user_image = (CircleImageView) headView.findViewById(R.id.note_user_image);//发起人头像
		
		tv_baoming=(TextView) headView.findViewById(R.id.tv_baoming);
		
		// 获取活动数据
		Intent intent =getIntent();
		aid = intent.getStringExtra("id");//获取到活动ID
		uid = UserStateUtil.getUserId();

		if (!StringUtil.isEmpty(aid)){
			image = intent.getStringExtra("image");

			//倒计时
			long endTime = StringUtil.stringToMillis(intent.getStringExtra("startDt"));
			tv_countdown.setEndTime(endTime, 1);
			tv_countdown.setClockListener(new ClockListener() {
				@Override
				public void timeEnd() {
					//时间结束的回调
				}
				@Override
				public void remainFiveMinutes() {
				}
			});
		}else{
			aid=getIntent().getData().getQueryParameter("aid");
		}

		
		/*if (!StringUtil.isEmpty(image)) {
			bitmap = BitmapUtils.loadImageDefault(context,context.getCacheDir(), ConstantsOnline.SERVER_IMAGEURL
					+ image,callback, true);
			if (bitmap != null) {
				rl_bg_sport.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
		}*/
		
		if (!StringUtil.isEmpty(image)) {
			bitmap = OnlineApp.myApp.imageLoader.loadImageSync(ConstantsOnline.SERVER_IMAGEURL+ image);
			if (bitmap != null) {
				rl_bg_sport.setBackgroundDrawable(new BitmapDrawable(bitmap));
			}
		}

		ptrlv = (PullToRefreshListView) contentView.findViewById(R.id.lv);

		// 下拉加载事件屏蔽
		ptrlv.setPullLoadEnabled(false);
		// 屏蔽包含下拉刷新,上拉加载事件
		ptrlv.setScrollLoadEnabled(true);
		ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多

		ptrlv.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉刷新
				getNewData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 上拉加载
				getMoreData();
			}
		});
		return contentView;
	}

	protected void getNewData() {
		// 下拉刷新<注：不使用下拉刷新功能>

		ptrlv.onPullDownRefreshComplete();// 结束下拉刷新

	}

	protected void getMoreData() {
		//上拉加载
		if (!isPullUpToRefresh) {
			isPullUpToRefresh = true;
			if (!StringUtil.isEmpty(totalPage)) {
				if (currentPage < Integer.parseInt(totalPage)) {
					currentPage++;
					initListData();
				}else {
					ShowUtils.showMsg(context, "没有更多数据");
					isPullUpToRefresh = false;
					ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
				} 
			}else {
//				getNewData();
				isPullUpToRefresh = false;
			}
		}
	}


	@SuppressLint("NewApi")
	@Override
	protected void initData() {
		//填充活动信息的数据
		Map<String, String> requestDataMap = ParamsMapUtil.SportDetail(context,
				aid, uid);
		RequestVo requestVo = new RequestVo(ConstantsOnline.SPORT_DETAIL,
				context, requestDataMap, new MyBaseParser<>(
						SportDetailBean.class));
		DataCallBack<SportDetailBean> dataCallBack = new DataCallBack<SportDetailBean>() {
			

			@Override
			public void processData(SportDetailBean object) {
				if (object != null) {
					if ("true".equals(object.success)) {

						mBean = object;
						picList = object.entity.activityInfo.picList;
						contactList = object.entity.activityInfo.contactList;
						sportTitle = object.entity.activityInfo.title;
						tv_sport_title.setText(sportTitle);//活动标题
						tv_browse.setText("浏览："+object.entity.activityInfo.browseNum);// 浏览
						tv_share.setText("分享："+object.entity.activityInfo.shareNum);// 分享
						tv_apply.setText("报名："+object.entity.activityInfo.joinNum);// 报名
						joinNumber=Integer.parseInt(object.entity.activityInfo.joinNum);
						tv_sport_content.setText(object.entity.activityInfo.content);// 活动详情
						//TODO 未实现
						//判断是否已收藏，收藏把星星置为白色 实心的
						if ("1".equals(object.entity.iscollected)) {
							setRightButton(R.drawable.iconfont_shoucang,null);
						}
						
						if("1".equals(object.entity.isjoin)){
							tv_baoming.setText("已报名");
							tv_baoming.setClickable(false);
							tv_baoming.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_greay_2x));
						}
						if (picList!=null&&picList.size()>0) {
							newUrlList.clear();
							for (int i = 0; i < picList.size(); i++) {
								newUrlList.add(picList.get(i).picPath);
							}
							gvAdapter = new GridViewAdapter(context,newUrlList);
							gv_photo.setAdapter(gvAdapter);
							
							CommonUtil.setGridViewHeightBasedOnChildren(context, gv_photo,4);
						}
						ContactAdapter lvAdapter = new ContactAdapter(context,contactList);
						lv_contact.setAdapter(lvAdapter);
						setListViewHeight(lv_contact);// 设置listview的高度
						sportStartTimes = object.entity.activityInfo.startDt;
						tv_sport_time.setText(sportStartTimes);//活动开始时间
						if (StringUtil.isEmpty(object.entity.activityInfo.endDt)) {
							tv_end_time.setVisibility(View.GONE);
						}else {
							tv_end_time.setText(object.entity.activityInfo.endDt);//活动结束时间
						}
						sportAddress = object.entity.activityInfo.address;
						tv_sport_address.setText(sportAddress);

						note_user_name.setText(object.entity.activityInfo.uname);
						note_user_position.setText(object.entity.activityInfo.uposition);
						if (!StringUtil.isEmpty(object.entity.activityInfo.ucompany)) {
							note_content.setText(object.entity.activityInfo.ucompany);
						}else {
							note_content.setText("公司未公开");
						}
						leaderId=object.entity.activityInfo.uid;
						// bitmapUtils.display(note_user_image,ConstantsOnline.SERVER_IMAGEURL+object.entity.activityInfo.avatar);
						/*if (!StringUtil.isEmpty(object.entity.activityInfo.avatar)) {
							bitmap = BitmapUtils.loadImageDefault(context,
									context.getCacheDir(), 
									ConstantsOnline.SERVER_IMAGEURL
									+ object.entity.activityInfo.avatar,
									callback, true);
							if (bitmap != null) {
								note_user_image.setImageBitmap(bitmap);
							}
						}*/
						
						/*OnlineApp.myApp.imageLoader.displayImage(
				        		  ConstantsOnline.SERVER_IMAGEURL+object.entity.activityInfo.avatar, 
				        		  note_user_image, 
				        		  OnlineApp.myApp.builder.build()
				        		  );*/
						ImageUtils.setImage(object.entity.activityInfo.avatar, note_user_image, 0);
						image = object.entity.activityInfo.logo;
						if (!StringUtil.isEmpty(image)) {
							bitmap = OnlineApp.myApp.imageLoader.loadImageSync(ConstantsOnline.SERVER_IMAGEURL+ image);
							if (bitmap != null) {
								rl_bg_sport.setBackgroundDrawable(new BitmapDrawable(bitmap));
							}
						}

						//倒计时
						long endTime = StringUtil.stringToMillis(object.entity.activityInfo.startDt);
						tv_countdown.setEndTime(endTime, 1);
						tv_countdown.setClockListener(new ClockListener() {
							@Override
							public void timeEnd() {
								//时间结束的回调
							}

							@Override
							public void remainFiveMinutes() {
							}
						});
					}
				} else {
					ShowUtils.showMsg(context, "数据请求失败，请稍后重试");
				}
			}

		};
		getDataServer(requestVo, dataCallBack);
		initListData();
		if (ptrlv.getRefreshableView().getHeaderViewsCount() < 1) {
			ptrlv.getRefreshableView().addHeaderView(headView);
		}
		ll_loading.setVisibility(8);
	}

	/**
	 * 获取已报名学友列表的数据
	 */
	private void initListData() {
		
		Map<String, String> requestDataMap;
		requestDataMap = ParamsMapUtil.SportMate(context, aid, uid,
				String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.SPORT_DETAIL,
				context, requestDataMap,
				new MyBaseParser<>(SportMateBean.class));
		DataCallBack<SportMateBean> coursseDataCallBack = new DataCallBack<SportMateBean>() {
			@Override
			public void processData(SportMateBean object) {
				if (object == null) {
					if (isPullDownToRefresh) {
						ptrlv.onPullDownRefreshComplete();// 结束下拉刷新
						isPullDownToRefresh = false;
					} else if (isPullUpToRefresh) {
						ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
						isPullUpToRefresh = false;
					} else {
						ShowUtils.showMsg(context, "获取已报名学友数据失败，请稍后重试");
					}
				} else {
					mateBean = object;
					handler.obtainMessage(0).sendToTarget();
					String stringDate = StringUtil.getStringDate();
					ptrlv.setLastUpdatedLabel(stringDate.subSequence(stringDate.indexOf("-")+1, stringDate.lastIndexOf(":")));
				}
			}
		};
		getDataServer(requestVo, coursseDataCallBack);
	}

	
	
	
	/**
	 * 加入活动的学友的列表接口
	 */
	@SuppressLint("NewApi")
	public void initJoinSport(){
		Map<String, String> requestDataMap = ParamsMapUtil.SportDetail(context, aid, uid);
		RequestVo requestVo = new RequestVo(ConstantsOnline.JOIN_SPORT,
				context, requestDataMap,
				new MyBaseParser<>(JoinSportBean.class));
		DataCallBack<JoinSportBean> coursseDataCallBack = new DataCallBack<JoinSportBean>() {

			@Override
			public void processData(JoinSportBean object) {
				if (object!=null) {
					if ("true".equals(object.success)) {
						ShowUtils.showMsg(context, object.message);
						tv_baoming.setText("已报名");
						tv_baoming.setClickable(false);
						tv_baoming.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.btn_greay_2x));
						
						tv_apply.setText("报名："+(joinNumber+1));// 报名
						initListData();
						
					}
				}else {
					if (isPullUpToRefresh) {
						ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
						isPullUpToRefresh = false;
					}
					ShowUtils.showMsg(context, object.message);
				}
			}
		};
		getDataServer(requestVo, coursseDataCallBack);
	}
	/**
	 * 加入活动的javabean
	 * @author Administrator
	 *
	 */
	class JoinSportBean{
		public String message;
		public String success;
	}

	@Override
	protected void initListener() {
		super.initListener();
		tv_baoming.setOnClickListener(this);
		note_user_image.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.tv_baoming:
			initJoinSport();
			break;
		case R.id.note_user_image:
			if (!StringUtil.isEmpty(leaderId)) {
				Intent intent = new Intent(context, UserShowActivity.class);
				intent.putExtra("userId", leaderId);
				context.startActivity(intent);
			}
			break;

		default:
			break;
		}
	}

	//展示出来的图片的URL集合(包括)
	List<String> newUrlList = new ArrayList<String>();
	
	/**
	 * 活动详情页面，活动照片GridVeiw的Adapter
	 * 
	 * @author Administrator
	 * 
	 */
	public class GridViewAdapter extends BaseMyAdapter<String> {

		public GridViewAdapter(Context context, List<String> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			// 复用
			if (convertView == null) {
				contentView = View.inflate(context, R.layout.pager_sportphoto_item,null);
				holder = new ViewHolder();
				holder.ib_zhiku_item = (ImageButton) contentView.findViewById(R.id.ib_zhiku_item);

				contentView.setTag(holder);
			} else {
				contentView = convertView;
				holder = (ViewHolder) contentView.getTag();
			}
			/*if (!StringUtil.isEmpty(picList.get(position).picPath)) {
				bitmap = BitmapUtils.loadImageDefault(context,
						context.getCacheDir(), ConstantsOnline.SERVER_IMAGEURL
						+ picList.get(position).picPath, callback, false);
				if (bitmap != null) {
					holder.ib_zhiku_item.setImageBitmap(bitmap);
				}
			}else {
				//没有活动图片展示默认图
//				holder.ib_zhiku_item.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.wodeimg_default));
				holder.ib_zhiku_item.setImageDrawable(getResources().getDrawable(R.drawable.wodeimg_default));
				
			}*/
			
			/*OnlineApp.myApp.imageLoader.displayImage(
	        		  ConstantsOnline.SERVER_IMAGEURL+picList.get(position).picPath, 
	        		  holder.ib_zhiku_item, 
	        		  OnlineApp.myApp.builder
	        			.showImageOnLoading(R.drawable.wodeimg_default)  
	        	        .showImageOnFail(R.drawable.wodeimg_default)
	        	        .build());*/
			
			
//			ImageUtils.setImage(picList.get(position).picPath, holder.ib_zhiku_item, R.drawable.wodeimg_default);
			ImageUtils.setImage(list.get(position), holder.ib_zhiku_item, R.drawable.wodeimg_default);
				holder.ib_zhiku_item.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
//						if (bitmap != null) {
							Intent intent = new Intent(context,ImageActivity.class);
							intent.putExtra("image_list", (Serializable) newUrlList);
							intent.putExtra("image_position", position);
							context.startActivity(intent);
//						}else{
//							ShowUtils.showMsg(context, "图片加载失败");
//						}
					}
				});
			
			return contentView;
		}


		private class ViewHolder {

			ImageButton ib_zhiku_item;
		}
	}
	/**
	 * 活动联系人列表的adapter
	 * @author Administrator
	 *
	 */
	class ContactAdapter extends BaseMyAdapter<ContactItem> {

		public ContactAdapter(Context context, List<ContactItem> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View contactView = View.inflate(context,
					R.layout.activity_sport_contact_item, null);
			ll_text = (LinearLayout) contactView.findViewById(R.id.ll_text);
			View textView;
			TextView tv_text;
			if (!StringUtil.isEmpty(contactList.get(position).contact)) {
				textView = View.inflate(context, R.layout.layout_textview, null);
				tv_text = (TextView) textView.findViewById(R.id.tv_text);
				tv_text.setText(contactList.get(position).contact);
				tv_text.setTextColor(context.getResources().getColor(R.color.course_doc));
				ll_text.addView(textView);
			}
			if (!StringUtil.isEmpty(contactList.get(position).mobile)) {
				textView = View.inflate(context, R.layout.layout_textview, null);
				tv_text = (TextView) textView.findViewById(R.id.tv_text);
				tv_text.setText(contactList.get(position).mobile);
				tv_text.setTextColor(context.getResources().getColor(R.color.course_doc));
				ll_text.addView(textView);
			}
			if (!StringUtil.isEmpty(contactList.get(position).email)) {
				textView = View.inflate(context, R.layout.layout_textview, null);
				tv_text = (TextView) textView.findViewById(R.id.tv_text);
				tv_text.setText(contactList.get(position).email);
				tv_text.setTextColor(context.getResources().getColor(R.color.course_doc));
				ll_text.addView(textView);
			}

			// tv_sport_linkmen.setText(name);
			return contactView;

		}
	}

	/**
	 * 设置Listview的高度
	 */
	public void setListViewHeight(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


	private OnekeyShare oks;
	private View contentView;
//	protected void onResume() {
//		if (!isFistInitdata) {
//			initData();
//		}else {
//			isFistInitdata = false;
//		}
//		super.onResume();
//	}
}
