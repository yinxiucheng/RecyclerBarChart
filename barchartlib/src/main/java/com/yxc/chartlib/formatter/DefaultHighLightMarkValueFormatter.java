package com.yxc.chartlib.formatter;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.commonlib.util.TimeDateUtil;

/**
 * @author yxc
 * @date 2019/4/24
 */
public class DefaultHighLightMarkValueFormatter extends DefaultValueFormatter {
    public static final String CONNECT_STR = "&";
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     *
     * @param digits
     */
    public DefaultHighLightMarkValueFormatter(int digits) {
        super(digits);
    }

    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        String str1 = TimeDateUtil.getDateStr(barEntry.timestamp, "M月d日");
        String str2 = TimeDateUtil.getDateStr(barEntry.timestamp, "yyyy年");
        String str3 = getFormattedValue(barEntry.getY());
        String resultStr = str1 + CONNECT_STR + str2 + CONNECT_STR + str3;
        return barEntry.getY() > 0 ? resultStr : "";
    }


}
