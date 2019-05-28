package com.yxc.widgetlib.calendar.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.widgetlib.calendar.painter.CalendarPainter;
import com.yxc.widgetlib.calendar.painter.InnerPainter;
import com.yxc.widgetlib.calendar.utils.CalendarAttrs;
import com.yxc.widgetlib.calendar.utils.AttrsUtil;
import com.yxc.widgetlib.calendar.utils.Util;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class DayCalendarView extends View {

    private int mLineNum;//行数
    protected LocalDate mInitialDate;//由mInitialDate和周开始的第一天 算出当前页面的数据
    protected List<Rect> mRectList;//点击用的矩形集合
    protected List<LocalDate> mDateList;//页面的数据集合
    private LocalDate mSelectDate;//点击选中的日期
    private CalendarAttrs mAttrs;
    private CalendarPainter mCalendarPainter;

    public DayCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = AttrsUtil.getAttrs(context, attrs);
        LocalDate localDate = LocalDate.now();

        this.mInitialDate = this.mSelectDate = localDate;
        mDateList = Util.getMonthLocalDateCalendar(localDate);
        mRectList = new ArrayList<>();
        mLineNum = mDateList.size() / 7; //天数/7
        this.mCalendarPainter = new InnerPainter(mAttrs);
    }

    public CalendarPainter getCalendarPainter() {
        return mCalendarPainter;
    }

    public LocalDate getSelectLocalDate(){
        return mSelectDate;
    }

    public void setSelectDateInvalidate(LocalDate selectDate){
        if (isSameDay(selectDate)) {
            return;
        }
        onClick(selectDate);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制时获取区间开始结束日期和绘制类Painter
        CalendarPainter painter = getCalendarPainter();
        mRectList.clear();

        for (int i = 0; i < mLineNum; i++) {
            for (int j = 0; j < 7; j++) {
                Rect rect = getRect(i, j);
                mRectList.add(rect);
                LocalDate date = mDateList.get(i * 7 + j);

                if (Util.isAfterToday(date)) {
                    painter.onDrawNotCurrentMonth(canvas, rect, date);
                } else if (Util.isToday(date) && date.equals(mSelectDate)) {
                    painter.onDrawToday(canvas, rect, date, true);
                } else if (Util.isToday(date) && !date.equals(mSelectDate)) {
                    painter.onDrawToday(canvas, rect, date, false);
                } else if (date.equals(mSelectDate)) {//如果默认选择，就绘制，如果默认不选择且不是点击，就不绘制
                    painter.onDrawCurrentMonthOrWeek(canvas, rect, date, true);
                } else {
                    painter.onDrawCurrentMonthOrWeek(canvas, rect, date, false);
                }
            }
        }
    }


    //获取每个元素矩形
    private Rect getRect(int i, int j) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        Rect rect;
        //5行的月份，5行矩形平分view的高度
        if (mLineNum == 5) {
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
                    if (!isSameDay(localDate)){
                        // 点击事件去掉
                        onClick(localDate);
                    }
                    break;
                }
            }
            return true;
        }
    });

    private boolean isSameDay(LocalDate localDate){
        return localDate.getMonthOfYear() == mSelectDate.getMonthOfYear() && localDate.getDayOfMonth() == mSelectDate.getDayOfMonth();
    }

    /**
     * 点击事件
     *
     * @param localDate 当前点击的页面
     */
    protected  void onClick(final LocalDate localDate){
        if (Util.isLastMonth(mSelectDate, localDate)) {
             mSelectDate = localDate;
             animatorChange(localDate, 0, -DisplayUtil.getScreenWidth(getContext()), 300);
        } else if (!Util.isAfterToday(localDate)) {//不是未来，有数据
            if (Util.isNextMonth(localDate, mSelectDate)) {//下一个月
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
                        DayCalendarView.this, "translationX",  end, begin);
                animatorInner.setDuration(duration * 4/3);
                animatorInner.setInterpolator(new DecelerateInterpolator());
                animatorInner.start();
            }
        });
    }

    //需要重新计算数据，用来刷新页面。
    private void calculateData(LocalDate localDate) {
        mDateList = Util.getMonthLocalDateCalendar(localDate);
        mLineNum = mDateList.size() / 7;
    }


    private void invalidateViewAndData(LocalDate localDate){
        if (null != mWeekCalendarItemSelectListener){
            mWeekCalendarItemSelectListener.onDayItemSelect(localDate);
        }
        invalidate();
    }

    private OnDayCalendarItemSelectListener mWeekCalendarItemSelectListener;

    public interface OnDayCalendarItemSelectListener{
        void onDayItemSelect(LocalDate localDate);
    }

    public void setOnDayCalendarItemSelectListener(OnDayCalendarItemSelectListener listener){
        this.mWeekCalendarItemSelectListener = listener;
    }

}
