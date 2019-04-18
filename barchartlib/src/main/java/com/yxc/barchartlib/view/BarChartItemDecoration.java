package com.yxc.barchartlib.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yxc.barchartlib.component.ChartRectF;
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
import com.yxc.barchartlib.util.DecimalUtil;

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
            //横向 list 画竖线
            yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
            yAxisRenderer.drawRightYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度

            mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

            drawBarChart(canvas, parent);//draw BarChart
            mBarChartRender.drawValueMark(canvas, parent, mYAxis);
            mBarChartRender.drawBarChartValue(canvas, parent, mYAxis);//draw BarChart value

        } else if (mOrientation == VERTICAL_LIST) {//暂时不支持
            //竖向list 画横线
            //drawHorizontalLine(c, parent, mXAxis);
        }
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawBarChart(final Canvas canvas, @NonNull final RecyclerView parent) {
        final float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        float realYAxisLabelHeight = bottom - mBarChartAttrs.maxYAxisPaddingTop - parent.getPaddingTop();
        final int childCount = parent.getChildCount();
        View child;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            BarEntry barEntry = (BarEntry) child.getTag();
            final ChartRectF rectF = new ChartRectF();
            float width = child.getWidth();
            float barSpaceWidth = width * mBarChartAttrs.barSpace;
            float barChartWidth = width - barSpaceWidth;//柱子的宽度
            final float start = child.getLeft() + barSpaceWidth / 2;

            final float end = start + barChartWidth;

            float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;

            final float top = Math.max(bottom - height, parent.getPaddingTop());

            if (drawChart(canvas, rectF, start, end, top, bottom, parentLeft, parentRight)) {
                continue;
            }
        }
    }

    final public void drawChart(final Canvas canvas, @NonNull final RecyclerView parent){
        boolean mustInvalidate = false;
        if (parent != null && parent.getChildCount() > 0) {
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    int position = parent.getChildAdapterPosition(child);
                    if (position != RecyclerView.NO_POSITION) {
                        mustInvalidate = true;
//                        drawView(canvas, drawable, child);
                    }
                }
                if (mustInvalidate) parent.invalidate();
        }
    }

    private void drawView(Canvas canvas, AnimatedDecoratorDrawable drawable, View child) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        canvas.save();
        canvas.translate(child.getLeft(), child.getTop());
        drawable.draw(canvas, mBarChartPaint);
        canvas.restore();
    }

    private boolean drawChart(Canvas canvas, ChartRectF rectF, float start, float end, float top,
                              float bottom, float parentLeft, float parentRight) {
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(end, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return true;
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
        return false;
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
