package com.yxc.barchart;

import android.graphics.Color;

/**
 * @author yxc
 * @date 2019/4/8
 */
public class YAxis {

    public static final int DEFAULT_MAX_YAXIS_LABEL = 30000;

    public static final int DEFAULT_LABEL_SIZE = 4;

    public int maxLabel = DEFAULT_MAX_YAXIS_LABEL;

    public int labelSize = DEFAULT_LABEL_SIZE;

    public int labelTxtSize;

    public int labelTxtColor;

    public YAxis() {
        this(DEFAULT_MAX_YAXIS_LABEL, DEFAULT_LABEL_SIZE);
    }

    public YAxis(int maxLabel, int labelSize) {
        this.maxLabel = maxLabel;
        this.labelSize = labelSize;
        this.labelTxtSize = DisplayUtil.dip2px(11);
        this.labelTxtColor = Color.GRAY;
    }

    public void setLabelTxtSize(int labelTxtSize) {
        this.labelTxtSize = labelTxtSize;
    }

    public void setLabelTxtColor(int labelTxtColor) {
        this.labelTxtColor = labelTxtColor;
    }


    //获取Y轴刻度值
    public static YAxis getYAxis(float max) {
        YAxis axis = new YAxis();
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
