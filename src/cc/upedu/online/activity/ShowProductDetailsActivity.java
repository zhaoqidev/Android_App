package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.PicItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.CommonUtil;
import cc.upedu.online.utils.ImageUtils;
import cc.upedu.online.utils.StringUtil;
/**
 * 展示产品详情界面
 * @author Administrator
 *
 */
public class ShowProductDetailsActivity extends TitleBaseActivity {
	private List<ProductItem> productList;
	private TextView tv_productname,tv_productdesc,tv_productcustomer,tv_productvalue,tv_nopic;
	/** 网络，用于动态显示添加删除图片 */
	private GridView gv;
	/** 适配器 */
	private MyGridViewAdapter adapter;
	private String currentCompanyPosition = "0";
	
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("主营产品");
		setRightText("更多", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ShowProductListActivity.class);
				intent.putExtra("productList", (Serializable)productList);
				startActivityForResult(intent, 0);
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		setContentBackgroundColor(getResources().getColor(R.color.backGrond));
		productList = (List<ProductItem>)getIntent().getSerializableExtra("productList");
		
		View view = View.inflate(context, R.layout.activity_showproductitem, null);
		tv_productname = (TextView) view.findViewById(R.id.tv_productname);
		tv_productdesc = (TextView) view.findViewById(R.id.tv_productdesc);
		tv_productcustomer = (TextView) view.findViewById(R.id.tv_productcustomer);
		tv_productvalue = (TextView) view.findViewById(R.id.tv_productvalue);
		tv_nopic = (TextView) view.findViewById(R.id.tv_nopic);
		gv = (GridView) view.findViewById(R.id.gridView);
		return view;
	}
	@Override
	protected void initData() {
		setData2View(productList.get(Integer.valueOf(currentCompanyPosition)));
	}
	/**
	 * 
	 */
	private void setData2View(ProductItem productItem) {
		if (!StringUtil.isEmpty(productItem.getTitle())) {
			tv_productname.setText(productItem.getTitle());
		}else {
			tv_productname.setText("未设置产品名称");
		}
		if (!StringUtil.isEmpty(productItem.getDesc())) {
			tv_productdesc.setText(productItem.getDesc());
		}else {
			tv_productdesc.setText("未设置产品信息描述");
		}
		if (!StringUtil.isEmpty(productItem.getCustomer())) {
			tv_productcustomer.setText(productItem.getCustomer());
		}else {
			tv_productcustomer.setText("未设置产品适合客户描述");
		}
		if (!StringUtil.isEmpty(productItem.getValue())) {
			tv_productvalue.setText(productItem.getValue());
		}else {
			tv_productvalue.setText("未设置产品价值描述");
		}
		if (productItem.getPicList().size() > 0) {
			gv.setVisibility(View.VISIBLE);
			tv_nopic.setVisibility(View.GONE);
			adapter = new MyGridViewAdapter(context,productItem.getPicList());
			gv.setAdapter(adapter);
			CommonUtil.setGridViewHeightBasedOnChildren(context,gv,4);
		}else {
			gv.setVisibility(View.GONE);
			tv_nopic.setVisibility(View.VISIBLE);
		}
	}
	private List<String> picUrlList;
	public class MyGridViewAdapter extends BaseMyAdapter<PicItem> {
		
		public MyGridViewAdapter(Context context, List list) {
			super(context, list);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View picView = View.inflate(context, R.layout.gv_pic_item, null);
			final ImageButton picIBtn = (ImageButton) picView
					.findViewById(R.id.pic);
			if (position == 0) {
				picUrlList = new ArrayList<String>();
				for (PicItem picitem : list) {
					picUrlList.add(picitem.getPicPath());
				}
			}
			
			ImageUtils.setImageToImageButton(picUrlList.get(position), picIBtn, R.drawable.wodeimg_default);
			picIBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					if (loadImageSuccess) {
						Intent intent = new Intent(context,ImageActivity.class);
						intent.putExtra("image_list", (Serializable) picUrlList);
						intent.putExtra("image_position", position);
						context.startActivity(intent);
//					}else {
//						ShowUtils.showMsg(context, "图片加载失败!");
//					}
				}
			});
			return picView;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				currentCompanyPosition  = bundle.getString("position");
				setData2View(productList.get(Integer.valueOf(currentCompanyPosition)));
			}
		}
	}
}
