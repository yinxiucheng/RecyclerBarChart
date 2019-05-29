package com.yxc.barchart.formatter;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.commonlib.util.TimeDateUtil;

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
        return TimeDateUtil.getWeekStr(localDate.getDayOfWeek());
    }
}
