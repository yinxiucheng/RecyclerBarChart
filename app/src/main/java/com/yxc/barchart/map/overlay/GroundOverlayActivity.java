package com.yxc.barchart.map.overlay;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.GroundOverlay;
import com.amap.api.maps2d.model.GroundOverlayOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.yxc.barchart.R;

/**
 * AMapV1地图中简单介绍一些GroundOverlay的用法.
 */
public class GroundOverlayActivity extends Activity {

	private AMap amap;
	private MapView mapview;
	private GroundOverlay groundoverlay;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groundoverlay_activity);
		mapview = (MapView) findViewById(R.id.map);
		mapview.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (amap == null) {
			amap = mapview.getMap();
			addOverlayToMap();
		}
	}

	/**
	 * 往地图上添加一个groundoverlay覆盖物
	 */
	private void addOverlayToMap() {
		amap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.936713,
				116.386475), 18));// 设置当前地图显示为北京市恭王府
		LatLngBounds bounds = new LatLngBounds.Builder()
				.include(new LatLng(39.935029, 116.384377))
				.include(new LatLng(39.939577, 116.388331)).build();

		groundoverlay = amap.addGroundOverlay(new GroundOverlayOptions()
				.anchor(0.5f, 0.5f)
				.transparency(0.1f)
				.image(BitmapDescriptorFactory
						.fromResource(R.mipmap.groundoverlay))
				.positionFromBounds(bounds));
	}

	/**
	 * 方法必须重写
	 */
	protected void onResume() {
		super.onResume();
		mapview.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapview.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapview.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapview.onDestroy();
	}
}
