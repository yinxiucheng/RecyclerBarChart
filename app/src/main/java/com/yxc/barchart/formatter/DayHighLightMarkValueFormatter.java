package com.yxc.barchart.formatter;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.TimeUtil;

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
        String connectStr = "|";
        String str1 = TimeUtil.getDateStr(barEntry.timestamp, "M月d日");
        String str2 = TimeUtil.get12HourOfTheDayStr(barEntry.timestamp);
        String str3 = DecimalUtil.addComma("10000");
        String resultStr = str1 + connectStr + str2 + connectStr + str3;
        return barEntry.getY() > 0 ? resultStr : "";
    }
}
