package com.yxc.barchart.map.location.util;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.trace.TraceLocation;
import com.google.gson.Gson;
import com.yxc.barchart.map.location.database.LocationDBHelper;
import com.yxc.barchart.map.model.RecordCorrect;
import com.yxc.barchart.map.model.RecordLocation;
import com.yxc.barchart.util.Util;

import java.util.ArrayList;
import java.util.List;

public class LocationComputeUtil {
    /**
     * 将AMapLocation List 转为TraceLocation list
     *
     * @param list
     * @return
     */
    public static List<TraceLocation> parseTraceLocationList(
            List<AMapLocation> list) {
        List<TraceLocation> traceList = new ArrayList<TraceLocation>();
        if (list == null) {
            return traceList;
        }
        for (int i = 0; i < list.size(); i++) {
            TraceLocation location = new TraceLocation();
            AMapLocation amapLocation = list.get(i);
            location.setBearing(amapLocation.getBearing());
            location.setLatitude(amapLocation.getLatitude());
            location.setLongitude(amapLocation.getLongitude());
            location.setSpeed(amapLocation.getSpeed());
            location.setTime(amapLocation.getTime());
            traceList.add(location);
        }
        return traceList;
    }

    public static TraceLocation parseTraceLocation(AMapLocation amapLocation) {
        TraceLocation location = new TraceLocation();
        location.setBearing(amapLocation.getBearing());
        location.setLatitude(amapLocation.getLatitude());
        location.setLongitude(amapLocation.getLongitude());
        location.setSpeed(amapLocation.getSpeed());
        location.setTime(amapLocation.getTime());
        return location;
    }

    /**
     * 将AMapLocation List 转为LatLng list
     *
     * @param list
     * @return
     */
    public static List<LatLng> parseLatLngList(List<AMapLocation> list) {
        List<LatLng> traceList = new ArrayList<LatLng>();
        if (list == null) {
            return traceList;
        }
        for (int i = 0; i < list.size(); i++) {
            AMapLocation loc = list.get(i);
            double lat = loc.getLatitude();
            double lng = loc.getLongitude();
            LatLng latlng = new LatLng(lat, lng);
            traceList.add(latlng);
        }
        return traceList;
    }

    public static AMapLocation parseLocation(String latLonStr) {
        if (latLonStr == null || latLonStr.equals("") || latLonStr.equals("[]")) {
            return null;
        }
        String[] loc = latLonStr.split(",");
        AMapLocation location = null;
        if (loc.length == 6) {
            location = new AMapLocation(loc[2]);
            location.setProvider(loc[2]);
            location.setLatitude(Double.parseDouble(loc[0]));
            location.setLongitude(Double.parseDouble(loc[1]));
            location.setTime(Long.parseLong(loc[3]));
            location.setSpeed(Float.parseFloat(loc[4]));
            location.setBearing(Float.parseFloat(loc[5]));
        } else if (loc.length == 2) {
            location = new AMapLocation("gps");
            location.setLatitude(Double.parseDouble(loc[0]));
            location.setLongitude(Double.parseDouble(loc[1]));
        }

        return location;
    }

    public static ArrayList<AMapLocation> parseLocations(String latLonStr) {
        ArrayList<AMapLocation> locations = new ArrayList<AMapLocation>();
        String[] latLonStrs = latLonStr.split(";");
        for (int i = 0; i < latLonStrs.length; i++) {
            AMapLocation location = LocationComputeUtil.parseLocation(latLonStrs[i]);
            if (location != null) {
                locations.add(location);
            }
        }
        return locations;
    }


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

    public static String getPathLineStr(List<RecordLocation> locationList) {
        if (locationList == null || locationList.size() == 0) {
            return "";
        }
        Gson gson = Util.createGson();
        String result = gson.toJson(locationList);
        return result;
    }

    public static String getPathLineCorrectStr(List<RecordCorrect> locationList) {
        if (locationList == null || locationList.size() == 0) {
            return "";
        }
        Gson gson = Util.createGson();
        String result = gson.toJson(locationList);
        return result;
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
    public static double getDistance(AMapLocation location, AMapLocation lastLocation) {
        if (lastLocation == null) {
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


    public static List<LatLng> getLatLngList(List<AMapLocation> list) {
        List<LatLng> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            AMapLocation aMapLocation = list.get(i);
            LatLng latLng = new LatLng(aMapLocation.getAltitude(), aMapLocation.getLongitude());
            resultList.add(latLng);
        }

        return resultList;
    }

    public static boolean isExceptPoint(AMapLocation lastSaveLocation, AMapLocation aMapLocation, int type, long intervalTime) {
        RecordLocation recordLocation = LocationDBHelper.queryRecordLocation(aMapLocation.getTime());
        if (recordLocation != null && recordLocation.duration > 2 * intervalTime) {
            //假如在改点连续打点三次，而且没有偏移处 精度，那么判断它不是异常点。
            return false;
        } else if (recordLocation == null) {
            double distance = LocationComputeUtil.getDistance(lastSaveLocation, aMapLocation);
            RecordLocation savedLocation = LocationDBHelper.queryRecordLocation(lastSaveLocation.getTime());
            long time = (savedLocation.endTime - aMapLocation.getTime()) / 1000L;//单位s
            float speedActual = (float) (distance / time * 1.0f);
            if (speedActual > LocationComputeUtil.getMaxSpeedByType(type)) {
                return true;
            }
        }
        return false;
    }

    //根据运动类型获取 该运动类型的 最大速率，以下值是用来排除异常点，跟真实运动类型数据不大相符，扩大了。
    public static long getMaxSpeedByType(int type) {
        switch (type) {
            case LocationConstants.SPORT_TYPE_STEP:
                return 2;//走路的上限5米每秒
            case LocationConstants.SPORT_TYPE_RUNNING:
                return 4;//跑步的上限15米每秒
            case LocationConstants.SPORT_TYPE_DRIVE:
            case LocationConstants.SPORT_TYPE_RIDE:
                return 10;//骑行、驾驶 50米每秒。
            default:
                return 5;
        }
    }


    public static int computeIntervalTimes(long duration) {
        long timeMin = 60 * 1000;
        if (duration > timeMin) {
            return 2;
        } else if (duration > 4 * timeMin) {
            return 3;
        } else if (duration > 10 * timeMin) {
            return 5;
        }
        return 1;
    }

    public static List<AMapLocation> getAMapLocationList(List<RecordLocation> recordLocationList) {
        List<AMapLocation> aMapLocationList = new ArrayList<>();
        if (recordLocationList == null) {
            return aMapLocationList;
        }
        for (int i = 0; i < recordLocationList.size(); i++) {
            RecordLocation recordLocation = recordLocationList.get(i);
            AMapLocation aMapLocation = LocationComputeUtil.parseLocation(recordLocation.locationStr);
            aMapLocationList.add(aMapLocation);
        }
        return aMapLocationList;
    }


    public static List<AMapLocation> getAMapLocationList2(List<RecordCorrect> recordLocationList) {
        List<AMapLocation> aMapLocationList = new ArrayList<>();
        if (recordLocationList == null) {
            return aMapLocationList;
        }
        for (int i = 0; i < recordLocationList.size(); i++) {
            RecordCorrect recordLocation = recordLocationList.get(i);
            AMapLocation aMapLocation = LocationComputeUtil.parseLocation(recordLocation.locationStr);
            aMapLocationList.add(aMapLocation);
        }
        return aMapLocationList;
    }

}
