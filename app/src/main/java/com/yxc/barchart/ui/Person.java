package com.yxc.barchart.ui;

import io.realm.RealmObject;

/**
 * @author yxc
 * @date 2019-06-14
 */
public class Person extends RealmObject {

    public long timestamp;//时间戳
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

    public Person() {

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public double getItemDistance() {
        return itemDistance;
    }

    public void setItemDistance(double itemDistance) {
        this.itemDistance = itemDistance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public int getRecordType() {
        return recordType;
    }

    public void setRecordType(int recordType) {
        this.recordType = recordType;
    }

    public String getLocationStr() {
        return locationStr;
    }

    public void setLocationStr(String locationStr) {
        this.locationStr = locationStr;
    }

    public double getMilePost() {
        return milePost;
    }

    public void setMilePost(double milePost) {
        this.milePost = milePost;
    }


    @Override
    public String toString() {
        return "Person{" +
                "timestamp=" + timestamp +
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
                ", milePost=" + milePost +
                '}';
    }
}
