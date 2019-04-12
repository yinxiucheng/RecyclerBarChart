package com.yxc.barchartlib.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.yxc.barchartlib.component.DistanceCompare;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.view.BarChartAdapter;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;

/**
 * @author yxc
 * @date 2019/4/12
 */
public class ReLocationUtil {

    public static final int VIEW_DAY = 0;
    public static final int VIEW_WEEK = 1;
    public static final int VIEW_MONTH = 2;
    public static final int VIEW_YEAR = 3;

    //位置进行微调
    public static HashMap<Float, List<BarEntry>> microRelation(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();

        lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();

        Log.d("VisiblePosition", "begin:" + entries.get(firstVisibleItemPosition).localDate + ": end" + entries.get(lastVisibleItemPosition).localDate ) ;

        List<BarEntry> visibleEntries = entries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
        float yAxisMaximum = DecimalUtil.getTheMaxNumber(visibleEntries);
        HashMap<Float, List<BarEntry>> map = new HashMap<>();
        map.put(yAxisMaximum, visibleEntries);
        return map;

    }

    public static DistanceCompare createDistanceCompare(RecyclerView recyclerView, int type) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();

        BarEntry barEntry = entries.get(lastVisibleItemPosition);
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        if (type == VIEW_MONTH) {
            distanceCompare = TimeUtil.createMonthDistance(barEntry.localDate);
        } else if (type == VIEW_WEEK) {
            distanceCompare = TimeUtil.createWeekDistance(barEntry.localDate);
        } else if (type == VIEW_DAY) {
            distanceCompare = TimeUtil.createDayDistance(barEntry);
        } else if (type == VIEW_YEAR) {
            distanceCompare = TimeUtil.createYearDistance(barEntry.localDate);
        }
        return distanceCompare;
    }


    //滑动到附近的刻度线
    public static HashMap<Float, List<BarEntry>> scrollToScale(RecyclerView recyclerView, int type, int displayNumber) {
        List<BarEntry> visibleEntries = scrollToScaleXAxis(recyclerView, type, displayNumber);
        float yAxisMaximum = DecimalUtil.getTheMaxNumber(visibleEntries);
        HashMap<Float, List<BarEntry>> map = new HashMap<>();
        map.put(yAxisMaximum, visibleEntries);
        return map;
    }

    public static DistanceCompare findNearFirstType(RecyclerView recyclerView, int displayNumbers){
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int position = lastVisibleItemPosition;
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        for (int i = 0; i < displayNumbers ; i++) {
            if (position > 0 && position < entries.size()){
                BarEntry barEntry = entries.get(position);
                if (barEntry.type == BarEntry.TYPE_XAXIS_FIRST || barEntry.type == BarEntry.TYPE_XAXIS_SPECIAL){
                    distanceCompare.distanceRight = i + 1;
                    distanceCompare.distanceLeft = displayNumbers - distanceCompare.distanceRight;
                    break;
                }
            }
            position--;
        }
        return distanceCompare;
    }


    //todo 注意左右滑的到定的问题。
    public static int findScrollToPosition(int type, RecyclerView recyclerView,
                                           DistanceCompare distanceCompare, int displayNums){
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int scrollToPosition = entries.size() - 1;
        if (type == VIEW_MONTH){
            BarEntry barEntry = entries.get(lastVisibleItemPosition);
            LocalDate currentLocalDate = barEntry.localDate;
            LocalDate nearFirstDay;
            int distance;

            if (distanceCompare.isNearLeft()){//靠近左边，移动到 下一条月线
                nearFirstDay = TimeUtil.getFirstDayOfNextMonth(currentLocalDate);
                distance = TimeUtil.getIntervalDay(currentLocalDate, nearFirstDay);
                scrollToPosition = lastVisibleItemPosition + distance;
                if (scrollToPosition >= entries.size()){
                    scrollToPosition = entries.size() - 1;
                }
            }else {//靠近右边，移动到当前的月线
                nearFirstDay = TimeUtil.getFirstDayOfMonth(currentLocalDate);
                distance = TimeUtil.getIntervalDay(currentLocalDate, nearFirstDay);
                scrollToPosition = lastVisibleItemPosition - distance;
                if (scrollToPosition < displayNums){
                    scrollToPosition = 0;
                }
            }
            return scrollToPosition;
        }else if (type == VIEW_WEEK){
            BarEntry barEntry = entries.get(lastVisibleItemPosition);
            LocalDate currentLocalDate = barEntry.localDate;
            int distance;
            if (distanceCompare.isNearLeft()){//靠近左边，移动到下一个周一
                int dayOfWeek = currentLocalDate.getDayOfWeek();
                distance = TimeUtil.NUM_DAY_OF_WEEK - dayOfWeek + 1;
                scrollToPosition = lastVisibleItemPosition + distance;
                if (scrollToPosition >= entries.size()){
                    scrollToPosition = entries.size() - 1;
                }
            }else {
                int dayOfWeek = currentLocalDate.getDayOfWeek();
                distance = dayOfWeek - 1;
                scrollToPosition = lastVisibleItemPosition - distance;
                if (scrollToPosition < displayNums){
                    scrollToPosition = 0;
                }
            }
            return scrollToPosition;
        }else if (type == VIEW_DAY){
            BarEntry barEntry = entries.get(lastVisibleItemPosition);
            long timestamp = barEntry.timestamp;
            int distance;
            int hourOfTheDay = TimeUtil.getHourOfTheDay(timestamp);
            if (distanceCompare.isNearLeft()){//靠近左边，移动到下一天的0点
                distance = TimeUtil.NUM_HOUR_OF_DAY - hourOfTheDay;
                scrollToPosition = lastVisibleItemPosition + distance;
                if (scrollToPosition >= entries.size()){
                    scrollToPosition = entries.size() - 1;
                }
            }else {
                distance = hourOfTheDay;
                scrollToPosition = lastVisibleItemPosition - distance;
                if (scrollToPosition < displayNums){
                    scrollToPosition = 0;
                }
            }
            return scrollToPosition;
        }else if (type == VIEW_YEAR){
            BarEntry barEntry = entries.get(lastVisibleItemPosition);
            LocalDate localDate = barEntry.localDate;
            int distance;
            if (distanceCompare.isNearLeft()){
                distance = TimeUtil.NUM_MONTH_OF_YEAR - localDate.getMonthOfYear() + 1;
                scrollToPosition = lastVisibleItemPosition + distance;
                if (scrollToPosition >= entries.size()){
                    scrollToPosition = entries.size() - 1;
                }
            }else {
                distance = localDate.getMonthOfYear() - 1;
                scrollToPosition = lastVisibleItemPosition - distance;
                if (scrollToPosition < displayNums){
                    scrollToPosition = 0;
                }
            }
            return scrollToPosition;
        }
        return scrollToPosition;
    }


    public static HashMap<Float, List<BarEntry>> getVisibleEntries(int scrollToPosition, RecyclerView recyclerView){
        recyclerView.scrollToPosition(scrollToPosition);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> mEntries = adapter.getEntries();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
        List<BarEntry> visibleEntries = mEntries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);

        float yAxisMaximum = DecimalUtil.getTheMaxNumber(visibleEntries);
        HashMap<Float, List<BarEntry>> map = new HashMap<>();
        map.put(yAxisMaximum, visibleEntries);
        return map;
    }

    public static List<BarEntry> scrollToScaleXAxis(RecyclerView recyclerView, int type, int displayNumber) {
        DistanceCompare distanceCompare = createDistanceCompare(recyclerView, type);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> mEntries = adapter.getEntries();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int endOfPosition;

        int transactionType = 0;// type 为 1往左回弹，为0往右回弹
        if (distanceCompare.distanceLeft <= distanceCompare.distanceRight) {//左间距小于右间距,靠近左，往左回弹
            Log.d("Location", "type 值为1, 往左回弹 " + "distanceLeft:" + distanceCompare.distanceLeft + " distanceRight:" + distanceCompare.distanceRight);
            transactionType = 1;
            endOfPosition = lastVisibleItemPosition - distanceCompare.distanceLeft + 1;
            if (lastVisibleItemPosition + distanceCompare.distanceRight > mEntries.size()) {//右尽头
                Log.d("Location", "type 值为1, 往左回弹 " + ":positon 不够用" + (lastVisibleItemPosition + distanceCompare.distanceRight) + " size:" + mEntries.size());
                endOfPosition = mEntries.size() - 1;
            }
        } else {
            transactionType = 0;
            Log.d("Location", "type 值为0, 向右回弹 " + "distanceLeft:" + distanceCompare.distanceLeft + " distanceRight:" + distanceCompare.distanceRight);
            endOfPosition = lastVisibleItemPosition + distanceCompare.distanceRight;
            if (lastVisibleItemPosition - displayNumber <= 0) {//左尽头
                Log.d("Location", "type 值为0, 向右回弹 " + ":position 不够用" + (lastVisibleItemPosition - displayNumber));
                endOfPosition = displayNumber;
            }
        }

        //往左回弹
        if (transactionType == 1 && endOfPosition == mEntries.size() - 1) {//右边到头
            recyclerView.scrollToPosition(endOfPosition);//这里是不是要减1
            lastVisibleItemPosition = endOfPosition;
            Log.d("Location", "type 值为1, 往左回弹" + "验证左弹，右到头");
        } else if (transactionType == 1) {
            recyclerView.scrollToPosition(endOfPosition);
            lastVisibleItemPosition = endOfPosition;
            Log.d("Location", "type 值为1, 往左回弹" + "验证左弹，右不到头");
        }

        //往右回弹
        if (transactionType == 0 && endOfPosition == displayNumber) {//右边界，lastVisibleItemPosition 保持不变
            recyclerView.scrollToPosition(displayNumber - 1);
            lastVisibleItemPosition = endOfPosition;
            Log.d("Location", "type 值为0, 往右回弹 " + "验证右弹，左到头");
        } else if (transactionType == 0) {
            recyclerView.scrollToPosition(endOfPosition);
            lastVisibleItemPosition = endOfPosition;
            Log.d("Location", "type 值为0, 往右回弹 " + "验证右弹，左不到头");
        }

//        int firstVisibleItemPosition = lastVisibleItemPosition - displayNumber;
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
        int endPosition = lastVisibleItemPosition;
        if (firstVisibleItemPosition <= 0) {
            firstVisibleItemPosition = 0;
            endPosition = displayNumber;
        }
        List<BarEntry> visibleEntries = mEntries.subList(firstVisibleItemPosition, endPosition);
        return visibleEntries;
    }
}
