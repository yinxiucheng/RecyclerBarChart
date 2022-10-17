
package com.yxc.barchart.ui.hrm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.barchart.R;
import com.yxc.barchart.RateTestData;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisDayFormatter;
import com.yxc.barchart.ui.line.BaseLineFragment;
import com.yxc.chartlib.recyclerchart.attrs.LineChartAttrs;
import com.yxc.chartlib.recyclerchart.barchart.BarChartAdapter;
import com.yxc.chartlib.recyclerchart.barchart.SpeedRatioLayoutManager;
import com.yxc.chartlib.recyclerchart.component.XAxis;
import com.yxc.chartlib.recyclerchart.component.YAxis;
import com.yxc.chartlib.recyclerchart.entrys.BarEntry;
import com.yxc.chartlib.recyclerchart.entrys.YAxisMaxEntries;
import com.yxc.chartlib.recyclerchart.formatter.ValueFormatter;
import com.yxc.chartlib.recyclerchart.itemdecoration.HrmChartItemDecoration;
import com.yxc.chartlib.recyclerchart.listener.RecyclerItemGestureListener;
import com.yxc.chartlib.recyclerchart.listener.SimpleItemGestureListener;
import com.yxc.chartlib.recyclerchart.util.ChartComputeUtil;
import com.yxc.chartlib.recyclerchart.util.DecimalUtil;
import com.yxc.chartlib.recyclerchart.view.CustomAnimatedDecorator;
import com.yxc.chartlib.recyclerchart.view.LineChartRecyclerView;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HrmDayFragment extends BaseLineFragment implements ViewTreeObserver.OnGlobalLayoutListener {

    LineChartRecyclerView recyclerView;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    HrmChartItemDecoration mItemDecoration;

    RecyclerItemGestureListener mItemGestureListener;

    YAxis mYAxis;
    XAxis mXAxis;
    ValueFormatter valueFormatter;
    public int mType;
    private int displayNumber;
    private LineChartAttrs mBarChartAttrs;
    long currentTimestamp;
    int preEntrySize = 0;

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
        View view = View.inflate(getActivity(), R.layout.fragment_day_hrm, null);
        initView(view);
        initData();
        reSizeYAxis();
        setListener();
        return view;
    }


    private void initView(View view) {
        txtLeftLocalDate = view.findViewById(R.id.txt_left_local_date);
        txtRightLocalDate = view.findViewById(R.id.txt_right_local_date);
        recyclerView = view.findViewById(R.id.line_recycler);
        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData() {
        displayNumber = mBarChartAttrs.displayNumbers;
        mType = TestData.VIEW_DAY;
        valueFormatter = new XAxisDayFormatter();
        mEntries = new ArrayList<>();

        SpeedRatioLayoutManager layoutManager = new SpeedRatioLayoutManager(getActivity(), mBarChartAttrs);
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber, valueFormatter);

        mItemDecoration = new HrmChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis, mBarChartAttrs);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);

        currentTimestamp = TimeDateUtil.changZeroOfTheDay(LocalDate.now().plusDays(1));

        List<BarEntry> preEntries = RateTestData.createHrmEntries(mBarChartAttrs,
                currentTimestamp + preEntrySize * TimeDateUtil.TIME_HOUR, preEntrySize, mEntries.size(), true);
        List<BarEntry> barEntries = RateTestData.createHrmEntries(mBarChartAttrs, currentTimestamp,
                10 * displayNumber, mEntries.size(), false);

        barEntries.addAll(0, preEntries);
        bindBarChartList(barEntries);
        currentTimestamp = currentTimestamp - TimeDateUtil.TIME_HOUR * displayNumber * 10;
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
        mBarChartAdapter.setYAxis(mYAxis);
    }

    //滑动监听
    private void setListener() {
        mItemGestureListener = new RecyclerItemGestureListener(getActivity(), recyclerView,
                new SimpleItemGestureListener() {
                    private boolean isRightScroll;

                    @Override
                    public void onItemSelected(BarEntry barEntry, int position) {
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        // 当不滚动时
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            //加载更多
                            if (recyclerView.canScrollHorizontally(1) && isRightScroll) {
                                List<BarEntry> entries = RateTestData.createHrmEntries(mBarChartAttrs, currentTimestamp, displayNumber, mEntries.size(), false);
                                currentTimestamp = currentTimestamp - displayNumber * TimeDateUtil.TIME_HOUR;
                                mEntries.addAll(entries);
                                mBarChartAdapter.notifyDataSetChanged();
                            }
                            //回溯
                            if (mBarChartAttrs.enableScrollToScale) {
                                int scrollToByDx = ChartComputeUtil.computeScrollByXOffset(recyclerView, displayNumber, mType);
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
        YAxisMaxEntries yAxisMaxEntries = ChartComputeUtil.getVisibleEntries(recyclerView);
        setVisibleEntries(yAxisMaxEntries.visibleEntries);
        mYAxis = YAxis.getYAxis(mBarChartAttrs, yAxisMaxEntries.yAxisMaximum);
        YAxis yAxis = YAxis.getYAxis(mBarChartAttrs, yAxisMaxEntries.yAxisMaximum);
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

    @Override
    public void onGlobalLayout() {
        HashMap<Integer, CustomAnimatedDecorator> map = new HashMap<>();
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View child = recyclerView.getChildAt(i);
            int position = recyclerView.getChildAdapterPosition(child);
            BarEntry barEntry = mEntries.get(position);
            Log.d("DayFragment", " barEntry, localDate" + barEntry.localDate);
            float realBottomPadding = recyclerView.getPaddingBottom() + mBarChartAttrs.contentPaddingBottom;
            float realTopPadding = recyclerView.getPaddingTop() + mBarChartAttrs.contentPaddingTop;
            float realContentHeight = recyclerView.getHeight() - realBottomPadding - realTopPadding;

            float width = child.getWidth();
            float barSpaceWidth = width * mBarChartAttrs.barSpace;
            float barChartWidth = width - barSpaceWidth;//柱子的宽度
            float height = barEntry.getY() / mYAxis.getAxisMaximum() * realContentHeight;

            CustomAnimatedDecorator drawable = new CustomAnimatedDecorator(barChartWidth, realContentHeight,
                    0, realContentHeight - height);
            map.put(position, drawable);
        }
    }

    @Override
    public void resetSelectedEntry() {
        if (mItemGestureListener != null){
            Log.d("DayFragment", " visibleHint" );
            mItemGestureListener.resetSelectedBarEntry();
        }
    }

    @Override
    public void displayDateAndRate() {

    }

    @Override
    public void scrollToCurrentCycle() {

    }
}
