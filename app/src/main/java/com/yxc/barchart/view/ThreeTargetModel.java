package com.yxc.barchart.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class ThreeTargetModel {
    public float reSize;
    public RectF rectF;
    public float itemWidth;
    public float spaceWidth;
    public float width;
    public float height;
    public float centerStartAngel = 180f;
    public float sweepAngel;
    public float wrapperFixAngel;
    public float innerFixAngel;

    public ThreeTargetModel(float reSize, RectF rectF, float itemWidth, float spaceWidth,
                            float wrapperFixAngel, float innerFixAngel,
                            float sweepAngel) {
        this.reSize = reSize;
        this.rectF = rectF;
        this.width = rectF.width();
        this.height = rectF.height();
        this.itemWidth = itemWidth;
        this.spaceWidth = spaceWidth;
        this.wrapperFixAngel = wrapperFixAngel;
        this.innerFixAngel = innerFixAngel;
        this.sweepAngel = sweepAngel;
    }

    private Path centerCircle;
    private Path wrapperCircle;
    private Path innerCircle;

    private Path wrapperStartPath;
    private Path wrapperEndPath;
    private Path innerStartPath;
    private Path innerEndPath;

    private RectF wrapperStartRectF;
    private RectF wrapperEndRectF;
    private RectF innerStartRectF;
    private RectF innerEndRectF;

    private void createComponents() {
        createWrapperCircle();
        createCenterCircle();
        createInnerCircle();
        createWrapperPath();
        createInnerPath();
    }

    public void drawComponents(Canvas canvas, Paint paint) {
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
        wrapperStartRectF = new RectF(0, 0, width, height);
        wrapperEndRectF = new RectF(spaceWidth + reSize, spaceWidth + reSize,
                width - spaceWidth - reSize, height - spaceWidth - reSize);
        float wrapperStartAngel = 180 + wrapperFixAngel;
        wrapperCircle = createCircle(wrapperStartRectF, wrapperEndRectF, wrapperStartAngel,
                sweepAngel - 2 * wrapperFixAngel, width / 2, height / 2);
    }

    private void createCenterCircle() {
        RectF rectFWrapper = new RectF(spaceWidth, spaceWidth, width - spaceWidth, height - spaceWidth);
        RectF rectFInner = new RectF(itemWidth - spaceWidth, itemWidth - spaceWidth, width - itemWidth + spaceWidth,
                height - itemWidth + spaceWidth);
        centerCircle = createCircle(rectFWrapper, rectFInner, centerStartAngel, sweepAngel, width / 2, height / 2);
    }

    private void createInnerCircle() {
        innerStartRectF = new RectF(itemWidth - spaceWidth - reSize, itemWidth - spaceWidth - reSize,
                width - itemWidth + spaceWidth + reSize, height - itemWidth + spaceWidth + reSize);
        innerEndRectF = new RectF(itemWidth, itemWidth, width - itemWidth, height - itemWidth);
        float innerStartAngel = 180 + innerFixAngel;
        innerCircle = createCircle(innerStartRectF, innerEndRectF, innerStartAngel,
                sweepAngel - 2 * innerFixAngel, width / 2, height / 2);
    }

    private Path createCircle(RectF rectFWrapper, RectF rectFInner, float startAngle, float sweepAngle, float centerX, float centerY) {
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
        innerStartPath = new Path();
        QuadModel startQuadModel = new QuadModel();
        startQuadModel.centerPointF = startQuadModel.createCommonPoint(innerStartRectF, innerFixAngel);
        startQuadModel.ctrlPointF = startQuadModel.createCommonPoint(innerEndRectF, 0);
        startQuadModel.startPointF = startQuadModel.createCommonPoint(innerEndRectF, innerFixAngel);
        startQuadModel.endPointF = startQuadModel.createCommonPoint(innerStartRectF, 0);
        innerStartPath = startQuadModel.createQuadPath();

        QuadModel endQuadModel = new QuadModel();
        endQuadModel.centerPointF = endQuadModel.createEndPoint(innerStartRectF, innerFixAngel);
        endQuadModel.ctrlPointF = endQuadModel.createCommonPoint(innerEndRectF, sweepAngel);
        endQuadModel.startPointF = endQuadModel.createCommonPoint(innerStartRectF, sweepAngel);
        endQuadModel.endPointF = endQuadModel.createEndPoint(innerEndRectF, innerFixAngel);
        innerEndPath = endQuadModel.createQuadPath();
    }

    private void createWrapperPath() {
        QuadModel startQuadModel = new QuadModel();
        startQuadModel.centerPointF = startQuadModel.createCommonPoint(wrapperEndRectF, wrapperFixAngel);
        startQuadModel.ctrlPointF = startQuadModel.createCommonPoint(wrapperStartRectF, 0);
        startQuadModel.startPointF = startQuadModel.createCommonPoint(wrapperEndRectF, 0);
        startQuadModel.endPointF = startQuadModel.createCommonPoint(wrapperStartRectF, wrapperFixAngel);
        wrapperStartPath = startQuadModel.createQuadPath();

        QuadModel endQuadModel = new QuadModel();
        endQuadModel.centerPointF = endQuadModel.createEndPoint(wrapperEndRectF, wrapperFixAngel);
        endQuadModel.ctrlPointF = endQuadModel.createCommonPoint(wrapperStartRectF, sweepAngel);
        endQuadModel.startPointF = endQuadModel.createEndPoint(wrapperStartRectF, wrapperFixAngel);
        endQuadModel.endPointF = endQuadModel.createCommonPoint(wrapperEndRectF, sweepAngel);
        wrapperEndPath = endQuadModel.createQuadPath();

        //  Matrix matrix = new Matrix();
//        matrix.setRotate(-90, (rightRectF.left + rightRectF.right)/2, (rightRectF.top + rightRectF.bottom)/2);
//        roundRightArc.transform(matrix);

//        canvas.drawArc(rightRectF, 0, 90, true, circlePaint);

//        Path roundRightRectF = CanvasUtil.createRectRoundPath(rightRectF, spaceWidth, RoundRectType.TYPE_RIGHT_BOTTOM);
//
//        canvas.drawPath(roundRightRectF, circlePaint);
    }

}
