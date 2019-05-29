package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.util.DecimalUtil;

/**
 * @author yxc
 * @since  2019/4/14
 */
public class BarChartRender extends BaseChartRender{

    public BarChartRender(BarChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, highLightValueFormatter);
    }

    public BarChartRender(BarChartAttrs barChartAttrs, ValueFormatter barChartValueFormatter,
                          ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, barChartValueFormatter, highLightValueFormatter);
    }


    public BarChartRender(BarChartAttrs barChartAttrs) {
        super(barChartAttrs);
    }

    @Override
    protected int getChartColor(BarEntry entry) {
        return mBarChartAttrs.barChartColor;
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
            drawChart(canvas, rectF, parentLeft, parentRight);
        }
    }

    final public void drawBarChartDisplay(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            BarEntry barEntry = (BarEntry) child.getTag();
            RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mBarChartAttrs, barEntry);
            float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;

            if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
                //中间的; 浮点数的 == 比较需要注意
                mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
                Path path = CanvasUtil.createRectRoundPath(rectF, radius);
                canvas.drawPath(path, mBarChartPaint);
            }
        }
    }

    private void drawChart(Canvas canvas, RectF rectF, float parentLeft, float parentRight) {
        float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return;
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            Path path = CanvasUtil.createRectRoundPathRight(rectF, radius);
            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
            Path path = CanvasUtil.createRectRoundPath(rectF, radius);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            Path path = CanvasUtil.createRectRoundPathLeft(rectF, radius);
            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        }
    }

}
