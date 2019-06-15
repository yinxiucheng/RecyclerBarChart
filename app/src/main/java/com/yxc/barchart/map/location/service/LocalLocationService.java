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
import com.yxc.barchart.map.location.event.LocationEvent;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.location.util.Utils;
import com.yxc.barchart.map.model.RecordLocation;

import org.greenrobot.eventbus.EventBus;

/**
 * 类说明：后台服务定位
 * 1. 只有在由息屏造成的网络断开造成的定位失败时才点亮屏幕
 * 2. 利用notification机制增加进程优先级
 */
public class LocalLocationService extends Service {

    private Utils.CloseServiceReceiver mCloseReceiver;

    private long mMilePost = LocationConstants.MILE_POST_ONE_KILOMETRE;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d("LocationService", "service start!");
        recordType = intent.getIntExtra(LocationConstants.KEY_RECORD_TYPE, LocationConstants.SPORT_TYPE_RUNNING);
        recordId = intent.getStringExtra(LocationConstants.KEY_RECORD_ID);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LocationConstants.ACTION_LOCATION_BACKGROUND);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);

        mCloseReceiver = new Utils.CloseServiceReceiver(this);
        registerReceiver(mCloseReceiver, Utils.getCloseServiceFilter());

        mMilePost = LocationConstants.MILE_POST_ONE_KILOMETRE;
        return START_STICKY;
    }

    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(LocationConstants.ACTION_LOCATION_BACKGROUND)) {
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
        mMilePost = LocationConstants.MILE_POST_ONE_KILOMETRE;
        lastSaveLocation = null;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private AMapLocation lastSaveLocation;//用于计算speed，排除异常点。
    private RecordLocation lastRecordLocation;
    private int recordType = LocationConstants.SPORT_TYPE_RUNNING;
    private String recordId;

    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation.getLatitude() == 0f || aMapLocation.getLongitude() <= 0.001f) {
            return;
        }
        //插入数据库
        double itemDistance = ComputeUtil.getDistance(aMapLocation, lastSaveLocation);
        if (lastSaveLocation == null && aMapLocation.getLatitude() > 0f) {
            //record的第一个埋点，插入数据库
            Log.d("LocationService", "第一个点。。。");
            Toast.makeText(LocalLocationService.this, "Service first insert Point", Toast.LENGTH_SHORT).show();
            LocationDBHelper.deleteRecordLocationList(recordType, recordId);
            String locationStr = ComputeUtil.amapLocationToString(aMapLocation);
            double distance = 0;
            double milePost = 0;
            RecordLocation recordLocation = RecordLocation.createLocation(aMapLocation, recordId, recordType, itemDistance, distance, locationStr, milePost);
            LocationDBHelper.insertRecordLocation(recordLocation);

            Log.d("LocationService", "first insert recordLocation:" + recordLocation.toString());
            sendEventbus(aMapLocation, recordLocation);
            lastSaveLocation = aMapLocation;
            lastRecordLocation = recordLocation;
        } else if (itemDistance > 1.0f) {
            Toast.makeText(LocalLocationService.this, "save Point:" + aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            String locationStr = ComputeUtil.amapLocationToString(aMapLocation);
            if (lastRecordLocation != null) {
                double distance = lastRecordLocation.distance + itemDistance;
                double milePost = 0;
                if (distance >= mMilePost){
                    milePost = mMilePost;
                    mMilePost += LocationConstants.MILE_POST_ONE_KILOMETRE;
                }
                RecordLocation recordLocation = RecordLocation.createLocation(aMapLocation, recordId, recordType,
                        itemDistance, distance, locationStr, milePost);
                long time = (aMapLocation.getTime() - lastRecordLocation.endTime)/1000;
                float speed = (float) (itemDistance * 1.0f/time);
                recordLocation.speed = speed;
                lastRecordLocation = recordLocation;
                LocationDBHelper.insertRecordLocation(recordLocation);

                //修改lastSaveLocation的endTime， duration。
//                long lastSaveLocationEndTime = aMapLocation.getTime();
//                long lastSaveLocationDuration = aMapLocation.getTime() - lastSaveLocation.getTime();
//                LocationDBHelper.updateRecordLocation(lastSaveLocation.getTime(), lastSaveLocationEndTime, lastSaveLocationDuration);

                sendEventbus(aMapLocation, recordLocation);
                Log.d("LocationService", "insert recordLocation:" + recordLocation.toString());
            }
            lastSaveLocation = aMapLocation;
        } else {//可能在原地打点，不存入新数据，update endTime。
            Toast.makeText(LocalLocationService.this, "update Point:" + aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show();
            long timestamp = lastSaveLocation.getTime();
            long endTime = System.currentTimeMillis();//todo 需要考虑定位时间跟系统时间的差值。
            long duration = endTime - timestamp;
            LocationDBHelper.updateRecordLocation(timestamp, endTime, duration);
        }
    }

    private void sendEventbus(AMapLocation aMapLocation, RecordLocation recordLocation) {
        //改成发送eventBus
        EventBus.getDefault().post(new LocationEvent(aMapLocation, recordLocation));
    }

}
