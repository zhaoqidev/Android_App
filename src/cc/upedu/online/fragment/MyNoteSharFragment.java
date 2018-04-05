package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.activity.MyNoteOneCourseListActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.MyNoteCourseAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.MyNoteCourseBean;
import cc.upedu.online.domin.MyNoteCourseBean.ClassItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 我的笔记--我的分享的笔记
 * @author Administrator
 *
 */
public class MyNoteSharFragment extends RecyclerViewBaseFragment<ClassItem>{
	String userId;// 用户ID
	public MyNoteSharFragment(Context context) {
		super(context);
		userId = UserStateUtil.getUserId();//获取用户ID
	}
	
	@Override
	public void initData() {
		Map<String, String> requestDataMap = ParamsMapUtil.getMyNoteCourse(
				context, userId, "2", "1", "100");
		RequestVo requestVo = new RequestVo(ConstantsOnline.MY_NOTECOURSE,
				context, requestDataMap, new MyBaseParser<>(
						MyNoteCourseBean.class));
		DataCallBack<MyNoteCourseBean> dataCallBack = new DataCallBack<MyNoteCourseBean>() {
			@Override
			public void processData(MyNoteCourseBean object) {
				if (object == null) {
					objectIsNull();
				} else {
					if ("true".equals(object.getSuccess())) {
						if (!isLoadMore()) {
							if (list==null) {
								list = new ArrayList<ClassItem>();
							}else {
								list.clear();
							}
						}
						if (object.getEntity().getClassList() != null&&object.getEntity().getClassList().size() > 0) {
							list.addAll(object.getEntity().getClassList());
							if (isAdapterEmpty()) {
								setRecyclerView(new MyNoteCourseAdapter(context, list));
								setOnItemClick(new OnItemClickLitener() {
									
									@Override
									public void onItemLongClick(View view, int position) {
										// TODO Auto-generated method stub
										
									}
									
									@Override
									public void onItemClick(View view, int position) {
										// TODO Auto-generated method stub
										Intent intent = new Intent(context,MyNoteOneCourseListActivity.class);
										intent.putExtra("courseName",list.get(position).getCourseName());
										intent.putExtra("courseId",list.get(position).getCourseId());
										intent.putExtra("type","2");
										context.startActivity(intent);
									}
								});
							}else {
								notifyData();
							}
						}else {
							setNocontentVisibility(View.VISIBLE);
						}
					}else {
						ShowUtils.showMsg(context, object.getMessage());
					}
					setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
				}
			}
		};

		getDataServer(requestVo, dataCallBack);
	}

	/**
	 * 一页加载完成没有上拉加载操作
	 */
	@Override
	public void onLoadMore() {
		ShowUtils.showMsg(context, "没有更多数据");
		setHasMore(false);
		setPullLoadMoreCompleted();
		
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}
}