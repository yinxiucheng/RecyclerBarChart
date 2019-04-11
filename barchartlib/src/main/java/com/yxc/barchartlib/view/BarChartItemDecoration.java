package com.yxc.barchartlib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.component.ChartRectF;
import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.component.YAxis;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.DisplayUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public BarChartItemDecoration(Context context, int orientation, YAxis yAxis, XAxis xAxis, BarChartAttrs barChartAttrs) {
        this.mContext = context;
        this.mOrientation = orientation;
        this.mYAxis = yAxis;
        this.mXAxis = xAxis;
        this.mBarChartAttrs = barChartAttrs;
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
            drawLeftYAxisLabel(canvas, parent, mYAxis);//画左边y坐标的刻度，会设定RecyclerView的 leftPadding
            drawRightYAxisLabel(canvas, parent, mYAxis);//画右边y坐标的刻度，会设定RecyclerView的 rightPadding
            drawVerticalLine(canvas, parent, mXAxis);//画竖的网格线
            drawHorizontalLine(canvas, parent, mYAxis);//画横的网格线
            drawXAxis(canvas, parent, mXAxis);//画x轴坐标的刻度
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

    //绘制 Y轴刻度线 横的网格线
    private void drawHorizontalLine(Canvas canvas, RecyclerView parent, YAxis yAxis) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        mLinePaint.setColor(yAxis.getGridColor());
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        float distance = bottom - mBarChartAttrs.contentPaddingBottom - mBarChartAttrs.maxYAxisPaddingTop;
        int lineNums = yAxis.getLabelCount();
        float lineDistance = distance / lineNums;
        float gridLine = top + mBarChartAttrs.maxYAxisPaddingTop;

        for (int i = 0; i <= lineNums; i++) {
            if (i > 0) {
                gridLine = gridLine + lineDistance;
            }
            Path path = new Path();
            path.moveTo(left, gridLine);
            path.lineTo(right, gridLine);

            boolean enable = false;
            if (i == lineNums && mBarChartAttrs.enableYAxisZero) {
                enable = true;
            } else {
                enable = mBarChartAttrs.enableXAxisGridLine;//允许画 Y轴刻度
            }
            if (enable) {
                canvas.drawPath(path, mLinePaint);
            }
        }
    }

    //绘制左边的刻度
    private void drawLeftYAxisLabel(Canvas canvas, RecyclerView parent, YAxis yAxis) {
        if (mBarChartAttrs.enableLeftYAxisLabel) {
            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();

            mTextPaint.setTextSize(yAxis.getTextSize());
            String longestStr = yAxis.getLongestLabel();
            float yAxisWidth = mTextPaint.measureText(longestStr) + mBarChartAttrs.recyclerPaddingLeft;
            //设置 recyclerView的 BarChart 内容区域
            parent.setPadding((int) yAxisWidth, parent.getPaddingTop(), parent.getPaddingRight(), parent.getPaddingBottom());

            float topLocation = top + mBarChartAttrs.maxYAxisPaddingTop;
            float containerHeight = bottom - mBarChartAttrs.contentPaddingBottom - topLocation;
            float itemHeight = containerHeight / yAxis.getLabelCount();
            HashMap<Float, Float> yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.getLabelCount());

            for (Map.Entry<Float, Float> entry : yAxisScaleMap.entrySet()) {
                float yAxisScaleLocation = entry.getKey();
                float yAxisScaleValue = entry.getValue();
                String labelStr = yAxis.getValueFormatter().getFormattedValue(yAxisScaleValue);

                float txtY = yAxisScaleLocation + yAxis.labelVerticalPadding;
                float txtX = yAxisWidth - mTextPaint.measureText(labelStr) - yAxis.labelHorizontalPadding;
                canvas.drawText(labelStr, txtX, txtY, mTextPaint);
            }
        }
    }

    //绘制右边的刻度
    private void drawRightYAxisLabel(Canvas canvas, RecyclerView parent, YAxis yAxis) {
        if (mBarChartAttrs.enableRightYAxisLabel) {
            int right = parent.getWidth();
            int top = parent.getPaddingTop();
            int bottom = parent.getHeight() - parent.getPaddingBottom();

            mTextPaint.setTextSize(yAxis.getTextSize());
            String longestStr = yAxis.getLongestLabel();
            float yAxisWidth = mTextPaint.measureText(longestStr) + mBarChartAttrs.recyclerPaddingRight;
            //设置 recyclerView的 BarChart 内容区域
            parent.setPadding(parent.getPaddingLeft(), parent.getPaddingTop(), (int) yAxisWidth, parent.getPaddingBottom());

            float topLocation = top + mBarChartAttrs.maxYAxisPaddingTop;
            float containerHeight = bottom - mBarChartAttrs.contentPaddingBottom - topLocation;
            float itemHeight = containerHeight / yAxis.getLabelCount();
            HashMap<Float, Float> yAxisScaleMap = yAxis.getYAxisScaleMap(topLocation, itemHeight, yAxis.getLabelCount());

            float txtX = right - parent.getPaddingRight() + yAxis.labelHorizontalPadding;

            for (Map.Entry<Float, Float> entry : yAxisScaleMap.entrySet()) {
                float yAxisScaleLocation = entry.getKey();
                float yAxisScaleValue = entry.getValue();
                String labelStr = yAxis.getValueFormatter().getFormattedValue(yAxisScaleValue);
                float txtY = yAxisScaleLocation + yAxis.labelVerticalPadding;
                canvas.drawText(labelStr, txtX, txtY, mTextPaint);
            }
        }
    }

    //绘制网格 纵轴线
    private void drawVerticalLine(Canvas canvas, RecyclerView parent, XAxis xAxis) {
        int parentTop = parent.getPaddingTop();
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        int parentLeft = parent.getPaddingLeft();
        final int childCount = parent.getChildCount();
        mTextPaint.setTextSize(xAxis.getTextSize());
        int parentRight = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(child);
            int type = parent.getAdapter().getItemViewType(adapterPosition);
            final int x = child.getLeft();
            if (x > parentRight || x < parentLeft) {//超出的时候就不要画了
                continue;
            }
            if (type == BarEntry.TYPE_XAXIS_FIRST || type == BarEntry.TYPE_XAXIS_SPECIAL) {
                boolean isNextSecondType = isNearEntrySecondType(xAxis, child.getWidth(), adapterPosition);
                mLinePaint.setColor(xAxis.firstDividerColor);
                Path path = new Path();
                if (isNextSecondType) {
                    path.moveTo(x, parentBottom - mBarChartAttrs.contentPaddingBottom);
                } else {
                    path.moveTo(x, parentBottom);
                }
                path.lineTo(x, parentTop);
                canvas.drawPath(path, mLinePaint);
            } else if (type == BarEntry.TYPE_XAXIS_SECOND) {
                //拿到child 的布局信息
                PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mDashPaint.setPathEffect(pathEffect);
                mDashPaint.setColor(xAxis.secondDividerColor);
                Path path = new Path();
                path.moveTo(x, parentBottom - DisplayUtil.dip2px(1));
                path.lineTo(x, parentTop);
                canvas.drawPath(path, mDashPaint);
            } else if (type == BarEntry.TYPE_XAXIS_THIRD) {
                //拿到child 的布局信息
                PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mDashPaint.setPathEffect(pathEffect);
                mDashPaint.setColor(xAxis.thirdDividerColor);
                Path path = new Path();
                path.moveTo(x, parentBottom - mBarChartAttrs.contentPaddingBottom);
                path.lineTo(x, parentTop);
                canvas.drawPath(path, mDashPaint);
            }
        }
    }

    //画月线的时候，当邻近的靠左的存在需要写 X轴坐标的BarEntry，返回true, 柱体宽度大于文本宽度时除外。
    private boolean isNearEntrySecondType(XAxis xAxis, int barWidth, int adapterPosition) {
        int position1 = adapterPosition - 1;
        int position2 = adapterPosition - 2;
        BarEntry barEntryNext;
        if (position1 > 0 && mEntries.get(position1).type == BarEntry.TYPE_XAXIS_SECOND) {
            barEntryNext = mEntries.get(position1);
            mTextPaint.setTextSize(xAxis.getTextSize());
            String xAxisLabel = xAxis.getValueFormatter().getBarLabel(barEntryNext);
            if (null != barEntryNext && barWidth > mTextPaint.measureText(xAxisLabel)) {
                //对于宽的柱状体，不缩短临近TYPE_SECOND的月线
                return false;
            }
            return true;
        } else if (position2 > 0 && mEntries.get(position2).type == BarEntry.TYPE_XAXIS_SECOND) {
            barEntryNext = mEntries.get(position2);
            mTextPaint.setTextSize(xAxis.getTextSize());
            String xAxisLabel = xAxis.getValueFormatter().getBarLabel(barEntryNext);
            if (null != barEntryNext && barWidth > mTextPaint.measureText(xAxisLabel)){
                //对于宽的柱状体，不缩短临近TYPE_SECOND的月线
                return false;
            }
            return true;
        }
        return false;
    }

    //绘制X坐标
    private void drawXAxis(Canvas canvas, RecyclerView parent, XAxis xAxis) {

        int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        int parentLeft = parent.getPaddingLeft();
        final int childCount = parent.getChildCount();
        mTextPaint.setTextSize(xAxis.getTextSize());
        int parentRight = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(child);
            int type = parent.getAdapter().getItemViewType(adapterPosition);
            final int x = child.getLeft();

            if (type == BarEntry.TYPE_XAXIS_SECOND || type == BarEntry.TYPE_XAXIS_SPECIAL || type == BarEntry.TYPE_XAXIS_FIRST) {
                BarEntry barEntry = mEntries.get(adapterPosition);

                String dateStr = xAxis.getValueFormatter().getBarLabel(barEntry);

                if (!TextUtils.isEmpty(dateStr)) {
                    int childWidth = child.getWidth();
                    float txtWidth = mTextPaint.measureText(dateStr);
                    float txtXLeft = 0;
                    float txtY = parentBottom - DisplayUtil.dip2px(1);
                    if (childWidth > txtWidth) {//柱状图的宽度比较大的时候，文字居中
                        float distance = childWidth - txtWidth;
                        txtXLeft = x + distance / 2;
                    } else {
                        txtXLeft = x + xAxis.labelTxtPadding;
                    }
                    float txtXRight = txtXLeft + txtWidth;
                    int length = dateStr.length();

                    if (DecimalUtil.bigOrEquals(txtXLeft, parentLeft) && DecimalUtil.smallOrEquals(txtXRight, parentRight)) {//中间位置
                        canvas.drawText(dateStr, txtXLeft, txtY, mTextPaint);
                    } else if (txtXLeft < parentLeft && txtXRight > parentLeft) {//处理左边界
                        int displayLength = (int) ((txtXRight - parentLeft) / txtWidth * length);
                        int index = length - displayLength;
                        canvas.drawText(dateStr, index, length, parentLeft, txtY, mTextPaint);
                    } else if (txtXRight > parentRight && txtXLeft < parentRight) {//处理右边界
                        int displayLength = (int) ((parentRight - txtXLeft + 1) / txtWidth * length);
                        int endIndex = displayLength;
                        if (endIndex < length) {
                            endIndex += 1;
                        }
                        canvas.drawText(dateStr, 0, endIndex, txtXLeft, txtY, mTextPaint);
                    }
                }
            }
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
