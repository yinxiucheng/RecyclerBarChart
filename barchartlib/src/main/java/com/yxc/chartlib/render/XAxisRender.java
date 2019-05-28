package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.util.DecimalUtil;
import com.yxc.chartlib.barchart.BarChartAdapter;
import com.yxc.commonlib.util.DisplayUtil;

import java.util.List;

final public class XAxisRender {

    private Paint mTextPaint;
    private Paint mDashPaint;
    private Paint mLinePaint;
    private BarChartAttrs mBarChartAttrs;

    public XAxisRender(BarChartAttrs barChartAttrs) {
        this.mBarChartAttrs = barChartAttrs;
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
        mTextPaint.setTextSize(mBarChartAttrs.xAxisTxtSize);
    }


    //绘制网格 纵轴线
    final public void drawVerticalLine(Canvas canvas, RecyclerView parent, XAxis xAxis) {
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
            final int x = child.getRight();
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
    private boolean isNearEntrySecondType(List<BarEntry> entries, XAxis xAxis, int itemWidth, int adapterPosition) {
        if (adapterPosition == 0){
            return false;
        }
        BarEntry barEntryNext;
        for (int nearPosition = adapterPosition - 1; nearPosition > 0; nearPosition--) {
            barEntryNext = entries.get(nearPosition);
            mTextPaint.setTextSize(xAxis.getTextSize());
            float distance = itemWidth * (adapterPosition - nearPosition);
            String xAxisLabel = xAxis.getValueFormatter().getBarLabel(barEntryNext);
            if (!TextUtils.isEmpty(xAxisLabel)){
                float txtWidth = mTextPaint.measureText(xAxisLabel) + xAxis.labelTxtPadding;
                if (txtWidth > distance){
                    return true;
                }
                break;
            }
        }
        return false;
    }

    //绘制X坐标
    final public void drawXAxis(Canvas canvas, RecyclerView parent, XAxis xAxis) {
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

            final int xLeft = child.getLeft();
            final int xRight = child.getRight();
            BarEntry barEntry = entries.get(adapterPosition);

            String dateStr = xAxis.getValueFormatter().getBarLabel(barEntry);
            if (!TextUtils.isEmpty(dateStr)) {
                int childWidth = child.getWidth();
                float txtWidth = mTextPaint.measureText(dateStr);
                float txtXLeft = 0;
                float txtY = parentBottom - DisplayUtil.dip2px(1);
                if (childWidth > txtWidth) {//柱状图的宽度比较大的时候，文字居中
                    float distance = childWidth - txtWidth;
                    txtXLeft = xLeft + distance / 2;
                } else {
                    txtXLeft = xRight - xAxis.labelTxtPadding - txtWidth;
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
