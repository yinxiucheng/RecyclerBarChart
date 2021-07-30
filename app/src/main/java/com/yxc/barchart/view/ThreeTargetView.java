package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
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
        circlePaint.setStyle(Paint.Style.STROKE);
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
        RectF bgRectF = new RectF(padding, padding, width - padding, height - padding);
        RectF innerRectF = new RectF(padding + itemWidth, padding + itemWidth, width - padding - itemWidth,
                height - padding - itemWidth);

        PathModel pathModel = generalPathModelWrapper(bgRectF, 0.015f);
        PathModel pathModelInner = generalPathModelInner(innerRectF, 0.02f);

        float wrapperYHalfRectF = bgRectF.top + bgRectF.height()/2;
        float wrapperHalfSquareWidth = wrapperYHalfRectF - pathModel.leftPointF.y;

        float innerYHalfRectF = innerRectF.top + innerRectF.height()/2;
        float innerHalfSquareWidth = innerYHalfRectF - pathModelInner.leftPointF.y;

        Path path = new Path();
        path.moveTo(pathModelInner.leftPointF.x - innerHalfSquareWidth, wrapperYHalfRectF);
        path.lineTo(pathModel.leftPointF.x + wrapperHalfSquareWidth, wrapperYHalfRectF);
        path.arcTo(pathModel.leftSmallRectF, 90, 90, false);
        path.addPath(pathModel.pathArc);
        path.arcTo(pathModel.rightSmallRectF, 0, 90, false);
        path.lineTo(pathModelInner.endPointF.x + innerHalfSquareWidth, wrapperYHalfRectF);
        path.arcTo(pathModelInner.rightSmallRectF, 90, 90, true);
        path.addPath(pathModelInner.pathArc);
        path.arcTo(pathModelInner.leftSmallRectF, 0, 90, true);

        circlePaint.setColor(firstColor);
        circlePaint.setAlpha(transParentValue);
        circlePaint.setStrokeWidth(paintWidth);
        canvas.drawPath(path, circlePaint);
        circlePaint.setColor(originalColor);
        canvas.restore();
    }

    private PathModel generalPathModelInner(RectF rectF, float fractionArc){
        PathModel pathModel = generalPathModel(rectF, fractionArc);
        float yHalfRectF = rectF.top + rectF.height()/2;
        float halfSquareWidth = yHalfRectF - pathModel.leftPointF.y;
        RectF smallLeftRectF = new RectF(pathModel.leftPointF.x - 2 * halfSquareWidth,
                yHalfRectF - 2 * halfSquareWidth, pathModel.leftPointF.x, yHalfRectF);
        pathModel.setLeftSmallRectF(smallLeftRectF);

        RectF smallRightRectF = new RectF(pathModel.endPointF.x , yHalfRectF - 2 * halfSquareWidth,
                pathModel.endPointF.x + 2 * halfSquareWidth, yHalfRectF);
        pathModel.setRightSmallRectF(smallRightRectF);
        return pathModel;
    }

    private PathModel generalPathModelWrapper(RectF rectF, float fractionArc){
        PathModel pathModel = generalPathModel(rectF, fractionArc);
        float yHalfRectF = rectF.top + rectF.height()/2;
        float halfSquareWidth = yHalfRectF - pathModel.leftPointF.y;
        RectF smallLeftRectF = new RectF(pathModel.leftPointF.x, yHalfRectF - 2 * halfSquareWidth,
                pathModel.leftPointF.x + 2 * halfSquareWidth, yHalfRectF);
        pathModel.setLeftSmallRectF(smallLeftRectF);

        RectF smallRightRectF = new RectF(pathModel.endPointF.x - 2 * halfSquareWidth, yHalfRectF - 2 * halfSquareWidth,
                pathModel.endPointF.x, yHalfRectF);
        pathModel.setRightSmallRectF(smallRightRectF);
        return pathModel;
    }

    private PathModel generalPathModel(RectF rectF, float fractionArc) {
        Path pathOriginal = new Path();
        pathOriginal.moveTo(rectF.left, (rectF.top + rectF.bottom) / 2);
        pathOriginal.arcTo(rectF, 180, 180, true);
        PathMeasure pathMeasure = new PathMeasure(pathOriginal, true);
        float radius = rectF.width() / 2;
        float halfCircleLength = DecimalUtil.getDecimalFloat(DecimalUtil.THREE_LENGTH_DECIMAL,
                (float) (Math.PI * radius));
        float[] dstPathFirstPoint = new float[2];
        float[] dstPathEndPoint = new float[2];
        float firstPointLength = halfCircleLength * fractionArc;
        float endPointLength = halfCircleLength * (1 - fractionArc);
        pathMeasure.getPosTan(firstPointLength, dstPathFirstPoint, null);
        pathMeasure.getPosTan(endPointLength, dstPathEndPoint, null);
        PointF startPointF = new PointF(dstPathFirstPoint[0], dstPathFirstPoint[1]);
        PointF endPointF = new PointF(dstPathEndPoint[0], dstPathEndPoint[1]);
        Path clipCirclePath = new Path();
        pathMeasure.getSegment(firstPointLength, endPointLength, clipCirclePath, true);
        PathModel pathModel = new PathModel(startPointF, endPointF, clipCirclePath);
        return pathModel;
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
