package com.yxc.chartlib.attrs;

/**
 * @author yxc
 * @since 2019/4/9
 *
 */
public class BarChartAttrs extends BaseChartAttrs{

    public float barChartRoundRectRadiusRatio;//圆角矩形半径占宽度的比率
    public float barChartValuePaddingBottom;//柱状图顶部value文字据柱状图的padding
    public int barChartValueTxtColor;//柱状图顶部value文字的颜色
    public float barChartValueTxtSize;//柱状图顶部value文字的大小
    public float barChartValuePaddingLeft;//柱状图顶部value文字不居中时据左的padding
    public float barChartValueTxtMaskSize;//value 顶部popup的文字大小
    public int barChartValueTxtMaskColor;//value 顶部popup的文字颜色
    public boolean averageDisplay;//画柱子时剩余的宽度分给部分柱子

    public int rateChartLightColor;
    public int rateChartDarkColor;

}
