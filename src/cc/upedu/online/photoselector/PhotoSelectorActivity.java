package cc.upedu.online.photoselector;
/**
 * 
 * @author Aizaz AZ
 *
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.upedu.online.R;
import cc.upedu.online.base.ThreePartModelBaseActivity;
import cc.upedu.online.base.ThreePartModelListBaseActivity;
import cc.upedu.online.base.TwoPartModelBottomBaseActivity;
import cc.upedu.online.interfaces.OnClickMyListener;
import cc.upedu.online.photoselector.PhotoItem.onItemLongClickListener;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemCheckedListener;
import cc.upedu.online.photoselector.PhotoItem.onPhotoItemDeleteListener;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;



/**
 * 图片选择界面
 * wa.android.libs.photoselector.PhotoSelectorActivity
 * @author guowla@yonyou
 * create at 2015年6月9日 上午9:05:15
 */
public class PhotoSelectorActivity extends TwoPartModelBottomBaseActivity implements
		onItemLongClickListener, onPhotoItemCheckedListener, OnItemClickListener,
		onPhotoItemDeleteListener {

	public static final int SINGLE_IMAGE = 1;
	public static final String KEY_MAX = "key_max";
	private int MAX_IMAGE;

	public static final int REQUEST_PHOTO = 0;
	private static final int REQUEST_CAMERA = 1;

	public static String RECCENT_PHOTO = null;

	private GridView gvPhotos;
	private ListView lvAblum;
	private TextView tvAlbum, tvPreview;
	private PhotoSelectorDomain photoSelectorDomain;
	private PhotoSelectorAdapter photoAdapter;
	private AlbumAdapter albumAdapter;
	private RelativeLayout layoutAlbum;
	private ArrayList<PhotoModel> selected;
	private ArrayList<PhotoModel> totalpic;
	private TextView tvNumber;

	@Override
	protected void initData() {

	}

	@Override
	protected void initTitle() {
		setTitleText("选择照片");
		setRightText("确定", new OnClickMyListener() {
			@Override
			public void onClick(View v) {
				ok(); // 选完照片
			}
		});
		RECCENT_PHOTO = "最近照片";
		totalpic = new ArrayList<PhotoModel>();
		if (getIntent().getExtras() != null) {
			MAX_IMAGE = getIntent().getIntExtra(KEY_MAX, 10);
		}
	}

	@Override
	public View initBottomLayout() {
		View bottomView = View.inflate(context,R.layout.activity_photoselector_bottom,null);
		tvAlbum = (TextView) bottomView.findViewById(R.id.tv_album_ar);
		tvNumber = (TextView) bottomView.findViewById(R.id.tv_number);
		tvPreview = (TextView) bottomView.findViewById(R.id.tv_preview_ar);

		selected = new ArrayList<PhotoModel>();
		try {
			selected = (ArrayList<PhotoModel>) getIntent().getSerializableExtra("selected");
			if(selected.get(selected.size()-1) instanceof PicImage&&!((PicImage)selected.get(selected.size()-1)).isPic)
			{
				selected.remove(selected.size()-1);
				tvPreview.setEnabled(true);
			}
		} catch (Exception e) {
			// TODO: handle exception
			selected = new ArrayList<PhotoModel>();
		}

		tvNumber.setText("(" + selected.size() + "/"+MAX_IMAGE+")");
		tvAlbum.setOnClickListener(this);
		tvPreview.setOnClickListener(this);
		return bottomView;
	}

	@Override
	public View initTwelfthLayout() {
		View view = View.inflate(context,R.layout.activity_photoselector,null);
		gvPhotos = (GridView) view.findViewById(R.id.gv_photos_ar);
		lvAblum = (ListView) view.findViewById(R.id.lv_ablum_ar);
		layoutAlbum = (RelativeLayout) view.findViewById(R.id.layout_album_ar);

		photoAdapter = new PhotoSelectorAdapter(this,
				totalpic, CommonUtils.getWidthPixels(this),
				this, this, this,this);
		gvPhotos.setAdapter(photoAdapter);
		photoAdapter.setSelected(selected);
		albumAdapter = new AlbumAdapter(getApplicationContext(),
				new ArrayList<AlbumModel>());
		lvAblum.setAdapter(albumAdapter);
		lvAblum.setOnItemClickListener(this);

		photoSelectorDomain = new PhotoSelectorDomain(getApplicationContext());
		photoSelectorDomain.getReccent(reccentListener); // 更新最近照片
		photoSelectorDomain.updateAlbum(albumListener); // 跟新相册信息
		return view;
	}



	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.tv_album_ar)
			album();
		else if (v.getId() == R.id.tv_preview_ar)
			priview();
		else if (v.getId() == R.id.tv_camera_vc)
			catchPicture();

	}

	/** 拍照 */
	private void catchPicture() {
		CommonUtils.launchActivityForResult(this, new Intent(
				MediaStore.ACTION_IMAGE_CAPTURE), REQUEST_CAMERA);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
//			
//			if(data !=null){ //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);  
//                //返回有缩略图  
//                if(data.hasExtra("data")){  
//                    Bitmap thumbnail = data.getParcelableExtra("data");  
//                    //得到bitmap后的操作  
//                }  
//            }else{  
//                //由于指定了目标uri，存储在目标uri，intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);  
//                // 通过目标uri，找到图片  
//                // 对图片的缩放处理  
//                // 操作  
//            }  
//			PhotoModel photoModel = new PhotoModel(CommonUtils.query(
//					getApplicationContext(), photoAdapter.getUri()));
			// selected.clear();
			// //--keep all
			// selected photos
			// tvNumber.setText("(0)");
			// //--keep all
			// selected photos
			// ///////////////////////////////////////////////////////////////////////////////////////////
			
			PhotoModel photoModel = new PhotoModel(photoAdapter.getUri().toString().replace("file://", ""));
			if (selected.size() >= MAX_IMAGE) {
				Toast.makeText(
						this,
						"不能选择超过%d个图像", Toast.LENGTH_SHORT).show();
				photoModel.setChecked(false);
//				totalpic.add(photoModel);
				photoAdapter.notifyDataSetChanged();
			} else {
				if (!selected.contains(photoModel)) {
					selected.add(photoModel);
				}
			}
			ok();
		}
	}

	/** 完成 */
	private void ok() {
		if (selected.isEmpty()) {
			setResult(RESULT_CANCELED);
		} else {
			Intent data = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable("photos", selected);
			data.putExtras(bundle);
			setResult(RESULT_OK, data);
		}
		finish();
	}

	/** 预览照片 */
	private void priview() {
		Bundle bundle = new Bundle();
		bundle.putSerializable("photos", selected);
		CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}

	private void album() {
		if (layoutAlbum.getVisibility() == View.GONE) {
			popAlbum();
		} else {
			hideAlbum();
		}
	}

	/** 弹出相册列表 */
	private void popAlbum() {
		layoutAlbum.setVisibility(View.VISIBLE);
		new AnimationUtil(getApplicationContext(), R.anim.translate_up_current)
				.setLinearInterpolator().startAnimation(layoutAlbum);
	}

	/** 隐藏相册列表 */
	private void hideAlbum() {
		new AnimationUtil(getApplicationContext(), R.anim.translate_down)
				.setLinearInterpolator().startAnimation(layoutAlbum);
		layoutAlbum.setVisibility(View.GONE);
	}

	/** 清空选中的图片 */
	private void reset() {
		selected.clear();
		tvNumber.setText("(0/" + MAX_IMAGE + ")");
		tvPreview.setEnabled(false);
	}

	@Override
	/** 点击查看照片 */
	public void onItemLongClick(int position) {
		Bundle bundle = new Bundle();
		if (tvAlbum.getText().toString().equals(RECCENT_PHOTO))
			bundle.putInt("position", position - 1);
		else
			bundle.putInt("position", position);
		bundle.putString("album", tvAlbum.getText().toString());
		CommonUtils.launchActivity(this, PhotoPreviewActivity.class, bundle);
	}

	@Override
	/** 照片选中状态改变之后 */
	public void onCheckedChanged(PhotoModel photoModel,
			CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			if(selected.size()<MAX_IMAGE){
			if (!selected.contains(photoModel))
				selected.add(photoModel);
			tvPreview.setEnabled(true);
			
			}else{
				buttonView.setChecked(false);
			}
		} else {
			selected.remove(photoModel);
		}
		tvNumber.setText("("+ selected.size() + "/"+MAX_IMAGE+")");

		if (selected.isEmpty()) {
			tvPreview.setEnabled(false);
			tvPreview.setText("预览");
		}
		
	}

	@Override
	public void onBackPressed() {
		if (layoutAlbum.getVisibility() == View.VISIBLE) {
			hideAlbum();
		} else
			super.onBackPressed();
	}

	@Override
	/** 相册列表点击事件 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		AlbumModel current = (AlbumModel) parent.getItemAtPosition(position);
		for (int i = 0; i < parent.getCount(); i++) {
			AlbumModel album = (AlbumModel) parent.getItemAtPosition(i);
			if (i == position)
				album.setCheck(true);
			else
				album.setCheck(false);
		}
		albumAdapter.notifyDataSetChanged();
		hideAlbum();
		tvAlbum.setText(current.getName());
		// tvTitle.setText(current.getName());

		// 更新照片列表
		if (current.getName().equals(RECCENT_PHOTO))
			photoSelectorDomain.getReccent(reccentListener);
		else
			photoSelectorDomain.getAlbum(current.getName(), reccentListener); // 获取选中相册的照片
	}

	@Override
	public void onDeleteClick(PhotoModel photoModel, View view, int position) {

	}

	/** 获取本地图库照片回调 */
	public interface OnLocalReccentListener {
		public void onPhotoLoaded(List<PhotoModel> photos);
	}

	/** 获取本地相册信息回调 */
	public interface OnLocalAlbumListener {
		public void onAlbumLoaded(List<AlbumModel> albums);
	}

	private OnLocalAlbumListener albumListener = new OnLocalAlbumListener() {
		@Override
		public void onAlbumLoaded(List<AlbumModel> albums) {
			albumAdapter.update(albums);
		}
	};

	private OnLocalReccentListener reccentListener = new OnLocalReccentListener() {
		@Override
		public void onPhotoLoaded(List<PhotoModel> photos) {
			for (PhotoModel model : photos) {
				if (selected.contains(model)) {
					model.setChecked(true);
				}
			}
			if(photos.size()>0){
				photoAdapter.update(photos);
			}else{
				photoAdapter.updateWithoutPic();
			}
			
			gvPhotos.smoothScrollToPosition(0); // 滚动到顶端
			// reset(); //--keep selected photos

		}
	};

}
