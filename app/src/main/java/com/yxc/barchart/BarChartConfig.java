package com.yxc.barchart;

import android.content.Context;

/**
 * @author yxc
 * @date 2019/4/9
 */
public class BarChartConfig {

    public int barBorderColor;
    public int barBorderEndgColor;
    public float barBorderWidth;
    public int barChartColor;
    public int contentPaddingBottom = DisplayUtil.dip2px(15);//底部的 X轴刻度所占的高度
    public int maxYAxisPaddingTop = DisplayUtil.dip2px(10);//顶部显示的预留空间

    private Context mContext;

    public BarChartConfig(Context context){
        this.mContext = context;
        barChartColor = ColorUtil.getResourcesColor(mContext, R.color.pink);
        barBorderColor = ColorUtil.getResourcesColor(mContext, R.color.black_80_transparent);
        barBorderWidth = DisplayUtil.dip2px(0.5f);
        barBorderEndgColor = ColorUtil.getResourcesColor(mContext, R.color.black_20_transparent);;
    }
}
