package com.yxc.barchart.map.location;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapLoadedListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.tracereplay.TraceRePlay;
import com.yxc.barchart.map.location.tracereplay.TraceRePlay.TraceRePlayListener;
import com.yxc.barchart.map.location.util.LocationComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordLocation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 实现轨迹回放、纠偏后轨迹回放
 * 
 */
public class RecordShowActivity extends Activity implements
		OnMapLoadedListener, OnClickListener {
	private final static int AMAP_LOADED = 2;

	private ToggleButton mDisplaybtn;

	private MapView mMapView;
	private AMap mAMap;
	private Marker mOriginStartMarker, mOriginEndMarker, mOriginRoleMarker;
	private int mRecordItemId;
	private int recordType;
	private List<LatLng> mOriginLatLngList;
	private boolean mOriginChecked = true;
	private ExecutorService mThreadPool;
	private TraceRePlay mRePlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_item);
		mMapView = findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);// 此方法必须重写
		mDisplaybtn = findViewById(R.id.displaybtn);
		mDisplaybtn.setOnClickListener(this);
		Intent recordIntent = getIntent();
		int threadPoolSize = Runtime.getRuntime().availableProcessors() * 2 + 3;
		mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
		if (recordIntent != null) {
			mRecordItemId = recordIntent.getIntExtra(RecordActivity.RECORD_ID,
					-1);
			recordType = recordIntent.getIntExtra(LocationConstants.KEY_RECORD_TYPE, -1);
		}
		initMap();
	}

	private void initMap() {
		if (mAMap == null) {
			mAMap = mMapView.getMap();
			mAMap.setOnMapLoadedListener(this);
		}
	}

	
	private void startMove() {
		if(mRePlay !=null){
			mRePlay.stopTrace();
		}
		if (mOriginChecked) {
			mRePlay = rePlayTrace(mOriginLatLngList, mOriginRoleMarker);
		}
	}

	/**
	 * 轨迹回放方法
	 */
	private TraceRePlay rePlayTrace(List<LatLng> list, final Marker updateMarker) {
		TraceRePlay replay = new TraceRePlay(list, 100,
				new TraceRePlayListener() {

					@Override
					public void onTraceUpdating(LatLng latLng) {
						if (updateMarker != null) {
							updateMarker.setPosition(latLng); // 更新小人实现轨迹回放
						}
					}

					@Override
					public void onTraceUpdateFinish() {
						mDisplaybtn.setChecked(false);
						mDisplaybtn.setClickable(true);
					}
				});
		mThreadPool.execute(replay);
		return replay;
	}


	/**
	 * 将原始轨迹小人设置到起点
	 */
	private void resetOriginRole() {
		if (mOriginLatLngList == null) {
			return;
		}
		LatLng startLatLng = mOriginLatLngList.get(0);
		if (mOriginRoleMarker != null) {
			mOriginRoleMarker.setPosition(startLatLng);
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

	public void onDestroy() {
		super.onDestroy();
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
			LatLng startLatLng = new LatLng(startLoc.getLatitude(),
					startLoc.getLongitude());
			LatLng endLatLng = new LatLng(endLoc.getLatitude(),
					endLoc.getLongitude());
			mOriginLatLngList = LocationComputeUtil.parseLatLngList(recordList);
			addOriginTrace(startLatLng, endLatLng, mOriginLatLngList);
			addMilePost(recordLocationList);
		} else {

		}
	}


	private void addMilePost(List<RecordLocation> recordLocationList){
		for (int i = 0; i < recordLocationList.size() ; i++) {
			RecordLocation recordLocation = recordLocationList.get(i);
			if (recordLocation.milePost > 0){
				AMapLocation location = LocationComputeUtil.parseLocation(recordLocation.locationStr);
				LatLng milePostPoint = new LatLng(location.getLatitude(), location.getLongitude());
				mAMap.addMarker(new MarkerOptions().position(milePostPoint)
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_mile_post_24dp)));
			}
		}
	}


	/**
	 * 地图上添加原始轨迹线路及起终点、轨迹动画小人
	 * 
	 * @param startPoint
	 * @param endPoint
	 * @param originList
	 */
	private void addOriginTrace(LatLng startPoint, LatLng endPoint, List<LatLng> originList) {
		mAMap.addPolyline(new PolylineOptions().color(Color.BLUE).addAll(originList));
		mAMap.addMarker(new MarkerOptions().position(startPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
		mAMap.addMarker(new MarkerOptions().position(endPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.end)));
		try {
			mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(getBounds(), 50));
		} catch (Exception e) {
			e.printStackTrace();
		}
		mAMap.addMarker(new MarkerOptions().position(startPoint).icon(BitmapDescriptorFactory.fromResource(R.drawable.walk)));
	}

	@Override
	public void onMapLoaded() {
		Message msg = handler.obtainMessage();
		msg.what = AMAP_LOADED;
		handler.sendMessage(msg);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.displaybtn:
			if (mDisplaybtn.isChecked()) {
				startMove();
				mDisplaybtn.setClickable(false);
			}
			break;
		}
	}
}
