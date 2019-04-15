package com.yxc.barchartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yxc.barchartlib.component.ChartRectF;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.DecimalUtil;

/**
 * @author yxc
 * @date 2019/4/14
 *
 */
final public class BarChartRender {
    private BarChartAttrs mBarChartAttrs;
    private Paint mBarChartPaint;
    private Paint mTextPaint;
    private ValueFormatter mBarChartValueFormatter;

    public BarChartRender(BarChartAttrs barChartAttrs, ValueFormatter barChartValueFormatter){
        this.mBarChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        this.mBarChartValueFormatter = barChartValueFormatter;
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(mBarChartAttrs.yAxisLabelSize);
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawBarChart(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float parentRight = parent.getWidth() - parent.getPaddingRight();
        float parentLeft = parent.getPaddingLeft();

        float realYAxisLabelHeight = bottom - mBarChartAttrs.maxYAxisPaddingTop;
        final int childCount = parent.getChildCount();

        View child;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            BarEntry barEntry = (BarEntry) child.getTag();
            ChartRectF rectF = new ChartRectF();
            float width = child.getWidth();
            float barSpaceWidth = width * mBarChartAttrs.barSpace;
            float barChartWidth = width - barSpaceWidth;//柱子的宽度
            float start = child.getLeft() + barSpaceWidth / 2;
            float end = start + barChartWidth;
            float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
            float top = bottom - height;

            // 浮点数的 == 比较需要注意
            if (DecimalUtil.smallOrEquals(end, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
                continue;
            } else if (start < parentLeft && end > parentLeft) {//左边部分滑入的时候，处理柱状图的显示
                start = parentLeft;
                rectF.set(start, top, end, bottom);
                mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
                canvas.drawRect(rectF, mBarChartPaint);
            } else if (DecimalUtil.bigOrEquals(start, parentLeft) && DecimalUtil.smallOrEquals(end, parentRight)) {//中间的; 浮点数的 == 比较需要注意
                mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
                rectF.set(start, top, end, bottom);
                canvas.drawRect(rectF, mBarChartPaint);
            } else if (DecimalUtil.smallOrEquals(start, parentRight) && end > parentRight) {//右边部分滑出的时候，处理柱状图，文字的显示
                float distance = (parentRight - start);
                end = start + distance;
                rectF.set(start, top, end, bottom);
                mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
                canvas.drawRect(rectF, mBarChartPaint);
            }
        }
    }


    //绘制柱状图顶部value文字
    final public void drawBarChartValue(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float parentRight = parent.getWidth() - parent.getPaddingRight();
        float parentLeft = parent.getPaddingLeft();

        float realYAxisLabelHeight = bottom - mBarChartAttrs.maxYAxisPaddingTop;
        int childCount = parent.getChildCount();

        View child;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            BarEntry barEntry = (BarEntry) child.getTag();
            float width = child.getWidth();
            int height = (int) (barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight);
            float top = bottom - height;

            mTextPaint.setTextSize(mBarChartAttrs.barChartValueTxtSize);
            String valueStr = mBarChartValueFormatter.getBarLabel(barEntry);
            Log.d("BarChartRender", " valueStr:" + valueStr);
            float widthText = mTextPaint.measureText(valueStr);
            float txtXLeft = getTxtX(child, width, valueStr);
            float txtXRight = txtXLeft + widthText;
            float txtY = top - mBarChartAttrs.barChartValuePaddingBottom;

            int txtStart = 0;
            int txtEnd = valueStr.length();

            if (txtXRight <= parentLeft) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
                continue;
            } else if (txtXLeft < parentLeft && txtXRight > parentLeft) {//左边部分滑入的时候，处理柱状图、文字的显示
                int displaySize = (int) (valueStr.length() * (txtXRight - parentLeft) / widthText);//比如要显示  "123456"的末两位，需要从 length - displaySize的位置开始显示。
                txtStart = valueStr.length() - displaySize;
                txtXLeft = Math.max(txtXLeft, parentLeft);
                displayCharValue(mBarChartAttrs.enableCharValueDisplay, canvas, valueStr, txtStart, txtEnd, txtXLeft, txtY);
            } else if (DecimalUtil.bigOrEquals(txtXLeft, parentLeft) && DecimalUtil.smallOrEquals(txtXRight, parentRight)) {//中间的
                displayCharValue(mBarChartAttrs.enableCharValueDisplay, canvas, valueStr, txtStart, txtEnd, txtXLeft, txtY);
            } else if (txtXLeft <= parentRight && txtXRight > parentRight) {//右边部分滑出的时候，处理柱状图，文字的显示
                txtXLeft = getTxtX(child, width, valueStr);
                txtEnd = (int) (valueStr.length() * (parentRight - txtXLeft) / widthText);
                displayCharValue(mBarChartAttrs.enableCharValueDisplay, canvas, valueStr, txtStart, txtEnd, txtXLeft, txtY);
            }
        }
    }

    //获取文字显示的起始 X 坐标
    private float getTxtX(View child, float width, String valueStr) {
        float txtX = child.getLeft() + width / 2 - mTextPaint.measureText(valueStr) / 2;
        return txtX;
    }

    //控制char上的value是否显示
    private void displayCharValue(boolean enableCharValueDisplay, Canvas canvas, String valueStr, int start, int end, float x, float y) {
        if (enableCharValueDisplay) {
            canvas.drawText(valueStr, start, end, x, y, mTextPaint);
        }
    }
}
