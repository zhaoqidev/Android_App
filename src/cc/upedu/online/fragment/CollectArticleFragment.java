package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.ArticleDetailsActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.CollectArticleAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.CollectArticleBean;
import cc.upedu.online.domin.CollectArticleBean.ArticleItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 我的收藏--文章收藏
 * @author Administrator
 *
 */
public class CollectArticleFragment extends RecyclerViewBaseFragment<ArticleItem>{
	private CollectArticleBean bean;
	
	public CollectArticleFragment(Context context, String userId) {
		super(context);
		this.userId = userId;
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list==null) {
						list = new ArrayList<ArticleItem>();
					}else {
						list.clear();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.message);
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	public void refreshDataSetChanged(Boolean extraData) {
		this.extraData = extraData;
		if (!isAdapterEmpty() && getAdapter().getItemCount() > 0) {
			((CollectArticleAdapter)getAdapter()).setManaging(extraData);
			notifyData();
		}else {
			setNocontentVisibility(View.VISIBLE);
		}
	}
	private Boolean extraData = false;
	public boolean isCanManagingArticle() {
		if (list != null && list.size() > 0) {
			return true;
		}else {
			return false;
		}
	}

	private void setData() {
		totalPage = bean.entity.totalPageSize;
		canLodeNextPage();
		list.addAll(bean.entity.articleList);
		if (isAdapterEmpty()) {
			setRecyclerView(new CollectArticleAdapter(context, list,extraData),R.drawable.nocollect_articledata);
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context,ArticleDetailsActivity.class);
					intent.putExtra("articleId",(Serializable) list.get(position).articleId);
					intent.putExtra("articleTitle",(Serializable) list.get(position).title);
					intent.putExtra("articleImage",(Serializable) list.get(position).picPath);
					intent.putExtra("clickTimes",(Serializable) list.get(position).clickTimes);
					intent.putExtra("addtime",(Serializable) list.get(position).clickTimes);
					intent.putExtra("iscollected","true");
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}
	private String userId;
	@Override
	public void initData() {
		//获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getCollectArticle(context, userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_COLLECTARTICLE, context, requestDataMap, new MyBaseParser<>(CollectArticleBean.class));
		DataCallBack<CollectArticleBean> CollectArticleDataCallBack = new DataCallBack<CollectArticleBean>() {

			@Override
			public void processData(CollectArticleBean object) {
				if (object==null) {
					objectIsNull();
				}else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, CollectArticleDataCallBack);

	}

	public void getData() {
		currentPage = 1;
		initData();
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}

}
