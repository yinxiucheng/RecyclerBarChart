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

    public int barEntryTypeFirstColor;
    public int barEntryTypeSecondColor;
    public int barEntryTypeThirdColor;

    public float labelTxtPadding;

    public int lastVisiblePosition = -1;
    public int firstVisiblePosition = -1;

    public XAxis(BarChartAttrs attrs, int displayNumbers) {
        this.displayNumbers = displayNumbers;
        this.txtColor = attrs.xAxisTxtColor;
        this.txtSize = attrs.xAxisTxtSize;
        this.barEntryTypeFirstColor = attrs.xAxisFirstDividerColor;
        this.barEntryTypeSecondColor = attrs.xAxisSecondDividerColor;
        this.barEntryTypeThirdColor = attrs.xAxisThirdDividerColor;
        this.labelTxtPadding = attrs.xAxisLabelTxtPadding;
    }

}
