
package com.yxc.barchart.ui.hrm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.barchart.EcgTestData;
import com.yxc.barchart.R;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisHrmFormatter;
import com.yxc.barchart.ui.line.BaseLineFragment;
import com.yxc.chartlib.recyclerchart.attrs.LineChartAttrs;
import com.yxc.chartlib.recyclerchart.barchart.BarChartAdapter;
import com.yxc.chartlib.recyclerchart.barchart.SpeedRatioLayoutManager;
import com.yxc.chartlib.recyclerchart.component.XAxis;
import com.yxc.chartlib.recyclerchart.component.YAxis;
import com.yxc.chartlib.recyclerchart.entrys.BarEntry;
import com.yxc.chartlib.recyclerchart.entrys.EcgEntry;
import com.yxc.chartlib.recyclerchart.formatter.ValueFormatter;
import com.yxc.chartlib.recyclerchart.itemdecoration.EcgChartItemDecoration;
import com.yxc.chartlib.recyclerchart.listener.RecyclerItemGestureListener;
import com.yxc.chartlib.recyclerchart.listener.SimpleItemGestureListener;
import com.yxc.chartlib.recyclerchart.util.DecimalUtil;
import com.yxc.chartlib.recyclerchart.view.LineChartRecyclerView;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class EcgDayFragment extends BaseLineFragment {

    LineChartRecyclerView recyclerView;

    BarChartAdapter mBarChartAdapter;
    List<EcgEntry> mEntries;
    EcgChartItemDecoration mItemDecoration;

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
        View view = View.inflate(getActivity(), R.layout.fragment_day_ecg, null);
        initView(view);
        initData();
        setListener();
        return view;
    }


    private void initView(View view) {
        recyclerView = view.findViewById(R.id.line_recycler);
        mBarChartAttrs = recyclerView.mAttrs;
    }

    private int computeDisplayNumber() {
        float itemHeight = recyclerView.contentHeight() / 8.0f;
        int displayNumber = (int) ((recyclerView.contentWidth() / itemHeight) * 5);
        return displayNumber;
    }

    private void initData() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                displayNumber = computeDisplayNumber();
                mType = TestData.VIEW_DAY;
                valueFormatter = new XAxisHrmFormatter();
                mEntries = new ArrayList<>();

                SpeedRatioLayoutManager layoutManager = new SpeedRatioLayoutManager(getActivity(), mBarChartAttrs);
                mYAxis = new YAxis(mBarChartAttrs);
                mXAxis = new XAxis(mBarChartAttrs, displayNumber, valueFormatter);

                mItemDecoration = new EcgChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
                recyclerView.addItemDecoration(mItemDecoration);
                mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis, mBarChartAttrs);
                recyclerView.setAdapter(mBarChartAdapter);
                recyclerView.setLayoutManager(layoutManager);

                currentTimestamp = TimeDateUtil.changZeroOfTheDay(LocalDate.now().plusDays(1));

                List<EcgEntry> barEntries = EcgTestData.createEcgEntries(mBarChartAttrs, currentTimestamp,  displayNumber + 20, mEntries.size());
                bindBarChartList(barEntries);
                currentTimestamp = currentTimestamp - TimeDateUtil.TIME_HOUR *  (displayNumber + 20);
                setXAxis(displayNumber);
                reSizeYAxis();
            }
        });
    }

    private void reSizeYAxis() {
        recyclerView.scrollToPosition(0);
        YAxis yAxis = mYAxis.resetEcgYAxis(mYAxis);
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
                    @Override
                    public void onItemSelected(BarEntry barEntry, int position) {
                    }
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        // 当不滚动时
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    }
                });
        recyclerView.addOnItemTouchListener(mItemGestureListener);
    }


    private void bindBarChartList(List<EcgEntry> entries) {
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
