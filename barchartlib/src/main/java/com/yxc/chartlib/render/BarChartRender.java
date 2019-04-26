package com.yxc.chartlib.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.yxc.chartlib.component.YAxis;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.util.CanvasUtil;
import com.yxc.chartlib.util.ChartComputeUtil;
import com.yxc.chartlib.util.DecimalUtil;
import com.yxc.chartlib.view.AnimatedDecoratorDrawable;
import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.commonlib.util.TextUtil;

/**
 * @author yxc
 * @since  2019/4/14
 */
final public class BarChartRender {
    private BarChartAttrs mBarChartAttrs;
    private Paint mBarChartPaint;
    private Paint mTextPaint;

    private Paint mHighLightValueLeftPaint;
    private Paint mHighLightValueRightPaint;
    private Paint mHighLightUnitRightPaint;

    private ValueFormatter mBarChartValueFormatter;
    private ValueFormatter mHighLightValueFormatter;


    public void setBarChartValueFormatter(ValueFormatter mBarChartValueFormatter) {
        this.mBarChartValueFormatter = mBarChartValueFormatter;
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter) {
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    public BarChartRender(BarChartAttrs barChartAttrs, ValueFormatter barChartValueFormatter, ValueFormatter highLightValueFormatter) {
        this.mBarChartAttrs = barChartAttrs;
        initBarChartPaint();
        initTextPaint();
        initHighLightLeftPaint();
        initHighLightRightPaint();
        initHighLightUnitRightPaint();
        this.mBarChartValueFormatter = barChartValueFormatter;
        this.mHighLightValueFormatter = highLightValueFormatter;
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setColor(mBarChartAttrs.barChartValueTxtColor);
        mTextPaint.setTextSize(DisplayUtil.dip2px(12));
    }

    private void initBarChartPaint() {
        mBarChartPaint = new Paint();
        mBarChartPaint.reset();
        mBarChartPaint.setAntiAlias(true);
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
    }


    private void initHighLightLeftPaint() {
        mHighLightValueLeftPaint = new Paint();
        mHighLightValueLeftPaint.reset();
        mHighLightValueLeftPaint.setAntiAlias(true);
        mHighLightValueLeftPaint.setStyle(Paint.Style.FILL);
        mHighLightValueLeftPaint.setStrokeWidth(3);
        mHighLightValueLeftPaint.setColor(mBarChartAttrs.highLightLeftTxtColor);
        mHighLightValueLeftPaint.setTextSize(mBarChartAttrs.highLightLeftTxtSize);
    }

    private void initHighLightRightPaint() {
        mHighLightValueRightPaint = new Paint();
        mHighLightValueRightPaint.reset();
        mHighLightValueRightPaint.setAntiAlias(true);
        mHighLightValueRightPaint.setStyle(Paint.Style.FILL);
        mHighLightValueRightPaint.setStrokeWidth(1);
        mHighLightValueRightPaint.setColor(mBarChartAttrs.highLightRightTxtColor);
        mHighLightValueRightPaint.setTextSize(mBarChartAttrs.highLightRightTxtSize);
    }

    private void initHighLightUnitRightPaint() {
        mHighLightUnitRightPaint = new Paint();
        mHighLightUnitRightPaint.reset();
        mHighLightUnitRightPaint.setAntiAlias(true);
        mHighLightUnitRightPaint.setStyle(Paint.Style.FILL);
        mHighLightUnitRightPaint.setStrokeWidth(1);
        mHighLightUnitRightPaint.setColor(mBarChartAttrs.highLightRightTxtColor);
        mHighLightUnitRightPaint.setTextSize(DisplayUtil.dip2px(10));
    }

    //绘制柱状图, mYAxis这个坐标会实时变动，所以通过 BarChartItemDecoration 传过来的精确值。
    final public void drawBarChart(final Canvas canvas, @NonNull final RecyclerView parent, final YAxis mYAxis) {
        final float parentRight = parent.getWidth() - parent.getPaddingRight();
        final float parentLeft = parent.getPaddingLeft();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            BarEntry barChart = (BarEntry) child.getTag();
            RectF rectF = ChartComputeUtil.getBarChartRectF(child, parent, mYAxis, mBarChartAttrs, barChart);
            if (drawChart(canvas, rectF, parentLeft, parentRight)) {
                continue;
            }
        }
    }

