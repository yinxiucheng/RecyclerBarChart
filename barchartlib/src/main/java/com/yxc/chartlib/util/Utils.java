package com.yxc.chartlib.util;

/**
 * @author yxc
 * @date 2019-06-04
 */
public class Utils {

    public static String getActionName(int action){
        switch (action){
            case 0:
                return "ACTION_DOWN";
            case 1:
                return "ACTION_UP";
            case 2:
                return "ACTION_MOVE";
            case 3:
                return "ACTION_CANCEL";
                default:
                    return "ACTION_DOWN";
        }

    }
}
