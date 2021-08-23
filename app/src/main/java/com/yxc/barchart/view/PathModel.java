package com.yxc.barchart.view;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

public class PathModel {
    PointF startPointF;
    PointF endPointF;
    Path pathArc;
    Path startPathArc;
    Path endPathArc;
    RectF startSmallRectF;
    RectF endSmallRectF;
    float halfSquareWidth;

    public PathModel(PointF startPointF, PointF endPointF, Path pathArc) {
        this.startPointF = startPointF;
        this.endPointF = endPointF;
        this.pathArc = pathArc;
    }

    public PathModel(PointF startPointF, PointF endPointF, Path pathArc,
                     RectF leftSmallRectF, RectF rightSmallRectF) {
        this.startPointF = startPointF;
        this.endPointF = endPointF;
        this.pathArc = pathArc;
        this.startSmallRectF = leftSmallRectF;
        this.endSmallRectF = rightSmallRectF;
    }

    public void setEndPathArc(Path endPathArc) {
        this.endPathArc = endPathArc;
    }

    public void setStartPathArc(Path startPathArc) {
        this.startPathArc = startPathArc;
    }

    public void setStartSmallRectF(RectF startSmallRectF) {
        this.startSmallRectF = startSmallRectF;
    }

    public void setEndSmallRectF(RectF endSmallRectF) {
        this.endSmallRectF = endSmallRectF;
    }

    public void setHalfSquareWidth(float halfSquareWidth) {
        this.halfSquareWidth = halfSquareWidth;
    }
}
