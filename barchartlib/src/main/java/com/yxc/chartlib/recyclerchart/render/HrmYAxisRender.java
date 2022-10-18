package com.yxc.chartlib.recyclerchart.render;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.recyclerchart.attrs.BaseChartAttrs;
import com.yxc.chartlib.recyclerchart.component.BaseYAxis;

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
}
