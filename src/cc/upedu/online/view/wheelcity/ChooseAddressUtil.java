package cc.upedu.online.view.wheelcity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.ArrayList;

public class ChooseAddressUtil {
	private ArrayList<AddressBean> list;
	public void getAddressData(Context context,final SQLiteDatabase db,final SucceedCallBack mSucceedCallBack,final int parent_id){
		
		if (list == null) {
			list = new ArrayList<AddressBean>();
		}else {
			list.clear();
		}
		new AsyncTask<Integer, Integer, String>() {

			@Override
			protected String doInBackground(Integer... params) {
				
				// 对数据库进行操作  
//						db1.execSQL("insert into tb([ID],[content]) values(null, 'db1');");
				Cursor cursor = db.rawQuery("select * from citys where parent_id=?;",new String[]{String.valueOf(parent_id)});
				while(cursor.moveToNext()){
					AddressBean mAddressBean = new AddressBean();
					int id = cursor.getInt(cursor.getColumnIndex("id"));  //根据索引获取属性
					int parent_id = cursor.getInt(cursor.getColumnIndex("parent_id"));
					String area_name = cursor.getString(cursor.getColumnIndex("area_name"));
					int area_type = cursor.getInt(cursor.getColumnIndex("area_type"));
					mAddressBean.setId(id);
					mAddressBean.setParent_id(parent_id);
					mAddressBean.setArea_name(area_name);
					mAddressBean.setArea_type(area_type);
					list.add(mAddressBean);
				}
				cursor.close();
				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				mSucceedCallBack.onSucceed(list);
				super.onPostExecute(result);
				
			}
		}.execute(0);
	}
	public abstract class SucceedCallBack{
		public abstract void onSucceed(ArrayList<AddressBean> List);
	}
}
