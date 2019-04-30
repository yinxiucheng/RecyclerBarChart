package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;

/**
 * @author yxc
 * @date 2019/4/29
 */
public class WaterDropCore extends View {
    private Path mPath;
    private Paint mFillCirclePaint;
    private VPoint p2;
    private VPoint p4;
    private HPoint p1;
    private HPoint p3;

    private Context mContext;
    private float control;
    private float blackMagic = 0.551915024494f;

    private int radius = DisplayUtil.dip2px(24);
    private int number = 2;
    private int colorResource;

    public WaterDropCore(Context context, int radius, int number, int colorResource) {
        this(context, null);
        this.mContext = context;
        this.radius = radius;
        this.number = number;
        this.colorResource = colorResource;
        init();
    }

    public WaterDropCore(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
        init();
    }

    public WaterDropCore(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
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
        colorResource = ColorUtil.getResourcesColor(mContext, R.color.water_drop5);
        radius = DisplayUtil.dip2px(24);
        control = radius * blackMagic;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        canvas.translate(getWidth() / 2, getHeight() * 0.6f);
        drawWaterDrop(canvas, number, radius, colorResource);
    }


    private void drawWaterDrop(Canvas canvas, int number, int radius, int colorResource) {
        canvas.save();
        Path path = getPath(number * radius);
        if (number > 1) {
            int radiusCircle = (number - 1) * radius;
            Path clipPath = getPath(radiusCircle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                canvas.clipOutPath(clipPath);
            } else {
                canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
            }
        }
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, colorResource), path);
        canvas.restore();
    }

    /**
     * 画圆
     */
    private Path getPath(int radius) {
        CircleModel(radius);
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        p3.setY(p3.y - radius * 0.2f * 1.5f);
        p3.y -= radius * 0.2f * 1.8f;
        path.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        path.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        path.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        path.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);
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
        control = radius * blackMagic;
        // p2.p4属于圆左右两点
        p1.setY(radius);//右边
        p3.setY(-radius);// 左边
        p3.x = p1.x = 0;//圆心
        p3.left.x = -control * 0.36f;
        p3.right.x = control * 0.36f;
        p1.left.x = -control;
        p1.right.x = control;
        //p1.p3属于圆的上下两点
        p2.setX(radius); // 下边
        p4.setX(-radius);// 上边
        p2.y = p4.y = 0;//圆心
        p2.top.y = p4.top.y = -control;
        p2.bottom.y = p4.bottom.y = control;
    }
}
