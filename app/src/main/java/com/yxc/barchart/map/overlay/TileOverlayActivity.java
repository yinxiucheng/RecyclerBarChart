package com.yxc.barchart.map.overlay;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.TileOverlayOptions;
import com.amap.api.maps2d.model.UrlTileProvider;
import com.yxc.barchart.R;

import java.net.URL;

/**
 * AMapV2地图中简单介绍TileOverlay绘制
 */
public class TileOverlayActivity extends Activity {

	private AMap aMap;
	private MapView mapView;
	final String url = "http://a.tile.openstreetmap.org/%d/%d/%d.png";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.arc_activity);
        /*
         * 设置离线地图存储目录，在下载离线地图或初始化地图设置;
         * 使用过程中可自行设置, 若自行设置了离线地图存储的路径，
         * 则需要在离线地图下载和使用地图页面都进行路径设置
         * */
	    //Demo中为了其他界面可以使用下载的离线地图，使用默认位置存储，屏蔽了自定义设置
//        MapsInitializer.sdcardDir =OffLineMapUtils.getSdCacheDir(this);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
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

	private void setUpMap() {

		aMap.moveCamera(CameraUpdateFactory.zoomTo(4));

//		final String url = "http://mt0.google.cn/vt/lyrs=y@198&hl=zh-CN&gl=cn&src=app&x=%d&y=%d&z=%d&s=";

		aMap.addTileOverlay(new TileOverlayOptions().tileProvider(new UrlTileProvider(256, 256) {
			
			@Override
			public URL getTileUrl(int x, int y, int zoom) {
				try {
					return new URL(String.format(url, zoom, x, y));
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		})
		.diskCacheEnabled(true)  
        .diskCacheDir("/storage/emulated/0/amap/cache")
        .diskCacheSize(100000)  
        .memoryCacheEnabled(true)  
        .memCacheSize(100000));
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
