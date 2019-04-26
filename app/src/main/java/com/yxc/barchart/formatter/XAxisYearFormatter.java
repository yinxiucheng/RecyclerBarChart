package com.yxc.barchart.formatter;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.ValueFormatter;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @date 2019/4/11
 */
public class XAxisYearFormatter extends ValueFormatter {

    @Override
    public String getBarLabel(BarEntry barEntry) {
        LocalDate localDate = barEntry.localDate;
        return Integer.toString(localDate.getMonthOfYear());
    }
}
