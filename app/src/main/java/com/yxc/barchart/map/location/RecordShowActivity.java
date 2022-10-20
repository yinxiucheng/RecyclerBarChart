package com.yxc.barchart.map.location;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.util.LocationComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordLocation;
import com.yxc.barchart.view.LocationMarker;
import com.yxc.commonlib.util.DisplayUtil;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordShowActivity extends AppCompatActivity implements OnMapLoadedListener {
	private final static int AMAP_LOADED = 2;

	private MapView mMapView;
	private AMap mAMap;
	private int mRecordItemId;
	private int recordType;
	private List<LatLng> mOriginLatLngList;
	private ExecutorService mThreadPool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_item);
		initView(savedInstanceState);
		Intent recordIntent = getIntent();
		int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
		mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
		if (recordIntent != null) {
			mRecordItemId = recordIntent.getIntExtra(RecordListActivity.RECORD_ID, -1);
			recordType = recordIntent.getIntExtra(LocationConstants.KEY_RECORD_TYPE, -1);
		}
		initMap();
	}

	private void initView(Bundle savedInstanceState) {
		mMapView = findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);// 此方法必须重写
	}

	UiSettings uiSettings;
	private void initMap() {
		if (mAMap == null) {
			mAMap = mMapView.getMap();
			mAMap.setOnMapLoadedListener(this);
			uiSettings = mAMap.getUiSettings();
			uiSettings.setZoomControlsEnabled(false);
			uiSettings.setLogoBottomMargin(-50);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case AMAP_LOADED:
				setupRecord();
				break;
			default:
				break;
			}
		}
	};

	public void onBackClick(View view) {
		this.finish();
		if (mThreadPool != null) {
			mThreadPool.shutdownNow();
		}
	}


	private LatLngBounds getBounds() {
		LatLngBounds.Builder b = LatLngBounds.builder();
		if (mOriginLatLngList == null) {
			return b.build();
		}
		for (int i = 0; i < mOriginLatLngList.size(); i++) {
			b.include(mOriginLatLngList.get(i));
		}
		return b.build();

	}

	/**
	 * 轨迹数据初始化
	 * 
	 */
	private void setupRecord() {
		Record mRecord = LocationDBHelper.queryRecordById(recordType, mRecordItemId);
		if (mRecord != null) {
			List<RecordLocation> recordLocationList = mRecord.getPathLine();
			List<AMapLocation> recordList = LocationComputeUtil.getAMapLocationList(recordLocationList);
			AMapLocation startLoc = mRecord.getStartPoint();
			AMapLocation endLoc = mRecord.getEndpoint();
			if (recordList == null || startLoc == null || endLoc == null) {
				return;
			}
			LatLng startLatLng = LocationComputeUtil.createLatLng(startLoc);
			LatLng endLatLng = LocationComputeUtil.createLatLng(endLoc);
			mOriginLatLngList = LocationComputeUtil.parseLatLngList(recordList);
			addOriginTrace(startLatLng, endLatLng, mOriginLatLngList);
			addMilePost(recordLocationList);
		}
	}

	private void addMilePost(List<RecordLocation> recordLocationList) {
		for (int i = 0; i < recordLocationList.size(); i++) {
			RecordLocation recordLocation = recordLocationList.get(i);
			if (recordLocation.milePost > 0) {
				AMapLocation location = LocationComputeUtil.parseLocation(recordLocation.locationStr);
				LatLng milePostPoint = LocationComputeUtil.createLatLng(location);
				String milePost = String.valueOf(Math.round(recordLocation.getMilePost() / 1000));
				addMarker(milePostPoint, milePost, 16, 15, R.color.location_wrapper, R.color.location_inner_circle);
			}
		}
	}

	private void addMarker(LatLng position, String displayStr,int radius, int textSize, int wrapperColor, int circleColor){
		addMarker(position, displayStr, radius, textSize, wrapperColor, circleColor, false);
	}

	private void addMarker(LatLng position, String displayStr,int radius, int textSize, int wrapperColor, int circleColor, boolean showBottomShader){
		View view = View.inflate(RecordShowActivity.this, R.layout.custom_location_view, null);
		RelativeLayout locationContainer = view.findViewById(R.id.locationContainer);
		LocationMarker locationMarker = new LocationMarker(mMapView.getContext(),
				DisplayUtil.dip2px(radius), displayStr, textSize);
		locationMarker.setColors(wrapperColor, circleColor);
		locationMarker.setDrawBottomShader(showBottomShader);

		locationContainer.addView(locationMarker);
		BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromView(view);

		MarkerOptions optionPosition = new MarkerOptions()
				.position(position)
				.icon(markerIcon);
		Marker marker = mAMap.addMarker(optionPosition);

		Animation markerAnimation = new ScaleAnimation(0, 1, 0, 1); //初始化生长效果动画
		markerAnimation.setDuration(1000);  //设置动画时间 单位毫秒
		marker.setAnimation(markerAnimation);

		marker.startAnimation();
	}


	/**
	 * 地图上添加原始轨迹线路及起终点、轨迹动画小人
	 * 
	 * @param startPoint
	 * @param endPoint
	 * @param originList
	 */
	private void addOriginTrace(LatLng startPoint, LatLng endPoint, List<LatLng> originList) {
		mAMap.addPolyline(new PolylineOptions().color(Color.BLACK).addAll(originList));
		addMarker(startPoint, "起", 17, 16, R.color.location_wrapper, R.color.location_inner_circle, true);
		addMarker(endPoint, "终", 17, 16, R.color.location_end_wrapper, R.color.location_end_circle, true);
		try {
			mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(), 50));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMapLoaded() {
		Message msg = handler.obtainMessage();
		msg.what = AMAP_LOADED;
		handler.sendMessage(msg);
	}


	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	public void onDestroy() {
		super.onDestroy();
		if (mThreadPool != null) {
			mThreadPool.shutdownNow();
		}
		mMapView.onDestroy();
		//退出界面的时候停止定位
		if (mLocationClient != null) {
			mLocationClient.stopLocation();
		}
	}
}