    private boolean drawChart(Canvas canvas, RectF rectF, float parentLeft, float parentRight) {
        float radius = (rectF.right - rectF.left) * mBarChartAttrs.barChartRoundRectRadiusRatio;
        // 浮点数的 == 比较需要注意
        if (DecimalUtil.smallOrEquals(rectF.right, parentLeft)) {
            //continue 会闪，原因是end == parentLeft 没有过滤掉，显示出来柱状图了。
            return true;
        } else if (rectF.left < parentLeft && rectF.right > parentLeft) {
            //左边部分滑入的时候，处理柱状图的显示
            rectF.left = parentLeft;
            Path path = CanvasUtil.createRectRoundPathRight(rectF, radius);
            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.bigOrEquals(rectF.left, parentLeft) && DecimalUtil.smallOrEquals(rectF.right, parentRight)) {
            //中间的; 浮点数的 == 比较需要注意
            mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
            Path path = CanvasUtil.createRectRoundPath(rectF, radius);
            canvas.drawPath(path, mBarChartPaint);
        } else if (DecimalUtil.smallOrEquals(rectF.left, parentRight) && rectF.right > parentRight) {
            //右边部分滑出的时候，处理柱状图，文字的显示
            float distance = (parentRight - rectF.left);
            rectF.right = rectF.left + distance;
            Path path = CanvasUtil.createRectRoundPathLeft(rectF, radius);
            mBarChartPaint.setColor(mBarChartAttrs.barChartEdgeColor);
            canvas.drawPath(path, mBarChartPaint);
        }
        return false;
    }


