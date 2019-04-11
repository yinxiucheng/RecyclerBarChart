package com.yxc.barchartlib.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import com.yxc.barchartlib.R;

public class AttrsUtil {

    public static BarChartAttrs getCustomerRecyclerAttrs(Context context, AttributeSet attributeSet) {
        BarChartAttrs attrs = new BarChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BarChartRecyclerView);

        attrs.barBorderColor = ta.getColor(R.styleable.BarChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.barChartColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartColor, ColorUtil.getResourcesColor(context, R.color.pink));
        attrs.barChartEdgeColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartEdgeColor, ColorUtil.getResourcesColor(context, R.color.black_20_transparent));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.barSpace = ta.getFloat(R.styleable.BarChartRecyclerView_barSpace, 0.5f);

        //顶部文字
        attrs.barChartValueTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartValueTxtColor, ColorUtil.getResourcesColor(context, Color.GRAY));
        attrs.barChartValueTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValueTxtSize, DisplayUtil.sp2px(context, 10));
        attrs.barChartValuePaddingBottom = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValuePaddingBottom, DisplayUtil.dip2px(3));
        attrs.barChartValuePaddingLeft = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValuePaddingLeft, DisplayUtil.dip2px(2));
        attrs.barBorderWidth = ta.getDimension(R.styleable.BarChartRecyclerView_barBorderWidth, DisplayUtil.dip2px(0.5f));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.BarChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(15));
        attrs.maxYAxisPaddingTop = ta.getDimension(R.styleable.BarChartRecyclerView_maxYAxisPaddingTop, DisplayUtil.dip2px(10));


        //控制开关
        attrs.enableBarBorder = ta.getBoolean(R.styleable.BarChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableLeftYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableLeftYAxisLabel, true);
        attrs.enableRightYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableRightYAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisZero, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.BarChartRecyclerView_enableScrollToScale, true);

        attrs.ratioVelocity = ta.getFloat(R.styleable.BarChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.BarChartRecyclerView_ratioSpeed, 0.7f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.BarChartRecyclerView_layoutManagerOrientation, 0);

        //y轴相关
        attrs.yAxisLabelMaxScale = ta.getInteger(R.styleable.BarChartRecyclerView_yAxisLabelMaxScale, 30000);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_yAxisLabelTxtColor, Color.GRAY);
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.dip2px(11));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.BarChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.BarChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(context, R.color.black_20_transparent));
        attrs.yAxisLabelPaddingLeftRight = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelPaddingLeftRight, DisplayUtil.dip2px(2));
        attrs.yAxisLabelCenterPadding = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelCenterPadding, DisplayUtil.dip2px(3));

        //X轴相关
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(context, R.color.black_30_transparent));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisTxtSize = ta.getColor(R.styleable.BarChartRecyclerView_xAxisTxtSize, DisplayUtil.sp2px(context, 12));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.BarChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));

        ta.recycle();
        return attrs;
    }

}
