package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.SleepChartAttrs;
import com.yxc.chartlib.entrys.SleepItemEntry;
import com.yxc.chartlib.entrys.model.SleepItemTime;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.commonlib.util.TextUtil;
import com.yxc.commonlib.util.TimeDateUtil;


/**
 * @author yxc
 * @since 2019/4/14
 */
final public class SleepChartRender {

    private SleepChartAttrs mChartAttrs;

    private Paint mBarChartPaint;
    private Paint mTextPaint;
    protected Paint mHighLightValuePaint;

    protected ValueFormatter mHighLightValueFormatter;
    public SleepChartRender(SleepChartAttrs sleepChartAttrs, ValueFormatter highLightValueFormatter) {
        this.mChartAttrs = sleepChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initHighLightLeftPaint();
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mChartAttrs.txtSize);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setColor(mChartAttrs.txtColor);
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mChartAttrs.deepSleepColor);
    }

    private void initHighLightLeftPaint() {
        mHighLightValuePaint = new Paint();
        mHighLightValuePaint.reset();
        mHighLightValuePaint.setAntiAlias(true);
        mHighLightValuePaint.setStyle(Paint.Style.FILL);
        mHighLightValuePaint.setStrokeWidth(1);
        mHighLightValuePaint.setColor(Color.WHITE);
        mHighLightValuePaint.setTextSize(DisplayUtil.dip2px(12));
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
    }


    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawSleepChart(final Canvas canvas, @NonNull final RecyclerView parent) {
        float parentRight = parent.getWidth() - parent.getPaddingRight();
        float parentLeft = parent.getPaddingLeft();
        float contentWidth = parent.getWidth() - parent.getPaddingStart() - parent.getPaddingEnd();
        float distanceHeight = mChartAttrs.sleepItemHeight;
        float chartTop = parent.getTop() + parent.getPaddingTop();
        final int childCount = parent.getChildCount();
        int sumWidth = 0;
        long sumWake = 0;
        long sumDeepSleep = 0;
        long sumSlumber = 0;
        SleepItemEntry latestSleepEntry = null;
        SleepItemEntry longestSleepEntry = null;

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            SleepItemEntry sleepEntry = (SleepItemEntry) child.getTag();
            SleepItemTime sleepTime = sleepEntry.sleepItemTime;
            if (i == 0) {
                latestSleepEntry = sleepEntry;
            } else if (i == childCount - 1) {
                longestSleepEntry = sleepEntry;
            }
            long timeDistance = sleepTime.getSleepTime();
            float end = parentRight - sumWidth;
            float start = end - child.getWidth();
            sumWidth += child.getWidth();
            drawSleepChartItem(canvas, sleepTime, chartTop, start, end);
            sumWake += timeDistance;
        }

        if (sumWidth < contentWidth) {//补一段填满。
            int colorOrigin = mBarChartPaint.getColor();
            float start = parentLeft;
            float end = parentLeft + (contentWidth - sumWidth);
            float rectFTop = getSleepRectTop(SleepItemTime.TYPE_WAKE, chartTop);
            mBarChartPaint.setColor(mChartAttrs.weakColor);
            drawRectF(canvas, mBarChartPaint, start, rectFTop, end, rectFTop + distanceHeight);
            mBarChartPaint.setColor(colorOrigin);
        }
        float rectFTextTop = chartTop + 4 * distanceHeight + mChartAttrs.contentPaddingBottom;
        if (longestSleepEntry.sleepItemTime != null) {
            String leftStr = TimeDateUtil.getDateStr(longestSleepEntry.sleepItemTime.startTimestamp, "HH:mm") + " 入睡";
            float leftRectFStart = parentLeft;
            float leftRectFEnd = leftRectFStart + mTextPaint.measureText(leftStr);
            drawTopTime(canvas, leftStr, rectFTextTop, leftRectFStart, leftRectFEnd);
        }
        if (latestSleepEntry.sleepItemTime != null) {
            String rightStr = TimeDateUtil.getDateStr(latestSleepEntry.sleepItemTime.endTimestamp, "HH:mm") + " 醒来";
            float rightRectFEnd = parentRight;
            float rightRectFStart = parentRight - mTextPaint.measureText(rightStr);
            drawTopTime(canvas, rightStr, rectFTextTop, rightRectFStart, rightRectFEnd);
        }
    }

    private void drawSleepChartItem(Canvas canvas, SleepItemTime sleepItemTime, float chartTop, float start, float end) {
        float distanceHeight = mChartAttrs.sleepItemHeight;
        float rectFTop = getSleepRectTop(sleepItemTime.sleepType, chartTop);
        int color = sleepItemTime.getChartColor(mChartAttrs);
        int colorOrigin = mBarChartPaint.getColor();
        mBarChartPaint.setColor(color);
        drawRectF(canvas, mBarChartPaint, start, rectFTop, end, rectFTop + distanceHeight);
        mBarChartPaint.setColor(colorOrigin);
    }

    private float getSleepRectTop(int sleepType, float parentTop) {
        float distanceHeight = mChartAttrs.sleepItemHeight;
        float rectFTop = parentTop + sleepType * distanceHeight;
        return rectFTop;
    }

    private void drawTopTime(Canvas canvas, String txtStr, float rectFTextTop, float rectFLeft, float rectFRight) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        float txtHeight = bottom - top;
        float txtCenter = (top + bottom) / 2;
        float rectFBottom = rectFTextTop + txtHeight;
        RectF rectFTimeTop = new RectF(rectFLeft, rectFTextTop, rectFRight, rectFBottom);
        int baseYLine = (int) (rectFTimeTop.centerY() - txtCenter);
        canvas.drawText(txtStr, rectFTimeTop.left, baseYLine, mTextPaint);
    }

    private void drawRectF(Canvas canvas, Paint paint, float start, float top, float right, float bottom) {
        RectF rectF = new RectF();
        rectF.set(start, top, right, bottom);
        canvas.drawRect(rectF, paint);
    }


    //绘制选中时 highLight 标线及浮框。
    public void drawHighLight(Canvas canvas, @NonNull RecyclerView parent) {
        if (mChartAttrs.enableValueMark) {
            float parentTop = parent.getPaddingTop();
            int childCount = parent.getChildCount();


            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                SleepItemEntry barEntry = (SleepItemEntry) child.getTag();
                SleepItemTime sleepItemTime = barEntry.sleepItemTime;

                float width = child.getWidth();
                float top = getSleepRectTop(sleepItemTime.sleepType, parentTop);
                float childCenter = child.getLeft() + width / 2;
                String valueStr = mHighLightValueFormatter.getBarLabel(barEntry);

                if (barEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    int chartColor = sleepItemTime.getChartColor(mChartAttrs);
                    float rectFHeight = drawHighLightValue(canvas, valueStr, childCenter, parent, chartColor);
                    float[] points = new float[]{childCenter, top, childCenter, rectFHeight};
                    drawHighLightLine(canvas, points, chartColor);

                }
            }
        }
    }


    private float drawHighLightValue(Canvas canvas, String valueStr, float childCenter,
                                    RecyclerView parent, int barChartColor) {

        float contentRight = parent.getWidth() - parent.getPaddingRight();
        float contentLeft = parent.getPaddingLeft();

        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
        float rightEdgeDistance = Math.abs(contentRight - childCenter);

        float leftPadding = DisplayUtil.dip2px(8);
        float rightPadding = DisplayUtil.dip2px(8);
        float centerPadding = DisplayUtil.dip2px(16);
        float txtTopPadding = DisplayUtil.dip2px(8);

        String leftStr = strings[0];
        String rightStr = strings[1];

        float txtLeftWidth = mHighLightValuePaint.measureText(leftStr);
        float txtRightWidth = mHighLightValuePaint.measureText(rightStr);

        float rectFHeight = TextUtil.getTxtHeight1(mHighLightValuePaint) + txtTopPadding * 2;
        float txtWidth = txtLeftWidth + txtRightWidth + leftPadding + rightPadding + centerPadding;

        float edgeDistance = txtWidth / 2.0f;
        float rectTop = parent.getTop();
        float rectBottom = rectTop + rectFHeight;

        //绘制RectF
        RectF rectF = new RectF();
        mBarChartPaint.setColor(barChartColor);
        if (leftEdgeDistance <= edgeDistance) {//矩形框靠左对齐
            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
        } else if (rightEdgeDistance <= edgeDistance) {//矩形框靠右对齐
            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
        } else {//居中对齐。
            rectF.set(childCenter - edgeDistance, rectTop, childCenter + edgeDistance, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
        }

        //绘文字
        RectF leftRectF = new RectF(rectF.left + leftPadding, rectTop + txtTopPadding,
                rectF.left + leftPadding + txtLeftWidth, rectTop + txtTopPadding + rectFHeight);
        mHighLightValuePaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fontMetrics = mHighLightValuePaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (leftRectF.centerY()+(top + bottom)/2);//基线中间点的y轴计算公式
        canvas.drawText(leftStr, rectF.left + leftPadding, baseLineY, mHighLightValuePaint);

        float dividerLineStartX = rectF.left + leftPadding + txtLeftWidth + centerPadding / 2.0f;
        float dividerLineStartY = rectTop + DisplayUtil.dip2px(10);
        float dividerLineEndX = dividerLineStartX;
        float dividerLineEndY = rectBottom - DisplayUtil.dip2px(10);
        float[] lines = new float[]{dividerLineStartX, dividerLineStartY, dividerLineEndX, dividerLineEndY};
        canvas.drawLines(lines, mHighLightValuePaint);

        float rightRectFStart = rectF.left + leftPadding + txtLeftWidth + centerPadding;
        RectF rightRectF = new RectF(rightRectFStart, rectTop + txtTopPadding, rectF.right - rightPadding, rectBottom - txtTopPadding);
        canvas.drawText(rightStr, rightRectF.left, baseLineY, mHighLightValuePaint);

        return rectFHeight;
    }


    private void drawHighLightLine(Canvas canvas, float[] floats, int barChartColor) {
        Paint.Style previous = mBarChartPaint.getStyle();
        float strokeWidth = mBarChartPaint.getStrokeWidth();
        int color = mBarChartPaint.getColor();
        // set
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mBarChartPaint.setColor(barChartColor);
        canvas.drawLines(floats, mBarChartPaint);
        // restore
        mBarChartPaint.setStyle(previous);
        mBarChartPaint.setStrokeWidth(strokeWidth);
        mBarChartPaint.setColor(color);
    }


}
