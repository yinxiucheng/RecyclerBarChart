package com.yxc.barchartlib.util;

import com.yxc.barchartlib.component.BarEntry;

import java.util.List;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class DecimalUtil {

    final float THRESHOLD = 0.00001f;

    public static final boolean equals(float a, float b) {
        return Math.abs(a - b) < 0.00001;
    }

    public static final boolean equals(int a, float b) {
        return Math.abs(a - b) < 0.00001;
    }


    public static final boolean equals(float a, int b) {
        return Math.abs(a - b) < 0.00001;
    }

    public static final boolean smallOrEquals(float a, float b) {
        boolean result = (a < b) || equals(a, b);
        return result;
    }

    public static final boolean bigOrEquals(float a, float b){
        return (a > b) || equals(a, b);
    }


    public static final boolean smallOrEquals(int a, float b) {
        boolean result = (a < b) || equals(a, b);
        return result;
    }


    public static final boolean smallOrEquals(float a, int b) {
        boolean result = (a < b) || equals(a, b);
        return result;
    }


    //获取最大值
    public static float getTheMaxNumber(List<BarEntry> entries) {
        BarEntry barEntry = entries.get(0);
        float max = barEntry.value;
        for (int i = 0; i < entries.size(); i++) {
            BarEntry entryTemp = entries.get(i);
            max = Math.max(max, entryTemp.value);
        }
        return max;
    }

}
