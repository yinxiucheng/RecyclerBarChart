package com.yxc.barchart.formatter;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.DefaultValueFormatter;
import com.yxc.barchartlib.util.DecimalUtil;
import com.yxc.barchartlib.util.TimeUtil;

/**
 * @author yxc
 * @date 2019/4/24
 */
public class YearHighLightMarkValueFormatter extends DefaultValueFormatter {
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
        String connectStr = "|";
        String str1 = TimeUtil.getDateStr(barEntry.timestamp, "M月");
        String str2 = TimeUtil.getDateStr(barEntry.timestamp, "yyyy年");
        String str3 = "日均";
        String str4 = DecimalUtil.addComma("10000");
        String resultStr = str1 + connectStr + str2 + connectStr + str3 + str4;
        return barEntry.getY() > 0 ?resultStr:"";
    }


}
