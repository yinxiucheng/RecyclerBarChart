package com.yxc.chartlib.util;

import com.yxc.chartlib.entrys.BarEntry;

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class DecimalUtil {

    //一位小数
    public static final int ONE_LENGTH_DECIMAL = 1;
    //两位小数
    public static final int TWO_LENGTH_DECIMAL = 2;
    //三位小数
    public static final int THREE_LENGTH_DECIMAL = 3;

    //获取小数点后 type 小数的Float值。
    public static float getDecimalFloat(int type, float value) {
        float f;
        if (type == TWO_LENGTH_DECIMAL) {
            float temp = (value * 100.0f);
            f = Math.round(temp);
            f = f / 100.0f;
        } else if (type == THREE_LENGTH_DECIMAL) {
            float temp = (value * 1000.0f);
            f = Math.round(temp);
            f = f / 1000.0f;
        } else {
            float temp = (value * 10.0f);
            f = Math.round(temp);
            f = f / 10.0f;
        }
        return f;
    }


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
    public static float getTheMaxNumber(List<? extends BarEntry> entries) {
        BarEntry barEntry = entries.get(0);
        float max = barEntry.getY();
        for (int i = 0; i < entries.size(); i++) {
            BarEntry entryTemp = entries.get(i);
            max = Math.max(max, entryTemp.getY());
        }
        return max;
    }


    public static float getTheMinNumber(List<? extends BarEntry> entries) {
        BarEntry barEntry = entries.get(0);
        float min = barEntry.getY();
        for (int i = 0; i < entries.size(); i++) {
            BarEntry entryTemp = entries.get(i);
            min = Math.min(min, entryTemp.getY());
        }
        return min;
    }

    public static String getTheMaxNumberIntStr(List<BarEntry> entries){
        return addComma(Integer.toString((int)getTheMaxNumber(entries)));
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


    public static long getCountSum(List<BarEntry> displayEntries){
        long count = 0;
        for (int i = 0; i < displayEntries.size(); i++) {
            BarEntry entry = displayEntries.get(i);
            count += entry.getY();
        }
        return count;
    }

    public static long getAverage(List<BarEntry> displayEntries){
        return getCountSum(displayEntries)/displayEntries.size();
    }

    public static String getAverageStr(List<BarEntry> displayEntries){
        return addComma(Long.toString(getAverage(displayEntries)));
    }


    public static String getCountStr(List<BarEntry> displayEntries){
        return addComma(Long.toString(getCountSum(displayEntries)));
    }

}
