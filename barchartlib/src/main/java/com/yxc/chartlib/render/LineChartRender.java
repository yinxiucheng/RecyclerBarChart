package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.LineChartAttrs;
import com.yxc.chartlib.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.itemdecoration.LineChartDrawable;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.commonlib.util.TextUtil;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/14
 */
final public class LineChartRender {
    private LineChartAttrs mLineChartAttrs;
    private Paint mLineChartPaint;
    private Paint mTextPaint;
    private Paint mLineFillPaint;
    protected Paint mHighLightValuePaint;

    protected ValueFormatter mHighLightValueFormatter;

    public LineChartRender(LineChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
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

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    //绘制选中时 highLight 标线及浮框。
    public void drawHighLight(Canvas canvas, @NonNull RecyclerView parent, YAxis yAxis) {
        if (mLineChartAttrs.enableValueMark) {
            float parentTop = parent.getPaddingTop();
            float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mLineChartAttrs.contentPaddingBottom;
            int childCount = parent.getChildCount();
            float contentRight = parent.getWidth() - parent.getPaddingRight();
            float contentLeft = parent.getPaddingLeft();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                BarEntry barEntry = (BarEntry) child.getTag();
                float width = child.getWidth();
                float childCenter = child.getLeft() + width / 2;
                String valueStr = mHighLightValueFormatter.getBarLabel(barEntry);
                float[] points = new float[]{childCenter, contentBottom, childCenter, parentTop};
                if (mLineChartAttrs.lineHighLightType == LineChartAttrs.HIGH_LIGHT_LINE_Y) {
                    float yPosition = ChartComputeUtil.getYPosition(barEntry, parent, yAxis, mLineChartAttrs);
                    points = new float[]{childCenter, yPosition - mLineChartAttrs.linePointSelectRadius, childCenter, parentTop};
                }
                if (barEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    int chartColor = mLineChartAttrs.chartColor;
                    drawHighLightLine(canvas, points, chartColor);
                    drawHighLightValue(canvas, valueStr, childCenter, contentLeft, contentRight, parentTop, chartColor);
                }
            }
        }
    }

