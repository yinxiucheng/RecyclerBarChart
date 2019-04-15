package com.yxc.barchartlib.formatter;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.util.TimeUtil;

/**
 * @author yxc
 * @date 2019/4/13
 */
public class DefaultBarChartValueFormatter extends DefaultValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public DefaultBarChartValueFormatter(int digits) {
        super(digits);
    }


    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        return barEntry.getY() > 0 ?getFormattedValue(barEntry.getY()):"";
    }

}
