package com.yxc.barchartlib.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewConfiguration;

/**
 * Utilities class that has some helper methods. Needs to be initialized by
 * calling Utils.init(...) before usage. Inside the Chart.init() method, this is
 * done, if the Utils are used before that, Utils.init(...) needs to be called
 * manually.
 *
 * @author Philipp Jahoda
 */
public abstract class Utils {

    private static DisplayMetrics mMetrics;
    private static int mMinimumFlingVelocity = 50;
    private static int mMaximumFlingVelocity = 8000;
    public final static double DEG2RAD = (Math.PI / 180.0);
    public final static float FDEG2RAD = ((float) Math.PI / 180.f);

    @SuppressWarnings("unused")
    public final static double DOUBLE_EPSILON = Double.longBitsToDouble(1);

    @SuppressWarnings("unused")
    public final static float FLOAT_EPSILON = Float.intBitsToFloat(1);

    /**
     * initialize method, called inside the Chart.init() method.
     *
     * @param context
     */
    @SuppressWarnings("deprecation")
    public static void init(Context context) {

        if (context == null) {
            // noinspection deprecation
            mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
            // noinspection deprecation
            mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();

            Log.e("MPChartLib-Utils"
                    , "Utils.init(...) PROVIDED CONTEXT OBJECT IS NULL");

        } else {
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            mMinimumFlingVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
            mMaximumFlingVelocity = viewConfiguration.getScaledMaximumFlingVelocity();

            Resources res = context.getResources();
            mMetrics = res.getDisplayMetrics();
        }
    }

    /**
     * initialize method, called inside the Chart.init() method. backwards
     * compatibility - to not break existing code
     *
     * @param res
     */
    @Deprecated
    public static void init(Resources res) {

        mMetrics = res.getDisplayMetrics();

        // noinspection deprecation
        mMinimumFlingVelocity = ViewConfiguration.getMinimumFlingVelocity();
        // noinspection deprecation
        mMaximumFlingVelocity = ViewConfiguration.getMaximumFlingVelocity();
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device
     * density. NEEDS UTILS TO BE INITIALIZED BEFORE USAGE.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need
     *           to convert into pixels
     * @return A float value to represent px equivalent to dp depending on
     * device density
     */
    public static float convertDpToPixel(float dp) {

        if (mMetrics == null) {

            Log.e("MPChartLib-Utils",
                    "Utils NOT INITIALIZED. You need to call Utils.init(...) at least once before" +
                            " calling Utils.convertDpToPixel(...). Otherwise conversion does not " +
                            "take place.");
            return dp;
        }

        return dp * mMetrics.density;
    }







    /**
     * rounds the given number to the next significant number
     *
     * @param number
     * @return
     */
    public static float roundToNextSignificant(double number) {
        if (Double.isInfinite(number) ||
            Double.isNaN(number) ||
            number == 0.0)
            return 0;

        final float d = (float) Math.ceil((float) Math.log10(number < 0 ? -number : number));
        final int pw = 1 - (int) d;
        final float magnitude = (float) Math.pow(10, pw);
        final long shifted = Math.round(number * magnitude);
        return shifted / magnitude;
    }



    /**
     * Replacement for the Math.nextUp(...) method that is only available in
     * HONEYCOMB and higher. Dat's some seeeeek sheeet.
     *
     * @param d
     * @return
     */
    public static double nextUp(double d) {
        if (d == Double.POSITIVE_INFINITY)
            return d;
        else {
            d += 0.0d;
            return Double.longBitsToDouble(Double.doubleToRawLongBits(d) +
                    ((d >= 0.0d) ? +1L : -1L));
        }
    }








}
