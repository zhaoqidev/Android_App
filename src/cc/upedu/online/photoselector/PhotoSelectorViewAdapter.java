package cc.upedu.online.photoselector;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;

import cc.upedu.online.photoselector.PhotoItem.onItemLongClickListener;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemCheckedListener;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemDeleteListener;



/**
 * wa.android.libs.photoselector.PhotoSelectorViewAdapter
 * @author guowla@yonyou
 * create at 2015年6月3日 下午2:19:17
 */
public class PhotoSelectorViewAdapter extends MBaseAdapter<PicImage> {

	private int itemWidth;
	private int numColumns = 4;
	private onPhotoItemCheckedListener listener;
	private onPhotoItemDeleteListener delistener;
	private LayoutParams itemLayoutParams;
	private onItemLongClickListener mCallback;
	private OnClickListener cameraListener;
	private Context context;
	private boolean isCamera = false;
	private OnClickListener addBtnclick;
	private PhotoSelectorViewAdapter(Context context, ArrayList<PicImage> models,int numColumns) {
		super(context, models);
		this.context = context;
		this.numColumns = numColumns;
	}

	/**
	 *
	 * @param context 上下文
	 * @param models picImage对象的集合
	 * @param screenWidth 屏幕宽度
	 * @param listener
	 * @param mCallback item的长点击事件
	 * @param cameraListener
	 * @param addBtnclick
	 * @param delistener
	 * @param numColumns
	 */
	public PhotoSelectorViewAdapter(Context context,
			ArrayList<PicImage> models, int screenWidth,
			onPhotoItemCheckedListener listener, onItemLongClickListener mCallback,
			OnClickListener cameraListener,OnClickListener addBtnclick,onPhotoItemDeleteListener delistener,int numColumns) {
		this(context, models, numColumns);
		setItemWidth(screenWidth);
		this.listener = listener;
		this.delistener = delistener;
		this.mCallback = mCallback;
		this.cameraListener = cameraListener;
		this.addBtnclick = addBtnclick;
	}

	public void setItemWidth(int screenWidth) {
		int horizentalSpace = (int) (8* (context.getResources()
				.getDisplayMetrics().density));
		this.itemWidth = (screenWidth - (horizentalSpace * (numColumns - 1)))
				/ numColumns;
		this.itemLayoutParams = new LayoutParams(itemWidth-horizentalSpace, itemWidth-horizentalSpace);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoItem item = null;
			item = new PhotoItem(context, listener,null, delistener);
			item.getCbPhoto().setVisibility(View.GONE);
			item.setLayoutParams(itemLayoutParams);
			convertView = item;
		if (models.get(position).isPic()) {
			item.setImageDrawable(models.get(position));
			item.setOnClickListener(mCallback, position);
		} else {
			item.setBtnImageDrawable(models.get(position));
			item.setOnClickListener(addBtnclick);
		}

		return convertView;
	}

	public boolean isCamera() {
		return isCamera;
	}

	public void setCamera(boolean isCamera) {
		this.isCamera = isCamera;
	}
}
