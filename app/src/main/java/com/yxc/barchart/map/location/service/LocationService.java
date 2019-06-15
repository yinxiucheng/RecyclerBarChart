package com.yxc.barchart.map.location.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.location.util.IWifiAutoCloseDelegate;
import com.yxc.barchart.map.location.util.LocationConstants;
import com.yxc.barchart.map.location.util.NetUtil;
import com.yxc.barchart.map.location.util.PowerManagerUtil;
import com.yxc.barchart.map.location.util.Utils;
import com.yxc.barchart.map.location.util.WifiAutoCloseDelegate;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        applyNotiKeepMech(); //开启利用notification提高进程优先级的机制
        Log.d("LocationService", "service start!");

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

    private AMapLocation lastSaveLocation;

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {

            if (aMapLocation.getLatitude() == 0f || aMapLocation.getLongitude() <= 0.001f) {
                return;
            }
            double itemDistance = ComputeUtil.getDistance(aMapLocation, lastSaveLocation);
            if (lastSaveLocation == null && aMapLocation.getLatitude() > 0f) {
                //record的第一个埋点，插入数据库
                lastSaveLocation = aMapLocation;
            } else if (itemDistance > 1.0f) {
                resetIntervalTimes(0);//新的点
                lastSaveLocation = aMapLocation;
            } else {//可能在原地打点，不存入新数据，update endTime。
                long timestamp = lastSaveLocation.getTime();
                long endTime = System.currentTimeMillis();//todo 需要考虑定位时间跟系统时间的差值。
                long duration = endTime - timestamp;
                resetIntervalTimes(duration);
            }
            sendLocationBroadcast(aMapLocation);
            //发送结果的通知
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

        private long intervalTime = LocationConstants.DEFAULT_INTERVAL_TIME;
        private void resetIntervalTimes(long duration) {
            if (duration >= 60 * 60 * 1000){// 90分钟停止自己的服务, 应该还要关闭守护进程
                onDestroy();
                return;
            }
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
            }
            Intent intent = new Intent(LocationConstants.ACTION_LOCATION_BACKGROUND);
            Bundle bundle = new Bundle();
            bundle.putString("result", sb.toString());
            bundle.putParcelable("location", aMapLocation);
            intent.putExtras(bundle);
            //发送广播
            sendBroadcast(intent);
        }

    };

}
