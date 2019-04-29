package com.yxc.barchart.ui.sleep;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yxc.barchart.R;
import com.yxc.barchart.ui.base.BaseFragment;
import com.yxc.chartlib.attrs.SleepChartAttrs;
import com.yxc.chartlib.entrys.SleepEntry;
import com.yxc.chartlib.sleepchart.SleepChartAdapter;
import com.yxc.chartlib.sleepchart.SleepChartItemDecoration;
import com.yxc.chartlib.sleepchart.SleepChartRecyclerView;
import com.yxc.chartlib.sleepchart.SleepLinearLayoutManager;
import com.yxc.commonlib.util.TimeUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @date 2019/4/26
 */
public class SleepFragment extends BaseFragment {

    private SleepChartRecyclerView mRecyclerView;
    private List<SleepEntry> mDataList;
    private LocalDate mLocalDate;
    private SleepChartAdapter mAdapter;
    private SleepChartAttrs mAttrs;
    private SleepLinearLayoutManager mLayoutManager;
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
        mLayoutManager = new SleepLinearLayoutManager(mContext, mAttrs);
        mItemDecoration = new SleepChartItemDecoration(mAttrs);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
    }


    private void initData() {
        Bundle bundle = getArguments();
        if (null == bundle) {
            long timestamp = bundle.getLong("timestamp", -1);
            if (timestamp != -1) {
                mLocalDate = TimeUtil.timestampToLocalDate(timestamp);
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
        List<SleepEntry> entryList = SleepTestData.createSleepEntry(mLocalDate);
        mDataList.addAll(entryList);
    }

    private void setListener() {

    }

}
