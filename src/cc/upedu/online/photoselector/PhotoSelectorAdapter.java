package cc.upedu.online.photoselector;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;

import cc.upedu.online.R;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemCheckedListener;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemDeleteListener;
import cc.upedu.online.photoselector.PhotoItem.onItemLongClickListener;

import java.util.ArrayList;

/**
 * 
 * @author Aizaz AZ
 *
 */

public class PhotoSelectorAdapter extends MBaseAdapter<PhotoModel> {

	private int itemWidth;
	private int horizentalNum = 3;
	private onPhotoItemCheckedListener listener;
	private onPhotoItemDeleteListener delistener;
	private LayoutParams itemLayoutParams;
	private onItemLongClickListener mCallback;
	private OnClickListener cameraListener;
	private Uri uri;
	private ArrayList<PhotoModel> selected;

	private PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models) {
		super(context, models);
	}

	public PhotoSelectorAdapter(Context context, ArrayList<PhotoModel> models,
			int screenWidth, onPhotoItemCheckedListener listener,
			onItemLongClickListener mCallback, OnClickListener cameraListener,
			onPhotoItemDeleteListener delistener) {
		this(context, models);
		setItemWidth(screenWidth);
		this.listener = listener;
		this.delistener = delistener;
		this.mCallback = mCallback;
		this.cameraListener = cameraListener;
	}

	public void setItemWidth(int screenWidth) {
		int horizentalSpace = context.getResources().getDimensionPixelSize(
				R.dimen.sticky_item_Spacing);
		this.itemWidth = (screenWidth - (horizentalSpace * (horizentalNum - 1)))
				/ horizentalNum;
		this.itemLayoutParams = new LayoutParams(itemWidth, itemWidth);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		PhotoItem item = null;
		// if (convertView == null || !(convertView instanceof PhotoItem)) {
		item = new PhotoItem(context, listener, this, delistener);
		item.setLayoutParams(itemLayoutParams);
		convertView = item;
		// } else {
		// item = (PhotoItem) convertView;
		// }
		try {
			if (selected.contains(models.get(position))) {
				models.get(position).setChecked(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		item.setImageDrawable(models.get(position));
		if (models.get(position).isCamera()) {
			item.setSelected(false);
			item.setOnClickListener(null, position);
		} else {
			item.setSelected(models.get(position).isChecked());
			item.setOnClickListener(mCallback, position);
		}

		return convertView;
	}

	public void setUri(Uri uri) {
		this.uri = uri;
	}

	public Uri getUri() {
		return this.uri;
	}

	public void setSelected(ArrayList<PhotoModel> selected) {
		// TODO Auto-generated method stub
		this.selected = selected;
	}
}
