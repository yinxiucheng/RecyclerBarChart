package com.yxc.barchart.ui.bezier;

import android.view.View;

import com.yxc.barchart.ui.base.BaseChartFragment;
import com.yxc.chartlib.recyclerchart.entrys.BarEntry;
import com.yxc.chartlib.recyclerchart.listener.RecyclerItemGestureListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since 2019-05-13
 */
public abstract class BaseBezierFragment extends BaseChartFragment {

    protected List<BarEntry> visibleEntries;

    protected OnRateSelectChangedListener mRateSelectChangedListener;

    protected RecyclerItemGestureListener mItemGestureListener;

    public interface OnRateSelectChangedListener {

        void onDayChanged(List<BarEntry> visibleEntries);

        void onMonthChanged(List<BarEntry> visibleEntries);

        void onWeekSelectChanged(List<BarEntry> visibleEntries);

        void onYearSelectChanged(List<BarEntry> visibleEntries);
    }

    public void setOnRateSelectChangedListener(OnRateSelectChangedListener listener) {
        this.mRateSelectChangedListener = listener;
    }

    //防止 Fragment重叠
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
        }
    }

    protected void setVisibleEntries(List<BarEntry> barEntries) {
        if (null == visibleEntries) {
            visibleEntries = new ArrayList<>();
        } else {
            visibleEntries.clear();
        }
        visibleEntries.addAll(barEntries);
        displayDateAndRate();
    }

    public  void resetSelectedEntry(){
        if (mItemGestureListener != null){
            mItemGestureListener.resetSelectedBarEntry();
        }
    }

    public abstract void displayDateAndRate();

    public abstract void scrollToCurrentCycle();//回到当前周期




}
