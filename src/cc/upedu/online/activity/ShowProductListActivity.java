package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import cc.upedu.online.adapter.ProductListAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.CompanyItem.ProductItem;
/**
 * 展示主营产品的界面
 * @author Administrator
 *
 */
public class ShowProductListActivity extends ListBaseActivity<ProductItem> {
//	public static final int RESULT_SETPRODUCTLIST = 18;
//	protected static final int REQUEST_SETPRODUCTITEM = 21;
	private List<ProductItem> productList;
	
	@Override
	protected void initData() {
		if (isAdapterEmpty()) {
			setListView(new ProductListAdapter(context,productList,null));
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
		setTitleText("所有产品");
		productList = (List<ProductItem>)getIntent().getSerializableExtra("productList");
	}
}
