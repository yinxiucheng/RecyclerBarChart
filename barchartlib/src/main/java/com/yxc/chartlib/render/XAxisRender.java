package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;


import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.barchart.BaseBarChartAdapter;
import com.yxc.chartlib.component.XAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.util.DecimalUtil;
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
        if (!mBarChartAttrs.enableXAxisGridLine){
            return;
        }
        BaseBarChartAdapter mAdapter = (BaseBarChartAdapter) parent.getAdapter();
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
            if (adapterPosition == RecyclerView.NO_POSITION){
                continue;
            }
            int type = parent.getAdapter().getItemViewType(adapterPosition);
            final int x = child.getRight();
            if (x > parentRight || x < parentLeft) {//超出的时候就不要画了
                continue;
            }
            if (type == BarEntry.TYPE_XAXIS_FIRST || type == BarEntry.TYPE_XAXIS_SPECIAL) {
                if (mBarChartAttrs.enableXAxisFirstGridLine){
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
                }
            } else if (type == BarEntry.TYPE_XAXIS_SECOND) {
                if (mBarChartAttrs.enableXAxisSecondGridLine){
                    //拿到child 的布局信息
                    PathEffect pathEffect = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                    mDashPaint.setPathEffect(pathEffect);
                    mDashPaint.setColor(xAxis.secondDividerColor);
                    Path path = new Path();
                    path.moveTo(x, parentBottom - DisplayUtil.dip2px(1));
                    path.lineTo(x, parentTop);
                    canvas.drawPath(path, mDashPaint);
                }
            } else if (type == BarEntry.TYPE_XAXIS_THIRD) {
                if (mBarChartAttrs.enableXAxisThirdGridLine){
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
    }

    //画月线的时候，当邻近的靠左的存在需要写 X轴坐标的BarEntry，返回true, 柱体宽度大于文本宽度时除外。
    private boolean isNearEntrySecondType(List<? extends BarEntry> entries, XAxis xAxis, int itemWidth, int adapterPosition) {
        if (adapterPosition == 0) {
            return false;
        }
        BarEntry barEntryNext;
        for (int nearPosition = adapterPosition - 1; nearPosition > 0; nearPosition--) {
            barEntryNext = entries.get(nearPosition);
            mTextPaint.setTextSize(xAxis.getTextSize());
            float distance = itemWidth * (adapterPosition - nearPosition);
            String xAxisLabel = xAxis.getValueFormatter().getBarLabel(barEntryNext);
            if (!TextUtils.isEmpty(xAxisLabel)) {
                float txtWidth = mTextPaint.measureText(xAxisLabel) + xAxis.labelTxtPadding;
                if (txtWidth > distance) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    //绘制X坐标
    final public void drawXAxis(Canvas canvas, RecyclerView parent, XAxis xAxis) {
        if (!mBarChartAttrs.enableXAxisLabel){
            return;
        }
        int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        int parentLeft = parent.getPaddingLeft();
        final int childCount = parent.getChildCount();
        mTextPaint.setTextSize(xAxis.getTextSize());
        int parentRight = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int xLeft = child.getLeft();
            final int xRight = child.getRight();
            BarEntry barEntry = (BarEntry) child.getTag();

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


    //绘制X坐标
    final public void drawXAxisDisplay(Canvas canvas, RecyclerView parent, BarChartAttrs attrs) {
        if (!mBarChartAttrs.enableXAxisDisplayLabel){
            return;
        }
        float parentBottom = parent.getHeight() - parent.getPaddingBottom() + DisplayUtil.dip2px(5);
        mTextPaint.setTextSize(attrs.xAxisTxtSize);
        mTextPaint.setColor(attrs.xAxisTxtColor);
        String[] strArray = new String[]{"00:00", "06:00", "12:00", "18:00", "24:00"};
        float textWidth = mTextPaint.measureText(strArray[0]);
        float parentWidth = parent.getWidth() - parent.getPaddingStart() - parent.getPaddingEnd();
        float spaceWidth = (parentWidth - textWidth * 5) / 4;

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        float height = bottom - top;
        for (int i = 0; i < 5; i++) {
            float rectFLeft = parent.getPaddingStart() + i * (spaceWidth + textWidth);
            float rectFRight = rectFLeft + textWidth;
            RectF rect = new RectF(rectFLeft, parentBottom,
                    rectFRight, parentBottom + height);
            int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);
            canvas.drawText(strArray[i], rect.left, baseLineY, mTextPaint);

        }
    }

}
