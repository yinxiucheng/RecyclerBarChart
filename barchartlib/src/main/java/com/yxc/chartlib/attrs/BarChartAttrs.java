package com.yxc.chartlib.attrs;

/**
 * @author yxc
 * @since 2019/4/9
 *
 */
public class BarChartAttrs extends BaseChartAttrs{

    public static final int HIGH_LIGHT_LINE_BOTTOM = 1;//高亮线从bottom处开始画
    public static final int HIGH_LIGHT_LINE_Y = 2;//高亮线从y值处开始画

    public int barChartEdgeColor;//边界 barChart 滑入时的过度颜色，这里以后需要用渐变动画来控制。
    public int barChartColor; //柱状图颜色

    public int barChartValueTxtColor;//柱状图顶部value文字的颜色
    public float barChartValueTxtSize;//柱状图顶部value文字的大小
    public float barChartValuePaddingBottom;//柱状图顶部value文字据柱状图的padding
    public float barChartValuePaddingLeft;//柱状图顶部value文字不居中时据左的padding
    public float barChartValueTxtMaskSize;//value 顶部popup的文字大小
    public int barChartValueTxtMaskColor;//value 顶部popup的文字颜色
    public float barChartRoundRectRadiusRatio;//圆角矩形半径占宽度的比率


    public float bezierIntensity;//bezier curve intensity
    public boolean enableBezierLineFill;//
    public int bezierFillColor;
    public int bezierFillAlpha;
    public int bezierLinePaintColor;
    public float bezierLinePaintStrokeWidth;


}
