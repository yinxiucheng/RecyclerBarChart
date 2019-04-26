package com.yxc.chartlib.component;

import android.graphics.RectF;

/**
 * @author yxc
 * @date 2019/4/8
 *  想做属性动画，包装的一个类
 */
public class ChartRectF extends RectF {

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
