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
        drawCircleBg(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
//        drawCircle(canvas, width, height, padding, itemWidth, paintWidth, spaceWidth);
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
        PathModel pathModelInner = generalPathModelInner(innerRectF, 0.015f);

        float wrapperYHalfRectF = bgRectF.top + bgRectF.height()/2;
        float wrapperHalfSquareWidth = wrapperYHalfRectF - pathModel.startPointF.y;
        float innerYHalfRectF = innerRectF.top + innerRectF.height()/2;
        float innerHalfSquareWidth = innerYHalfRectF - pathModelInner.startPointF.y;

        Path path = new Path();
        path.moveTo(pathModelInner.endPointF.x - innerHalfSquareWidth, wrapperYHalfRectF);
        path.lineTo(pathModel.startPointF.x + wrapperHalfSquareWidth, wrapperYHalfRectF);
        path.addPath(pathModel.startPathArc);
//        path.arcTo(pathModel.startSmallRectF, 90, 90, false);
        path.addPath(pathModel.pathArc);
        path.addPath(pathModel.endPathArc);
//        path.arcTo(pathModel.endSmallRectF, 0, 90, false);
        path.lineTo(pathModelInner.endPointF.x + innerHalfSquareWidth, wrapperYHalfRectF);
        path.addPath(pathModelInner.startPathArc);
//        path.arcTo(pathModelInner.endSmallRectF, 90, 90, true);
        path.addPath(pathModelInner.pathArc);
        path.addPath(pathModelInner.endPathArc);
//        path.arcTo(pathModelInner.startSmallRectF, 0, 90, true);

        circlePaint.setColor(thirdColor);
        circlePaint.setAlpha(transParentValue);
        circlePaint.setStrokeWidth(paintWidth);
        canvas.drawPath(path, circlePaint);
        circlePaint.setColor(originalColor);
        canvas.restore();
    }

    private PathModel generalPathModelInner(RectF rectF, float fractionArc){
        PathModel pathModel = generalPathModelRight(rectF, fractionArc, 180, 60);
        float yHalfRectF = rectF.top + rectF.height()/2;
        float halfSquareWidth = yHalfRectF - pathModel.endPointF.y;

        float x = (float) (pathModel.startPointF.x - halfSquareWidth * Math.cos(60));
        float y = (float) (pathModel.startPointF.y - halfSquareWidth * Math.sin(60));

//        RectF smallStartRectF = new RectF(pathModel.startPointF.x - 2 * halfSquareWidth,
//                yHalfRectF - 2 * halfSquareWidth, pathModel.startPointF.x, yHalfRectF);
//        pathModel.setStartSmallRectF(smallStartRectF);

        RectF smallStartRectF = new RectF(x - halfSquareWidth, y - halfSquareWidth, x + halfSquareWidth, y + halfSquareWidth);
        Path startSmallPath = new Path();
        startSmallPath.moveTo(x, y - halfSquareWidth);
        startSmallPath.arcTo(smallStartRectF, 0, 90);
//        Matrix matrix = new Matrix();
//        matrix.setRotate(-30);
//        startSmallPath.transform(matrix);
        pathModel.setStartPathArc(startSmallPath);

        RectF smallRightRectF = new RectF(pathModel.endPointF.x , yHalfRectF - 2 * halfSquareWidth,
                pathModel.endPointF.x + 2 * halfSquareWidth, yHalfRectF);
        pathModel.setEndSmallRectF(smallRightRectF);
        Path endSmallPath = new Path();
        endSmallPath.moveTo(pathModel.endPointF.x, pathModel.endPointF.y);
        endSmallPath.arcTo(smallRightRectF, 0, 90);
        pathModel.setEndPathArc(endSmallPath);
        return pathModel;
    }

    private PathModel generalPathModelWrapper(RectF rectF, float fractionArc){
        PathModel pathModel = generalPathModelLeft(rectF, fractionArc, 180, 60);
        float yHalfRectF = rectF.top + rectF.height()/2;
        float halfSquareWidth = yHalfRectF - pathModel.startPointF.y;

        RectF smallStartRectF = new RectF(pathModel.startPointF.x, yHalfRectF - 2 * halfSquareWidth, pathModel.startPointF.x + 2 * halfSquareWidth, yHalfRectF);
        pathModel.setStartSmallRectF(smallStartRectF);
        Path startSmallPath = new Path();
        startSmallPath.moveTo(pathModel.startPointF.x + halfSquareWidth, yHalfRectF);
        startSmallPath.arcTo(smallStartRectF, 90, 90);
        pathModel.setStartPathArc(startSmallPath);

        float x = (float) (pathModel.endPointF.x + halfSquareWidth * Math.cos(60));
        float y = (float) (pathModel.endPointF.y + halfSquareWidth * Math.sin(60));
        RectF smallEndRectF = new RectF(x - halfSquareWidth, y - halfSquareWidth, x + halfSquareWidth, y + halfSquareWidth);
        Path endSmallPath = new Path();
        endSmallPath.moveTo(x, y - halfSquareWidth);
        endSmallPath.arcTo(smallEndRectF, 270, 90);
//        Matrix matrix = new Matrix();
//        matrix.setRotate(-60);
//        endSmallPath.transform(matrix);
        pathModel.setEndPathArc(endSmallPath);
        pathModel.setHalfSquareWidth(halfSquareWidth);
        return pathModel;
    }

    private void drawCircleBg(Canvas canvas, int width, int height, float padding,
                            float itemWidth, float paintWidth, float spaceWidth) {
        canvas.save();
        int originalColor = circlePaint.getColor();
        RectF bgRectF = new RectF(padding, padding, width - padding, height - padding);
        RectF innerRectF = new RectF(padding + itemWidth, padding + itemWidth, width - padding - itemWidth,
                height - padding - itemWidth);
        PathModel pathModel = generalPathModelWrapperBg(bgRectF, 0.015f);
        PathModel pathModelInner = generalPathModelInnerBg(innerRectF, 0.015f);
        float wrapperYHalfRectF = bgRectF.top + bgRectF.height()/2;
        float wrapperHalfSquareWidth = wrapperYHalfRectF - pathModel.startPointF.y;
        float innerYHalfRectF = innerRectF.top + innerRectF.height()/2;
        float innerHalfSquareWidth = innerYHalfRectF - pathModelInner.startPointF.y;

        Path path = new Path();
        path.moveTo(pathModelInner.endPointF.x, pathModelInner.endPointF.y);
        path.addPath(pathModelInner.endPathArc);
        path.lineTo(pathModelInner.endPointF.x - innerHalfSquareWidth, wrapperYHalfRectF);
        path.lineTo(pathModel.startPointF.x + wrapperHalfSquareWidth, wrapperYHalfRectF);
        path.addPath(pathModel.startPathArc);
//        path.arcTo(pathModel.startSmallRectF, 90, 90);
        path.addPath(pathModel.pathArc);
        path.addPath(pathModel.endPathArc);
//        path.arcTo(pathModel.endSmallRectF, 0, 90, false);
        path.lineTo(pathModelInner.startPointF.x + innerHalfSquareWidth, wrapperYHalfRectF);
        path.addPath(pathModelInner.startPathArc);
//        path.arcTo(pathModelInner.startSmallRectF, 90, 90, false);
        path.addPath(pathModelInner.pathArc);

//        path.arcTo(pathModelInner.endSmallRectF, 0, 90, false);
//        path.lineTo(pathModelInner.endPointF.x - innerHalfSquareWidth, wrapperYHalfRectF);
        circlePaint.setColor(firstColor);
        circlePaint.setAlpha(transParentValue);
        circlePaint.setStrokeWidth(paintWidth);
        canvas.drawPath(path, circlePaint);
        circlePaint.setColor(originalColor);
        canvas.restore();
    }

    private PathModel generalPathModelInnerBg(RectF rectF, float fractionArc){
        PathModel pathModel = generalPathModelRight(rectF, fractionArc, 0, -180);
        float yHalfRectF = rectF.top + rectF.height()/2;
        float halfSquareWidth = yHalfRectF - pathModel.startPointF.y;

        RectF smallStartRectF = new RectF(pathModel.startPointF.x, yHalfRectF - 2 * halfSquareWidth,
                pathModel.startPointF.x + 2 * halfSquareWidth, yHalfRectF);
        pathModel.setStartSmallRectF(smallStartRectF);
        Path startArcPath = new Path();
        startArcPath.moveTo(pathModel.startPointF.x + halfSquareWidth, yHalfRectF);
        startArcPath.arcTo(smallStartRectF, 90, 90);
        pathModel.setStartPathArc(startArcPath);

        RectF smallEndRectF = new RectF(pathModel.endPointF.x - 2 * halfSquareWidth , yHalfRectF - 2 * halfSquareWidth,
                pathModel.endPointF.x , yHalfRectF);
        pathModel.setEndSmallRectF(smallEndRectF);
        Path endArcPath = new Path();
        endArcPath.moveTo(pathModel.endPointF.x, pathModel.endPointF.y);
        endArcPath.arcTo(smallEndRectF, 0, 90);
        pathModel.setEndPathArc(endArcPath);
        return pathModel;
    }

    private PathModel generalPathModelRight(RectF rectF, float fractionArc, int startAngle, int sweepAngle) {
        Path pathOriginal = new Path();
        pathOriginal.moveTo(rectF.right, (rectF.top + rectF.bottom) / 2);
        pathOriginal.arcTo(rectF, startAngle, sweepAngle, false);
        PathMeasure pathMeasure = new PathMeasure(pathOriginal, false);
        float radius = rectF.width() / 2;
        float halfCircleLength = DecimalUtil.getDecimalFloat(DecimalUtil.THREE_LENGTH_DECIMAL,
                (float) (Math.PI * radius));
        float[] dstPathFirstPoint = new float[2];
        float[] dstPathEndPoint = new float[2];
        float firstPointLength = halfCircleLength * fractionArc;
        float endPointLength = pathMeasure.getLength() - firstPointLength;
        pathMeasure.getPosTan(firstPointLength, dstPathFirstPoint, null);
        pathMeasure.getPosTan(endPointLength, dstPathEndPoint, null);
        PointF startPointF = new PointF(dstPathFirstPoint[0], dstPathFirstPoint[1]);
        PointF endPointF = new PointF(dstPathEndPoint[0], dstPathEndPoint[1]);
        Path clipCirclePath = new Path();
        pathMeasure.getSegment(firstPointLength, endPointLength, clipCirclePath, true);
        PathModel pathModel = new PathModel(startPointF, endPointF, clipCirclePath);
        return pathModel;
    }

    private PathModel generalPathModelWrapperBg(RectF rectF, float fractionArc){
        PathModel pathModel = generalPathModelLeft(rectF, fractionArc, 180, 180);
        float yHalfRectF = rectF.top + rectF.height()/2;
        float halfSquareWidth = yHalfRectF - pathModel.startPointF.y;

        RectF smallStartRectF = new RectF(pathModel.startPointF.x, yHalfRectF - 2 * halfSquareWidth, pathModel.startPointF.x + 2 * halfSquareWidth, yHalfRectF);
        pathModel.setStartSmallRectF(smallStartRectF);
        Path startArcPath = new Path();
        startArcPath.moveTo(pathModel.startPointF.x + halfSquareWidth, yHalfRectF);
        startArcPath.arcTo(smallStartRectF, 90, 90);
        pathModel.setStartPathArc(startArcPath);

        RectF smallEndRectF = new RectF(pathModel.endPointF.x - 2 * halfSquareWidth, yHalfRectF - 2 * halfSquareWidth,
                pathModel.endPointF.x, yHalfRectF);
        pathModel.setEndSmallRectF(smallEndRectF);
        Path endArcPath = new Path();
        endArcPath.moveTo(pathModel.endPointF.x, pathModel.endPointF.y);
        endArcPath.arcTo(smallEndRectF, 0, 90);
        pathModel.setEndPathArc(endArcPath);

        pathModel.setHalfSquareWidth(halfSquareWidth);
        return pathModel;
    }

    private PathModel generalPathModelLeft(RectF rectF, float fractionArc, int startAngle, int sweepAngle) {
        Path pathOriginal = new Path();
        pathOriginal.moveTo(rectF.left, (rectF.top + rectF.bottom) / 2);
        pathOriginal.arcTo(rectF, startAngle, sweepAngle, false);
        PathMeasure pathMeasure = new PathMeasure(pathOriginal, false);
        float radius = rectF.width() / 2;
        float halfCircleLength = DecimalUtil.getDecimalFloat(DecimalUtil.THREE_LENGTH_DECIMAL, (float) (Math.PI * radius));
        float firstPointLength = halfCircleLength * fractionArc;
        float endPointLength = pathMeasure.getLength() - firstPointLength;
        float[] dstPathFirstPoint = new float[2];
        float[] dstPathEndPoint = new float[2];
        pathMeasure.getPosTan(firstPointLength, dstPathFirstPoint, null);
        pathMeasure.getPosTan(endPointLength, dstPathEndPoint, null);
        PointF startPointF = new PointF(dstPathFirstPoint[0], dstPathFirstPoint[1]);
        PointF endPointF = new PointF(dstPathEndPoint[0], dstPathEndPoint[1]);
        Path clipCirclePath = new Path();
        pathMeasure.getSegment(firstPointLength, endPointLength, clipCirclePath, true);
        PathModel pathModel = new PathModel(startPointF, endPointF, clipCirclePath);
        return pathModel;
    }

}
