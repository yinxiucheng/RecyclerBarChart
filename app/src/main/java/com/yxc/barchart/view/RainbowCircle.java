package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.commonlib.util.ColorUtil;

public class RainbowCircle extends View {
    /**
     * 路径
     */
    private Path mPath;
    /**
     * 画笔
     */
    private Paint mFillCirclePaint;

    /**
     * 半径
     */
    private Context mContext;

    private float blackMagic = 0.551915024494f;

    private int number;
    private int colorResource;
    private int radius;
    private int clipRadius;

    public RainbowCircle(Context context, int radius, int clipRadius, int number, int colorResource) {
        this(context, null);
        this.mContext = context;
        this.radius = radius;
        this.clipRadius = clipRadius;
        this.number = number;
        this.colorResource = colorResource;
        init();
    }

    public RainbowCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
        init();
    }

    public RainbowCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(0xFFFFFFFF);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(10);
        mFillCirclePaint.setAntiAlias(true);
        mPath = new Path();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        canvas.translate(getWidth() / 2, getHeight() * 0.6f);
        drawRainbow(canvas, radius, clipRadius, colorResource);
    }

    private void drawRainbow(Canvas canvas, int radius, int clipRadius, int colorResource) {
        canvas.save();
        BezierCircleModel circleModel =  createCircleModel(radius);
        BezierCircleModel circleModelInner =  createCircleModel(clipRadius);
        Path path = new Path();

        RectF rectF = new RectF();
        rectF.set(circleModel.p2.x, circleModel.p3.y, circleModel.p4.x, circleModel.p1.y);
        RectF rectFInner = new RectF();
        rectFInner.set(circleModelInner.p2.x, circleModelInner.p3.y, circleModelInner.p4.x, circleModelInner.p1.y);
//        path.moveTo(circleModelInner.p2.x, circleModelInner.p2.y);
        path.moveTo(circleModel.p2.x, circleModel.p2.y);
        path.arcTo(rectF, 180, 180, false);
//        path.cubicTo(circleModel.p2.top.x, circleModel.p2.top.y, circleModel.p3.right.x, circleModel.p3.right.y, circleModel.p3.x, circleModel.p3.y);
//        path.cubicTo(circleModel.p3.left.x, circleModel.p3.left.y, circleModel.p4.top.x, circleModel.p4.top.y, circleModel.p4.x, circleModel.p4.y);
//        path.lineTo(circleModelInner.p4.x, circleModelInner.p4.y);
//        path.moveTo(circleModelInner.p2.x, circleModelInner.p2.y);
        path.arcTo(rectF, 0, 180, false);
        path.close();

        //去锯齿
//        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, colorResource), path, rectF);
        canvas.restore();
    }

    /**
     * 画圆
     */
    private Path getPath(int radius) {
        BezierCircleModel circleModel =  createCircleModel(radius);
        Path path = new Path();
        path.moveTo(circleModel.p2.x, circleModel.p2.y);
        path.cubicTo(circleModel.p2.top.x, circleModel.p2.top.y, circleModel.p3.right.x,
                circleModel.p3.right.y, circleModel.p3.x, circleModel.p3.y);
        path.cubicTo(circleModel.p3.left.x, circleModel.p3.left.y,
                circleModel.p4.top.x, circleModel.p4.top.y, circleModel.p4.x, circleModel.p4.y);
        return path;
    }

    private void drawBezierPath(Canvas canvas, int color, Path path, RectF rectF) {
        int colorOrigin = mFillCirclePaint.getColor();
        mFillCirclePaint.setColor(color);
        canvas.drawRect(rectF, mFillCirclePaint);
        canvas.drawPath(path, mFillCirclePaint);
        mFillCirclePaint.setColor(colorOrigin);
    }

    private BezierCircleModel createCircleModel(int radius) {
        float c = radius * blackMagic;
        VPoint p2 = new VPoint();
        VPoint p4 = new VPoint();
        HPoint p1 = new HPoint();
        HPoint p3 = new HPoint();
        // p2.p4属于圆左右两点
        p1.setY(radius);//右边
        p3.setY(-radius);//左边
        p3.x = p1.x = 0;// 圆心
        p3.left.x = -c;
        p3.right.x = c;
        p1.left.x = -c;
        p1.right.x = c;
        //p1.p3属于圆的上下两点
        p2.setX(radius); // 下边
        p4.setX(-radius);// 上边
        p2.y = p4.y = 0;//圆心
        p2.top.y = p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
        return new BezierCircleModel(p1, p3, p2, p4);
    }
}
