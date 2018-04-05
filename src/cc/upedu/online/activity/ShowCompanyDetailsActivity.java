package cc.upedu.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.TitleBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.StringUtil;

public class ShowCompanyDetailsActivity extends TitleBaseActivity {
	private ImageView iv_pro_arrow;
	private RelativeLayout rl_showproductlist;
	private TextView tv_showcompanyname,tv_showindustry,tv_showcompanycity,tv_showproductlist,tv_showposition,tv_showcompanyweb;
	private List<CompanyItem> companyList;
	private String currentCompanyPosition = "0";
	
	private void setData2View(CompanyItem companyItem) {
		//设置企业名称
		if (!StringUtil.isEmpty(companyItem.getName())) {
			if ("0".equals(companyItem.getIsnameopen())) {
				tv_showcompanyname.setText(companyItem.getName());
			}else {
				tv_showcompanyname.setText("未公开企业名称");
			}
		}else {
			tv_showcompanyname.setText("未设置企业名称");
		}
		//设置所属行业
		if (!StringUtil.isEmpty(companyItem.getIndustryName())) {
			tv_showindustry.setText(companyItem.getIndustryName());
		}else {
			tv_showindustry.setText("未设置所属行业");
		}
		//设置企业所在地
		if (!StringUtil.isEmpty(companyItem.getCityName())) {
			tv_showcompanycity.setText(companyItem.getCityName());
		}else {
			tv_showcompanycity.setText("未设置企业所在地");
		}
		//设置主营产品
		if (companyItem.getProductList() != null && companyItem.getProductList().size() > 0) {
			iv_pro_arrow.setVisibility(View.VISIBLE);
			tv_showproductlist.setText(companyItem.getProductList().get(0).getTitle());
		}else {
			iv_pro_arrow.setVisibility(View.INVISIBLE);
			tv_showproductlist.setText("未设置主营产品");
		}
		//设置所在职位
		if (!StringUtil.isEmpty(companyItem.getPositionName())) {
			tv_showposition.setText(companyItem.getPositionName());
		}else {
			tv_showposition.setText("未设置所在职位");
		}
		//设置企业网站
		if (!StringUtil.isEmpty(companyItem.getWebsite())) {
			if ("0".equals(companyItem.getIswebsiteopen())) {
				tv_showcompanyweb.setText(companyItem.getWebsite());
			}else {
				tv_showcompanyweb.setText("未公开企业网站");
			}
		}else {
			tv_showcompanyweb.setText("未设置企业网站");
		}
	}
	@Override
	protected void initData() {
		setData2View(companyList.get(Integer.valueOf(currentCompanyPosition)));
		
//		IndustryListBean mIndustryListBean = (IndustryListBean) NetUtil.getData("industryList",new MyBaseParser<>(IndustryListBean.class));
//		industryMap = new HashMap<>();
//		if (mIndustryListBean != null) {
//			industryList = mIndustryListBean.getIndustryList();
//			for (IndustryItem industryItem : industryList) {
//				industryMap.put(industryItem.getId(), industryItem.getContent());
//			}
//		}
//		PositionListBean mPositionListBean = (PositionListBean) NetUtil.getData("positionList",new MyBaseParser<>(PositionListBean.class));
//		positionMap = new HashMap<>();
//		if (mPositionListBean != null) {
//			positionList = mPositionListBean.getPositionList();
//			for (PositionItem positionItem : positionList) {
//				positionMap.put(positionItem.getId(), positionItem.getContent());
//			}
//		}
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的企业");
		setRightText("更多", new OnClickMyListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, ShowCompanyListActivity.class);
				intent.putExtra("companyList", (Serializable) companyList);
				startActivityForResult(intent, 0);
			}
		});
	}
	@Override
	protected View initContentView() {
		// TODO Auto-generated method stub
		companyList = (List<CompanyItem>)getIntent().getSerializableExtra("companyList");
		
		View view = View.inflate(context, R.layout.activity_showcompanyitem, null);
		tv_showcompanyname = (TextView) view.findViewById(R.id.tv_showcompanyname);
		tv_showindustry = (TextView) view.findViewById(R.id.tv_showindustry);
		tv_showcompanycity = (TextView) view.findViewById(R.id.tv_showcompanycity);
		rl_showproductlist = (RelativeLayout) view.findViewById(R.id.rl_showproductlist);
		tv_showproductlist = (TextView) view.findViewById(R.id.tv_showproductlist);
		iv_pro_arrow = (ImageView) view.findViewById(R.id.iv_pro_arrow);
		tv_showposition = (TextView) view.findViewById(R.id.tv_showposition);
		tv_showcompanyweb = (TextView) view.findViewById(R.id.tv_showcompanyweb);
		return view;
	}
	@Override
	protected void initListener() {
		super.initListener();
		rl_showproductlist.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.rl_showproductlist://展示主营产品
			if (companyList.get(Integer.valueOf(currentCompanyPosition)).getProductList().size() > 0) {
				intent = new Intent(context, ShowProductDetailsActivity.class);
				intent.putExtra("productList", (Serializable)companyList.get(Integer.valueOf(currentCompanyPosition)).getProductList());
				startActivity(intent);
			}
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				currentCompanyPosition  = bundle.getString("position");
				setData2View(companyList.get(Integer.valueOf(currentCompanyPosition)));
			}
		}
	}
}
