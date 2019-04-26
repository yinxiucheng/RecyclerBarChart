package com.yxc.barchart.formatter;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.DefaultBarChartValueFormatter;

/**
 * @author yxc
 * @date 2019/4/14
 */
public class ChartValueMarkFormatter extends DefaultBarChartValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     */
    public ChartValueMarkFormatter() {
        super(0);
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        return super.getBarLabel(barEntry);
//        return barEntry.getY() > 0 ? TimeUtil.getDateStr(barEntry.timestamp, "MM-dd") : "";
    }

    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }
}
