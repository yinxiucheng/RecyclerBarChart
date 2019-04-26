package com.yxc.chartlib.util;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.DistanceCompare;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.barchart.BarChartAdapter;
import com.yxc.commonlib.util.TimeUtil;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/12
 */
public class ChartComputeUtil {

    private static final int VIEW_DAY = 0;
    private static final int VIEW_WEEK = 1;
    private static final int VIEW_MONTH = 2;
    public static final int VIEW_YEAR = 3;

    //位置进行微调

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


    //compute the scrollByDx, the left is large position, right is small position.
    public static int computeScrollByXOffset(RecyclerView recyclerView, int displayNumbers, int type) {
        DistanceCompare distanceCompare = findDisplayFirstTypePosition(recyclerView, displayNumbers);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BarChartAdapter adapter = (BarChartAdapter) recyclerView.getAdapter();
        List<BarEntry> entries = adapter.getEntries();
        int positionCompare = distanceCompare.position;

        View compareView = manager.findViewByPosition(positionCompare);
        int compareViewRight = compareView.getRight();
        int compareViewLeft = compareView.getLeft();

        int childWidth = compareView.getWidth();
        int parentLeft = recyclerView.getPaddingLeft();
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();

        int scrollByXOffset;
        if (distanceCompare.isNearLeft()) {//靠近左边，content左移，recyclerView右移，取正。
            //情况 1.
            int distance = compareViewRight - parentLeft;//原始调整距离
            if (positionCompare < displayNumbers + 1){//防止 positionCompare过大，计算firstViewRight时，int越界
                int firstViewRight = compareViewRight + positionCompare * childWidth;
                int distanceRightBoundary = Math.abs(firstViewRight - parentRight);//右边界
                if (distanceRightBoundary < distance) { //content左移不够，顶到头，用 distanceRightBoundary
                    distance = distanceRightBoundary;
                }
            }
            scrollByXOffset = distance;
        } else {//靠近右边，content右移，recyclerView左移，取负。
            int distance = parentRight - compareViewRight;//原始调整距离
            if (entries.size() - positionCompare < displayNumbers ){
                //这个值会为 负的。
                int lastViewLeft = compareViewLeft - (entries.size() - 1 - positionCompare) * childWidth;
                int distanceLeftBoundary = Math.abs(parentLeft - lastViewLeft);//右边 - 左边，因为 lastViewLeft是负值，实际上是两值相加。
                if (distanceLeftBoundary < distance) {//content右移不够，顶到头，distanceLeftBoundary
                    distance = distanceLeftBoundary;
                }
            }
            //记得取负， scrollBy的话
            scrollByXOffset = distance - 2 * distance;
        }
        return scrollByXOffset;
    }

    //find the largest divider position ( ItemDecoration ).
    public static DistanceCompare findDisplayFirstTypePosition(RecyclerView recyclerView, int displayNumbers) {
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
                    View positionView = manager.findViewByPosition(position);
                    int viewLeft = positionView.getLeft();
                    distanceCompare.distanceRight = parentRight - viewLeft;
                    distanceCompare.distanceLeft = viewLeft - parentLeft;
                    distanceCompare.setBarEntry(barEntry);
                    break;
                }
            }
        }
        return distanceCompare;
    }


    private static int getNumbersUnitType(BarEntry currentBarEntry, int type) {
        if (type == VIEW_DAY) {
            return TimeUtil.NUM_HOUR_OF_DAY;
        } else if (type == VIEW_WEEK) {
            return TimeUtil.NUM_DAY_OF_WEEK;
        } else if (type == VIEW_MONTH) {
            LocalDate localDate = currentBarEntry.localDate;
            LocalDate lastMonthEndLocalDate = TimeUtil.getFirstDayOfMonth(localDate).minusDays(1);//上个月末的最后一天
            int distance = TimeUtil.getIntervalDay(lastMonthEndLocalDate, localDate);
            Log.d("Tag", "localDate:" + localDate + " lastMonthDay:" + lastMonthEndLocalDate + " distance:" + distance);
            return distance;
        } else if (type == VIEW_YEAR) {
            return TimeUtil.NUM_MONTH_OF_YEAR;
        }
        return TimeUtil.NUM_HOUR_OF_DAY;
    }


    /**
     * @param recyclerView
     * @param displayNumbers
     * @param type
     * @return the scrollToPosition Just let this position item display,
     * but don't consume the location in the edge of screen. so Deprecated and
     * use computeScrollByXOffset replace
     */
    @Deprecated
    public static DistanceCompare findScrollToPosition(RecyclerView recyclerView, int displayNumbers, int type) {
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
                            distanceCompare.position = lastPosition;
                            distanceCompare.barEntry = entries.get(lastPosition);
                        } else {
                            distanceCompare.position = 0;
                            distanceCompare.barEntry = entries.get(0);
                        }
                        distanceCompare.position = lastPosition;
                    } else {//靠近右边，直接返回当月的。

                    }
                    break;
                }
            }
        }
        return distanceCompare;
    }

    public static RectF getBarChartRectF(View child, final RecyclerView parent, YAxis mYAxis,
                                         BarChartAttrs mBarChartAttrs, BarEntry barEntry){
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - mBarChartAttrs.maxYAxisPaddingTop - parent.getPaddingTop();
        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
        final float top = Math.max(contentBottom - height, parent.getPaddingTop());
        rectF.set(left, top, right, contentBottom);
        return rectF;
    }


    public static RectF getBarChartRectF2(View child, final RecyclerView parent, YAxis mYAxis,
                                         BarChartAttrs mBarChartAttrs, BarEntry barEntry){
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - mBarChartAttrs.maxYAxisPaddingTop - parent.getPaddingTop();
        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;

        float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
        final float top = Math.max(contentBottom - height, parent.getPaddingTop());

        rectF.set(0, 0, right - left, contentBottom - top);
        return rectF;
    }


    public static float getYPosition(BarEntry entry, RecyclerView parent, YAxis mYAxis, BarChartAttrs mBarChartAttrs) {
        float contentBottom = parent.getHeight() - mBarChartAttrs.contentPaddingBottom - parent.getPaddingBottom();
        float contentTop = mBarChartAttrs.maxYAxisPaddingTop + parent.getPaddingTop();
        float contentHeight = contentBottom - contentTop;
        float height = entry.getY() / mYAxis.getAxisMaximum() * contentHeight;
        return contentBottom - height;
    }

    public static Path createColorRectPath(PointF pointF1, PointF pointF2, float bottom) {
        Path path = new Path();
        path.moveTo(pointF1.x, pointF1.y);
        path.lineTo(pointF2.x, pointF2.y);
        path.lineTo(pointF2.x, bottom);
        path.lineTo(pointF1.x, bottom);
        path.close();
        return path;
    }

    public static PointF getInterceptPointF(PointF pointF1, PointF pointF2, float x) {
        float width = Math.abs(pointF1.x - pointF2.x);
        float height = Math.abs(pointF1.y - pointF2.y);
        float interceptWidth = Math.abs(pointF1.x - x);
        float interceptHeight = interceptWidth * 1.0f / width * height;
        float y;
        if (pointF2.y < pointF1.y) {
            y = pointF1.y - interceptHeight;
        } else {
            y = pointF1.y + interceptHeight;
        }
        return new PointF(x, y);
    }


}
