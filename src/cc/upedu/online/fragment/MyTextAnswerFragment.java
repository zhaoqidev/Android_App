package cc.upedu.online.fragment;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.adapter.TextAnswerAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.TextAnswerListBean;
import cc.upedu.online.domin.TextAnswerListBean.Entity.TextAnswerItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;

public class MyTextAnswerFragment extends RecyclerViewBaseFragment<TextAnswerItem> {

	private TextAnswerListBean bean;
	
	public MyTextAnswerFragment(Context context, String courseId) {
		super(context);
		this.courseId = courseId;
	}

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.getSuccess())) {
				if (!isLoadMore()) {
					if(list==null){
						list = new ArrayList<TextAnswerItem>();
					}else{
						list.clear();
					}
				}
				setData();
			}else {
				ShowUtils.showMsg(context, bean.getMessage());
			}
			setPullLoadMoreCompleted();// 结束下拉刷新
		}
		
	};
	
	private void setData() {
		totalPage = bean.getEntity().getTotalPage();
		canLodeNextPage();
		list.addAll(bean.getEntity().getQaWordsList());
		
		if (isAdapterEmpty()) {
			setRecyclerView(new TextAnswerAdapter(context, list));
		}else {
			notifyData();
		}
	}
	private String courseId;
	@Override
	public void initData() {
		//获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getTextAnswer(context, courseId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_TEXTANSWER, context, requestDataMap, new MyBaseParser<>(TextAnswerListBean.class));
		DataCallBack<TextAnswerListBean> textAnswerDataCallBack = new DataCallBack<TextAnswerListBean>() {

			@Override
			public void processData(TextAnswerListBean object) {
				if (object==null) {
					objectIsNull();
				}else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, textAnswerDataCallBack);

	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		 setItemDecoration(true);
		
	}
}
