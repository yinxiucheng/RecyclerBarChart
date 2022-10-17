package com.yxc.barchart.formatter;

import com.yxc.chartlib.recyclerchart.entrys.BarEntry;
import com.yxc.chartlib.recyclerchart.formatter.ValueFormatter;

/**
 * @author yxc
 * @date 2019/4/11
 */
public class XAxisHrmFormatter extends ValueFormatter {

    @Override
    public String getBarLabel(BarEntry barEntry) {
        if (barEntry.type == BarEntry.TYPE_XAXIS_FIRST || barEntry.type == BarEntry.TYPE_XAXIS_SPECIAL) {
            int index = (int)barEntry.getX();
            return index/25 + "秒";
        } else {
            return "";
        }
    }

}
