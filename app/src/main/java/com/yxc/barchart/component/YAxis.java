package com.yxc.barchart.component;

import com.yxc.util.BarChartAttrs;

/**
 * @author yxc
 * @date 2019/4/8
 */
public class YAxis {

    BarChartAttrs attrs;

    public int maxLabel;//y轴刻度默认的最大刻度
    public int labelSize;
    public float labelTxtSize;
    public float labelPaddingLeftRight;
    public float labelCenterPadding;//刻度 字跟刻度线的位置对齐的调整
    public int lineColor;

    public YAxis(BarChartAttrs barChartAttrs) {
        this.attrs = barChartAttrs;
        this.maxLabel = attrs.yAxisLabelMaxScale;
        this.labelSize = attrs.yAxisLabelSize;
        this.labelTxtSize = attrs.yAxisLabelTxtSize;
        this.lineColor = attrs.yAxisLineColor;
        this.labelPaddingLeftRight = attrs.yAxisLabelPaddingLeftRight;
        this.labelCenterPadding = attrs.yAxisLabelCenterPadding;
    }

    //获取Y轴刻度值
    public static YAxis getYAxis(BarChartAttrs attrs, float max) {
        YAxis axis = new YAxis(attrs);
        if (max > 50000) {
            axis.maxLabel = 80000;
            axis.labelSize = 5;
        } else if (max > 30000) {
            axis.maxLabel = 50000;
            axis.labelSize = 5;
        } else if (max > 25000) {
            axis.maxLabel = 30000;
            axis.labelSize = 5;
        } else if (max > 20000) {
            axis.maxLabel = 25000;
            axis.labelSize = 4;
        } else if (max > 15000) {
            axis.maxLabel = 20000;
            axis.labelSize = 4;
        } else if (max > 10000) {
            axis.maxLabel = 15000;
            axis.labelSize = 4;
        } else if (max > 8000) {
            axis.maxLabel = 10000;
            axis.labelSize = 4;
        } else if (max > 6000) {
            axis.maxLabel = 8000;
            axis.labelSize = 4;
        } else if (max > 4000) {
            axis.maxLabel = 6000;
            axis.labelSize = 4;
        } else if (max > 3000) {
            axis.maxLabel = 5000;
            axis.labelSize = 4;
        } else if (max > 2000) {
            axis.maxLabel = 3000;
            axis.labelSize = 4;
        } else if (max > 1500) {
            axis.maxLabel = 2000;
            axis.labelSize = 4;
        } else if (max > 1000) {
            axis.maxLabel = 1500;
            axis.labelSize = 4;
        } else if (max > 500) {
            axis.maxLabel = 800;
            axis.labelSize = 4;
        } else if (max > 300) {
            axis.maxLabel = 500;
            axis.labelSize = 4;
        } else if (max > 200) {
            axis.maxLabel = 300;
            axis.labelSize = 4;
        } else {
            axis.maxLabel = 200;
            axis.labelSize = 4;
        }
        return axis;
    }
}
