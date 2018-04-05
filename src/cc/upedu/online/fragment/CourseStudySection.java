package cc.upedu.online.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.adapter.StudySectionAdapter;
import cc.upedu.online.base.RecyclerViewBaseFragment;
import cc.upedu.online.domin.CourseSectionListBean;
import cc.upedu.online.domin.CourseSectionListBean.Entity.CatalogItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 课程学习中的章节页
 * @author Administrator
 *
 */
public class CourseStudySection extends RecyclerViewBaseFragment<List<CatalogItem>> {
	private CourseSectionListBean mCourseSectionListBean;
	private String courseId;
	/**
	 * 正在加载布局
	 */
	private Dialog loadingDialog;
	
	public CourseStudySection() {
		
	}

	public CourseStudySection(Context context, String courseId) {
		super(context);
		this.courseId = courseId;
		
	}
	
	private Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if ("true".equals(mCourseSectionListBean.getSuccess())) {
					if (list==null) {
						list = new ArrayList<List<CatalogItem>>();
					}else {
						list.clear();
					}
					setData();
				}else {
					ShowUtils.showMsg(context, mCourseSectionListBean.getMessage());
				}
				setPullLoadMoreCompleted();
				break;
			
			}
		};
	};

	private void setData() {
		list.addAll(mCourseSectionListBean.getEntity().getCatalogList());

		setHasMore(false);
		setRecyclerView(new StudySectionAdapter(context,list));
		//清除正在加载的布局
		if (loadingDialog != null && loadingDialog.isShowing()) {
			loadingDialog.dismiss();
		}
	}

	@Override
	public void initData() {
		//正在加载布局
		loadingDialog = ShowUtils.createLoadingDialog(context, true);
		loadingDialog.show();
		//获取课程章节列表数据 
		Map<String, String> requestDataMap = ParamsMapUtil.getCourseCatalog(context, courseId);
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_CATALOG, context, requestDataMap, new MyBaseParser<>(CourseSectionListBean.class));
		DataCallBack<CourseSectionListBean> catalogDataCallBack = new DataCallBack<CourseSectionListBean>() {

			@Override
			public void processData(CourseSectionListBean object) {
				if (object==null) {
					objectIsNull();
				}else {
					mCourseSectionListBean = object;
					handler.obtainMessage(0).sendToTarget();
				}
			}
		};
		getDataServer(requestVo, catalogDataCallBack);
	}
	
	@Override
	public void onLoadMore() {
		setHasMore(false);
		setPullLoadMoreCompleted();
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}


	
}
