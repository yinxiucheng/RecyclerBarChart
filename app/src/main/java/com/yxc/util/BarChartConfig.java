package com.yxc.util;

import android.content.Context;

import com.yxc.barchart.R;

/**
 * @author yxc
 * @date 2019/4/9
 *
 */
public class BarChartConfig {

    public int barBorderColor;//边框颜色
    public int barChartEdgeColor;//边界 barChart 滑入时的过度颜色，这里以后需要用渐变动画来控制。
    public float barBorderWidth;//边框的宽度
    public int barChartColor; //
    public int contentPaddingBottom;//底部的 X轴刻度所占的高度
    public int maxYAxisPaddingTop;//顶部显示的预留空间
    public float barSpace;//barchart item中 space 占比，能够控制barchart的宽度
    public int recyclerPaddingLeft;//原始RecyclerView的 paddingLeft 值
    public int recyclerPaddingRight;//原始RecyclerView的 paddingRight 值

    public boolean enableCharValueDisplay = true;//控制是否显示顶部的 value值
    public boolean enableYAxisZero = true;// 控制是否显示 Y轴中的 0 刻度线
    public boolean enableXAxisGridLine = true;// 控制是否显示 X轴对应的 纵轴网格线
    public boolean enableRightYAxisLabel = true;//控制是否显示 Y轴右刻度
    public boolean enableLeftYAxisLabel = true;// 控制是否显示 Y轴左刻度
    public boolean enableBarBorder = true;//控制是否显示边框

    private Context mContext;

    public BarChartConfig(Context context) {
        this.mContext = context;
        barChartColor = ColorUtil.getResourcesColor(mContext, R.color.pink);
        barBorderColor = ColorUtil.getResourcesColor(mContext, R.color.black_80_transparent);
        barBorderWidth = DisplayUtil.dip2px(0.5f);
        barChartEdgeColor = ColorUtil.getResourcesColor(mContext, R.color.black_20_transparent);
        barSpace = 1.0f / 3;
        contentPaddingBottom = DisplayUtil.dip2px(15);
        maxYAxisPaddingTop = DisplayUtil.dip2px(10);
        recyclerPaddingLeft = DisplayUtil.dip2px(2);
        recyclerPaddingRight = DisplayUtil.dip2px(3);
    }


}
