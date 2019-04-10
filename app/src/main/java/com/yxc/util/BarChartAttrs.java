package com.yxc.util;

import android.content.Context;

/**
 * @author yxc
 * @date 2019/4/9
 *
 */
public class BarChartAttrs {

    public int barBorderColor;//边框颜色
    public int barChartEdgeColor;//边界 barChart 滑入时的过度颜色，这里以后需要用渐变动画来控制。
    public int barChartColor; //柱状图颜色
    public int barChartValueTxtColor;//柱状图顶部value文字的颜色
    public float barChartValueTxtSize;//柱状图顶部value文字的大小
    public float barChartValuePaddingBottom;//柱状图顶部value文字据柱状图的padding
    public float barChartValuePaddingLeft;//柱状图顶部value文字不居中时据左的padding

    public float barBorderWidth;//边框的宽度
    public float contentPaddingBottom;//底部的 X轴刻度所占的高度
    public float maxYAxisPaddingTop;//顶部显示的预留空间
    public float recyclerPaddingLeft;//原始RecyclerView的 paddingLeft 值
    public float recyclerPaddingRight;//原始RecyclerView的 paddingRight 值

    public boolean enableCharValueDisplay;//控制是否显示顶部的 value值
    public boolean enableYAxisZero;// 控制是否显示 Y轴中的 0 刻度线
    public boolean enableXAxisGridLine;// 控制是否显示 X轴对应的 纵轴网格线
    public boolean enableRightYAxisLabel;//控制是否显示 Y轴右刻度
    public boolean enableLeftYAxisLabel;// 控制是否显示 Y轴左刻度
    public boolean enableBarBorder;//控制是否显示边框
    public boolean enableScrollToScale;

    public float barSpace;//barchart item中 space 占比，能够控制barchart的宽度
    public float ratioVelocity;//recyclerView 惯性滑动的 加速度 比率。

    public int yAxisLabelMaxScale;//y轴刻度默认的最大刻度
    public float yAxisLabelTxtSize;//y轴刻度字体大小
    public int yAxisLabelTxtColor;//y轴字体颜色
    public int yAxisLabelSize;//y轴刻度的格数
    public int yAxisLineColor;//y轴对应的网格线的颜色
    public float yAxisLabelPaddingLeftRight;//刻度字跟边框的间距
    public float yAxisLabelCenterPadding;//刻度 字跟刻度线的位置对齐的调整

    public float xAxisTxtSize;//x轴刻度字体大小
    public int xAxisTxtColor;//x轴刻度字体颜色
    public int xAxisFirstDividerColor;//x轴对应纵轴 第一种网格线颜色
    public int xAxisSecondDividerColor;//x轴对应纵轴 第二种网格线颜色
    public int xAxisThirdDividerColor;//x轴对应纵轴 第三种网格线颜色
    public float xAxisLabelTxtPadding;//x轴刻度跟 坐标线之间的间距（不居中的情况下）

    public int displayNumbers;//一屏显示多少个 barChart


}
