package com.yxc.barchart;

import android.util.Log;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.util.TimeUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class TestData {

    public static final int VIEW_DAY = 0;
    public static final int VIEW_WEEK = 1;
    public static final int VIEW_MONTH = 2;
    public static final int VIEW_YEAR = 3;

    // 创建 月视图的数据
    public static List<BarEntry> getMonthEntries(LocalDate localDate, int length) {
        long timestamp = TimeUtil.changZeroOfTheDay(localDate);
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                timestamp = timestamp - TimeUtil.TIME_DAY;
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);
            int type = BarEntry.TYPE_XAXIS_THIRD;
            LocalDate localDateEntry = TimeUtil.timestampToLocalDate(timestamp);
            boolean isFirstDayOfMonth = TimeUtil.isFirstDayOfMonth(localDateEntry);
            int dayOfYear = localDateEntry.getDayOfYear();
            Log.d("TestData", "dayOfYear:" + dayOfYear + " localDate:" + localDateEntry);
            if (isFirstDayOfMonth && (dayOfYear+1) % 6 == 0) {
                type = BarEntry.TYPE_XAXIS_SPECIAL;
            } else if (isFirstDayOfMonth) {
                type = BarEntry.TYPE_XAXIS_FIRST;
            } else if ((dayOfYear+1) % 6 == 0) {
                type = BarEntry.TYPE_XAXIS_SECOND;
            }
            BarEntry barEntry = new BarEntry(i, value, timestamp, type);
            barEntry.localDate = localDateEntry;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }

    //创建Week视图的数据
    public static List<BarEntry> createWeekEntries(LocalDate localDate, int length) {
        long timestamp = TimeUtil.changZeroOfTheDay(localDate);
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                timestamp = timestamp - TimeUtil.TIME_DAY;
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);
            int type = BarEntry.TYPE_XAXIS_SECOND;
            LocalDate localDateEntry = TimeUtil.timestampToLocalDate(timestamp);
            boolean isMonday = TimeUtil.isMonday(localDateEntry);
            if (isMonday) {
                type = BarEntry.TYPE_XAXIS_FIRST;
            }
            BarEntry barEntry = new BarEntry(i, value, timestamp, type);
            barEntry.localDate = localDateEntry;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }


    //创建 Day视图的数据
    public static List<BarEntry> createDayEntries(long timestamp, int length) {
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                timestamp = timestamp - TimeUtil.TIME_HOUR;
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);
            int type = BarEntry.TYPE_XAXIS_THIRD;
            boolean isNextDay = TimeUtil.isNextDay(timestamp);
            LocalDate localDateEntry = TimeUtil.timestampToLocalDate(timestamp);
            int hourOfTheDay = TimeUtil.getHourOfTheDay(timestamp);
            if (isNextDay && (hourOfTheDay + 1) % 3 == 0) {
                type = BarEntry.TYPE_XAXIS_SPECIAL;
            } else if (isNextDay) {
                type = BarEntry.TYPE_XAXIS_FIRST;
            } else if ((hourOfTheDay + 1) % 3 == 0) {
                type = BarEntry.TYPE_XAXIS_SECOND;
            }
            BarEntry barEntry = new BarEntry(i, value, timestamp, type);
            barEntry.localDate = localDateEntry;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }


    //创建 Day视图的数据
    public static List<BarEntry> createYearEntries(LocalDate localDateParams, int length) {
        //获取下个月1号
        LocalDate localDate = TimeUtil.getFirstDayOfMonth(localDateParams);
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                localDate = localDate.minusMonths(1);
            }
            float mult = 10;
            float value = 0;
            if (i > 500) {
                value = (float) (Math.random() * 30000) + mult;
            } else if (i > 400) {
                value = (float) (Math.random() * 3000) + mult;
            } else if (i > 300) {
                value = (float) (Math.random() * 20000) + mult;
            } else if (i > 200) {
                value = (float) (Math.random() * 5000) + mult;
            } else if (i > 100) {
                value = (float) (Math.random() * 300) + mult;
            } else {
                value = (float) (Math.random() * 6000) + mult;
            }
            value = Math.round(value);

            int type = BarEntry.TYPE_XAXIS_SECOND;
            boolean isNextYear = TimeUtil.isAnotherYear(localDate);
            if (isNextYear) {
                type = BarEntry.TYPE_XAXIS_FIRST;
            }
            long timestamp = TimeUtil.changZeroOfTheDay(localDate);
            BarEntry barEntry = new BarEntry(i, value, timestamp, type);
            barEntry.localDate = localDate;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }

}
