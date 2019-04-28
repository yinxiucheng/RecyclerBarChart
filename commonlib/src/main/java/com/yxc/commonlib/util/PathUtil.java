package com.yxc.commonlib.util;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * @author yxc
 * @date 2019/4/28
 */
public class PathUtil {

    public static void moveTo(Path path, PointF startPoint) {
        path.moveTo(startPoint.x, startPoint.y);
    }

    public static void cubicTo(Path path, PointF control1, PointF control2, PointF pointf) {
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, pointf.x, pointf.y);
    }


}
