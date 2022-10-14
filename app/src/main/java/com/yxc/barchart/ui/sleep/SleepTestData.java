package com.yxc.barchart.ui.sleep;

import com.yxc.chartlib.recyclerchart.attrs.BarChartAttrs;
import com.yxc.chartlib.recyclerchart.entrys.SleepEntry;
import com.yxc.chartlib.recyclerchart.entrys.SleepItemEntry;
import com.yxc.chartlib.recyclerchart.entrys.model.SleepItemTime;
import com.yxc.chartlib.recyclerchart.entrys.model.SleepTime;
import com.yxc.chartlib.recyclerchart.util.DecimalUtil;
import com.yxc.commonlib.util.TimeDateUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author yxc
 * @since 2019/4/26
 */
public class SleepTestData {

    public static final int TIME_DIVIDER_NUMBERS = 400;
    public static final int random_range = 15;

    public static List<SleepItemEntry> createSleepEntry(LocalDate localDate) {
        List<SleepItemEntry> sleepEntryList = new ArrayList<>();
        long startTimestamp = TimeDateUtil.changZeroOfTheDay(localDate);
        long endTimestamp = (startTimestamp + 8 * TimeDateUtil.TIME_HOUR);
        long timeDistance = endTimestamp - startTimestamp;//时间总的长度

        long timeUnit = timeDistance / TIME_DIVIDER_NUMBERS;//单位时间长度

        long sumTimestamp = 0;
        int sumCount = 0;
        int randomRange = random_range;

        while (sumTimestamp < timeDistance && sumCount < TIME_DIVIDER_NUMBERS) {
            SleepItemEntry barEntry = new SleepItemEntry();
            SleepItemTime sleepItemTime = new SleepItemTime();
            sleepItemTime.sleepType = getRandomType(4);//模拟的 深睡， 潜睡，眼动，醒着的四种数据。

            if (sumTimestamp == 0) {
                barEntry.type = SleepEntry.TYPE_XAXIS_FIRST;//X坐标 第一种分界线，用来定位用的
            } else {
                barEntry.type = SleepEntry.TYPE_XAXIS_SECOND;//其余的用第二种
            }

            sleepItemTime.startTimestamp = startTimestamp + sumTimestamp;

            int count = getRandomTimeRangeUnit(randomRange);
            if (sumCount + count > TIME_DIVIDER_NUMBERS) {// 衰变
                randomRange = randomRange / 2;
                continue;
            } else if (TIME_DIVIDER_NUMBERS - sumCount - count <= 2) {//处理最后的数据
                count = TIME_DIVIDER_NUMBERS - sumCount;
            }
            sumCount += count;
            long timestampRange = count * timeUnit;
            sumTimestamp += timestampRange;

            sleepItemTime.endTimestamp = sleepItemTime.startTimestamp + timestampRange;
            sleepItemTime.durationTime = sleepItemTime.endTimestamp - sleepItemTime.startTimestamp;
            barEntry.localDate = TimeDateUtil.timestampToLocalDate(sleepItemTime.endTimestamp);
            barEntry.timestamp = sleepItemTime.endTimestamp;
            barEntry.sleepItemTime = sleepItemTime;
            barEntry.setY(sleepItemTime.sleepType + 1);
            sleepEntryList.add(0, barEntry);
        }

        if (sumTimestamp < timeDistance) {
            SleepItemEntry sleepEntry = new SleepItemEntry();
            SleepItemTime sleepItemTime = new SleepItemTime();
            sleepItemTime.sleepType = getRandomType(4);
            sleepItemTime.startTimestamp = startTimestamp + sumTimestamp;
            sleepItemTime.endTimestamp = startTimestamp + timeDistance;
            sleepEntry.localDate = TimeDateUtil.timestampToLocalDate(sleepItemTime.endTimestamp);
            sleepEntry.sleepItemTime = sleepItemTime;
            sleepEntryList.add(0, sleepEntry);
        }
        return sleepEntryList;
    }

    private static int getRandomType(int num) {
        return ((int) (Math.random() * 10)) % num;
    }

    private static int getRandomTimeRangeUnit(int randomRange) {
        double random = Math.random() * 100;
        return (int) (random % randomRange + 1);
    }


