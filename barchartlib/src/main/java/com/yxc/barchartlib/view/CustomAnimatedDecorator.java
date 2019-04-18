package com.yxc.barchartlib.view;

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
    public MovingNumber movingNumber;

    public CustomAnimatedDecorator(float width, float height, float start, float end) {
        super(width, height);
        rect = new RectF(0, height, width, height);
        movingNumber = new MovingNumber(start, end, height);
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (movingNumber.current > movingNumber.end + 1) {
            Log.d("Decorator", "currentTop.end:" + movingNumber.end + " currentTop.current:" + movingNumber.current);
            rect.set(rect.left, (int) movingNumber.getValue(), rect.right, rect.bottom);
        }
        canvas.drawRect(rect, paint);
    }

    public class MovingNumber {

        public float start;
        public float end;
        public float current;
        public int changeNumber = 30;
        private float distance;
        private float itemDistance;

        public float getValue() {
            if (current > 0) {
                Log.d("Decorator", "FORWARD , current:" + current);
                current = current - itemDistance;
            }
            return current;
        }

        public MovingNumber(float start, float end, float current) {
            this.start = start;
            this.end = end;
            this.current = current - start;
            distance = current - end;
            itemDistance = distance / changeNumber;
        }

        public void setValue(float value) {
            this.current = value;
        }
    }
}
