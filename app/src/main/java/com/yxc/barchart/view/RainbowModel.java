package com.yxc.barchart.view;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;

import com.yxc.chartlib.util.DecimalUtil;

public class RainbowModel {
    PointF point1;
    PointF point2;
    PointF point3;
    PointF point4;
    PointF point5;
    PointF point6;
    PointF point7;
    PointF point8;

    PointF pointCtr1;
    PointF pointCtr2;
    PointF pointCtr3;
    PointF pointCtr4;

    Path pathArc1;
    Path pathArc2;
    Path pathArc3;
    Path pathArc4;
    Path pathArc5;
    Path pathArc6;

    PathMeasure pathMeasureWrapper;
    PathMeasure pathMeasureInner;

    public RainbowModel generalCommonModelBg(RainbowModel rainbowModel, RectF rectFWrapper, RectF rectInner,
                                             float fractionArcWrapper, float fractionInner){
        generalCommonModel(rectFWrapper, fractionArcWrapper, 180, 180, true);
        generalCommonModel(rectInner, fractionInner, 0, -180, false);
        generalPathModelWrapperBg(rectFWrapper);
        generalPathModelInnerBg(rectInner);
        return rainbowModel;
    }

    public RainbowModel generalPathModelInnerBg(RectF rectF) {
        float yHalfRectF = rectF.top + rectF.height() / 2;
        float halfSquareWidth = yHalfRectF - point7.y;
        Path path = new Path();
        path.moveTo(pointCtr3.x, pointCtr3.y);
        path.lineTo(pointCtr2.x, pointCtr2.y);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float[] point5F = new float[2];
        pathMeasure.getPosTan(halfSquareWidth, point5F, null);
        point5 = new PointF(point5F[0], point5F[1]);

        pathArc4 = new Path();
        pathArc4.moveTo(point5.x, point5.y);
        pathArc4.quadTo(pointCtr3.x, pointCtr3.y, point6.x, point6.y);
        point8 = new PointF(point7.x - halfSquareWidth, yHalfRectF);
        pathArc6 = new Path();
        pathArc6.moveTo(point7.x, point7.y);
        pathArc6.quadTo(pointCtr4.x, pointCtr4.y, point8.x, point8.y);
        return this;
    }

    public RainbowModel generalPathModelWrapperBg(RectF rectF) {
        float yHalfRectF = rectF.top + rectF.height() / 2;
        float halfSquareWidth = yHalfRectF - point2.y;
        point1 = new PointF(rectF.left + halfSquareWidth, yHalfRectF);
        pathArc1 = new Path();
        pathArc1.moveTo(point1.x, point1.y);
        pathArc1.quadTo(pointCtr1.x, pointCtr1.y, point2.x, point2.y);

        Path path = new Path();
        path.moveTo(pointCtr2.x, pointCtr2.y);
        path.lineTo(pointCtr3.x, pointCtr3.y);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float[] point4F = new float[2];
        pathMeasure.getPosTan(halfSquareWidth, point4F, null);

        point4 = new PointF(point4F[0], point4F[1]);
        pathArc3 = new Path();
        pathArc3.moveTo(point3.x, point3.y);
        pathArc3.quadTo(pointCtr2.x, pointCtr2.y, point4.x, point4.y);
        return this;
    }



    private RainbowModel generalCommonModel(RectF rectF, float fractionArc, int startAngle, int sweepAngle, boolean isFromLeft) {
        Path pathOriginal = new Path();
        float radius = rectF.height() / 2;
        if (isFromLeft) {
            pathOriginal.moveTo(rectF.left, (rectF.top + rectF.bottom) / 2);
        } else {
            pathOriginal.moveTo(rectF.right, (rectF.top + rectF.bottom) / 2);
        }
        pathOriginal.arcTo(rectF, startAngle, sweepAngle, false);
        PathMeasure pathMeasure = new PathMeasure(pathOriginal, false);

        float halfCircleLength = DecimalUtil.getDecimalFloat(DecimalUtil.THREE_LENGTH_DECIMAL, (float) (Math.PI * radius));
        float[] dstPathFirstPoint = new float[2];
        float[] dstPathEndPoint = new float[2];
        float[] dstCtrlPoint1 = new float[2];
        float[] dstCtrlPoint2 = new float[2];

        float firstPointLength = halfCircleLength * fractionArc;
        pathMeasure.getPosTan(firstPointLength, dstPathFirstPoint, null);
        float endPointLength = pathMeasure.getLength() - firstPointLength;
        pathMeasure.getPosTan(endPointLength, dstPathEndPoint, null);

        float dstCtrlLength1 = 0;
        pathMeasure.getPosTan(dstCtrlLength1, dstCtrlPoint1, null);
        float dstCtrlLength2 = halfCircleLength * Math.abs(sweepAngle)/180.0f;
        pathMeasure.getPosTan(dstCtrlLength2, dstCtrlPoint2, null);

        PointF startPointF = new PointF(dstPathFirstPoint[0], dstPathFirstPoint[1]);
        PointF endPointF = new PointF(dstPathEndPoint[0], dstPathEndPoint[1]);
        PointF pointCtrStart = new PointF(dstCtrlPoint1[0], dstCtrlPoint1[1]);
        PointF pointCtrEnd = new PointF(dstCtrlPoint2[0], dstCtrlPoint2[1]);

        Path clipCirclePath = new Path();
        pathMeasure.getSegment(firstPointLength, endPointLength, clipCirclePath, true);
        if (isFromLeft) {
            point2 = startPointF;
            point3 = endPointF;
            pathArc2 = clipCirclePath;
            pointCtr1 = pointCtrStart;
            pointCtr2 = pointCtrEnd;
            pathMeasureWrapper = pathMeasure;
        } else {
            point6 = startPointF;
            point7 = endPointF;
            pathArc5 = clipCirclePath;
            pointCtr3 = pointCtrStart;
            pointCtr4 = pointCtrEnd;
            pathMeasureInner = pathMeasure;
        }
        return this;
    }

}
