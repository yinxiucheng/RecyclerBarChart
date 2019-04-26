package com.yxc.widgetlib.calendar.painter;

import android.graphics.Canvas;
import android.graphics.Rect;

import org.joda.time.LocalDate;


public interface CalendarPainter {


    /**
     * 绘制今天的日期
     *
     * @param canvas
     * @param rect
     * @param nDate
     * @param isSelect 今天是否被选中
     */
    void onDrawToday(Canvas canvas, Rect rect, LocalDate nDate, boolean isSelect);

    /**
     * 绘制当前月或周的日期
     *
     * @param canvas
     * @param rect
     * @param nDate
     * @param isSelect 是否选中
     */
    void onDrawCurrentMonthOrWeek(Canvas canvas, Rect rect, LocalDate nDate, boolean isSelect);

    /**
     * 绘制上一月，下一月的日期，周日历不须实现
     *
     * @param canvas
     * @param rect
     * @param nDate
     */
    void onDrawNotCurrentMonth(Canvas canvas, Rect rect, LocalDate nDate);


    /**
     * 绘制不可用的日期，和方法setDateInterval(startFormatDate, endFormatDate)对应
     *
     * @param canvas
     * @param rect
     * @param nDate
     */
    void onDrawDisableDate(Canvas canvas, Rect rect, LocalDate nDate);



}
