package cc.upedu.online.base;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;
import java.util.concurrent.ExecutorService;

import cc.upedu.online.OnlineApp;

public abstract class BaseMyAdapter<T> extends BaseAdapter {
	public Context context;
	public List<T> list;
	private ExecutorService instance;
	
	public BaseMyAdapter(Context context,List<T> list){
		this.context=context;
		this.list=list;
		instance = OnlineApp.myApp.instance;
	}

	@Override
	public int getCount() {
		if (list != null && list.size() > 0) {
			return list.size();
		}else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (list != null && list.size() > 0) {
			return list.get(position);
		}else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView, ViewGroup parent);
}
