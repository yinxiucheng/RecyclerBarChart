package com.yxc.barchart.ui.sleep;

import com.yxc.chartlib.entrys.SleepEntry;
import com.yxc.commonlib.util.TimeUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yxc
 * @since  2019/4/26
 */
public class SleepTestData {

    public static final int TIME_DIVIDER_NUMBERS = 400;
    public static final int random_range = 10;

    //todo create dataList
    public static List<SleepEntry> createSleepEntry(LocalDate localDate) {

        List<SleepEntry> sleepEntryList = new ArrayList<>();
        long startTimestamp = TimeUtil.changZeroOfTheDay(localDate) - TimeUtil.TIME_HOUR;

        long endTimestamp = (startTimestamp + 8 * TimeUtil.TIME_HOUR);
        long timeDistance = endTimestamp - startTimestamp;//时间总的长度
        long timeUnit = timeDistance / TIME_DIVIDER_NUMBERS;//单位时间长度

        long sumTimestamp = 0;
        int sumCount = 0;
        int randomRange = random_range;

        while (sumTimestamp < timeDistance && sumCount < TIME_DIVIDER_NUMBERS) {
            SleepEntry sleepEntry = new SleepEntry();
            sleepEntry.type = getRandomType();//模拟的 深睡， 潜睡，醒着的三种数据。
            sleepEntry.startTimestamp = startTimestamp + sumTimestamp;

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

            sleepEntry.endTimestamp = sleepEntry.startTimestamp + timestampRange;
            sleepEntry.localDate = TimeUtil.timestampToLocalDate(sleepEntry.endTimestamp);
            sleepEntryList.add(0, sleepEntry);
        }

        if (sumTimestamp < timeDistance){
            SleepEntry sleepEntry = new SleepEntry();
            sleepEntry.type = getRandomType();
            sleepEntry.startTimestamp = startTimestamp + sumTimestamp;
            sleepEntry.endTimestamp = startTimestamp + timeDistance;
            sleepEntry.localDate = TimeUtil.timestampToLocalDate(sleepEntry.endTimestamp);
            sleepEntryList.add(0, sleepEntry);
        }
        return sleepEntryList;
    }


    private static int getRandomType() {
        return ((int) (Math.random() * 10)) % 3 + 1;
    }

    private static int getRandomTimeRangeUnit(int randomRange) {
        double random = Math.random() * 100;
        return (int) (random % randomRange + 1);
    }

}
