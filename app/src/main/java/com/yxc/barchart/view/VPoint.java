package com.yxc.barchart.view;

import android.graphics.PointF;

public class VPoint {
    public float x;
    public float y;
    public PointF top = new PointF();
    public PointF bottom = new PointF();

    public void setX(float x) {
        this.x = x;
        top.x = x;
        bottom.x = x;
    }

    public void adjustY(float offset) {
        top.y -= offset;
        bottom.y += offset;
    }

    public void adjustAllX(float offset) {
        this.x += offset;
        top.x += offset;
        bottom.x += offset;
    }

    public void adjustAllY(float offset) {
        this.y += offset;
        top.y += offset;
        bottom.y += offset;
    }

    public void adjustAllXY(float x, float y) {
        adjustAllX(x);
        adjustAllY(y);
    }
}