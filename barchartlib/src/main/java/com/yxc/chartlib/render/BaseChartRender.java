package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.component.BaseYAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.util.DecimalUtil;
import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.commonlib.util.TextUtil;


/**
 * @author yxc
 * @since 2019/4/14
 */
public abstract class BaseChartRender<T extends BarEntry> {

    protected BarChartAttrs mBarChartAttrs;
    protected Paint mBarChartPaint;
    protected Paint mTextPaint;
    protected Paint mHighLightValuePaint;

    protected ValueFormatter mBarChartValueFormatter;
    protected ValueFormatter mHighLightValueFormatter;

    public BaseChartRender(BarChartAttrs barChartAttrs, ValueFormatter highLightValueFormatter) {
        this.mBarChartAttrs = barChartAttrs;
        this.mHighLightValueFormatter = highLightValueFormatter;
        initBarChartPaint();
        initTextPaint();
        initHighLightPaint();
    }

    public BaseChartRender(BarChartAttrs barChartAttrs, ValueFormatter barChartValueFormatter,
                           ValueFormatter highLightValueFormatter) {
        this.mBarChartAttrs = barChartAttrs;
        this.mBarChartValueFormatter = barChartValueFormatter;
        this.mHighLightValueFormatter = highLightValueFormatter;
        initBarChartPaint();
        initTextPaint();
        initHighLightPaint();
    }


    public BaseChartRender(BarChartAttrs barChartAttrs) {
        this.mBarChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
    }

