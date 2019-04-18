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

    public static final long ANIMATE_DELAY = 1L;

    long lastTimeAnimated = 0L;
    long deltaTime = 0L;

    Rect rect = new Rect(0, 0, width, height);
    Paint paint = new Paint();

    MovingNumber currentTop = new MovingNumber(0, height, height);

    public CustomAnimatedDecorator(int height, int width){
        super(height, width);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void draw(Canvas canvas) {
        lastTimeAnimated = SystemClock.uptimeMillis();
        if (currentTop.current < currentTop.end + 1) {
            deltaTime = lastTimeAnimated;
            rect.set(0, currentTop.getValue(), width/2, height);
        }
        canvas.drawRect(rect, paint);
    }


    public class MovingNumber {

        public int start;
        public int end;
        public int current;
        public Direction direction;
        public int value;

        public int getValue() {
            if (direction == Direction.FORWARD) {
                if (current > 0){
                    Log.d("Decorator", "FORWARD");
                    current--;
                }
            } else {
                Log.d("Decorator", "BACKWARD");
                current++;
            }
            return current;
        }

        public MovingNumber(int start, int end, int current) {
            this.start = start;
            this.end = end;
            this.current = current;
            this.value = current;

            if (Math.abs(current - start) > Math.abs(current - end)) {
                direction = Direction.FORWARD;
            } else {
                direction = Direction.BACKWARD;
            }
        }
    }
}
