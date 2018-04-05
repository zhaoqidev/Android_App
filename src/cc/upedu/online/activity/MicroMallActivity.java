package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.MicroMallListAdapter;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.AdRollViewBean;
import cc.upedu.online.domin.MicroMallListFromVdianBean;
import cc.upedu.online.domin.MicroMallListFromVdianBean.Item;
import cc.upedu.online.domin.MyWalletBean;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.utils.UserStateUtil.FailureCallBack;
import cc.upedu.online.utils.UserStateUtil.SuccessCallBack;
import cc.upedu.online.view.pullrefreshview.PullToRefreshBase;
import cc.upedu.online.view.pullrefreshview.PullToRefreshBase.OnRefreshListener;
import cc.upedu.online.view.pullrefreshview.PullToRefreshListView;
/**
 * 微商城页面
 * @author Administrator
 *
 */
public class MicroMallActivity extends TitleBaseActivity{

	PullToRefreshListView ptrlv;// 商品列表的listview
	
	LinearLayout ll_nodata;//没有数据时显示的布局
	LinearLayout ll_loading;//正在加载时显示的布局
	
	View mall_head;//商品列表上方的图片
	
	String userId;//用户Id
	
	TextView tv_coin_number;//显示成长币个数
	ImageView iv_bg_card;//商城头布局图片
	
	boolean isPullDownToRefresh = false;// 记录是否是下拉刷新操作
	boolean isPullUpToRefresh = false; // 记录是否是上拉加载操作
	
	private final int number=50;//每一页请求的商品个数
	
	public int currentPage = 1;//当前页
//	public String totalPage;//总页
	public String coin;
	/**
	 * 微店token
	 */
	public String token;
	
