package com.yxc.barchart.view;

public class BezierCircleModel {

    public VPoint p2;
    public VPoint p4;
    public HPoint p1;
    public HPoint p3;

    public BezierCircleModel(HPoint p1, HPoint p3, VPoint p2, VPoint p4){
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.p4 = p4;
    }
}
