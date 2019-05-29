package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.entrys.SleepEntry;
import com.yxc.chartlib.entrys.model.SleepItemTime;
import com.yxc.chartlib.entrys.model.SleepTime;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.util.DecimalUtil;

import java.util.List;

/**
 * @author yxc
 * @since 2019-05-09
 */
public class SleepChartDayRender extends BaseChartRender<SleepEntry> {

    @Override
    protected <T extends BarEntry> int getChartColor(T entry) {
        return mBarChartAttrs.barChartColor;
    }


    public SleepChartDayRender(BarChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        super(barChartAttrs, highLightValueFormatter);
    }


    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    public <T extends BaseYAxis>void drawSleepDayChart(final Canvas canvas, @NonNull final RecyclerView parent, final T mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            SleepEntry barChart = (SleepEntry) child.getTag();
            SleepTime sleepTime = barChart.sleepTime;
            List<RectF> rectFList = ChartComputeUtil.getSleepChartRectFList(child, parent, mYAxis, mBarChartAttrs, sleepTime.sleepItemList);
            for (int position = 0; position < rectFList.size(); position++) {
                RectF rectF = rectFList.get(position);
                drawColorChart(canvas, rectF, parentLeft, parentRight, position, SleepItemTime.getSleepTypeColor(parent.getContext(), position));
            }
        }
    }

    protected void drawColorChart(Canvas canvas, RectF rectF, float parentLeft, float parentRight, int position, int color) {
        float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return;
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            Path path;
            if (position == 0) {
                path = CanvasUtil.createRectRoundPathRight(rectF, radius);
            } else {
                path = CanvasUtil.createRectPath(rectF);
            }

            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            mBarChartPaint.setColor(color);
            Path path;
            if (position == 0) {
                path = CanvasUtil.createRectRoundPath(rectF, radius);
            } else {
                path = CanvasUtil.createRectPath(rectF);
            }
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            Path path;
            if (position == 0) {
                path = CanvasUtil.createRectRoundPathLeft(rectF, radius);
            } else {
                path = CanvasUtil.createRectPath(rectF);
            }

            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        }
    }
}
