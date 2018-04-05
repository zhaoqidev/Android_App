package cc.upedu.online.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
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
 * Fragment中只有一个listview可继承该类
 * 
 * @author Administrator
 * 
 * @param <T>
 *            列表条目类型
 */
public abstract class RecyclerViewBaseFragment<T> extends BaseFragment
		implements PullLoadMoreListener {
	/**
	 * 可下拉加载上拉刷新的RecyclerView
	 */
	private PullLoadMoreRecyclerView mPullLoadMoreRecyclerView;// 可以下拉刷新，上拉加载的RecyclerView
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

	private View view;

	public RecyclerViewBaseFragment() {
		super();
		view = View.inflate(context, R.layout.refresh_loadmore_recyclerview,
				null);
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view
				.findViewById(R.id.pullLoadMoreRecyclerView);
	}

	public RecyclerViewBaseFragment(Context context) {
		super();
		view = View.inflate(context, R.layout.refresh_loadmore_recyclerview,
				null);
		mPullLoadMoreRecyclerView = (PullLoadMoreRecyclerView) view
				.findViewById(R.id.pullLoadMoreRecyclerView);
	}

	@Override
	protected View initView(LayoutInflater inflater) {

		ll_nodata = (LinearLayout) view.findViewById(R.id.ll_nodata);
		iv_nodata = (ImageView) view.findViewById(R.id.iv_nodata);

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
		currentPage = 1;
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
			} else {
				ShowUtils.showMsg(context, "没有更多数据");
				setHasMore(false);
				setPullLoadMoreCompleted();
			}
		} else {
			onRefresh();
		}
	}

	/**
	 * 传入adapter，执行setAdapter
	 * 
	 * @param adapter
	 */
	protected void setRecyclerView(AbsRecyclerViewAdapter adapter) {
		this.adapter = adapter;
		if (adapter != null && adapter.getItemCount() > 0) {
			setNocontentVisibility(View.GONE);
			mPullLoadMoreRecyclerView.setAdapter(adapter);
		} else {
			if (adapter != null) {
				mPullLoadMoreRecyclerView.setAdapter(adapter);
			}
			setNocontentVisibility(View.VISIBLE);
		}
	}

	protected void setRecyclerView(AbsRecyclerViewAdapter adapter, int imgresid) {
		iv_nodata.setImageResource(imgresid);
		setRecyclerView(adapter);
	}

	/**
	 * 设置无数据布局是否显示
	 * 
	 * @param visibility
	 *            visible，gone，invisible
	 */
	protected void setNocontentVisibility(int visibility) {

		ll_nodata.setVisibility(visibility);
	}

	/**
	 * 设置条目的点击事件的处理
	 */
	protected void setOnItemClick(OnItemClickLitener mOnItemClickLitener) {
		if (mOnItemClickLitener != null) {
			adapter.setOnItemClickLitener(mOnItemClickLitener);
		}
	}

	/**
	 * 当请求数据为空时调用此方法
	 */
	public void objectIsNull() {
		setPullLoadMoreCompleted();
		ShowUtils.showMsg(context, "获取数据失败，请稍后重试");
	}

	/**
	 * 设置下拉刷新和上拉加载结束
	 */
	protected void setPullLoadMoreCompleted() {
		if (mPullLoadMoreRecyclerView.isLoadMore()
				|| mPullLoadMoreRecyclerView.isRefresh()) {
			mPullLoadMoreRecyclerView.setPullLoadMoreCompleted();
		}
	}

	/**
	 * 是否加载下一页
	 */
	protected void canLodeNextPage() {

		if (currentPage < Integer.valueOf(totalPage)) {
			setHasMore(true);
		} else {
			setHasMore(false);
		}

	}

	/**
	 * 设置是否加载下一页
	 */
	protected void setHasMore(boolean hasMore) {
		mPullLoadMoreRecyclerView.setHasMore(hasMore);
	}

	/**
	 * 配置PullLoadMoreRecyclerView,全部默认为false 可以调用 ：setIsRefresh()设置是否为刷新状态
	 * setItemDecoration()设置条目之间是否下划线
	 */
	protected abstract void setPullLoadMoreRecyclerView();

	/**
	 * 设置是否为刷新状态
	 * 
	 * @param isRefresh
	 *            true 刷新状态， false 正常状态
	 */
	public void setIsRefresh(boolean isRefresh) {
		mPullLoadMoreRecyclerView.setIsRefresh(isRefresh);
	}

	/**
	 * 设置条目之间是否下划线
	 * 
	 * @param isSet
	 *            true 有下划线， false 没有下划线
	 */
	public void setItemDecoration(boolean isSet) {
		if (isSet) {
			mPullLoadMoreRecyclerView
					.setItemDecoration(new DividerItemDecoration(context,
							LinearLayoutManager.VERTICAL));
		}
	}

	/**
	 * 设置是否可刷新
	 * 
	 * @param enable
	 *            true 可刷新，false 不可刷新
	 */
	public void setPullRefreshEnable(boolean enable) {
		if (mPullLoadMoreRecyclerView != null) {
			mPullLoadMoreRecyclerView.setPullRefreshEnable(enable);
		}
	}

	/**
	 * 在adapter存在的情况下刷新界面
	 */
	public void notifyData() {
		if (!isAdapterEmpty()){
			adapter.notifyDataSetChanged();
		}
		if (list.size() > 0) {
			setNocontentVisibility(View.GONE);
		}else {
			setNocontentVisibility(View.VISIBLE);
		}
	}

	/**
	 * 判断adapter是否为空 
	 * @return true：adapter为空     false：adapter不为空
	 */
	public boolean isAdapterEmpty() {
		if (adapter == null) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 返回adapter对象
	 * @return adapter
	 */
	public AbsRecyclerViewAdapter getAdapter() {
		return adapter;
	}
	/**
	 * 判断是否是下拉加载
	 * @return true：正在加载   false：不是加载状态
	 */
	public boolean isLoadMore() {
		return mPullLoadMoreRecyclerView.isLoadMore();
	}
}
