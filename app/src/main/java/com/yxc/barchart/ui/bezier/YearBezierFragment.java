package com.yxc.barchart.ui.bezier;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yxc.barchart.R;
import com.yxc.barchart.RateTestData;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisYearFormatter;
import com.yxc.barchart.ui.base.BaseChartFragment;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.barchart.BarChartAdapter;
import com.yxc.chartlib.barchart.BarChartRecyclerView;
import com.yxc.chartlib.barchart.SpeedRatioLinearLayoutManager;
import com.yxc.chartlib.barchart.itemdecoration.BezierChartItemDecoration;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.listener.RecyclerItemGestureListener;
import com.yxc.chartlib.listener.SimpleItemGestureListener;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.util.DecimalUtil;
import com.yxc.commonlib.util.TextUtil;
import com.yxc.commonlib.util.TimeUtil;

import org.joda.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearBezierFragment extends BaseChartFragment {

    BarChartRecyclerView recyclerView;
    TextView txtLeftLocalDate;
    TextView txtRightLocalDate;
    TextView textTitle;
    TextView txtCountStep;

    RelativeLayout rlTitle;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    BezierChartItemDecoration mItemDecoration;
    YAxis mYAxis;
    XAxis mXAxis;

    private int displayNumber;
    private BarChartAttrs mBarChartAttrs;
    private LocalDate currentLocalDate;
    private int preEntrySize = 3;

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
        View view = View.inflate(getActivity(), R.layout.fragment_year_bezier, null);
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
        mEntries = new ArrayList<>();
        SpeedRatioLinearLayoutManager layoutManager = new SpeedRatioLinearLayoutManager(getActivity(), mBarChartAttrs);
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber, new XAxisYearFormatter());
        mItemDecoration = new BezierChartItemDecoration(mYAxis, mXAxis, mBarChartAttrs);
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis, mBarChartAttrs);
        recyclerView.setAdapter(mBarChartAdapter);
        recyclerView.setLayoutManager(layoutManager);
        currentLocalDate = TimeUtil.getLastMonthOfTheYear(LocalDate.now());
        List<BarEntry> barEntries = RateTestData.createYearEntries(currentLocalDate.plusMonths(preEntrySize), preEntrySize + 5 * displayNumber, mEntries.size());
        bindBarChartList(barEntries);
        currentLocalDate = currentLocalDate.minusMonths(displayNumber * 5);
        setXAxis(displayNumber);
    }

    private void reSizeYAxis() {
        recyclerView.scrollToPosition(preEntrySize);
        List<BarEntry> visibleEntries = mEntries.subList(preEntrySize, preEntrySize + displayNumber + 1);
        YAxis yAxis = mYAxis.resetYAxis(mYAxis, DecimalUtil.getTheMaxNumber(visibleEntries));
        mBarChartAdapter.notifyDataSetChanged();
        if (yAxis != null) {
            mYAxis = yAxis;
            mItemDecoration.setYAxis(mYAxis);
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
                            //左滑
                            if (recyclerView.canScrollHorizontally(1) && isRightScroll) {
                                List<BarEntry> entries = RateTestData.createYearEntries(currentLocalDate, displayNumber, mEntries.size());
                                currentLocalDate = currentLocalDate.minusMonths(displayNumber);
                                mEntries.addAll(entries);
                                mBarChartAdapter.notifyDataSetChanged();
                            }

                            if (mBarChartAttrs.enableScrollToScale) {
                                int scrollByDx = ChartComputeUtil.computeScrollByXOffset(recyclerView, displayNumber, TestData.VIEW_YEAR);
                                recyclerView.scrollBy(scrollByDx, 0);
                            }
                            resetYAxis(recyclerView);
                        }
                    }
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        //判断左滑，右滑时，ScrollView的位置不一样。
                        if (dx < 0) {
                            isRightScroll = true;
                        } else {
                            isRightScroll = false;
                        }
                    }
                });
        recyclerView.addOnItemTouchListener(mItemGestureListener);
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

        if (TimeUtil.isSameYear(leftBarEntry.timestamp, rightBarEntry.timestamp)) {
            textTitle.setText(TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy年"));
        } else {
            String beginDateStr = TimeUtil.getDateStr(leftBarEntry.timestamp, "yyyy/MM/dd");
            String endDateStr = TimeUtil.getDateStr(rightBarEntry.timestamp, "yyyy/MM/dd");
            String connectStr = " -- ";
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
    public void resetSelectedEntry() {
        if (mItemGestureListener != null) {
            Log.d("DayFragment", " visibleHint");
            mItemGestureListener.resetSelectedBarEntry();
            rlTitle.setVisibility(View.VISIBLE);
        }
    }
}
