package cc.upedu.online.photoselector;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * wa.android.libs.photoselector.MBaseAdapter
 * @author guowla@yonyou
 * create at 2015年6月8日 上午10:25:32
 * @param <T>
 */
public class MBaseAdapter<T> extends BaseAdapter {

	protected Context context;
	protected ArrayList<T> models;

	public MBaseAdapter(Context context, ArrayList<T> models) {
		this.context = context;
		if (models == null)
			this.models = new ArrayList<T>();
		else
			this.models = models;
	}

	@Override
	public int getCount() {
		if (models != null) {
			return models.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return models.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

	public void update(List<T> models) {
		if (models == null){
			return;	
		}else{
			this.models.clear();
			addCameraItem(models);

			for (T t : models) {
				this.models.add(t);
			}
			notifyDataSetChanged();
		}
	}
	

	public void updateWithoutPic() {
		//这里为了添加相机调用功能
		PhotoModel pPhotoModel = new PhotoModel();
		pPhotoModel.setCamera(true);
		pPhotoModel.setChecked(false);
		pPhotoModel.setOriginalPath("");
		this.models.add((T) pPhotoModel);
		notifyDataSetChanged();
	}
	
	private void addCameraItem(List<T> models) {
		try {
			if (models.get(0) instanceof PhotoModel) {
				//这里为了添加相机调用功能
				PhotoModel pPhotoModel = new PhotoModel();
				pPhotoModel.setCamera(true);
				pPhotoModel.setChecked(false);
				pPhotoModel.setOriginalPath("");
				this.models.add((T) pPhotoModel);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public ArrayList<T> getItems() {
		return models;
	}

}
