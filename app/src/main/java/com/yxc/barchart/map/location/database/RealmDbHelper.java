package com.yxc.barchart.map.location.database;

import android.os.Environment;

import com.yxc.barchart.ChartApplication;

import java.util.List;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.RealmSchema;

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


        mSDCardDefaultConfig = new RealmConfiguration.Builder()
                .directory(Environment.getExternalStorageDirectory())
                .schemaVersion(dbVersion)
                .deleteRealmIfMigrationNeeded()
//                .encryptionKey(key.getBytes())
//                .migration(migration)
                .name(dbName + ".realm")
                .build();

        Realm.setDefaultConfiguration(mSDCardDefaultConfig);
        Realm.setDefaultConfiguration(mDefaultConfig);
    }

    public static Realm createRealm() {
        //一个Realm只能在同一个线程中访问，在子线程中进行数据库操作必须重新获取Realm对象
        return Realm.getInstance(mSDCardDefaultConfig);
    }


    public static Realm getSDCardRealm(){
        return Realm.getInstance(mSDCardDefaultConfig);
    }


    /**
     * 添加指定类到数据库
     *
     * @param info
     */
    public static synchronized void insertRealmObject(RealmObject info) {
        Realm realm = createRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(info);
        realm.commitTransaction();
        realm.close();


    }

    /**
     * 批量插入数据
     *
     * @param infos
     */
    public static synchronized void insertRealmObjects(final List<? extends RealmObject> infos) {
        Realm realm = createRealm();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(infos);
            }
        });
        realm.close();
    }

    /**
     * 查询指定类的全部存储信息
     *
     * @param clazz
     * @return
     */
    public static synchronized List<? extends RealmObject> queryRealmObjects(Class<? extends RealmObject> clazz) {
        Realm realm = createRealm();
        RealmResults<? extends RealmObject> realmResults = realm.where(clazz).findAll();
        List<? extends RealmObject> arrayList = realm.copyFromRealm(realmResults);
        realm.close();
        return arrayList;
    }

    /**
     * 删除指定类的全部数据库信息
     */
    public static synchronized void deleteRealmObjects(Class<? extends RealmObject> clazz) {
        Realm realm = createRealm();
        realm.beginTransaction();
        realm.delete(clazz);
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 删除此realm对应的全部数据库信息
     */
    public static synchronized void deleteAllRealmObjects() {
        Realm realm = createRealm();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 升级数据库
     */
    protected static  RealmMigration migration = new RealmMigration() {//升级数据库
        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            // DynamicRealm exposes an editable schema
            RealmSchema schema = realm.getSchema();
            if (oldVersion > newVersion) {//数据库降级
                Realm.deleteRealm(mDefaultConfig);
            } else if (oldVersion < newVersion) {//数据库升级
            }
        }
    };
}
