package com.yxc.barchart.map.basic;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.MapsInitializer;
import com.yxc.barchart.R;

/*
 * 替换底图资源显示，这里用于显示openstreetmap的底图
 * (OSM数据在国内采用原始84坐标系，在国内谨慎使用，  国外场景下可使用osm数据)
 * */
public class OsmMapActivity extends Activity {
	private MapView mapView;

	private static final String OSM_URL = "http://a.tile.openstreetmap.org/%d/%d/%d.png";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.osmmap_activity);

		// 用开源栅格图源替换AMap图源（坐标系符合国测局规定坐标系）
		// 相关限制：1、需要在地图初始化之前调用该方法
		// 2、设置该方法会导致中英文地图切换失效
		// 3、提供地址默认替换顺序为 zoom、x、y
		MapsInitializer.replaceURL(OSM_URL, "OSM"); // 地图初始化之前设置,将底图资源进行替换
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写

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

}
