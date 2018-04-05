package cc.upedu.online.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cc.upedu.online.R;
import cc.upedu.online.base.BaseMyAdapter;
import cc.upedu.online.base.TwoPartModelGridViewBottomBaseActivity;
import cc.upedu.online.domin.UserCordBean.Entity.UserInfo.PicItem;
import cc.upedu.online.utils.ImageUtils;
/**
 * 展示我的图片
 * @author Administrator
 *
 */
public class ShowPictureActivity extends TwoPartModelGridViewBottomBaseActivity{
	public static final int RESULT_SETPICTURE = 2;
	private List<PicItem> picList;
	private View picView;
	private List<String> picUrlList;

	@Override
	protected void initTitle() {
		// TODO Auto-generated method stub
		setTitleText("用户图片");
		picList = (List<PicItem>)getIntent().getSerializableExtra("picList");
	}
	@Override
	protected void initData() {
		if (isAdapterEmpty()) {
			setNumColumns(3);
			setGridView(new MyGridViewAdapter(context,picList));
		}else {
			notifyData();
		}
	}
	public class MyGridViewAdapter extends BaseMyAdapter<PicItem> {
		private LayoutInflater inflater;

		public MyGridViewAdapter(Context context, List list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			inflater = LayoutInflater.from(ShowPictureActivity.this);
			View picView = inflater.inflate(R.layout.gv_picitem_show, null);
			if (position == 0) {
				picUrlList = new ArrayList<String>();
				for (PicItem picitem : list) {
					picUrlList.add(picitem.getPicPath());
				}
			}
			ImageButton picIBtn = (ImageButton) picView.findViewById(R.id.pic);
	
			ImageUtils.setImageToImageButton(picUrlList.get(position), picIBtn, R.drawable.wodeimg_default);
			
			picIBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
						Intent intent = new Intent(context,ImageActivity.class);
						intent.putExtra("image_list", (Serializable) picUrlList);
						intent.putExtra("image_position", position);
						context.startActivity(intent);
				}
			});
			return picView;
		}
	}
	@Override
	public View initBottomLayout() {
		// TODO Auto-generated method stub
		return null;
	}
}
