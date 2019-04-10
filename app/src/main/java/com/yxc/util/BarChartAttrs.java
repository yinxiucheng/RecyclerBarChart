package com.yxc.util;

import android.content.Context;

import com.yxc.barchart.R;

/**
 * @author yxc
 * @date 2019/4/9
 *
 */
public class BarChartAttrs {

    public int barBorderColor;//边框颜色
    public int barChartEdgeColor;//边界 barChart 滑入时的过度颜色，这里以后需要用渐变动画来控制。
    public int barChartColor; //柱状图颜色

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
    public float ratioVelocity;

}
