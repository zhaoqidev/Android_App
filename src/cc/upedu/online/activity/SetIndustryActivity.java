package cc.upedu.online.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import cc.upedu.online.adapter.IndustryListAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.IndustryListBean.IndustryItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
/**
 * 设置所属行业的界面
 * @author Administrator
 *
 */
public class SetIndustryActivity extends ListBaseActivity<IndustryItem> {
	public static final int RESULT_SETINDUSTRY = 16;
	private List<IndustryItem> industryList;
//	private IndustryListAdapter mIndustryListAdapter;
	private String oldIndustry;
	private String newIndustry = "";
	private String newIndustryName = "";
	private String currentPosition;
	
	@Override
	protected void initData() {
		industryList = (List<IndustryItem>) PreferencesObjectUtil.readObject("industryList", context);
		if (isAdapterEmpty()) {
			setListView(new IndustryListAdapter(context,industryList,oldIndustry));
			setOnItemClickListion(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					newIndustry = industryList.get(position).getId();
					newIndustryName = industryList.get(position).getContent();
					((IndustryListAdapter)getAdapter()).setIndustry(newIndustry);
					notifyData();
				}
			});
		}else {
			notifyData();
		}
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("所属行业");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (StringUtil.isEmpty(newIndustry)) {
					ShowUtils.showMsg(context, "您还没选择所属行业!");
				} else {
					intent.putExtra("industry", newIndustry);
					intent.putExtra("industryname", newIndustryName);
					intent.putExtra("currentPosition", currentPosition);
					setResult(RESULT_SETINDUSTRY, intent);
					finish();
				}
			}
		});

		oldIndustry = getIntent().getStringExtra("industry");
		if (StringUtil.isEmpty(oldIndustry))
			oldIndustry = "";
		currentPosition = getIntent().getStringExtra("currentPosition");
		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!oldIndustry.equals(newIndustry))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetIndustryActivity.this.finish();
						}
					});
				else
					SetIndustryActivity.this.finish();
			}
		});
	}
	
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (!oldIndustry.equals(newIndustry)) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetIndustryActivity.this.finish();
					}
				});
				return false;
			}else {
				return super.onKeyDown(keyCode, event);
			}
	    }else {
	    	return super.onKeyDown(keyCode, event); 
		}
	}
}
