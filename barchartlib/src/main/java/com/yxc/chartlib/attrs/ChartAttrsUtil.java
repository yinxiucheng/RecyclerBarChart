package com.yxc.chartlib.attrs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;

import com.yxc.chartlib.R;
import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;


public class ChartAttrsUtil {

    public static SleepChartAttrs getSleepChartAttrs(Context context, AttributeSet attributeSet){
        SleepChartAttrs attrs = new SleepChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.SleepChartRecyclerView);
        attrs.ratioSpeed = ta.getFloat(R.styleable.SleepChartRecyclerView_sr_layoutManagerOrientation, 1f);
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.SleepChartRecyclerView_sr_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.SleepChartRecyclerView_sr_layoutManagerReverseLayout, true);
        attrs.deepSleepColor = ta.getColor(R.styleable.SleepChartRecyclerView_deepSleepColor, ColorUtil.getResourcesColor(context, R.color.deep_sleep_color));
        attrs.slumberColor = ta.getColor(R.styleable.SleepChartRecyclerView_slumberColor, ColorUtil.getResourcesColor(context, R.color.slumber_color));
        attrs.eyeMoveColor = ta.getColor(R.styleable.SleepChartRecyclerView_eyeMoveColor, ColorUtil.getResourcesColor(context, R.color.eye_move_color));
        attrs.weakColor = ta.getColor(R.styleable.SleepChartRecyclerView_weakColor, ColorUtil.getResourcesColor(context, R.color.wake_color));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.SleepChartRecyclerView_sr_contentPaddingBottom, DisplayUtil.dip2px(80));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.SleepChartRecyclerView_sr_contentPaddingTop, DisplayUtil.dip2px(5));
        attrs.txtColor = ta.getColor(R.styleable.SleepChartRecyclerView_sr_txtColor, ColorUtil.getResourcesColor(context, R.color.black_40_transparent));
        attrs.txtSize = ta.getDimension(R.styleable.SleepChartRecyclerView_sr_txtSize, DisplayUtil.dip2px(10));
        attrs.sleepItemHeight = ta.getDimension(R.styleable.SleepChartRecyclerView_sleepItemHeight, DisplayUtil.dip2px(33));

        attrs.enableValueMark = ta.getBoolean(R.styleable.SleepChartRecyclerView_sr_enableValueMark, true);
        //highLight
        attrs.highLightLeftTxtColor = ta.getColor(R.styleable.SleepChartRecyclerView_sr_highLightLeftTxtColor, ColorUtil.getResourcesColor(context, R.color.white_70_transparent));
        attrs.highLightRightTxtColor = ta.getColor(R.styleable.SleepChartRecyclerView_sr_highLightRightTxtColor, ColorUtil.getResourcesColor(context, R.color.white));
        attrs.highLightLeftTxtSize = ta.getDimension(R.styleable.SleepChartRecyclerView_sr_highLightLeftTxtSize, DisplayUtil.dip2px(11));
        attrs.highLightRightTxtSize = ta.getDimension(R.styleable.SleepChartRecyclerView_sr_highLightRightTxtSize, DisplayUtil.dip2px(14));
        attrs.highLightRectColor = ta.getColor(R.styleable.SleepChartRecyclerView_sr_highLightRectColor, ColorUtil.getResourcesColor(context, R.color.pink));

        ta.recycle();
        return attrs;
    }

    public static BarChartAttrs getBarChartRecyclerAttrs(Context context, AttributeSet attributeSet) {
        BarChartAttrs attrs = new BarChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BarChartRecyclerView);

        attrs.barBorderColor = ta.getColor(R.styleable.BarChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.barChartColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartColor, ColorUtil.getResourcesColor(context, R.color.pink));
        attrs.barChartEdgeColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartEdgeColor, ColorUtil.getResourcesColor(context, R.color.black_10_transparent));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.barSpace = ta.getFloat(R.styleable.BarChartRecyclerView_barSpace, 0.5f);

        //BarChart Value
        attrs.barChartValueTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartValueTxtColor, ColorUtil.getResourcesColor(context, R.color.black_50_transparent));
        attrs.barChartValueTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValueTxtSize, DisplayUtil.sp2px(context, 12));
        attrs.barChartValueTxtMaskColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartValueTxtMaskColor, ColorUtil.getResourcesColor(context, R.color.white));
        attrs.barChartValueTxtMaskSize = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValueTxtMaskSize, DisplayUtil.sp2px(context, 13));
        attrs.barChartValuePaddingBottom = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValuePaddingBottom, DisplayUtil.dip2px(3));
        attrs.barChartValuePaddingLeft = ta.getDimension(R.styleable.BarChartRecyclerView_barChartValuePaddingLeft, DisplayUtil.dip2px(2));
        attrs.barBorderWidth = ta.getDimension(R.styleable.BarChartRecyclerView_barBorderWidth, DisplayUtil.dip2px(0.5f));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.BarChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(20));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.BarChartRecyclerView_maxYAxisPaddingTop, DisplayUtil.dip2px(15));
        attrs.displayNumbers = ta.getInteger(R.styleable.BarChartRecyclerView_displayNumbers, 12);
        attrs.barChartRoundRectRadiusRatio = ta.getFloat(R.styleable.BarChartRecyclerView_barChartRoundRectRadiusRatio, 1.0f/8);

        //Switch Button
        attrs.enableBarBorder = ta.getBoolean(R.styleable.BarChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableLeftYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableLeftYAxisLabel, true);
        attrs.enableRightYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableRightYAxisLabel, true);
        attrs.enableYAxisGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisZero, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.BarChartRecyclerView_enableScrollToScale, true);
        attrs.enableValueMark = ta.getBoolean(R.styleable.BarChartRecyclerView_enableValueMark, true);
        attrs.enableXAxisDisplayLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisDisplayLabel, false);
        attrs.enableXAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableXAxisFirstGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisFirstGridLine, true);
        attrs.enableXAxisSecondGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisSecondGridLine, true);
        attrs.enableXAxisThirdGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisThirdGridLine, true);
        attrs.enableXAxisLineCircle = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisLineCircle, true);
        attrs.averageDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisLabel, false);

        attrs.ratioVelocity = ta.getFloat(R.styleable.BarChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.BarChartRecyclerView_ratioSpeed, 1.0f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.BarChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.BarChartRecyclerView_layoutManagerReverseLayout, true);

        //YAxis
        attrs.yAxisMaximum = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisMaximum, 30000);
        attrs.yAxisMinimum = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisMinimum, 0);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_yAxisLabelTxtColor, Color.GRAY);
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.dip2px(11));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.BarChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.BarChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(context, R.color.black_20_transparent));
        attrs.yAxisLabelHorizontalPadding = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelHorizontalPadding, DisplayUtil.dip2px(2));
        attrs.yAxisLabelVerticalPadding = ta.getDimension(R.styleable.BarChartRecyclerView_yAxisLabelVerticalPadding, DisplayUtil.dip2px(3));
        attrs.yAxisReverse = ta.getBoolean(R.styleable.BarChartRecyclerView_yAxisReverse, false);

        //XAxis
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(context, R.color.black_30_transparent));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.BarChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisTxtSize = ta.getDimension(R.styleable.BarChartRecyclerView_xAxisTxtSize, DisplayUtil.sp2px(context, 12));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.BarChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));
        attrs.xAxisScaleDistance = ta.getInteger(R.styleable.BarChartRecyclerView_xAxisScaleDistance, 5);

        //bezier curve
        attrs.bezierIntensity = ta.getFloat(R.styleable.BarChartRecyclerView_bezierIntensity, 0.25f);
        attrs.enableBezierLineFill = ta.getBoolean(R.styleable.BarChartRecyclerView_enableBezierLineFill, true);
        attrs.bezierFillAlpha = ta.getInteger(R.styleable.BarChartRecyclerView_bezierFillAlpha, 0x30);
        attrs.bezierFillColor = ta.getColor(R.styleable.BarChartRecyclerView_bezierFillColor, ColorUtil.getResourcesColor(context, R.color.pink));
        attrs.bezierLinePaintColor = ta.getColor(R.styleable.BarChartRecyclerView_bezierLinePaintColor, ColorUtil.getResourcesColor(context, R.color.pink));
        attrs.bezierLinePaintStrokeWidth = ta.getDimension(R.styleable.BarChartRecyclerView_bezierLinePaintStrokeWidth, DisplayUtil.dip2px(3));

        //line fill color
        attrs.lineShaderBeginColor = ta.getColor(R.styleable.BarChartRecyclerView_lineShaderBeginColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_begin));
        attrs.lineShaderEndColor = ta.getColor(R.styleable.BarChartRecyclerView_lineShaderEndColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_end));
        attrs.enableLineFill = ta.getBoolean(R.styleable.BarChartRecyclerView_enableLineFill, true);
        attrs.lineColor = ta.getColor(R.styleable.BarChartRecyclerView_lineColor, -1);
        attrs.fillAlpha = ta.getInteger(R.styleable.BarChartRecyclerView_fillAlpha, 0xA0);

        //highLight
        attrs.highLightLineType = ta.getInt(R.styleable.BarChartRecyclerView_highLightLineType, BarChartAttrs.HIGH_LIGHT_LINE_BOTTOM);
        attrs.linePointRadius = ta.getDimension(R.styleable.BarChartRecyclerView_linePointRadius, DisplayUtil.dip2px(3));
        attrs.linePointSelectRadius = ta.getDimension(R.styleable.BarChartRecyclerView_linePointSelectRadius, DisplayUtil.dip2px(5));
        attrs.linePointSelectStrokeWidth = ta.getDimension(R.styleable.BarChartRecyclerView_linePointSelectStrokeWidth, DisplayUtil.dip2px(1));

        attrs.rateChartDarkColor = ta.getColor(R.styleable.BarChartRecyclerView_rateChartDarkColor, ColorUtil.getResourcesColor(context, R.color.rate_title_select_color));
        attrs.rateChartLightColor = ta.getColor(R.styleable.BarChartRecyclerView_rateChartLightColor, ColorUtil.getResourcesColor(context, R.color.rate_chart_color_light));

        attrs.isDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_isDisplay, false);

        ta.recycle();
        return attrs;
    }
}
