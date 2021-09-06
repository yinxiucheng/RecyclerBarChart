package com.yxc.barchart.view;

import android.graphics.Path;
import android.graphics.PointF;

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
}
