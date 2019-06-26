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
        attrs.ratioSpeed = ta.getFloat(R.styleable.SleepChartRecyclerView_layoutManagerOrientation, 1f);
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.SleepChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.SleepChartRecyclerView_layoutManagerReverseLayout, true);
        attrs.deepSleepColor = ta.getColor(R.styleable.SleepChartRecyclerView_deepSleepColor, ColorUtil.getResourcesColor(context, R.color.deep_sleep_color));
        attrs.slumberColor = ta.getColor(R.styleable.SleepChartRecyclerView_slumberColor, ColorUtil.getResourcesColor(context, R.color.slumber_color));
        attrs.eyeMoveColor = ta.getColor(R.styleable.SleepChartRecyclerView_eyeMoveColor, ColorUtil.getResourcesColor(context, R.color.eye_move_color));
        attrs.weakColor = ta.getColor(R.styleable.SleepChartRecyclerView_weakColor, ColorUtil.getResourcesColor(context, R.color.wake_color));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.SleepChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(80));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.SleepChartRecyclerView_contentPaddingTop, DisplayUtil.dip2px(5));
        attrs.txtColor = ta.getColor(R.styleable.SleepChartRecyclerView_txtColor, ColorUtil.getResourcesColor(context, R.color.black_40_transparent));
        attrs.txtSize = ta.getDimension(R.styleable.SleepChartRecyclerView_txtSize, DisplayUtil.dip2px(10));
        attrs.sleepItemHeight = ta.getDimension(R.styleable.SleepChartRecyclerView_sleepItemHeight, DisplayUtil.dip2px(33));

        attrs.enableValueMark = ta.getBoolean(R.styleable.SleepChartRecyclerView_enableValueMark, true);
        //highLight
        ta.recycle();
        return attrs;
    }

    public static BarChartAttrs getBarChartRecyclerAttrs(Context context, AttributeSet attributeSet) {
        BarChartAttrs attrs = new BarChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BarChartRecyclerView);

        attrs.barBorderColor = ta.getColor(R.styleable.BarChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.chartColor = ta.getColor(R.styleable.BarChartRecyclerView_chartColor, ColorUtil.getResourcesColor(context, R.color.bar_chart_pink));
        attrs.chartEdgeColor = ta.getColor(R.styleable.BarChartRecyclerView_chartEdgeColor, ColorUtil.getResourcesColor(context, R.color.black_10_transparent));
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
        attrs.contentPaddingTop = ta.getDimension(R.styleable.BarChartRecyclerView_contentPaddingTop, DisplayUtil.dip2px(15));
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
        attrs.yAxisHighStandardLine = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisHighStandardLine, -1);
        attrs.yAxisMiddleStandardLine = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisMiddleStandardLine, -1);
        attrs.yAxisLowStandardLine = ta.getFloat(R.styleable.BarChartRecyclerView_yAxisLowStandardLine, -1);
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
        
        //line fill color
        attrs.lineShaderBeginColor = ta.getColor(R.styleable.BarChartRecyclerView_lineShaderBeginColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_begin));
        attrs.lineShaderEndColor = ta.getColor(R.styleable.BarChartRecyclerView_lineShaderEndColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_end));
        attrs.enableLineFill = ta.getBoolean(R.styleable.BarChartRecyclerView_enableLineFill, true);
        attrs.fillAlpha = ta.getInteger(R.styleable.BarChartRecyclerView_fillAlpha, 0xA0);

        //highLight
        attrs.isDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_isDisplay, false);

        attrs.rateChartDarkColor = ta.getColor(R.styleable.BarChartRecyclerView_rateChartDarkColor, ColorUtil.getResourcesColor(context, R.color.rate_title_select_color));
        attrs.rateChartLightColor = ta.getColor(R.styleable.BarChartRecyclerView_rateChartLightColor, ColorUtil.getResourcesColor(context, R.color.rate_chart_color_light));

        ta.recycle();
        return attrs;
    }

    public static LineChartAttrs getLineChartRecyclerAttrs(Context context, AttributeSet attributeSet) {
        LineChartAttrs attrs = new LineChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.LineChartRecyclerView);

        attrs.barBorderColor = ta.getColor(R.styleable.LineChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.LineChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.LineChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.chartColor = ta.getColor(R.styleable.LineChartRecyclerView_chartColor, ColorUtil.getResourcesColor(context, R.color.bar_chart_pink));
        attrs.barSpace = ta.getFloat(R.styleable.LineChartRecyclerView_barSpace, 0.5f);

        //BarChart Value
        attrs.barBorderWidth = ta.getDimension(R.styleable.LineChartRecyclerView_barBorderWidth, DisplayUtil.dip2px(0.5f));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.LineChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(20));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.LineChartRecyclerView_contentPaddingTop, DisplayUtil.dip2px(15));
        attrs.displayNumbers = ta.getInteger(R.styleable.LineChartRecyclerView_displayNumbers, 12);

        //Switch Button
        attrs.enableBarBorder = ta.getBoolean(R.styleable.LineChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.LineChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableLeftYAxisLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableLeftYAxisLabel, true);
        attrs.enableRightYAxisLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableRightYAxisLabel, true);
        attrs.enableYAxisGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableYAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.LineChartRecyclerView_enableYAxisZero, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.LineChartRecyclerView_enableScrollToScale, true);
        attrs.enableValueMark = ta.getBoolean(R.styleable.LineChartRecyclerView_enableValueMark, true);
        attrs.enableXAxisDisplayLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisDisplayLabel, false);
        attrs.enableXAxisLabel = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableXAxisFirstGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisFirstGridLine, true);
        attrs.enableXAxisSecondGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisSecondGridLine, true);
        attrs.enableXAxisThirdGridLine = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisThirdGridLine, true);
        attrs.enableXAxisLineCircle = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisLineCircle, true);
        attrs.averageDisplay = ta.getBoolean(R.styleable.LineChartRecyclerView_enableXAxisLabel, false);

        attrs.ratioVelocity = ta.getFloat(R.styleable.LineChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.LineChartRecyclerView_ratioSpeed, 1.0f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.LineChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.LineChartRecyclerView_layoutManagerReverseLayout, true);

        //YAxis
        attrs.yAxisMaximum = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisMaximum, 30000);
        attrs.yAxisMinimum = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisMinimum, 0);
        attrs.yAxisHighStandardLine = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisHighStandardLine, -1);
        attrs.yAxisMiddleStandardLine = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisMiddleStandardLine, -1);
        attrs.yAxisLowStandardLine = ta.getFloat(R.styleable.LineChartRecyclerView_yAxisLowStandardLine, -1);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.LineChartRecyclerView_yAxisLabelTxtColor, Color.GRAY);
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.LineChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.dip2px(11));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.LineChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.LineChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(context, R.color.black_20_transparent));
        attrs.yAxisLabelHorizontalPadding = ta.getDimension(R.styleable.LineChartRecyclerView_yAxisLabelHorizontalPadding, DisplayUtil.dip2px(2));
        attrs.yAxisLabelVerticalPadding = ta.getDimension(R.styleable.LineChartRecyclerView_yAxisLabelVerticalPadding, DisplayUtil.dip2px(3));
        attrs.yAxisReverse = ta.getBoolean(R.styleable.LineChartRecyclerView_yAxisReverse, false);

        //XAxis
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(context, R.color.black_30_transparent));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.LineChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisTxtSize = ta.getDimension(R.styleable.LineChartRecyclerView_xAxisTxtSize, DisplayUtil.sp2px(context, 12));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.LineChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));
        attrs.xAxisScaleDistance = ta.getInteger(R.styleable.LineChartRecyclerView_xAxisScaleDistance, 5);

        //line fill color
        attrs.lineShaderBeginColor = ta.getColor(R.styleable.LineChartRecyclerView_lineShaderBeginColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_begin));
        attrs.lineShaderEndColor = ta.getColor(R.styleable.LineChartRecyclerView_lineShaderEndColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_end));
        attrs.enableLineFill = ta.getBoolean(R.styleable.LineChartRecyclerView_enableLineFill, true);
        attrs.lineColor = ta.getColor(R.styleable.LineChartRecyclerView_lineColor, -1);
        attrs.fillAlpha = ta.getInteger(R.styleable.LineChartRecyclerView_fillAlpha, 0xA0);

        //highLight
        attrs.lineHighLightType = ta.getInt(R.styleable.LineChartRecyclerView_lineHighLightType, LineChartAttrs.HIGH_LIGHT_LINE_BOTTOM);
        attrs.linePointRadius = ta.getDimension(R.styleable.LineChartRecyclerView_linePointRadius, DisplayUtil.dip2px(3));
        attrs.linePointSelectRadius = ta.getDimension(R.styleable.LineChartRecyclerView_linePointSelectRadius, DisplayUtil.dip2px(5));
        attrs.linePointSelectStrokeWidth = ta.getDimension(R.styleable.LineChartRecyclerView_linePointSelectStrokeWidth, DisplayUtil.dip2px(1));
        attrs.lineSelectCircles = ta.getInteger(R.styleable.LineChartRecyclerView_lineSelectCircles, 2);//默认显示两个

        //highLight
        attrs.isDisplay = ta.getBoolean(R.styleable.LineChartRecyclerView_isDisplay, false);

        ta.recycle();
        return attrs;
    }


    public static BezierChartAttrs getBezierChartAttrs(Context context, AttributeSet attributeSet) {
        BezierChartAttrs attrs = new BezierChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BezierChartRecyclerView);
        attrs.barBorderColor = ta.getColor(R.styleable.BezierChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.chartColor = ta.getColor(R.styleable.BezierChartRecyclerView_chartColor, ColorUtil.getResourcesColor(context, R.color.bar_chart_pink));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.BezierChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.BezierChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));
        attrs.barSpace = ta.getFloat(R.styleable.BezierChartRecyclerView_barSpace, 0.5f);

        //BarChart Value
        attrs.barBorderWidth = ta.getDimension(R.styleable.BezierChartRecyclerView_barBorderWidth, DisplayUtil.dip2px(0.5f));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.BezierChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(20));
        attrs.contentPaddingTop = ta.getDimension(R.styleable.BezierChartRecyclerView_contentPaddingTop, DisplayUtil.dip2px(15));
        attrs.displayNumbers = ta.getInteger(R.styleable.BezierChartRecyclerView_displayNumbers, 12);

        //Switch Button
        attrs.enableBarBorder = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableLeftYAxisLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableLeftYAxisLabel, true);
        attrs.enableRightYAxisLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableRightYAxisLabel, true);
        attrs.enableYAxisGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableYAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableYAxisZero, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableScrollToScale, true);
        attrs.enableValueMark = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableValueMark, true);
        attrs.enableXAxisDisplayLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisDisplayLabel, false);
        attrs.enableXAxisLabel = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableXAxisFirstGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisFirstGridLine, true);
        attrs.enableXAxisSecondGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisSecondGridLine, true);
        attrs.enableXAxisThirdGridLine = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisThirdGridLine, true);
        attrs.enableXAxisLineCircle = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisLineCircle, true);
        attrs.averageDisplay = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableXAxisLabel, false);

        attrs.ratioVelocity = ta.getFloat(R.styleable.BezierChartRecyclerView_ratioVelocity, 0.5f);
        attrs.ratioSpeed = ta.getFloat(R.styleable.BezierChartRecyclerView_ratioSpeed, 1.0f);
        //default is horizontal
        attrs.layoutManagerOrientation = ta.getInteger(R.styleable.BezierChartRecyclerView_layoutManagerOrientation, 0);
        attrs.layoutManagerReverseLayout = ta.getBoolean(R.styleable.BezierChartRecyclerView_layoutManagerReverseLayout, true);

        //YAxis
        attrs.yAxisMaximum = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisMaximum, 30000);
        attrs.yAxisMinimum = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisMinimum, 0);
        attrs.yAxisHighStandardLine = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisHighStandardLine, -1);
        attrs.yAxisMiddleStandardLine = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisMiddleStandardLine, -1);
        attrs.yAxisLowStandardLine = ta.getFloat(R.styleable.BezierChartRecyclerView_yAxisLowStandardLine, -1);
        attrs.yAxisLabelTxtColor = ta.getColor(R.styleable.BezierChartRecyclerView_yAxisLabelTxtColor, Color.GRAY);
        attrs.yAxisLabelTxtSize = ta.getDimension(R.styleable.BezierChartRecyclerView_yAxisLabelTxtSize, DisplayUtil.dip2px(11));
        attrs.yAxisLabelSize = ta.getInteger(R.styleable.BezierChartRecyclerView_yAxisLabelSize, 5);
        attrs.yAxisLineColor = ta.getColor(R.styleable.BezierChartRecyclerView_yAxisLineColor, ColorUtil.getResourcesColor(context, R.color.black_20_transparent));
        attrs.yAxisLabelHorizontalPadding = ta.getDimension(R.styleable.BezierChartRecyclerView_yAxisLabelHorizontalPadding, DisplayUtil.dip2px(2));
        attrs.yAxisLabelVerticalPadding = ta.getDimension(R.styleable.BezierChartRecyclerView_yAxisLabelVerticalPadding, DisplayUtil.dip2px(3));
        attrs.yAxisReverse = ta.getBoolean(R.styleable.BezierChartRecyclerView_yAxisReverse, false);

        //XAxis
        attrs.xAxisFirstDividerColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisFirstDividerColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisSecondDividerColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisSecondDividerColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.xAxisThirdDividerColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisThirdDividerColor, ColorUtil.getResourcesColor(context, R.color.black_30_transparent));
        attrs.xAxisTxtColor = ta.getColor(R.styleable.BezierChartRecyclerView_xAxisTxtColor, ColorUtil.getResourcesColor(context, R.color.black));
        attrs.xAxisTxtSize = ta.getDimension(R.styleable.BezierChartRecyclerView_xAxisTxtSize, DisplayUtil.sp2px(context, 12));
        attrs.xAxisLabelTxtPadding = ta.getDimension(R.styleable.BezierChartRecyclerView_xAxisLabelTxtPadding, DisplayUtil.dip2px(2));
        attrs.xAxisScaleDistance = ta.getInteger(R.styleable.BezierChartRecyclerView_xAxisScaleDistance, 5);

        //bezier curve
        attrs.bezierIntensity = ta.getFloat(R.styleable.BezierChartRecyclerView_bezierIntensity, 0.25f);
        attrs.enableBezierLineFill = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableBezierLineFill, true);
        attrs.bezierFillAlpha = ta.getInteger(R.styleable.BezierChartRecyclerView_bezierFillAlpha, 0x30);
        attrs.bezierFillColor = ta.getColor(R.styleable.BezierChartRecyclerView_bezierFillColor, ColorUtil.getResourcesColor(context, R.color.bar_chart_pink));
        attrs.bezierLinePaintColor = ta.getColor(R.styleable.BezierChartRecyclerView_bezierLinePaintColor, ColorUtil.getResourcesColor(context, R.color.bar_chart_pink));
        attrs.bezierLinePaintStrokeWidth = ta.getDimension(R.styleable.BezierChartRecyclerView_bezierLinePaintStrokeWidth, DisplayUtil.dip2px(3));

        //line fill color
        attrs.lineShaderBeginColor = ta.getColor(R.styleable.BezierChartRecyclerView_lineShaderBeginColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_begin));
        attrs.lineShaderEndColor = ta.getColor(R.styleable.BezierChartRecyclerView_lineShaderEndColor, ColorUtil.getResourcesColor(context, R.color.rate_shader_end));
        attrs.enableLineFill = ta.getBoolean(R.styleable.BezierChartRecyclerView_enableLineFill, true);
        attrs.fillAlpha = ta.getInteger(R.styleable.BezierChartRecyclerView_fillAlpha, 0xA0);

        //highLight
        attrs.isDisplay = ta.getBoolean(R.styleable.BezierChartRecyclerView_isDisplay, false);
        ta.recycle();
        return attrs;
    }

}
