package com.yxc.chartlib.entrys;

import androidx.annotation.NonNull;
import org.joda.time.LocalDate;

/**
 * @author yxc
 * @since 2019/4/6
 */
public class SleepEntry extends Entry implements Comparable<SleepEntry> {

    public static final int TYPE_DEEP_SLEEP = 1;//一个月

    public static final int TYPE_SLUMBER = 2;//最小刻度的线

    public static final int TYPE_WAKE = 3;//7天的线，需要drawText

    public long startTimestamp;

    public long endTimestamp;

    public int type;

    public LocalDate localDate;

    public SleepEntry() {

    }

    public SleepEntry(float x, float y, long startTimestamp, long endTimestamp, int type) {
        super(x, y);
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull SleepEntry o) {// 因为这里的 SleepEntry都是连续的，不可能重叠
        return (int) (o.startTimestamp - startTimestamp);
    }

}
