
package com.yxc.barchart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxc.barchart.component.BarEntry;
import com.yxc.barchart.component.DistanceCompare;
import com.yxc.barchart.component.XAxis;
import com.yxc.barchart.component.YAxis;
import com.yxc.barchart.tab.OnTabSelectListener;
import com.yxc.barchart.tab.TopTabLayout;
import com.yxc.util.BarChartAttrs;
import com.yxc.util.ColorUtil;
import com.yxc.util.TimeUtil;
import com.yxc.wediget.BarChartRecyclerView;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BarChartActivity extends AppCompatActivity {

    public static final int VIEW_DAY = 0;
    public static final int VIEW_WEEK = 1;
    public static final int VIEW_MONTH = 2;
    public static final int VIEW_YEAR = 3;
    int mType;

    BarChartRecyclerView recyclerView;
    TopTabLayout mTabLayout;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    ImageView imgLast;
    ImageView imgNext;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    List<BarEntry> mVisibleEntries;
    BarChartItemDecoration mItemDecoration;
    private int displayNumber;
    YAxis mYAxis;
    XAxis mXAxis;

    private String[] mTitles = {"日", "周", "月", "年"};
    private boolean needRelocation = false;
    private BarChartAttrs mBarChartAttrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barchart_main);

        mTabLayout = findViewById(R.id.topTabLayout);
        recyclerView = findViewById(R.id.recycler);
        mBarChartAttrs = recyclerView.mAttrs;

        txtLeftLocalDate = findViewById(R.id.txt_left_local_date);
        txtRightLocalDate = findViewById(R.id.txt_right_local_date);
        imgLast = findViewById(R.id.img_left);
        imgNext = findViewById(R.id.img_right);
        initTableLayout();
        mEntries = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);

        displayNumber = 25;
        mYAxis = new YAxis();
        mXAxis = new XAxis(this, displayNumber);
        mItemDecoration = new BarChartItemDecoration(this, BarChartItemDecoration.HORIZONTAL_LIST, mYAxis, mXAxis, mBarChartAttrs);
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(this, mEntries, recyclerView, mXAxis);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);

        createDayEntries();
        reSizeYAxis();
        setListener();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mYAxis.labelSize = 5;
                mYAxis.maxLabel = 30000;
                mItemDecoration.setYAxis(mYAxis);
                recyclerView.invalidate();
            }
        });
    }

    private void reSizeYAxis() {
        recyclerView.scrollToPosition(mEntries.size() - 1);
        int lastVisiblePosition = mEntries.size() - 1;
        int firstVisiblePosition = lastVisiblePosition - displayNumber + 1;
        mVisibleEntries = mEntries.subList(firstVisiblePosition, lastVisiblePosition);
        mYAxis = YAxis.getYAxis(getTheMaxNumber(mVisibleEntries));
        mBarChartAdapter.notifyDataSetChanged();
        mItemDecoration.setYAxis(mYAxis);

        BarEntry leftBarEntry = mEntries.get(firstVisiblePosition);
        BarEntry rightBarEntry = mEntries.get(lastVisiblePosition);

        txtLeftLocalDate.setText(TimeUtil.getDateStr(leftBarEntry.timestamp));
        txtRightLocalDate.setText(TimeUtil.getDateStr(rightBarEntry.timestamp));
    }


    //滑动监听
    private void setListener() {
        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                lastVisiblePosition = lastVisiblePosition + displayNumber - 1;
                if (lastVisiblePosition < mEntries.size()) {
                    recyclerView.scrollToPosition(lastVisiblePosition);
                } else {
                    recyclerView.scrollToPosition(mEntries.size() - 1);
                }
                if (needRelocation) {
                    //微调
                    scrollToScale(recyclerView);
                } else {
                    microRelation(recyclerView);
                }
            }
        });

        imgLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = firstCompletelyVisibleItemPosition - displayNumber;
                if (lastCompletelyVisibleItemPosition < 0) {
                    recyclerView.scrollToPosition(displayNumber);
                } else {
                    recyclerView.scrollToPosition(lastCompletelyVisibleItemPosition);
                }
                if (needRelocation) {
                    //微调
                    scrollToScale(recyclerView);
                } else {
                    microRelation(recyclerView);
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (needRelocation) {
                        scrollToScale(recyclerView);
                    } else {
                        microRelation(recyclerView);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //判断左滑，右滑时，ScrollView的位置不一样。
            }
        });
    }


    //位置进行微调
    private void microRelation(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();

        //进行微调
        recyclerView.scrollToPosition(lastVisibleItemPosition);

        lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();

        //todo 试图在 ItemDecoration中进行微调，根据 child的 getRight、getLeft跟 parentLeft、parentRight的位置来主动判断
        // todo 真正的显示边界(completeDisplayVisibleItemPosition), 取到 displayEntries
        mXAxis.firstVisiblePosition = firstVisibleItemPosition;
        mXAxis.lastVisiblePosition = lastVisibleItemPosition;
        mItemDecoration.setXAxis(mXAxis);

        List<BarEntry> displayEntries = mEntries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
        float max = getTheMaxNumber(displayEntries);
        mYAxis = YAxis.getYAxis(max);
        mItemDecoration.setYAxis(mYAxis);
        recyclerView.invalidate();

        //todo 调试显示用的
        BarEntry leftBarEntry = displayEntries.get(0);
        BarEntry rightBarEntry = displayEntries.get(displayEntries.size() - 1);
        txtLeftLocalDate.setText(TimeUtil.getDateStr(leftBarEntry.timestamp));
        txtRightLocalDate.setText(TimeUtil.getDateStr(rightBarEntry.timestamp));
    }


    //滑动到附近的刻度线
    private void scrollToScale(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
        BarEntry barEntry = mEntries.get(lastVisibleItemPosition);
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        if (mType == VIEW_MONTH) {
            distanceCompare = createMonthDistance(barEntry.localDate);
        } else if (mType == VIEW_WEEK) {
            distanceCompare = createWeekDistance(barEntry.localDate);
        } else if (mType == VIEW_DAY) {
            distanceCompare = createDayDistance(barEntry);
        } else if (mType == VIEW_YEAR) {
            distanceCompare = createYearDistance(barEntry.localDate);
        }
        int endOfPosition;
        int transactionType = 0;
        if (distanceCompare.distanceLeft < distanceCompare.distanceRight) {//左间距 小于 右间距
            transactionType = 1;
            endOfPosition = lastVisibleItemPosition - distanceCompare.distanceLeft + 1;
            if (lastVisibleItemPosition + distanceCompare.distanceRight > mEntries.size()) {//右尽头
                transactionType = 0;
            }
        } else {
            endOfPosition = lastVisibleItemPosition + distanceCompare.distanceRight;
            if (lastVisibleItemPosition - displayNumber <= 0) {//左尽头
                transactionType = 1;
            }
        }

        if (transactionType == 1 && endOfPosition <= displayNumber) {//左移到头
            recyclerView.scrollToPosition(0);
            lastVisibleItemPosition = displayNumber;
        } else if (transactionType == 1) {
            recyclerView.scrollToPosition(endOfPosition - displayNumber);
            lastVisibleItemPosition = endOfPosition;
        }
        if (transactionType == 0 && endOfPosition >= mEntries.size()) {//右边界，lastVisibleItemPosition 保持不变
            recyclerView.scrollToPosition(mEntries.size() - 1);
            lastVisibleItemPosition = mEntries.size() - 1;
        } else if (transactionType == 0) {
            recyclerView.scrollToPosition(endOfPosition);
            lastVisibleItemPosition = endOfPosition;
        }
        firstVisibleItemPosition = lastVisibleItemPosition - displayNumber;

        List<BarEntry> displayEntries = mEntries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
        float max = getTheMaxNumber(displayEntries);
        mYAxis = YAxis.getYAxis(max);
        mItemDecoration.setYAxis(mYAxis);
//        recyclerView.invalidate();

        //todo 调试显示用的
        BarEntry leftBarEntry = displayEntries.get(0);
        BarEntry rightBarEntry = displayEntries.get(displayEntries.size() - 1);
        txtLeftLocalDate.setText(TimeUtil.getDateStr(leftBarEntry.timestamp));
        txtRightLocalDate.setText(TimeUtil.getDateStr(rightBarEntry.timestamp));
    }


    private DistanceCompare createYearDistance(LocalDate localDate) {
        int month = localDate.getMonthOfYear();
        return new DistanceCompare(month, 12 - month);
    }

    private DistanceCompare createDayDistance(BarEntry barEntry) {
        LocalDate localDate = barEntry.localDate;
        long tomorrowZeroTime = TimeUtil.changZeroOfTheDay(localDate.plusDays(1));//明天凌晨
        long todayZeroTime = TimeUtil.changZeroOfTheDay(localDate);//今天0点

        int distanceRight = (int) ((tomorrowZeroTime - barEntry.timestamp) / TimeUtil.TIME_HOUR); //右边的距离
        int distanceLeft = (int) ((barEntry.timestamp - todayZeroTime) / TimeUtil.TIME_HOUR);//左边的距离

        return new DistanceCompare(distanceLeft, distanceRight);
    }

    private DistanceCompare createWeekDistance(LocalDate localDate) {
        int dayOfWeek = localDate.getDayOfWeek();
        return new DistanceCompare(dayOfWeek, 7 - dayOfWeek);
    }

    private DistanceCompare createMonthDistance(LocalDate localDate) {
        LocalDate nextMonthFirstDay = TimeUtil.getFirstDayOfNextMonth(localDate);
        LocalDate lastMonthLastDay = TimeUtil.getFirstDayOfMonth(localDate).minusDays(1);

        int distanceRight = TimeUtil.getIntervalDay(localDate, nextMonthFirstDay); //右边的距离
        int distanceLeft = TimeUtil.getIntervalDay(lastMonthLastDay, localDate);
        return new DistanceCompare(distanceLeft, distanceRight);
    }

    //获取最大值
    private float getTheMaxNumber(List<BarEntry> entries) {
        BarEntry barEntry = entries.get(0);
        float max = barEntry.value;
        for (int i = 0; i < entries.size(); i++) {
            BarEntry entryTemp = entries.get(i);
            max = Math.max(max, entryTemp.value);
        }
        return max;
    }

    private void initTableLayout() {
        mTabLayout.setIndicatorColor(ColorUtil.getResourcesColor(this, R.color.pink));
        mTabLayout.setTextUnselectColor(ColorUtil.getResourcesColor(this, R.color.tab_checked));
        mTabLayout.setDividerColor(ColorUtil.getResourcesColor(this, R.color.pink));
//        mTabLayout.setIndicatorColor(ColorUtil.getResourcesColor(this, R.color.pink));
        mTabLayout.setTabData(mTitles);

        mTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                if (position == 0) {
                    createDayEntries();
                    reSizeYAxis();
                } else if (position == 1) {
                    createWeekEntries();
                    reSizeYAxis();
                } else if (position == 2) {
                    createMonthEntries();
                    reSizeYAxis();
                } else if (position == 3) {
                    createYearEntries();
                    reSizeYAxis();
                }
            }
            @Override
            public void onTabReselect(int position) {
            }
        });
        mTabLayout.setCurrentTab(0);
    }

    // 创建 月视图的数据
    private void createMonthEntries() {
        mEntries.clear();
        mType = VIEW_MONTH;
        displayNumber = 32;
        mXAxis = new XAxis(this, displayNumber);
        long timestamp = TimeUtil.changZeroOfTheDay(LocalDate.now());
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 600; i++) {
            if (i > 0) {
                timestamp = timestamp - TimeUtil.TIME_DAY;
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);
            int type = BarEntry.TYPE_THIRD;
            String xAxisLabel = "";
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            boolean isFirstDayOfMonth = TimeUtil.isFirstDayOfMonth(localDate);
            if (isFirstDayOfMonth && (i + 1) % 7 == 0) {
                type = BarEntry.TYPE_SPECIAL;
                xAxisLabel = localDate.getDayOfMonth() + "日";
            } else if (isFirstDayOfMonth) {
                type = BarEntry.TYPE_FIRST;
            } else if ((i + 1) % 7 == 0) {
                type = BarEntry.TYPE_SECOND;
                xAxisLabel = localDate.getDayOfMonth() + "日";
            }
            BarEntry barEntry = new BarEntry(value, timestamp, type);
            barEntry.localDate = localDate;
            barEntry.xAxisLabel = xAxisLabel;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        mEntries.addAll(0, entries);
        mBarChartAdapter.setXAxis(mXAxis);
    }

    //创建Week视图的数据
    private void createWeekEntries() {
        mEntries.clear();
        mType = VIEW_WEEK;
        displayNumber = 8;
        mXAxis = new XAxis(this, displayNumber);
        long timestamp = TimeUtil.changZeroOfTheDay(LocalDate.now());
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 102; i++) {
            if (i > 0) {
                timestamp = timestamp - TimeUtil.TIME_DAY;
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);
            int type = BarEntry.TYPE_SECOND;
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            boolean isMonday = TimeUtil.isMonday(localDate);
            if (isMonday) {
                type = BarEntry.TYPE_FIRST;
            }
            String xAxis = TimeUtil.getWeekStr(localDate.getDayOfWeek());
            BarEntry barEntry = new BarEntry(value, timestamp, type);
            barEntry.localDate = localDate;
            barEntry.xAxisLabel = xAxis;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        mEntries.addAll(0, entries);
        mBarChartAdapter.setXAxis(mXAxis);
    }


    //创建 Day视图的数据
    private void createDayEntries() {
        mEntries.clear();
        mType = VIEW_DAY;
        displayNumber = 25;
        mXAxis = new XAxis(this, displayNumber);
        long timestamp = TimeUtil.changZeroOfTheDay(LocalDate.now().plusDays(1));
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 600; i++) {
            if (i > 0) {
                timestamp = timestamp - TimeUtil.TIME_HOUR;
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);
            int type = BarEntry.TYPE_THIRD;
            boolean isNextDay = TimeUtil.isNextDay(timestamp);
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            String xAxisStr = "";

            if (isNextDay && i % 3 == 0) {
                type = BarEntry.TYPE_SPECIAL;
                xAxisStr = TimeUtil.getHourOfTheDay(timestamp);
            } else if (isNextDay) {
                type = BarEntry.TYPE_FIRST;
            } else if (i % 3 == 0) {
                type = BarEntry.TYPE_SECOND;
                xAxisStr = TimeUtil.getHourOfTheDay(timestamp);
            }
            BarEntry barEntry = new BarEntry(value, timestamp, type);
            barEntry.localDate = localDate;
            barEntry.xAxisLabel = xAxisStr;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        mEntries.addAll(0, entries);
        mBarChartAdapter.setXAxis(mXAxis);
    }


    //创建 Day视图的数据
    private void createYearEntries() {
        mEntries.clear();
        mType = VIEW_YEAR;
        displayNumber = 13;
        mXAxis = new XAxis(this, displayNumber);
        //获取下个月1号
        LocalDate localDate = TimeUtil.getFirstDayOfMonth(LocalDate.now().plusMonths(1));
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            if (i > 0) {
                localDate = localDate.minusMonths(1);
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);

            int type = BarEntry.TYPE_SECOND;
            boolean isNextYear = TimeUtil.isAnotherYear(localDate);
            if (isNextYear) {
                type = BarEntry.TYPE_FIRST;
            }
            String xAxis = Integer.toString(localDate.getMonthOfYear());
            long timestamp = TimeUtil.changZeroOfTheDay(localDate);
            BarEntry barEntry = new BarEntry(value, timestamp, type);
            barEntry.localDate = localDate;
            barEntry.xAxisLabel = xAxis;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        mEntries.addAll(0, entries);
        mBarChartAdapter.setXAxis(mXAxis);
    }


}
