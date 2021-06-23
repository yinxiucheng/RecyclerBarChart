package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;

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
     * 四个点
     */
    private VPoint p2;
    private VPoint p4;
    private HPoint p1;
    private HPoint p3;

    //内部的四个点
    private VPoint n2;
    private VPoint n4;
    private HPoint n1;
    private HPoint n3;

    /**
     * 半径
     */
    private Context mContext;

    private float c;
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
        mFillCirclePaint.setStrokeWidth(1);
        mFillCirclePaint.setAntiAlias(true);
        mPath = new Path();
        p2 = new VPoint();
        p4 = new VPoint();
        p1 = new HPoint();
        p3 = new HPoint();
        c = radius * blackMagic;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        canvas.translate(getWidth() / 2, getHeight() * 0.6f);
        drawRainbow(canvas, number, radius, clipRadius, colorResource);
    }

    private void drawRainbow(Canvas canvas, int number, int radius, int clipRadius, int colorResource) {
        canvas.save();
        Path path = getPath(radius);
        if (number > 1) {
//            int radiusCircle = (number - 1) * radius + DisplayUtil.dip2px(2);
            Path clipPath = getPath(clipRadius);
            path.op(clipPath, Path.Op.DIFFERENCE);
        }
        //去锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, colorResource), path);
        canvas.restore();
    }


    /**
     * 画圆
     */
    private Path getPath(int radius) {
        CircleModel(radius);
        Path path = new Path();
//        path.moveTo(p1.x, p1.y);
////        p3.setY(p3.y - radius * 0.2f * 1.5f);
////        p3.y -= radius * 0.2f * 1.8f;
//        path.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
//        path.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
//        path.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
//        path.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
//        path.close();

        path.moveTo(p2.x, p2.y);
        path.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        path.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
//        path.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
//        path.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        path.close();
        return path;
    }

    private void drawBezierPath(Canvas canvas, int color, Path path) {
        int colorOrigin = mFillCirclePaint.getColor();
        mFillCirclePaint.setColor(color);
        canvas.drawPath(path, mFillCirclePaint);
        mFillCirclePaint.setColor(colorOrigin);
    }

    private void CircleModel(int radius) {
        c = radius * blackMagic;
        // p2.p4属于圆左右两点
        p1.setY(radius);//右边
        p3.setY(-radius);// 左边
        p3.x = p1.x = 0;//圆心
//        p3.left.x = -c * 0.36f;
//        p3.right.x = c * 0.36f;
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
    }
}
