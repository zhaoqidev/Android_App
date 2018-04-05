package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.CourseIntroduceActivity;
import cc.upedu.online.activity.TelecastApplayActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.CollectCoureAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.CollectCoureListBean;
import cc.upedu.online.domin.CollectCoureListBean.Entity.CollectCoureItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 我的收藏--课程收藏
 * @author Administrator
 *
 */
public class CollectCoureFragment extends RecyclerViewBaseFragment<CollectCoureItem>{
	private CollectCoureListBean bean;

	public CollectCoureFragment(Context context, String userId) {
		super(context);
		this.userId = userId;
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.getSuccess())) {
				if (!isLoadMore()) {
					if(list==null){
						list = new ArrayList<CollectCoureItem>();
					}else{
						list.clear();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.getMessage());
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};
	public void refreshDataSetChanged(boolean extraData) {
		this.extraData = extraData;
		if (!isAdapterEmpty() && getAdapter().getItemCount() > 0) {
			((CollectCoureAdapter)getAdapter()).setManaging(extraData);
			notifyData();
		}else {
			setNocontentVisibility(View.VISIBLE);
		}
	}

	private Boolean extraData = false;
	public boolean isCanManagingCoure() {
		if (list != null && list.size() > 0) {
			return true;
		}else {
			return false;
		}
	}

	private void setData() {
		totalPage = bean.getEntity().gettotalPageSize();
		//判断是否可以加载下一页
		canLodeNextPage();
		list.addAll(bean.getEntity().getCourseList());
		if (isAdapterEmpty()) {
			setRecyclerView(new CollectCoureAdapter(context, list,extraData),R.drawable.nocollect_coursedata);
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					if ("LIVE".equals(list.get(position).getCourseType())) {
						Intent intent = new Intent(context, TelecastApplayActivity.class);
						intent.putExtra("courseId", list.get(position).getCourseId());
						context.startActivity(intent);
					}else {
						Intent intent = new Intent(context, CourseIntroduceActivity.class);
						intent.putExtra("courseId", list.get(position).getCourseId());
						context.startActivity(intent);
					}
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
		Map<String, String> requestDataMap = ParamsMapUtil.getCollectCoure(context, userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_COLLECTCOURE, context, requestDataMap, new MyBaseParser<>(CollectCoureListBean.class));
		DataCallBack<CollectCoureListBean> CollectCoureDataCallBack = new DataCallBack<CollectCoureListBean>() {

			@Override
			public void processData(CollectCoureListBean object) {
				if (object==null) {
					objectIsNull();
				}else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, CollectCoureDataCallBack);

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
