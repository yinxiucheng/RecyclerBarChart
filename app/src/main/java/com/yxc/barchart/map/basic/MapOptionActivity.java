package com.yxc.barchart.map.basic;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.CameraPosition;
import com.yxc.barchart.map.util.Constants;

/**
 * 通过Java代码添加一个SupportMapFragment对象
 */
public class MapOptionActivity extends FragmentActivity {

	private static final String MAP_FRAGMENT_TAG = "map";
	static final CameraPosition LUJIAZUI = new CameraPosition.Builder()
			.target(Constants.SHANGHAI).zoom(18).bearing(0).tilt(30).build();
	private AMap aMap;
	private SupportMapFragment aMapFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initMap();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		AMapOptions aOptions = new AMapOptions();
		aOptions.zoomGesturesEnabled(false);// 禁止通过手势缩放地图
		aOptions.scrollGesturesEnabled(false);// 禁止通过手势移动地图
		aOptions.camera(LUJIAZUI);
		if (aMapFragment == null) {
			aMapFragment = SupportMapFragment.newInstance(aOptions);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager()
					.beginTransaction();
			fragmentTransaction.add(android.R.id.content, aMapFragment,
					MAP_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

	}

	/**
	 * 初始化AMap对象
	 */
	private void initMap() {
		if (aMap == null) {
			aMap = aMapFragment.getMap();// amap对象初始化成功
		}
	}
}
