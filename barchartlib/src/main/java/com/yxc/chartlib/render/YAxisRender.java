package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.commonlib.util.DisplayUtil;

import java.util.HashMap;
import java.util.Map;

public class YAxisRender<T extends BaseYAxis> {

    protected Paint mLinePaint;
    protected Paint mTextPaint;
    protected BarChartAttrs mBarChartAttrs;

    public YAxisRender(BarChartAttrs barChartAttrs) {
        this.mBarChartAttrs = barChartAttrs;
        initPaint();
        initTextPaint();
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mBarChartAttrs.yAxisLabelTxtColor);
        mTextPaint.setTextSize(mBarChartAttrs.yAxisLabelTxtSize);
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.reset();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(mBarChartAttrs.yAxisLineColor);
    }


    //绘制 Y轴刻度线 横的网格线
    public void drawHorizontalLine(Canvas canvas, RecyclerView parent, T yAxis) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        mLinePaint.setColor(yAxis.getGridColor());
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        float distance = bottom - mBarChartAttrs.contentPaddingBottom - mBarChartAttrs.contentPaddingTop - top;
        int lineNums = yAxis.getLabelCount();
        float lineDistance = distance / lineNums;
        float gridLine = top + mBarChartAttrs.contentPaddingTop;
        for (int i = 0; i <= lineNums; i++) {
            if (i > 0) {
                gridLine = gridLine + lineDistance;
            }
            Path path = new Path();
            path.moveTo(left, gridLine);
            path.lineTo(right, gridLine);

            boolean enable = false;
            if (i == lineNums && mBarChartAttrs.enableYAxisZero) {
                enable = true;
            } else {
                enable = mBarChartAttrs.enableYAxisGridLine;//允许画 Y轴刻度
            }
            if (enable) {
                canvas.drawPath(path, mLinePaint);
            }
        }
    }

    //绘制左边的刻度
    public void drawLeftYAxisLabel(Canvas canvas, RecyclerView parent, T yAxis) {
        if (mBarChartAttrs.enableLeftYAxisLabel) {
            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();

            mTextPaint.setTextSize(yAxis.getTextSize());
            String longestStr = yAxis.getLongestLabel();

            float yAxisWidth = mTextPaint.measureText(longestStr) + mBarChartAttrs.recyclerPaddingLeft;

            int paddingLeft = computeYAxisWidth(parent.getPaddingLeft(), yAxisWidth);
            //设置 recyclerView的 BarChart 内容区域
            parent.setPadding(paddingLeft, parent.getPaddingTop(), parent.getPaddingRight(), parent.getPaddingBottom());

            float topLocation = top + mBarChartAttrs.contentPaddingTop;
            float containerHeight = bottom - mBarChartAttrs.contentPaddingBottom - topLocation;
            float itemHeight = containerHeight / yAxis.getLabelCount();
            HashMap<Float, Float> yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.getLabelCount());

            for (Map.Entry<Float, Float> entry : yAxisScaleMap.entrySet()) {
                float yAxisScaleLocation = entry.getKey();
                float yAxisScaleValue = entry.getValue();
                String labelStr = yAxis.getValueFormatter().getFormattedValue(yAxisScaleValue);

                float txtY = yAxisScaleLocation + yAxis.labelVerticalPadding;
                float txtX = yAxisWidth - mTextPaint.measureText(labelStr) - yAxis.labelHorizontalPadding;
                canvas.drawText(labelStr, txtX, txtY, mTextPaint);
            }
        }
    }

    //绘制右边的刻度
    public void drawRightYAxisLabel(Canvas canvas, RecyclerView parent, T yAxis) {
        if (mBarChartAttrs.enableRightYAxisLabel) {
            int right = parent.getWidth();
            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();

            mTextPaint.setTextSize(yAxis.getTextSize());
            String longestStr = yAxis.getLongestLabel();
            float yAxisWidth = mTextPaint.measureText(longestStr) + mBarChartAttrs.recyclerPaddingRight;

            int paddingRight = computeYAxisWidth(parent.getPaddingRight(), yAxisWidth);
            //设置 recyclerView的 BarChart 内容区域
            parent.setPadding(parent.getPaddingLeft(), parent.getPaddingTop(), paddingRight, parent.getPaddingBottom());

            float topLocation = top + mBarChartAttrs.contentPaddingTop;
            float containerHeight = bottom - mBarChartAttrs.contentPaddingBottom - topLocation;
            float itemHeight = containerHeight / yAxis.getLabelCount();
            HashMap<Float, Float> yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.getLabelCount());

            float txtX = right - parent.getPaddingRight() + yAxis.labelHorizontalPadding;

            for (Map.Entry<Float, Float> entry : yAxisScaleMap.entrySet()) {
                float yAxisScaleLocation = entry.getKey();
                float yAxisScaleValue = entry.getValue();
                String labelStr = yAxis.getValueFormatter().getFormattedValue(yAxisScaleValue);
                float txtY = yAxisScaleLocation + yAxis.labelVerticalPadding;
                canvas.drawText(labelStr, txtX, txtY, mTextPaint);
            }
        }
    }

    private int computeYAxisWidth(int originPadding, float yAxisWidth) {
        float resultPadding;
        Log.d("YAxis1", "originPadding:" + originPadding + " yAxisWidth:" + yAxisWidth);
        if (originPadding > yAxisWidth) {
            float distance = originPadding - yAxisWidth;
            if (distance > DisplayUtil.dip2px(8)) {
                Log.d("YAxis", "if control originPadding:" + originPadding + " yAxisWidth:" + yAxisWidth);
                resultPadding = yAxisWidth;//实际需要的跟原来差8dp了就用，实际测量的，否则就用原来的
            } else {
                Log.d("YAxis", "else control originPadding:" + originPadding + " yAxisWidth:" + yAxisWidth);
                resultPadding = originPadding;
            }
        } else {//原来设定的 padding 不够用
            Log.d("YAxis", "control originPadding:" + originPadding + " yAxisWidth:" + yAxisWidth);
            resultPadding = yAxisWidth;
        }
        return (int) resultPadding;
    }
}
