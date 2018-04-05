package cc.upedu.online.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import cc.upedu.online.R;
import cc.upedu.online.utils.StringUtil;

public class BaiduMapAddress extends Activity {
	MapView mMapView = null;
	BaiduMap mBaiduMap;
	EditText conaddress;
	Button btn_confirm;
	
	String city;
	
	static int RESULT_ADDRESS=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());//初始化百度地图
		setContentView(R.layout.activity_baidu_map);
		 
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		conaddress=(EditText) findViewById(R.id.conaddress);
		btn_confirm=(Button) findViewById(R.id.btn_confirm);
		
		city=getIntent().getStringExtra("city");
		if (!StringUtil.isEmpty(city)) {
			goToCity();
		}
		
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			
			@Override
			public boolean onMapPoiClick(MapPoi poi) {
				poi.getName(); //名称
				//System.out.println(")))))))))))))))))))))"+poi.getName());
				poi.getPosition(); //坐标    		
				//System.out.println("((((((((((((((((((((("+poi.getPosition());
				
				LatLng lat=new LatLng(poi.getPosition().latitude,poi.getPosition().longitude);
				popWindow(lat);
				conaddress.setText(poi.getName());
			    return false;
			}
			
			@Override
			public void onMapClick(LatLng point) {
				double myLatitude = point.latitude;
	            double myLongitude = point.longitude; 
	            popWindow(point); 
	            conaddress.setText("");
			}
		});
		
		/*
		 * 确认按钮的点击事件
		 */
		btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String str= conaddress.getText().toString().trim();
				Intent intent=new Intent();  
	            intent.putExtra("address", str);  
	            setResult(RESULT_ADDRESS, intent);  
	            finish();  
			}
		});
	}
	/**
	 * 地图跳转到用户选择的活动城市
	 */
	public void goToCity(){
		/**
		 * 地理编码请求参数
		 */
		GeoCodeOption option=new GeoCodeOption();
		option.city(city);
		option.address(city);
		/**
		 * 地址编码查询对象
		 */
		GeoCoder geoCoder= GeoCoder.newInstance();
		geoCoder.geocode(option);
		
		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			
			@Override
			public void onGetReverseGeoCodeResult(
					ReverseGeoCodeResult reversegeocoderesult) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onGetGeoCodeResult(GeoCodeResult geocoderesult) {
				LatLng latLng = geocoderesult.getLocation();
				
				MapStatus mMapStatus = new MapStatus.Builder().target(latLng).zoom(12).build();
				MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

				mBaiduMap.setMapStatus(mMapStatusUpdate); 
			}
		});
		
	
	}

	@Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
	        mMapView.onDestroy();  
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
	        mMapView.onResume();  
	        }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
	        mMapView.onPause();  
	        }  
	    
	
		public void popWindow(LatLng point){
			//定义Maker坐标点  
			//LatLng point = new LatLng(39.963175, 116.400244);  
			//构建Marker图标  
			BitmapDescriptor bitmap = BitmapDescriptorFactory  
			    .fromResource(R.drawable.nav_turn_via_1);  
			//构建MarkerOption，用于在地图上添加Marker  
			OverlayOptions option = new MarkerOptions().position(point).icon(bitmap);
			//在地图上添加Marker，并显示  
			mBaiduMap.clear();
			mBaiduMap.addOverlay(option);
		}
		
}
