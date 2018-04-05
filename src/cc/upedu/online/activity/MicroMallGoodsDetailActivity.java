package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.MicroMallActivity.getTokenBean;
import cc.upedu.online.adapter.MicroMallGoodsPicListAdapter;
import cc.upedu.online.base.BaseActivity;
import cc.upedu.online.domin.MicroMallGoodsFromVdianBean;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.StringUtil;
/**
 * 商品详情页面
 * @author Administrator
 *
 */
public class MicroMallGoodsDetailActivity extends BaseActivity {

	/**
	 * 头部布局图片
	 */
	private ImageView iv_head;

	/**
	 * 返回按钮
	 */
	private ImageView iv_back;
	/**
	 * 商品名称
	 */
	private TextView tv_goods_name;
	/**
	 * 所需成长币数量
	 */
	private TextView tv_coin_num;
	/**
	 * 库存数量
	 */
	private TextView tv_kucun;
	/**
	 * 已售数量
	 */
	private TextView tv_sales_volume;
	/**
	 * 展示产品图片的列表
	 */
	private ListView lv;
	/**
	 * 确认兑换的按钮
	 */
	private Button btn_sure;
	
	private View headView;
	
	private String token;
	
	private int requestNumber=0;//请求token的次数
	
	MicroMallGoodsFromVdianBean bean=new MicroMallGoodsFromVdianBean();
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_sure:
			/**
			 * 跳转到确认订单页面
			 * @param position
			 */
			Intent intent = new Intent(context, MicroMallOrderActivity.class);
			intent.putExtra("coin", getIntent().getStringExtra("coin"));
			context.startActivity(intent);
				
			break;

		default:
			break;
		}

	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_micromall_goodsdetail);
		headView = View.inflate(context, R.layout.activity_micromall_goodsdetail_roll_view, null);
		iv_head=(ImageView) headView.findViewById(R.id.iv_head);
		iv_back=(ImageView) headView.findViewById(R.id.iv_back);
		tv_goods_name=(TextView) headView.findViewById(R.id.tv_goods_name);
		tv_coin_num=(TextView) headView.findViewById(R.id.tv_coin_num);
		tv_kucun=(TextView) headView.findViewById(R.id.tv_kucun);
		tv_sales_volume=(TextView) headView.findViewById(R.id.tv_sales_volume);
		lv=(ListView) findViewById(R.id.lv);
		btn_sure=(Button) findViewById(R.id.btn_sure);
		
		token=getIntent().getStringExtra("token");

	}


	@Override
	protected void initListener() {
		iv_back.setOnClickListener(this);
		btn_sure.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		Gson gson = new Gson();
		
		Map<String, String> paramMap=new HashMap<String, String>();
		paramMap.put("itemid", getIntent().getStringExtra("itemid"));

		
		Map<String, String> pubMap=new HashMap<String, String>();
		pubMap.put("access_token", token);
		pubMap.put("format", "json");
		pubMap.put("method", "vdian.item.get");
		pubMap.put("version", "1.0");
		 
		Map<String, String> requestDataMap = ParamsMapUtil.getListFromVDian(context, gson.toJson(paramMap),gson.toJson(pubMap));
		RequestVo requestVo = new RequestVo(ConstantsOnline.GET_LIST_VDIAN,context, requestDataMap, new MyBaseParser<>(
				MicroMallGoodsFromVdianBean.class));
		System.out.println(ConstantsOnline.GET_LIST_VDIAN+"?param="+gson.toJson(paramMap)+"&public"+gson.toJson(pubMap));
		DataCallBack<MicroMallGoodsFromVdianBean> dataCallBack = new DataCallBack<MicroMallGoodsFromVdianBean>() {

			@Override
			public void processData(MicroMallGoodsFromVdianBean object) {
				if (object != null) {
					bean=object;
					handler.obtainMessage().sendToTarget();
				}else {
					ShowUtils.showMsg(context,"获取数据失败，请稍后重试");
				}
			}
		};
		getDataServer(requestVo, dataCallBack);

	}
	MicroMallGoodsPicListAdapter adapter; 
	private Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if ("0".equals(bean.status.status_code)) {
				if (bean.result!=null) {
					setView();
					List<String> srclist=new ArrayList<String>() ;
					for (int i=1;i<bean.result.imgs.size();i++){
						srclist.add(bean.result.imgs.get(i));
					}
					adapter =new MicroMallGoodsPicListAdapter(context, srclist);
					lv.setAdapter(adapter);
					if (lv.getHeaderViewsCount() < 1) {
						lv.addHeaderView(headView);
					}
					
				}
			}else if ("10013".equals(bean.status.status_code)) {
				
					if (requestNumber<3) {
						requestNumber++;
						getAccessToken("true");//token过期，重新请求token
					}
					
			}else{
				ShowUtils.showMsg(context,bean.status.status_reason);
			}
		};
	};

	
	/**
	 * 把数据设置到界面上
	 */
	private void setView() {
		if (bean.result.imgs.size()>0) {
			ImageUtils.setBackGroundImage(bean.result.imgs.get(0), iv_head, 0);
		}

		tv_goods_name.setText(bean.result.item_name);
		tv_coin_num.setText(bean.result.price);
		tv_kucun.setText(bean.result.stock);
		tv_sales_volume.setText(bean.result.sold);

	}
	
	/**
	 * 获取token
	 */
	private void getAccessToken(String flag) {
		
		Map<String, String> requestDataMap = ParamsMapUtil.getToken(context, flag);
		RequestVo requestVo = new RequestVo(ConstantsOnline.ACCESSTOKEN, context,
				requestDataMap, new MyBaseParser<>(getTokenBean.class));
		
		DataCallBack<getTokenBean> dataCallBack = new DataCallBack<getTokenBean>() {

			@Override
			public void processData(getTokenBean object) {
				if (object != null) {
					if ("false".equals(object.success)) {
						ShowUtils.showMsg(context, object.message);
					} else {
						if (!StringUtil.isEmpty(object.entity)) {
							token=object.entity;
							initData();
						}else {
							ShowUtils.showMsg(context,"获取数据失败，请稍后重试");
						}
					}
				}else{
					ShowUtils.showMsg(context,"获取数据失败，请稍后重试");
				}
			}
		};
		
		getDataServer(requestVo, dataCallBack);
	}

}
