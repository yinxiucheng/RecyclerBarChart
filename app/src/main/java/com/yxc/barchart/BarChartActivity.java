
package com.yxc.barchart;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yxc.barchart.formatter.XAxisDayFormatter;
import com.yxc.barchart.formatter.XAxisMonthFormatter;
import com.yxc.barchart.formatter.XAxisWeekFormatter;
import com.yxc.barchart.formatter.XAxisYearFormatter;
import com.yxc.barchart.tab.OnTabSelectListener;
import com.yxc.barchart.tab.TopTabLayout;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.component.DistanceCompare;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.ColorUtil;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.TextUtil;
import com.yxc.barchartlib.util.TimeUtil;
import com.yxc.barchartlib.view.BarChartAdapter;
import com.yxc.barchartlib.view.BarChartItemDecoration;
import com.yxc.barchartlib.view.BarChartRecyclerView;
import com.yxc.barchartlib.view.SpeedRatioLinearLayoutManager;
import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BarChartActivity extends AppCompatActivity {

    public static final int VIEW_DAY = 0;
    public static final int VIEW_WEEK = 1;
    public static final int VIEW_MONTH = 2;
    public static final int VIEW_YEAR = 3;
    public int mType;
    private String[] mTitles = {"日", "周", "月", "年"};

    BarChartRecyclerView recyclerView;
    TopTabLayout mTabLayout;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    TextView textTitle;
    TextView txtCountStep;
    ImageView imgLast;
    ImageView imgNext;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    BarChartItemDecoration mItemDecoration;
    private int displayNumber;
    private BarChartAttrs mBarChartAttrs;
    YAxis mYAxis;
    XAxis mXAxis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barchart_main);
        initView();
        initTableLayout();
        initData();
        bindBarChartList(25, TestData.createDayEntries(TimeUtil.changZeroOfTheDay(LocalDate.now()),
                25, mEntries.size()), VIEW_DAY);
        reSizeYAxis();
        setListener();
    }

    private void initView(){
        mTabLayout = findViewById(R.id.topTabLayout);
        txtLeftLocalDate = findViewById(R.id.txt_left_local_date);
        txtRightLocalDate = findViewById(R.id.txt_right_local_date);
        textTitle = findViewById(R.id.txt_layout);
        txtCountStep = findViewById(R.id.txt_count_Step);
        imgLast = findViewById(R.id.img_left);
        imgNext = findViewById(R.id.img_right);
        recyclerView = findViewById(R.id.recycler);

        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData(){
        mEntries = new ArrayList<>();
        SpeedRatioLinearLayoutManager layoutManager = new SpeedRatioLinearLayoutManager(this, mBarChartAttrs);
        displayNumber = 25;
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mXAxis.setValueFormatter(new XAxisDayFormatter());
        mItemDecoration = new BarChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(this, mEntries, recyclerView, mXAxis);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }


    private void reSizeYAxis() {
        recyclerView.scrollToPosition(mEntries.size() - 1);
        int lastVisiblePosition = mEntries.size() - 1;
        int firstVisiblePosition = lastVisiblePosition - displayNumber + 1;
        List<BarEntry> visibleEntries = mEntries.subList(firstVisiblePosition, lastVisiblePosition);
        mYAxis = YAxis.getYAxis(mBarChartAttrs, DecimalUtil.getTheMaxNumber(visibleEntries));
        mBarChartAdapter.notifyDataSetChanged();
        mItemDecoration.setYAxis(mYAxis);
        displayDateAndStep(visibleEntries);
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
                if (mBarChartAttrs.enableScrollToScale) {
                    scrollToScale(recyclerView, createDistanceCompare(recyclerView));
                } else {
                    //微调
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
                if (mBarChartAttrs.enableScrollToScale) {
                    //微调
                    scrollToScale(recyclerView, createDistanceCompare(recyclerView));
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
                    if (mBarChartAttrs.enableScrollToScale) {
                        scrollToScale(recyclerView, createDistanceCompare(recyclerView));
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
        float max = DecimalUtil.getTheMaxNumber(displayEntries);
        YAxis yAxis = mYAxis.resetYAxis(mYAxis, max);
        if (null != yAxis){
            mYAxis = yAxis;
            mItemDecoration.setYAxis(mYAxis);
            recyclerView.invalidate();
        }
        //todo 调试显示用的
        displayDateAndStep(displayEntries);
    }

    private DistanceCompare createDistanceCompare(RecyclerView recyclerView){
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        BarEntry barEntry = mEntries.get(lastVisibleItemPosition);
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        if (mType == VIEW_MONTH) {
            distanceCompare = TimeUtil.createMonthDistance(barEntry.localDate);
        } else if (mType == VIEW_WEEK) {
            distanceCompare = TimeUtil.createWeekDistance(barEntry.localDate);
        } else if (mType == VIEW_DAY) {
            distanceCompare = TimeUtil.createDayDistance(barEntry);
        } else if (mType == VIEW_YEAR) {
            distanceCompare = TimeUtil.createYearDistance(barEntry.localDate);
        }
        return distanceCompare;
    }


    //滑动到附近的刻度线
    private void scrollToScale(RecyclerView recyclerView, DistanceCompare distanceCompare) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int endOfPosition;

        int transactionType = 0;
        if (distanceCompare.distanceLeft < distanceCompare.distanceRight) {//左间距 小于 右间距
            transactionType = 1;
            endOfPosition = lastVisibleItemPosition - distanceCompare.distanceLeft + 1;
            if (lastVisibleItemPosition + distanceCompare.distanceRight > mEntries.size()) {//右尽头
                transactionType = 0;
                endOfPosition = mEntries.size() - 1;
            }
        } else {
            endOfPosition = lastVisibleItemPosition + distanceCompare.distanceRight;
            if (lastVisibleItemPosition - displayNumber <= 0) {//左尽头
                transactionType = 1;
                endOfPosition = displayNumber;
            }
        }

        if (transactionType == 1 && endOfPosition <= displayNumber) {//左移到头
            recyclerView.scrollToPosition(0);
            lastVisibleItemPosition = displayNumber;
        } else if (transactionType == 1) {
            recyclerView.scrollToPosition(endOfPosition - displayNumber);
            lastVisibleItemPosition = endOfPosition;
        }

        if (transactionType == 0 && endOfPosition >= mEntries.size() - 1) {//右边界，lastVisibleItemPosition 保持不变
            recyclerView.scrollToPosition(mEntries.size() - 1);
            lastVisibleItemPosition = mEntries.size() - 1;
        } else if (transactionType == 0) {
            recyclerView.scrollToPosition(endOfPosition);
            lastVisibleItemPosition = endOfPosition;
        }

        int firstVisibleItemPosition = lastVisibleItemPosition - displayNumber;
        List<BarEntry> visibleEntries = mEntries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
        float max = DecimalUtil.getTheMaxNumber(visibleEntries);

        YAxis yAxis = mYAxis.resetYAxis(mYAxis, max);
        if (null != yAxis){
            mYAxis = yAxis;
            mItemDecoration.setYAxis(mYAxis);
        }

//        recyclerView.invalidate();
        displayDateAndStep(visibleEntries);
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
                if (position == VIEW_DAY) {// 创建 月视图的数据
                    displayNumber = 25;
                    bindBarChartList(displayNumber, TestData.createDayEntries(TimeUtil.changZeroOfTheDay(LocalDate.now()), displayNumber, mEntries.size()), VIEW_DAY);
                    mXAxis.setValueFormatter(new XAxisDayFormatter());
                    mItemDecoration.setXAxis(mXAxis);
                } else if (position == VIEW_WEEK) {//创建Week视图的数据
                    displayNumber = 8;
                    bindBarChartList(displayNumber, TestData.createWeekEntries(LocalDate.now(), displayNumber, mEntries.size()), VIEW_WEEK);
                    mXAxis.setValueFormatter(new XAxisWeekFormatter());
                    mItemDecoration.setXAxis(mXAxis);

                } else if (position == VIEW_MONTH) {//创建Month视图的数据
                    displayNumber = 32;
                    bindBarChartList(displayNumber, TestData.getMonthEntries(LocalDate.now(), displayNumber, mEntries.size()), VIEW_MONTH);
                    mXAxis.setValueFormatter(new XAxisMonthFormatter(BarChartActivity.this));
                    mItemDecoration.setXAxis(mXAxis);

                } else if (position == VIEW_YEAR) {//创建Year视图的数据
                    displayNumber = 13;
                    bindBarChartList(displayNumber, TestData.createYearEntries(LocalDate.now(), displayNumber, mEntries.size()), VIEW_YEAR);
                    mXAxis.setValueFormatter(new XAxisYearFormatter());
                    mItemDecoration.setXAxis(mXAxis);
                }
                reSizeYAxis();
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mTabLayout.setCurrentTab(0);
    }

    private void bindBarChartList(int displayNumber, List<BarEntry> entries, int type){
        mEntries.clear();
        mType = type;
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mEntries.addAll(0, entries);
        mBarChartAdapter.setXAxis(mXAxis);
    }


    private void displayDateAndStep(List<BarEntry> displayEntries) {
        //todo 调试显示用的
        BarEntry leftBarEntry = displayEntries.get(0);
        BarEntry rightBarEntry = displayEntries.get(displayEntries.size() - 1);
        txtLeftLocalDate.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));
        txtRightLocalDate.setText(TimeUtil.getDateStr(rightBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));

        if (mType == VIEW_MONTH) {
            String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日");
            String patternStr = "yyyy年MM月dd日";
            if (TimeUtil.isSameMonth(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月"));
            } else if (TimeUtil.isSameYear(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                patternStr = "MM月dd日";
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
                String connectStr = "至";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            } else {
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
                String connectStr = "至";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            }
        } else if (mType == VIEW_WEEK) {
            String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日");
            String patternStr = "yyyy年MM月dd日";
            if (TimeUtil.isSameMonth(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                patternStr = "dd日";
            } else if (TimeUtil.isSameYear(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                patternStr = "MM月dd日";
            }
            String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
            String connectStr = "至";
            textTitle.setText(beginDateStr + connectStr + endDateStr);
        } else if (mType == VIEW_DAY) {
            String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日 HH:mm");
            String patternStr = "yyyy年MM月dd日 HH:mm";
            if (TimeUtil.isTheSameDay(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日"));
            } else {
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
                String connectStr = " - ";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            }
        } else if (mType == VIEW_YEAR) {
            if (TimeUtil.isSameYear(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
                textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年"));
            } else {
                String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy/MM/dd");
                String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, "yyyy/MM/dd");
                String connectStr = " -- ";
                textTitle.setText(beginDateStr + connectStr + endDateStr);
            }
        }

        long count = 0;
        for (int i = 0; i < displayEntries.size(); i++) {
            BarEntry entry = displayEntries.get(i);
            count += entry.getY();
        }
        int averageStep = (int) (count / displayEntries.size());
        String childStr = DecimalUtil.addComma(Integer.toString(averageStep));
        String parentStr = String.format(getString(R.string.str_count_step), childStr);
        SpannableStringBuilder spannable = TextUtil.getSpannableStr(this, parentStr, childStr, 24);
        txtCountStep.setText(spannable);
    }

}
