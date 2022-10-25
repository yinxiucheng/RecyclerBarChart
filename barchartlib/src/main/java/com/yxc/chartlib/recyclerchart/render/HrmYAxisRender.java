package com.yxc.chartlib.recyclerchart.render;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.recyclerchart.attrs.BaseChartAttrs;
import com.yxc.chartlib.recyclerchart.component.BaseYAxis;

import java.util.HashMap;
import java.util.Map;

public class HrmYAxisRender<T extends BaseYAxis, V extends BaseChartAttrs> extends YAxisRender<T, V> {

    protected Paint mLinePaint;
    private Paint mDashPaint;
    protected Paint mTextPaint;
    protected V mBarChartAttrs;

    public HrmYAxisRender(V barChartAttrs) {
        super(barChartAttrs);
        this.mBarChartAttrs = barChartAttrs;
        initPaint();
        initTextPaint();
        initDathPaint();
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

    private void initDathPaint() {
        mDashPaint = new Paint();
        mDashPaint.reset();
        mDashPaint.setAntiAlias(true);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(1);
        PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        mDashPaint.setPathEffect(pathEffect);
        mDashPaint.setColor(mBarChartAttrs.xAxisThirdDividerColor);
    }


    //绘制 Y轴刻度线 横的网格线
    @Override
    public void drawHorizontalLine(Canvas canvas, RecyclerView parent, T yAxis) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        mLinePaint.setColor(yAxis.getGridColor());
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        float distance = bottom - mBarChartAttrs.contentPaddingBottom - mBarChartAttrs.contentPaddingTop - top;
        int lineNums = yAxis.getLabelCount();
        float lineDistance = distance / lineNums;
        float itemLineDistance = lineDistance/5.0f;
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
                if (i < lineNums){
                    float baseYItem = gridLine;
                    for (int j = 0; j < 4; j++) {
                        baseYItem += itemLineDistance;
                        Path itemPath = new Path();
                        itemPath.moveTo(left, baseYItem);
                        itemPath.lineTo(right, baseYItem);
                        canvas.drawPath(itemPath, mDashPaint);
                    }
                }
                canvas.drawPath(path, mLinePaint);
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
}
