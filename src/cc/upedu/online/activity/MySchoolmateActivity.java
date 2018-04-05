package cc.upedu.online.activity;

import android.os.Handler;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.MySchoolmateAdapter;
import cc.upedu.online.base.RecyclerViewBaseActivity;
import cc.upedu.online.domin.MySchoolmateBean;
import cc.upedu.online.domin.MySchoolmateBean.SchoolmateItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;

/**
 * 侧拉栏，我的关注的学友的界面
 * 
 * @author Administrator
 * 
 */
public class MySchoolmateActivity extends RecyclerViewBaseActivity<SchoolmateItem>{

	private MySchoolmateBean bean = new MySchoolmateBean();

	String type = "4";
	String userId;
//	private boolean isFistInitdata = true;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.success)) {
				if (!isLoadMore()) {
					if (list!=null) {
						list.clear();
					}else {
						list = new ArrayList<SchoolmateItem>();
					}
				}
				setData();
			} else {
				ShowUtils.showMsg(context, bean.message);
			}
			setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
		}
	};

//	protected void onResume() {
//		if (!isFistInitdata) {
//			currentPage=1;
//			initData();
//		}else {
//			isFistInitdata = false;
//		}
//		super.onResume();
//	};
	private void setData() {
		totalPage = bean.entity.totalPage;
		//判断是否可以加载下一页
		canLodeNextPage();

		list.addAll(bean.entity.followList);
		if (isAdapterEmpty()) {
			setRecyclerView(new MySchoolmateAdapter(context, list), R.drawable.noschoolmatedata);
		}else {
			notifyData();
		}
	}

	@Override
	public void initData() {
		userId = getIntent().getStringExtra("userId");// 获取用户ID
		
		// 获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.mySchoolMate(context,userId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_SCHOOLMATE,
				context, requestDataMap, new MyBaseParser<>(
						MySchoolmateBean.class));
		DataCallBack<MySchoolmateBean> dataCallBack = new DataCallBack<MySchoolmateBean>() {
			@Override
			public void processData(MySchoolmateBean object) {
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

	@Override
	protected void initTitle() {
		setTitleText("我的关注");
		
	}

}
