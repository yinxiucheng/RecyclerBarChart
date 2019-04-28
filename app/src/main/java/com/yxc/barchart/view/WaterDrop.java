package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.commonlib.util.PathUtil;

/**
 * @author yxc
 * @date 2019/4/28
 */
public class WaterDrop extends View {

    private int mainColor = Color.parseColor("#E55049");

    private float xoff = 70f;
    private float yoff = 0f;
    private float startX = 0f;
    private float startY = 0f;
    private float endY = 0f;
    private float endX = 0f;

    private PointF startPoint = new PointF();
    private PointF endPoint = new PointF();
    private PointF control1 = new PointF();
    private PointF control2 = new PointF();
    private PointF control3 = new PointF();
    private PointF control4 = new PointF();

    private float circleX = 0f;
    private float circleY = 0f;
    private float circleR = 0f;

    private Path waterPath = new Path();

    private int canvasW = 0;
    private int canvasH = 0;

    private Paint waterPaint;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public WaterDrop(Context context) {
        super(context);
        initPaint();
    }

    public WaterDrop(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public WaterDrop(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint(){
        waterPaint = new Paint();
        waterPaint.setStyle(Paint.Style.FILL);
        waterPaint.setAntiAlias(true);
        waterPaint.setColor(mainColor);
    }

    private void initPoint() {
        xoff = canvasW / 4f;
        yoff = canvasH / 6f;
        startPoint.x = canvasW / 2f;
        startPoint.y = yoff;
        endPoint.x = startPoint.x;
        endPoint.y = canvasH - 1.6f * yoff;

        startX = startPoint.x;
        startY = startPoint.y;
        endX = endPoint.x;
        endY = endPoint.y;
    }

    private void calculatePoint() {
        control1.x = startPoint.x - xoff;
        control1.y = startPoint.y;

        control2.x = startPoint.x + xoff;
        control2.y = control1.y;

        control3.x = startPoint.x - xoff / 3;
        control3.y = endPoint.y - startPoint.y;

        control4.x = startPoint.x + xoff / 3;
        control4.y = control3.y;

        circleX = canvasW / 2f;
        circleR = xoff / 4;
        circleY = startPoint.y + circleR + circleR / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        canvasW = MeasureSpec.getSize(widthMeasureSpec);
        canvasH = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(canvasW, canvasH);
        initPoint();
        calculatePoint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawWater(canvas);
    }


    private void drawWater(Canvas canvas) {
        waterPath.reset();
        PathUtil.moveTo(waterPath, startPoint);
        PathUtil.cubicTo(waterPath, control1, control3, endPoint);
        PathUtil.cubicTo(waterPath, control4, control2, startPoint);
        canvas.save();

        Path circle = new Path();
        circle.addCircle(circleX, circleY, circleR, Path.Direction.CW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            canvas.clipOutPath(circle);
        } else {
            canvas.clipPath(circle, Region.Op.DIFFERENCE);
        }
        canvas.drawPath(waterPath, waterPaint);
        canvas.drawCircle(circleX, circleY, circleR, waterPaint);
        canvas.restore();
    }
}
