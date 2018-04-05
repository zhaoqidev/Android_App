package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.TeacherVisitCard;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.DaoshiAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.DaoshiBean;
import cc.upedu.online.domin.DaoshiBean.TeacherItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 课程体系中的导师界面
 * 
 * @author Administrator
 *
 */
public class ArchitectureTheacherFragment extends RecyclerViewBaseFragment<TeacherItem> {
	private DaoshiBean bean = new DaoshiBean();
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list==null) {
						list = new ArrayList<TeacherItem>();
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
	private String subjectId;
	public ArchitectureTheacherFragment(Context context,String subjectId) {
		super(context);
		this.subjectId = subjectId;
	}
	
	private void setData() {
		totalPage = bean.entity.totalPage;
		canLodeNextPage();
		list.addAll(bean.entity.teacherList);
		if (isAdapterEmpty()) {
			setRecyclerView(new DaoshiAdapter(context, list));
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub

					Intent intent = new Intent(context, TeacherVisitCard.class);
					intent.putExtra("teacherId", list.get(position).teacherId);
					intent.putExtra("teacherPosition", list.get(position).intro);
					intent.putExtra("teacherName", list.get(position).name);
					intent.putExtra("teacherLogo", list.get(position).picPath);
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}
	@Override
	public void initData() {
		//获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getDaoshi(context,subjectId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.DAOSHI_LIST, context,
				requestDataMap, new MyBaseParser<>(DaoshiBean.class));
		DataCallBack<DaoshiBean> dataCallBack = new DataCallBack<DaoshiBean>() {
			@Override
			public void processData(DaoshiBean object) {
				if (object == null) {
					objectIsNull();
				} else {
					bean = object;
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
