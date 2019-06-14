package com.yxc.barchart.map.location.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.location.util.Utils;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordLocation;

/**
 * 包名： com.amap.locationservicedemo
 * <p>
 * 创建时间：2016/10/27
 * 项目名称：LocationServiceDemo
 *
 * @author guibao.ggb
 * @email guibao.ggb@alibaba-inc.com
 * <p>
 * 类说明：后台服务定位
 *
 * <p>
 * modeified by liangchao , on 2017/01/17
 * update:
 * 1. 只有在由息屏造成的网络断开造成的定位失败时才点亮屏幕
 * 2. 利用notification机制增加进程优先级
 * </p>
 */
public class LocalLocationService extends Service {
    private long intervalTime = LocationConstants.DEFAULT_INTERVAL_TIME;
    public static final String RECEIVER_ACTION = "location_in_background";

    private Utils.CloseServiceReceiver mCloseReceiver;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("LocationService", "service start!");

        recordType = intent.getIntExtra("recordType", LocationConstants.SPORT_TYPE_RUNNING);
        getRecordId(recordType);//获取recordId

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);

        mCloseReceiver = new Utils.CloseServiceReceiver(this);
        registerReceiver(mCloseReceiver, Utils.getCloseServiceFilter());
        return START_STICKY;
    }

    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVER_ACTION)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    AMapLocation aMLocation = bundle.getParcelable("location");
                    onLocationChanged(aMLocation);
                }
            }
        }
    };


    @Override
    public void onDestroy() {
        if (mCloseReceiver != null) {
            unregisterReceiver(mCloseReceiver);
            mCloseReceiver = null;
        }

        if (locationChangeBroadcastReceiver != null){
            unregisterReceiver(locationChangeBroadcastReceiver);
        }

        lastSaveLocation = null;
        lastLocation = null;
        lastRecordLocation = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private AMapLocation lastLocation;//用于计算speed，排除异常点。
    private AMapLocation lastSaveLocation;//用于计算speed，排除异常点。
    private RecordLocation lastRecordLocation;

    private int recordType = LocationConstants.SPORT_TYPE_RUNNING;
    private String recordId;

    private String getRecordId(int recordType) {
        Record record = LocationDBHelper.getLastRecord(recordType);
        recordId = (record == null) ? "0" : Integer.toString(record.id + 1);
        Log.d("LocationService", "recordId = " + recordId);
        return recordId;
    }

    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation.getLatitude() == 0f || aMapLocation.getLongitude() <= 0.001f) {
            return;
        }
        //插入数据库
        double itemDistance = ComputeUtil.getDistance(aMapLocation, lastSaveLocation);
        if (lastSaveLocation == null && aMapLocation.getLatitude() > 0f) {//record的第一个埋点，插入数据库
            Log.d("LocationService", "第一个点。。。");
            Toast.makeText(LocalLocationService.this, "Service first insert Point", Toast.LENGTH_SHORT).show();
            LocationDBHelper.deleteRecordLocationList(recordType, recordId);
            String locationStr = ComputeUtil.amapLocationToString(aMapLocation);
            double distance = 0;
            RecordLocation recordLocation = RecordLocation.createLocation(aMapLocation, recordId, recordType, itemDistance, distance, locationStr);
            LocationDBHelper.insertRecordLocation(recordLocation);
            Log.d("LocationService", "first insert recordLocation:" + recordLocation.toString());
            sendLocationBroadcast(aMapLocation);
            lastSaveLocation = aMapLocation;
            lastRecordLocation = recordLocation;
        } else if (itemDistance > 1.0f) {
            //条件1 itemDistance < aMapLocation.getAccuracy() / 4.0f 视为原点
            Toast.makeText(LocalLocationService.this, "save Point:" + aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            String locationStr = ComputeUtil.amapLocationToString(aMapLocation);
            RecordLocation lastDBLocation = LocationDBHelper.getLastItem(recordId);
            if (lastDBLocation != null) {
                double distance = lastDBLocation.distance + itemDistance;
                RecordLocation recordLocation = RecordLocation.createLocation(aMapLocation, recordId, recordType,
                        itemDistance, distance, locationStr);
                LocationDBHelper.insertRecordLocation(recordLocation);
                Log.d("LocationService", "insert recordLocation:" + recordLocation.toString());
                resetIntervalTimes(recordLocation.duration);
                lastRecordLocation = recordLocation;
            }
            sendLocationBroadcast(aMapLocation);
            lastSaveLocation = aMapLocation;
        } else {//可能在原地打点，不存入新数据，update endTime。
            Toast.makeText(LocalLocationService.this, "update Point:" + aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            long timestamp = lastSaveLocation.getTime();
            long endTime = System.currentTimeMillis();//todo 需要考虑定位时间跟系统时间的差值。
            long duration = endTime - timestamp;
            LocationDBHelper.updateRecordLocation(timestamp, endTime, duration);
            resetIntervalTimes(duration);
        }
        lastLocation = aMapLocation;
    }

    private void resetIntervalTimes(long duration) {
//            int intervalTimes = ComputeUtil.computeIntervalTimes(duration);
//            intervalTime = intervalTimes * LocationConstants.DEFAULT_INTERVAL_TIME;
//            mLocationOption.setInterval(intervalTime);
//            mLocationClient.setLocationOption(mLocationOption);
    }

    private void sendLocationBroadcast(AMapLocation aMapLocation) {
        //改成发送eventBus

    }


}
