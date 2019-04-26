
package com.yxc.barchart.ui.step;

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
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.DayHighLightMarkValueFormatter;
import com.yxc.barchart.formatter.XAxisDayFormatter;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.barchart.BarChartAdapter;
import com.yxc.chartlib.barchart.BarChartRecyclerView;
import com.yxc.chartlib.barchart.SpeedRatioLinearLayoutManager;
import com.yxc.chartlib.barchart.itemdecoration.BarChartItemDecoration;
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

public class StepDayFragment extends BaseStepFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    BarChartRecyclerView recyclerView;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    TextView textTitle;
    TextView txtCountStep;

    RelativeLayout rlTitle;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    BarChartItemDecoration mItemDecoration;

    RecyclerItemGestureListener mItemGestureListener;

    YAxis mYAxis;
    XAxis mXAxis;
    ValueFormatter valueFormatter;
    public int mType;
    private int displayNumber;
    private BarChartAttrs mBarChartAttrs;
    long currentTimestamp;
    int preEntrySize = 4;

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
        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData() {
        displayNumber = mBarChartAttrs.displayNumbers;
        mType = TestData.VIEW_DAY;
        valueFormatter = new XAxisDayFormatter();
        mEntries = new ArrayList<>();

        SpeedRatioLinearLayoutManager layoutManager = new SpeedRatioLinearLayoutManager(getActivity(), mBarChartAttrs);
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber, valueFormatter);

        mItemDecoration = new BarChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
        mItemDecoration.setHighLightValueFormatter(new DayHighLightMarkValueFormatter(0));
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis, mBarChartAttrs);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);

        currentTimestamp = TimeUtil.changZeroOfTheDay(LocalDate.now().plusDays(1));

        List<BarEntry> preEntries = TestData.createDayEntries(mBarChartAttrs,
                currentTimestamp + preEntrySize * TimeUtil.TIME_HOUR, preEntrySize, mEntries.size(), true);

        List<BarEntry> barEntries = TestData.createDayEntries(mBarChartAttrs, currentTimestamp,
                10 * displayNumber, mEntries.size(), false);
        barEntries.addAll(0, preEntries);
        bindBarChartList(barEntries);
        currentTimestamp = currentTimestamp - TimeUtil.TIME_HOUR * displayNumber * 10;
        setXAxis(displayNumber);
    }

    private void reSizeYAxis() {
        recyclerView.scrollToPosition(preEntrySize + 1);
        List<BarEntry> visibleEntries = mEntries.subList(preEntrySize, preEntrySize + displayNumber + 1);
        YAxis yAxis = mYAxis.resetYAxis(mYAxis, DecimalUtil.getTheMaxNumber(visibleEntries));
        mBarChartAdapter.notifyDataSetChanged();
        if (yAxis != null) {
            mYAxis = yAxis;
            mItemDecoration.setYAxis(mYAxis);
            mBarChartAdapter.setYAxis(mYAxis);
        }
        displayDateAndStep(visibleEntries);
    }

    //滑动监听
    private void setListener() {
        mItemGestureListener = new RecyclerItemGestureListener(getActivity(), recyclerView,
                new SimpleItemGestureListener() {
                    private boolean isRightScroll;

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
                        // 当不滚动时
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            //加载更多
                            if (recyclerView.canScrollHorizontally(1) && isRightScroll) {
                                List<BarEntry> entries = TestData.createDayEntries(mBarChartAttrs, currentTimestamp, displayNumber, mEntries.size(), false);
                                currentTimestamp = currentTimestamp - displayNumber * TimeUtil.TIME_HOUR;
                                mEntries.addAll(entries);
                                mBarChartAdapter.notifyDataSetChanged();
                            }
                            //回溯
                            if (mBarChartAttrs.enableScrollToScale) {
                                int scrollToByDx = ChartComputeUtil.computeScrollByXOffset(recyclerView, displayNumber, TestData.VIEW_DAY);
                                recyclerView.scrollBy(scrollToByDx, 0);
                            }
                            //重绘Y轴
                            resetYAxis(recyclerView);
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        isRightScroll = dx < 0;
                    }
                });
        recyclerView.addOnItemTouchListener(mItemGestureListener);
        //recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
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
        YAxis yAxis = YAxis.getYAxis(mBarChartAttrs, yAxisMaximum);
        if (yAxis != null) {
            mYAxis = yAxis;
            mBarChartAdapter.setYAxis(mYAxis);
            mItemDecoration.setYAxis(mYAxis);
        }

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

    @Override
    public void onGlobalLayout() {
        HashMap<Integer, CustomAnimatedDecorator> map = new HashMap<>();
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            int position = recyclerView.getChildAdapterPosition(child);
            BarEntry barEntry = mEntries.get(position);
            Log.d("DayFragment", " barEntry, localDate" + barEntry.localDate);
            float realBottomPadding = recyclerView.getPaddingBottom() + mBarChartAttrs.contentPaddingBottom;
            float realTopPadding = recyclerView.getPaddingTop() + mBarChartAttrs.maxYAxisPaddingTop;
            float realContentHeight = recyclerView.getHeight() - realBottomPadding - realTopPadding;

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
