package com.yxc.barchartlib.view;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Log;

/**
 * @author yxc
 * @date 2019/4/17
 */
public class CustomAnimatedDecorator extends AnimatedDecoratorDrawable {

    long lastTimeAnimated = 0L;
    long deltaTime = 0L;
    Rect rect;
    Paint paint = new Paint();

    public MovingNumber movingNumber;

    public CustomAnimatedDecorator(int height, int end, int width) {
        super(width, height);
        movingNumber = new MovingNumber(0, end, height);
        rect = new Rect(0, height, width, height);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas) {
        lastTimeAnimated = SystemClock.uptimeMillis();
        if (movingNumber.current > movingNumber.end + 1) {
            Log.d("Tag", "currentTop.end:" + movingNumber.end + " currentTop.current:" + movingNumber.current);
            rect.set(0, movingNumber.getValue(), width / 2, height);
        }
        canvas.drawRect(rect, paint);
    }

    public class MovingNumber {
        public int start;
        public int end;
        public int current;
        public int value;

        public int getValue() {
            if (current > 0) {
                Log.d("Decorator", "FORWARD , current:" + current);
                current = current - 30;
            }
            return current;
        }

        public MovingNumber(int start, int end, int current) {
            this.start = start;
            this.end = end;
            this.current = current;
            this.value = current;
        }

        public void setValue(int value) {
            this.value = value;
            this.current = value;
        }
    }
}
