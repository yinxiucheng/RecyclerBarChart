package com.yxc.barchart.formatter;

import android.text.TextUtils;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.formatter.ValueFormatter;
import com.yxc.barchartlib.util.TimeUtil;

/**
 * @author yxc
 * @date 2019/4/14
 */
public class BarChartValueFormatter extends ValueFormatter {
    /**
     * Constructor that specifies to how many digits the value should be
     * formatted.
     */
    public BarChartValueFormatter() {
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        return super.getBarLabel(barEntry);
//        return barEntry.getY() > 0 ? TimeUtil.getDateStr(barEntry.timestamp, "MM-dd") : "";
    }

    @Override
    public String getFormattedValue(float value) {
        return super.getFormattedValue(value);
    }
}
