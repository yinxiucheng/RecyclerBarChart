package com.yxc.widgetlib.calendar.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;


import com.yxc.widgetlib.calendar.utils.CalendarAttrs;
import com.yxc.widgetlib.calendar.utils.Util;

import org.joda.time.LocalDate;

public class InnerMonthPainter implements CalendarWeekPainter {

    private CalendarAttrs mAttrs;
    protected Paint mTextPaint;
    protected Paint mCirclePaint;

    private int noAlphaColor = 255;

    public InnerMonthPainter(CalendarAttrs attrs) {
        this.mAttrs = attrs;
        mTextPaint = getPaint();
        mCirclePaint = getPaint();
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }


    @Override
    public void onDrawToday(Canvas canvas, Rect rect, LocalDate localDate, boolean isSelect) {
        drawTodaySolar(canvas, rect, localDate);
    }

    @Override
    public void onDrawWeekBg(Canvas canvas, RectF rectF, int roundX, int roundY, boolean isTodayWeek) {
        drawWeekBgRectF(canvas, rectF, roundX, roundY, isTodayWeek);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, Rect rect, LocalDate localDate) {
        drawOtherSolar(canvas, rect, mAttrs.disabledAlphaColor, localDate);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, Rect rect, LocalDate localDate, boolean isSelect) {
        drawOtherSolar(canvas, rect, noAlphaColor, localDate);
    }

    @Override
    public void onDrawNotCurrentMonth(Canvas canvas, Rect rect, LocalDate localDate) {
        drawOtherSolar(canvas, rect, mAttrs.alphaColor, localDate);
    }

    //今天的公历
    private void drawTodaySolar(Canvas canvas, Rect rect, LocalDate date) {
        if (Util.isAfterToday(date)) {
            mTextPaint.setColor(mAttrs.solarTextColor);
        } else {
            mTextPaint.setColor(mAttrs.todaySolarSelectTextColor);
        }
        mTextPaint.setAlpha(noAlphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), mAttrs.isShowLunar ? rect.centerY() : getBaseLineY(rect), mTextPaint);
    }

    //绘制公历
    private void drawOtherSolar(Canvas canvas, Rect rect, int alphaColor, LocalDate date) {
        mTextPaint.setColor(mAttrs.solarTextColor);
        mTextPaint.setAlpha(alphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), mAttrs.isShowLunar ? rect.centerY() : getBaseLineY(rect), mTextPaint);
    }


    public void drawWeekBgRectF(Canvas canvas, RectF rectF, int roundX, int roundY, boolean isTodayWeek) {
        if (isTodayWeek) {
            mCirclePaint.setColor(mAttrs.todayWeekBgColor);
        } else {
            mCirclePaint.setColor(mAttrs.hollowCircleColor);
        }
        canvas.drawRoundRect(rectF, roundX, roundY, mCirclePaint);
    }

    private int getBaseLineY(Rect rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }

}
