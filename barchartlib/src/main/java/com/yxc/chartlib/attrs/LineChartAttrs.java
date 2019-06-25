package com.yxc.chartlib.attrs;

/**
 * @author yxc
 * @date 2019-06-25
 */
public class LineChartAttrs extends BaseChartAttrs {

    public float linePointRadius;//point的
    public float linePointSelectRadius;//选中的 SelectRadius
    public float linePointSelectStrokeWidth;//选中的点外层圈的 paint的strokeWidth。
    public int lineColor;//线的颜色
    public int lineHighLightType;//分从底部开始画，从y值处开始画。

    public int xAxisThirdDividerColor;//x轴对应纵轴 第三种网格线颜色
}
