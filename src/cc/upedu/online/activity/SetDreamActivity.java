package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.ThreePartModelListBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.utils.ShowUtils;
import cc.upedu.online.utils.ShowUtils.ConfirmBackCall;
import cc.upedu.online.utils.StringUtil;
import cc.upedu.online.view.factory.MyHorizontalIconTextItem;
/**
 * 设置我 的梦想
 * @author Administrator
 *
 */
public class SetDreamActivity extends ThreePartModelListBaseActivity<String> {
	public static final int RESULT_SETDREAM = 11;
	public static final int REQUEST_SETDREAM = 23;
	private List<String> dreamList;
	private LinearLayout ll_adddream;
	private boolean isSeting;
	private boolean isNewDream;
	private int alterDream = -1;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if (isAdapterEmpty()) {
			setListView(new DreamAdapter(context, dreamList));
			if (isSeting) {
				setOnItemClickListion(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(context,AddDreamActivity.class);
						isNewDream = false;
						alterDream = position;
						intent.putExtra("dreamItem", dreamList.get(position));
						startActivityForResult(intent, REQUEST_SETDREAM);
					}
				});
			}
		}else {
			notifyData();
		}
	}
	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		setContentBackgroundResource(R.color.backGrond);
		MyHorizontalIconTextItem topview = new MyHorizontalIconTextItem(MyHorizontalIconTextItem.TEXT);
		View view = topview.initView(context);
		topview.setBackgroundColor(getResources().getColor(R.color.backGrond));
		topview.setText(R.string.name_dreamdoc);
		return view;
	}
	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_setdream_bottom, null);
		
		ll_adddream = (LinearLayout) view.findViewById(R.id.ll_adddream);
		if (isSeting) {
			ll_adddream.setVisibility(View.VISIBLE);
		}else {
			ll_adddream.setVisibility(View.GONE);
		}
		return view;
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("我的梦想");
		isSeting = getIntent().getBooleanExtra("isSeting", false);
		dreamList = (List<String>)getIntent().getSerializableExtra("dreamList");
		if (isSeting) {
			setRightText("保存", new OnClickMyListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("dreamList", (Serializable)dreamList);
					setResult(RESULT_SETDREAM, intent);
					finish();
				}
			});
		}
		if (isSeting) {
			setLeftButton(new OnClickMyListener() {
				@Override
				public void onClick(View v) {
					ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							SetDreamActivity.this.finish();
						}
					});
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode==REQUEST_SETDREAM){//设置的数据回传
				if (resultCode==AddDreamActivity.RESULT_SETDREAM){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						if (isNewDream) {//新数据首次修改
							isNewDream = false;
							String newDream = bundle.getString("newDreamItem");
							dreamList.add(newDream);
						}else {//修改旧数据和新数据多次修改
							String alterDreamItem = bundle.getString("alterDreamItem");
							if (-1 != alterDream) {//新数据多次修改
								dreamList.remove(alterDream);
								dreamList.add(alterDream, alterDreamItem);
								alterDream = -1;
							}else {
							}
						}
						notifyData();
					}
				}
		}
		}
	}
	@Override
	protected void initListener() {
		super.initListener();
		if (isSeting) {
			ll_adddream.setOnClickListener(this);
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent;
		switch (v.getId()) {
		case R.id.ll_adddream:
			intent = new Intent(context,AddDreamActivity.class);
			isNewDream = true;
			startActivityForResult(intent, REQUEST_SETDREAM);
			break;
		}
	}
	class DreamAdapter extends BaseMyAdapter<String> {

		public DreamAdapter(Context context, List<String> list) {
			super(context, list);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}else {
				view = View.inflate(context, R.layout.layout_dreamitem, null);
				holder = new ViewHolder();
				holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
				holder.iv_del = (ImageView) view.findViewById(R.id.iv_del);
				holder.tv_dream = (TextView) view.findViewById(R.id.tv_dream);
				if (isSeting) {
					holder.iv_del.setVisibility(View.VISIBLE);
				}else{
					holder.iv_del.setVisibility(View.GONE);
				}
				view.setTag(holder);
			}
			
			holder.tv_title.setText("梦想"+StringUtil.numberLower2Capital(position+1));
			holder.tv_dream.setText(list.get(position));
			holder.iv_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ShowUtils.showDiaLog(context, "温馨提醒", "请确定,是否删除此梦想?", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							dreamList.remove(position);
							notifyDataSetChanged();
						}
					});
				}
			});
			
			return view;
		}
		class ViewHolder{
			TextView tv_title;
			ImageView iv_del;
			TextView tv_dream;
		}

	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if (isSeting) {
        		ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
        			@Override
        			public void confirmOperation() {
        				SetDreamActivity.this.finish();
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
