package com.yxc.barchart.map.overlay;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Polygon;
import com.amap.api.maps2d.model.PolygonOptions;
import com.yxc.barchart.R;
import com.yxc.barchart.map.util.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * AMapV1地图中简单介绍一些Polygon的用法.
 */
public class PolygonActivity extends Activity implements
		OnSeekBarChangeListener {
	private static final int WIDTH_MAX = 50;
	private static final int HUE_MAX = 255;
	private static final int ALPHA_MAX = 255;
	private AMap aMap;
	private MapView mapView;
	private Polygon polygon;
	private SeekBar mColorBar;
	private SeekBar mAlphaBar;
	private SeekBar mWidthBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.polygon_activity);
		mapView = (MapView) findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
	}

	/**
	 * 初始化AMap对象
	 */
	private void init() {
		mColorBar = (SeekBar) findViewById(R.id.hueSeekBar);
		mColorBar.setMax(HUE_MAX);
		mColorBar.setProgress(50);

		mAlphaBar = (SeekBar) findViewById(R.id.alphaSeekBar);
		mAlphaBar.setMax(ALPHA_MAX);
		mAlphaBar.setProgress(50);

		mWidthBar = (SeekBar) findViewById(R.id.widthSeekBar);
		mWidthBar.setMax(WIDTH_MAX);
		mWidthBar.setProgress(25);
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}

	private void setUpMap() {
		mColorBar.setOnSeekBarChangeListener(this);
		mAlphaBar.setOnSeekBarChangeListener(this);
		mWidthBar.setOnSeekBarChangeListener(this);
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.BEIJING, 5));// 设置指定的可视区域地图
		// 绘制一个长方形
		aMap.addPolygon(new PolygonOptions()
				.addAll(createRectangle(Constants.SHANGHAI, 1, 1))
				.fillColor(Color.LTGRAY).strokeColor(Color.RED).strokeWidth(1));
		PolygonOptions options = new PolygonOptions();
		int numPoints = 400;
		float semiHorizontalAxis = 5f;
		float semiVerticalAxis = 2.5f;
		double phase = 2 * Math.PI / numPoints;
		for (int i = 0; i <= numPoints; i++) {
			options.add(new LatLng(Constants.BEIJING.latitude
					+ semiVerticalAxis * Math.sin(i * phase),
					Constants.BEIJING.longitude + semiHorizontalAxis
							* Math.cos(i * phase)));
		}
		// 绘制一个椭圆
		polygon = aMap.addPolygon(options.strokeWidth(25)
				.strokeColor(Color.argb(50, 1, 1, 1))
				.fillColor(Color.argb(50, 1, 1, 1)));
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
	 * 生成一个长方形的四个坐标点
	 */
	private List<LatLng> createRectangle(LatLng center, double halfWidth,
                                         double halfHeight) {
		return Arrays.asList(new LatLng(center.latitude - halfHeight,
				center.longitude - halfWidth), new LatLng(center.latitude
				- halfHeight, center.longitude + halfWidth), new LatLng(
				center.latitude + halfHeight, center.longitude + halfWidth),
				new LatLng(center.latitude + halfHeight, center.longitude
						- halfWidth));
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	/**
	 * Polygon中对填充颜色，透明度，画笔宽度设置响应事件
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (polygon == null) {
			return;
		}
		if (seekBar == mColorBar) {
			polygon.setFillColor(Color.argb(progress, 1, 1, 1));
			
		} else if (seekBar == mAlphaBar) {
			polygon.setStrokeColor(Color.argb(progress, 1, 1, 1));
		} else if (seekBar == mWidthBar) {
			polygon.setStrokeWidth(progress);
		}
		aMap.invalidate();//刷新地图
	}
}
