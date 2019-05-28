package com.yxc.chartlib.barchart.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.formatter.DefaultBarChartValueFormatter;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.render.BarBoardRender;
import com.yxc.chartlib.render.BarChartRender;
import com.yxc.chartlib.render.XAxisRender;
import com.yxc.chartlib.render.YAxisRender;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.view.CustomAnimatedDecorator;

import java.util.HashMap;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class BarChartItemDecoration extends RecyclerView.ItemDecoration {

    private int mOrientation;
    private YAxis mYAxis;
    private XAxis mXAxis;
    private BarChartAttrs mBarChartAttrs;
    private YAxisRender yAxisRenderer;
    private XAxisRender xAxisRenderer;
    private BarBoardRender mBarBoardRender;
    private BarChartRender mBarChartRender;

    private ValueFormatter mBarChartValueFormatter;
    private ValueFormatter mHighLightValueFormatter;

    private Paint mBarChartPaint;

    @SuppressWarnings("unused")
    private HashMap<Integer, CustomAnimatedDecorator> mAnimatorMap;

    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public BarChartItemDecoration(YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        this.mOrientation = barChartAttrs.layoutManagerOrientation;
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;
        this.yAxisRenderer = new YAxisRender(mBarChartAttrs);
        this.xAxisRenderer = new XAxisRender(mBarChartAttrs);
        this.mBarBoardRender = new BarBoardRender(mBarChartAttrs);
        this.mBarChartValueFormatter = new DefaultBarChartValueFormatter(0);
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.mBarChartRender = new BarChartRender(mBarChartAttrs, mBarChartValueFormatter, mHighLightValueFormatter);

        initBarChartPaint();
    }

    public void setAnimatorMap(HashMap<Integer, CustomAnimatedDecorator> map) {
        this.mAnimatorMap = map;
    }


    //支持自定义 柱状图顶部 value的格式。
    public void setBarChartValueFormatter(ValueFormatter barChartValueFormatter) {
        this.mBarChartValueFormatter = barChartValueFormatter;
        this.mBarChartRender.setBarChartValueFormatter(barChartValueFormatter);
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter){
        this.mHighLightValueFormatter = highLightValueFormatter;
        this.mBarChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        if (mOrientation == HORIZONTAL_LIST) {
            Log.d("ItemDecoration", " itemdecoration invoke!");
            //横向 list 画竖线
            yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
            yAxisRenderer.drawRightYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线
            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度

            mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

            mBarChartRender.drawBarChart(canvas, parent, mYAxis);//draw BarChart
            mBarChartRender.drawHighLight(canvas, parent, mYAxis);
            mBarChartRender.drawBarChartValue(canvas, parent, mYAxis);//draw BarChart value

        } else if (mOrientation == VERTICAL_LIST) {//暂时不支持
            //竖向list 画横线
            //drawHorizontalLine(c, parent, mXAxis);
        }
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
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

}
