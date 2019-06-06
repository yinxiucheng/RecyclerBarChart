package com.yxc.barchart.map.location;


import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.yxc.barchart.R;


/**
 * AMapV2地图中介绍定位几种
 */
public class LocationModeSourceActivity extends Activity implements AMap.OnMyLocationChangeListener,OnCheckedChangeListener {
	private AMap aMap;
	private MapView mapView;
	private RadioGroup mGPSModeGroup;

	private MyLocationStyle myLocationStyle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
		setContentView(R.layout.locationmodesource_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		mGPSModeGroup = (RadioGroup) findViewById(R.id.gps_radio_group);
		mGPSModeGroup.setOnCheckedChangeListener(this);

		//设置SDK 自带定位消息监听
		aMap.setOnMyLocationChangeListener(this);
	}


	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {

		// 如果要设置定位的默认状态，可以在此处进行设置
		myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker));
		aMap.setMyLocationStyle(myLocationStyle);

		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
			case R.id.gps_show_button:
				// 只定位，不进行其他操作
				aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW));
            case R.id.gps_locate_button:
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE));
                break;
            case R.id.gps_follow_button:
                // 设置定位的类型为 跟随模式
                aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW));
                break;
			case R.id.gps_follow_button_nocenter :
				// 设置定位的类型为 持续定位不移动到中心点
				aMap.setMyLocationStyle(myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER));
				break;
		}

	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();

	}

	@Override
	public void onMyLocationChange(Location location) {

		// 定位回调监听
		if(location != null) {

			Log.e("amap", "onMyLocationChange 定位成功， lat: " + location.getLatitude() + " lon: " + location.getLongitude());
			Bundle bundle = location.getExtras();
			if(bundle != null) {
				int errorCode = bundle.getInt(MyLocationStyle.ERROR_CODE);
				String errorInfo = bundle.getString(MyLocationStyle.ERROR_INFO);
				// 定位类型，可能为GPS WIFI等，具体可以参考官网的定位SDK介绍
				int locationType = bundle.getInt(MyLocationStyle.LOCATION_TYPE);

                /*
                errorCode
                errorInfo
                locationType
                */
				Log.e("amap", "定位信息， code: " + errorCode + " errorInfo: " + errorInfo + " locationType: " + locationType );
			} else {
				Log.e("amap", "定位信息， bundle is null ");

			}

		} else {
			Log.e("amap", "定位失败");
		}
	}
}
