package com.yxc.chartlib.component;


import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.attrs.BaseChartAttrs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/8
 */
public class YAxis extends BaseYAxis {

    protected BaseChartAttrs mAttrs;
    public float labelHorizontalPadding;
    public float labelVerticalPadding;//刻度 字跟刻度线的位置对齐的调整
    public List<Float> scaleYLocationList;
    public HashMap<Float, Float> yAxisScaleMap;

    public YAxis(BaseChartAttrs barChartAttrs) {
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
    public static YAxis getYAxis(BarChartAttrs attrs, float max) {
        YAxis axis = new YAxis(attrs);
        if (max > 50000) {
            axis.mAxisMaximum = 80000;
            axis.setLabelCount(5);
        } else if (max > 30000) {
            axis.mAxisMaximum = 50000;
            axis.setLabelCount(5);
        } else if (max > 25000) {
            axis.mAxisMaximum = 30000;
            axis.setLabelCount(5);
        } else if (max > 20000) {
            axis.mAxisMaximum = 25000;
            axis.setLabelCount(5);
        } else if (max > 15000) {
            axis.mAxisMaximum = 20000;
            axis.setLabelCount(5);
        } else if (max > 10000) {
            axis.mAxisMaximum = 15000;
            axis.setLabelCount(5);
        } else if (max > 8000) {
            axis.mAxisMaximum = 10000;
            axis.setLabelCount(5);
        } else if (max > 6000) {
            axis.mAxisMaximum = 8000;
            axis.setLabelCount(5);
        } else if (max > 4000) {
            axis.mAxisMaximum = 6000;
            axis.setLabelCount(5);
        } else if (max > 3000) {
            axis.mAxisMaximum = 5000;
            axis.setLabelCount(5);
        } else if (max > 2000) {
            axis.mAxisMaximum = 3000;
            axis.setLabelCount(5);
        } else if (max > 1500) {
            axis.mAxisMaximum = 2000;
            axis.setLabelCount(5);
        } else if (max > 1000) {
            axis.mAxisMaximum = 1500;
            axis.setLabelCount(5);
        } else if (max > 800) {
            axis.mAxisMaximum = 1000;
            axis.setLabelCount(5);
        } else if (max > 500) {
            axis.mAxisMaximum = 800;
            axis.setLabelCount(5);
        } else if (max > 300) {
            axis.mAxisMaximum = 500;
            axis.setLabelCount(4);
        } else if (max > 200) {
            axis.mAxisMaximum = 300;
            axis.setLabelCount(4);
        } else if (max > 140) {
            axis.mAxisMaximum = 200;
            axis.setLabelCount(4);
        } else if (max > 120) {
            axis.mAxisMaximum = 140;
            axis.setLabelCount(7);
        } else if (max < 120 && max > 40) {
            axis.mAxisMaximum = 120;
            axis.setLabelCount(6);
        } else if (max < 12) {
            axis.mAxisMaximum = 12;
            axis.setLabelCount(6);
        } else if (max < 10) {
            axis.mAxisMaximum = 10;
            axis.setLabelCount(5);
        } else {
            int maxInt = Math.round(max);
            int distance = maxInt / 6;
            axis.mAxisMaximum = maxInt + distance;
            axis.setLabelCount(7);
        }
        return axis;
    }

    //获取Y轴刻度值, 当上一次 跟 这一次 对应的 刻度一样的时候，
    public YAxis resetYAxis(YAxis axis, float max) {
        float axisMaximum;
        int layoutCount;
        if (max > 50000) {
            axisMaximum = 8000;
            layoutCount = 5;
        } else if (max > 30000) {
            axisMaximum = 50000;
            layoutCount = 5;
        } else if (max > 25000) {
            axisMaximum = 30000;
            layoutCount = 5;
        } else if (max > 20000) {
            axisMaximum = 25000;
            layoutCount = 5;
        } else if (max > 15000) {
            axisMaximum = 20000;
            layoutCount = 5;
        } else if (max > 10000) {
            axisMaximum = 15000;
            layoutCount = 5;
        } else if (max > 8000) {
            axisMaximum = 10000;
            layoutCount = 5;
        } else if (max > 6000) {
            axisMaximum = 8000;
            layoutCount = 5;
        } else if (max > 4000) {
            axisMaximum = 6000;
            layoutCount = 5;
        } else if (max > 3000) {
            axisMaximum = 5000;
            layoutCount = 5;
        } else if (max > 2000) {
            axisMaximum = 3000;
            layoutCount = 5;
        } else if (max > 1500) {
            axisMaximum = 2000;
            layoutCount = 5;
        } else if (max > 1000) {
            axisMaximum = 1500;
            layoutCount = 5;
        } else if (max > 800) {
            axisMaximum = 1000;
            layoutCount = 5;
        } else if (max > 500) {
            axisMaximum = 800;
            layoutCount = 4;
        } else if (max > 300) {
            axisMaximum = 500;
            layoutCount = 4;
        } else if (max > 200) {
            axisMaximum = 300;
            layoutCount = 4;
        } else if (max > 140) {
            axisMaximum = 200;
            layoutCount = 4;
        } else if (max > 120) {
            axisMaximum = 140;
            layoutCount = 7;
        } else if (max < 120 && max > 40) {
            axisMaximum = 120;
            layoutCount = 6;
        } else if (max < 12) {
            axisMaximum = 12;
            layoutCount = 6;
        } else if (max < 10) {
            axisMaximum = 10;
            layoutCount = 5;
        } else {
            int maxInt = Math.round(max);
            int distance = maxInt / 6;
            axisMaximum = maxInt + distance;
            layoutCount = 7;
        }
        if (axisMaximum != mAxisMaximum) {
            axis.setAxisMaximum(axisMaximum);
            axis.setLabelCount(layoutCount);
            return axis;
        }
        return null;
    }
}
