package com.yxc.chartlib.recyclerchart.component;


import com.yxc.chartlib.recyclerchart.attrs.BaseChartAttrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author yxc
 * @since 2019/4/8
 */
public class HrmYAxis extends BaseYAxis {

    protected BaseChartAttrs mAttrs;
    public HashMap<Float, Float> yAxisScaleMap;

    public HrmYAxis(BaseChartAttrs barChartAttrs) {
        super(barChartAttrs);
    }

    public void setLabelCount(int count) {
        super.setLabelCount(count);
        float label = mAxisMaximum;
        float itemRange = mAxisMaximum / count;

        if (null == mEntries) {
            mEntries = new ArrayList<>();
        } else {
            mEntries.clear();
        }
        for (int i = 0; i <= count; i++) {
            if (i > 0) {
                label = label - itemRange;
            }
            mEntries.add(i, label);
        }
    }

    public HashMap<Float, Float> getYAxisScaleMap(float topLocation, float itemHeight, int count) {
        if (null == mEntries || mEntries.isEmpty()) {
            return new HashMap<>();
        }
        if (null == yAxisScaleMap) {
            yAxisScaleMap = new LinkedHashMap<>();
        } else {
            yAxisScaleMap.clear();
        }
        float location = topLocation;
        for (int i = 0; i <= count; i++) {
            if (i > 0) {
                location = location + itemHeight;
            }
            if (i < mEntries.size()) {
                yAxisScaleMap.put(location, mEntries.get(i));
            } else {
                //这里其实已经出错了，值的个数跟位置不匹配
                yAxisScaleMap.put(location, 0f);
            }
        }
        return yAxisScaleMap;
    }


    //获取Y轴刻度值
    public static HrmYAxis getYAxis(BaseChartAttrs attrs, float max) {
        HrmYAxis axis = new HrmYAxis(attrs);
        int maxInt = Math.round(max);
        int distance = maxInt / 7;
        axis.mAxisMaximum = maxInt + distance;
        axis.setLabelCount(8);
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public HrmYAxis resetYAxis(HrmYAxis axis, float max) {
        float axisMaximum;
        int layoutCount;
        int maxInt = Math.round(max);
        int distance = maxInt / 7;
        axisMaximum = maxInt + distance;
        layoutCount = 8;
        if (axisMaximum != mAxisMaximum) {
            axis.setAxisMaximum(axisMaximum);
            axis.setLabelCount(layoutCount);
            return axis;
        }
        return null;
    }
}
