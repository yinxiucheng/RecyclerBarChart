
package com.yxc.barchart;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class BarChartActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    List<BarEntry> mVisibleEntries;
    BarChartItemDecoration mItemDecoration;
    private int displayNumber;
    YAxis mYAxis;
    XAxis mXAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barchart_main);

        recyclerView = findViewById(R.id.recycler);
        mEntries = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.HORIZONTAL);

        displayNumber = 8;
        mYAxis = new YAxis();
        mXAxis = new XAxis(this, displayNumber);

        mItemDecoration = new BarChartItemDecoration(this, BarChartItemDecoration.HORIZONTAL_LIST, mYAxis, mXAxis);
//        mItemDecoration.setEnableCharValueDisplay(false);
//        mItemDecoration.setEnableYAxisGridLine(false);
//        mItemDecoration.setEnableYAxisZero(false);
//        mItemDecoration.setBarBorderWidth(10);
//        mItemDecoration.setEnableBarBorder(false);
        mItemDecoration.setEnableLeftYAxisLabel(false);
//        mItemDecoration.setEnableRightYAxisLabel(false);
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(this, mEntries, recyclerView, mXAxis);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);

        createWeekEntries();
        recyclerView.scrollToPosition(mEntries.size() - 1);

        int lastVisiblePosition = mEntries.size() - 1;
        int firstVisiblePosition = lastVisiblePosition - displayNumber;
        mVisibleEntries = mEntries.subList(firstVisiblePosition, lastVisiblePosition);
        mYAxis = YAxis.getYAxis(getTheMaxNumber(mVisibleEntries));
        mBarChartAdapter.notifyDataSetChanged();

        mItemDecoration.setYAxis(mYAxis);
        recyclerView.invalidate();
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

    //滑动监听
    private void setListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition
                    int lastVisibleItem = manager.findLastVisibleItemPosition();
                    int firstVisibleItem = manager.findFirstVisibleItemPosition();
                    List<BarEntry> displayEntries = mEntries.subList(firstVisibleItem, lastVisibleItem);
                    float max = getTheMaxNumber(displayEntries);
                    mYAxis = YAxis.getYAxis(max);
                    mItemDecoration.setYAxis(mYAxis);
                    recyclerView.invalidate();
                    int totalItemCount = manager.getItemCount();
                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                if (dx > 0) {
                    //大于0表示正在向右滚动
                    isSlidingToLast = true;
                } else {
                    //小于等于0表示停止或向左滚动
                    isSlidingToLast = false;
                }
            }
        });
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

    // 创建 月视图的数据
    private void createMonthEntries() {
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
            boolean isLastDayOfMonth = TimeUtil.isLastDayOfMonth(localDate);
            if (isLastDayOfMonth && i % 7 == 0) {
                type = BarEntry.TYPE_SPECIAL;
                xAxisLabel = localDate.getDayOfMonth() + "日";
            } else if (isLastDayOfMonth) {
                type = BarEntry.TYPE_FIRST;
            } else if (i % 7 == 0) {
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
        mBarChartAdapter.notifyDataSetChanged();
    }


    //创建Week视图的数据
    private void createWeekEntries() {
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
            int type = BarEntry.TYPE_SECOND;
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            boolean isLastDayOfMonth = TimeUtil.isLastDayOfMonth(localDate);
            if (isLastDayOfMonth) {
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
        mBarChartAdapter.notifyDataSetChanged();
    }


    //创建 Day视图的数据
    private void createDayEntries() {
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
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            boolean isLastDayofMonth = TimeUtil.isLastDayOfMonth(localDate);

            if (isLastDayofMonth && (i + 1) % 7 == 0) {
                type = BarEntry.TYPE_SPECIAL;
            } else if (isLastDayofMonth) {
                type = BarEntry.TYPE_FIRST;
            } else if ((i + 1) % 7 == 0) {
                type = BarEntry.TYPE_SECOND;
            }
            BarEntry barEntry = new BarEntry(value, timestamp, type);
            barEntry.localDate = localDate;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        mEntries.addAll(0, entries);
        mBarChartAdapter.notifyDataSetChanged();
    }
}
