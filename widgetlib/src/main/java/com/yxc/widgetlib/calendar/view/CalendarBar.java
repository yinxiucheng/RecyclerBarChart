package com.yxc.widgetlib.calendar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.yxc.widgetlib.R;
import com.yxc.widgetlib.calendar.utils.AttrsUtil;
import com.yxc.widgetlib.calendar.utils.CalendarAttrs;

public class CalendarBar extends AppCompatTextView {


    public String[] days = { "一", "二", "三", "四", "五", "六", "日"};

    private int type;//一周的第一天是周几
    private Paint textPaint;
    private CalendarAttrs mAttrs;

    public CalendarBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.NCalendar);
        type = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, CalendarAttrs.SUNDAY);

        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        ta.recycle();

        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(mAttrs.calendarBarTextSize);
        textPaint.setColor(mAttrs.calendarBarTextColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getMeasuredWidth() - paddingRight - paddingLeft;
        int height = getMeasuredHeight() - paddingTop - paddingBottom;
        for (int i = 0; i < days.length; i++) {
            Rect rect = new Rect(paddingLeft + (i * width / days.length), paddingTop,
                    paddingLeft + ((i + 1) * width / days.length), paddingTop + height);
            Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
            float top = fontMetrics.top;
            float bottom = fontMetrics.bottom;
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
            String day;
            if (type == CalendarAttrs.MONDAY) {
                int j = i + 1;
                day = days[j > days.length - 1 ? 0 : j];
            } else {
                day = days[i];
            }
            canvas.drawText(day, rect.centerX(), baseLineY, textPaint);
        }
    }

}
