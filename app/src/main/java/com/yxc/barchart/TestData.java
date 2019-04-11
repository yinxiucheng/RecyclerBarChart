package com.yxc.barchart;

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
    public static List<BarEntry> createMonthEntries() {
        long timestamp = TimeUtil.changZeroOfTheDay(LocalDate.now());
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 600; i++) {
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
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            boolean isFirstDayOfMonth = TimeUtil.isFirstDayOfMonth(localDate);
            if (isFirstDayOfMonth && (i + 1) % 7 == 0) {
                type = BarEntry.TYPE_XAXIS_SPECIAL;
            } else if (isFirstDayOfMonth) {
                type = BarEntry.TYPE_XAXIS_FIRST;
            } else if ((i + 1) % 7 == 0) {
                type = BarEntry.TYPE_XAXIS_SECOND;
            }
            BarEntry barEntry = new BarEntry(i, value, timestamp, type);
            barEntry.localDate = localDate;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }

    //创建Week视图的数据
    public static List<BarEntry> createWeekEntries() {
        long timestamp = TimeUtil.changZeroOfTheDay(LocalDate.now());
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 102; i++) {
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
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            boolean isMonday = TimeUtil.isMonday(localDate);
            if (isMonday) {
                type = BarEntry.TYPE_XAXIS_FIRST;
            }
            BarEntry barEntry = new BarEntry(i, value, timestamp, type);
            barEntry.localDate = localDate;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }


    //创建 Day视图的数据
    public static List<BarEntry> createDayEntries() {
        long timestamp = TimeUtil.changZeroOfTheDay(LocalDate.now().plusDays(1));
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 600; i++) {
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
            LocalDate localDate = TimeUtil.timestampToLocalDate(timestamp);
            if (isNextDay && i % 3 == 0) {
                type = BarEntry.TYPE_XAXIS_SPECIAL;
            } else if (isNextDay) {
                type = BarEntry.TYPE_XAXIS_FIRST;
            } else if (i % 3 == 0) {
                type = BarEntry.TYPE_XAXIS_SECOND;
            }
            BarEntry barEntry = new BarEntry(i, value, timestamp, type);
            barEntry.localDate = localDate;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }


    //创建 Day视图的数据
    public static List<BarEntry> createYearEntries() {
        //获取下个月1号
        LocalDate localDate = TimeUtil.getFirstDayOfMonth(LocalDate.now().plusMonths(1));
        List<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
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
