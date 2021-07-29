package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.yxc.barchart.R;
import com.yxc.chartlib.util.DecimalUtil;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;

/**
 * @author yxc
 * @date 2019-09-27
 */
public class ThreeTargetView extends View {

    public static final int ANGLE = 0;
    private Context mContext;
    private Paint circlePaint;
    private int transParentValue;

    public ThreeTargetView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ThreeTargetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public ThreeTargetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }


    //固定宽高。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        drawThreeCircle(canvas, width, height);
    }

    private void drawThreeCircle(Canvas canvas, int width, int height){
        float itemWidth = width / 6.5f;
        float padding = DisplayUtil.dip2px(2);
        float spaceWidth = itemWidth / 7.5f;
//        float paintWidth = itemWidth - spaceWidth;
        float paintWidth = DisplayUtil.dip2px(2);
        drawCircle(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
//        drawCircle2(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
//        drawCircle3(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
    }

    private float firstPercent = 0;
    private float secondPercent = 0;
    private float thirdPercent = 0;
    private int firstColor = -1;
    private int secondColor = -1;
    private int thirdColor = -1;

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setStrokeCap(Paint.Cap.BUTT);
        transParentValue = (int) (255 * 0.8);
        firstColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color1);
        secondColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color2);
        thirdColor = ColorUtil.getResourcesColor(mContext, R.color.rainbow_color3);
    }

    private void drawCircle(Canvas canvas, int width, int height, float padding,
                            float itemWidth, float paintWidth, float spaceWidth) {
        canvas.save();
        int originalColor = circlePaint.getColor();
        Path pathOriginal = new Path();
        RectF bgRectF = new RectF(padding, padding, width - padding, height - padding);
        pathOriginal.moveTo(padding, padding + height/2);
        pathOriginal.arcTo(bgRectF, 180, 180);
        PathMeasure pathMeasure = new PathMeasure(pathOriginal, true);
        float radius = (width - 2 * padding) / 2;
        float halfCircleLength = DecimalUtil.getDecimalFloat(DecimalUtil.THREE_LENGTH_DECIMAL,
                (float) (Math.PI * radius));

        Path path = new Path();
        float[] dstPathFirstPoint = new float[2];
        float[] dstPathEndPoint = new float[2];
        float firstPointLength = halfCircleLength * 0.015f;
        float endPointLength = halfCircleLength * 0.985f;
        pathMeasure.getPosTan(firstPointLength, dstPathFirstPoint, null);
        pathMeasure.getPosTan(endPointLength, dstPathEndPoint, null);
        float halfSquareWidth = height/2 - dstPathFirstPoint[1];

        path.moveTo(padding + itemWidth, height/2);
        path.lineTo(dstPathFirstPoint[0] + halfSquareWidth, height/2);

        RectF smallLeftRectF = new RectF(dstPathFirstPoint[0], height/2 - 2 * halfSquareWidth,
                dstPathFirstPoint[0] + 2* halfSquareWidth, height/2);
        path.arcTo(smallLeftRectF, 90, 90, false);
        Path clipCirclePath = new Path();
        pathMeasure.getSegment(firstPointLength, endPointLength, clipCirclePath, true);
        path.addPath(clipCirclePath);

        RectF smallRightRectF = new RectF(dstPathEndPoint[0] - 2* halfSquareWidth, height/2 - 2 * halfSquareWidth,
                dstPathEndPoint[0], height/2);
        path.arcTo(smallRightRectF, 0, 90, false);
        path.lineTo(width - itemWidth - padding, height/2);
        RectF innerRectF = new RectF(padding + itemWidth, padding + itemWidth, width - padding - itemWidth, height - padding - itemWidth);
        path.arcTo(innerRectF, 0, -180, false);
        circlePaint.setColor(firstColor);
        circlePaint.setAlpha(transParentValue);
        circlePaint.setStrokeWidth(paintWidth);
        canvas.drawPath(path, circlePaint);
        circlePaint.setColor(originalColor);
        canvas.restore();
    }

    private void drawCircle2(Canvas canvas, int width, int height, float padding,
                             float itemWidth, float paintWidth, float spaceWidth) {
        canvas.save();
        RectF bgRectF = new RectF(padding + itemWidth,
                padding + itemWidth, width - (padding + itemWidth), height - (padding + itemWidth));
        Path path = new Path();
        path.arcTo(bgRectF, 180, 180, true);
        circlePaint.setColor(secondColor);
        circlePaint.setAlpha(transParentValue);
        circlePaint.setStrokeWidth(paintWidth);
        canvas.drawPath(path, circlePaint);

        Path path2 = new Path();
        path2.arcTo(bgRectF, 180, 180 * secondPercent, true);
        circlePaint.setStrokeWidth(paintWidth);
        circlePaint.setAlpha(255);
        canvas.drawPath(path2, circlePaint);

        canvas.restore();
    }

    private void drawCircle3(Canvas canvas, int width, int height, float padding,
                             float itemWidth, float paintWidth, float spaceWidth) {
        canvas.save();
        RectF bgRectF = new RectF(padding + 2 * itemWidth, padding + 2 * itemWidth,
                width - (padding + 2 * itemWidth), height - (padding + 2 * itemWidth));
        Path path = new Path();
        path.arcTo(bgRectF, 180, 180, true);
        circlePaint.setStrokeWidth(paintWidth);
        circlePaint.setColor(thirdColor);
        circlePaint.setAlpha(transParentValue);
        canvas.drawPath(path, circlePaint);

        Path path2 = new Path();
        path2.arcTo(bgRectF, 180, 180 * thirdPercent, true);
        circlePaint.setStrokeWidth(paintWidth);
        circlePaint.setAlpha(255);
        canvas.drawPath(path2, circlePaint);
        canvas.restore();
    }

    private void resetPercent(){
        firstPercent = 0;
        secondPercent = 0;
        thirdPercent = 0;
    }

}
