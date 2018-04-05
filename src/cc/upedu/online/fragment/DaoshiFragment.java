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
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 发现->导师界面
 * 
 * @author Administrator
 * 
 */
public class DaoshiFragment extends RecyclerViewBaseFragment<TeacherItem>{
	private DaoshiBean bean = new DaoshiBean();

	private String search=null;//搜索内容

	public DaoshiFragment(){
	}

	public DaoshiFragment(Context context) {
		super(context);

	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if(list==null){
						list = new ArrayList<TeacherItem>();
					}else{
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
	
	private void setData() {
		totalPage = bean.entity.totalPage;
		//判断是否可以加载下一页
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

	
	public void getData() {
		currentPage = 1;
		search=null;
		initData();
	}
	@Override
	public void initData() {

		//获取课程列表的数据
		Map<String, String> requestDataMap;
		subjectId = SharedPreferencesUtil.getInstance().spGetString("downmenu_teacherSubjectId", "-1");
		if ("-1".equals(subjectId)) {
			requestDataMap = ParamsMapUtil.getDaoshi(context, String.valueOf(currentPage),search);
		}else {
			requestDataMap = ParamsMapUtil.getCourseDaoshi(context, subjectId, String.valueOf(currentPage));
		}
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
	public void seacherDaoshi(String searchText){
		ShowUtils.showMsg(context, "搜索导师");
		SharedPreferencesUtil.getInstance().editPutString("downmenu_teacherSubjectId", "-1");
		if ("查找全部".equals(searchText)) {
			search=null;
			currentPage=1;
			list.clear();
			initData();
		}else {
			search=searchText;//搜索内容
			currentPage=1;
			list.clear();
			initData();
		}
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	/**
	 * 根据特定的key,重新加载数据
	 * @param key
	 */
	public void initNewData(String key){
		subjectId = SharedPreferencesUtil.getInstance().spGetString(key, "-1");
		getData();
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}

}
