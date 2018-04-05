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
 * 设置价值观与信念
 * @author Administrator
 *
 */
public class SetThoughtActivity extends ThreePartModelListBaseActivity<String> {
	public static final int RESULT_SETTHOUGHT = 8;
	public static final int REQUEST_SETTHOUGHT = 24;
	private List<String> thoughtList;
	private LinearLayout ll_addthought;
	private boolean isSeting;
	private boolean isNewThought;
	private int alterThought = -1;

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if (isAdapterEmpty()) {
			setListView(new ThoughtAdapter(context, thoughtList));
			if (isSeting) {
				setOnItemClickListion(new OnItemClickListener() {
					
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Intent intent = new Intent(context,AddThoughtActivity.class);
						isNewThought = false;
						alterThought = position;
						intent.putExtra("thoughtItem", thoughtList.get(position));
						startActivityForResult(intent, REQUEST_SETTHOUGHT);
					}
				});
			}
		}else {
			notifyData();
		}
	}
	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("价值观与信念");
		isSeting = getIntent().getBooleanExtra("isSeting", false);
		isSeting = getIntent().getBooleanExtra("isSeting", false);
		thoughtList = (List<String>)getIntent().getSerializableExtra("thoughtList");
		if (isSeting) {
			setRightText("保存", new OnClickMyListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("thoughtList", (Serializable)thoughtList);
					setResult(RESULT_SETTHOUGHT, intent);
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
							SetThoughtActivity.this.finish();
						}
					});
				}
			});
		}
	}
	@Override
	public View initTopLayout() {
		// TODO Auto-generated method stub
		setContentBackgroundResource(R.color.backGrond);
		MyHorizontalIconTextItem topview = new MyHorizontalIconTextItem(MyHorizontalIconTextItem.TEXT);
		View view = topview.initView(context);
		topview.setBackgroundColor(getResources().getColor(R.color.backGrond));
		topview.setText(R.string.name_thoughdoc);
		return view;
	}
	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		View view = View.inflate(context, R.layout.layout_setthought_bottom, null);
		ll_addthought = (LinearLayout) view.findViewById(R.id.ll_addthought);
		if (isSeting) {
			ll_addthought.setVisibility(View.VISIBLE);
		}else {
			ll_addthought.setVisibility(View.GONE);
		}
		return view;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode==REQUEST_SETTHOUGHT){//设置的数据回传
				if (resultCode==AddThoughtActivity.RESULT_SETTHOUGHT){
					Bundle bundle=data.getExtras();  
					if (bundle != null) {
						if (isNewThought) {//新数据首次修改
							isNewThought = false;
							String newThought = bundle.getString("newThoughtItem");
							thoughtList.add(newThought);
						}else {//修改旧数据和新数据多次修改
							String alterThoughtItem = bundle.getString("alterThoughtItem");
							if (-1 != alterThought) {//新数据多次修改
								thoughtList.remove(alterThought);
								thoughtList.add(alterThought, alterThoughtItem);
								alterThought = -1;
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
			ll_addthought.setOnClickListener(this);
		}
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.ll_addthought:
			Intent intent = new Intent(context,AddThoughtActivity.class);
			isNewThought = true;
			startActivityForResult(intent, REQUEST_SETTHOUGHT);
			break;
		}
	}
	class ThoughtAdapter extends BaseMyAdapter<String> {

		public ThoughtAdapter(Context context, List<String> list) {
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
				view = View.inflate(context, R.layout.layout_thoughtitem, null);
				holder = new ViewHolder();
				holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
				holder.iv_del = (ImageView) view.findViewById(R.id.iv_del);
				holder.tv_thought = (TextView) view.findViewById(R.id.tv_thought);
				if (isSeting) {
					holder.iv_del.setVisibility(View.VISIBLE);
				}else{
					holder.iv_del.setVisibility(View.GONE);
				}
				view.setTag(holder);
			}
			
			holder.tv_title.setText("价值观与信念"+StringUtil.numberLower2Capital(position+1));
			holder.iv_del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ShowUtils.showDiaLog(context, "温馨提醒", "请确定,是否删除此价值观?", new ConfirmBackCall() {
						@Override
						public void confirmOperation() {
							thoughtList.remove(position);
							notifyDataSetChanged();
						}
					});
				}
			});
			holder.tv_thought.setText(list.get(position));
			return view;

		}
		class ViewHolder{
			TextView tv_title;
			ImageView iv_del;
			TextView tv_thought;
		}
	}
	@Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
        	if (isSeting) {
        		ShowUtils.showDiaLog(context, "温馨提醒", "您的设置还没保存,是否要取消!", new ConfirmBackCall() {
        			@Override
        			public void confirmOperation() {
        				SetThoughtActivity.this.finish();
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
