package com.yxc.chartlib.entrys.model;

import java.util.List;

/**
 * @author yxc
 * @since 2019-05-09
 */
public class SleepTime {

    public List<SleepItemTime> sleepItemList;

    public long startTimestamp;//入睡时间点
    public long endTimestamp;//醒来时间点

    public long sleepTime;//睡的时间

    public SleepTime(List<SleepItemTime> sleepTimeList) {
        this.sleepItemList = sleepTimeList;
    }

    public long getSleepTime() {
        return endTimestamp - startTimestamp;
    }

}
