package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.render.SleepChartDayRender;
import com.yxc.chartlib.render.XAxisRender;
import com.yxc.chartlib.render.YAxisRender;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class SleepChartDayItemDecoration<T extends BaseYAxis> extends RecyclerView.ItemDecoration {

    private int mOrientation;
    private T mYAxis;
    private XAxis mXAxis;
    private BarChartAttrs mBarChartAttrs;
    private YAxisRender yAxisRenderer;
    private XAxisRender xAxisRenderer;
    private SleepChartDayRender mBarChartRender;

    private ValueFormatter mHighLightValueFormatter;


    public SleepChartDayItemDecoration(T yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        this.mOrientation = barChartAttrs.layoutManagerOrientation;
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;
        this.yAxisRenderer = new YAxisRender(mBarChartAttrs);
        this.xAxisRenderer = new XAxisRender(mBarChartAttrs);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mBarChartRender = new SleepChartDayRender(mBarChartAttrs, mHighLightValueFormatter);
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
        mBarChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        //横向 list 画竖线
        yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
        yAxisRenderer.drawRightYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
        yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

        xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
        xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
        xAxisRenderer.drawXAxisDisplay(canvas, parent, mBarChartAttrs);//绘制数据首页中的供显示用的非数据关联的坐标。

        mBarChartRender.drawSleepDayChart(canvas, parent, mYAxis);//draw BarChart
        mBarChartRender.drawHighLight(canvas, parent, mYAxis);
        mBarChartRender.drawBarChartValue(canvas, parent, mYAxis);//draw BarChart value
    }


    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

    }

    public void setXAxis(XAxis mXAxis) {
        this.mXAxis = mXAxis;
    }


    public void setYAxis(T mYAxis) {
        this.mYAxis = mYAxis;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
    }
}
