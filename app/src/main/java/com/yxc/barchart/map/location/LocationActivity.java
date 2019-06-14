package com.yxc.barchart.map.location;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.core.app.ActivityCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.yxc.barchart.R;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.event.LocationEvent;
import com.yxc.barchart.map.location.service.LocalLocationService;
import com.yxc.barchart.map.location.service.LocationService;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.location.util.Utils;
import com.yxc.barchart.map.model.PathRecord;
import com.yxc.barchart.map.model.Record;
import com.yxc.commonlib.util.TimeDateUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;


public class LocationActivity extends Activity {
    private MapView mMapView;
    private AMap mAMap;
    private PolylineOptions mPolyoptions, tracePolytion;
    private Polyline mpolyline;
    private PathRecord record;
    private long mStartTime;
    private long mEndTime;
    private ToggleButton btn;

    private LinearLayout rightBtn;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private int recordType = LocationConstants.SPORT_TYPE_RUNNING;
    private String recordId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_location_map_activity);

        mMapView = findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        rightBtn = findViewById(R.id.title_lly_right);

        init();
        initPolyline();
        permissionApply();

        Intent intent = getIntent();
        recordType = intent.getIntExtra("recordType", LocationConstants.SPORT_TYPE_RUNNING);
        recordId = getRecordId(recordType);//获取recordId
    }

    private String getRecordId(int recordType) {
        Record record = LocationDBHelper.getLastRecord(recordType);
        recordId = (record == null) ? "0" : Integer.toString(record.id + 1);
        Log.d("LocationService", "recordId = " + recordId);
        return recordId;
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
     *
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }
        btn = findViewById(R.id.locationbtn);

        OnClickListener clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn.isChecked()) {
                    rightBtn.setVisibility(View.GONE);
                    mAMap.clear(true);
                    if (record != null) {
                        record = null;
                    }
                    record = new PathRecord();
                    mStartTime = System.currentTimeMillis();
                    record.setDate(TimeDateUtil.getDateStrMinSecond(mStartTime));
                    startLocationService();
                } else {
                    rightBtn.setVisibility(View.VISIBLE);
                    mEndTime = System.currentTimeMillis();
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

            Record record = Record.createRecord(recordType, Float.toString(distance),
                    duration, averageSpeed, pathLineStr, startPoint, endPoint, time);

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
        super.onDestroy();
        mMapView.onDestroy();
    }

    /**
     * 开始定位服务
     */
    private void startLocationService() {
        Intent intentLocalService = new Intent(this, LocalLocationService.class);
        intentLocalService.putExtra(LocationConstants.KEY_RECORD_TYPE, recordType);
        intentLocalService.putExtra(LocationConstants.KEY_RECORD_ID, recordId);
        startService(intentLocalService);

        Intent intent = new Intent(this, LocationService.class);
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
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null && amapLocation.getErrorCode() == 0) {
            if (lastLocation != null) {
                long timestamp = lastLocation.getTime();
                //从数据库里拿点
                List<AMapLocation> locationList = LocationDBHelper.getLateLocationList(recordId, timestamp);
                record.addPointList(locationList);
                for (int i = 0; i < locationList.size(); i++) {
                    AMapLocation aMapLocation = locationList.get(i);
                    LatLng myLocation = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
                    mPolyoptions.add(myLocation);
                }
                redRawLine();
            } else {
                LatLng myLocation = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                mAMap.moveCamera(CameraUpdateFactory.changeLatLng(myLocation));
                if (btn.isChecked()) {
                    Log.d("Location", "record " + myLocation);
                    record.addPoint(amapLocation);
                    mPolyoptions.add(myLocation);
                    redRawLine();
                }
            }
            lastLocation = amapLocation;
        } else {
            String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                    + amapLocation.getErrorInfo();
            Log.e("AmapErr", errText);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLocationSaved(LocationEvent locationEvent){
        if (locationEvent.mapLocation != null){
            onLocationChanged(locationEvent.mapLocation);
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
    }

    public void record(View view) {
        Intent intent = new Intent(LocationActivity.this, RecordActivity.class);
        intent.putExtra(LocationConstants.KEY_RECORD_TYPE, recordType);
        startActivity(intent);
        finish();
    }
}
