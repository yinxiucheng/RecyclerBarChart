package com.yxc.barchart.view;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class PathModel {
    PointF leftPointF;
    PointF endPointF;
    Path pathArc;
    RectF leftSmallRectF;
    RectF rightSmallRectF;

    public PathModel(PointF startPointF, PointF endPointF, Path pathArc) {
        this.leftPointF = startPointF;
        this.endPointF = endPointF;
        this.pathArc = pathArc;
    }

    public PathModel(PointF startPointF, PointF endPointF, Path pathArc,
                     RectF leftSmallRectF, RectF rightSmallRectF) {
        this.leftPointF = startPointF;
        this.endPointF = endPointF;
        this.pathArc = pathArc;
        this.leftSmallRectF = leftSmallRectF;
        this.rightSmallRectF = rightSmallRectF;
    }

    public void setLeftSmallRectF(RectF leftSmallRectF) {
        this.leftSmallRectF = leftSmallRectF;
    }

    public void setRightSmallRectF(RectF rightSmallRectF) {
        this.rightSmallRectF = rightSmallRectF;
    }
}
