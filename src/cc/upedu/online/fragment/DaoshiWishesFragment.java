package cc.upedu.online.fragment;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.adapter.DaoshiWishesAdapter;
import cc.upedu.online.base.TwoPartModelBottomRecyclerViewBaseFragment;
import cc.upedu.online.domin.DaoshiWishesBean;
import cc.upedu.online.domin.DaoshiWishesBean.WordsItem;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.SharedPreferencesUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.utils.UserStateUtil;
/**
 * 导师名片页面的寄语
 * @author Administrator
 *
 */
public class DaoshiWishesFragment extends TwoPartModelBottomRecyclerViewBaseFragment<WordsItem> {



	private DaoshiWishesBean mDaoshiWishesBean = new DaoshiWishesBean();
	
	private TextView btn_commit;//提交寄语按钮
	private EditText et_wishes;//寄语输入框
	private ScrollView scrollview_wishes;//寄语布局
	
	String teacherId;


	public DaoshiWishesFragment(Context context) {
		super();
		
	}


	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if ("true".equals(mDaoshiWishesBean.success)) {
				if (isLoadMore()) {
					list.addAll(mDaoshiWishesBean.entity.wordsList);
					if (list.size() > 0) {
						if (isAdapterEmpty()) {
							setRecyclerView(new DaoshiWishesAdapter(context, list));
						} else {
							notifyData();
						}

					}
					//判断是否可以加载下一页
					canLodeNextPage();
					setPullLoadMoreCompleted();// 结束上拉加载更多
				} else {
					if (list==null) {
						list=new ArrayList<WordsItem>();
					}else {
						list.clear();
					}
					setData();
					setPullLoadMoreCompleted();// 结束下拉刷新
				}
			} else {
				ShowUtils.showMsg(context, mDaoshiWishesBean.message);
				setPullLoadMoreCompleted();// 结束上拉加载或者下拉刷新
			}
		}
	};

	private void setData() {
		totalPage = mDaoshiWishesBean.entity.totalPage;
		if (currentPage < Integer.valueOf(totalPage)) {
			setHasMore(true);
		}
		if (Integer.parseInt(totalPage) > 0) {
			setNocontentVisibility(View.GONE);
			list.addAll(mDaoshiWishesBean.entity.wordsList);
		}else {
			//做无数据时的页面显示操作
//			ShowUtils.showMsg(context, "暂时没有数据哦~");
			setNocontentVisibility(View.VISIBLE);
		}
		if (list.size() > 0) {
			setRecyclerView(new DaoshiWishesAdapter(context, list));
		}
	}

	@Override
	public void initData() {
		teacherId=SharedPreferencesUtil.getInstance().spGetString("teacherId", "0");
				// 获取寄语列表的数据
				Map<String, String> requestDataMap = ParamsMapUtil.DaoshiWishes(
						context, teacherId, String.valueOf(currentPage));
				RequestVo requestVo = new RequestVo(ConstantsOnline.DAOSHI_WISHES,
						context, requestDataMap, new MyBaseParser<>(
								DaoshiWishesBean.class));
				DataCallBack<DaoshiWishesBean> dataCallBack = new DataCallBack<DaoshiWishesBean>() {
					@Override
					public void processData(DaoshiWishesBean object) {
						if (object == null) {
							objectIsNull();
						} else {
							mDaoshiWishesBean = object;
							handler.obtainMessage().sendToTarget();

						}
					}
				};
				getDataServer(requestVo, dataCallBack);
		
	}

	
	/**
	 * 向导师发送新的寄语
	 */
	protected void sendNewWishes() {
		btn_commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String wishes = et_wishes.getText().toString().trim();
				if (StringUtil.isEmpty(wishes)) {
					ShowUtils.showMsg(context, "提交的内容不能为空哦~");
				} else {
					Map<String, String> requestDataMap = ParamsMapUtil.newWishes(context, teacherId,UserStateUtil.getUserId(), wishes);
					RequestVo requestVo = new RequestVo(
							ConstantsOnline.NEW_WISHES, context,
							requestDataMap, new MyBaseParser<>(
									NewWishesBean.class));
					DataCallBack<NewWishesBean> dataCallBack = new DataCallBack<NewWishesBean>() {

						@Override
						public void processData(NewWishesBean object) {
							if (object != null) {
								if ("true".equals(object.success)) {
									et_wishes.setText("");
									initData();
								}
								
								ShowUtils.showMsg(context, object.message);
							}else{
								ShowUtils.showMsg(context, "获取数据失败请检查网络");
							}

						}

					};
					getDataServer(requestVo, dataCallBack);
				}
			}
			
		});
	}
	
	/**
	 * 发表新的寄语请求的javabean
	 * 
	 * @author Administrator
	 * 
	 */
	class NewWishesBean {
		public String message;
		public String success;
	}

	@Override
	protected void setPullLoadMoreRecyclerView() {
		setItemDecoration(true);
		
	}


	@Override
	public View initBottomLayout() {
		View view=View.inflate(context, R.layout.fragment_daoshiwishes_bottom, null);
		btn_commit=(TextView) view.findViewById(R.id.btn_commit);
		et_wishes= (EditText) view.findViewById(R.id.et_wishes);
		scrollview_wishes=(ScrollView) view.findViewById(R.id.scrollview_wishes);
		if (UserStateUtil.isLogined()) {
			scrollview_wishes.setVisibility(View.VISIBLE);
		}else {
			scrollview_wishes.setVisibility(View.GONE);
		}
		
		sendNewWishes();
		return view;
	}



}
