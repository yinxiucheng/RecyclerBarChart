package com.yxc.barchart.map.location.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

import java.util.List;

/**
 * @author yxc
 * @date 2019-06-07
 */
public class LocationUtil {

    public static String amapLocationToString(AMapLocation location) {
        StringBuffer locString = new StringBuffer();
        locString.append(location.getLatitude()).append(",");
        locString.append(location.getLongitude()).append(",");
        locString.append(location.getProvider()).append(",");
        locString.append(location.getTime()).append(",");
        locString.append(location.getSpeed()).append(",");
        locString.append(location.getBearing());
        return locString.toString();
    }

    public static String getPathLineString(List<AMapLocation> list) {
        if (list == null || list.size() == 0) {
            return "";
        }
        StringBuffer pathline = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation location = list.get(i);
            String locString = amapLocationToString(location);
            pathline.append(locString).append(";");
        }
        String pathLineString = pathline.toString();
        pathLineString = pathLineString.substring(0,
                pathLineString.length() - 1);
        return pathLineString;
    }


    public static float getDistance(List<AMapLocation> list) {
        float distance = 0;
        if (list == null || list.size() == 0) {
            return distance;
        }
        for (int i = 0; i < list.size() - 1; i++) {//取到最后一个点
            double betweenDis = getDistance(list.get(i + 1), list.get(i));
            distance = (float) (distance + betweenDis);
        }
        return distance;
    }

    //获取两点的距离
    public static double getDistance(AMapLocation location, AMapLocation lastLocation){
        if (lastLocation == null){
            return 0;
        }
        LatLng firstLatLng = new LatLng(location.getLatitude(),
                location.getLongitude());
        LatLng secondLatLng = new LatLng(lastLocation.getLatitude(),
                lastLocation.getLongitude());
        double betweenDis = AMapUtils.calculateLineDistance(firstLatLng,
                secondLatLng);
        return betweenDis;
    }
}
