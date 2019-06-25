package com.yxc.barchart.map.location.database;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yxc.barchart.map.location.util.LocationComputeUtil;
import com.yxc.barchart.map.model.Record;
import com.yxc.barchart.map.model.RecordCorrect;
import com.yxc.barchart.map.model.RecordLocation;
import com.yxc.barchart.util.Util;
import com.yxc.commonlib.util.TimeDateUtil;

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

    public static void deleteRecordLocationList(final int recordType, final String recordId) {
        Realm realm = RealmDbHelper.createSDRealm();
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
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<Record> list = realm.where(Record.class).equalTo("recordType", recordType).findAll();
        list.sort("id");
        if (null != list && list.size() > 0) {
            return list.get(list.size() - 1);
        }
        return null;
    }


    //获取数据库中最近的时间戳
    public static RecordLocation getLastItem(int recordType, String recordId) {
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class).equalTo("recordType", recordType)
                .and()
                .equalTo("recordId", recordId)
                .findAll();
        list.sort("timestamp");
        if (null != list && list.size() > 0) {
            return list.get(list.size() - 1);
        }
        return null;
    }


    public static RecordLocation getLastItem(int recordType) {
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class)
                .equalTo("recordType", recordType)
                .findAll();
        list.sort("timestamp");
        if (null != list && list.size() > 0) {
            return list.get(list.size() - 1);
        }
        return null;
    }


    public static List<RecordLocation> getLocationList(int recordType, String recordId) {
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class)
                .equalTo("recordType", recordType)
                .and()
                .equalTo("recordId", recordId)
                .findAll();
        list.sort("timestamp");

        ArrayList<RecordLocation> recordLocationList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            RecordLocation originLocation = list.get(i);
            RecordLocation recordLocation = RecordLocation.copyLocation(originLocation);
            recordLocationList.add(recordLocation);
        }
        return recordLocationList;
    }


    public static List<RecordLocation> queryRecordLocationAll(int recordType, String recordId) {
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class)
                .equalTo("recordType", recordType)
                .and()
                .equalTo("recordId", recordId)
                .findAll();
        list.sort("timestamp");
         return list;
    }


    public static List<RecordCorrect> queryRecordCorrectAll(int recordType, String recordId) {
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<RecordCorrect> list = realm.where(RecordCorrect.class)
                .equalTo("recordType", recordType)
                .and()
                .equalTo("recordId", recordId)
                .findAll();
        list.sort("timestamp");
        return list;
    }

    public static List<RecordLocation> getLateLocationList(String recordId, long timestamp) {
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<RecordLocation> list = realm.where(RecordLocation.class).equalTo("recordId", recordId)
                .greaterThan("timestamp", timestamp).findAll();
        list.sort("timestamp");
        return list;
    }

    /**
     * 查询所有轨迹记录
     *
     * @return
     */
    public static List<Record> queryRecordAll(int recordType) {
        List<Record> allRecord = new ArrayList<>();
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<Record> realmResults = realm.where(Record.class).equalTo("recordType", recordType).findAll();
        for (int i = 0; i < realmResults.size(); i++) {
            Record record = realmResults.get(i);
            String lines = record.pathLine;
            Gson gson = Util.createGson();
            List<RecordLocation> recordLocationList = gson.fromJson(lines, new TypeToken<List<RecordLocation>>() {
            }.getType());
            record.setPathLine(recordLocationList);
            record.setStartPoint(LocationComputeUtil.parseLocation(record.startPoint));
            record.setEndpoint(LocationComputeUtil.parseLocation(record.endPoint));
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
        Realm realm = RealmDbHelper.createSDRealm();
        Record record = realm.where(Record.class)
                .equalTo("recordType", recordType)
                .and()
                .equalTo("id", mRecordItemId)
                .findFirst();

        if (null != record) {
            String lines = record.pathLine;
            Gson gson = Util.createGson();
            if (record.isCorrect) {
                List<RecordCorrect> recordLocationList = gson.fromJson(lines, new TypeToken<List<RecordCorrect>>() {
                }.getType());
                record.setPathCorrectLine(recordLocationList);
            } else {
                List<RecordLocation> recordLocationList = gson.fromJson(lines, new TypeToken<List<RecordLocation>>() {
                }.getType());
                record.setPathLine(recordLocationList);
            }
            record.setStartPoint(LocationComputeUtil.parseLocation(record.startPoint));
            record.setEndpoint(LocationComputeUtil.parseLocation(record.endPoint));
        }
        return record;
    }


    public static void insertRecord(final Record record) {
        Realm realm = RealmDbHelper.createSDRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                // increment index
                Number currentIdNum = realm.where(Record.class).max("id");
                int nextId;
                if (currentIdNum == null) {
                    nextId = 1;
                } else {
                    nextId = currentIdNum.intValue() + 1;
                }
                record.id = nextId;
                realm.insertOrUpdate(record); // using insert API
            }
        });
    }


    public static void insertRecordLocation(final RecordLocation recordLocation) {
        Realm realm = RealmDbHelper.createSDRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                realm.insertOrUpdate(recordLocation); // using insert API
            }
        });
    }

    public static void updateRecordLocation(final long timestamp, final long endTime, final long duration) {
        Realm realm = RealmDbHelper.createSDRealm();
        realm.executeTransaction(new Realm.Transaction() { // must be in transaction for this to work
            @Override
            public void execute(Realm realm) {
                RecordLocation recordLocation = realm.where(RecordLocation.class).equalTo("timestamp", timestamp).findFirst();
                Log.d("LocationService", " saveLocation:" + recordLocation);
                recordLocation.setEndTime(endTime);
                recordLocation.setDuration(duration);
            }
        });
        realm = null;
    }

    public static RecordLocation queryRecordLocation(long timestamp) {
        Realm realm = RealmDbHelper.createSDRealm();
        RealmResults<RecordLocation> realmResults = realm.where(RecordLocation.class).equalTo("timestamp", timestamp).findAll();
        Log.d("LocationService", "realmResults'size:" + realmResults.size());
        return realmResults.first();
    }


    public static void saveRecord(Context context, List<RecordLocation> list) {
        if (list != null && list.size() > 0) {
            RecordLocation firstLocation = list.get(0);
            RecordLocation lastLocation = list.get(list.size() - 1);
            int recordType = firstLocation.getRecordType();
            double distance = lastLocation.distance;
            long duration = getDuration(firstLocation, lastLocation);
            String averageSpeed = getAverage(distance, duration);
            String pathLineStr = LocationComputeUtil.getPathLineStr(list);
            String dateStr = TimeDateUtil.getDateStrMinSecond(firstLocation.getTimestamp());

            Record record = Record.createRecord(recordType, Double.toString(distance),
                    Long.toString(duration), averageSpeed, pathLineStr,
                    firstLocation.locationStr, lastLocation.locationStr, dateStr, false);
            LocationDBHelper.insertRecord(record);
        } else {
            Toast.makeText(context, "没有记录到路径", Toast.LENGTH_SHORT).show();
        }
    }


    private static long getDuration(RecordLocation firstLocation, RecordLocation lastLocation) {
        return (lastLocation.getEndTime() - firstLocation.getTimestamp()) / 1000;
    }

    private static String getAverage(double distance, long duration) {
        return String.valueOf(distance / duration);
    }

    public static void saveRecordCorrect(Context context, List<RecordCorrect> list) {
        if (list != null && list.size() > 0) {
            RecordCorrect firstLocation = list.get(0);
            RecordCorrect lastLocation = list.get(list.size() - 1);

            int recordType = firstLocation.getRecordType();
            double distance = lastLocation.distance;

            long duration = getDuration(firstLocation, lastLocation);
            String averageSpeed = getAverage(distance, duration);

            String pathLineStr = LocationComputeUtil.getPathLineCorrectStr(list);
            String dateStr = TimeDateUtil.getDateStrMinSecond(firstLocation.getTimestamp());

            Record record = Record.createRecord(recordType, Double.toString(distance),
                    Long.toString(duration), averageSpeed, pathLineStr,
                    firstLocation.locationStr, lastLocation.locationStr, dateStr, true);
            LocationDBHelper.insertRecord(record);
        } else {
            Toast.makeText(context, "没有记录到路径", Toast.LENGTH_SHORT).show();
        }
    }


    private static long getDuration(RecordCorrect firstLocation, RecordCorrect lastLocation) {
        return (lastLocation.getEndTime() - firstLocation.getTimestamp()) / 1000;
    }

}
