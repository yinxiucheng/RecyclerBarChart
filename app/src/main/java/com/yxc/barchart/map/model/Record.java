package com.yxc.barchart.map.model;

import com.amap.api.location.AMapLocation;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/**
 * @author yxc
 * @date 2019-06-10
 */
public class Record extends RealmObject {

    @PrimaryKey
    public int id;

    public int recordType;

    public String distance;

    public String duration;

    public String speed;

    public String pathLine;

    @Ignore
    private AMapLocation mStartPoint;

    @Ignore
    private AMapLocation mEndPoint;

    public String startPoint;

    public String endPoint;

    public String date;

    @Ignore
    private List<AMapLocation> mPathLinePoints = new ArrayList<AMapLocation>();

    @Ignore
    public List<RecordLocation> mPathLocationList = new ArrayList<>();

    public Record() {

    }

    public static Record createRecord(int recordType, String distance, String duration, String speed,
                                      String pathLine, String startPoint,
                                      String endPoint, String date) {
        Record record = new Record();
        record.recordType = recordType;
        record.distance = distance;
        record.duration = duration;
        record.speed = speed;
        record.pathLine = pathLine;
        record.startPoint = startPoint;
        record.endPoint = endPoint;
        record.date = date;
        return record;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void addPoint(AMapLocation point) {
        mPathLinePoints.add(point);
    }

    public void addPointList(List<AMapLocation> pointList) {
        mPathLinePoints.addAll(pointList);
    }

    public List<AMapLocation> getPathLine() {
        return mPathLinePoints;
    }

    public void setPathLine(List<AMapLocation> pathLine) {
        this.mPathLinePoints = pathLine;
    }


    public AMapLocation getStartPoint() {
        return mStartPoint;
    }

    public void setStartPoint(AMapLocation startPoint) {
        this.mStartPoint = startPoint;
    }

    public AMapLocation getEndpoint() {
        return mEndPoint;
    }

    public void setEndpoint(AMapLocation endpoint) {
        this.mEndPoint = endpoint;
    }


    @Override
    public String toString() {
        StringBuilder record = new StringBuilder();
        record.append("recordSize:" + getPathLine().size() + ", ");
        record.append("distance:" + distance + "m, ");
        record.append("duration:" + duration + "s");
        return record.toString();
    }

}