    //绘制柱状图顶部value文字
    private void drawHighLightValue(Canvas canvas, String valueStr, float childCenter,
                                    float contentLeft, float contentRight, float contentTop, int barChartColor) {
        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
        float rightEdgeDistance = Math.abs(contentRight - childCenter);

        float leftPadding = DisplayUtil.dip2px(8);
        float rightPadding = DisplayUtil.dip2px(8);
        float centerPadding = DisplayUtil.dip2px(16);

        float rectBottom = contentTop;
        float txtTopPadding = DisplayUtil.dip2px(8);

        String leftStr = strings[0];
        String rightStr = strings[1];

        float txtLeftWidth = mHighLightValuePaint.measureText(leftStr);
        float txtRightWidth = mHighLightValuePaint.measureText(rightStr);

        float rectFHeight = TextUtil.getTxtHeight1(mHighLightValuePaint) + txtTopPadding * 2;
        float txtWidth = txtLeftWidth + txtRightWidth + leftPadding + rightPadding + centerPadding;

        float edgeDistance = txtWidth / 2.0f;
        float rectTop = contentTop - rectFHeight;

        //绘制RectF
        RectF rectF = new RectF();
        mLineChartPaint.setColor(barChartColor);
        if (leftEdgeDistance <= edgeDistance) {//矩形框靠左对齐
            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mLineChartPaint);
        } else if (rightEdgeDistance <= edgeDistance) {//矩形框靠右对齐
            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mLineChartPaint);
        } else {//居中对齐。
            rectF.set(childCenter - edgeDistance, rectTop, childCenter + edgeDistance, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mLineChartPaint);
        }

        //绘文字
        RectF leftRectF = new RectF(rectF.left + leftPadding, rectTop + txtTopPadding,
                rectF.left + leftPadding + txtLeftWidth, rectTop + txtTopPadding + rectFHeight);
        mHighLightValuePaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fontMetrics = mHighLightValuePaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (leftRectF.centerY() + (top + bottom) / 2);//基线中间点的y轴计算公式
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
    }


    private void drawHighLightLine(Canvas canvas, float[] floats, int barChartColor) {
        Paint.Style previous = mLineChartPaint.getStyle();
        float strokeWidth = mLineChartPaint.getStrokeWidth();
        int color = mLineChartPaint.getColor();
        // set
        mLineChartPaint.setStyle(Paint.Style.FILL);
        mLineChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mLineChartPaint.setColor(barChartColor);
        canvas.drawLines(floats, mLineChartPaint);
        // restore
        mLineChartPaint.setStyle(previous);
        mLineChartPaint.setStrokeWidth(strokeWidth);
        mLineChartPaint.setColor(color);
    }

    public <T extends BarEntry> void drawLineChart(Canvas canvas, RecyclerView parent, YAxis mYAxis) {
        if (mLineChartAttrs.enableXAxisLineCircle) {
            drawLineChartWithPoint(canvas, parent, mYAxis);
        } else {
            drawLineChartWithoutPoint(canvas, parent, mYAxis);
        }
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
                    drawFill(parent, mLineChartAttrs, canvas, pointF1, pointF2, rectF.bottom);
                    if (pointF1Child.getLeft() < parentLeft) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition + 2 < entryList.size()) {
                            float x = pointF1.x - pointF1Child.getWidth();
                            T barEntry0 = entryList.get(adapterPosition + 2);
                            float y = ChartComputeUtil.getYPosition(barEntry0, parent, mYAxis, mLineChartAttrs);
                            PointF pointF0 = new PointF(x, y);
                            PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF0, pointF1, parentLeft);
                            float[] points = new float[]{pointFIntercept.x, pointFIntercept.y, pointF1.x, pointF1.y};
                            drawChartLine(canvas, points);
                            drawFill(parent, mLineChartAttrs, canvas, pointFIntercept, pointF1, rectF.bottom);
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
                                drawFill(parent, mLineChartAttrs, canvas, pointFIntercept, pointF2, rectF.bottom);
                            } else if (pointF3.x < parentRightBoard) {
                                if (adapterPosition - 2 > 0) {
                                    float xInner = pointF3.x + child.getWidth();
                                    T barEntry4 = entryList.get(adapterPosition - 2);
                                    float yInner = ChartComputeUtil.getYPosition(barEntry4, parent, mYAxis, mLineChartAttrs);
                                    PointF pointF4 = new PointF(xInner, yInner);

                                    PointF pointFInterceptInner = ChartComputeUtil.getInterceptPointF(pointF3, pointF4, parentRightBoard);
                                    float[] pointsInner = new float[]{pointF3.x, pointF3.y, pointFInterceptInner.x, pointFInterceptInner.y};
                                    drawChartLine(canvas, pointsInner);
                                    drawFill(parent, mLineChartAttrs, canvas, pointF3, pointFInterceptInner, rectF.bottom);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentLeft && pointF1Child.getRight() >= parentLeft) {//左边界，处理pointF1值没有显示出来
                    PointF pointF = ChartComputeUtil.getInterceptPointF(pointF1, pointF2, parentLeft);
                    float[] points = new float[]{pointF.x, pointF.y, pointF2.x, pointF2.y};
                    drawChartLine(canvas, points);
                    drawFill(parent, mLineChartAttrs, canvas, pointF, pointF2, rectF.bottom);
                }
            }
        }
    }

    private <T extends BarEntry> void drawLineChartWithPoint(Canvas canvas, RecyclerView parent, YAxis mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
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

                if (pointF1.x >= parentLeft && pointF2.x <= parentRight) {
                    drawChartLine(canvas, pointF1, pointF2, barEntryLeft, barEntry);
                    drawFill(parent, mLineChartAttrs, canvas, pointF1, pointF2, rectF.bottom);

                    if (pointF1Child.getLeft() < parentLeft) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition + 2 < entryList.size()) {
                            float x = pointF1.x - pointF1Child.getWidth();
                            T barEntry0 = entryList.get(adapterPosition + 2);
                            float y = ChartComputeUtil.getYPosition(barEntry0, parent, mYAxis, mLineChartAttrs);
                            PointF pointF0 = new PointF(x, y);
                            PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF0, pointF1, parentLeft);
                            drawChartLineCross(canvas, pointF0, pointF1, pointFIntercept, barEntry, false);
                            drawFill(parent, mLineChartAttrs, canvas, pointFIntercept, pointF1, rectF.bottom);
                        }
                    } else if (child.getRight() < parentRight && parentRight - child.getRight() <= child.getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        if (adapterPosition - 1 > 0) {
                            T barEntryRight = entryList.get(adapterPosition - 1);
                            float x = pointF2.x + child.getWidth();
                            float y = ChartComputeUtil.getYPosition(barEntryRight, parent, mYAxis, mLineChartAttrs);
                            PointF pointF3 = new PointF(x, y);

                            if (pointF3.x >= parentRight) {
                                PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF2, pointF3, parentRight);
                                drawChartLineCross(canvas, pointF2, pointF3, pointFIntercept, barEntry, true);
                                drawFill(parent, mLineChartAttrs, canvas, pointFIntercept, pointF2, rectF.bottom);
                            } else if (pointF3.x < parentRight) {
                                if (adapterPosition - 2 > 0) {
                                    float xInner = pointF3.x + child.getWidth();
                                    T barEntry4 = entryList.get(adapterPosition - 2);
                                    float yInner = ChartComputeUtil.getYPosition(barEntry4, parent, mYAxis, mLineChartAttrs);
                                    PointF pointF4 = new PointF(xInner, yInner);

                                    PointF pointFInterceptInner = ChartComputeUtil.getInterceptPointF(pointF3, pointF4, parentRight);
                                    drawChartLineCross(canvas, pointF3, pointF4, pointFInterceptInner, barEntryRight, true);
                                    drawFill(parent, mLineChartAttrs, canvas, pointF3, pointFInterceptInner, rectF.bottom);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentLeft && pointF1Child.getRight() >= parentLeft) {//左边界，处理pointF1值没有显示出来
                    PointF pointF = ChartComputeUtil.getInterceptPointF(pointF1, pointF2, parentLeft);
                    drawChartLineCross(canvas, pointF1, pointF2, pointF, barEntry, false);
                    drawFill(parent, mLineChartAttrs, canvas, pointF, pointF2, rectF.bottom);
                }

                drawCircle(canvas, pointF1, barEntryLeft, parentRight, parentLeft);
                drawCircle(canvas, pointF2, barEntry, parentRight, parentLeft);
            }
        }
    }

    private <T extends BarEntry> void drawCircle(Canvas canvas, PointF pointF, T barEntry, float parentRight, float parentLeft) {
        if (pointF.x < parentRight && pointF.x > parentLeft){
            canvas.drawCircle(pointF.x, pointF.y, mLineChartAttrs.linePointRadius, mLineChartPaint);
            if (barEntry.isSelected()) {
                drawSelectCircle(canvas, pointF);
            }
        }
    }

    private <T extends BarEntry> void drawChartLineCross(Canvas canvas, PointF pointF0, PointF pointF1,
                                                         PointF pointIntercept, T barEntry, boolean crossLeft) {
        float[] points;
        PointF pointF1Cross;
        float radius = mLineChartAttrs.linePointRadius;
        if (barEntry.isSelected()) {//选中时的圆半径
            radius = mLineChartAttrs.linePointSelectRadius + mLineChartAttrs.linePointSelectStrokeWidth;
        }
        if (crossLeft) {
            pointF1Cross = getTheCrossPointFLeft(pointF0, pointF1, radius);
        } else {
            pointF1Cross = getTheCrossPointFRight(pointF0, pointF1, radius);
        }
        points = new float[]{pointIntercept.x, pointIntercept.y, pointF1Cross.x, pointF1Cross.y};
        drawChartLine(canvas, points);
    }


    private <T extends BarEntry> void drawChartLine(Canvas canvas, PointF pointF1, PointF pointF2, T barEntryLeft, T barEntry) {
        float[] points;
        PointF pointF1Cross;
        PointF pointF2Cross;
        if (barEntryLeft.isSelected()) {
            pointF1Cross = getTheCrossPointFLeft(pointF1, pointF2, mLineChartAttrs.linePointSelectRadius);
            pointF2Cross = getTheCrossPointFRight(pointF1, pointF2, mLineChartAttrs.linePointRadius);
        } else if (barEntry.isSelected()) {
            pointF1Cross = getTheCrossPointFLeft(pointF1, pointF2, mLineChartAttrs.linePointRadius);
            pointF2Cross = getTheCrossPointFRight(pointF1, pointF2, mLineChartAttrs.linePointSelectRadius);
        } else {
            pointF1Cross = getTheCrossPointFLeft(pointF1, pointF2, mLineChartAttrs.linePointRadius);
            pointF2Cross = getTheCrossPointFRight(pointF1, pointF2, mLineChartAttrs.linePointRadius);
        }
        points = new float[]{pointF1Cross.x, pointF1Cross.y, pointF2Cross.x, pointF2Cross.y};
        drawChartLine(canvas, points);
    }

    private PointF getTheCrossPointFLeft(PointF pointF1, PointF pointF2, float radius) {
        float xDistance = Math.abs(pointF1.x - pointF2.x);
        float yDistance = Math.abs(pointF1.y - pointF2.y);
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);

        double yLength = yDistance * (radius * 1.0f / distance);
        double xLength = xDistance * (radius * 1.0f / distance);

        PointF pointF3 = new PointF();

        if (pointF1.y > pointF2.y) {
            pointF3.y = (float) (pointF1.y - yLength);
        } else {
            pointF3.y = (float) (pointF1.y + yLength);
        }

        if (pointF1.x > pointF2.x) {
            pointF3.x = (float) (pointF1.x - xLength);
        } else {
            pointF3.x = (float) (pointF1.x + xLength);
        }
        return pointF3;
    }

    private PointF getTheCrossPointFRight(PointF pointF1, PointF pointF2, float radius) {
        float xDistance = Math.abs(pointF1.x - pointF2.x);
        float yDistance = Math.abs(pointF1.y - pointF2.y);
        double distance = Math.sqrt(xDistance * xDistance + yDistance * yDistance);
        double yLength = yDistance * (radius * 1.0f / distance);
        double xLength = xDistance * (radius * 1.0f / distance);

        PointF pointF4 = new PointF();

        if (pointF1.y > pointF2.y) {
            pointF4.y = (float) (pointF2.y + yLength);
        } else {
            pointF4.y = (float) (pointF2.y - yLength);
        }

        if (pointF1.x > pointF2.x) {
            pointF4.x = (float) (pointF2.x + xLength);
        } else {
            pointF4.x = (float) (pointF2.x - xLength);
        }
        return pointF4;
    }

    private void drawChartLine(Canvas canvas, float[] points) {
        int color = mLineChartPaint.getColor();
        mLineChartPaint.setColor(mLineChartAttrs.chartColor);
        canvas.drawLines(points, mLineChartPaint);
        mLineChartPaint.setColor(color);
    }

    private void drawSelectCircle(Canvas canvas, PointF pointF) {
        Paint.Style style = mLineChartPaint.getStyle();
        float strokeWidth = mLineChartPaint.getStrokeWidth();
        mLineChartPaint.setStyle(Paint.Style.STROKE);
        mLineChartPaint.setStrokeWidth(mLineChartAttrs.linePointSelectStrokeWidth);
        canvas.drawCircle(pointF.x, pointF.y, mLineChartAttrs.linePointSelectRadius, mLineChartPaint);
        mLineChartPaint.setStyle(style);
        mLineChartPaint.setStrokeWidth(strokeWidth);
    }


    private void drawFill(RecyclerView parent, LineChartAttrs mBarChartAttrs, Canvas canvas, PointF pointF, PointF pointF1, float bottom) {
        if (mBarChartAttrs.enableLineFill) {
            float yBottom = parent.getBottom() - parent.getPaddingBottom();
            float yTop = parent.getTop() + parent.getPaddingTop();
            LinearGradient mLinearGradient = new LinearGradient(
                    0,
                    yBottom,
                    0,
                    yTop,
                    new int[]{
                            mBarChartAttrs.lineShaderBeginColor, mBarChartAttrs.lineShaderEndColor},
                    null,
                    Shader.TileMode.CLAMP
            );
            mLineFillPaint.setShader(mLinearGradient);
            Path path = ChartComputeUtil.createColorRectPath(pointF, pointF1, bottom);
            LineChartDrawable drawable = new LineChartDrawable(mLineFillPaint, path);
            drawable.draw(canvas);
        }
    }
}
