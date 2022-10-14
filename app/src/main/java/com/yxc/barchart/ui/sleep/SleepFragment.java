package com.yxc.barchart.ui.sleep;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.barchart.R;
import com.yxc.barchart.ui.base.BaseFragment;
import com.yxc.chartlib.recyclerchart.attrs.SleepChartAttrs;
import com.yxc.chartlib.recyclerchart.barchart.SpeedRatioLayoutManager;
import com.yxc.chartlib.recyclerchart.entrys.SleepItemEntry;
import com.yxc.chartlib.recyclerchart.listener.RecyclerItemGestureListener;
import com.yxc.chartlib.recyclerchart.listener.SimpleItemGestureListener;
import com.yxc.chartlib.recyclerchart.sleepchart.SleepChartAdapter;
import com.yxc.chartlib.recyclerchart.sleepchart.SleepChartItemDecoration;
import com.yxc.chartlib.recyclerchart.view.SleepChartRecyclerView;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/26
 */
public class SleepFragment extends BaseFragment {

    private SleepChartRecyclerView mRecyclerView;
    private List<SleepItemEntry> mDataList;
    private LocalDate mLocalDate;
    private SleepChartAdapter mAdapter;
    private SleepChartAttrs mAttrs;
    private SpeedRatioLayoutManager mLayoutManager;
    private Activity mContext;
    private SleepChartItemDecoration mItemDecoration;

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
        mContext = getActivity();
        View view = View.inflate(mContext, R.layout.fragment_sleep, null);
        initData();
        initView(view);
        setListener();
        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Log.d("SleepFragment", "onHiddenChanged invoke, show!");
            initData();
            if (null != mAdapter) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            Log.d("SleepFragment", "onHiddenChanged invoke, hidden!");
        }
    }

    private void initView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler);
        mAttrs = mRecyclerView.mAttrs;
        mAdapter = new SleepChartAdapter(mContext, mDataList, mRecyclerView);
        mLayoutManager = new SpeedRatioLayoutManager(mContext, mAttrs);
        mItemDecoration = new SleepChartItemDecoration(mAttrs);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
    }


    private void initData() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            long timestamp = bundle.getLong("timestamp", -1);
            if (timestamp != -1) {
                mLocalDate = TimeDateUtil.timestampToLocalDate(timestamp);
            } else {
                mLocalDate = LocalDate.now();
            }
        } else {
            mLocalDate = LocalDate.now();
        }

        if (null == mDataList) {
            mDataList = new ArrayList<>();
        } else {
            mDataList.clear();
        }
        List<SleepItemEntry> entryList = SleepTestData.createSleepEntry(mLocalDate);
        mDataList.addAll(entryList);
    }

    RecyclerItemGestureListener mItemGestureListener;

    protected void setListener() {
        mItemGestureListener = new RecyclerItemGestureListener(getActivity(), mRecyclerView,
                new SimpleItemGestureListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                    }
                });
        mRecyclerView.addOnItemTouchListener(mItemGestureListener);
    }

}
