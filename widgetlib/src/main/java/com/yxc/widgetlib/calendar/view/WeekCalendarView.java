package com.yxc.widgetlib.calendar.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.widgetlib.calendar.painter.CalendarWeekPainter;
import com.yxc.widgetlib.calendar.painter.InnerMonthPainter;
import com.yxc.widgetlib.calendar.utils.CalendarAttrs;
import com.yxc.widgetlib.calendar.utils.AttrsUtil;
import com.yxc.widgetlib.calendar.utils.Util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class WeekCalendarView extends View {

    private int mLineNum;//行数
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected List<Rect> mRectList;//点击用的矩形集合
    protected List<LocalDate> mDateList;//页面的数据集合
    private LocalDate mSelectDate;//点击选中的日期
    private int selectDayWeek;
    private int todayWeek;
    private CalendarAttrs mAttrs;
    private CalendarWeekPainter mCalendarPainter;
    private int mSelectWeekOfMonth;

    public WeekCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        LocalDate localDate = LocalDate.now();

        this.mInitialDate = this.mSelectDate = localDate;
        mDateList = Util.getMonthLocalDateCalendar(localDate);
        mRectList = new ArrayList<>();
        mLineNum = mDateList.size() / 7; //天数/7
        mSelectWeekOfMonth = Util.getWeekOfMonth(mDateList, mSelectDate);
        this.mCalendarPainter = new InnerMonthPainter(mAttrs);
    }

    public void setSelectDateInvalidate(LocalDate selectDate){
        if (isSameWeekWithSelectDay(selectDate)) {
            return;
        }
        onClick(selectDate);
    }

    public CalendarWeekPainter getCalendarPainter() {
        return mCalendarPainter;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制时获取区间开始结束日期和绘制类Painter
        CalendarWeekPainter painter = getCalendarPainter();
        mRectList.clear();
        selectDayWeek = getSelectDayWeek();
        todayWeek = Util.getTodayWeek();

        for (int i = 0; i < mLineNum; i++) {
            drawSelectWeek(canvas, painter, i);
            for (int j = 0; j < 7; j++) {
                Rect rect = getRect(i, j);
                mRectList.add(rect);
                LocalDate date = mDateList.get(i * 7 + j);
                if (Util.isAfterToday(date)) {
                    painter.onDrawNotCurrentMonth(canvas, rect, date);
                }else if (Util.isSameWeekWithToday(date) || isSameWeekWithSelectDay(date)) {
                    painter.onDrawToday(canvas, rect, date, false);
                } else {
                    painter.onDrawCurrentMonthOrWeek(canvas, rect, date, false);
                }
            }
        }
    }

    private boolean isSameWeekWithSelectDay(LocalDate localDate) {
        int week = Util.getDayWeek(localDate);
        return week == getSelectDayWeek();
    }

    private void drawSelectWeek(Canvas canvas, CalendarWeekPainter painter, int lineNume) {
        LocalDate firstDayOfWeek = mDateList.get(lineNume * 7);
        int currentLineWeek = Util.getDayWeek(firstDayOfWeek);

        boolean isThisYear = firstDayOfWeek.getYear() == LocalDate.now().getYear();

        if (currentLineWeek == selectDayWeek) {
            painter.onDrawWeekBg(canvas, new RectF(getWeekRect(lineNume)), DisplayUtil.dip2px(25), DisplayUtil.dip2px(25), false);
        } else if (currentLineWeek == todayWeek && isThisYear) {//要求年份一样。
            painter.onDrawWeekBg(canvas, new RectF(getWeekRect(lineNume)), DisplayUtil.dip2px(25), DisplayUtil.dip2px(25), true);
        }
    }

    //选中的week
    private int getSelectDayWeek() {
        return Util.getDayWeek(mSelectDate);
    }


    public List<LocalDate> getMonthDateList(){
        return mDateList;
    }

    public LocalDate getSelectDate(){
        return mSelectDate;
    }

    //获取每个元素矩形
    private Rect getRect(int i, int j) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Rect rect;
        //5行的月份，5行矩形平分view的高度  mLineNum==1是周的情况
        if (mLineNum == 5 || mLineNum == 1) {
            int rectHeight = height / mLineNum;
            rect = new Rect(j * width / 7, i * rectHeight, j * width / 7 + width / 7, i * rectHeight + rectHeight);
        } else {
            //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
            //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
            int rectHeight5 = height / 5;
            int rectHeight6 = (height / 5) * 4 / 5;
            rect = new Rect(j * width / 7, i * rectHeight6 + (rectHeight5 - rectHeight6) / 2, j * width / 7 + width / 7,
                    i * rectHeight6 + rectHeight6 + (rectHeight5 - rectHeight6) / 2);
        }
        return rect;
    }

    //获取每个元素矩形
    private Rect getWeekRect(int i) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Rect rect;
        //5行的月份，5行矩形平分view的高度  mLineNum==1是周的情况
        int rectHeight5 = height / 5;
        int rectHeight6 = (height / 5) * 4 / 5;
        int heightValue = (rectHeight5 - rectHeight6) / 2;
        if (mLineNum == 5 || mLineNum == 1) {
            int rectHeight = height / mLineNum;
            rect = new Rect(DisplayUtil.dip2px(8), i * rectHeight + heightValue + DisplayUtil.dip2px(3),
                    width - DisplayUtil.dip2px(8), i * rectHeight + rectHeight - heightValue - DisplayUtil.dip2px(3));
        } else {
            //6行的月份，要第一行和最后一行矩形的中心分别和和5行月份第一行和最后一行矩形的中心对齐
            //5行一个矩形高度 mHeight/5, 画图可知,4个5行矩形的高度等于5个6行矩形的高度  故：6行的每一个矩形高度是  (mHeight/5)*4/5
            rect = new Rect(DisplayUtil.dip2px(8), i * rectHeight6 + heightValue + DisplayUtil.dip2px(3),
                    width - DisplayUtil.dip2px(8), i * rectHeight6 + rectHeight6 + heightValue - DisplayUtil.dip2px(3));
        }
        return rect;
    }


    //获取当前页面的初始日期
    public LocalDate getInitialDate() {
        return mInitialDate;
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
                    LocalDate localDate = mDateList.get(i);
//                    onClick(localDate);
                    break;
                }
            }
            return true;
        }
    });


    private void calculateData(LocalDate localDate) {
        mDateList = Util.getMonthLocalDateCalendar(localDate);
        mLineNum = mDateList.size() / 7;
    }

    protected void onClick(LocalDate localDate) {
        if (Util.isLastMonth( mSelectDate, localDate)) {
            mSelectDate = localDate;
            animatorChange(localDate, 0, -DisplayUtil.getScreenWidth(getContext()), 300);
        } else if (!Util.isAfterToday(localDate)) {
            if (Util.isNextMonth(localDate, mSelectDate)) {
                mSelectDate = localDate;
                animatorChange(localDate, 0, DisplayUtil.getScreenWidth(getContext()), 300);
            } else {
                mSelectDate = localDate;
                invalidateViewAndData(localDate);
            }
        } else if (Util.isSameWeekWithToday(localDate)) {//未来但是跟今天是同一周的情况。
            if (Util.isNextMonth(localDate, mSelectDate)) {
                mSelectDate = localDate;
                animatorChange(localDate, 0, DisplayUtil.getScreenWidth(getContext()), 300);
            } else {
                mSelectDate = localDate;
                invalidateViewAndData(localDate);
            }
        }
    }

    //换月的时候添加动画切换
    private void animatorChange(final LocalDate localDate, final float begin, final float end, final long duration){
        ObjectAnimator animator = ObjectAnimator.ofFloat(
                this, "translationX", begin,  end);
        animator.setDuration(duration * 2/3);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
        animator.addListener(new MyAnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animation) {
                calculateData(localDate);
                invalidateViewAndData(localDate);
                ObjectAnimator animatorInner = ObjectAnimator.ofFloat(
                        WeekCalendarView.this, "translationX",  end, begin);
                animatorInner.setDuration(duration * 4/3);
                animatorInner.setInterpolator(new DecelerateInterpolator());
                animatorInner.start();
            }
        });
    }

    private void invalidateViewAndData(LocalDate localDate){
        if (null != mWeekCalendarItemSelectListener){
            mWeekCalendarItemSelectListener.onWeekItemSelect(localDate);
        }
        invalidate();
    }

    private OnWeekCalendarItemSelectListener mWeekCalendarItemSelectListener;

    public interface OnWeekCalendarItemSelectListener{
        void onWeekItemSelect(LocalDate localDate);
    }

    public void setOnWeekCalendarItemSelectListener(OnWeekCalendarItemSelectListener listener){
        this.mWeekCalendarItemSelectListener = listener;
    }



}
