package com.yxc.barchartlib.view;

import android.graphics.Canvas;

/**
 * @author yxc
 * @date 2019/4/17
 */
abstract public class AnimatedDecoratorDrawable {

    abstract public void draw(Canvas canvas);

    public int height = 0;
    public int width = 0;

    public AnimatedDecoratorDrawable(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
