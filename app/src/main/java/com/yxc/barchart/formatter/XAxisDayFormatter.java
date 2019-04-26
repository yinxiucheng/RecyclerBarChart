package com.yxc.barchart.formatter;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.util.TimeUtil;

/**
 * @author yxc
 * @date 2019/4/11
 */
public class XAxisDayFormatter extends ValueFormatter {

    @Override
    public String getBarLabel(BarEntry barEntry) {
        if (barEntry.type == BarEntry.TYPE_XAXIS_SECOND
                || barEntry.type == BarEntry.TYPE_XAXIS_SPECIAL) {
            return TimeUtil.getHourOfTheDay(barEntry.timestamp) + "时";
        } else {
            return "";
        }
    }

}
