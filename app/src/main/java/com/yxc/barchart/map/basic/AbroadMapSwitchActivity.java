package com.yxc.barchart.map.basic;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.yxc.barchart.R;
import com.yxc.barchart.map.util.ToastUtil;


/**
 * AMapV1地图中介绍如何显示一个基本地图
 */
public class AbroadMapSwitchActivity extends Activity {
	private AMap aMap;
	private MapView mapView;
	private final static LatLng SYDNEY = new LatLng(-33.86759, 151.2088);
	private final static LatLng BEIJING = new LatLng(39.8965, 116.4074);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.abroadmap_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		MapsInitializer.loadWorldGridMap(true);
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	/**
	 * 对地图添加onMapIsAbroadListener
	 */
	private void setUpMap() {
		aMap.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChangeFinish(CameraPosition cameraPosition) {
				ToastUtil.showShortToast(getApplicationContext(), "当前地图中心位置是否在国外: "+cameraPosition.isAbroad);
			}

			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
			}
		});
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

	/**
	 * 移动到悉尼
	 */
	public void moveToSydney(View v) {
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY, 14));
		aMap.invalidate();// 刷新地图
	}

	/**
	 * 移动到北京
	 */
	public void moveToBeiJing(View v) {
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BEIJING, 14));
		aMap.invalidate();// 刷新地图
	}

}
