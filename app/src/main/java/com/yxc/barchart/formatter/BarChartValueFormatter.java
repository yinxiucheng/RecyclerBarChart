package com.yxc.barchart.formatter;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.DefaultBarChartValueFormatter;
import com.yxc.barchartlib.util.TimeUtil;

/**
 * @author yxc
 * @date 2019/4/14
 */
public class BarChartValueFormatter extends DefaultBarChartValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     */
    public BarChartValueFormatter(){
        super(0);

    }
    public BarChartValueFormatter(int digits) {
        super(digits);
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
//        return super.getBarLabel(barEntry);
        return TimeUtil.getDateStr(barEntry.timestamp, "MM-dd");
    }

    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }
}
