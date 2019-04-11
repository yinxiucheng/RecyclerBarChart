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
import com.yxc.barchartlib.render.XAxisRenderer;
import com.yxc.barchartlib.render.YAxisRenderer;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.DecimalUtil;

import java.util.List;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是BarChartAdapter专用的，里面直接用到了BarChartAdapter
 */
public class BarChartItemDecoration extends RecyclerView.ItemDecoration {

    private static final String TAG = "BarChartItemDecoration";

    private Context mContext;
    private int mOrientation;

    private Paint mDashPaint;
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mBarChartPaint;
    private Paint mBarBorderPaint;

    private BarChartAdapter mAdapter;
    private List<BarEntry> mEntries;
    private YAxis mYAxis;
    private XAxis mXAxis;
    private BarChartAttrs mBarChartAttrs;
    private YAxisRenderer yAxisRenderer;
    private XAxisRenderer xAxisRenderer;

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public BarChartItemDecoration(Context context, int orientation, YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        this.mContext = context;
        this.mOrientation = orientation;
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;

        this.yAxisRenderer = new YAxisRenderer(mBarChartAttrs, mYAxis);
        this.xAxisRenderer = new XAxisRenderer(mBarChartAttrs, mXAxis);

        setOrientation(orientation);
        initPaint();
        initDathPaint();
        initTextPaint();
        initBarChartPaint();
        initBarBorderPaint();
    }

    public void setYAxis(YAxis mYAxis) {
        this.mYAxis = mYAxis;
    }

    //设置屏幕方向
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
            throw new IllegalArgumentException("invalid orientation");
        }
        this.mOrientation = orientation;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        mAdapter = (BarChartAdapter) parent.getAdapter();
        mEntries = mAdapter.getEntries();
        if (mOrientation == HORIZONTAL_LIST) {
            //横向 list 画竖线
            yAxisRenderer.drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
            yAxisRenderer.drawRightYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            yAxisRenderer.drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线

            xAxisRenderer.drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            xAxisRenderer.drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度


            drawBarChart(canvas, parent);//draw barchart
            drawBarChartValue(canvas, parent);//draw barchart value
            drawBarBorder(canvas, parent);//绘制边框
        } else if (mOrientation == VERTICAL_LIST) {
            //竖向list 画横线
//            drawHorizontalLine(c, parent, mXAxis);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//        super.onDraw(c, parent, state);
//        mAdapter = (BarChartAdapter) parent.getAdapter();
//        mEntries = mAdapter.getEntries();
//
//        if (mOrientation == HORIZONTAL_LIST) {
//            //横向 list 画竖线
//            drawVerticalLine(c, parent, mXAxis);
//            drawHorizontalLine(c, parent, mYAxis);
//            drawRightYAxisLabel(c, parent, mYAxis);
//            drawBarChart(c, parent, state);
//            drawBarBorder(c, parent);
//        } else if (mOrientation == VERTICAL_LIST) {
//            //竖向list 画横线
//            drawHorizontalLine(c, parent, mXAxis);
//        }
    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.reset();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.GRAY);
    }

    private void initDathPaint() {
        mDashPaint = new Paint();
        mDashPaint.reset();
        mDashPaint.setAntiAlias(true);
        mDashPaint.setStyle(Paint.Style.STROKE);
        mDashPaint.setStrokeWidth(1);
        mDashPaint.setColor(Color.GRAY);
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

    private void initBarBorderPaint() {
        mBarBorderPaint = new Paint();
        mBarBorderPaint.reset();
        mBarBorderPaint.setAntiAlias(true);
        mBarBorderPaint.setStyle(Paint.Style.STROKE);
        mBarBorderPaint.setStrokeWidth(mBarChartAttrs.barBorderWidth);
        mBarBorderPaint.setColor(mBarChartAttrs.barBorderColor);
    }

    private void drawBarBorder(@NonNull Canvas canvas, @NonNull RecyclerView parent) {
        if (mBarChartAttrs.enableBarBorder) {
            RectF rectF = new RectF();
            float start = parent.getPaddingLeft();
            float top = parent.getPaddingTop();
            float end = parent.getRight() - parent.getPaddingRight();
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;//底部有0的刻度是不是不用画，就画折线了。

            rectF.set(start, top, end, bottom);
            mBarBorderPaint.setStrokeWidth(mBarChartAttrs.barBorderWidth);
            canvas.drawRect(rectF, mBarBorderPaint);
        }
    }

    //绘制柱状图
    private void drawBarChart(Canvas canvas, @NonNull RecyclerView parent) {
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
            int width = child.getWidth();
            float barSpaceWidth = width * mBarChartAttrs.barSpace;
            float barChartWidth = width - barSpaceWidth;//柱子的宽度
            float start = child.getLeft() + barSpaceWidth / 2;
            float end = start + barChartWidth;
            float height = barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight;
            float top = bottom - height;

            // 浮点数的 == 比较需要注意
            if (DecimalUtil.smallOrEquals(end, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
                continue;
            } else if (start < parentLeft && end > parentLeft) {//左边部分滑入的时候，处理柱状图、文字的显示
                start = parentLeft;
                rectF.set(start, top, end, bottom);
                mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
                canvas.drawRect(rectF, mBarChartPaint);
            } else if (DecimalUtil.bigOrEquals(start, parentLeft) && DecimalUtil.smallOrEquals(end, parentRight)) {//中间的; 浮点数的 == 比较需要注意
                if (child.getLeft() < parentLeft || child.getRight() > parentRight) {
                    //为了配合 findLastCompletelyVisibleItemPosition，看着压线都到不了 CompletelyVisible
                    mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
                } else {
                    mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
                }
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
            int valueInt = (int) barEntry.getY();
            int width = child.getWidth();
            int height = (int) (barEntry.getY() / mYAxis.getAxisMaximum() * realYAxisLabelHeight);
            float top = bottom - height;

            mTextPaint.setTextSize(mBarChartAttrs.barChartValueTxtSize);
            String valueStr = Integer.toString(valueInt);

            float widthText = mTextPaint.measureText(valueStr);
            float txtXLeft = 0;
            if (widthText < width) {
                txtXLeft = getTxtX(child, width, valueStr);
            } else {
                txtXLeft = child.getLeft() + mBarChartAttrs.barChartValuePaddingLeft;
            }
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
    private float getTxtX(View child, int width, String valueStr) {
        float txtDistance = width - mTextPaint.measureText(valueStr);
        float txtX = child.getLeft();
        if (txtDistance > 0) {
            txtX = txtX + txtDistance / 2;
        }
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

}