    protected void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mBarChartAttrs.barChartValueTxtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    protected void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
    }

    protected void initHighLightPaint() {
        mHighLightValuePaint = new Paint();
        mHighLightValuePaint.reset();
        mHighLightValuePaint.setAntiAlias(true);
        mHighLightValuePaint.setStyle(Paint.Style.FILL);
        mHighLightValuePaint.setStrokeWidth(1);
        mHighLightValuePaint.setColor(Color.WHITE);
        mHighLightValuePaint.setTextSize(DisplayUtil.dip2px(12));
    }


    public void setBarChartValueFormatter(ValueFormatter mBarChartValueFormatter) {
        this.mBarChartValueFormatter = mBarChartValueFormatter;
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
    }


    //绘制柱状图顶部value文字
    public <E extends BaseYAxis> void drawBarChartValue(Canvas canvas, @NonNull RecyclerView parent, E yAxis) {
        if (mBarChartAttrs.enableCharValueDisplay) {
            float parentRight = parent.getWidth() - parent.getPaddingRight();
            float parentLeft = parent.getPaddingLeft();
            int childCount = parent.getChildCount();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                BarEntry barEntry = (BarEntry) child.getTag();
                float width = child.getWidth();
                float childCenter = child.getLeft() + width / 2;
                float top = ChartComputeUtil.getYPosition(barEntry, parent, yAxis, mBarChartAttrs);
                String valueStr = mBarChartValueFormatter.getBarLabel(barEntry);
                float txtY = top - mBarChartAttrs.barChartValuePaddingBottom;
                drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextPaint);
            }
        }
    }


    //绘制选中时 highLight 标线及浮框。
    public  <E extends BaseYAxis> void drawHighLight(Canvas canvas, @NonNull RecyclerView parent, E yAxis) {
        if (mBarChartAttrs.enableValueMark) {

            int childCount = parent.getChildCount();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                T entry = (T) child.getTag();
                RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, yAxis, mBarChartAttrs, entry);
                float width = child.getWidth();
                float childCenter = child.getLeft() + width / 2;
                String valueStr = mHighLightValueFormatter.getBarLabel(entry);


                if (entry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    int chartColor = getChartColor(entry);
                    float rectHeight = drawHighLightValue(canvas, valueStr, childCenter, parent, chartColor);
                    float[] points = new float[]{childCenter, rectF.top, childCenter, rectHeight};
                    drawHighLightLine(canvas, points, chartColor);

                }
            }
        }
    }


    //绘制柱状图选中浮框
    protected float drawHighLightValue(Canvas canvas, String valueStr, float childCenter,
                                      RecyclerView parent, int barChartColor) {
        float parentTop = parent.getPaddingTop();
        float contentRight = parent.getWidth() - parent.getPaddingRight();
        float contentLeft = parent.getPaddingLeft();

        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
        float rightEdgeDistance = Math.abs(contentRight - childCenter);

        float leftPadding = DisplayUtil.dip2px(8);
        float rightPadding = DisplayUtil.dip2px(8);
        float centerPadding = DisplayUtil.dip2px(16);

        float rectBottom = parentTop;
        float txtTopPadding = DisplayUtil.dip2px(8);

        String leftStr = strings[0];
        String rightStr = strings[1];

        float txtLeftWidth = mHighLightValuePaint.measureText(leftStr);
        float txtRightWidth = mHighLightValuePaint.measureText(rightStr);

        float rectFHeight = TextUtil.getTxtHeight1(mHighLightValuePaint) + txtTopPadding * 2;
        float txtWidth = txtLeftWidth + txtRightWidth + leftPadding + rightPadding + centerPadding;

        float edgeDistance = txtWidth / 2.0f;
        float rectTop = parentTop - rectFHeight;

        //绘制RectF
        RectF rectF = new RectF();
        mBarChartPaint.setColor(barChartColor);
        if (leftEdgeDistance <= edgeDistance) {//矩形框靠左对齐
            rectF.set(contentLeft, rectTop, contentLeft + txtWidth, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
        } else if (rightEdgeDistance <= edgeDistance) {//矩形框靠右对齐
            rectF.set(contentRight - txtWidth, rectTop, contentRight, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
        } else {//居中对齐。
            rectF.set(childCenter - edgeDistance, rectTop, childCenter + edgeDistance, rectBottom);
            float radius = DisplayUtil.dip2px(8);
            canvas.drawRoundRect(rectF, radius, radius, mBarChartPaint);
        }

        //绘文字
        RectF leftRectF = new RectF(rectF.left + leftPadding, rectTop + txtTopPadding,
                rectF.left + leftPadding + txtLeftWidth, rectTop + txtTopPadding + rectFHeight);
        mHighLightValuePaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fontMetrics = mHighLightValuePaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (leftRectF.centerY() + (top + bottom) / 2);//基线中间点的y轴计算公式
        canvas.drawText(leftStr, rectF.left + leftPadding, baseLineY, mHighLightValuePaint);

        float dividerLineStartX = rectF.left + leftPadding + txtLeftWidth + centerPadding / 2.0f;
        float dividerLineStartY = rectTop + DisplayUtil.dip2px(10);
        float dividerLineEndX = dividerLineStartX;
        float dividerLineEndY = rectBottom - DisplayUtil.dip2px(10);
        float[] lines = new float[]{dividerLineStartX, dividerLineStartY, dividerLineEndX, dividerLineEndY};
        canvas.drawLines(lines, mHighLightValuePaint);

        float rightRectFStart = rectF.left + leftPadding + txtLeftWidth + centerPadding;
        RectF rightRectF = new RectF(rightRectFStart, rectTop + txtTopPadding, rectF.right - rightPadding, rectBottom - txtTopPadding);
        canvas.drawText(rightStr, rightRectF.left, baseLineY, mHighLightValuePaint);

        return rectFHeight;
    }


    private void drawHighLightLine(Canvas canvas, float[] floats, int barChartColor) {
        Paint.Style previous = mBarChartPaint.getStyle();
        float strokeWidth = mBarChartPaint.getStrokeWidth();
        int color = mBarChartPaint.getColor();
        // set
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setStrokeWidth(DisplayUtil.dip2px(1));
        mBarChartPaint.setColor(barChartColor);
        canvas.drawLines(floats, mBarChartPaint);
        // restore
        mBarChartPaint.setStyle(previous);
        mBarChartPaint.setStrokeWidth(strokeWidth);
        mBarChartPaint.setColor(color);
    }


    private boolean drawText(Canvas canvas, float parentLeft, float parentRight,
                             String valueStr, float childCenter, float txtY, Paint paint) {
//        Log.d("BarChartRender", " valueStr:" + valueStr);
        float widthText = paint.measureText(valueStr);
        float txtXLeft = getTxtX(childCenter, valueStr);
        float txtXRight = txtXLeft + widthText;

        int txtStart = 0;
        int txtEnd = valueStr.length();

        if (DecimalUtil.smallOrEquals(txtXRight, parentLeft)) {//continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return true;
        } else if (txtXLeft < parentLeft && txtXRight > parentLeft) {//左边部分滑入的时候，处理柱状图、文字的显示
            int displaySize = (int) (valueStr.length() * (txtXRight - parentLeft) / widthText);//比如要显示  "123456"的末两位，需要从 length - displaySize的位置开始显示。
            txtStart = valueStr.length() - displaySize;
            txtXLeft = Math.max(txtXLeft, parentLeft);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (DecimalUtil.bigOrEquals(txtXLeft, parentLeft) && DecimalUtil.smallOrEquals(txtXRight, parentRight)) {//中间的
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        } else if (txtXLeft <= parentRight && txtXRight > parentRight) {//右边部分滑出的时候，处理柱状图，文字的显示
            txtXLeft = getTxtX(childCenter, valueStr);
            txtEnd = (int) (valueStr.length() * (parentRight - txtXLeft) / widthText);
            canvas.drawText(valueStr, txtStart, txtEnd, txtXLeft, txtY, paint);
        }
        return false;
    }

    //获取文字显示的起始 X 坐标
    private float getTxtX(float center, String valueStr) {
        float txtX = center - mTextPaint.measureText(valueStr) / 2;
        return txtX;
    }

    protected abstract <T extends BarEntry> int getChartColor(T entry);

}
