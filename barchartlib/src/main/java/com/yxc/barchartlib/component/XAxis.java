package com.yxc.barchartlib.component;

import com.yxc.barchartlib.util.BarChartAttrs;

/**
 * @author yxc
 * @date 2019/4/8
 */
public class XAxis extends AxisBase {

    public int displayNumbers;
    public int firstDividerColor;
    public int secondDividerColor;
    public int thirdDividerColor;

    public float labelTxtPadding;

    public int lastVisiblePosition = -1;
    public int firstVisiblePosition = -1;

    public XAxis(BarChartAttrs attrs, int displayNumbers) {

        this.displayNumbers = displayNumbers;
        setTextColor(attrs.xAxisTxtColor);
        setTextSize(attrs.xAxisTxtSize);

        this.firstDividerColor = attrs.xAxisFirstDividerColor;
        this.secondDividerColor = attrs.xAxisSecondDividerColor;
        this.thirdDividerColor = attrs.xAxisThirdDividerColor;
        this.labelTxtPadding = attrs.xAxisLabelTxtPadding;
    }

    @Override
    public int getTextColor() {
        return textColor;
    }

    @Override
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
