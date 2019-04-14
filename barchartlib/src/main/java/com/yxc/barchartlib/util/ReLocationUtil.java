package com.yxc.barchartlib.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
        Log.d("VisiblePosition", "begin:" + entries.get(firstVisibleItemPosition).localDate + ": end" + entries.get(lastVisibleItemPosition).localDate);
        List<BarEntry> visibleEntries = entries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
        float yAxisMaximum = DecimalUtil.getTheMaxNumber(visibleEntries);
        HashMap<Float, List<BarEntry>> map = new HashMap<>();
        map.put(yAxisMaximum, visibleEntries);

        return map;
    }

    public static HashMap<Float, List<BarEntry>> getVisibleEntries(RecyclerView recyclerView) {
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


    public static DistanceCompare findNearFirstType(RecyclerView recyclerView, int displayNumbers, int type) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        int position = firstVisibleItemPosition; //从右边的第一个View开始找
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int parentLeft = recyclerView.getPaddingLeft();
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        for (int i = 0; i < displayNumbers; i++) {
            if (i > 0) {
                position++;
            }
            if (position >= 0 && position < entries.size()) {
                BarEntry barEntry = entries.get(position);
                if (barEntry.type == BarEntry.TYPE_XAXIS_FIRST || barEntry.type == BarEntry.TYPE_XAXIS_SPECIAL) {
                    distanceCompare.position = position;

                    //这里最好不要去找这个view 容易 crash
                    View positionView = manager.findViewByPosition(position);
                    int viewLeft = positionView.getLeft();
                    distanceCompare.distanceRight = parentRight - viewLeft;
                    distanceCompare.distanceLeft = viewLeft - parentLeft;
                    distanceCompare.setBarEntry(barEntry);
                    if (distanceCompare.isNearLeft()) {//靠近左边回到上一个月。
                        int lastPosition = distanceCompare.position - getNumbersUnitType(distanceCompare.barEntry, type);
                        Log.d("ReLocation", "lastPosition:" + lastPosition + " entries' size" + entries.size());
                        if (lastPosition > 0) {
                            distanceCompare.position = lastPosition ;
                            distanceCompare.barEntry = entries.get(lastPosition );
                        } else {
                            distanceCompare.position = 0;
                            distanceCompare.barEntry = entries.get(0);
                        }
                        distanceCompare.position = lastPosition;
                    }
                    break;
                }
            }
        }
        return distanceCompare;
    }


    public static int getNumbersUnitType(BarEntry currentBarEntry, int type) {
        if (type == VIEW_DAY) {
            return TimeUtil.NUM_HOUR_OF_DAY;
        } else if (type == VIEW_WEEK) {
            return TimeUtil.NUM_DAY_OF_WEEK;
        } else if (type == VIEW_MONTH) {
            LocalDate localDate = currentBarEntry.localDate;
            LocalDate lastMonthFirstDay = TimeUtil.getFirstDayOfMonth(localDate.minusMonths(1));
            return TimeUtil.getIntervalDay(localDate, lastMonthFirstDay);
        } else if (type == VIEW_YEAR) {
            return TimeUtil.NUM_MONTH_OF_YEAR;
        }
        return TimeUtil.NUM_HOUR_OF_DAY;
    }

    //compute the scrollByDx, the left is large position, right is small position.
    public static int computeScrollByXOffset(RecyclerView recyclerView, int displayNumbers, int type) {
        DistanceCompare distanceCompare = findNearFirstType(recyclerView, displayNumbers, type);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int positionCompare = distanceCompare.position;

        View compareView = manager.findViewByPosition(positionCompare);
        int compareViewLeft = compareView.getLeft();
        BarEntry barEntry = (BarEntry) compareView.getTag();
//        Log.d("Scroll1", " DistanceCompare's Position " + barEntry.localDate + " value:" + barEntry.getY());
//        Log.d("Scroll1", " compareView's left:" + compareViewLeft + " compareView's right:" + compareView.getRight());

        int parentWidth = recyclerView.getWidth();
        int leftPadding = recyclerView.getPaddingLeft();
        int rightPadding = recyclerView.getPaddingRight();
        int parentContent = parentWidth - leftPadding - rightPadding;

        int childWidthCompute = parentContent / displayNumbers;
        int childWidth = compareView.getWidth();

//        Log.d("Scroll1", " /parentWidth: " + parentWidth +
//                " /leftPadding:" + leftPadding + " /rightPadding:" +
//                rightPadding + " /parentContentWidth:" + parentContent +
//                " /childWidthCompute:" + childWidthCompute +
//                " /childWidth:"+ childWidth);

        int parentLeft = recyclerView.getPaddingLeft();
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();

        //todo int 会不会 越界
        int firstViewRight = compareViewLeft + positionCompare * childWidth;

        //这个值会为 负的。
        int lastViewLeft = compareViewLeft - (entries.size() - 1 - positionCompare) * childWidth;

        int scrollByXOffset;

        if (distanceCompare.isNearLeft()) {//靠近左边，content左移，recyclerView右移，取正。
            //情况 1.
            int distanceOrigin = compareViewLeft - parentLeft;//原始调整距离
            int distance = distanceOrigin - childWidth / 2;//原始 + 修正值（半个宽度的修正）
            int distanceRightBoundary = Math.abs(firstViewRight - parentRight);//右边界

            if (distanceRightBoundary < distance) { //content左移不够，顶到头，用 distanceRightBoundary
                distance = distanceRightBoundary;
            } else {//distance 不用修改

            }
            scrollByXOffset = distance;
        } else {//靠近右边，content右移，recyclerView左移，取负。
            int distanceOrigin = parentRight - compareViewLeft;//原始调整距离
            int distance = distanceOrigin - childWidth / 2;//原始 + 修正值（半个宽度的修正）
            int distanceLeftBoundary = Math.abs(lastViewLeft - parentLeft);//这里用 + 是因为 firstViewLeft为负的。

            if (distanceLeftBoundary < distance) {//content右移不够，顶到头，distanceLeftBoundary
                distance = distanceLeftBoundary;
            }
            //记得取负， scrollBy的话
            scrollByXOffset = distance - 2 * distance;
        }
        return scrollByXOffset;
    }


    //todo 注意左右滑的到定的问题。
    @Deprecated
    public static int findScrollToPosition(int type, RecyclerView recyclerView,
                                           DistanceCompare distanceCompare, int displayNums) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();

        View lastVisibleView = manager.findViewByPosition(lastVisibleItemPosition);
        View firstVisibleView = manager.findViewByPosition(firstVisibleItemPosition);

        BarEntry lastVisibleBarEntry = (BarEntry) lastVisibleView.getTag();
        BarEntry firstVisibleBarEntry = (BarEntry) firstVisibleView.getTag();

        BarEntry lastEntry = entries.get(entries.size() - 1);
        BarEntry firstEntry = entries.get(0);

        LocalDate lastVisibleLocalDate = lastVisibleBarEntry.localDate;
        LocalDate lastLocalDate = lastEntry.localDate;

        LocalDate firstVisibleLocalDate = firstVisibleBarEntry.localDate;
        LocalDate firstLocalDate = firstEntry.localDate;

        int childWidth = lastVisibleView.getWidth();
        int lastVisibleViewRight = lastVisibleView.getRight();
        int lastVisibleViewLeft = lastVisibleView.getLeft();
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int parentLeft = recyclerView.getPaddingLeft();
        int reSizeDistance = parentRight - lastVisibleViewLeft;//调整距离

        int distance = 0;
        int scrollToPosition = entries.size() - 1;
        if (type == VIEW_MONTH) {//左移为 负的
            LocalDate nearFirstDay;
            if (distanceCompare.isNearLeft()) {//靠近左边，移动到 下一条月线
                nearFirstDay = TimeUtil.getFirstDayOfNextMonth(lastVisibleLocalDate);
                int range = TimeUtil.getIntervalDay(lastVisibleLocalDate, nearFirstDay);
                distance = range * childWidth;
                distance = distance - reSizeDistance;
                scrollToPosition = lastVisibleItemPosition + range;
                if (scrollToPosition >= entries.size()) {
                    range = TimeUtil.getIntervalDay(lastVisibleLocalDate, lastLocalDate);
                    distance = range * childWidth;
                }

            } else {//靠近右边，移动到当前的月线
                nearFirstDay = TimeUtil.getFirstDayOfMonth(lastVisibleLocalDate);
                int range = TimeUtil.getIntervalDay(lastVisibleLocalDate, nearFirstDay);
                distance = range * childWidth;
                distance = distance + reSizeDistance;
                scrollToPosition = lastVisibleItemPosition - range;
                if (scrollToPosition < displayNums) {
                    range = TimeUtil.getIntervalDay(firstVisibleLocalDate, firstLocalDate);
                    distance = range * childWidth;
                }
                distance = distance - 2 * distance;
            }
        } else if (type == VIEW_WEEK) {
            if (distanceCompare.isNearLeft()) {//靠近左边，移动到下一个周一
                int dayOfWeek = lastVisibleLocalDate.getDayOfWeek();
                int range = TimeUtil.NUM_DAY_OF_WEEK - dayOfWeek + 1;
                distance = range * childWidth;
                distance = distance - reSizeDistance + childWidth / 2;

                scrollToPosition = lastVisibleItemPosition + range;
                if (scrollToPosition >= entries.size()) {
                    View lastChildView = manager.findViewByPosition(entries.size() - 1);
                    distance = lastChildView.getRight() - parentRight;
                }
            } else {//靠近右边
                int dayOfWeek = lastVisibleLocalDate.getDayOfWeek();
                int range = dayOfWeek - 1;
                distance = range * childWidth;
                distance = distance + reSizeDistance - childWidth / 2;
                scrollToPosition = lastVisibleItemPosition - range;
                if (scrollToPosition < displayNums) {
                    View firstChildView = manager.findViewByPosition(0);
                    distance = parentLeft - firstChildView.getLeft();
                }
                distance = distance - 2 * distance;
            }
        } else if (type == VIEW_DAY) {
            long timestamp = lastVisibleBarEntry.timestamp;
            int hourOfTheDay = TimeUtil.getHourOfTheDay(timestamp);
            if (distanceCompare.isNearLeft()) {//靠近左边，移动到下一天的0点
                int range = TimeUtil.NUM_HOUR_OF_DAY - hourOfTheDay;
                distance = range * childWidth;
                distance = distance - reSizeDistance;
                scrollToPosition = lastVisibleItemPosition + range;
                if (scrollToPosition >= entries.size()) {
                    range = TimeUtil.getIntervalDay(lastVisibleLocalDate, lastLocalDate);
                    distance = range * childWidth;
                }

            } else {//靠近右边
                int range = hourOfTheDay;
                distance = range * childWidth;
                distance = distance + reSizeDistance;
                scrollToPosition = lastVisibleItemPosition - range;
                if (scrollToPosition < displayNums) {
                    range = TimeUtil.getIntervalDay(firstVisibleLocalDate, firstLocalDate);
                    distance = range * childWidth;
                }
                distance = distance - 2 * distance;
            }
        } else if (type == VIEW_YEAR) {

            if (distanceCompare.isNearLeft()) {
                int range = TimeUtil.NUM_MONTH_OF_YEAR - lastVisibleLocalDate.getMonthOfYear() + 1;
                distance = range * childWidth;
                distance = distance - reSizeDistance;
                scrollToPosition = lastVisibleItemPosition + range;
                if (scrollToPosition >= entries.size()) {
                    range = TimeUtil.getIntervalDay(lastVisibleLocalDate, lastLocalDate);
                    distance = range * childWidth;
                }

            } else {
                int range = lastVisibleLocalDate.getMonthOfYear() - 1;
                distance = range * childWidth;
                distance = distance + reSizeDistance;
                scrollToPosition = lastVisibleItemPosition - range;
                if (scrollToPosition < displayNums) {
                    range = TimeUtil.getIntervalDay(firstVisibleLocalDate, firstLocalDate);
                    distance = range * childWidth;
                }
                distance = distance - 2 * distance;
            }
        }
        return distance;
    }


    //滑动到附近的刻度线
    @Deprecated
    public static HashMap<Float, List<BarEntry>> scrollToScale(RecyclerView recyclerView, int type, int displayNumber) {
        List<BarEntry> visibleEntries = scrollToScaleXAxis(recyclerView, type, displayNumber);
        float yAxisMaximum = DecimalUtil.getTheMaxNumber(visibleEntries);
        HashMap<Float, List<BarEntry>> map = new HashMap<>();
        map.put(yAxisMaximum, visibleEntries);
        return map;
    }

    @Deprecated
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

    @Deprecated
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
}
