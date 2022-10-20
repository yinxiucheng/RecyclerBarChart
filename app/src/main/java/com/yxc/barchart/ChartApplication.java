package com.yxc.barchart;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.yxc.barchart.map.location.database.RealmDbHelper;
import com.yxc.barchart.util.CommonUtil;

import java.lang.ref.WeakReference;

public class ChartApplication extends Application {

    private static ChartApplication sApplication;
    private WeakReference<AppCompatActivity> mCurrentActivity;
    private int mActivityCount = 0;


    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        // service process need not following initialization
        if (!CommonUtil.isMainProcess(this)) {
            return;
        }
        //db
        RealmDbHelper.init("chartdb", 1);
    }

    public static ChartApplication getInstance() {
        return sApplication;
    }

    public AppCompatActivity getCurrentActivity() {
        if (mCurrentActivity != null && mCurrentActivity.get() != null) {
            return mCurrentActivity.get();
        }
        return null;
    }

    /**
     * app是否在前台
     *
     * @return true前台，false后台
     */
    public boolean isForeground() {
        return mActivityCount > 0;
    }


}