	private MicroMallListFromVdianBean bean=new MicroMallListFromVdianBean();
	List<Item> list=new ArrayList<Item>();
	MicroMallListAdapter adapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("success".equals(bean.status.status_reason)) {
				if (isPullUpToRefresh) {
					if (bean.result!=null&&bean.result.items.size()>0) {
						canLoadNextpage();
						list.addAll(bean.result.items);
//						if (adapter == null) {
							adapter = new MicroMallListAdapter(context, list,coin,token);
							ptrlv.getRefreshableView().setAdapter(adapter);
							
//						} else {
//							adapter.notifyDataSetChanged();
//						}
					}

					ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
					isPullUpToRefresh = false;
				}else {
						list.clear();
						setData();
						ptrlv.onPullDownRefreshComplete();// 结束下拉刷新
						isPullDownToRefresh = false;
				}

			} else {
				ShowUtils.showMsg(context, bean.status.status_reason);
				ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
				isPullUpToRefresh = false;
				ptrlv.onPullDownRefreshComplete();// 结束下拉刷新
				isPullDownToRefresh = false;
			}
		}
	};

	private void canLoadNextpage(){
		if (list.size()%number==0) {
			ptrlv.setScrollLoadEnabled(true);
		}else {
			ptrlv.setScrollLoadEnabled(false);
			isPullUpToRefresh = false;
			ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
		}
	}

	private void setData() {
//		totalPage = bean.entity.totalPageSize;
//		if (currentPage < Integer.valueOf(totalPage)) {
//			ptrlv.setScrollLoadEnabled(true);
//		}else {
//			ptrlv.setScrollLoadEnabled(false);
//		}
//		if (Integer.parseInt(totalPage) > 0) {
//			ll_nodata.setVisibility(View.GONE);
//			list.addAll(bean.entity.commodityList);
//		}else {
//			ShowUtils.showMsg(context, "暂时没有数据哦~");
//			ll_nodata.setVisibility(View.VISIBLE);
//			
//		}
		 
		 if (bean.result!=null&&bean.result.items.size()>0) {
			ll_nodata.setVisibility(View.GONE);
			list.addAll(bean.result.items);
		}else {
			ll_nodata.setVisibility(View.VISIBLE);
		}
		 canLoadNextpage();
		if (list.size() > 0) {
			if (adapter == null) {
				adapter = new MicroMallListAdapter(context, list,coin,token);
				ptrlv.getRefreshableView().setAdapter(adapter);
				
				ll_loading.setVisibility(View.GONE);
			} else {
				if (isPullDownToRefresh) {
					adapter = new MicroMallListAdapter(context, list,coin,token);
					ptrlv.getRefreshableView().setAdapter(adapter);
					
					ptrlv.onPullDownRefreshComplete();// 结束下拉刷新
					isPullDownToRefresh = false;
				} else {
					adapter.notifyDataSetChanged();
				}
			}
//			//把图片加载到列表上方
			if (ptrlv.getRefreshableView().getHeaderViewsCount() < 1) {
				ptrlv.getRefreshableView().addHeaderView(mall_head);
			}
		}
		
	}

	
	/**
	 * 下拉刷新
	 */
	protected void getNewData() {
		if (!isPullDownToRefresh) {
			isPullDownToRefresh = true;
			currentPage = 1;
			initData();
		}
	}

	/**
	 * 上拉加载
	 */
	protected void getMoreData() {
		//上拉加载
		if (!isPullUpToRefresh) {
			isPullUpToRefresh = true;
			currentPage++;
			initData();
		}
	}

	@Override
	protected void initData() {
		//获取成长币个数
//		getCoinNumber();
		//获取商品列表的数据
//		getGoodsList();
		
		getAccessToken(null);
		

	}

	/**
	 * 获取头布局图片
	 */
	private void getHeadImage(){
		RequestVo requestVo = new RequestVo(ConstantsOnline.HOME_ROLLVIEW, context,
				ParamsMapUtil.getRollView(context, "shopCenterBanner"), new MyBaseParser<>(
				AdRollViewBean.class));

		DataCallBack<AdRollViewBean> dataCallBackCoin = new DataCallBack<AdRollViewBean>() {

			@Override
			public void processData(AdRollViewBean object) {
				if (object != null) {
					if ("false".equals(object.getSuccess())) {
						ShowUtils.showMsg(context, object.getMessage());
					} else {
						if (object.getEntity()!=null&&object.getEntity().getShopCenterBanner().size()>0) {
							ImageUtils.setImage(object.getEntity().getShopCenterBanner().get(0).getImagesUrl(),
									iv_bg_card,R.drawable.mall_head);
						}else {

						}

					}
				}else{
					ShowUtils.showMsg(context,"获取数据失败，请稍后重试");
				}
			}
		};

		getDataServer(requestVo, dataCallBackCoin);
	}


	/**
	 * 获取成长币个数
	 */
	private void getCoinNumber() {
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_WALLET, context,
				ParamsMapUtil.myWallet(context, userId), new MyBaseParser<>(
						MyWalletBean.class));
		
		DataCallBack<MyWalletBean> dataCallBackCoin = new DataCallBack<MyWalletBean>() {

			@Override
			public void processData(MyWalletBean object) {
				if (object != null) {
					if ("false".equals(object.success)) {
						ShowUtils.showMsg(context, object.message);
					} else {
						if (StringUtil.isEmpty(object.entity.coin)) {
							coin="0";
						}else {
							coin=object.entity.coin;
						}
						tv_coin_number.setText(coin);
						handler.obtainMessage().sendToTarget();
					}
				}else{
					ShowUtils.showMsg(context,"获取数据失败，请稍后重试");
				}
			}
		};
		
		getDataServer(requestVo, dataCallBackCoin);
	}
	
	/**
	 * 获取token
	 */
	private void getAccessToken(String flag) {
		
		Map<String, String> requestDataMap = ParamsMapUtil.getToken(context, flag);
		RequestVo requestVo = new RequestVo(ConstantsOnline.ACCESSTOKEN, context,
				requestDataMap, new MyBaseParser<>(getTokenBean.class));
		
		DataCallBack<getTokenBean> dataCallBack = new DataCallBack<getTokenBean>() {

			@Override
			public void processData(getTokenBean object) {
				if (object != null) {
					if ("false".equals(object.success)) {
						ShowUtils.showMsg(context, object.message);
					} else {
						if (!StringUtil.isEmpty(object.entity)) {
							token=object.entity;
							getGoodsList();
						}else {
							ShowUtils.showMsg(context,"获取数据失败，请尝试刷新重试");
						}
					}
				}else{
					ShowUtils.showMsg(context,"获取数据失败，请稍后重试");
				}
			}
		};
		
		getDataServer(requestVo, dataCallBack);
	}
	
	String param;
	String pub;
	/**
	 * 
	 */
	private void getGoodsList() {
		Gson gson = new Gson();
		
		Map<String,Integer> paramMap=new HashMap<String, Integer>();
		paramMap.put("page_num", currentPage);
		paramMap.put("page_size", number);
		paramMap.put("orderby", 1);
		
		Map<String, String> pubMap=new HashMap<String, String>();
		pubMap.put("access_token", token);
		pubMap.put("format", "json");
		pubMap.put("method", "vdian.item.list.get");
		pubMap.put("version", "1.0");
		 
		Map<String, String> requestDataMap = ParamsMapUtil.getListFromVDian(context, gson.toJson(paramMap),gson.toJson(pubMap));
		RequestVo requestVo = new RequestVo(ConstantsOnline.GET_LIST_VDIAN,context, requestDataMap, new MyBaseParser<>(
				MicroMallListFromVdianBean.class));
		DataCallBack<MicroMallListFromVdianBean> dataCallBack = new DataCallBack<MicroMallListFromVdianBean>() {

			@Override
			public void processData(MicroMallListFromVdianBean object) {
				if (object == null) {
					if (isPullDownToRefresh) {
						ptrlv.onPullDownRefreshComplete();// 结束下拉刷新
						isPullDownToRefresh = false;
					} else if (isPullUpToRefresh) {
						ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
						isPullUpToRefresh = false;
					} else {
						ShowUtils.showMsg(context, "获取商品数据失败请联系客服");
					}
				} else {
					bean = object;
					if ("10013".equals(object.status.status_code)) {
						if (requestNumber<3) {
							requestNumber++;
							getAccessToken("true");//token过期，重新请求token
						}

					}else {
						if ("0"!=UserStateUtil.getUserId()) {
							getCoinNumber();
						}else {
							tv_coin_number.setText("0");
							handler.obtainMessage().sendToTarget();
						}
					}
				}
			}
		};
		getDataServer(requestVo, dataCallBack);
	}
	private int requestNumber=0;
	/**
	 * 获取商品列表的数据
	 
	private void getGoodsList() {
		Map<String, String> requestDataMap = ParamsMapUtil.getMicroMallGoods(context, String.valueOf(currentPage),"20");
		RequestVo requestVo = new RequestVo(ConstantsOnline.MICROMALL,context, requestDataMap, new MyBaseParser<>(
						MicroMallBean.class));
		DataCallBack<MicroMallBean> dataCallBack = new DataCallBack<MicroMallBean>() {
			@Override
			public void processData(MicroMallBean object) {
				if (object == null) {
					if (isPullDownToRefresh) {
						ptrlv.onPullDownRefreshComplete();// 结束下拉刷新
						isPullDownToRefresh = false;
					} else if (isPullUpToRefresh) {
						ptrlv.onPullUpRefreshComplete();// 结束上拉加载更多
						isPullUpToRefresh = false;
					} else {
						ShowUtils.showMsg(context, "获取订单数据失败请联系客服");
					}
				} else {
					bean = object;
					if ("0"!=UserStateUtil.getUserId()) {
						getCoinNumber();
					}else {
						tv_coin_number.setText("0");
						handler.obtainMessage().sendToTarget();
					}
//					String stringDate = StringUtil.getStringDate();
//					ptrlv.setLastUpdatedLabel(stringDate.subSequence(stringDate.indexOf("-")+1, stringDate.lastIndexOf(":")));
				}
			}
		};
		getDataServer(requestVo, dataCallBack);
	}
*/
	
	class getTokenBean{
		public String message;
		public String success;
		public String entity;
		
	}


	@Override
	protected void initTitle() {
		setTitleText("微商城");
		setRightButton(R.drawable.exchange_record, new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				if (!UserStateUtil.isLogined()) {
					ShowUtils.showDiaLog(context, "温馨提醒", "您还没有登录,请先登录.", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							//跳转到登录界面
							Intent intent = new Intent(context, LoginActivity.class);
							startActivity(intent);
						}
					});
				}else {
					UserStateUtil.loginInFailuer(context,new FailureCallBack() {
						
						@Override
						public void onFailureCallBack() {
							// TODO Auto-generated method stub
							ShowUtils.showDiaLog(context, "温馨提醒", "登录已过期,请重新登录.", new ConfirmBackCall() {
								@Override
								public void confirmOperation() {
									//跳转到登录界面
									Intent intent = new Intent(context, LoginActivity.class);
									startActivity(intent);
								}
							});
						}
					},new SuccessCallBack() {
						
						@Override
						public void onSuccessCallBack() {
							Intent intent = new Intent(context, MicroMallExchangeRecordActivity.class);
							startActivity(intent);
						}
					});
				}
				
			}
		});
		
	}

	@Override
	protected View initContentView() {

		View view =View.inflate(context, R.layout.activity_micromall, null);
		ptrlv = (PullToRefreshListView) view.findViewById(R.id.lv);

		ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
		ll_loading=(LinearLayout) view.findViewById(R.id.ll_loading);
		
		userId = UserStateUtil.getUserId();//获取用户ID

		mall_head=View.inflate(context, R.layout.activity_micromall_head, null);
		
		tv_coin_number=(TextView) mall_head.findViewById(R.id.tv_coin_number);
		iv_bg_card= (ImageView) mall_head.findViewById(R.id.iv_bg_card);

		getHeadImage();//获取头布局图片
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
		return view;
	}

}
