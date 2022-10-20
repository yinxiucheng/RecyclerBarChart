package com.yxc.barchart.map.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import com.amap.api.maps.model.Marker;


public class SensorEventHelper implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private long lastTime = 0;
	private final int TIME_SENSOR = 100;
	private float mAngle;
	private Context mContext;
	private Marker mMarker;

	public SensorEventHelper(Context context) {
		mContext = context;
		mSensorManager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

	}

	public void registerSensorListener() {
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	public void unRegisterSensorListener() {
		mSensorManager.unregisterListener(this, mSensor);
	}

	public void setCurrentMarker(Marker marker) {
		mMarker = marker;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
			return;
		}
		switch (event.sensor.getType()) {
		case Sensor.TYPE_ORIENTATION: {
			float x = event.values[0];
			x += getScreenRotationOnPhone(mContext);
			x %= 360.0F;
			if (x > 180.0F)
				x -= 360.0F;
			else if (x < -180.0F)
				x += 360.0F;
			
			if (Math.abs(mAngle - x) < 3.0f) {
				break;
			}
			mAngle = Float.isNaN(x) ? 0 : x;
			if (mMarker != null) {
				mMarker.setRotateAngle(360-mAngle);
			}
			lastTime = System.currentTimeMillis();
		}
		}

	}

	public static int getScreenRotationOnPhone(Context context) {
		final Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		switch (display.getRotation()) {
		case Surface.ROTATION_0:
			return 0;

		case Surface.ROTATION_90:
			return 90;

		case Surface.ROTATION_180:
			return 180;

		case Surface.ROTATION_270:
			return -90;
		}
		return 0;
	}
}
