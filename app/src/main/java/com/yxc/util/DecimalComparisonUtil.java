package com.yxc.util;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class DecimalComparisonUtil {

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

}
