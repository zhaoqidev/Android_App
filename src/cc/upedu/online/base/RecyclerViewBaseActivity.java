package cc.upedu.online.base;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.pullrefreshRecyclerview.DividerItemDecoration;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView;
import cc.upedu.online.view.pullrefreshRecyclerview.PullLoadMoreRecyclerView.PullLoadMoreListener;
/**
 * 基于TitleBaseActivity,titlebar下方的主题内容为整个listview的activity
 * @author Administrator
 * @param <T>
 *
 */
public abstract class RecyclerViewBaseActivity<T> extends TitleBaseActivity implements PullLoadMoreListener{
	/**
	 * 可下拉加载上拉刷新的RecyclerView
	 */
	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;//可以下拉刷新，上拉加载的RecyclerView
	/**
	 * RecyclerView的数据适配器
	 */
	private AbsRecyclerViewAdapter adapter;
	/**
	 * 列表数据集合 
	 */
	public List<T> list;
	/**
	 * 无数据显示的布局
	 */
	private LinearLayout ll_nodata;
	/**
	 * 无数据显示的图片
	 */
	private ImageView iv_nodata;
	/**
	 * 当前数据加载到哪一页
	 */
	protected int currentPage = 1;
	/**
	 * 总页数
	 */
	protected String totalPage;
	@Override
	protected View initContentView() {
		View view = View.inflate(context, R.layout.refresh_loadmore_recyclerview, null);
		ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
		iv_nodata=(ImageView) view.findViewById(R.id.iv_nodata);
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view.findViewById(R.id.pullLoadMoreRecyclerView);
		mPullLoadMoreRecyclerView.setLinearLayout();
		mPullLoadMoreRecyclerView.setOnPullLoadMoreListener(this);
		setPullLoadMoreRecyclerView();
		return view;
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
				setHasMore(false);
				setPullLoadMoreCompleted();
			}
		}else {
			onRefresh();
		}
	}
	protected void setRecyclerView(AbsRecyclerViewAdapter adapter) {
		// TODO Auto-generated method stub
		this.adapter = adapter;
		if (adapter != null && adapter.getItemCount() > 0) {
			setNocontentVisibility(View.GONE);
			mPullLoadMoreRecyclerView.setAdapter(adapter);
		}else {
			if (adapter != null) {
				mPullLoadMoreRecyclerView.setAdapter(adapter);
			}
			setNocontentVisibility(View.VISIBLE);
		}
	}
	protected void setRecyclerView(AbsRecyclerViewAdapter adapter,int imgresid) {
		// TODO Auto-generated method stub
		iv_nodata.setImageResource(imgresid);
		setRecyclerView(adapter);
	}
	protected void setNocontentVisibility(int visibility) {
		// TODO Auto-generated method stub
		ll_nodata.setVisibility(visibility);
	}
	/**
	 * 设置条目的点击事件的处理
	 */
	protected void setOnItemClick(OnItemClickLitener mOnItemClickLitener){
		if (mOnItemClickLitener != null) {
			adapter.setOnItemClickLitener(mOnItemClickLitener);
		}
	}
	/**
	 * 当请求数据为空时调用此方法
	 */
	protected void objectIsNull(){
		setPullLoadMoreCompleted();
		ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
	}

	/**
	 * 
	 */
	protected void setPullLoadMoreCompleted() {
		if (mPullLoadMoreRecyclerView.isLoadMore()||mPullLoadMoreRecyclerView.isRefresh()) {
			mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
		}
	}
	/**
	 * 判断是否可以加载下一页
	 */
	protected void canLodeNextPage(){
		
		if (currentPage < Integer.valueOf(totalPage)) {
			setHasMore(true);
		} else {
			setHasMore(false);
		}

	}

	/**
	 * 
	 */
	protected void setHasMore(boolean hasMore) {
		mPullLoadMoreRecyclerView.setHasMore(hasMore);
	}
	
	/**
	 * 配置PullLoadMoreRecyclerView
	 * 可以调用 ：setIsRefresh()设置是否为刷新状态
	 * 		   setItemDecoration()设置条目之间是否下划线
	 */
	protected abstract void setPullLoadMoreRecyclerView();
	
	/**
	 * 设置是否为刷新状态
	 * @param isRefresh true 刷新状态， false 正常状态
	 */
	protected void setIsRefresh(boolean isRefresh){
		mPullLoadMoreRecyclerView.setIsRefresh(isRefresh);
	}
	/**
	 * 设置条目之间是否下划线
	 * @param isSet true 有下划线， false 没有下划线
	 */
	protected void setItemDecoration(boolean isSet){
		if (isSet) {
			mPullLoadMoreRecyclerView.setItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
		}
	}
	public void notifyData() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
		if (list.size() > 0) {
			setNocontentVisibility(View.GONE);
		}else {
			setNocontentVisibility(View.VISIBLE);
		}
	}
	public boolean isAdapterEmpty() {
		// TODO Auto-generated method stub
		if (adapter == null) {
			return true;
		}else {
			return false;
		}
	}
	public AbsRecyclerViewAdapter getAdapter(){
		return adapter;
	}

	public boolean isLoadMore() {
		// TODO Auto-generated method stub
		return mPullLoadMoreRecyclerView.isLoadMore();
	}
}
