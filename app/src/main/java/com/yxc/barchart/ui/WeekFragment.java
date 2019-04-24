
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
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.yxc.barchart.BaseFragment;
import com.yxc.barchart.R;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.ChartValueMarkFormatter;
import com.yxc.barchart.formatter.XAxisWeekFormatter;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.itemdecoration.LineChartItemDecoration;
import com.yxc.barchartlib.listener.RecyclerItemGestureListener;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.ChartComputeUtil;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.TextUtil;
import com.yxc.barchartlib.util.TimeUtil;
import com.yxc.barchartlib.view.BarChartAdapter;
import com.yxc.barchartlib.view.BarChartRecyclerView;
import com.yxc.barchartlib.view.CustomAnimatedDecorator;
import com.yxc.barchartlib.view.SpeedRatioLinearLayoutManager;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeekFragment extends BaseFragment implements ViewTreeObserver.OnGlobalLayoutListener{

    BarChartRecyclerView recyclerView;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    TextView textTitle;
    TextView txtCountStep;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    LineChartItemDecoration mItemDecoration;
    YAxis mYAxis;
    XAxis mXAxis;
    ValueFormatter valueFormatter;

    private int displayNumber;
    private BarChartAttrs mBarChartAttrs;
    private LocalDate currentLocalDate;

    private int preEntries = 0;

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
        View view = View.inflate(getActivity(), R.layout.fragment_week_step, null);
        initView(view);
        initData();
        reSizeYAxis();
        setListener();
        return view;
    }


    private void initView(View view) {
        txtLeftLocalDate = view.findViewById(R.id.txt_left_local_date);
        txtRightLocalDate = view.findViewById(R.id.txt_right_local_date);
        textTitle = view.findViewById(R.id.txt_layout);
        txtCountStep = view.findViewById(R.id.txt_count_Step);
        recyclerView = view.findViewById(R.id.recycler);
//        recyclerView.setScrollingTouchSlop(RecyclerView.TOUCH_SLOP_PAGING);

        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData() {
        displayNumber = mBarChartAttrs.displayNumbers;
        valueFormatter = new XAxisWeekFormatter();

        mEntries = new ArrayList<>();
        SpeedRatioLinearLayoutManager layoutManager = new SpeedRatioLinearLayoutManager(getActivity(), mBarChartAttrs);
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mXAxis.setValueFormatter(valueFormatter);
        mItemDecoration = new LineChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
//        mItemDecoration.setBarChartValueFormatter(new BarChartValueFormatter(){
//            @Override
//            public String getBarLabel(BarEntry barEntry) {
//                return TimeUtil.getDateStr(barEntry.timestamp, "MM-dd");
//            }
//        });
        mItemDecoration.setChartValueMarkFormatter(new ChartValueMarkFormatter() {
            @Override
            public String getBarLabel(BarEntry barEntry) {
                String childStr = super.getBarLabel(barEntry);
                String resultStr = TimeUtil.getDateStr(barEntry.timestamp, "yyyy/MM/dd") + " | " + childStr;
                return barEntry.getY() > 0 ? resultStr : "";
            }
        });
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mYAxis, mBarChartAttrs);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);

        currentLocalDate = TimeUtil.getLastDayOfThisWeek(LocalDate.now());
        List<BarEntry> barEntries = TestData.createWeekEntries(currentLocalDate.plusDays(preEntries),
                preEntries + 5 * displayNumber, mEntries.size());
        bindBarChartList(barEntries);
        currentLocalDate = currentLocalDate.minusDays(5 * displayNumber);

        setXAxis(displayNumber);
    }


    private void reSizeYAxis() {
        recyclerView.scrollToPosition(preEntries);
        List<BarEntry> visibleEntries = mEntries.subList(preEntries, preEntries + displayNumber + 1);
        YAxis yAxis = mYAxis.resetYAxis(mYAxis, DecimalUtil.getTheMaxNumber(visibleEntries));
        mBarChartAdapter.notifyDataSetChanged();
        if (yAxis != null){
            mYAxis = yAxis;
            mItemDecoration.setYAxis(mYAxis);
            mBarChartAdapter.setYAxis(mYAxis);
        }
        displayDateAndStep(visibleEntries);
    }

    //设置RecyclerView的监听
    private void setListener() {

        recyclerView.addOnItemTouchListener(new RecyclerItemGestureListener(getActivity(), recyclerView,
                new RecyclerItemGestureListener.OnItemGestureListener() {
                    boolean isRightScroll;
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("ItemTouch", "weekFragment onItemClick");
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Log.d("ItemTouch", "weekFragment onLongItemClick");
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (recyclerView.canScrollHorizontally(1) && isRightScroll) {//加载更多
                                List<BarEntry> entries = TestData.createWeekEntries(currentLocalDate, displayNumber, mEntries.size());
                                currentLocalDate = currentLocalDate.minusDays(displayNumber);
                                mEntries.addAll(entries);
                                mBarChartAdapter.setEntries(mEntries);
                            }

                            if (mBarChartAttrs.enableScrollToScale) {
                                int scrollByDx = ChartComputeUtil.computeScrollByXOffset(recyclerView, displayNumber, TestData.VIEW_WEEK);
                                recyclerView.scrollBy(scrollByDx, 0);
                            }

                            resetYAxis(recyclerView);
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        if (dx < 0) {
                            isRightScroll = true;
                        } else {
                            isRightScroll = false;
                        }
                    }
                }));
//        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    //重新设置Y坐标
    private void resetYAxis(RecyclerView recyclerView) {
        float yAxisMaximum = 0;
        HashMap<Float, List<BarEntry>> map = ChartComputeUtil.getVisibleEntries(recyclerView);

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
        BarEntry rightBarEntry = displayEntries.get(0);
        BarEntry leftBarEntry = displayEntries.get(displayEntries.size() - 1);
        txtLeftLocalDate.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));
        txtRightLocalDate.setText(TimeUtil.getDateStr(rightBarEntry.timestamp, "yyyy-MM-dd HH:mm:ss"));
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
        String childStr = DecimalUtil.getAverageStepStr(displayEntries);
        String parentStr = String.format(getString(R.string.str_count_step), childStr);
        SpannableStringBuilder spannable = TextUtil.getSpannableStr(getActivity(), parentStr, childStr, 24);
        txtCountStep.setText(spannable);
    }


    @Override
    public void onGlobalLayout() {
        HashMap<Integer, CustomAnimatedDecorator> map = new HashMap<>();
        for (int i = 0; i< recyclerView.getChildCount(); i++){

            View child = recyclerView.getChildAt(i);
            int position = recyclerView.getChildAdapterPosition(child);
            BarEntry barEntry = mEntries.get(position);
            float realBottomPadding = recyclerView.getPaddingBottom() + mBarChartAttrs.contentPaddingBottom;
            float realTopPadding = recyclerView.getPaddingTop() + mBarChartAttrs.maxYAxisPaddingTop;
            float realContentHeight = recyclerView.getHeight() - realBottomPadding - realTopPadding;
            Log.d("WeekFragment", " barEntry, localDate" + barEntry.localDate);
            float width = child.getWidth();
            float barSpaceWidth = width * mBarChartAttrs.barSpace;
            float barChartWidth = width - barSpaceWidth;//柱子的宽度
            float height = barEntry.getY() / mYAxis.getAxisMaximum() * realContentHeight;

            CustomAnimatedDecorator drawable = new CustomAnimatedDecorator(barChartWidth, realContentHeight,
                    0, realContentHeight - height);
            map.put(position, drawable);
        }
//        mItemDecoration.setAnimatorMap(map);
//        recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }
}
