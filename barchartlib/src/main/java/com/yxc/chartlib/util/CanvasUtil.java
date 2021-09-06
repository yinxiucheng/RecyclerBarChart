package com.yxc.chartlib.util;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * @author yxc
 * @since 2019/4/20
 */
public class CanvasUtil {

    public static Path createRectRoundPath(RectF rectF, float radius, int type) {
        Path path = new Path();
        //指定四个圆角不同大小
        float radii[] = RoundRectType.getRoundValues(type, radius);
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        return path;
    }

    public static Path createRectRoundPath(RectF rectF, float radius) {
        return createRectRoundPath(rectF, radius, RoundRectType.TYPE_TOP);
    }

    public static Path createRectPath(RectF rectF) {
        Path path = new Path();
        //指定四个圆角不同大小
        float radii[] = {0, 0, 0, 0, 0, 0, 0, 0};
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        return path;
    }





}
