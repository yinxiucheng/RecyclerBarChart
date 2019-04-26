package com.yxc.widgetlib.calendar.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;


import com.yxc.widgetlib.calendar.utils.CalendarAttrs;

import org.joda.time.LocalDate;

public class InnerPainter implements CalendarPainter {

    private CalendarAttrs mAttrs;
    protected Paint mTextPaint;
    protected Paint mCirclePaint;

    private int noAlphaColor = 255;


    public InnerPainter(CalendarAttrs attrs) {
        this.mAttrs = attrs;

        mTextPaint = getPaint();
        mCirclePaint = getPaint();
    }

    protected Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    @Override
    public void onDrawToday(Canvas canvas, Rect rect, LocalDate localDate, boolean isSelect) {
        if (isSelect) {
            drawSolidCircle(canvas, rect, true);
        } else {
            drawSolidCircle(canvas, rect, false);
        }
        drawSolar(canvas, rect, noAlphaColor,  localDate, true);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, Rect rect, LocalDate localDate) {
        drawSolar(canvas, rect, mAttrs.disabledAlphaColor, localDate, false);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, Rect rect, LocalDate localDate, boolean isSelect) {
        if (isSelect) {
            drawSolidCircle(canvas, rect, true);
            drawSolar(canvas, rect, noAlphaColor, localDate, true);
        } else {
            drawSolar(canvas, rect, noAlphaColor, localDate, false);
        }
    }

    @Override
    public void onDrawNotCurrentMonth(Canvas canvas, Rect rect, LocalDate localDate) {
        drawSolar(canvas, rect, mAttrs.alphaColor, localDate, false);
    }

    //实心圆
    private void drawSolidCircle(Canvas canvas, Rect rect, boolean isTodaySelect) {
        mCirclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mCirclePaint.setStrokeWidth(mAttrs.hollowCircleStroke);
        if (isTodaySelect) {
            mCirclePaint.setColor(mAttrs.selectCircleColor);
        } else {
            mCirclePaint.setColor(mAttrs.todayWeekBgColor);
        }
        mCirclePaint.setAlpha(noAlphaColor);
        canvas.drawCircle(rect.centerX(), rect.centerY(), mAttrs.selectCircleRadius, mCirclePaint);

    }

    //绘制公历
    private void drawSolar(Canvas canvas, Rect rect, int alphaColor, LocalDate date, boolean isSelect) {
        if (isSelect) {
            mTextPaint.setColor(mAttrs.todaySolarSelectTextColor);
        } else {
            mTextPaint.setColor(mAttrs.solarTextColor);
        }
        mTextPaint.setAlpha(alphaColor);
        mTextPaint.setTextSize(mAttrs.solarTextSize);
        canvas.drawText(date.getDayOfMonth() + "", rect.centerX(), mAttrs.isShowLunar ? rect.centerY() : getBaseLineY(rect), mTextPaint);
    }

    private int getBaseLineY(Rect rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }

}
