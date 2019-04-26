package com.yxc.barchart.formatter;

import android.content.Context;

import com.yxc.barchart.R;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.formatter.ValueFormatter;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @date 2019/4/11
 */
public class XAxisMonthFormatter extends ValueFormatter {

    private  Context mContext;

    public XAxisMonthFormatter(Context context){
        this.mContext = context;
    }

    @Override
    public String getBarLabel(BarEntry barEntry) {
        LocalDate localDate = barEntry.localDate;
        if (barEntry.type == BarEntry.TYPE_XAXIS_SPECIAL || barEntry.type == BarEntry.TYPE_XAXIS_SECOND){
            return String.format(mContext.getString(R.string.str_month_formatter), localDate.getDayOfMonth());
        }
        return "";
    }
}
