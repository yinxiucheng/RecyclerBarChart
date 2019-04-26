package com.yxc.chartlib.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

/**
 * @author yxc
 * @date 2019/4/17
 */
public class CustomAnimatedDecorator extends AnimatedDecoratorDrawable {

    RectF rect;
    public float start;
    public float end;
    public float current;
    public int changeNumber = 20;
    private float distance;
    private float itemDistance;

    public CustomAnimatedDecorator(float width, float height){
        super(width, height);
        rect = new RectF();
    }

    public CustomAnimatedDecorator(float width, float height, float start, float end) {
        super(width, height);
        rect = new RectF();
        bindData(width, height, start, end);
    }

    public void bindData(float width, float height, float start, float end){
//        rect = new RectF(0, height, width, height);
        rect.set(0, height, width, height);
        this.start = start;
        this.end = end;
        this.current = height - start;
        distance = height - end;
        itemDistance = distance / changeNumber;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (current > end + 1) {
            Log.d("Decorator", "currentTop.end:" + end + " currentTop.current:" + current);
            rect.set(rect.left, getValue(), rect.right, rect.bottom);
        }
        canvas.drawRect(rect, paint);
    }

    private float getValue() {
        if (current > 0) {
            Log.d("Decorator", "FORWARD , current:" + current);
            current = current - itemDistance;
        }
        return current;
    }
}
