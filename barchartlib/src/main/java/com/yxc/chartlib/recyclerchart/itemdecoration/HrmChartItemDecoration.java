package com.yxc.chartlib.recyclerchart.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.recyclerchart.attrs.LineChartAttrs;
import com.yxc.chartlib.recyclerchart.component.XAxis;
import com.yxc.chartlib.recyclerchart.component.YAxis;
import com.yxc.chartlib.recyclerchart.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.recyclerchart.formatter.ValueFormatter;
import com.yxc.chartlib.recyclerchart.render.HrmBarBoardRender;
import com.yxc.chartlib.recyclerchart.render.HrmLineChartRender;
import com.yxc.chartlib.recyclerchart.render.HrmXAxisRender;
import com.yxc.chartlib.recyclerchart.render.HrmYAxisRender;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class HrmChartItemDecoration extends RecyclerView.ItemDecoration {

    private int mOrientation;
    private YAxis mYAxis;
    private XAxis mXAxis;
    private LineChartAttrs mBarChartAttrs;
    private HrmYAxisRender yAxisRenderer;
    private HrmXAxisRender xAxisRenderer;
    private HrmBarBoardRender mBarBoardRender;
    private HrmLineChartRender mLineChartRender;
    private ValueFormatter mHighLightValueFormatter;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public HrmChartItemDecoration(YAxis yAxis, XAxis xAxis, LineChartAttrs barChartAttrs) {
        this.mOrientation = barChartAttrs.layoutManagerOrientation;
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;
        this.yAxisRenderer = new HrmYAxisRender(mBarChartAttrs);
        this.xAxisRenderer = new HrmXAxisRender(mBarChartAttrs);
        this.mBarBoardRender = new HrmBarBoardRender(mBarChartAttrs);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mLineChartRender = new HrmLineChartRender(mBarChartAttrs, mHighLightValueFormatter);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        if (mOrientation == HORIZONTAL_LIST) {
            Log.d("ItemDecoration", " itemdecoration invoke!");

            mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框
            //横向 list 画竖线

            yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
            yAxisRenderer.drawRightYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
            mLineChartRender.drawLineChart(canvas, parent, mYAxis);//draw LineChart
//            mLineChartRender.drawHighLight(canvas, parent, mYAxis);//绘制选中高亮

        } else if (mOrientation == VERTICAL_LIST) {//暂时不支持
            //竖向list 画横线
            //drawHorizontalLine(c, parent, mXAxis);
        }
    }


    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

    }

    public void setXAxis(XAxis mXAxis) {
        this.mXAxis = mXAxis;
    }


    public void setYAxis(YAxis mYAxis) {
        this.mYAxis = mYAxis;
    }


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter){
        this.mHighLightValueFormatter = highLightValueFormatter;
        this.mLineChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }

}
