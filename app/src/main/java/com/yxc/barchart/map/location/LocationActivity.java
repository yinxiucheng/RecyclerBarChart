package com.yxc.barchart.map.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.trace.LBSTraceClient;
import com.amap.api.trace.TraceListener;
import com.amap.api.trace.TraceLocation;
import com.amap.api.trace.TraceOverlay;
import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.service.LocationService;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.location.util.Utils;
import com.yxc.barchart.map.model.Record;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LocationActivity extends Activity implements TraceListener {
    private final static int CALLTRACE = 0;
    private MapView mMapView;
    private AMap mAMap;
    private PolylineOptions mPolyoptions, tracePolytion;
    private Polyline mpolyline;
    private PathRecord record;
    private long mStartTime;
    private long mEndTime;
    private ToggleButton btn;
    private List<TraceLocation> mTracelocationlist = new ArrayList<TraceLocation>();
    private List<TraceOverlay> mOverlayList = new ArrayList<TraceOverlay>();
    private List<AMapLocation> recordList = new ArrayList<AMapLocation>();
    private int tracesize = 30;
    private int mDistance = 0;
    private TraceOverlay mTraceoverlay;
    private TextView mResultShow;
    private Marker mlocMarker;

    public static final String RECEIVER_ACTION = "location_in_background";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private int recordType = LocationConstants.SPORT_TYPE_RUNNING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_location_map_activity);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        initPolyline();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);

        permissionApply();
    }


    private void permissionApply() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }
        btn = (ToggleButton) findViewById(R.id.locationbtn);
        mResultShow = (TextView) findViewById(R.id.show_all_dis);
        mTraceoverlay = new TraceOverlay(mAMap);

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.isChecked()) {
                    mAMap.clear(true);
                    if (record != null) {
                        record = null;
                    }
                    record = new PathRecord();
                    mStartTime = System.currentTimeMillis();
                    record.setDate(getDate(mStartTime));
                    mResultShow.setText("总距离");
                    startLocationService();
                } else {
                    mEndTime = System.currentTimeMillis();
                    mOverlayList.add(mTraceoverlay);
                    DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    mResultShow.setText(decimalFormat.format(getTotalDistance() / 1000d) + "KM");
                    LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
                    mTraceClient.queryProcessedTrace(2, ComputeUtil.parseTraceLocationList(record.getPathLine()),
                            LBSTraceClient.TYPE_AMAP, LocationActivity.this);

                    if (!TextUtils.isEmpty(recordId)){
                        List<AMapLocation> locationList = LocationDBHelper.getLocationList(recordId);
                        saveRecord(locationList, record.getDate());
                    }
                    stopLocationService();
                }
            }
        };
        btn.setOnClickListener(clickListener);
    }

    protected void saveRecord(List<AMapLocation> list, String time) {
        if (list != null && list.size() > 0) {
            String duration = getDuration();
            float distance = ComputeUtil.getDistance(list);
            String averageSpeed = getAverage(distance);
            String pathLineStr = ComputeUtil.getPathLineString(list);
            AMapLocation firstLocation = list.get(0);
            AMapLocation lastLocation = list.get(list.size() - 1);
            String startPoint = ComputeUtil.amapLocationToString(firstLocation);
            String endPoint = ComputeUtil.amapLocationToString(lastLocation);

            Record record = Record.createRecord(recordType, Float.toString(distance), duration, averageSpeed, pathLineStr, startPoint, endPoint, time);
            LocationDBHelper.insertRecord(record);

        } else {
            Toast.makeText(LocationActivity.this, "没有记录到路径", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private String getDuration() {
        return String.valueOf((mEndTime - mStartTime) / 1000f);
    }

    private String getAverage(float distance) {
        return String.valueOf(distance / (float) (mEndTime - mStartTime));
    }

    private void initPolyline() {
        mPolyoptions = new PolylineOptions();
        mPolyoptions.width(10f);
        mPolyoptions.color(Color.GRAY);
        mPolyoptions.useGradient(true);
        tracePolytion = new PolylineOptions();
        tracePolytion.width(40);
        tracePolytion.setCustomTexture(BitmapDescriptorFactory.fromResource(R.drawable.grasp_trace_line));
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        if (locationChangeBroadcastReceiver != null)
            unregisterReceiver(locationChangeBroadcastReceiver);
        super.onDestroy();
        mMapView.onDestroy();
    }

    private String recordId = "";

    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVER_ACTION)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    String locationResult = bundle.getString("result");
                    if (null != locationResult && !locationResult.trim().equals("")) {
                        AMapLocation aMLocation = bundle.getParcelable("location");
                        recordId = bundle.getString("recordId");
                        onLocationChanged(aMLocation, recordId);
                    }
                }
            }
        }
    };


    /**
     * 开始定位服务
     */
    private void startLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("recordType", recordType);
        getApplicationContext().startService(intent);
    }

    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
    private void stopLocationService() {
        sendBroadcast(Utils.getCloseBrodecastIntent());
    }


    /**
     * 定位结果回调
     *
     * @param amapLocation 位置信息类
     */
    public void onLocationChanged(AMapLocation amapLocation, String recordId) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
