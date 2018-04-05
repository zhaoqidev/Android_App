package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.TelecastApplayActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.TelecastAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.TelecastBean;
import cc.upedu.online.domin.TelecastBean.Entity.Live;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 直播列表页面
 * @author Administrator
 *
 */
public class TelecastFragment extends RecyclerViewBaseFragment<Live>{
		//推荐课程列表的Javabean文件
		private TelecastBean bean = null;
		public TelecastFragment(){

		}
		public TelecastFragment(Context context) {
			super(context);
		}
		private Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.getSuccess())) {
				if (!isLoadMore()) {
					if(list==null){
						list = new ArrayList<Live>();
					}else {
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
		
	
		private void setData() {
			totalPage = bean.getEntity().getTotalPage();
			//判断是否可以加载下一页
			canLodeNextPage();
			list.addAll(bean.getEntity().getLiveList());
			
			if (isAdapterEmpty()) {
				setRecyclerView(new TelecastAdapter(context, list));
				setOnItemClick(new OnItemClickLitener() {
					
					@Override
					public void onItemLongClick(View view, int position) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onItemClick(View view, int position) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(context, TelecastApplayActivity.class);
						intent.putExtra("courseId", list.get(position).getCourseId());
						//TODO add detail show data to intent's extra
						intent.putExtra("courseName", list.get(position).getTitle()==null?"":list.get(position).getTitle());
						intent.putExtra("startTime",  list.get(position).getStartTime()==null?"":list.get(position).getStartTime());
						context.startActivity(intent);
					}
				});
			}else {
				notifyData();
			}
		}

		@Override
		public void initData() {

			//获取直播列表的数据
			Map<String, String> requestDataMap = ParamsMapUtil.getAllCourse(context, String.valueOf(currentPage));
			RequestVo requestVo = new RequestVo(ConstantsOnline.HOME_LIVE, context, requestDataMap, new MyBaseParser<>(TelecastBean.class));
			DataCallBack<TelecastBean> coursseDataCallBack = new DataCallBack<TelecastBean>() {
				@Override
				public void processData(TelecastBean object) {
					if (object==null) {
						objectIsNull();
					}else {
						bean = object;
						handler.obtainMessage().sendToTarget();
					}
				}
			};
			getDataServer(requestVo, coursseDataCallBack);
		}

		public void seacherTelecast(String searchText){
			ShowUtils.showMsg(context, "搜索直播课程");
		}
		@Override
		protected void setPullLoadMoreRecyclerView() {
			// TODO Auto-generated method stub
			
		}

}
