package com.yxc.barchart.ui.sleep;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.TimeUtil;
import com.yxc.widgetlib.calendar.view.DayCalendarView;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @since  2019/4/26
 */
public class SleepActivity extends AppCompatActivity implements DayCalendarView.OnDayCalendarItemSelectListener {

    Toolbar toolbar;
    FrameLayout container;
    SleepFragment currentFragment;
    LocalDate mLocalDate;
    DayCalendarView mCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        initView();
    }

    private void initView() {

        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeUtil.getDateStr(TimeUtil.localDateToTimestamp(LocalDate.now()), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_45dp);
        toolbar.setTitleTextColor(ColorUtil.getResourcesColor(this, R.color.white));
        setSupportActionBar(toolbar);

        mCalendarView = findViewById(R.id.calendar);
        mCalendarView.setOnDayCalendarItemSelectListener(this);
        container = findViewById(R.id.container);
        mLocalDate = LocalDate.now();
        switchTab(SleepFragment.class, "SleepFragment", mLocalDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchTab(Class clz, String tag, LocalDate localDate) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        SleepFragment fragment = (SleepFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment == null) {
            try {
                fragment = (SleepFragment) clz.newInstance();
                fragment.setArguments(bindTimestamp(TimeUtil.localDateToTimestamp(localDate)));
                ft.add(R.id.container, fragment, tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        } else {
            fragment.setArguments(bindTimestamp(TimeUtil.localDateToTimestamp(localDate)));
            ft.show(fragment);
        }
        ft.commitAllowingStateLoss();
        currentFragment = fragment;
    }

    private Bundle bindTimestamp(long timestamp){
        Bundle bundle = new Bundle();
        bundle.putLong("timestamp", timestamp);
        return bundle;
    }

    @Override
    public void onDayItemSelect(LocalDate localDate) {
        switchTab(SleepFragment.class, "SleepFragment", localDate);
    }


}
