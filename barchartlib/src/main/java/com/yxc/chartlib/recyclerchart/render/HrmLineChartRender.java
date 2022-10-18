package com.yxc.chartlib.recyclerchart.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.recyclerchart.attrs.LineChartAttrs;
import com.yxc.chartlib.recyclerchart.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.recyclerchart.component.YAxis;
import com.yxc.chartlib.recyclerchart.entrys.BarEntry;
import com.yxc.chartlib.recyclerchart.formatter.ValueFormatter;
import com.yxc.chartlib.recyclerchart.util.ChartComputeUtil;
import com.yxc.commonlib.util.DisplayUtil;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/14
 */
final public class HrmLineChartRender extends LineChartRender {
    private LineChartAttrs mLineChartAttrs;
    private Paint mLineChartPaint;
    private Paint mTextPaint;
    private Paint mLineFillPaint;
    protected Paint mHighLightValuePaint;

    protected ValueFormatter mHighLightValueFormatter;

    public HrmLineChartRender(LineChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, highLightValueFormatter);
        this.mLineChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initLineFillPaint();
        initHighLightPaint();
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mLineChartAttrs.txtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }


    private void initBarChartPaint() {
        mLineChartPaint = new Paint();
        mLineChartPaint.reset();
        mLineChartPaint.setAntiAlias(true);
        mLineChartPaint.setStyle(Paint.Style.FILL);
        mLineChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mLineChartPaint.setColor(mLineChartAttrs.chartColor);
    }

    private void initLineFillPaint() {
        mLineFillPaint = new Paint();
        mLineFillPaint.reset();
        mLineFillPaint.setAntiAlias(true);
        mLineFillPaint.setStyle(Paint.Style.FILL);
        mLineFillPaint.setColor(mLineChartAttrs.chartColor);
    }

    private void initHighLightPaint() {
        mHighLightValuePaint = new Paint();
        mHighLightValuePaint.reset();
        mHighLightValuePaint.setAntiAlias(true);
        mHighLightValuePaint.setStyle(Paint.Style.FILL);
        mHighLightValuePaint.setStrokeWidth(1);
        mHighLightValuePaint.setColor(Color.WHITE);
        mHighLightValuePaint.setTextSize(DisplayUtil.dip2px(12));
    }

    @Override
    public <T extends BarEntry> void drawLineChart(Canvas canvas, RecyclerView parent, YAxis mYAxis) {
        drawLineChartWithoutPoint(canvas, parent, mYAxis);
    }

    private <T extends BarEntry> void drawLineChartWithoutPoint(Canvas canvas, RecyclerView parent, YAxis mYAxis) {
        final float parentRightBoard = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();
        BaseBarChartAdapter adapter = (BaseBarChartAdapter) parent.getAdapter();
        List<T> entryList = adapter.getEntries();
        final int childCount = parent.getChildCount();

        int adapterPosition;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            T barEntry = (T) child.getTag();
            if (barEntry.getY() == 0) {
                continue;
            }
            adapterPosition = parent.getChildAdapterPosition(child);
            RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mLineChartAttrs, barEntry);
            PointF pointF2 = new PointF((rectF.left + rectF.right) / 2, rectF.top);
            //pointF2 即为当前for 循环到的点，从左往右一共涉及到4个点 pointF0, pointF1, pointF2, pointF3; 其中 pointF1、pointF2为显示的。
            if (i < childCount - 1) {
                View pointF1Child = parent.getChildAt(i + 1);
                T barEntryLeft = (T) pointF1Child.getTag();
                RectF rectFLeft = ChartComputeUtil.getBarChartRectF(pointF1Child, parent, mYAxis, mLineChartAttrs, barEntryLeft);
                PointF pointF1 = new PointF((rectFLeft.left + rectFLeft.right) / 2, rectFLeft.top);

                if (pointF1.x >= parentLeft && pointF2.x <= parentRightBoard) {
                    float[] pointsOut = new float[]{pointF1.x, pointF1.y, pointF2.x, pointF2.y};
                    drawChartLine(canvas, pointsOut);
                    if (pointF1Child.getLeft() < parentLeft) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition + 2 < entryList.size()) {
                            float x = pointF1.x - pointF1Child.getWidth();
                            T barEntry0 = entryList.get(adapterPosition + 2);
                            float y = ChartComputeUtil.getYPosition(barEntry0, parent, mYAxis, mLineChartAttrs);
                            PointF pointF0 = new PointF(x, y);
                            PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF0, pointF1, parentLeft);
                            float[] points = new float[]{pointFIntercept.x, pointFIntercept.y, pointF1.x, pointF1.y};
                            drawChartLine(canvas, points);
                        }
                    } else if (child.getRight() < parentRightBoard && parentRightBoard - child.getRight() <= child.getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        if (adapterPosition - 1 > 0) {
                            T barEntryRight = entryList.get(adapterPosition - 1);
                            float x = pointF2.x + child.getWidth();
                            float y = ChartComputeUtil.getYPosition(barEntryRight, parent, mYAxis, mLineChartAttrs);
                            PointF pointF3 = new PointF(x, y);

                            if (pointF3.x > parentRightBoard) {
                                PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF2, pointF3, parentRightBoard);
                                float[] points = new float[]{pointF2.x, pointF2.y, pointFIntercept.x, pointFIntercept.y};
                                drawChartLine(canvas, points);
                            } else if (pointF3.x < parentRightBoard) {
                                if (adapterPosition - 2 > 0) {
                                    float xInner = pointF3.x + child.getWidth();
                                    T barEntry4 = entryList.get(adapterPosition - 2);
                                    float yInner = ChartComputeUtil.getYPosition(barEntry4, parent, mYAxis, mLineChartAttrs);
                                    PointF pointF4 = new PointF(xInner, yInner);

                                    PointF pointFInterceptInner = ChartComputeUtil.getInterceptPointF(pointF3, pointF4, parentRightBoard);
                                    float[] pointsInner = new float[]{pointF3.x, pointF3.y, pointFInterceptInner.x, pointFInterceptInner.y};
                                    drawChartLine(canvas, pointsInner);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentLeft && pointF1Child.getRight() >= parentLeft) {//左边界，处理pointF1值没有显示出来
                    PointF pointF = ChartComputeUtil.getInterceptPointF(pointF1, pointF2, parentLeft);
                    float[] points = new float[]{pointF.x, pointF.y, pointF2.x, pointF2.y};
                    drawChartLine(canvas, points);
                }
            }
        }
    }

    private void drawChartLine(Canvas canvas, float[] points) {
        int color = mLineChartPaint.getColor();
        mLineChartPaint.setColor(mLineChartAttrs.chartColor);
        canvas.drawLines(points, mLineChartPaint);
        mLineChartPaint.setColor(color);
    }

}
