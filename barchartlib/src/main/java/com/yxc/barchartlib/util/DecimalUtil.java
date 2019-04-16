package com.yxc.barchartlib.util;

import com.yxc.barchartlib.entrys.BarEntry;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class DecimalUtil {


    @SuppressWarnings("unused")
    public final static double DOUBLE_EPSILON = Double.longBitsToDouble(1);

    @SuppressWarnings("unused")
    public final static float FLOAT_EPSILON = Float.intBitsToFloat(1);



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
        float max = barEntry.getY();
        for (int i = 0; i < entries.size(); i++) {
            BarEntry entryTemp = entries.get(i);
            max = Math.max(max, entryTemp.getY());
        }
        return max;
    }


    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 需要处理的字符串
     * @return 处理完之后的字符串
     */
    public static String addComma(String str) {
        DecimalFormat decimalFormat = new DecimalFormat(",###");
        return decimalFormat.format(Double.parseDouble(str));
    }


    public static long getCountStepSum(List<BarEntry> displayEntries){
        long count = 0;
        for (int i = 0; i < displayEntries.size(); i++) {
            BarEntry entry = displayEntries.get(i);
            count += entry.getY();
        }

        return count;
    }

    public static long getAverageStep(List<BarEntry> displayEntries){
        return getCountStepSum(displayEntries)/displayEntries.size();
    }

    public static String getAverageStepStr(List<BarEntry> displayEntries){
        return addComma(Long.toString(getAverageStep(displayEntries)));
    }


    public static String getCountStepStr(List<BarEntry> displayEntries){
        return addComma(Long.toString(getCountStepSum(displayEntries)));
    }

}
