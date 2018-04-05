package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import cc.upedu.online.adapter.ShowCompanyListAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem;
/**
 * 展示我的企业列表
 * @author Administrator
 *
 */
public class ShowCompanyListActivity extends ListBaseActivity<CompanyItem> {
	private List<CompanyItem> companyList;

	@Override
	protected void initData() {
		if (isAdapterEmpty()) {
			setListView(new ShowCompanyListAdapter(context,companyList,null));
			setOnItemClickListion(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent=new Intent();
		        	intent.putExtra("position", String.valueOf(position));
					setResult(0, intent);
					finish();
				}
			});
		}else {
			notifyData();
		}
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("所有企业");
		companyList = (List<CompanyItem>)getIntent().getSerializableExtra("companyList");
	}
}