    // 创建 月视图的数据
    public static List<SleepEntry> getMonthEntries(BarChartAttrs attrs, LocalDate localDate, int length, int originEntrySize) {
        List<SleepEntry> entries = new ArrayList<>();
        long timestamp = TimeDateUtil.changZeroOfTheDay(localDate);
        for (int i = originEntrySize; i < originEntrySize + length; i++) {
            if (i > originEntrySize) {
                timestamp = timestamp - TimeDateUtil.TIME_DAY;
            }
            float mult = 5;
            float value = (float) (Math.random() * 5) + mult;
            value = DecimalUtil.getDecimalFloat(DecimalUtil.TWO_LENGTH_DECIMAL, value);
            int type = SleepEntry.TYPE_XAXIS_THIRD;
            LocalDate localDateEntry = TimeDateUtil.timestampToLocalDate(timestamp);
            boolean isLastDayOfMonth = TimeDateUtil.isLastDayOfMonth(localDateEntry);
            int dayOfYear = localDateEntry.getDayOfYear();
            if (isLastDayOfMonth && (dayOfYear + 1) % attrs.xAxisScaleDistance == 0) {
                type = SleepEntry.TYPE_XAXIS_SPECIAL;
            } else if (isLastDayOfMonth) {
                type = SleepEntry.TYPE_XAXIS_FIRST;
            } else if ((dayOfYear + 1) % attrs.xAxisScaleDistance == 0) {
                type = SleepEntry.TYPE_XAXIS_SECOND;
            }

            if (TimeDateUtil.isFuture(localDateEntry)) {
                value = 0;
            }
            SleepEntry barEntry = new SleepEntry(i, value, timestamp, type);
            barEntry.localDate = localDateEntry;
            SleepTime sleepTime = createSleepTime(value, 5);
            sleepTime.startTimestamp = timestamp;
            sleepTime.endTimestamp = (long) (timestamp + value * TimeDateUtil.TIME_HOUR);
            sleepTime.sleepTime = (long) (value * TimeDateUtil.TIME_HOUR);
            barEntry.sleepTime = sleepTime;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }


    //创建Week视图的数据
    public static List<SleepEntry> createWeekEntries(LocalDate localDate, int length, int originEntrySize) {
        List<SleepEntry> entries = new ArrayList<>();
        long timestamp = TimeDateUtil.changZeroOfTheDay(localDate);

        for (int i = originEntrySize; i < originEntrySize + length; i++) {
            if (i > originEntrySize) {
                timestamp = timestamp - TimeDateUtil.TIME_DAY;
            }
            float mult = 5;
            float value = (float) (Math.random() * 5) + mult;
            value = DecimalUtil.getDecimalFloat(DecimalUtil.TWO_LENGTH_DECIMAL, value);
            int type = SleepEntry.TYPE_XAXIS_SECOND;
            LocalDate localDateEntry = TimeDateUtil.timestampToLocalDate(timestamp);
            boolean isSunday = TimeDateUtil.isSunday(localDateEntry);
            if (isSunday) {
                type = SleepEntry.TYPE_XAXIS_FIRST;
            }
            if (TimeDateUtil.isFuture(localDateEntry)) {
                value = 0;
            }
            SleepEntry barEntry = new SleepEntry(i, value, timestamp, type);
            barEntry.localDate = localDateEntry;
            SleepTime sleepTime = createSleepTime(value, 5);
            sleepTime.startTimestamp = timestamp;
            sleepTime.endTimestamp = (long) (timestamp + value * TimeDateUtil.TIME_HOUR);
            sleepTime.sleepTime = (long) (value * TimeDateUtil.TIME_HOUR);
            barEntry.sleepTime = sleepTime;
            entries.add(barEntry);
        }
        Collections.sort(entries);
        return entries;
    }


    private static SleepTime createSleepTime(float value, int num) {
        int type = getRandomType(num);
        switch (type) {
            case TYPE_6_2_1_1:
                return getSleepTime(value, new int[]{1, 1, 2, 6});
            case TYPE_5_2_2_1:
                return getSleepTime(value, new int[]{1, 2, 2, 5});
            case TYPE_5_3_1_1:
                return getSleepTime(value, new int[]{1, 1, 3, 5});
            case TYPE_4_3_2_1:
                return getSleepTime(value, new int[]{1, 2, 3, 4});
            case TYPE_4_2_2_2:
                return getSleepTime(value, new int[]{2, 2, 2, 4});
            default:
                return getSleepTime(value, new int[]{1, 1, 2, 6});
        }
    }

    private static SleepTime getSleepTime(float value, int[] ratios) {

        List<SleepItemTime> sleepItemTimeList = new ArrayList<>();

        for (int i = 0; i < ratios.length; i++) {//todo 这里是统计的数据，start、endTime不重要，也不准确没法整
            SleepItemTime sleepItemTime = new SleepItemTime();
            float durationTime = value * ratios[i] / 10f;
            sleepItemTime.durationTime = durationTime;
            sleepItemTime.sleepType = SleepItemTime.getSleepType(i);
            sleepItemTimeList.add(sleepItemTime);
        }
        SleepTime sleepTime = new SleepTime(sleepItemTimeList);
        return sleepTime;
    }

    public static final int TYPE_6_2_1_1 = 1;
    public static final int TYPE_5_2_2_1 = 2;
    public static final int TYPE_5_3_1_1 = 3;
    public static final int TYPE_4_3_2_1 = 4;
    public static final int TYPE_4_2_2_2 = 5;
}
