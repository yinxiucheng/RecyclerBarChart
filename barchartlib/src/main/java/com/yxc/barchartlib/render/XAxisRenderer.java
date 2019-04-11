package com.yxc.barchartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.yxc.barchartlib.component.XAxis;
import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.util.BarChartAttrs;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.DisplayUtil;
import com.yxc.barchartlib.view.BarChartAdapter;

import java.util.List;

public class XAxisRenderer{

    protected XAxis mXAxis;

    private Paint mTextPaint;

    private Paint mDashPaint;

    private Paint mLinePaint;

    private BarChartAttrs mBarChartAttrs;


    public XAxisRenderer(BarChartAttrs barChartAttrs, XAxis xAxis) {
        this.mBarChartAttrs = barChartAttrs;
        this.mXAxis = xAxis;
        initTextPaint();
        initDathPaint();
        initPaint();
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


    //绘制网格 纵轴线
    public void drawVerticalLine(Canvas canvas, RecyclerView parent, XAxis xAxis) {

        BarChartAdapter mAdapter = (BarChartAdapter) parent.getAdapter();
        List<BarEntry> entries = mAdapter.getEntries();

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
                boolean isNextSecondType = isNearEntrySecondType(entries, xAxis, child.getWidth(), adapterPosition);
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
    private boolean isNearEntrySecondType(List<BarEntry> entries, XAxis xAxis, int barWidth, int adapterPosition) {
        int position1 = adapterPosition - 1;
        int position2 = adapterPosition - 2;
        BarEntry barEntryNext;
        if (position1 > 0 && entries.get(position1).type == BarEntry.TYPE_XAXIS_SECOND) {
            barEntryNext = entries.get(position1);
            mTextPaint.setTextSize(xAxis.getTextSize());
            String xAxisLabel = xAxis.getValueFormatter().getBarLabel(barEntryNext);
            if (null != barEntryNext && barWidth > mTextPaint.measureText(xAxisLabel)) {
                //对于宽的柱状体，不缩短临近TYPE_SECOND的月线
                return false;
            }
            return true;
        } else if (position2 > 0 && entries.get(position2).type == BarEntry.TYPE_XAXIS_SECOND) {
            barEntryNext = entries.get(position2);
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
    public void drawXAxis(Canvas canvas, RecyclerView parent, XAxis xAxis) {
        BarChartAdapter mAdapter = (BarChartAdapter) parent.getAdapter();
        List<BarEntry> entries = mAdapter.getEntries();

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
                BarEntry barEntry = entries.get(adapterPosition);

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

}
