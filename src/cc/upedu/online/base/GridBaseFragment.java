package cc.upedu.online.base;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
/**
 * fragment中只有一个gridView可以继承该类
 * @author Administrator
 *
 * @param <T>
 */
public abstract class GridBaseFragment<T> extends BaseFragment implements OnItemClickLitener{
	/**
	 * RecyclerView
	 */
	public RecyclerView gridRecyclerView;
	/**
	 * RecyclerView的数据适配器
	 */
	public AbsRecyclerViewAdapter adapter;
	/**
	 * 无数据显示的布局
	 */
	public LinearLayout ll_nodata;
	/**
	 * 无数据显示的图片
	 */
	public ImageView iv_nodata;
	/**
	 * 列表数据集合 
	 */
	public List<T> list;
	
	public View view;

	public GridBaseFragment(){
		
	}
	
	@Override
	protected View initView(LayoutInflater inflater) {
		view = View.inflate(context, R.layout.fragment_recyclerview, null);
		gridRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_recyclerview);
		
		ll_nodata=(LinearLayout) view.findViewById(R.id.ll_nodata);
		iv_nodata=(ImageView) view.findViewById(R.id.iv_nodata);
		
		
		setGridRecyclerView();
		return view;
	}
	
	
	/**
	 * 点击事件的处理
	 */
	public void onItemClick(){
		if (adapter!=null) {
			adapter.setOnItemClickLitener(this);
		}
	}

	
	/**
	 * 配置PullLoadMoreRecyclerView
	 * 可以调用 ：
	 * 			setGridRecyclerView()设置gridview的每一行有多少个元素
	 */
	protected abstract void setGridRecyclerView();

	 /**
	  * 设置gridview的每一行有多少个元素,并设置RecyclerView为gridview
	  * @param spanCount 每一行元素的个数
	  */
	 public void setGridRecyclerView(int spanCount){
		 GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
		 gridRecyclerView.setLayoutManager(gridLayoutManager);
	 }
}
