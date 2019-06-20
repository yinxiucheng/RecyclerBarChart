package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.commonlib.util.TextUtil;

public class LocationMarker extends View {
    private Path mPath;
    private Paint mFillCirclePaint;
    private Paint mTextPaint;
    private VPoint p2;
    private VPoint p4;
    private HPoint p1;
    private HPoint p3;
    private Context mContext;

    private float c;
    private float blackMagic = 0.551915024494f;
    private int wrapperColor;
    private int circleColor;

    private int radius = DisplayUtil.dip2px(20);
    private String mMilePost;
    private int textSize = 13;

    private boolean drawBottomShader;

    public LocationMarker(Context context, int radius, String milePost, int textSize) {
        this(context, null);
        this.mContext = context;
        this.radius = radius;
        this.mMilePost = milePost;
        this.textSize = textSize;
        init();
    }

    public LocationMarker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
        init();
    }

    public LocationMarker(Context context, AttributeSet attrs, int defStyleAttr) {
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
        initTextPain();
        wrapperColor = R.color.location_wrapper;
        circleColor = R.color.location_inner_circle;
    }

    public void setColors(int wrapperColorResource, int circleColorResource){
        this.wrapperColor = wrapperColorResource;
        this.circleColor = circleColorResource;
    }


    private void initTextPain() {
        mTextPaint = new Paint();
        mTextPaint.setColor(0xFFFFFFFF);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(DisplayUtil.sp2px(mContext, textSize));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        canvas.translate(getWidth() / 2, getHeight() / 2);
        drawWaterDrop(canvas, radius);
    }

    private void drawWaterDrop(Canvas canvas, int radius) {
        canvas.save();
        Path path = getPath(radius);

        Path circle = new Path();
        circle.addCircle(p3.x, p3.y + radius, radius - radius / 5, Path.Direction.CCW);

        //去锯齿
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        drawBottomOval(canvas);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, wrapperColor), path);
        drawBezierPath(canvas, ColorUtil.getResourcesColor(mContext, circleColor), circle);
        drawText(canvas, mMilePost);
        canvas.restore();
    }

    private void drawBottomOval(Canvas canvas){
        if (drawBottomShader){
            RectF rectF = new RectF();
            float width = DisplayUtil.dip2px(12);
            float height = DisplayUtil.dip2px(4);
            rectF.set(p1.x - width/2, p1.y - height/2, p1.x + width/2, p1.y + height/2);
            int color = mFillCirclePaint.getColor();
            mFillCirclePaint.setColor(ColorUtil.getResourcesColor(mContext, R.color.location_bottom_shader));
            canvas.drawOval(rectF, mFillCirclePaint);
            mFillCirclePaint.setColor(color);
        }
    }

    private void drawText(Canvas canvas, String mileStr) {

        RectF rectF = new RectF();
        float width = mTextPaint.measureText(mileStr);

        float rectFLeft = p3.x - width / 2 ;
        float rectFRight = p3.x + width / 2 ;

        float rectHeight = TextUtil.getTxtHeight1(mTextPaint);
        float rectTop = p2.y + DisplayUtil.dip2px(2);//调整位置，看起来居中
        float rectBottom = p2.y + rectHeight;

        rectF.set(rectFLeft, rectTop, rectFRight, rectBottom);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (rectF.centerY() + (top + bottom) / 2);//基线中间点的y轴计算公式
        canvas.drawText(mileStr, rectF.left, baseLineY, mTextPaint);

    }


    /**
     * 画圆
     */
    private Path getPath(int radius) {
        CircleModel(radius);
        Path path = new Path();
        p1.setY(p1.y + radius * 0.2f * 1.05f); //设置 p1 底部左右两个点的y值
        p1.y += radius * 0.2f * 1.05f;//设置 p1 自己的y值
        path.moveTo(p1.x, p1.y);
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
        c = radius * blackMagic;
        p1.setY(radius);//右边
        p3.setY(-radius);// 左边
        p3.x = p1.x = 0;//圆心

        p3.left.x = -c;
        p3.right.x = c;
        p1.left.x = -c * 0.36f;
        p1.right.x = c * 0.36f;
        //p1.p3属于圆的上下两点
        p2.setX(radius); // 下边
        p4.setX(-radius);// 上边
        p2.y = p4.y = 0;//  圆心
        p2.top.y = p4.top.y = -c;
        p2.bottom.y = p4.bottom.y = c;
    }


    public void setDrawBottomShader(boolean drawBottomShader) {
        this.drawBottomShader = drawBottomShader;
    }

}
