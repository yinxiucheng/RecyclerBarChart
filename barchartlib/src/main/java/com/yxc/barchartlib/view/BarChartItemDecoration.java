package com.yxc.barchartlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yxc.barchartlib.component.ChartRectF;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.BarChartValueFormatter;
import com.yxc.barchartlib.render.BarBoardRender;
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

    private Paint mTextPaint;
    private Paint mBarChartPaint;

    private YAxis mYAxis;
    private XAxis mXAxis;
    private BarChartAdapter mAdapter;
    private BarChartAttrs mBarChartAttrs;
    private YAxisRender yAxisRenderer;
    private XAxisRender xAxisRenderer;
    private BarBoardRender mBarBoardRender;

    private BarChartValueFormatter barChartValueFormatter;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public BarChartItemDecoration(Context context, YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        this.mOrientation = barChartAttrs.layoutManagerOrientation;
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;

        this.yAxisRenderer = new YAxisRender(mBarChartAttrs, mYAxis);
        this.xAxisRenderer = new XAxisRender(mBarChartAttrs, mXAxis);
        this.mBarBoardRender = new BarBoardRender(mBarChartAttrs);

        this.barChartValueFormatter = new BarChartValueFormatter(0);

        initTextPaint();
        initBarChartPaint();
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        mAdapter = (BarChartAdapter) parent.getAdapter();
        if (mOrientation == HORIZONTAL_LIST) {
            //横向 list 画竖线
            yAxisRenderer.drawLeftYAxisLabel(canvas, parent);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
            yAxisRenderer.drawRightYAxisLabel(canvas, parent);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            yAxisRenderer.drawHorizontalLine(canvas, parent);//画横的网格线

            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度

            drawBarChart(canvas, parent);//draw BarChart
            drawBarChartValue(canvas, parent);//draw BarChart value

            mBarBoardRender.drawBarBorder(canvas, parent);//绘制边框

        } else if (mOrientation == VERTICAL_LIST) {
            //竖向list 画横线
//            drawHorizontalLine(c, parent, mXAxis);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setTextSize(mXAxis.getTextSize());
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
    }

    //绘制柱状图
    private void drawBarChart(Canvas canvas, @NonNull RecyclerView parent) {
        float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
        float parentRight = parent.getWidth() - parent.getPaddingRight();
        float parentLeft = parent.getPaddingLeft();
        float parentContentWidth = parentRight - parentLeft;

        float realYAxisLabelHeight = bottom - mBarChartAttrs.maxYAxisPaddingTop;
        final int childCount = parent.getChildCount();

        View child;
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            BarEntry barEntry = (BarEntry) child.getTag();
            ChartRectF rectF = new ChartRectF();
            float width = parentContentWidth / mXAxis.displayNumbers;
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
    private void drawBarChartValue(Canvas canvas, @NonNull RecyclerView parent) {
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
            String valueStr = barEntry.getY() > 0 ? barChartValueFormatter.getFormattedValue(barEntry.getY()) : "";
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

    //文字宽度小于item的宽度时，获取文字显示的起始 X 坐标
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


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, 0);
    }

    public void setXAxis(XAxis mXAxis) {
        this.mXAxis = mXAxis;
    }


    public void setYAxis(YAxis mYAxis) {
        this.mYAxis = mYAxis;
    }

}
