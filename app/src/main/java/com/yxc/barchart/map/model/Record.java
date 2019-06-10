package com.yxc.barchart.map.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author yxc
 * @date 2019-06-10
 */
public class Record extends RealmObject {

    @PrimaryKey
    public int id;

    public String distance;

    public String duration;

    public String speed;

    public String pathLine;

    public String startPoint;

    public String endPoint;

    public String date;


    public Record() {
    }

    public static Record createRecord(String distance, String duration, String speed,
                               String pathLine, String startPoint,
                               String endPoint, String date){
        Record record = new Record();
        record.distance = distance;
        record.duration = duration;
        record.speed = speed;
        record.pathLine = pathLine;
        record.startPoint = startPoint;
        record.endPoint = endPoint;
        record.date = date;
        return record;
    }

}
