package com.yxc.chartlib.recyclerchart.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.recyclerchart.attrs.BaseChartAttrs;

/**
 * @author yxc
 * @date 2019/4/14
 */
final public class HrmBarBoardRender<T extends BaseChartAttrs> {

    T mBarChartAttrs;
    Paint mBarBorderPaint;
    Paint mBgPaint;

    public HrmBarBoardRender(T attrs) {
        this.mBarChartAttrs = attrs;
        initBarBorderPaint();
    }

    private void initBarBorderPaint() {
        mBarBorderPaint = new Paint();
        mBarBorderPaint.reset();
        mBarBorderPaint.setAntiAlias(true);
        mBarBorderPaint.setStyle(Paint.Style.STROKE);
        mBarBorderPaint.setStrokeWidth(mBarChartAttrs.barBorderWidth);
        mBarBorderPaint.setColor(mBarChartAttrs.barBorderColor);

        mBgPaint = new Paint();
        mBgPaint.reset();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setAlpha((int)(255 * 0.2));
        mBgPaint.setColor(mBarChartAttrs.barBorderBgColor);
    }

    final public void drawBarBorder(@NonNull Canvas canvas, @NonNull RecyclerView parent) {
        if (mBarChartAttrs.enableBarBorder) {
            RectF rectF = new RectF();
            float start = parent.getPaddingLeft();
            float top = parent.getPaddingTop();
            float end = parent.getRight() - parent.getPaddingRight();
            //底部有0的刻度是不是不用画，就画折线了。
            float bottom = parent.getHeight() - parent.getPaddingBottom() - mBarChartAttrs.contentPaddingBottom;
            rectF.set(start, top, end, bottom);
            mBarBorderPaint.setStrokeWidth(mBarChartAttrs.barBorderWidth);
            canvas.drawRect(rectF, mBarBorderPaint);

            canvas.drawRect(rectF, mBgPaint);
        }
    }



}
