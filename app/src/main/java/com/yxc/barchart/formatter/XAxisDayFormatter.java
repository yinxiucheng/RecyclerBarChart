package com.yxc.barchart.formatter;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.util.TimeUtil;

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
