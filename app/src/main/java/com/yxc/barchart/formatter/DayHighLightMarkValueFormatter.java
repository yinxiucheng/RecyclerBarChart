package com.yxc.barchart.formatter;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.util.TimeUtil;

/**
 * @author yxc
 * @date 2019/4/24
 */
public class DayHighLightMarkValueFormatter extends DefaultHighLightMarkValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public DayHighLightMarkValueFormatter(int digits) {
        super(digits);
    }


    @Override
    public String getBarLabel(BarEntry barEntry) {
        String str1 = TimeUtil.getDateStr(barEntry.timestamp, "M月d日");
        String str2 = TimeUtil.get12HourOfTheDayStr(barEntry.timestamp);
        String str3 = getFormattedValue(barEntry.getY());
        String resultStr = str1 + CONNECT_STR + str2 + CONNECT_STR + str3;
        return barEntry.getY() > 0 ? resultStr : "";
    }
}
