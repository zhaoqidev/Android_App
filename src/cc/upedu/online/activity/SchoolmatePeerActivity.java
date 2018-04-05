package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cc.upedu.online.R;
import cc.upedu.online.activity.SchoolmatePeerActivity.PeerBean.Entity;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.ListBaseActivity;
import cc.upedu.online.net.MyBaseParser;
import cc.upedu.online.net.RequestVo;
import cc.upedu.online.utils.ConstantsOnline;
import cc.upedu.online.utils.ParamsMapUtil;
import cc.upedu.online.utils.ShowUtils;
/**
 * 同行学友筛选界面
 * @author Administrator
 * @param <Entity>
 *
 */
public class SchoolmatePeerActivity extends ListBaseActivity<Entity> {
	List<Entity> list=new ArrayList<Entity>();
	PeerBean beenBean =new PeerBean();
	
	private RequestVo requestVo;
	private DataCallBack<PeerBean> dataCallBack;
	
	private final int INUDSTRY=1;//选择行业的标签

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("行业学友");
	}
	@Override
	protected void initData() {
		Map<String, String> paramsMap = ParamsMapUtil.basicData(context, "3");
		requestVo = new RequestVo(ConstantsOnline.BASIC_DATA, context,
				paramsMap, new MyBaseParser<>(PeerBean.class));
		dataCallBack=new DataCallBack<PeerBean>() {

			@Override
			public void processData(PeerBean object) {
				if("true".equals(object.success)){
					if(object.entity!=null){
						list=object.entity;
						if (isAdapterEmpty()) {
							setListView(new MyAdapter(context,list));
							setOnItemClickListion(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
									/*Intent intent=new Intent(context, SchoolmateActivity.class);
									intent.putExtra("content", list.get(position).content);
									startActivity(intent);*/
					                Intent intent = new Intent();
					                intent.putExtra("id", list.get(position).id);
					                //设置返回数据
					                setResult(INUDSTRY, intent);
					                //关闭Activity
					                finish();
								}
							});
						}
					}
				}else {
					ShowUtils.showMsg(context, object.message);
				}
				
			}
		};
		getDataServer(requestVo, dataCallBack);
	}

	/**
	 * 数据适配器
	 * 
	 * @author Administrator
	 * 
	 */
	class MyAdapter extends BaseMyAdapter<Entity> {
		
		public MyAdapter(Context context, List<Entity> list) {
			super(context, list);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context,R.layout.activity_schoolmate_peer_item, null);
				holder.tv_peer = (TextView) convertView.findViewById(R.id.tv_peer);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_peer.setText(list.get(position).content);
			return convertView;

		}

		class ViewHolder {
			TextView tv_peer;
		}
	}
	/**
	 * 请求筛选条件列表的javabean
	 * @author Administrator
	 *
	 */
	class PeerBean{
		public String success;
		public String message;
		public List<Entity> entity;
		
		class Entity{
			public String content;
			public String id;
			public String type;
		}
	}
}
