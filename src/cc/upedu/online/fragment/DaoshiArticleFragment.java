package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.ArticleDetailsActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.DaoshiArticleAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.DaoshiArticleBean;
import cc.upedu.online.domin.DaoshiArticleBean.ArticleItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 导师名片页面的文章列表
 * @author Administrator
 *
 */
public class DaoshiArticleFragment extends RecyclerViewBaseFragment<ArticleItem>{
	View view;

	private DaoshiArticleBean mDaoshiArticleBean = new DaoshiArticleBean();
	
	String teacherId;
	

	public DaoshiArticleFragment(Context context) {
		super(context);
	}

	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mDaoshiArticleBean.success)) {
				if (!isLoadMore()) {
					if (list==null) {
						list=new ArrayList<ArticleItem>();
					}else {
						list.clear();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, mDaoshiArticleBean.message);
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	
	private void setData() {
		totalPage = mDaoshiArticleBean.entity.totalPage;

		canLodeNextPage();
		list.addAll(mDaoshiArticleBean.entity.articleList);
		if (isAdapterEmpty()) {
			setRecyclerView(new DaoshiArticleAdapter(context, list));
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
					intent.putExtra("clickTimes",(Serializable) list.get(position).viewNum);
					intent.putExtra("addtime",(Serializable) list.get(position).addtime);
					intent.putExtra("iscollected",(Serializable) list.get(position).iscollected);
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}

	@Override
	public void initData() {
		teacherId=SharedPreferencesUtil.getInstance().spGetString("teacherId", "0");
		// 获取文章列表的数据
				Map<String, String> requestDataMap = ParamsMapUtil.DaoshiArticle(context,
						teacherId, String.valueOf(currentPage),UserStateUtil.getUserId());
				RequestVo requestVo = new RequestVo(ConstantsOnline.DAOSHI_ARTICLE,
						context, requestDataMap, new MyBaseParser<>(
								DaoshiArticleBean.class));
				DataCallBack<DaoshiArticleBean> dataCallBack = new DataCallBack<DaoshiArticleBean>() {
					@Override
					public void processData(DaoshiArticleBean object) {
						if (object == null) {
							objectIsNull();
						} else {
							mDaoshiArticleBean = object;
							handler.obtainMessage().sendToTarget();
						}
					}
				};
				getDataServer(requestVo, dataCallBack);
				
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
	}
}
