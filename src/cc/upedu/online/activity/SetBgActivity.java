package cc.upedu.online.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import cc.upedu.online.adapter.BgAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;

public class SetBgActivity extends ListBaseActivity<String> {
	public static final int RESULT_SETBG = 3;
	private List<String> bannerList;
	private String oldBannerUrl;
	private String newBannerUrl = "";

	@Override
	protected void initData() {
		if (isAdapterEmpty()) {
			setListView(new BgAdapter(context,bannerList,oldBannerUrl));
			setOnItemClickListion(new OnItemClickListener() {
				
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					newBannerUrl = bannerList.get(position);
//					mBgAdapter.setBannerUrl(newBannerUrl);
//					mBgAdapter.notifyDataSetChanged();
					Intent intent=new Intent();
					intent.putExtra("bannerUrl", newBannerUrl);
					setResult(RESULT_SETBG, intent);
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
		setTitleText("个性背景");
		bannerList = (List<String>) PreferencesObjectUtil.readObject("bannerList", context);
		oldBannerUrl = getIntent().getStringExtra("bannerUrl");
	}

	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
				@Override
				public void confirmOperation() {
					SetBgActivity.this.finish();
				}
			});
			return false;
        }else {
        	return super.onKeyDown(keyCode, event); 
		}
    }
}
