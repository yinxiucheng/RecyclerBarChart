package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yxc.chartlib.attrs.SleepChartAttrs;
import com.yxc.chartlib.entrys.SleepEntry;

/**
 * @author yxc
 * @since 2019/4/14
 *
 */
final public class SleepChartRender {

    private SleepChartAttrs mBarChartAttrs;

    private Paint mSleepPaint;

    public SleepChartRender(SleepChartAttrs sleepChartAttrs) {
        this.mBarChartAttrs = sleepChartAttrs;
        initSleepPaint();
    }

    private void initSleepPaint() {
        mSleepPaint = new Paint();
        mSleepPaint.reset();
        mSleepPaint.setAntiAlias(true);
        mSleepPaint.setStyle(Paint.Style.FILL);
        mSleepPaint.setColor(mBarChartAttrs.deepSleepColor);
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawSleepChart(final Canvas canvas, @NonNull final RecyclerView parent) {

        int parentHeight = parent.getMeasuredHeight();
        int parentBottom = parent.getBottom();
        int rectFTop = parentBottom - parentHeight;
        int distanceHeight = parentHeight / 3;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {

            View child = parent.getChildAt(i);
            SleepEntry sleepEntry = (SleepEntry) child.getTag();
            if (sleepEntry.type == SleepEntry.TYPE_DEEP_SLEEP) {
                mSleepPaint.setColor(mBarChartAttrs.deepSleepColor);
                rectFTop = rectFTop + 2 * distanceHeight;
            } else if (sleepEntry.type == SleepEntry.TYPE_SLUMBER) {
                mSleepPaint.setColor(mBarChartAttrs.slumberColor);
                rectFTop = rectFTop + distanceHeight;
            } else {
                mSleepPaint.setColor(mBarChartAttrs.weakColor);
            }
            RectF rectF = new RectF();
            rectF.set(child.getLeft(), rectFTop, child.getRight(), parentBottom);
            canvas.drawRect(rectF, mSleepPaint);
        }
    }

}
