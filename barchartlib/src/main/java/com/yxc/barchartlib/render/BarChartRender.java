package com.yxc.barchartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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

/**
 * @author yxc
 * @date 2019/4/14
 */
final public class BarChartRender {
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

    public BarChartRender(BarChartAttrs barChartAttrs, ValueFormatter barChartValueFormatter, ValueFormatter chartValueMarkFormatter) {
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

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawBarChart(final Canvas canvas, @NonNull final RecyclerView parent, final YAxis mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            BarEntry barChart = (BarEntry) child.getTag();
            RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mBarChartAttrs, barChart);
            if (drawChart(canvas, rectF, parentLeft, parentRight)){
                continue;
            }
        }
    }

    private boolean drawChart(Canvas canvas, RectF rectF, float parentLeft, float parentRight) {
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return true;
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawRect(rectF, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
            canvas.drawRect(rectF, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawRect(rectF, mBarChartPaint);
        }
        return false;
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

                if (drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextPaint)) {
                    continue;
                }
            }
        }
    }


    public void drawValueMark(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        if (mBarChartAttrs.enableValueMark) {
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
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
                    Log.d("ChartRender1", "barEntry is Selected:" + barEntry.localDate);
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

                    if (drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextMarkPaint)) {
                        continue;
                    }
                }
            }
        }
    }


    private boolean drawText(Canvas canvas, float parentLeft, float parentRight,
                             String valueStr, float childCenter, float txtY, Paint paint) {
        Log.d("BarChartRender", " valueStr:" + valueStr);
        float widthText = paint.measureText(valueStr);
        float txtXLeft = getTxtX(childCenter, valueStr);
        float txtXRight = txtXLeft + widthText;

        int txtStart = 0;
        int txtEnd = valueStr.length();

        if (DecimalUtil.smallOrEquals(txtXRight, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return true;
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
        return false;
    }

    //获取文字显示的起始 X 坐标
    private float getTxtX(float center, String valueStr) {
        float txtX = center - mTextPaint.measureText(valueStr) / 2;
        return txtX;
    }

}
