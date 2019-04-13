package com.yxc.barchartlib.formatter;

/**
 * @author yxc
 * @date 2019/4/13
 */
public class BarChartValueFormatter extends DefaultValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public BarChartValueFormatter(int digits) {
        super(digits);
    }


    @Override
    public String getFormattedValue(float value) {

        // put more logic here ...
        // avoid memory allocations here (for performance reasons)
        return super.getFormattedValue(value);
    }

}
