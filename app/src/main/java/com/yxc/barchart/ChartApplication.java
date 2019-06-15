package com.yxc.barchart;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.yxc.barchart.map.location.database.RealmDbHelper;
import com.yxc.barchart.ui.MainActivity;
import com.yxc.barchart.util.CommonUtil;

import java.lang.ref.WeakReference;

public class ChartApplication extends Application {

    private static ChartApplication sApplication;
    private WeakReference<Activity> mCurrentActivity;
    private int mActivityCount = 0;

    private Application.ActivityLifecycleCallbacks mLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            if(activity instanceof MainActivity){
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            mActivityCount++;
            //数值从0变到1说明是从后台切到前台
            if (mActivityCount == 1) {
                //从后台切到前台
            }
        }


        @Override
        public void onActivityResumed(Activity activity) {
            mCurrentActivity = new WeakReference<>(activity);
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
            mActivityCount--;
            //数值从1到0说明是从前台切到后台
            if (mActivityCount == 0) {
                //从前台切到后台
            }

        }


        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;

        // service process need not following initialization
        if (!CommonUtil.isMainProcess(this)) {
            return;
        }

        registerActivityLifecycleCallbacks(mLifecycleCallbacks);

        // db
        RealmDbHelper.init("chartdb", 1);
    }

    public static ChartApplication getInstance() {
        return sApplication;
    }

    public Activity getCurrentActivity() {
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
