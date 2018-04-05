package cc.upedu.online.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.MyVideoAnswerPlayActivity;
import cc.upedu.online.activity.TelecastApplayActivity;
import cc.upedu.online.adapter.AbsRecyclerViewAdapter.OnItemClickLitener;
import cc.upedu.online.adapter.MyVideoAnswerAdapter;
import cc.upedu.online.base.TwoPartModelTopRecyclerViewBaseFragment;
import cc.upedu.online.domin.VideoAnswerListBean;
import cc.upedu.online.domin.VideoAnswerListBean.Entity.Notice;
import cc.upedu.online.domin.VideoAnswerListBean.Entity.VideoAnswerItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.MarqueeText;

/**
 * 侧拉栏 我的答疑 导师视频答疑
 * @author Administrator
 *
 */
public class MyVideoAnswerFragment extends TwoPartModelTopRecyclerViewBaseFragment<VideoAnswerItem>{
	private VideoAnswerListBean bean;
	private String courseId;
	
	public MyVideoAnswerFragment(Context context, String courseId) {
		super();
		this.courseId = courseId;
	}
	
//	public void onPause() {
//		if (mVideoAnswerAdapter != null) {
//			mVideoAnswerAdapter.marqueetextPause();
//		}
//	}
	private Handler handler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			if ("true".equals(bean.getSuccess())) {
				if (!isLoadMore()) {
					if(list==null){
						list = new ArrayList<VideoAnswerItem>();
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
		list.addAll(bean.getEntity().getQaVideoList());
		notice= bean.getEntity().getNotice();
		//设置公告
		String noticeString = "";
		if (notice != null && !StringUtil.isEmpty(notice.getTitle())) {
			noticeString += ("[公告]:"+notice.getTitle());
		}else {
			noticeString += "[公告]:暂时没有公告信息,敬请期待!";
		}
		marqueetext.setText(noticeString);
		marqueetextStart();
		marqueetext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (notice != null && !StringUtil.isEmpty(notice.getTitle())) {
					Intent intent = new Intent(context,TelecastApplayActivity.class);
					intent.putExtra("courseId", notice.getLiveId());
					context.startActivity(intent);
				}else {
					ShowUtils.showMsg(context, "没有公告信息,敬请期待!");
				}
			}
		});
		
		if (isAdapterEmpty()) {
			setRecyclerView(new MyVideoAnswerAdapter(context,list));
			setOnItemClick(new OnItemClickLitener() {
				
				@Override
				public void onItemLongClick(View view, int position) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onItemClick(View view, int position) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(context, MyVideoAnswerPlayActivity.class);
					intent.putExtra("videoAnswerItem", (Serializable) list.get(position));
					context.startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}

	@Override
	public void initData() {
		//获取视频答疑列表的数据
		Map<String, String> requestDataMap = ParamsMapUtil.getTextAnswer(context, courseId, String.valueOf(currentPage));
		RequestVo requestVo = new RequestVo(ConstantsOnline.COURSE_VIDEOANSWER, context, requestDataMap, new MyBaseParser<>(VideoAnswerListBean.class));
		DataCallBack<VideoAnswerListBean> videoAnswerDataCallBack = new DataCallBack<VideoAnswerListBean>() {
			@Override
			public void processData(VideoAnswerListBean object) {
				if (object==null) {
					objectIsNull();
				}else {
					bean = object;
					handler.obtainMessage().sendToTarget();
				}
			}
		};
		getDataServer(requestVo, videoAnswerDataCallBack);
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		// TODO Auto-generated method stub
		
	}


	Notice notice;
	@Override
	public View initTopLayout() {
		// 公告
		View view = View.inflate(context, R.layout.layout_notice, null);
		marqueetext = (MarqueeText) view.findViewById(R.id.marqueetext);
		return view;
	}
	
	private MarqueeText marqueetext;
	public void marqueetextStart() {
		if (marqueetext != null) {
			marqueetext.startScroll();
		}
	}

	public void marqueetextStop() {
		if (marqueetext != null) {
			marqueetext.stopScroll();
		}
	}
	
	public void marqueetextPause() {
		if (marqueetext != null) {
			marqueetext.pauseScroll();
		}
	}

	public void marqueetextStartFor0() {
		if (marqueetext != null) {
			marqueetext.startFor0();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SharedPreferencesUtil.getInstance().editPutString("answerId", "");
	}
}
