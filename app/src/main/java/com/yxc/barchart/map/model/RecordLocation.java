package com.yxc.barchart.map.model;

import com.amap.api.location.AMapLocation;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * @author yxc
 * @since 2019-06-10
 *
 */
public class RecordLocation extends RealmObject implements Comparable<RecordLocation> {

    @Ignore
    public AMapLocation location;

    public long timestamp;//时间戳
    public double longitude;//精度
    public double latitude;//维度
    public float speed;//速度
    public double itemDistance;//距离上一个点的距离
    public double distance;//距离起始点的距离
    public String recordId;//运动记录 id(用于聚合查询)
    public int recordType;//运动类型，跑步，骑行，驾驶。
    public String locationStr;//包含AMapLocation的字段

    @Override
    public int compareTo(RecordLocation o) {
        return (int) (timestamp - o.timestamp);
    }

    public RecordLocation() {
    }

    public RecordLocation(AMapLocation location) {
        this.location = location;
    }

    public static RecordLocation createLocation(AMapLocation location, String recordId,
                                                int recordType, double itemDistance,
                                                double distance, String locationStr){
        RecordLocation recordLocation = new RecordLocation(location);
        recordLocation.timestamp = location.getTime();
        recordLocation.latitude = location.getLatitude();
        recordLocation.longitude = location.getLongitude();
        recordLocation.speed = location.getSpeed();
        recordLocation.recordId = recordId;
        recordLocation.recordType = recordType;
        recordLocation.itemDistance = itemDistance;
        recordLocation.distance = distance;
        recordLocation.locationStr = locationStr;
        return recordLocation;
    }

}
