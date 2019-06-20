package com.yxc.barchart.map.location.database;

import android.os.Environment;

import com.yxc.barchart.ChartApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmDbHelper {
    protected static RealmConfiguration mDefaultConfig;


    protected static RealmConfiguration mSDCardDefaultConfig;

    public static void init(String dbName,  int dbVersion) {
        Realm.init(ChartApplication.getInstance());

        mDefaultConfig = new RealmConfiguration.Builder()
                .schemaVersion(dbVersion)
                .deleteRealmIfMigrationNeeded()
//                .encryptionKey(key.getBytes())
//                .migration(migration)
                .name(dbName)
                .build();

        Realm.setDefaultConfiguration(mDefaultConfig);
    }

    public static void initSDCard(String dbName,  int dbVersion) {
        Realm.init(ChartApplication.getInstance());

        mSDCardDefaultConfig = new RealmConfiguration.Builder()
                .directory(Environment.getExternalStorageDirectory())
                .schemaVersion(dbVersion)
                .deleteRealmIfMigrationNeeded()
//                .encryptionKey(key.getBytes())
//                .migration(migration)
                .name(dbName + ".realm")
                .build();

        Realm.setDefaultConfiguration(mSDCardDefaultConfig);
    }

    public static Realm createRealm() {
        //一个Realm只能在同一个线程中访问，在子线程中进行数据库操作必须重新获取Realm对象
        return Realm.getInstance(mSDCardDefaultConfig);
    }

    public static Realm createSDRealm(){
        return Realm.getInstance(mSDCardDefaultConfig);
    }
}