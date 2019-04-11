package com.yxc.barchart;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yxc.barchartlib.component.DistanceCompare;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.TimeUtil;
import com.yxc.barchartlib.view.BarChartAdapter;

import java.util.List;

/**
 * @author yxc
 * @date 2019/4/12
 */
public class ReLocationUtil {

    //位置进行微调
    public static float microRelation(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        //进行微调
        recyclerView.scrollToPosition(lastVisibleItemPosition);

        lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();

        List<BarEntry> displayEntries = entries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
        float yAxisMaximum = DecimalUtil.getTheMaxNumber(displayEntries);
        return yAxisMaximum;

    }

    public static DistanceCompare createDistanceCompare(RecyclerView recyclerView, int type) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();

        BarEntry barEntry = entries.get(lastVisibleItemPosition);
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        if (type == TestData.VIEW_MONTH) {
            distanceCompare = TimeUtil.createMonthDistance(barEntry.localDate);
        } else if (type == TestData.VIEW_WEEK) {
            distanceCompare = TimeUtil.createWeekDistance(barEntry.localDate);
        } else if (type == TestData.VIEW_DAY) {
            distanceCompare = TimeUtil.createDayDistance(barEntry);
        } else if (type == TestData.VIEW_YEAR) {
            distanceCompare = TimeUtil.createYearDistance(barEntry.localDate);
        }
        return distanceCompare;
    }


    //滑动到附近的刻度线
    public static float scrollToScale(RecyclerView recyclerView, int type, int displayNumber) {
        DistanceCompare distanceCompare = createDistanceCompare(recyclerView, type);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> mEntries = adapter.getEntries();
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
        float yAxisMaximum = DecimalUtil.getTheMaxNumber(visibleEntries);
        return yAxisMaximum;
    }
}
