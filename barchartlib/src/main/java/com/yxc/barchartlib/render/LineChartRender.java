package com.yxc.barchartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.yxc.barchartlib.R;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.ChartComputeUtil;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.DisplayUtil;
import com.yxc.barchartlib.view.BarChartAdapter;
import com.yxc.barchartlib.itemdecoration.LineChartDrawable;

import java.util.List;

/**
 * @author yxc
 * @since 2019/4/14
 */
final public class LineChartRender {
    private BarChartAttrs mBarChartAttrs;
    private Paint mBarChartPaint;
    private Paint mTextPaint;
    private Paint mTextMarkPaint;
    private ValueFormatter mBarChartValueFormatter;
    private ValueFormatter mChartValueMarkFormatter;


    public void setChartValueMarkFormatter(ValueFormatter mChartValueMarkFormatter) {
        this.mChartValueMarkFormatter = mChartValueMarkFormatter;
    }

    public void setBarChartValueFormatter(ValueFormatter mBarChartValueFormatter) {
        this.mBarChartValueFormatter = mBarChartValueFormatter;
    }

    public LineChartRender(BarChartAttrs barChartAttrs, ValueFormatter barChartValueFormatter, ValueFormatter chartValueMarkFormatter) {
        this.mBarChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initTextMarkPaint();
        this.mBarChartValueFormatter = barChartValueFormatter;
        this.mChartValueMarkFormatter = chartValueMarkFormatter;
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mBarChartAttrs.barChartValueTxtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    private void initTextMarkPaint() {
        mTextMarkPaint = new Paint();
        mTextMarkPaint.reset();
        mTextMarkPaint.setAntiAlias(true);
        mTextMarkPaint.setStyle(Paint.Style.FILL);
        mTextMarkPaint.setStrokeWidth(1);
        mTextMarkPaint.setColor(Color.WHITE);
        mTextMarkPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
    }

    //绘制柱状图顶部value文字
    final public void drawBarChartValue(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        if (mBarChartAttrs.enableCharValueDisplay) {
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            float parentRight = parent.getWidth() - parent.getPaddingRight();
            float parentLeft = parent.getPaddingLeft();
            float realYAxisLabelHeight = bottom - mBarChartAttrs.maxYAxisPaddingTop - parent.getPaddingTop();
            int childCount = parent.getChildCount();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                BarEntry barEntry = (BarEntry) child.getTag();
                float width = child.getWidth();
                float childCenter = child.getLeft() + width / 2;
                int height = (int) (barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight);
                float top = bottom - height;
                String valueStr = mBarChartValueFormatter.getBarLabel(barEntry);
                float txtY = top - mBarChartAttrs.barChartValuePaddingBottom;
                drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextPaint);
            }
        }
    }

    public void drawValueMark(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        if (mBarChartAttrs.enableValueMark) {
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            Log.d("Recycler", "Height:" + parent.getHeight()
                    + " PaddingBottom:" + parent.getPaddingBottom() + " topPadding:" + parent.getPaddingTop() + " y:" + parent.getY());
            int childCount = parent.getChildCount();
            float realYAxisLabelHeight = bottom - mBarChartAttrs.maxYAxisPaddingTop - parent.getPaddingTop();
            float parentRight = parent.getWidth() - parent.getPaddingRight();
            float parentLeft = parent.getPaddingLeft();
            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                BarEntry barEntry = (BarEntry) child.getTag();
                float width = child.getWidth();
                int height = (int) (barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight);
                float top = bottom - height;
                float childCenter = child.getLeft() + width / 2;
                String valueStr = mChartValueMarkFormatter.getBarLabel(barEntry);
                float txtWidth = mTextMarkPaint.measureText(valueStr);
                float distance = txtWidth / 2 + DisplayUtil.dip2px(10);
                float txtY = top - mBarChartAttrs.barChartValuePaddingBottom - DisplayUtil.dip2px(15);

                if (barEntry.isSelected && !TextUtils.isEmpty(valueStr)) {
//                    Log.d("ChartRender1", "barEntry is Selected:" + barEntry.localDate);
                    Drawable drawable = parent.getContext().getResources().getDrawable(R.drawable.marker2, null);
                    int start = (int) (childCenter - distance);
                    int end = (int) (childCenter + distance);
                    int topMark = (int) (top - DisplayUtil.dip2px(35));
                    int bottomMark = (int) top;
                    drawable.setBounds(start, topMark, end, bottomMark);
                    drawable.draw(canvas);

                    // 浮点数的 == 比较需要注意
                    if (DecimalUtil.smallOrEquals(end, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
                        continue;
                    } else if (start < parentLeft && end > parentLeft) {//左边部分滑入的时候
                        start = (int) parentLeft;
                        drawable.setBounds(start, topMark, end, bottomMark);
                        drawable.draw(canvas);
                    } else if (DecimalUtil.bigOrEquals(start, parentLeft) && DecimalUtil.smallOrEquals(end, parentRight)) {//中间的; 浮点数的 == 比较需要注意
                        drawable.setBounds(start, topMark, end, bottomMark);
                        drawable.draw(canvas);
                    } else if (DecimalUtil.smallOrEquals(start, parentRight) && end > parentRight) {//右边部分滑出的时候
                        end = (int) (start + (parentRight - start));
                        drawable.setBounds(start, topMark, end, bottomMark);
                        drawable.draw(canvas);
                    }
                    drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextMarkPaint);
                }
            }
        }
    }


    private void drawText(Canvas canvas, float parentLeft, float parentRight,
                          String valueStr, float childCenter, float txtY, Paint paint) {
//        Log.d("BarChartRender", " valueStr:" + valueStr);
        float widthText = paint.measureText(valueStr);
        float txtXLeft = getTxtX(childCenter, valueStr);
        float txtXRight = txtXLeft + widthText;

        int txtStart = 0;
        int txtEnd = valueStr.length();

        if (DecimalUtil.smallOrEquals(txtXRight, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
        } else if (txtXLeft < parentLeft && txtXRight > parentLeft) {//左边部分滑入的时候，处理柱状图、文字的显示
            int displaySize = (int) (valueStr.length() * (txtXRight - parentLeft) / widthText);//比如要显示  "123456"的末两位，需要从 length - displaySize的位置开始显示。
            txtStart = valueStr.length() - displaySize;
            txtXLeft = Math.max(txtXLeft, parentLeft);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (DecimalUtil.bigOrEquals(txtXLeft, parentLeft) && DecimalUtil.smallOrEquals(txtXRight, parentRight)) {//中间的
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (txtXLeft <= parentRight && txtXRight > parentRight) {//右边部分滑出的时候，处理柱状图，文字的显示
            txtXLeft = getTxtX(childCenter, valueStr);
            txtEnd = (int) (valueStr.length() * (parentRight - txtXLeft) / widthText);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        }
    }

    //获取文字显示的起始 X 坐标
    private float getTxtX(float center, String valueStr) {
        float txtX = center - mTextPaint.measureText(valueStr) / 2;
        return txtX;
    }

    private PointF getChildPointF(RecyclerView parent, View child, YAxis mYAxis, BarChartAttrs mBarChartAttrs) {
        BarEntry barEntry = (BarEntry) child.getTag();
        RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mBarChartAttrs, barEntry);
        float pointX = (rectF.left + rectF.right) / 2;
        float pointY = rectF.top;
        PointF pointF = new PointF(pointX, pointY);
        return pointF;
    }

    public void drawLineChart(Canvas canvas, RecyclerView parent, YAxis mYAxis) {
        final float parentRightBoard = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();
        float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;

        BarChartAdapter adapter = (BarChartAdapter) parent.getAdapter();
        List<BarEntry> entryList = adapter.getEntries();
        final int childCount = parent.getChildCount();
        int adapterPosition;
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            BarEntry barEntry = (BarEntry) child.getTag();
            adapterPosition = parent.getChildAdapterPosition(child);
            PointF pointF2 = getChildPointF(parent, child, mYAxis, mBarChartAttrs);

            if (i < childCount - 1) {
                View pointF1Child = parent.getChildAt(i + 1);
                BarEntry barEntryNear = (BarEntry) pointF1Child.getTag();
                PointF pointF1 = getChildPointF(parent, pointF1Child, mYAxis, mBarChartAttrs);

                if (pointF1.x >= parentLeft && pointF2.x <= parentRightBoard) {
                    canvas.drawLine(pointF1.x, pointF1.y, pointF2.x, pointF2.y, mBarChartPaint);
                    Path path = ChartComputeUtil.createColorRectPath(pointF1, pointF2, bottom);
                    LineChartDrawable drawable = new LineChartDrawable(mBarChartPaint, path);
                    drawable.draw(canvas);

                    if (pointF1Child.getLeft() < parentLeft) {//左边界，处理pointF1值显示出来了的情况。
                        if (adapterPosition + 2 < entryList.size()) {
                            float x = pointF1.x - pointF1Child.getWidth();
                            BarEntry barEntry0 = entryList.get(adapterPosition + 2);
                            float y = ChartComputeUtil.getYPosition(barEntry0, parent, mYAxis, mBarChartAttrs);
                            PointF pointF0 = new PointF(x, y);
                            PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF0, pointF1, parentLeft);
                            float[] points = new float[]{pointFIntercept.x, pointFIntercept.y, pointF1.x, pointF1.y};
                            canvas.drawLines(points, mBarChartPaint);
                            Path pathInner = ChartComputeUtil.createColorRectPath(pointFIntercept, pointF1, bottom);
                            LineChartDrawable drawableInner = new LineChartDrawable(mBarChartPaint, pathInner);
                            drawableInner.draw(canvas);
                        }
                    } else if (child.getRight() < parentRightBoard && parentRightBoard - child.getRight() <= child.getWidth()) {
                        //右边界处理情况，pointF3显示出来跟没有显示出来。
                        if (adapterPosition - 1 > 0) {
                            BarEntry barEntry3 = entryList.get(adapterPosition - 1);
                            float x = pointF2.x + child.getWidth();
                            float y = ChartComputeUtil.getYPosition(barEntry3, parent, mYAxis, mBarChartAttrs);
                            PointF pointF3 = new PointF(x, y);

                            if (pointF3.x > parentRightBoard) {
                                PointF pointFIntercept = ChartComputeUtil.getInterceptPointF(pointF2, pointF3, parentRightBoard);
                                float[] points = new float[]{pointF2.x, pointF2.y, pointFIntercept.x, pointFIntercept.y};
                                canvas.drawLines(points, mBarChartPaint);

                                Path pathInner = ChartComputeUtil.createColorRectPath(pointFIntercept, pointF2, bottom);
                                LineChartDrawable drawableInner = new LineChartDrawable(mBarChartPaint, pathInner);
                                drawableInner.draw(canvas);

                            } else if (pointF3.x < parentRightBoard) {
                                if (adapterPosition - 2 > 0) {
                                    float xInner = pointF3.x + child.getWidth();
                                    BarEntry barEntry4 = entryList.get(adapterPosition - 2);
                                    float yInner = ChartComputeUtil.getYPosition(barEntry4, parent, mYAxis, mBarChartAttrs);
                                    PointF pointF4 = new PointF(xInner, yInner);

                                    PointF pointFInterceptInner = ChartComputeUtil.getInterceptPointF(pointF3, pointF4, parentRightBoard);
                                    float[] pointsInner = new float[]{pointF3.x, pointF3.y, pointFInterceptInner.x, pointFInterceptInner.y};
                                    canvas.drawLines(pointsInner, mBarChartPaint);
                                    Path pathInner = ChartComputeUtil.createColorRectPath(pointF3, pointFInterceptInner, bottom);
                                    LineChartDrawable drawableInner = new LineChartDrawable(mBarChartPaint, pathInner);
                                    drawableInner.draw(canvas);
                                }
                            }
                        }
                    }
                } else if (pointF1.x < parentLeft && pointF1Child.getRight() >= parentLeft) {//左边界，处理pointF1值没有显示出来
                    PointF pointF = ChartComputeUtil.getInterceptPointF(pointF1, pointF2, parentLeft);
                    canvas.drawLine(pointF.x, pointF.y, pointF2.x, pointF2.y, mBarChartPaint);
                    Path path = ChartComputeUtil.createColorRectPath(pointF, pointF2, bottom);
                    LineChartDrawable drawable = new LineChartDrawable(mBarChartPaint, path);
                    drawable.draw(canvas);
                }

                if (pointF1.x >= parentLeft && pointF2.x <= parentRightBoard) {
                    if (barEntry.isSelected) {
                        canvas.drawCircle(pointF2.x, pointF2.y, DisplayUtil.dip2px(10), mBarChartPaint);
                        canvas.drawLine(pointF2.x, pointF2.y, pointF2.x, bottom, mBarChartPaint);
                    } else {
                        canvas.drawCircle(pointF2.x, pointF2.y, DisplayUtil.dip2px(5), mBarChartPaint);
                    }
                    if (barEntryNear.isSelected) {
                        canvas.drawCircle(pointF1.x, pointF1.y, DisplayUtil.dip2px(10), mBarChartPaint);
                        canvas.drawLine(pointF1.x, pointF1.y, pointF1.x, bottom, mBarChartPaint);
                    } else {
                        canvas.drawCircle(pointF1.x, pointF1.y, DisplayUtil.dip2px(5), mBarChartPaint);
                    }
                }
            }
        }
    }

}
