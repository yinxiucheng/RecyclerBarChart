
package com.yxc.barchart.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxc.barchart.BaseFragment;
import com.yxc.barchart.R;
import com.yxc.barchart.TestData;
import com.yxc.barchart.formatter.XAxisDayFormatter;
import com.yxc.barchartlib.component.DistanceCompare;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.TimeUtil;
import com.yxc.barchartlib.view.BarChartAdapter;
import com.yxc.barchartlib.view.BarChartItemDecoration;
import com.yxc.barchartlib.view.BarChartRecyclerView;
import com.yxc.barchartlib.view.SpeedRatioLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

public class YearFragment extends BaseFragment {


    public int mType;
    private String[] mTitles = {"日", "周", "月", "年"};

    BarChartRecyclerView recyclerView;

    BarChartAdapter mBarChartAdapter;
    List<BarEntry> mEntries;
    BarChartItemDecoration mItemDecoration;
    private int displayNumber;
    private BarChartAttrs mBarChartAttrs;
    YAxis mYAxis;
    XAxis mXAxis;

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
        bindBarChartList(13, TestData.createWeekEntries(), TestData.VIEW_YEAR);
        reSizeYAxis();
        setListener();
        return view;
    }


    private void initView(View view){
        recyclerView = view.findViewById(R.id.recycler);
        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData(){
        mEntries = new ArrayList<>();
        SpeedRatioLinearLayoutManager layoutManager = new SpeedRatioLinearLayoutManager(getActivity(), mBarChartAttrs);
        displayNumber = 13;
        mYAxis = new YAxis(mBarChartAttrs);
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mXAxis.setValueFormatter(new XAxisDayFormatter());
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
    private void setListener() {
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
    }

    private DistanceCompare createDistanceCompare(RecyclerView recyclerView){
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        BarEntry barEntry = mEntries.get(lastVisibleItemPosition);
        return TimeUtil.createYearDistance(barEntry.localDate);
//        if (mType == VIEW_MONTH) {
//            distanceCompare = TimeUtil.createMonthDistance(barEntry.localDate);
//        } else if (mType == VIEW_WEEK) {
//            distanceCompare = TimeUtil.createWeekDistance(barEntry.localDate);
//        } else if (mType == VIEW_DAY) {
//
//        } else if (mType == VIEW_YEAR) {
//            distanceCompare = TimeUtil.createYearDistance(barEntry.localDate);
//        }
//        return distanceCompare;
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
    }


    private void bindBarChartList(int displayNumber, List<BarEntry> entries, int type){
        mEntries.clear();
        mType = type;
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mEntries.addAll(0, entries);
        mBarChartAdapter.setXAxis(mXAxis);
    }



}
