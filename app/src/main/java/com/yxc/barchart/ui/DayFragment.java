
package com.yxc.barchart.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yxc.barchart.BaseFragment;
import com.yxc.barchart.R;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisDayFormatter;
import com.yxc.barchartlib.component.DistanceCompare;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.ReLocationUtil;
import com.yxc.barchartlib.util.TextUtil;
import com.yxc.barchartlib.util.TimeUtil;
import com.yxc.barchartlib.view.BarChartAdapter;
import com.yxc.barchartlib.view.BarChartItemDecoration;
import com.yxc.barchartlib.view.BarChartRecyclerView;
import com.yxc.barchartlib.view.SpeedRatioLinearLayoutManager;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayFragment extends BaseFragment {

    BarChartRecyclerView recyclerView;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    TextView textTitle;
    TextView txtCountStep;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    BarChartItemDecoration mItemDecoration;
    YAxis mYAxis;
    XAxis mXAxis;
    ValueFormatter valueFormatter;
    public int mType;
    private int displayNumber;
    private BarChartAttrs mBarChartAttrs;
    long currentTimestamp;

    //防止 Fragment重叠
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_day_step, null);
        initView(view);
        displayNumber = 24;
        mType = TestData.VIEW_DAY;
        valueFormatter = new XAxisDayFormatter();

        initData(displayNumber, valueFormatter);
        currentTimestamp = TimeUtil.changZeroOfTheDay(LocalDate.now());
        bindBarChartList(TestData.createDayEntries(currentTimestamp, 3* displayNumber, mEntries.size()));
        currentTimestamp = currentTimestamp - TimeUtil.TIME_HOUR * displayNumber * 3;
        setXAxis(displayNumber);
        reSizeYAxis();
        setListener(mType, displayNumber);
        return view;
    }


    private void initView(View view) {
        txtLeftLocalDate = view.findViewById(R.id.txt_left_local_date);
        txtRightLocalDate = view.findViewById(R.id.txt_right_local_date);
        textTitle = view.findViewById(R.id.txt_layout);
        txtCountStep = view.findViewById(R.id.txt_count_Step);
        recyclerView = view.findViewById(R.id.recycler);

        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData(int displayNumber, ValueFormatter valueFormatter) {
        mEntries = new ArrayList<>();
        SpeedRatioLinearLayoutManager layoutManager = new SpeedRatioLinearLayoutManager(getActivity(), mBarChartAttrs);
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber, valueFormatter);
        mItemDecoration = new BarChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

    private void reSizeYAxis() {
        recyclerView.scrollToPosition(0);
        List<BarEntry> visibleEntries = mEntries.subList(0, displayNumber + 1);
        mYAxis = YAxis.getYAxis(mBarChartAttrs, DecimalUtil.getTheMaxNumber(visibleEntries));
        mBarChartAdapter.notifyDataSetChanged();
        mItemDecoration.setYAxis(mYAxis);
        displayDateAndStep(visibleEntries);
    }


    //滑动监听
    private void setListener(final int type, final int displayNumber) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private boolean isRightScroll;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //加载更多
                    if (recyclerView.canScrollHorizontally(1) && isRightScroll) {
                        List<BarEntry> entries = TestData.createDayEntries(currentTimestamp, displayNumber, mEntries.size());
                        currentTimestamp = currentTimestamp - displayNumber * TimeUtil.TIME_HOUR;
                        mEntries.addAll(entries);
                        mBarChartAdapter.notifyDataSetChanged();
                    }
                    Log.d("ScrollListener" , "Day idle resizeYAxis" + " time: " +
                            TimeUtil.getDateStr(System.currentTimeMillis()/1000, "mm-ss"));
                    //回溯
                    if (mBarChartAttrs.enableScrollToScale) {
                        DistanceCompare distanceCompare = ReLocationUtil.findNearFirstType(recyclerView, displayNumber, TestData.VIEW_DAY);
                        recyclerView.scrollToPosition(distanceCompare.position);
                        Log.d("ScrollListener" , "scrollToPosition " + " time: " +
                                TimeUtil.getDateStr(System.currentTimeMillis()/1000, "mm-ss"));
                    } else {
                        ReLocationUtil.microRelation(recyclerView);
                    }

                    //重绘Y轴
                    resetYAxis(recyclerView);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //判断左滑，右滑时，ScrollView的位置不一样。
                if (dx < 0){
                    isRightScroll = true;
                }else{
                    isRightScroll = false;
                }
            }
        });
    }

    //重新设置Y坐标
    private void resetYAxis(RecyclerView recyclerView) {
        float yAxisMaximum = 0;
        HashMap<Float, List<BarEntry>> map = ReLocationUtil.getVisibleEntries(recyclerView);
        for (Map.Entry<Float, List<BarEntry>> entry : map.entrySet()) {
            yAxisMaximum = entry.getKey();
            displayDateAndStep(entry.getValue());
            break;
        }
        mYAxis = YAxis.getYAxis(mBarChartAttrs, yAxisMaximum);
        mItemDecoration.setYAxis(mYAxis);
    }

    private void bindBarChartList(List<BarEntry> entries) {
        if (null == mEntries) {
            mEntries = new ArrayList<>();
        } else {
            mEntries.clear();
        }
        mEntries.addAll(entries);
    }

    private void setXAxis(int displayNumber) {
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mBarChartAdapter.setXAxis(mXAxis);
    }


    private void displayDateAndStep(List<BarEntry> displayEntries) {
        mBarChartAdapter.setYAxis(mYAxis);
        BarEntry rightBarEntry = displayEntries.get(0);
        BarEntry leftBarEntry = displayEntries.get(displayEntries.size() - 1);
        txtLeftLocalDate.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));
        txtRightLocalDate.setText(TimeUtil.getDateStr(rightBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));

        String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日 HH:mm");
        String patternStr = "yyyy年MM月dd日 HH:mm";
        if (TimeUtil.isTheSameDay(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
            textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年MM月dd日"));
        } else {
            String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, patternStr);
            String connectStr = " - ";
            textTitle.setText(beginDateStr + connectStr + endDateStr);
        }

        long count = 0;
        for (int i = 0; i < displayEntries.size(); i++) {
            BarEntry entry = displayEntries.get(i);
            count += entry.getY();
        }
        int averageStep = (int) (count / displayEntries.size());
        String childStr = DecimalUtil.addComma(Integer.toString(averageStep));
        String parentStr = String.format(getString(R.string.str_count_step), childStr);
        SpannableStringBuilder spannable = TextUtil.getSpannableStr(getActivity(), parentStr, childStr, 24);
        txtCountStep.setText(spannable);
    }

}
