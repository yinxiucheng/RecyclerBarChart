package com.yxc.barchart.component;

import com.yxc.util.BarChartAttrs;

/**
 * @author yxc
 * @date 2019/4/8
 */
public class XAxis {

    public int displayNumbers;
    public float txtSize;
    public int txtColor;

    public int firstDividerColor;
    public int secondDividerColor;
    public int thirdDividerColor;

    public float labelTxtPadding;

    public int lastVisiblePosition = -1;
    public int firstVisiblePosition = -1;

    public XAxis(BarChartAttrs attrs, int displayNumbers) {
        this.displayNumbers = displayNumbers;
        this.txtColor = attrs.xAxisTxtColor;
        this.txtSize = attrs.xAxisTxtSize;
        this.firstDividerColor = attrs.xAxisFirstDividerColor;
        this.secondDividerColor = attrs.xAxisSecondDividerColor;
        this.thirdDividerColor = attrs.xAxisThirdDividerColor;
        this.labelTxtPadding = attrs.xAxisLabelTxtPadding;
    }

}
