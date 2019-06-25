package com.yxc.chartlib.component;

import com.yxc.chartlib.attrs.BaseChartAttrs;
import com.yxc.chartlib.formatter.ValueFormatter;

/**
 * @author yxc
 * @date 2019/4/8
 */
public class XAxis<T extends BaseChartAttrs> extends AxisBase {

    public int displayNumbers;
    public int firstDividerColor;
    public int secondDividerColor;
    public int thirdDividerColor;

    public float labelTxtPadding;

    public XAxis(T attrs, int displayNumbers, ValueFormatter valueFormatter) {
        this(attrs, displayNumbers);
        setValueFormatter(valueFormatter);
    }

    public XAxis(T attrs, int displayNumbers) {
        this.displayNumbers = displayNumbers;
        setTextColor(attrs.xAxisTxtColor);
        setTextSize(attrs.xAxisTxtSize);
        this.firstDividerColor = attrs.xAxisFirstDividerColor;
        this.secondDividerColor = attrs.xAxisSecondDividerColor;
        this.thirdDividerColor = attrs.xAxisThirdDividerColor;
        this.labelTxtPadding = attrs.xAxisLabelTxtPadding;
    }
}
