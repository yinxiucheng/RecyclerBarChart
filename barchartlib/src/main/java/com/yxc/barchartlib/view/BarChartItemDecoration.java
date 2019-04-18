package com.yxc.barchartlib.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.DefaultBarChartValueFormatter;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.render.BarBoardRender;
import com.yxc.barchartlib.render.BarChartRender;
import com.yxc.barchartlib.render.XAxisRender;
import com.yxc.barchartlib.render.YAxisRender;
import com.yxc.barchartlib.util.BarChartAttrs;

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
    private ValueFormatter mChartValueMarkFormatter;

    private Paint mBarChartPaint;
    private HashMap<Integer, CustomAnimatedDecorator> mAnimatorMap;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public BarChartItemDecoration(YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        this.mOrientation = barChartAttrs.layoutManagerOrientation;
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;
        this.yAxisRenderer = new YAxisRender(mBarChartAttrs);
        this.xAxisRenderer = new XAxisRender(mBarChartAttrs);
        this.mBarBoardRender = new BarBoardRender(mBarChartAttrs);
        this.mBarChartValueFormatter = new DefaultBarChartValueFormatter(0);
        this.mChartValueMarkFormatter = new DefaultBarChartValueFormatter(0);
        this.mBarChartRender = new BarChartRender(mBarChartAttrs, mBarChartValueFormatter, mChartValueMarkFormatter);
        initBarChartPaint();
    }

    public void setAnimatorMap(HashMap<Integer, CustomAnimatedDecorator> map) {
        this.mAnimatorMap = map;
    }


    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
    }

    //支持自定义 柱状图顶部 value的格式。
    public void setBarChartValueFormatter(ValueFormatter barChartValueFormatter) {
        this.mBarChartValueFormatter = barChartValueFormatter;
        this.mBarChartRender.setBarChartValueFormatter(barChartValueFormatter);
    }

    public void setChartValueMarkFormatter(ValueFormatter chartValueFormatter) {
        this.mChartValueMarkFormatter = chartValueFormatter;
        this.mBarChartRender.setChartValueMarkFormatter(chartValueFormatter);
    }


    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        if (mOrientation == HORIZONTAL_LIST) {
            Log.d("ItemDecoration", " itemdecoration invoke!");
            //横向 list 画竖线
            yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
            yAxisRenderer.drawRightYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度

            mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

//            mBarChartRender.drawBarChart(canvas, parent, mYAxis);//draw BarChart
            drawChart(canvas, parent);
            mBarChartRender.drawValueMark(canvas, parent, mYAxis);
            mBarChartRender.drawBarChartValue(canvas, parent, mYAxis);//draw BarChart value

        } else if (mOrientation == VERTICAL_LIST) {//暂时不支持
            //竖向list 画横线
            //drawHorizontalLine(c, parent, mXAxis);
        }
    }


    final public void drawChart(final Canvas canvas, @NonNull final RecyclerView parent) {
        boolean mustInvalidate = false;
        if (parent != null && parent.getChildCount() > 0) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(child);
                float width = child.getWidth();
                float barSpaceWidth = width * mBarChartAttrs.barSpace;
                final float left = child.getLeft() + barSpaceWidth / 2;

                CustomAnimatedDecorator customAnimatedDecorator = mAnimatorMap.get(position);
                if (position != RecyclerView.NO_POSITION && null != customAnimatedDecorator) {
                    mustInvalidate = true;
                    drawView(canvas, customAnimatedDecorator,  left, child.getTop() + mBarChartAttrs.maxYAxisPaddingTop);
                }
            }
            if (mustInvalidate) parent.invalidate();
        }
    }

    private void drawView(Canvas canvas, AnimatedDecoratorDrawable drawable, float dx, float dy) {
        canvas.save();
        canvas.translate(dx, dy);
        drawable.draw(canvas, mBarChartPaint);
        canvas.restore();
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
