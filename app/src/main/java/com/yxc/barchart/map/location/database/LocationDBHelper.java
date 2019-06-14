package com.yxc.barchart.map.location.database;

import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordLocation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author yxc
 * @date 2019-06-10
 */
public class LocationDBHelper {

    public static void deleteRecordLocationList(final int recordType, final String recordId){
        Realm realm = RealmDbHelper.createRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {

                RealmResults<RecordLocation> locations = realm.where(RecordLocation.class)
                        .equalTo("recordType", recordType)
                        .and()
                        .equalTo("recordId", recordId)
                        .findAll();

                locations.deleteAllFromRealm();
            }
        });

    }

    //获取数据库中最近的时间戳
    public static Record getLastRecord(int recordType) {
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<Record> list = realm.where(Record.class).equalTo("recordType", recordType).findAll();
        list.sort("id");
        if (null != list && list.size() > 0) {
            return list.get(list.size() - 1);
        }
        return null;
    }


    //获取数据库中最近的时间戳
    public static RecordLocation getLastItem(String recordId) {
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class).equalTo("recordId", recordId).findAll();
        list.sort("timestamp");
        if (null != list && list.size() > 0) {
            return list.get(list.size() - 1);
        }
        return null;
    }


    public static List<RecordLocation> getLocationList(int recordType, String recordId){
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class)
                .equalTo("recordType", recordType)
                .and()
                .equalTo("recordId", recordId)
                .findAll();

        list.sort("timestamp");
        return list;
    }


    private static List<RecordLocation> getLateLocationListInner(String recordId){
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class).equalTo("recordId", recordId).findAll();
        list.sort("timestamp");
        return list;
    }

    private static List<RecordLocation> getLateLocationListInner(String recordId, long timestamp){
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class).equalTo("recordId", recordId)
                .greaterThan("timestamp", timestamp).findAll();
        list.sort("timestamp");
        return list;
    }

    private static List<AMapLocation> getLateLocationList(List<RecordLocation> locationList){
        List<AMapLocation> resultList = new ArrayList<>();
        for (int i = 0; i < locationList.size(); i++) {
            RecordLocation recordLocation = locationList.get(i);
            AMapLocation aMapLocation = ComputeUtil.parseLocation(recordLocation.locationStr);
            resultList.add(aMapLocation);
        }
        return resultList;
    }


    public static List<AMapLocation> getLateLocationList(String recordId, long timestamp){
        List<RecordLocation> locationList = getLateLocationListInner(recordId, timestamp);
        List<AMapLocation> mapLocationList = getLateLocationList(locationList);
        return mapLocationList;
    }

    public static List<AMapLocation> getLocationList(String recordId){
        List<RecordLocation> locationList = getLateLocationListInner(recordId);
        List<AMapLocation> mapLocationList = getLateLocationList(locationList);
        return mapLocationList;
    }


    /**
     * 查询所有轨迹记录
     *
     * @return
     */
    public static List<Record> queryRecordAll(int recordType) {
        List<Record> allRecord = new ArrayList<>();
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<Record> realmResults = realm.where(Record.class).equalTo("recordType", recordType).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            Record record = realmResults.get(i);
            String lines = record.pathLine;
            record.setPathLine(ComputeUtil.parseLocations(lines));
            record.setStartPoint(ComputeUtil.parseLocation(record.startPoint));
            record.setEndpoint(ComputeUtil.parseLocation(record.endPoint));
            allRecord.add(record);
        }
        Collections.reverse(allRecord);
        return allRecord;
    }


    /**
     * 按照id查询
     *
     * @param mRecordItemId
     * @return
     */
    public static Record queryRecordById(int recordType, int mRecordItemId) {
        Realm realm = RealmDbHelper.createRealm();
        Record record = realm.where(Record.class)
                .equalTo("recordType", recordType)
                .and()
                .equalTo("id", mRecordItemId)
                .findFirst();

        if (null != record) {
            String lines = record.pathLine;
            record.setPathLine(ComputeUtil.parseLocations(lines));
            record.setStartPoint(ComputeUtil.parseLocation(record.startPoint));
            record.setEndpoint(ComputeUtil.parseLocation(record.endPoint));
        }
        return record;
    }


    public static void insertRecord(final Record record){
        Realm realm = RealmDbHelper.createRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                // increment index
                Number currentIdNum = realm.where(Record.class).max("id");
                int nextId;
                if(currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                record.id = nextId;
                realm.insertOrUpdate(record); // using insert API
            }
        });
    }


    public static void  insertRecordLocation(final RecordLocation recordLocation ){
        Realm realm = RealmDbHelper.createRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(recordLocation); // using insert API
            }
        });
    }

    public static void  updateRecordLocation(final long timestamp, final long endTime, final long duration){
        Realm realm = RealmDbHelper.createRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {

                RecordLocation recordLocation =realm.where(RecordLocation.class).equalTo("timestamp", timestamp).findFirst();
                Log.d("LocationService", " saveLocation:" + recordLocation);
                recordLocation.setEndTime(endTime);
                recordLocation.setDuration(duration);
            }
        });
        realm = null;
    }

    public static RecordLocation queryRecordLocation(long timestamp){
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<RecordLocation> realmResults = realm.where(RecordLocation.class).equalTo("timestamp", timestamp).findAll();
        Log.d("LocationService", "realmResults'size:" + realmResults.size());
        return realmResults.first();
    }

}
