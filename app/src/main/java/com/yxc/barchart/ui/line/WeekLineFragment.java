
package com.yxc.barchart.ui.line;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxc.barchart.R;
import com.yxc.barchart.RateTestData;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisWeekFormatter;
import com.yxc.barchart.ui.base.BaseChartFragment;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.barchart.BarChartAdapter;
import com.yxc.chartlib.barchart.BarChartRecyclerView;
import com.yxc.chartlib.barchart.SpeedRatioLinearLayoutManager;
import com.yxc.chartlib.barchart.itemdecoration.BezierChartItemDecoration;
import com.yxc.chartlib.barchart.itemdecoration.LineChartItemDecoration;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.listener.RecyclerItemGestureListener;
import com.yxc.chartlib.listener.SimpleItemGestureListener;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.util.DecimalUtil;
import com.yxc.chartlib.view.CustomAnimatedDecorator;
import com.yxc.commonlib.util.TextUtil;
import com.yxc.commonlib.util.TimeUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WeekLineFragment extends BaseChartFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    BarChartRecyclerView recyclerView;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    TextView textTitle;
    TextView txtCountStep;

    RelativeLayout rlTitle;

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

    RecyclerItemGestureListener mItemGestureListener;

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
        View view = View.inflate(getActivity(), R.layout.fragment_week_line, null);
        initView(view);
        initData();
        reSizeYAxis();
        setListener();
        return view;
    }


    private void initView(View view) {
        rlTitle = view.findViewById(R.id.rl_title);
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
//      mItemDecoration.setBarChartValueFormatter(new BarChartValueFormatter(){
//            @Override
//            public String getBarLabel(BarEntry barEntry) {
//                return TimeUtil.getDateStr(barEntry.timestamp, "MM-dd");
//            }
//        });
//        mItemDecoration.setChartValueMarkFormatter(new ChartValueMarkFormatter() {
//            @Override
//            public String getBarLabel(BarEntry barEntry) {
//                String childStr = super.getBarLabel(barEntry);
//                String resultStr = TimeUtil.getDateStr(barEntry.timestamp, "yyyy/MM/dd") + " | " + childStr;
//                return barEntry.getY() > 0 ? resultStr : "";
//            }
//        });

        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis, mBarChartAttrs);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);

        currentLocalDate = TimeUtil.getLastDayOfThisWeek(LocalDate.now());
        List<BarEntry> barEntries = RateTestData.createWeekEntries(currentLocalDate.plusDays(preEntries),
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
        if (yAxis != null) {
            mYAxis = yAxis;
            mItemDecoration.setYAxis(mYAxis);
            mBarChartAdapter.setYAxis(mYAxis);
        }
        displayDateAndStep(visibleEntries);
    }

    //设置RecyclerView的监听
    private void setListener() {
        mItemGestureListener = new RecyclerItemGestureListener(getActivity(), recyclerView,
                new SimpleItemGestureListener() {
                    boolean isRightScroll;

                    @Override
                    public void onItemSelected(BarEntry barEntry, int position) {
                        if (null == barEntry || !barEntry.isSelected()) {
                            rlTitle.setVisibility(View.VISIBLE);
                        } else {
                            rlTitle.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            if (recyclerView.canScrollHorizontally(1) && isRightScroll) {//加载更多
                                List<BarEntry> entries = RateTestData.createWeekEntries(currentLocalDate, displayNumber, mEntries.size());
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
                });
        recyclerView.addOnItemTouchListener(mItemGestureListener);
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


        displayTitle(leftBarEntry, rightBarEntry);

        String childStr = DecimalUtil.getAverageStepStr(displayEntries);
        String parentStr = String.format(getString(R.string.str_count_step), childStr);
        SpannableStringBuilder spannable = TextUtil.getSpannableStr(getActivity(), parentStr, childStr, 24);
        txtCountStep.setText(spannable);
    }

    private void displayTitle(BarEntry leftBarEntry, BarEntry rightBarEntry) {
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
    }


    @Override
    public void onGlobalLayout() {
        HashMap<Integer, CustomAnimatedDecorator> map = new HashMap<>();
        for (int i = 0; i < recyclerView.getChildCount(); i++) {

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

    @Override
    public void resetSelectedEntry() {
        if (mItemGestureListener != null){
            Log.d("DayFragment", " visibleHint" );
            mItemGestureListener.resetSelectedBarEntry();
            rlTitle.setVisibility(View.VISIBLE);
        }
    }
}
