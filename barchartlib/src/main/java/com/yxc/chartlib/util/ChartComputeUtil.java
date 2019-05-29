package com.yxc.chartlib.util;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.component.DistanceCompare;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.entrys.YAxisMaxEntries;
import com.yxc.chartlib.entrys.model.SleepItemTime;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
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
    public static <T extends BarEntry> YAxisMaxEntries getVisibleEntries(RecyclerView recyclerView) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        List<T> mEntries = adapter.getEntries();
        //获取最后一个完全显示的ItemPosition
        int lastVisibleItemPosition = manager.findLastCompletelyVisibleItemPosition();
        int firstVisibleItemPosition = manager.findFirstCompletelyVisibleItemPosition();
        //todo lastVisibleItemPosition + 1 可能越界
        List<T> visibleEntries = mEntries.subList(firstVisibleItemPosition, lastVisibleItemPosition + 1);
        float yAxisMaximum = DecimalUtil.getTheMaxNumber(visibleEntries);
        return new YAxisMaxEntries(yAxisMaximum, visibleEntries);
    }

    //对于月进行清洗数据
    public static <T extends BarEntry> List<T> getMonthCleanEntries(List<T> visibleEntries) {
        int middle = visibleEntries.size() / 2;
        LocalDate middleDate = visibleEntries.get(middle).localDate;

        List<T> entryList = new ArrayList<>();
        for (int i = 0; i < visibleEntries.size(); i++) {
            T barEntry = visibleEntries.get(i);
            if (TimeDateUtil.isSameMonth(middleDate, barEntry.localDate)) {
                entryList.add(barEntry);
            }
        }
        return entryList;
    }


    //compute the scrollByDx, the left is large position, right is small position.
    public static <T extends BarEntry> int computeScrollByXOffset(RecyclerView recyclerView, int displayNumbers, int type) {
        DistanceCompare distanceCompare = findDisplayFirstTypePosition(recyclerView, displayNumbers);
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        if (null == adapter) {
            return 0;
        }
        List<T> entries = adapter.getEntries();
        int positionCompare = distanceCompare.position;

        View compareView = manager.findViewByPosition(positionCompare);
        if (null == compareView) {
            return 0;
        }
        int compareViewRight = compareView.getRight();
        int compareViewLeft = compareView.getLeft();

        int childWidth = compareView.getWidth();
        int parentLeft = recyclerView.getPaddingLeft();
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();

        int scrollByXOffset;
        if (distanceCompare.isNearLeft()) {//靠近左边，content左移，recyclerView右移，取正。
            //情况 1.
            int distance = compareViewRight - parentLeft;//原始调整距离
            if (positionCompare < displayNumbers + 1) {//防止 positionCompare过大，计算firstViewRight时，int越界
                int firstViewRight = compareViewRight + positionCompare * childWidth;
                int distanceRightBoundary = Math.abs(firstViewRight - parentRight);//右边界
                if (distanceRightBoundary < distance) { //content左移不够，顶到头，用 distanceRightBoundary
                    distance = distanceRightBoundary;
                }
            }
            scrollByXOffset = distance;
        } else {//靠近右边，content右移，recyclerView左移，取负。
            int distance = parentRight - compareViewRight;//原始调整距离
            if (entries.size() - positionCompare < displayNumbers) {
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
    private static <T extends BarEntry> DistanceCompare findDisplayFirstTypePosition(RecyclerView recyclerView, int displayNumbers) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        DistanceCompare distanceCompare = new DistanceCompare(0, 0);
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        if (null == manager || null == adapter) {
            return distanceCompare;
        }
        List<T> entries = adapter.getEntries();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        int position = firstVisibleItemPosition; //从右边的第一个View开始找
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int parentLeft = recyclerView.getPaddingLeft();

        for (int i = 0; i < displayNumbers; i++) {
            if (i > 0) {
                position++;
            }
            if (position >= 0 && position < entries.size()) {
                T barEntry = entries.get(position);
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
            return TimeDateUtil.NUM_HOUR_OF_DAY;
        } else if (type == VIEW_WEEK) {
            return TimeDateUtil.NUM_DAY_OF_WEEK;
        } else if (type == VIEW_MONTH) {
            LocalDate localDate = currentBarEntry.localDate;
            LocalDate lastMonthEndLocalDate = TimeDateUtil.getFirstDayOfMonth(localDate).minusDays(1);//上个月末的最后一天
            int distance = TimeDateUtil.getIntervalDay(lastMonthEndLocalDate, localDate);
            return distance;
        } else if (type == VIEW_YEAR) {
            return TimeDateUtil.NUM_MONTH_OF_YEAR;
        }
        return TimeDateUtil.NUM_HOUR_OF_DAY;
    }


    /**
     * @return the scrollToPosition Just let this position item display,
     * but don't consume the location in the edge of screen. so Deprecated and
     * use computeScrollByXOffset replace
     */
    @Deprecated
    public static <T extends BarEntry> DistanceCompare findScrollToPosition(RecyclerView recyclerView, int displayNumbers, int type) {
        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) recyclerView.getAdapter();
        List<T> entries = adapter.getEntries();
        int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
        int position = firstVisibleItemPosition; //从右边的第一个View开始找
        int parentRight = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int parentLeft = recyclerView.getPaddingLeft();
        DistanceCompare<T> distanceCompare = new DistanceCompare(0, 0);
        for (int i = 0; i < displayNumbers; i++) {
            if (i > 0) {
                position++;
            }
            if (position >= 0 && position < entries.size()) {
                T barEntry = entries.get(position);
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

    public static <T extends BarEntry> float getChildYPosition(final RecyclerView parent, YAxis mYAxis, BarChartAttrs mBarChartAttrs, T barEntry) {
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - mBarChartAttrs.contentPaddingTop;
        float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
        float top = Math.max(contentBottom - height, parent.getPaddingTop());
        return top;
    }

    public static <T extends BarEntry, V extends BaseYAxis> RectF getBarChartRectF(View child, final RecyclerView parent, V mYAxis,
                                                                                   BarChartAttrs mBarChartAttrs, T barEntry) {
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - mBarChartAttrs.contentPaddingTop;
        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;

        if (mBarChartAttrs.yAxisReverse && barEntry.getY() > 0) {
            float valueTemp = mYAxis.getAxisMaximum() - barEntry.getY();
            height = valueTemp / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
        }

        final float top = Math.max(contentBottom - height, parent.getPaddingTop());
        rectF.set(left, top, right, contentBottom);
        return rectF;
    }


    public static <T extends BaseYAxis> List<RectF> getSleepChartRectFList(View child, final RecyclerView parent, T yAxis,
                                                                           BarChartAttrs mBarChartAttrs, List<SleepItemTime> sleepItemTimeList) {
        List<RectF> rectFList = new ArrayList<>();

        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - parent.getPaddingTop() - mBarChartAttrs.contentPaddingTop;
        float width = child.getWidth();
        float barSpaceWidth = width * mBarChartAttrs.barSpace;
        float barChartWidth = width - barSpaceWidth;//柱子的宽度
        final float left = child.getLeft() + barSpaceWidth / 2;
        final float right = left + barChartWidth;
        float parentTop = parent.getPaddingTop();

        float rectFBottom = contentBottom;
        for (int i = sleepItemTimeList.size() - 1; i >= 0; i--) {//创建的时候需要把深睡放在最后，顺序保持
            SleepItemTime sleepItemTime = sleepItemTimeList.get(i);
            float yRectFTop = sleepItemTime.durationTime / yAxis.getAxisMaximum() * realYAxisLabelHeight;
            RectF rectF = createRectF(left, rectFBottom, right, yRectFTop, parentTop);
            rectFList.add(0, rectF);
            rectFBottom = rectF.top;
        }
        return rectFList;
    }


    private static RectF createRectF(float left, float bottom, float right, float height, float parentTop) {
        RectF rectF = new RectF();
        rectF.set(left, bottom - height, right, bottom);
        final float top = Math.max(bottom - height, parentTop);
        rectF.set(left, top, right, bottom);
        return rectF;
    }


    public static <T extends BarEntry> RectF getBarChartRectF2(View child, final RecyclerView parent, YAxis mYAxis,
                                                               BarChartAttrs mBarChartAttrs, T barEntry) {
        final RectF rectF = new RectF();
        float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float realYAxisLabelHeight = contentBottom - mBarChartAttrs.contentPaddingTop - parent.getPaddingTop();
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


    public static <T extends BarEntry> float getYPosition(T entry, RecyclerView parent, BaseYAxis mYAxis, BaseChartAttrs baseChartAttrs) {
        float contentBottom = parent.getHeight() - baseChartAttrs.contentPaddingBottom - parent.getPaddingBottom();
        float contentTop = baseChartAttrs.contentPaddingTop + parent.getPaddingTop();
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
