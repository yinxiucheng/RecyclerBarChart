package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;

public class BezierCircle extends View {
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
    /**
     * 半径
     */
    private int radius;
    private float c;
    private float blackMagic = 0.551915024494f;

    private Context mContext;

    public BezierCircle(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public BezierCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public BezierCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        this.mContext = context;
    }

    /**
     * 初始化操作
     */
    private void init() {
        mFillCirclePaint = new Paint();
        mFillCirclePaint.setColor(0xFFFFFFFF);//fe626d);
        mFillCirclePaint.setStyle(Paint.Style.FILL);
        mFillCirclePaint.setStrokeWidth(1);
        mFillCirclePaint.setAntiAlias(true);
        mPath = new Path();
        p2 = new VPoint();
        p4 = new VPoint();
        p1 = new HPoint();
        p3 = new HPoint();

        radius = DisplayUtil.dip2px(24);
        c = radius * blackMagic;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        canvas.translate(getWidth() / 2, getHeight() * 0.6f);

        CircleModel(8 * radius);
        Path path8 = getPath(7 * radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop8), path8);

        CircleModel(7 * radius);
        Path path7 = getPath(7 * radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop7), path7);

        CircleModel(6 * radius);
        Path path6 = getPath(6 * radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop6), path6);

        CircleModel(5 * radius);
        Path path5 = getPath(5 * radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop5), path5);

        CircleModel(4 * radius);
        Path path4 = getPath(4 * radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop4), path4);

        CircleModel(3 * radius);
        Path path3 = getPath(3 * radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop3), path3);

        CircleModel(2 * radius);
        Path path2 = getPath(2 * radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop2), path2);

        CircleModel(radius);
        Path path1 = getPath(radius);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, R.color.water_drop1), path1);

    }


    /**
     * 画圆
     */
    private Path getPath(int radius) {
        Path mPath = new Path();
        mPath.moveTo(p1.x, p1.y);
        p3.setY(p3.y - radius * 0.2f * 1.5f);
        p3.y -= radius * 0.2f * 1.5f;
        mPath.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        mPath.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        mPath.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        mPath.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
        return mPath;
    }

    private void drawBezierPath(Canvas canvas, int color, Path path) {
        int colorOrigin = mFillCirclePaint.getColor();
        mFillCirclePaint.setColor(color);
        canvas.drawPath(path, mFillCirclePaint);
        mFillCirclePaint.setColor(colorOrigin);
        path.reset();
    }

    private void CircleModel(int radius) {
        c = radius * blackMagic;
        // p2.p4属于圆左右两点
        p1.setY(radius);//右边
        p3.setY(-radius);// 左边
        p3.x = p1.x = 0;//圆心
        p3.left.x = p1.left.x = -c;
        p3.right.x = p1.right.x = c;
        //p1.p3属于圆的上下两点
        p2.setX(radius); // 下边
        p4.setX(-radius);// 上边
        p2.y = p4.y = 0;//圆心
        p2.top.y = p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
    }
}
