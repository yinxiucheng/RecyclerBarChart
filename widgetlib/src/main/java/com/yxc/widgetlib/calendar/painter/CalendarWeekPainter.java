package com.yxc.widgetlib.calendar.painter;

import android.graphics.Canvas;
import android.graphics.RectF;


public interface CalendarWeekPainter extends CalendarPainter {

    void onDrawWeekBg(Canvas canvas, RectF rectF, int roundX, int roundY, boolean isTodayWeek);


}
