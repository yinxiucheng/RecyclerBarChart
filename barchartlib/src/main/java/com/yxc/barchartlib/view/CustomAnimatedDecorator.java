package com.yxc.barchartlib.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

/**
 * @author yxc
 * @date 2019/4/17
 */
public class CustomAnimatedDecorator extends AnimatedDecoratorDrawable {

    Rect rect;
    public MovingNumber movingNumber;

    public CustomAnimatedDecorator(int height, int start, int end, int width) {
        super(width, height);
        rect = new Rect(0, height, width, height);
        movingNumber = new MovingNumber(start, end, height);

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        if (movingNumber.current > movingNumber.end + 1) {
            Log.d("Tag", "currentTop.end:" + movingNumber.end + " currentTop.current:" + movingNumber.current);
            rect.set(0, (int) movingNumber.getValue(), width / 2, height);
        }
        canvas.drawRect(rect, paint);
    }

    public class MovingNumber {
        public float start;
        public float end;
        public float current;

        public float distance;
        public int changeNumber = 200;
        public float itemDistance;

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
