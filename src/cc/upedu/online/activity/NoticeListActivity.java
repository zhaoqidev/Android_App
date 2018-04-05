package cc.upedu.online.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.io.Serializable;
import java.util.List;

import cc.upedu.online.adapter.NoticeAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.NoticeItem;
/**
 * 公告列表界面
 * @author Administrator
 *
 */
public class NoticeListActivity extends ListBaseActivity<NoticeItem> {
	private List<NoticeItem> noticeList;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("公告列表");
		noticeList = (List<NoticeItem>) getIntent().getSerializableExtra("noticeList");
	}

	@Override
	protected void initData() {
		if (isAdapterEmpty()) {
			setListView(new NoticeAdapter(context,noticeList));
			setOnItemClickListion(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = new Intent(NoticeListActivity.this, NoticeDetailsActivity.class);
					intent.putExtra("noticeList", (Serializable)noticeList);
					intent.putExtra("position", position);
					startActivity(intent);
				}
			});
		}else {
			notifyData();
		}
	}
}
