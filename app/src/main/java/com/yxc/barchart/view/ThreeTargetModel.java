package com.yxc.barchart.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.yxc.barchart.R;
import com.yxc.commonlib.util.ColorUtil;

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

    public static ThreeTargetModel createTargetModel(int type, RectF rectF, float itemWidth, float spaceWidth, float sweepAngel) {
        float wrapperFixAngel, innerFixAngel;
        if (type == ThreeTargetConstant.TARGET_FIRST_TYPE) {
            wrapperFixAngel = ThreeTargetConstant.FIRST_WRAPPER_FIX_ANGLE;
            innerFixAngel = ThreeTargetConstant.FIRST_INNER_FIX_ANGLE;
        } else if (type == ThreeTargetConstant.TARGET_SECOND_TYPE) {
            wrapperFixAngel = ThreeTargetConstant.SECOND_WRAPPER_FIX_ANGLE;
            innerFixAngel = ThreeTargetConstant.SECOND_INNER_FIX_ANGLE;
        } else {
            wrapperFixAngel = ThreeTargetConstant.THIRD_WRAPPER_FIX_ANGLE;
            innerFixAngel = ThreeTargetConstant.THIRD_INNER_FIX_ANGLE;
        }
        return new ThreeTargetModel(rectF, itemWidth, spaceWidth, wrapperFixAngel, innerFixAngel, sweepAngel);
    }

    public ThreeTargetModel(RectF rectF, float itemWidth, float spaceWidth,
                            float wrapperFixAngel, float innerFixAngel,
                            float sweepAngel) {
        this.reSize = ThreeTargetConstant.RESIZE;
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
        endQuadModel.centerPointF = endQuadModel.createEndPoint(innerStartRectF, 180 - sweepAngel + innerFixAngel);
        endQuadModel.ctrlPointF = endQuadModel.createCommonPoint(innerEndRectF, sweepAngel);
        endQuadModel.startPointF = endQuadModel.createCommonPoint(innerStartRectF, sweepAngel);
        endQuadModel.endPointF = endQuadModel.createEndPoint(innerEndRectF, 180 - sweepAngel + innerFixAngel);
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
        endQuadModel.centerPointF = endQuadModel.createEndPoint(wrapperEndRectF, 180 - sweepAngel + wrapperFixAngel);
        endQuadModel.ctrlPointF = endQuadModel.createCommonPoint(wrapperStartRectF, sweepAngel);
        endQuadModel.startPointF = endQuadModel.createEndPoint(wrapperStartRectF, 180 - sweepAngel + wrapperFixAngel);
        endQuadModel.endPointF = endQuadModel.createCommonPoint(wrapperEndRectF, sweepAngel);
        wrapperEndPath = endQuadModel.createQuadPath();
    }

    public int getColor(Context context, int type) {
        if (type == ThreeTargetConstant.TARGET_THIRD_TYPE) {
            return ColorUtil.getResourcesColor(context, R.color.rainbow_color3);
        } else if (type == ThreeTargetConstant.TARGET_SECOND_TYPE) {
            return ColorUtil.getResourcesColor(context, R.color.rainbow_color2);
        }
        return ColorUtil.getResourcesColor(context, R.color.rainbow_color1);
    }

}