    //绘制柱状图顶部value文字
    final public void drawBarChartValue(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
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
                float top = ChartComputeUtil.getYPosition(barEntry, parent, mYAxis, mBarChartAttrs);
                String valueStr = mBarChartValueFormatter.getBarLabel(barEntry);
                float txtY = top - mBarChartAttrs.barChartValuePaddingBottom;
                drawText(canvas, parentLeft, parentRight, valueStr, childCenter, txtY, mTextPaint);
            }
        }
    }

    //绘制选中时 highLight 标线及浮框。
    public void drawHighLight(Canvas canvas, @NonNull RecyclerView parent, YAxis mYAxis) {
        if (mBarChartAttrs.enableValueMark) {
            float contentBottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            float contentTop = parent.getPaddingTop();

            int childCount = parent.getChildCount();
            float contentRight = parent.getWidth() - parent.getPaddingRight();
            float contentLeft = parent.getPaddingLeft();

            View child;
            for (int i = 0; i < childCount; i++) {
                child = parent.getChildAt(i);
                BarEntry barEntry = (BarEntry) child.getTag();
                float width = child.getWidth();
                float top = ChartComputeUtil.getYPosition(barEntry, parent, mYAxis, mBarChartAttrs);
                float childCenter = child.getLeft() + width / 2;
                String valueStr = mHighLightValueFormatter.getBarLabel(barEntry);
                float lineTop = top - DisplayUtil.dip2px(1);
                float[] points = new float[]{childCenter, contentBottom + DisplayUtil.dip2px(1), childCenter, contentBottom + DisplayUtil.dip2px(8),
                        childCenter, lineTop, childCenter, contentTop - DisplayUtil.dip2px(2)
                };

                if (barEntry.isSelected() && !TextUtils.isEmpty(valueStr)) {
                    drawHighLightLine(canvas, points);
                    drawHighLightValue(canvas, valueStr, childCenter, contentLeft, contentRight, contentTop);
                }
            }
        }
    }

    private void drawHighLightValue(Canvas canvas, String valueStr, float childCenter, float contentLeft, float contentRight, float contentTop) {
        float txtWidth = 0;
        String[] strings = valueStr.split(DefaultHighLightMarkValueFormatter.CONNECT_STR);
        float leftEdgeDistance = Math.abs(childCenter - contentLeft);
        float rightEdgeDistance = Math.abs(contentRight - childCenter);

        float leftPadding = DisplayUtil.dip2px(8);
        float rightPadding = DisplayUtil.dip2px(8);
        float centerPadding = DisplayUtil.dip2px(16);
        float rightValueUnitPadding = DisplayUtil.dip2px(3);

        float rectFHeight = 0;

        float rectBottom = contentTop - DisplayUtil.dip2px(2);
        float txtTopPadding = DisplayUtil.dip2px(4);
        float txtLeftWidth = 0f;
        float txtRightWidth = 0f;

        if (strings.length == 4) {
            String leftStr1 = strings[0];
            String leftStr2 = strings[1];
            String rightStr1 = strings[2];
            String rightStr2 = strings[3];
            txtLeftWidth = Math.max(mHighLightValueLeftPaint.measureText(leftStr1), mHighLightValueLeftPaint.measureText(leftStr2));
            float txtLeftHeight = (TextUtil.getTxtHeight1(mHighLightValueLeftPaint) + txtTopPadding) * 2;
            float txtRightHeight = TextUtil.getTxtHeight1(mHighLightValueRightPaint) + TextUtil.getTxtHeight1(mHighLightUnitRightPaint) + txtTopPadding;

            float txtRightWidthBottom = mHighLightValueRightPaint.measureText(rightStr2) + rightValueUnitPadding + mHighLightValueLeftPaint.measureText("步");
            float txtRightWidthTop = mHighLightValueLeftPaint.measureText(rightStr1);
            txtRightWidth = Math.max(txtRightWidthTop, txtRightWidthBottom);
            txtWidth = txtLeftWidth + txtRightWidth + leftPadding + rightPadding + centerPadding;
            rectFHeight = Math.max(txtLeftHeight, txtRightHeight);
        } else {// size is 3
            String leftStr1 = strings[0];
            String leftStr2 = strings[1];
            String rightStr = strings[2];
            txtLeftWidth = Math.max(mHighLightValueLeftPaint.measureText(leftStr1), mHighLightValueLeftPaint.measureText(leftStr2));
            txtRightWidth = mHighLightValueRightPaint.measureText(rightStr) + mHighLightValueLeftPaint.measureText("步");
            txtWidth = txtLeftWidth + txtRightWidth + leftPadding + rightPadding + centerPadding;

            rectFHeight = (TextUtil.getTxtHeight1(mHighLightValueLeftPaint) + txtTopPadding) * 2;

        }

        float edgeDistance = txtWidth / 2.0f;
        float rectTop = contentTop - rectFHeight;

        //绘制RectF
        RectF rectF = new RectF();
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);


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
        RectF topLeftRectF = new RectF(rectF.left + leftPadding, rectTop + txtTopPadding,
                rectF.left + leftPadding + txtLeftWidth, rectTop + rectFHeight / 2);

        mHighLightValueLeftPaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics fontMetrics = mHighLightValueLeftPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int leftTopBaseLineY = (int) (topLeftRectF.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式

        canvas.drawText(strings[0], rectF.left + leftPadding, leftTopBaseLineY, mHighLightValueLeftPaint);
        //绘文字
        RectF bottomLeftRectF = new RectF(rectF.left, rectTop + rectFHeight / 2,
                rectF.left + leftPadding + txtLeftWidth, rectBottom - txtTopPadding);

        int bottomLeftBaseLineY = (int) (bottomLeftRectF.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式

        canvas.drawText(strings[1], rectF.left + leftPadding, bottomLeftBaseLineY, mHighLightValueLeftPaint);

        float dividerLineStartX = rectF.left + leftPadding + txtLeftWidth + centerPadding / 2.0f;
        float dividerLineStartY = rectTop + DisplayUtil.dip2px(5);
        float dividerLineEndX = dividerLineStartX;
        float dividerLineEndY = rectBottom - DisplayUtil.dip2px(5);
        float[] lines = new float[]{dividerLineStartX, dividerLineStartY, dividerLineEndX, dividerLineEndY};
        canvas.drawLines(lines, mHighLightValueLeftPaint);

        float rightRectFStart = rectF.left + leftPadding + txtLeftWidth + centerPadding;

        mHighLightValueRightPaint.setTextAlign(Paint.Align.LEFT);
        Paint.FontMetrics rightFontMetrics = mHighLightValueRightPaint.getFontMetrics();
        float rightTop = rightFontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float rightBottom = rightFontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        Paint.FontMetrics rightUnitFontMetrics = mHighLightValueRightPaint.getFontMetrics();
        float rightUnitTop = rightUnitFontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float rightUnitBottom = rightUnitFontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom


        if (strings.length == 4) {
            float topRightRectFTop = rectTop + txtTopPadding;
            float topRightRectFBottom = topRightRectFTop + (rightUnitBottom - rightUnitTop)/2;
            RectF topRightRectF = new RectF(rightRectFStart, rectTop, rectF.right, topRightRectFBottom);
            int rightTopBaseLineY = (int) (topRightRectF.centerY() - rightUnitTop / 2 - rightUnitBottom / 2);//基线中间点的y轴计算公式
            canvas.drawText(strings[2], topRightRectF.left, rightTopBaseLineY, mHighLightUnitRightPaint);

            RectF bottomRightRectF = new RectF(rightRectFStart, topRightRectF.bottom, rectF.right, rectBottom - txtTopPadding/ 2.0f);
            int rightBottomBaseLineY = (int) (bottomRightRectF.centerY() - rightTop / 2 - rightBottom / 2);//基线中间点的y轴计算公式
            canvas.drawText(strings[3], bottomRightRectF.left, rightBottomBaseLineY, mHighLightValueRightPaint);
            float unitStart = topRightRectF.left + mHighLightValueRightPaint.measureText(strings[3]) + rightValueUnitPadding;
            canvas.drawText("步", unitStart, rightBottomBaseLineY, mHighLightUnitRightPaint);
        } else {// 右边只有一个文字，居中显示。
            RectF rightRectF = new RectF(rightRectFStart, rectTop + txtTopPadding, rectF.right, rectBottom - txtTopPadding);
            int rightBaseLineY = (int) (rightRectF.centerY() - rightTop / 2 - rightBottom / 2);//基线中间点的y轴计算公式
            canvas.drawText(strings[2], rightRectF.left, rightBaseLineY, mHighLightValueRightPaint);

            float unitStart = rightRectF.left + mHighLightValueRightPaint.measureText(strings[2]) + rightValueUnitPadding;

            int color = mHighLightValueLeftPaint.getColor();
            mHighLightValueLeftPaint.setColor(mBarChartAttrs.highLightRightTxtColor);
            canvas.drawText("步", unitStart, rightBaseLineY, mHighLightValueLeftPaint);
            mHighLightValueLeftPaint.setColor(color);
        }
    }

    private void drawHighLightLine(Canvas canvas, float[] floats) {
        Paint.Style previous = mBarChartPaint.getStyle();
        float strokeWidth = mBarChartPaint.getStrokeWidth();
        int color = mBarChartPaint.getColor();
        // set
        mBarChartPaint.setStyle(Paint.Style.FILL);
        mBarChartPaint.setStrokeWidth(DisplayUtil.dip2px(2));
        mBarChartPaint.setColor(mBarChartAttrs.barChartColor);
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


    final public void drawChart(final Canvas canvas, @NonNull final RecyclerView parent, YAxis mYAxis) {
        boolean mustInvalidate = false;
        if (parent != null && parent.getChildCount() > 0) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(child);
                BarEntry barEntry = (BarEntry) child.getTag();
                float width = child.getWidth();
                float barSpaceWidth = width * mBarChartAttrs.barSpace;
                final float left = child.getLeft() + barSpaceWidth / 2;
                if (position != RecyclerView.NO_POSITION && null != barEntry.drawable) {
                    mustInvalidate = true;
                    drawView(canvas, barEntry.drawable, left, child.getTop() + mBarChartAttrs.maxYAxisPaddingTop);
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

}
