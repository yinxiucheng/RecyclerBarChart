package com.yxc.chartlib.recyclerchart.util;

/**
 * @author yxc
 * @date 2019-10-11
 */
public class RoundRectType {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_TOP = 1;
    public static final int TYPE_BOTTOM = 2;
    public static final int TYPE_LEFT = 3;
    public static final int TYPE_RIGHT = 4;
    public static final int TYPE_LEFT_TOP = 5;
    public static final int TYPE_LEFT_BOTTOM = 6;
    public static final int TYPE_RIGHT_TOP = 7;
    public static final int TYPE_RIGHT_BOTTOM = 8;

    public static float[] getRoundValues(int type, float radius) {
        switch (type) {
            case TYPE_ALL:
                return new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
            case TYPE_BOTTOM:
                return new float[]{0, 0, 0, 0, radius, radius, radius, radius};
            case TYPE_LEFT:
                return new float[]{radius, radius, 0, 0, 0, 0, radius, radius};
            case TYPE_RIGHT:
                return new float[]{0, 0, radius, radius, radius, radius, 0, 0};
            case TYPE_LEFT_TOP:
                return new float[]{radius, radius, 0, 0, 0, 0, 0, 0};
            case TYPE_LEFT_BOTTOM:
                return new float[]{0, 0, 0, 0, 0, 0, radius, radius};
            case TYPE_RIGHT_TOP:
                return new float[]{0, 0, radius, radius, 0, 0, 0, 0};
            case TYPE_RIGHT_BOTTOM:
                return new float[]{0, 0, 0, 0, radius, radius, 0, 0};
            case TYPE_TOP:
            default:
                return new float[]{radius, radius, radius, radius, 0, 0, 0, 0};
        }

    }
}
