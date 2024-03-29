
package com.yxc.barchart.ui.hrm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.barchart.R;
import com.yxc.barchart.RateTestData;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisHrmFormatter;
import com.yxc.barchart.ui.line.BaseLineFragment;
import com.yxc.barchart.util.BaseFileUtil;
import com.yxc.barchart.util.ScrollHorizontalCapture;
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
import com.yxc.chartlib.recyclerchart.view.LineChartRecyclerView;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HrmDayFragment extends BaseLineFragment {

    LineChartRecyclerView recyclerView;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    Button captureBtn;
    ImageView imgCaptureView;

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
        setListener();
        return view;
    }


    private void initView(View view) {
        txtLeftLocalDate = view.findViewById(R.id.txt_left_local_date);
        txtRightLocalDate = view.findViewById(R.id.txt_right_local_date);
        recyclerView = view.findViewById(R.id.line_recycler);
        captureBtn = view.findViewById(R.id.captureViewBtn);
        imgCaptureView = view.findViewById(R.id.imgCaptureView);
        mBarChartAttrs = recyclerView.mAttrs;
    }

    private int computeDisplayNumber() {
        float itemHeight = recyclerView.contentHeight() / 6.0f;
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

                mItemDecoration = new HrmChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
                recyclerView.addItemDecoration(mItemDecoration);
                mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis, mBarChartAttrs);
                recyclerView.setAdapter(mBarChartAdapter);
                recyclerView.setLayoutManager(layoutManager);

                currentTimestamp = TimeDateUtil.changZeroOfTheDay(LocalDate.now().plusDays(1));

                List<BarEntry> barEntries = RateTestData.createHrmEntries(mBarChartAttrs, currentTimestamp, 5 * displayNumber, mEntries.size());

                bindBarChartList(barEntries);
                currentTimestamp = currentTimestamp - TimeDateUtil.TIME_HOUR * displayNumber * 5;
                setXAxis(displayNumber);
                reSizeYAxis();
            }
        });
    }

    private void reSizeYAxis() {
        recyclerView.scrollToPosition(0);
        List<BarEntry> visibleEntries = mEntries.subList(preEntrySize, displayNumber);
        YAxis yAxis = mYAxis.resetHrmYAxis(mYAxis, DecimalUtil.getTheMaxNumber(visibleEntries) + 20);
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
//                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                            //加载更多
//                            if (recyclerView.canScrollHorizontally(1) && isRightScroll) {
//                                List<BarEntry> entries = RateTestData.createHrmEntries(mBarChartAttrs, currentTimestamp, displayNumber, mEntries.size());
//                                currentTimestamp = currentTimestamp - displayNumber * TimeDateUtil.TIME_HOUR;
//                                mEntries.addAll(entries);
//                                mBarChartAdapter.notifyDataSetChanged();
//                            }
//                            //回溯
////                            if (mBarChartAttrs.enableScrollToScale) {
////                                int scrollToByDx = ChartComputeUtil.computeScrollByXOffset(recyclerView, displayNumber, mType);
////                                recyclerView.scrollBy(scrollToByDx, 0);
////                            }
//                            //重绘Y轴
//                            resetYAxis(recyclerView);
//                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        isRightScroll = dx < 0;
                    }
                });
        recyclerView.addOnItemTouchListener(mItemGestureListener);

        String sharePath = BaseFileUtil.getShareDirPath();
        ScrollHorizontalCapture scrollCapture = new ScrollHorizontalCapture(recyclerView, sharePath + File.separator + System.currentTimeMillis() + ".jpg");
        //开始或停止截屏
        captureBtn.setOnClickListener(view -> {
            scrollCapture.toggle();
//           Bitmap captureBitmap = ShareUtil.createHorizontalRecyclerView(getActivity(), recyclerView);
//           imgCaptureView.setImageBitmap(captureBitmap);
        });

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
