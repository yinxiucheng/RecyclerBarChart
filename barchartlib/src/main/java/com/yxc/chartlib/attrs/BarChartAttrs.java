package com.yxc.chartlib.attrs;

/**
 * @author yxc
 * @since 2019/4/9
 *
 */
public class BarChartAttrs extends BaseChartAttrs{
    public static final int HIGH_LIGHT_LINE_BOTTOM = 1;//高亮线从bottom处开始画
    public static final int HIGH_LIGHT_LINE_Y = 2;//高亮线从y值处开始画

    public int barBorderColor;//边框颜色
    public int barChartEdgeColor;//边界 barChart 滑入时的过度颜色，这里以后需要用渐变动画来控制。
    public int barChartColor; //柱状图颜色

    public int barChartValueTxtColor;//柱状图顶部value文字的颜色
    public float barChartValueTxtSize;//柱状图顶部value文字的大小
    public float barChartValuePaddingBottom;//柱状图顶部value文字据柱状图的padding
    public float barChartValuePaddingLeft;//柱状图顶部value文字不居中时据左的padding
    public float barChartValueTxtMaskSize;//value 顶部popup的文字大小
    public int barChartValueTxtMaskColor;//value 顶部popup的文字颜色
    public float barChartRoundRectRadiusRatio;//圆角矩形半径占宽度的比率

    public float barSpace;//barChart item中 space 占比，能够控制barchart的宽度
    public float barBorderWidth;//边框的宽度
    public float recyclerPaddingLeft;//原始RecyclerView的 paddingLeft 值
    public float recyclerPaddingRight;//原始RecyclerView的 paddingRight 值

    public boolean enableCharValueDisplay;//控制是否显示顶部的 value值
    public boolean enableScrollToScale;//控制是否回溯到分界线处
    public boolean enableValueMark;//控制柱状图顶部markView的显示
    public boolean averageDisplay;//画柱子时剩余的宽度分给部分柱子

    public int displayNumbers;//一屏显示多少个 barChart
    public float bezierIntensity;//bezier curve intensity
    public boolean enableBezierLineFill;//
    public int bezierFillColor;
    public int bezierFillAlpha;
    public int bezierLinePaintColor;

    public float linePointRadius;//point的
    public float linePointSelectRadius;//选中的 SelectRadius
    public float linePointSelectStrokeWidth;//选中的点外层圈的 paint的strokeWidth。
    public int lineColor;//线的颜色

    public int lineShaderBeginColor;//fill底部的开始的深的颜色
    public int lineShaderEndColor;//fill底部的结束的浅的颜色
    public boolean enableLineFill;//底部fill
    public int fillAlpha;
    public float bezierLinePaintStrokeWidth;

    public int highLightLineType;//分从底部开始画，从y值处开始画。

    public int rateChartLightColor;
    public int rateChartDarkColor;

    public boolean isDisplay;
}