//				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
            if (lastLocation != null) {
                Toast.makeText(LocationActivity.this, "receive Broadcast!!", Toast.LENGTH_SHORT).show();
                long timestamp = lastLocation.getTime();
                //从数据库里拿点
                List<AMapLocation> locationList = LocationDBHelper.getLateLocationList(recordId, timestamp);
                record.addPointList(locationList);
                for (int i = 0; i < locationList.size(); i++) {
                    AMapLocation aMapLocation = locationList.get(i);
                    LatLng myLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    mPolyoptions.add(myLocation);
                }
                mTracelocationlist.addAll(ComputeUtil.parseTraceLocationList(locationList));
                if (mTracelocationlist.size() > tracesize - 1) {
                    trace();
                }
                redRawLine();
            } else {
                LatLng myLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(myLocation));
                if (btn.isChecked()) {
                    Log.d("Location", "record " + myLocation);
                    record.addPoint(amapLocation);
                    mPolyoptions.add(myLocation);
                    mTracelocationlist.add(ComputeUtil.parseTraceLocation(amapLocation));
                    redRawLine();
                    if (mTracelocationlist.size() > tracesize - 1) {
                        trace();
                    }
                }
            }
            lastLocation = amapLocation;
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                    + amapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }


    private AMapLocation lastLocation;

    /**
     * 实时轨迹画线
     */
    private void redRawLine() {
        if (mPolyoptions.getPoints().size() > 1) {
            if (mpolyline != null) {
                mpolyline.setPoints(mPolyoptions.getPoints());
            } else {
                mpolyline = mAMap.addPolyline(mPolyoptions);
            }
        }
//		if (mpolyline != null) {
//			mpolyline.remove();
//		}
//		mPolyoptions.visible(true);
//		mpolyline = mAMap.addPolyline(mPolyoptions);
//			PolylineOptions newpoly = new PolylineOptions();
//			mpolyline = mAMap.addPolyline(newpoly.addAll(mPolyoptions.getPoints()));
//		}
    }

    @SuppressLint("SimpleDateFormat")
    private String getDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd  HH:mm:ss ");
        Date curDate = new Date(time);
        String date = formatter.format(curDate);
        return date;
    }

    public void record(View view) {
        Intent intent = new Intent(LocationActivity.this, RecordActivity.class);
        intent.putExtra(LocationConstants.KEY_RECORD_TYPE, recordType);
        startActivity(intent);
    }

    private void trace() {
        List<TraceLocation> locationList = new ArrayList<>(mTracelocationlist);
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
        mTraceClient.queryProcessedTrace(1, locationList, LBSTraceClient.TYPE_AMAP, this);
        TraceLocation lastlocation = mTracelocationlist.get(mTracelocationlist.size() - 1);
        mTracelocationlist.clear();
        mTracelocationlist.add(lastlocation);
    }

    /**
     * 轨迹纠偏失败回调。
     *
     * @param i
     * @param s
     */
    @Override
    public void onRequestFailed(int i, String s) {
        mOverlayList.add(mTraceoverlay);
        mTraceoverlay = new TraceOverlay(mAMap);
    }

    @Override
    public void onTraceProcessing(int i, int i1, List<LatLng> list) {

    }

    /**
     * 轨迹纠偏成功回调。
     *
     * @param lineID      纠偏的线路ID
     * @param linepoints  纠偏结果
     * @param distance    总距离
     * @param waitingtime 等待时间
     */
    @Override
    public void onFinished(int lineID, List<LatLng> linepoints, int distance, int waitingtime) {
        if (lineID == 1) {
            if (linepoints != null && linepoints.size() > 0) {
                mTraceoverlay.add(linepoints);
                mDistance += distance;
                mTraceoverlay.setDistance(mTraceoverlay.getDistance() + distance);
                if (mlocMarker == null) {
                    mlocMarker = mAMap.addMarker(new MarkerOptions().position(linepoints.get(linepoints.size() - 1))
                            .icon(BitmapDescriptorFactory
                                    .fromResource(R.drawable.point))
                            .title("距离：" + mDistance + "米"));
                    mlocMarker.showInfoWindow();
                } else {
                    mlocMarker.setTitle("距离：" + mDistance + "米");
                    Toast.makeText(LocationActivity.this, "距离" + mDistance, Toast.LENGTH_SHORT).show();
                    mlocMarker.setPosition(linepoints.get(linepoints.size() - 1));
                    mlocMarker.showInfoWindow();
                }
            }
        } else if (lineID == 2) {
            if (linepoints != null && linepoints.size() > 0) {
                mAMap.addPolyline(new PolylineOptions()
                        .color(Color.RED)
                        .width(40).addAll(linepoints));
            }
        }
    }

    /**
     * 最后获取总距离
     *
     * @return
     */
    private int getTotalDistance() {
        int distance = 0;
        for (TraceOverlay to : mOverlayList) {
            distance = distance + to.getDistance();
        }
        return distance;
    }
}
