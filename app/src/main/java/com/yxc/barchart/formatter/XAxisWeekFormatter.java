package com.yxc.barchart.formatter;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.util.TimeUtil;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @date 2019/4/11
 */
public class XAxisWeekFormatter extends ValueFormatter {

    @Override
    public String getBarLabel(BarEntry barEntry) {
        LocalDate localDate = barEntry.localDate;
//        return TimeUtil.getDateStr(barEntry.timestamp, "MM-dd");
        return TimeUtil.getWeekStr(localDate.getDayOfWeek());
    }
}
