package com.yxc.barchart.map.model;

import com.amap.api.location.AMapLocation;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * @author yxc
 * @since 2019-06-10
 *
 */
public class RecordLocation extends RealmObject implements Comparable<RecordLocation> {

    @Ignore
    public AMapLocation location;

    @PrimaryKey
    public long timestamp;//时间戳

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long endTime;//当前点待了多久，用 endTime - timestamp = duration。
    public long duration;
    public double longitude;//精度
    public double latitude;//维度
    public float speed;//单点的速度，用来划线的时候上不同的颜色
    public double itemDistance;//距离上一个点的距离
    public double distance;//距离起始点的距离
    public String recordId;//运动记录 id(用于聚合查询)
    public int recordType;//运动类型，跑步，骑行，驾驶。
    public String locationStr;//包含AMapLocation的字段
    public double milePost = 0;//里程碑

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
        recordLocation.endTime = recordLocation.timestamp;
        recordLocation.duration = 0;
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

    public static RecordLocation copy(RecordLocation recordLocation){
        RecordLocation result = new RecordLocation();
        result.timestamp= recordLocation.timestamp;
        result.duration = recordLocation.duration;
        result.location = recordLocation.location;
        result.endTime = recordLocation.endTime;
        result.speed = recordLocation.speed;
        result.recordType = recordLocation.recordType;
        result.recordId = recordLocation.recordId;
        result.distance = recordLocation.distance;
        result.itemDistance = recordLocation.itemDistance;
        result.locationStr = recordLocation.locationStr;
        result.longitude = recordLocation.longitude;
        result.latitude = recordLocation.latitude;
        return result;
    }

    @Override
    public String toString() {
        return "RecordLocation{" +
                "location=" + location +
                ", timestamp=" + timestamp +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", speed=" + speed +
                ", itemDistance=" + itemDistance +
                ", distance=" + distance +
                ", recordId='" + recordId + '\'' +
                ", recordType=" + recordType +
                ", locationStr='" + locationStr + '\'' +
                '}';
    }
}
