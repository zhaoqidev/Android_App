package cc.upedu.online.photoselector;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.GridView;

import cc.upedu.online.photoselector.PhotoItem.onItemLongClickListener;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemCheckedListener;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemDeleteListener;

/**
 * wa.android.libs.photoselector.PhotoSelectorView
 * 
 * @author guowla@yonyou create at 2015年6月3日 上午10:51:38
 */
public class PhotoSelectorView extends GridView implements
		onPhotoItemCheckedListener, onItemLongClickListener, OnClickListener, onPhotoItemDeleteListener {
	private Context context;
	private PhotoSelectorViewAdapter pPhotoSelectorViewAdapter;
	private ArrayList<PicImage> picList = new ArrayList<PicImage>();
	private OnClickListener addBtnclick = null;
	private int numColumns = 4;
	private int maxImags = 10;

	/**
	 * contructor
	 * 
	 * @author guowla@yonyou create at 2015年6月3日 上午10:51:45
	 * @param context
	 * @param attrs
	 */
	public PhotoSelectorView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * contructor
	 * 
	 * @author guowla@yonyou create at 2015年6月3日 上午10:51:49
	 * @param context
	 */
	public PhotoSelectorView(Context context) {
		super(context, null);
	}

	/**
	 * contructor
	 * 
	 * @author guowla@yonyou create at 2015年6月3日 上午10:51:52
	 * @param context
	 * @param picList
	 */
	public PhotoSelectorView(Context context, ArrayList<PicImage> picList,
			OnClickListener addBtnclick,int numColumns,int maxImags) {
		super(context, null);
		this.context = context;
		this.picList = picList;
		this.addBtnclick = addBtnclick;
		this.numColumns = numColumns;
		this.maxImags = maxImags;
		initView();
	}

	public void notifyAdapter() {
		this.pPhotoSelectorViewAdapter.notifyDataSetChanged();
	}

	/**
	 * @author guowla@yonyou create at 2015年6月3日 上午10:56:52 void
	 */

	private void initView() {
		this.setNumColumns(numColumns);
		this.setStretchMode(STRETCH_COLUMN_WIDTH);
		this.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				getWidthPixels((Activity) context)));
		pPhotoSelectorViewAdapter = new PhotoSelectorViewAdapter(context,
				picList, getWidthPixels((Activity) context), this, (onItemLongClickListener) context, this,
				addBtnclick, this,numColumns);
		this.setAdapter(pPhotoSelectorViewAdapter);
	}

	/**
	 * @author guowla@yonyou create at 2015年6月3日 上午10:52:10
	 * @param activity
	 * @return int
	 */

	public static int getWidthPixels(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(PhotoModel photoModel,
			CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeleteClick(PhotoModel photoModel, View view,int position) {
		if (picList.size() == maxImags&&picList.get(picList.size()-1).isPic) {
			PicImage picImage1 = new PicImage();
			picImage1.setPic(false);
			picList.add(picImage1);
		}
		for(PicImage pic:picList){
			if(photoModel.getOriginalPath().equals(pic.getOriginalPath())){
				picList.remove(pic);
				break;
			}
	
		}
		notifyAdapter();
	}

	@Override
	public void onItemLongClick(int position) {

	}

//	@Override
//	public void onItemClick(int position) {
//		// TODO Auto-generated method stub
//		if (null == picList.get(position).getOriginalPath()
//				||picList.get(position).getOriginalPath().equals("")) {
////			downLoadPic
//		}
//	}
}
