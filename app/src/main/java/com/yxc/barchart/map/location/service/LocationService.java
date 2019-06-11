package com.yxc.barchart.map.location.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yxc.barchart.map.location.LocationActivity;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.location.util.IWifiAutoCloseDelegate;
import com.yxc.barchart.map.location.util.NetUtil;
import com.yxc.barchart.map.location.util.PowerManagerUtil;
import com.yxc.barchart.map.location.util.Utils;
import com.yxc.barchart.map.location.util.WifiAutoCloseDelegate;
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
public class LocationService extends NotiService {

    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;

    private int locationCount;

    /**
     * 处理息屏关掉wifi的delegate类
     */
    private IWifiAutoCloseDelegate mWifiAutoCloseDelegate = new WifiAutoCloseDelegate();

    /**
     * 记录是否需要对息屏关掉wifi的情况进行处理
     */
    private boolean mIsWifiCloseable = false;

    private long intervalTime = LocationConstants.DEFAULT_INTERVAL_TIME;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        applyNotiKeepMech(); //开启利用notification提高进程优先级的机制
        Log.d("LocationService", "service start!");

        recordType = intent.getIntExtra("recordType", LocationConstants.SPORT_TYPE_RUNNING);
        getRecordId(recordType);//获取recordId

        if (mWifiAutoCloseDelegate.isUseful(getApplicationContext())) {
            mIsWifiCloseable = true;
            mWifiAutoCloseDelegate.initOnServiceStarted(getApplicationContext());
        }
        startLocation();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        unApplyNotiKeepMech();
        stopLocation();

        super.onDestroy();
    }

    /**
     * 启动定位
     */
    void startLocation() {
        Log.d("LocationService", "start location!!");
        stopLocation();

        if (null == mLocationClient) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }

        mLocationOption = new AMapLocationClientOption();
        // 使用连续
        mLocationOption.setOnceLocation(false);
        mLocationOption.setLocationCacheEnable(false);
        // 每10秒定位一次
        mLocationOption.setInterval(LocationConstants.DEFAULT_INTERVAL_TIME);
        // 地址信息
        mLocationOption.setNeedAddress(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
        }
    }

    private AMapLocation lastLocation;//用于计算speed，排除异常点。
    private AMapLocation lastSaveLocation;

    private int recordType = LocationConstants.SPORT_TYPE_RUNNING;
    private String recordId;

    private String getRecordId(int recordType) {
        Record record = LocationDBHelper.getLastRecord(recordType);
        recordId = record == null ? "0" : Integer.toString(record.id + 1);
        Log.d("LocationService", "recordId = " + recordId);
        return recordId;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //插入数据库
            double itemDistance = ComputeUtil.getDistance(aMapLocation, lastLocation);
            if (lastLocation == null) {//record的第一个埋点，插入数据库
                Log.d("LocationService", "第一个点。。。");
                String locationStr = ComputeUtil.amapLocationToString(aMapLocation);
                double distance = 0;
                RecordLocation recordLocation = RecordLocation.createLocation(aMapLocation, recordId, recordType,
                        itemDistance, distance, locationStr);
                LocationDBHelper.insertRecordLocation(recordLocation);
                Log.d("LocationService", "first insert recordLocation:" + recordLocation.toString());
                sendLocationBroadcast(aMapLocation);
                lastSaveLocation = aMapLocation;
            } else if (itemDistance > aMapLocation.getAccuracy() / 4.0f &&
                    ComputeUtil.isExceptPoint(lastSaveLocation, aMapLocation, recordType, intervalTime)) {
                //条件1 itemDistance < aMapLocation.getAccuracy() / 4.0f 视为原点
                String locationStr = ComputeUtil.amapLocationToString(aMapLocation);
                RecordLocation lastRecordLocation = LocationDBHelper.getLastItem(recordId);
                if (lastRecordLocation != null) {
                    double distance = lastRecordLocation.distance + itemDistance;
                    RecordLocation recordLocation = RecordLocation.createLocation(aMapLocation, recordId, recordType,
                            itemDistance, distance, locationStr);
                    LocationDBHelper.insertRecordLocation(recordLocation);
                    Log.d("LocationService", "insert recordLocation:" + recordLocation.toString());
                    resetIntervalTimes(recordLocation.duration);
                }
                sendLocationBroadcast(aMapLocation);
                lastSaveLocation = aMapLocation;
            } else {//可能在原地打点，不存入新数据，update endTime。
                long timestamp = lastSaveLocation.getTime();
                RecordLocation recordLocation = new RecordLocation();
                Log.d("LocationService", "update recordLocation1:" + aMapLocation.getTime());
                long endTime = System.currentTimeMillis();//todo 需要考虑定位时间跟系统时间的差值。
                recordLocation.timestamp = timestamp;
                recordLocation.setEndTime(endTime);
                long duration = endTime - recordLocation.timestamp;
                recordLocation.setDuration(duration);
                Log.d("LocationService", "update recordLocation2:" + recordLocation.toString());
                LocationDBHelper.updateRecordLocation(recordLocation);//这里调用的是update.
                resetIntervalTimes(duration);
            }
            lastLocation = aMapLocation;
            //发送结果的通知
            Log.d("LocationService", "onLocationChanged");
            if (!mIsWifiCloseable) {
                return;
            }

            if (aMapLocation.getErrorCode() == AMapLocation.LOCATION_SUCCESS) {
                mWifiAutoCloseDelegate.onLocateSuccess(getApplicationContext(),
                        PowerManagerUtil.getInstance().isScreenOn(getApplicationContext()),
                        NetUtil.getInstance().isMobileAva(getApplicationContext()));
            } else {
                mWifiAutoCloseDelegate.onLocateFail(getApplicationContext(),
                        aMapLocation.getErrorCode(), PowerManagerUtil.getInstance().isScreenOn(getApplicationContext()),
                        NetUtil.getInstance().isWifiCon(getApplicationContext()));
            }
        }

        private void resetIntervalTimes(long duration){
            int intervalTimes = ComputeUtil.computeIntervalTimes(duration);
            intervalTime = intervalTimes * LocationConstants.DEFAULT_INTERVAL_TIME;
            mLocationOption.setInterval(intervalTime);
            mLocationClient.setLocationOption(mLocationOption);
        }

        private void sendLocationBroadcast(AMapLocation aMapLocation) {
            //记录信息并发送广播
            locationCount++;
            long callBackTime = System.currentTimeMillis();
            StringBuffer sb = new StringBuffer();
            sb.append("定位完成 第" + locationCount + "次\n");
            sb.append("回调时间: " + Utils.formatUTC(callBackTime, null) + "\n");
            if (null == aMapLocation) {
                sb.append("定位失败：location is null!!!!!!!");
                Log.d("LocationService", "Location failed!");
            } else {
                sb.append(Utils.getLocationStr(aMapLocation));
                Log.d("LocationService", "sendLocationBroadcast");
            }
            Intent intent = new Intent(LocationActivity.RECEIVER_ACTION);
            Bundle bundle = new Bundle();
            bundle.putString("result", sb.toString());
            bundle.putString("recordId", recordId);
            bundle.putParcelable("location", aMapLocation);
            intent.putExtras(bundle);
            //发送广播
            sendBroadcast(intent);
        }

    };

}
