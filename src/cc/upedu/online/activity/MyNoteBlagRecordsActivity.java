package cc.upedu.online.activity;

import java.util.Map;

import cc.upedu.online.adapter.MyNoteBlagRecordsAdapter;
import cc.upedu.online.base.RecyclerViewBaseActivity;
import cc.upedu.online.domin.MyNoteRecordsBean;
import cc.upedu.online.domin.MyNoteRecordsBean.RecordItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.UserStateUtil;

/**
 * 我的笔记中索要笔记的记录
 * 
 * @author Administrator
 * 
 */
public class MyNoteBlagRecordsActivity extends RecyclerViewBaseActivity<RecordItem>{
	String userId;// 用户ID

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("索要记录");
		userId = UserStateUtil.getUserId();//获取用户ID

	}

	@Override
	protected void initData() {
		// 获取课程列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getMyNoteRecord(context, userId);
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_NOTERECORD, context,
				requestDataMap, new MyBaseParser<>(MyNoteRecordsBean.class));
		DataCallBack<MyNoteRecordsBean> coursseDataCallBack = new DataCallBack<MyNoteRecordsBean>() {

			@Override
			public void processData(final MyNoteRecordsBean object) {
				
				if (object == null) {
					objectIsNull();
				} else {
					list = object.getEntity().getRecordlist();
					if (isAdapterEmpty()) {
						setRecyclerView(new MyNoteBlagRecordsAdapter(context, list));
					}else {
						notifyData();
					}
				}
				setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
			}
		};

		getDataServer(requestVo, coursseDataCallBack);

	}
	
	
	private boolean isFistInitdata = true;
	@Override
	protected void onResume() {
		if (!isFistInitdata) {
			initData();
		}else {
			isFistInitdata = false;
		}
		super.onResume();
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		
	}
}
