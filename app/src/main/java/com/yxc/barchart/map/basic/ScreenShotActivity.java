package com.yxc.barchart.map.basic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.OnMapScreenShotListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.MarkerOptions;
import com.yxc.barchart.R;
import com.yxc.barchart.map.util.Constants;
import com.yxc.barchart.map.util.ToastUtil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AMapV1地图中对截屏简单介绍
 */
public class ScreenShotActivity extends Activity implements
        OnMapScreenShotListener {
	private AMap aMap;
	private MapView mapView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screenshot_activity);
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

	/**
	 * 对地图添加一个marker
	 */
	private void setUpMap() {
		aMap.addMarker(new MarkerOptions().position(Constants.FANGHENG)
				.title("方恒").snippet("方恒国际中心大楼A座"));
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
	 * 对地图进行截屏
	 */
	public void getMapScreenShot(View v) {
		aMap.getMapScreenShot(this);
		aMap.invalidate();// 刷新地图
	}

	@Override
	public void onMapScreenShot(Bitmap bitmap) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		if(null == bitmap){
			return;
		}
		try {
			FileOutputStream fos = new FileOutputStream(
					Environment.getExternalStorageDirectory() + "/test_"
							+ sdf.format(new Date()) + ".png");
			boolean b = bitmap.compress(CompressFormat.PNG, 100, fos);
			try {
				fos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (b)
				ToastUtil.show(ScreenShotActivity.this, "截屏成功");
			else {
				ToastUtil.show(ScreenShotActivity.this, "截屏失败");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
