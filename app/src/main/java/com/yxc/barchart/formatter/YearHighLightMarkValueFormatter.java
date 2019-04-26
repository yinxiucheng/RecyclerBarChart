package com.yxc.barchart.formatter;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.commonlib.util.TimeUtil;

/**
 * @author yxc
 * @date 2019/4/24
 */
public class YearHighLightMarkValueFormatter extends DefaultHighLightMarkValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public YearHighLightMarkValueFormatter(int digits) {
        super(digits);
    }

    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        String str1 = TimeUtil.getDateStr(barEntry.timestamp, "M月");
        String str2 = TimeUtil.getDateStr(barEntry.timestamp, "yyyy年");
        String str3 = "日均";
        String str4 = getFormattedValue(barEntry.getY());
        String resultStr = str1 + CONNECT_STR + str2 + CONNECT_STR + str3 + CONNECT_STR + str4;
        return barEntry.getY() > 0 ?resultStr:"";
    }
}
