package cc.upedu.online.activity;

import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.MyWalletDaoshiAdapter;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.MyWalletDetailBean;
import cc.upedu.online.domin.MyWalletDetailBean.recordItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView.PullLoadMoreListener;

/**
 * 导师和代理商的零钱收入明细界面
 * @author Administrator
 *
 */
public class MyWalletDetailDaoshiAndAgentActivity extends TitleBaseActivity implements PullLoadMoreListener{
	LinearLayout ll_nodata;//无数据布局
	TextView tv_search_time;//查询时间的范围
//	TextView tv_money;//总金额
	String time;//查询区间
	String userType;//用户类型，0:普通用户 1:导师 2:代理商
	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;//可以下拉刷新，上拉加载的RecyclerView
	boolean isPullDownToRefresh = false;// 记录是否是下拉刷新操作
	boolean isPullUpToRefresh = false;// 记录是否是下拉加载操作
	
	private int currentPage = 1;// 当前数据加载到哪个page
	private String totalPage;// 总页数
	
	private MyWalletDetailBean bean = new MyWalletDetailBean();
	private MyWalletDaoshiAdapter adapter;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("收支明细");
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.activity_mywallet_detail_daoshiandagent, null);
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
		mPullLoadMoreRecyclerView.setLinearLayout();
		mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
		mPullLoadMoreRecyclerView.setIsRefresh(true);
//		mPullLoadMoreRecyclerView.setItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));

		tv_search_time = (TextView) view.findViewById(R.id.tv_search_time);
		
		userType=UserStateUtil.getUserType();
		time=getIntent().getStringExtra("searchTime");
		if (time.contains(":")) {
			tv_search_time.setText(time.replace(":", "至"));
		}else {
			tv_search_time.setText(time);
		}
		
		ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
		return view;
	}
	
	@Override
	protected void initData() {

		Map<String, String> requestDataMap;
		RequestVo requestVo = null;
		requestDataMap = ParamsMapUtil.myWalletDaoshiAndAgentTime(context, UserStateUtil.getUserId(),time,String.valueOf(currentPage));
		if("1".equals(userType)){//导师
			requestVo = new RequestVo(ConstantsOnline.MY_WALLET_USER, context,
					requestDataMap, new MyBaseParser<>(MyWalletDetailBean.class));
		}else if("2".equals(userType)){//代理商
			requestVo = new RequestVo(ConstantsOnline.MY_WALLET_AGENT, context,
					requestDataMap, new MyBaseParser<>(MyWalletDetailBean.class));
		}
		DataCallBack<MyWalletDetailBean> dataCallBack = new DataCallBack<MyWalletDetailBean>() {
			@Override
			public void processData(MyWalletDetailBean object) {
				if (object == null) {
					if (mPullLoadMoreRecyclerView.isLoadMore()||mPullLoadMoreRecyclerView.isRefresh()) {
						mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
					} else {
						ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
					}
				} else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, dataCallBack);
		
	}
	
private List<recordItem> list = new ArrayList<recordItem>();
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (mPullLoadMoreRecyclerView.isLoadMore()) {
					list.addAll(bean.entity.recordList);
					if (list.size() > 0) {
						if (adapter == null) {
							adapter = new MyWalletDaoshiAdapter(context, list);
							mPullLoadMoreRecyclerView.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
					}
					//判断是否可以加载下一页
					if (currentPage < Integer.valueOf(totalPage)) {
						mPullLoadMoreRecyclerView.setHasMore(true);
					} else {
						mPullLoadMoreRecyclerView.setHasMore(false);
					}

					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载更多
				} else {
					list.clear();
					setData();
					mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束下拉刷新
				}
			} else {
				ShowUtils.showMsg(context, bean.message);
				mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
			}
			
//			onItemClick();//添加条目点击事件的监听
		}
	};
	
	private void setData() {
		totalPage = bean.entity.totalPageSize;
		if (currentPage < Integer.valueOf(totalPage)) {
			mPullLoadMoreRecyclerView.setHasMore(true);
		}
		if (Integer.parseInt(totalPage) > 0) {
			ll_nodata.setVisibility(View.GONE);
			list.addAll(bean.entity.recordList);
		}else {
			//做无数据时的页面显示操作
//			ShowUtils.showMsg(context, "暂时没有数据哦~");
			ll_nodata.setVisibility(View.VISIBLE);
		}
		if (list.size() > 0) {
			if (adapter==null) {
				adapter = new MyWalletDaoshiAdapter(context, list);
				mPullLoadMoreRecyclerView.setAdapter(adapter);
			}else {		
				adapter.notifyDataSetChanged();
			}
		}
	}

	/**
	 * 下拉刷新
	 */
	@Override
	public void onRefresh() {
		currentPage=1;
		initData();
		
	}

	/**
	 * 上拉加载
	 */
	@Override
	public void onLoadMore() {

    	if (!StringUtil.isEmpty(totalPage)) {
			if (currentPage < Integer.parseInt(totalPage)) {
				currentPage++;
				initData();
			}else {
				ShowUtils.showMsg(context, "没有更多数据");
				mPullLoadMoreRecyclerView.setHasMore(false);
				mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
			}
		}else {
			onRefresh();
		}
	}
	
	/**
	 * 点击事件的处理
	 */
	/*private void onItemClick(){
		if (adapter!=null) {
			adapter.setOnItemClickLitener(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					
				}
			});
		}
	}*/

	
}
