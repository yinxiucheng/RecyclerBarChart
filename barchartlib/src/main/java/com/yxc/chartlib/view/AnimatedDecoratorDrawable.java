package com.yxc.chartlib.view;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author yxc
 * @date 2019/4/17
 */
abstract public class AnimatedDecoratorDrawable {

    abstract public void draw(Canvas canvas, Paint paint);

    public float height = 0;
    public float width = 0;

    public AnimatedDecoratorDrawable(float width, float height) {
        this.width = width;
        this.height = height;
    }
}
