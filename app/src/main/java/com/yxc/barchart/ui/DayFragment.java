
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
import com.yxc.barchart.formatter.XAxisDayFormatter;
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

import java.util.ArrayList;
import java.util.List;

public class DayFragment extends BaseFragment {

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

    private OnDaySelectListener mListener;

    public interface OnDaySelectListener {
        void onDaySelect(List<BarEntry> visibleList);
    }


    public void setOnDaySelectListener(OnDaySelectListener mListener) {
        this.mListener = mListener;
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
        displayNumber = 25;
        mType = TestData.VIEW_DAY;
        valueFormatter = new XAxisDayFormatter();


        initData(displayNumber, valueFormatter);
        bindBarChartList(TestData.createDayEntries());
        setXAxis(displayNumber);
        reSizeYAxis();
        setListener(mType, displayNumber);
        return view;
    }


    private void initView(View view) {
        recyclerView = view.findViewById(R.id.recycler);
        mBarChartAttrs = recyclerView.mAttrs;
    }

    private void initData(int displayNumber, ValueFormatter valueFormatter) {
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

    @Override
    public void onResume() {
        super.onResume();
        Log.d("DayFragment", "onResume");
//        if (mListener != null){
//            mListener.onDaySelect(ReLocationUtil.getVisibleEntries(recyclerView, mType, displayNumber));
//        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d("DayFragment", "setUserVisibleHint");
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {//当可见的时候执行操作
            Log.d("DayFragment", "onDaySelect if setUserVisibleHint");
            if (mListener != null) {
                Log.d("DayFragment", "onDaySelect if");
                mListener.onDaySelect(ReLocationUtil.getVisibleEntries(recyclerView, mType, displayNumber));
            }
        } else {//不可见时执行相应的操作
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("DayFragment", "onHiddenChanged");
        if (!hidden){
            Log.d("DayFragment", "onDaySelect if onHiddenChanged");
            if (mListener != null) {
                mListener.onDaySelect(ReLocationUtil.getVisibleEntries(recyclerView, mType, displayNumber));
            }
        }
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
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resetYAxis(recyclerView, type, displayNumber);
                    if (mListener != null) {
                        mListener.onDaySelect(ReLocationUtil.getVisibleEntries(recyclerView, type, displayNumber));
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

    private void bindBarChartList(List<BarEntry> entries) {
        if (null == mEntries) {
            mEntries = new ArrayList<>();
        } else {
            mEntries.clear();
        }
        mEntries.addAll(0, entries);
    }

    private void setXAxis(int displayNumber) {
        mXAxis = new XAxis(mBarChartAttrs, displayNumber);
        mBarChartAdapter.setXAxis(mXAxis);
    }

}
