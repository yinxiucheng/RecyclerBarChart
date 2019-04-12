
package com.yxc.barchart.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxc.barchart.BaseFragment;
import com.yxc.barchart.R;
import com.yxc.barchartlib.util.ReLocationUtil;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisWeekFormatter;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.view.BarChartAdapter;
import com.yxc.barchartlib.view.BarChartItemDecoration;
import com.yxc.barchartlib.view.BarChartRecyclerView;
import com.yxc.barchartlib.view.SpeedRatioLinearLayoutManager;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class WeekFragment extends BaseFragment {

    public int mType;

    BarChartRecyclerView recyclerView;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    BarChartItemDecoration mItemDecoration;
    private int displayNumber;
    private BarChartAttrs mBarChartAttrs;
    YAxis mYAxis;
    XAxis mXAxis;
    ValueFormatter valueFormatter;
    LocalDate currentLocalDate;

    public void setOnWeekSelectListener(OnWeekSelectListener mListener) {
        this.mListener = mListener;
    }

    OnWeekSelectListener mListener;

    public interface OnWeekSelectListener{
        void onWeekSelect(List<BarEntry> barEntries);
    }

    public void showDisplayEntries() {
        if (null != mListener){
            mListener.onWeekSelect(ReLocationUtil.getVisibleEntries(recyclerView, mType, displayNumber));
        }
    }

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
        displayNumber = 8;
        mType = TestData.VIEW_WEEK;
        valueFormatter = new XAxisWeekFormatter();

        initData(displayNumber, valueFormatter);
        currentLocalDate = LocalDate.now();
        bindBarChartList(TestData.createWeekEntries(currentLocalDate, displayNumber));
        currentLocalDate = currentLocalDate.minusDays(displayNumber);

        setXAxis(displayNumber);
        reSizeYAxis();
        setListener(mType, displayNumber);
        return view;
    }


    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler);
        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData(int displayNumber, ValueFormatter valueFormatter){
        mEntries = new ArrayList<>();
        SpeedRatioLinearLayoutManager layoutManager = new SpeedRatioLinearLayoutManager(getActivity(), mBarChartAttrs);
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mXAxis.setValueFormatter(valueFormatter);
        mItemDecoration = new BarChartItemDecoration(getActivity(), mYAxis, mXAxis, mBarChartAttrs);
        recyclerView.addItemDecoration(mItemDecoration);
        mBarChartAdapter = new BarChartAdapter(getActivity(), mEntries, recyclerView, mXAxis);
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
    }


    //滑动监听
    private void setListener(final int type, final int displayNumber) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isRightScroll;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resetYAxis(recyclerView, type, displayNumber);
                    if (mListener != null){
                        mListener.onWeekSelect(ReLocationUtil.getVisibleEntries(recyclerView, type, displayNumber));
                    }
                    if (recyclerView.canScrollHorizontally(1) && isRightScroll) {//加载更多
                        List<BarEntry> entries = TestData.createWeekEntries(currentLocalDate, displayNumber);
                        currentLocalDate = currentLocalDate.minusDays(displayNumber);
                        mEntries.addAll(0, entries);
                        mBarChartAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d("weekFragment", "dx:" + dx);
                if (dx < -2){
                    isRightScroll = true;
                }else {
                    isRightScroll = false;
                }
                //判断左滑，右滑时，ScrollView的位置不一样。
            }
        });
    }

    //重新设置Y坐标
    private void resetYAxis(RecyclerView recyclerView, int type, int displayNumber) {
        float yAxisMaximum;
        if (mBarChartAttrs.enableScrollToScale) {
            yAxisMaximum = ReLocationUtil.scrollToScale(recyclerView, type, displayNumber);
        } else {
            yAxisMaximum = ReLocationUtil.microRelation(recyclerView);
        }
        YAxis yAxis = mYAxis.resetYAxis(mYAxis, yAxisMaximum);
        if (null != yAxis) {
            mYAxis = yAxis;
            mItemDecoration.setYAxis(mYAxis);
        }
    }

    private void bindBarChartList(List<BarEntry> entries){
        if (null == mEntries){
            mEntries = new ArrayList<>();
        }else {
            mEntries.clear();
        }
        mEntries.addAll(0, entries);
    }

    private void setXAxis(int displayNumber){
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mBarChartAdapter.setXAxis(mXAxis);
    }



}
