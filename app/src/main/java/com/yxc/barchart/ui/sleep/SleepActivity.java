package com.yxc.barchart.ui.sleep;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.TimeDateUtil;
import com.yxc.widgetlib.calendar.view.DayCalendarView;
import com.yxc.widgetlib.viewpager2.ViewPager2;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/26
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
    private List<LocalDate> mDataList = new ArrayList<>();

    private void initView() {

        mLocalDate = LocalDate.now().minusDays(10);

        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(mLocalDate), "M月dd日"));
        toolbar.setNavigationIcon(R.drawable.ic_chevron_left_white_45dp);
        toolbar.setTitleTextColor(ColorUtil.getResourcesColor(this, R.color.white));
        setSupportActionBar(toolbar);

        mCalendarView = findViewById(R.id.calendar);
        mCalendarView.setOnDayCalendarItemSelectListener(this);
        container = findViewById(R.id.container);

        LocalDate localDate = mLocalDate;
        for (int i = 0; i < 10; i++) {
            mDataList.add(0, localDate);
            localDate = localDate.plusDays(1);
        }

        mPageAdapter = new ViewPagerAdapter(this, mDataList);
        viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setAdapter(mPageAdapter);
        viewPager2.setReverseLayout(true);

        viewPager2.addScrollListener(new RecyclerView.OnScrollListener() {

            private boolean isRightScrollInner;

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //加载更多, direction Negative to check scrolling left, positive to check scrolling right
                    if (!recyclerView.canScrollHorizontally(-1) && isRightScrollInner) {
                        Log.d("OnScroll", "getMore left!!");
                        LocalDate localDateLeft = mDataList.get(mDataList.size() - 1);
                        for (int i = 0; i < 5; i++) {
                            mDataList.add(localDateLeft);
                            localDateLeft = localDateLeft.minusDays(1);
                        }
                        mPageAdapter.notifyDataSetChanged();
                    } else if (!recyclerView.canScrollHorizontally(1)) {
                        Log.d("OnScroll", "getMore Right!!");
                        int oldPosition = viewPager2.getCurrentItem();

                        LocalDate localDateRight = mDataList.get(0);
                        mDataList.add(0, localDateRight.plusDays(1));
//                        mPageAdapter.notifyItemChanged(0);
                        mPageAdapter.notifyDataSetChanged();
                        Log.d("OnScroll", " currentItem:" + viewPager2.getCurrentItem());
                        viewPager2.setCurrentItem(0, false);
//                        viewPager2.setCurrentItem(0, true);
//                        for (int i = 0; i < 5; i++) {
//                            localDateRight = localDateRight.plusDays(1);
//                            mDataList.add(0, localDateRight);
//                        }
//                        mPageAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                isRightScrollInner = dx < 0;
            }

        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                Log.d("SleepActivity", "onPageScrolled position:" + position + " positionOffset:" + positionOffset + " positionOffsetPixels:" + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d("SleepActivity", "onPageSelected position:" + position);

                Fragment fragment = mPageAdapter.getItem(position);
                if (fragment instanceof SleepFragment) {

                }

                LocalDate localDate = mLocalDate.minusDays(position);
                mCalendarView.setSelectDateInvalidate(localDate);
                toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(localDate), "M月dd日"));

//                if (position == mDataList.size() - 1) {
//                    LocalDate localDateLeft = mDataList.get(position);
//                    for (int i = 0; i < 5; i++) {
//                        mDataList.add(localDateLeft);
//                        localDateLeft = localDateLeft.minusDays(1);
//                    }
//                    mPageAdapter.notifyDataSetChanged();
//                } else if (position == 0) {
//                    LocalDate localDateRight = mDataList.get(0);
//                    for (int i = 0; i < 5; i++) {
//                        localDateRight = localDateRight.plusDays(1);
//                        mDataList.add(0, localDateRight);
//                    }
//                    mPageAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                Log.d("SleepActivity", "onPageScrollStateChanged state:" + state);
            }
        });

        viewPager2.setCurrentItem(0, false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Bundle bindTimestamp(long timestamp) {
        Bundle bundle = new Bundle();
        bundle.putLong("timestamp", timestamp);
        return bundle;
    }

    @Override
    public void onDayItemSelect(LocalDate localDate) {
        int position = TimeDateUtil.getIntervalDay(localDate, mLocalDate);
        viewPager2.setCurrentItem(position, true);
        toolbar.setTitle(TimeDateUtil.getDateStr(TimeDateUtil.localDateToTimestamp(localDate), "M月dd日"));
//        switchTab(SleepFragment.class, "SleepFragment", localDate);
    }


    class ViewPagerAdapter extends FragmentStateAdapter {
        List<LocalDate> mDataList;

        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<LocalDate> dateList) {
            super(fragmentActivity);
            this.mDataList = dateList;
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
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

    }


}
