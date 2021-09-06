package com.yxc.barchart.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.yxc.commonlib.util.DisplayUtil;

public class ThreeTargetModel{
    public float reSize;
    public RectF rectF;
    public float itemWidth;
    public float spaceWidth;

    public float width;
    public float height;

    public float wrapperStartAngel;
    public float wrapperSweepAngel;
    public float centerStartAngel = 180f;
    public float centerSweepAngel;
    public float innerStartAngel;
    public float innerSweepAngel;

    public ThreeTargetModel(float reSize, RectF rectF, float itemWidth, float spaceWidth,
                            float wrapperStartAngel, float wrapperSweepAngel,
                            float centerSweepAngel,
                            float innerStartAngel, float innerSweepAngel){
        this.reSize = reSize;
        this.rectF = rectF;
        this.width = rectF.width();
        this.height = rectF.height();
        this.itemWidth = itemWidth;
        this.spaceWidth = spaceWidth;
        this.wrapperStartAngel = wrapperStartAngel;
        this.wrapperSweepAngel = wrapperSweepAngel;
        this.centerSweepAngel = centerSweepAngel;
        this.innerStartAngel = innerStartAngel;
        this.innerSweepAngel = innerSweepAngel;
    }

    public  Path centerCircle;
    public  Path wrapperCircle;
    public  Path innerCircle;
    public  Path wrapperStartPath;
    public  Path wrapperEndPath;
    public  Path innerStartPath;
    public  Path innerEndPath;

    private void createComponents(){
        createWrapperCircle();
        createCenterCircle();
        createInnerCircle();
        createWrapperPath();
        createInnerPath();
    }

    public void drawComponents(Canvas canvas, Paint paint){
        createComponents();
        canvas.drawPath(wrapperCircle, paint);
        canvas.drawPath(centerCircle, paint);
        canvas.drawPath(innerCircle, paint);
        canvas.drawPath(wrapperStartPath, paint);
        canvas.drawPath(wrapperEndPath, paint);
        canvas.drawPath(innerStartPath, paint);
        canvas.drawPath(innerEndPath, paint);
    }

    private void createWrapperCircle() {
        RectF rectFWrapper = new RectF(0, 0, width, height);
        RectF rectFInner = new RectF(spaceWidth + reSize, spaceWidth + reSize,
                width - spaceWidth - reSize, height - spaceWidth - reSize);
        wrapperCircle = createCircle(rectFWrapper, rectFInner, wrapperStartAngel, wrapperSweepAngel, width/2, height/2);
    }

    private void createCenterCircle() {
        RectF rectFWrapper = new RectF(spaceWidth, spaceWidth, width - spaceWidth, height - spaceWidth);
        RectF rectFInner = new RectF(itemWidth - spaceWidth, itemWidth - spaceWidth, width - itemWidth + spaceWidth,
                height - itemWidth + spaceWidth);
        centerCircle = createCircle(rectFWrapper, rectFInner, centerStartAngel, centerSweepAngel, width/2, height/2);
    }

    private void createInnerCircle() {
        RectF rectFWrapper = new RectF(itemWidth - spaceWidth - reSize, itemWidth - spaceWidth - reSize,
                width - itemWidth + spaceWidth + reSize, height - itemWidth + spaceWidth + reSize);
        RectF rectFInner = new RectF(itemWidth, itemWidth, width - itemWidth, height - itemWidth);
        innerCircle = createCircle(rectFWrapper, rectFInner, innerStartAngel, innerSweepAngel, width/2, height/2);
    }

    private Path createCircle(RectF rectFWrapper, RectF rectFInner, float startAngle, float sweepAngle, float centerX, float centerY){
        Path path = new Path();
        path.addArc(rectFWrapper, startAngle, sweepAngle);
        path.lineTo(centerX, centerY);
        path.close();

        Path clipPath = new Path();
        clipPath.addArc(rectFInner, startAngle, sweepAngle);
        clipPath.lineTo(centerX, centerY);
        clipPath.close();
        path.op(clipPath, Path.Op.DIFFERENCE);
        return path;
    }

    private void createInnerPath() {
        RectF leftRectF = new RectF(itemWidth - 2 * spaceWidth - reSize, height / 2 - 2 * spaceWidth - reSize,
                itemWidth + DisplayUtil.dip2px(0.13f), height / 2);
        innerStartPath = new Path();
        innerStartPath.addArc(leftRectF, 0, 90);
        innerStartPath.lineTo((leftRectF.left + leftRectF.right) / 2, (leftRectF.top + leftRectF.bottom) / 2);
        innerStartPath.close();

        RectF rightRectF = new RectF(width - itemWidth, height / 2 - 2 * spaceWidth,
                width - itemWidth + 2 * spaceWidth + reSize, height / 2);
        innerEndPath = new Path();
        innerEndPath.addArc(rightRectF, 90, 90);
        innerEndPath.lineTo((rightRectF.left + rightRectF.right) / 2, (rightRectF.top + rightRectF.bottom) / 2);
        innerEndPath.close();
    }

    private void createWrapperPath() {
        RectF leftRectF = new RectF(0, height / 2 - 2 * spaceWidth - reSize, 2 * spaceWidth + reSize, height / 2);
        wrapperStartPath = new Path();
        wrapperStartPath.addArc(leftRectF, 90, 90);
        wrapperStartPath.lineTo((leftRectF.left + leftRectF.right) / 2, (leftRectF.top + leftRectF.bottom) / 2);
        wrapperStartPath.close();

        RectF rightRectF = new RectF(width - 2 * spaceWidth - reSize, height / 2 - 2 * spaceWidth, width, height / 2);
        wrapperEndPath = new Path();
        wrapperEndPath.addArc(rightRectF, 0, 90);
        wrapperEndPath.lineTo((rightRectF.left + rightRectF.right) / 2, (rightRectF.top + rightRectF.bottom) / 2);
        wrapperEndPath.close();

        //  Matrix matrix = new Matrix();
//        matrix.setRotate(-90, (rightRectF.left + rightRectF.right)/2, (rightRectF.top + rightRectF.bottom)/2);
//        roundRightArc.transform(matrix);

//        canvas.drawArc(rightRectF, 0, 90, true, circlePaint);

//        Path roundRightRectF = CanvasUtil.createRectRoundPath(rightRectF, spaceWidth, RoundRectType.TYPE_RIGHT_BOTTOM);
//
//        canvas.drawPath(roundRightRectF, circlePaint);
    }

}
