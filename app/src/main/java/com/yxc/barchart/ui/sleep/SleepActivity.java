package com.yxc.barchart.ui.sleep;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.view.MenuItem;
import android.widget.FrameLayout;

import com.yxc.barchart.R;
import com.yxc.widgetlib.viewpager2.ViewPager2;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.TimeDateUtil;
import com.yxc.widgetlib.calendar.view.DayCalendarView;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @since  2019/4/26
 */
public class SleepActivity extends AppCompatActivity implements DayCalendarView.OnDayCalendarItemSelectListener {

    Toolbar toolbar;
    FrameLayout container;
    LocalDate mLocalDate;
    DayCalendarView mCalendarView;

    ViewPager2 viewPager2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep);
        initView();
    }

    ViewPagerAdapter mPageAdapter;

    private void initView() {

        mLocalDate = LocalDate.now();

        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(mLocalDate), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_45dp);
        toolbar.setTitleTextColor(ColorUtil.getResourcesColor(this, R.color.white));
        setSupportActionBar(toolbar);

        mCalendarView = findViewById(R.id.calendar);
        mCalendarView.setOnDayCalendarItemSelectListener(this);
        container = findViewById(R.id.container);

        mPageAdapter = new ViewPagerAdapter(this, mLocalDate);
        viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(mPageAdapter);
        viewPager2.setReverseLayout(true);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
               LocalDate localDate = mLocalDate.minusDays(position);
               mCalendarView.setSelectDateInvalidate(localDate);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bundle bindTimestamp(long timestamp){
        Bundle bundle = new Bundle();
        bundle.putLong("timestamp", timestamp);
        return bundle;
    }

    @Override
    public void onDayItemSelect(LocalDate localDate) {
        int position = TimeDateUtil.getIntervalDay(localDate, mLocalDate);
        viewPager2.setCurrentItem(position, true);
//        switchTab(SleepFragment.class, "SleepFragment", localDate);
    }


    class ViewPagerAdapter extends FragmentStateAdapter{
        LocalDate mLocalDate;
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, LocalDate localDate) {
            super(fragmentActivity);
            this.mLocalDate = localDate;
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            LocalDate localDate = mLocalDate.minusDays(position);
            Class clz = SleepFragment.class;
            SleepFragment fragment = null;
            try {
                fragment = (SleepFragment) clz.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            fragment.setArguments(bindTimestamp(TimeDateUtil.localDateToTimestamp(localDate)));
            return fragment;
        }

        @Override
        public int getItemCount() {
            return 1000;
        }
    }


}
