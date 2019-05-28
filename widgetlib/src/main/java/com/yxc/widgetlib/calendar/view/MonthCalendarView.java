package com.yxc.widgetlib.calendar.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.widgetlib.R;
import com.yxc.widgetlib.calendar.utils.CalendarAttrs;
import com.yxc.widgetlib.calendar.utils.AttrsUtil;
import com.yxc.widgetlib.calendar.utils.Util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @date 2019/3/18
 */
public class MonthCalendarView extends View {

    public static final int COLUMN_MONTH = 4;

    public static final int MONTH_OF_YEAR = 12;

    public static final int LINE_NUMBER = 3;

    private List<MonthCalendar> mDateList; //月数


    protected List<Rect> mRectList;//点击用的矩形集合

    private Paint mTextPaint;

    private Paint mCirclePaint;

    private CalendarAttrs mAttrs;

    private MonthCalendar mSelectYearCalendar;

    private OnYearCalendarItemClickListener mListener;

    private int noAlphaColor = 255;

    public MonthCalendarView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAttrs = AttrsUtil.getAttrs(context, attributeSet);
        initDateList();
        mTextPaint = getPaint();
        mCirclePaint = getPaint();
        mRectList = new ArrayList<>();
    }

    private void initDateList(){
        LocalDate today = LocalDate.now();
        mDateList = createYearCalendarList(today);
        mSelectYearCalendar = findMonthCalendar(today);
        if (null != mSelectYearCalendar){
            mSelectYearCalendar.setSelected(true);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < LINE_NUMBER; i++) {
            for (int j = 0; j < COLUMN_MONTH; j++) {
                Rect rect = getRect(i, j);
                mRectList.add(rect);
                MonthCalendar yearCalendar = mDateList.get(i * COLUMN_MONTH + j);
                //在可用区间内的正常绘制，
                mTextPaint.setTextSize(DisplayUtil.dip2px(20));

                String subStr = yearCalendar.localDate.getMonthOfYear() + "";

                int length = subStr.length();
                int padding = DisplayUtil.dip2px(7);
                if (length > 1) {
                    padding = DisplayUtil.dip2px(9);
                }
                if (yearCalendar.isFuture) {
                    mTextPaint.setColor(ColorUtil.getResourcesColor(getContext(), R.color.cpb_unclick_color));
                } else if (yearCalendar.isSelected) {
                    mTextPaint.setColor(Color.WHITE);
                    drawSolidCircle(canvas, rect, true);
                } else if (yearCalendar.isCurrent && !yearCalendar.isSelected) {
                    mTextPaint.setColor(Color.WHITE);
                    drawSolidCircle(canvas, rect, false);
                } else {
                    mTextPaint.setColor(Color.BLACK);
                }
                canvas.drawText(subStr, rect.centerX() - padding, getBaseLineY(rect), mTextPaint);
                mTextPaint.setTextSize(DisplayUtil.dip2px(16));
                canvas.drawText(getContext().getString(R.string.str_month), rect.centerX() + padding, getBaseLineY(rect), mTextPaint);
            }
        }
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

    //获取每个元素矩形
    private Rect getRect(int i, int j) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int rectHeight = height / LINE_NUMBER;
        Rect rect = new Rect(j * width / COLUMN_MONTH, i * rectHeight,
                j * width / COLUMN_MONTH + width / COLUMN_MONTH, i * rectHeight + rectHeight);
        return rect;
    }

    private int getBaseLineY(Rect rect) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
        return baseLineY;
    }

    private Paint getPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        return paint;
    }

    private List<MonthCalendar> createYearCalendarList(LocalDate selectDate) {
        if (mDateList == null) {
            mDateList = new ArrayList<>();
        } else {
            mDateList.clear();
        }
        int currentMonth = LocalDate.now().getMonthOfYear();
        int selectMonth = selectDate.getMonthOfYear();

        for (int i = 1; i <= MONTH_OF_YEAR; i++) {
            MonthCalendar monthCalendar = new MonthCalendar();
            int monthDistance = i - selectMonth;
            LocalDate localDate = selectDate.plusMonths(monthDistance);
            int month = localDate.getMonthOfYear();
            monthCalendar.setLocalDate(localDate);
            if (isFuture(localDate)) {
                monthCalendar.isFuture = true;
            }else if(isCurrentYear(selectDate) && month == currentMonth) {
                monthCalendar.isCurrent = true;
            }
            mDateList.add(monthCalendar);
        }
        return mDateList;
    }

    public boolean isCurrentYear(LocalDate localDate) {
        if (localDate.getYear() == LocalDate.now().getYear()) {
            return true;
        }
        return false;
    }

    public boolean isFuture(LocalDate localDate) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthOfYear();
        int selectMonth = localDate.getMonthOfYear();
        int selectYear = localDate.getYear();
        if (currentYear < selectYear) {
            return true;
        } else if (selectYear < currentYear) {
            return false;
        } else if (selectYear == currentYear && selectMonth > currentMonth) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    private GestureDetector mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            for (int i = 0; i < mRectList.size(); i++) {
                Rect rect = mRectList.get(i);
                if (rect.contains((int) e.getX(), (int) e.getY())) {
                    MonthCalendar yearCalendar = mDateList.get(i);//去掉点击效果
//                    if (onClick(yearCalendar)) {
//                        invalidate();
//                    }
                    break;
                }
            }
            return true;
        }
    });


    public void setOnYearCalendarItemClickListener(OnYearCalendarItemClickListener listener) {
        this.mListener = listener;
    }


    private boolean onClick(MonthCalendar yearCalendar) {
        if (yearCalendar.isFuture || yearCalendar.isSelected) {
            return false;
        }
        mSelectYearCalendar.isSelected = false;
        yearCalendar.isSelected = true;
        mSelectYearCalendar = yearCalendar;
        if (null != mListener) {
            mListener.onYearItemClick(yearCalendar.localDate);
        }
        return true;
    }

    public MonthCalendar getSelectYearCalendar() {
        return mSelectYearCalendar;
    }

    public void setSelectDateInvalidate(LocalDate selectDate) {
        if (isSameMonthCalendar(selectDate)) {
            return;
        }
        MonthCalendar monthCalendar = findMonthCalendar(selectDate);
        if (monthCalendar == null){
            createYearCalendarList(selectDate);
            monthCalendar = findMonthCalendar(selectDate);
        }
        if (onClick(monthCalendar)) {
            invalidate();
        }
    }

    private MonthCalendar findMonthCalendar(LocalDate selectDate) {
        for (int i = 0; i < mDateList.size(); i++) {
            MonthCalendar monthCalendar = mDateList.get(i);
            boolean b = isSameMonthCalendar(selectDate, monthCalendar);
            if (b) {
                return monthCalendar;
            } else {
                continue;
            }
        }
        return null;
    }

    private boolean isSameMonthCalendar(LocalDate localDate, MonthCalendar mSelectYearCalendar) {
        LocalDate mSelectDate = mSelectYearCalendar.getLocalDate();
        if (Util.isSameLocalDate(localDate, mSelectDate)) {
            return true;
        }
        return false;
    }

    //
    private boolean isSameMonthCalendar(LocalDate localDate) {
        return isSameMonthCalendar(localDate, mSelectYearCalendar);
    }

    public interface OnYearCalendarItemClickListener {
        void onYearItemClick(LocalDate localDate);
    }


}
