package cc.upedu.online.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.List;

import cc.upedu.online.adapter.PositionListAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.domin.PositionListBean.PositionItem;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.PreferencesObjectUtil;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
/**
 * 设置我在企业中职务的界面
 * @author Administrator
 *
 */
public class SetPositionActivity extends ListBaseActivity<PositionItem> {
	public static final int RESULT_SETPOSITION = 13;
	private List<PositionItem> positionList;
	private String oldPosition;
	private String newPosition = "";
	private String positionName = "";
//	private ListView lv_positionlist;
//	private PositionListAdapter mPositionListAdapter;
	private String currentPosition;
	
	@Override
	protected void initData() {
		positionList = (List<PositionItem>) PreferencesObjectUtil.readObject("positionList", context);
		if (isAdapterEmpty()) {
			setListView(new PositionListAdapter(context,positionList,oldPosition));
			setOnItemClickListion(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					newPosition = positionList.get(position).getId();
					positionName = positionList.get(position).getContent();
					((PositionListAdapter)getAdapter()).setPosition(newPosition);
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
		setTitleText("我的职务");
		setRightText("保存", new OnClickMyListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				if (StringUtil.isEmpty(newPosition)) {
					ShowUtils.showMsg(context, "您还没选择职务!");
				} else {
					intent.putExtra("positionid", newPosition);
					intent.putExtra("positionName", positionName);
					intent.putExtra("currentPosition", currentPosition);
					setResult(RESULT_SETPOSITION, intent);
					finish();
				}
			}
		});
		oldPosition = getIntent().getStringExtra("positionid");
		if (StringUtil.isEmpty(oldPosition))
			oldPosition = "";
		currentPosition = getIntent().getStringExtra("currentPosition");

		setLeftButton(new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				if (!oldPosition.equals(newPosition))
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetPositionActivity.this.finish();
						}
					});
				else
					SetPositionActivity.this.finish();
			}
		});
	}
	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (!oldPosition.equals(newPosition)) {
				ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
					@Override
					public void confirmOperation() {
						SetPositionActivity.this.finish();
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
