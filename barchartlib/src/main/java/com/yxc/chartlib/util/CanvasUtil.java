package com.yxc.chartlib.util;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * @author yxc
 * @since  2019/4/20
 */
public class CanvasUtil {

    public static Path createRectRoundPath2(float left, float top, float right, float bottom) {
        Path path = new Path();
        RectF rect1 = new RectF(left, top, right, bottom);

        float width = right - left;
        //指定四个圆角不同大小
        float radii[] = {width / 8, width / 8, width / 8, width / 8, 0, 0, 0, 0};
        path.addRoundRect(rect1, radii, Path.Direction.CCW);
        return path;
    }


    public static Path createRectRoundPath(RectF rectF, float radius) {
        Path path = new Path();
        //指定四个圆角不同大小
        float radii[] = {radius, radius, radius, radius, 0, 0, 0, 0};
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        return path;
    }

    public static Path createRectPath(RectF rectF) {
        Path path = new Path();
        //指定四个圆角不同大小
        float radii[] = {0, 0, 0, 0, 0, 0, 0, 0};
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        return path;
    }


    public static Path createRectRoundPathLeft(RectF rectF, float radius) {
        Path path = new Path();
        float radii[] = {radius, radius, 0, 0, 0, 0, 0, 0};
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        return path;
    }


    public static Path createRectRoundPathRight(RectF rectF, float radius) {
        Path path = new Path();
        float radii[] = {0, 0, radius, radius, 0, 0, 0, 0};
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        return path;
    }


    public static Path createRectRoundPath(RectF rectF, float radii[]) {
        Path path = new Path();
        path.addRoundRect(rectF, radii, Path.Direction.CCW);
        return path;
    }



}
