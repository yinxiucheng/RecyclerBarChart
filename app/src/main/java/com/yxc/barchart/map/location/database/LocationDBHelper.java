package com.yxc.barchart.map.location.database;

import com.yxc.barchart.map.location.PathRecord;
import com.yxc.barchart.map.location.util.ComputeUtil;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordLocation;
import com.yxc.barchart.util.RealmDbHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * @author yxc
 * @date 2019-06-10
 */
public class LocationDBHelper {

    //获取数据库中最近的时间戳
    public static Record getLastRecord() {
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<Record> list = realm.where(Record.class).findAll();
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


    /**
     * 查询所有轨迹记录
     *
     * @return
     */
    public static List<PathRecord> queryRecordAll() {
        List<PathRecord> allRecord = new ArrayList<PathRecord>();
        Realm realm = RealmDbHelper.createRealm();
        RealmResults<Record> realmResults = realm.where(Record.class).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            PathRecord pathRecord = new PathRecord();
            Record record = realmResults.get(i);
            pathRecord.setId(record.id);
            pathRecord.setDistance(record.distance);
            pathRecord.setDuration(record.duration);
            pathRecord.setDate(record.date);
            String lines = record.pathLine;
            pathRecord.setPathline(ComputeUtil.parseLocations(lines));
            pathRecord.setStartpoint(ComputeUtil.parseLocation(record.startPoint));
            pathRecord.setEndpoint(ComputeUtil.parseLocation(record.endPoint));
            allRecord.add(pathRecord);
        }
        return allRecord;
    }


    /**
     * 按照id查询
     *
     * @param mRecordItemId
     * @return
     */
    public static PathRecord queryRecordById(int mRecordItemId) {

        Realm realm = RealmDbHelper.createRealm();
        Record record = realm.where(Record.class).equalTo("id", mRecordItemId).findFirst();

        PathRecord pathRecord = new PathRecord();
        if (null != record) {
            pathRecord.setId(record.id);
            pathRecord.setDistance(record.distance);
            pathRecord.setDuration(record.duration);
            pathRecord.setDate(record.date);
            String lines = record.pathLine;
            pathRecord.setPathline(ComputeUtil.parseLocations(lines));
            pathRecord.setStartpoint(ComputeUtil.parseLocation(record.startPoint));
            pathRecord.setEndpoint(ComputeUtil.parseLocation(record.endPoint));
        }
        return pathRecord;
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


    public static void insertRecordLocation(final RecordLocation recordLocation){
        Realm realm = RealmDbHelper.createRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(recordLocation); // using insert API
            }
        });
    }

}
