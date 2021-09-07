package com.yxc.barchart.view;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;

public class QuadModel {

    public PointF startPointF;
    public PointF endPointF;
    public PointF ctrlPointF;
    public PointF centerPointF;

    Path quadPath;

    public Path createQuadPath(){
        quadPath = new Path();
        quadPath.moveTo(startPointF.x, startPointF.y);
        quadPath.quadTo(ctrlPointF.x, ctrlPointF.y, endPointF.x, endPointF.y);
        quadPath.lineTo(centerPointF.x, centerPointF.y);
        quadPath.close();
        return quadPath;
    }

    public PointF createCommonPoint(RectF rectF, float sweepAngel) {
        float radius = rectF.width() / 2;
        float halfCircleLength = (float) (Math.PI * radius);
        Path pathOriginal = new Path();
        pathOriginal.moveTo(rectF.left, (rectF.top + rectF.bottom) / 2);
        pathOriginal.arcTo(rectF, 180, 180, false);
        PathMeasure pathMeasure = new PathMeasure(pathOriginal, false);
        float[] points = new float[2];
        float pointLength = halfCircleLength * sweepAngel / 180.f;
        pathMeasure.getPosTan(pointLength, points, null);
        return new PointF(points[0], points[1]);
    }

    public PointF createEndPoint(RectF rectF, float sweepAngel) {
        float radius = rectF.width() / 2;
        float halfCircleLength = (float) (Math.PI * radius);
        Path pathOriginal = new Path();
        pathOriginal.moveTo(rectF.right, (rectF.top + rectF.bottom) / 2);
        pathOriginal.arcTo(rectF, 0, -180, false);
        PathMeasure pathMeasure = new PathMeasure(pathOriginal, false);
        float[] points = new float[2];
        float pointLength = halfCircleLength * sweepAngel / 180.f;
        pathMeasure.getPosTan(pointLength, points, null);
        return new PointF(points[0], points[1]);
    }

}
