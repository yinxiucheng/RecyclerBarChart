package com.yxc.barchartlib.entrys;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

import java.util.Objects;

/**
 * @author yxc
 * @date 2019/4/6
 */
public class BarEntry extends Entry implements Comparable<BarEntry> {

    public static final int TYPE_XAXIS_FIRST = 1;//一个月

    public static final int TYPE_XAXIS_SECOND = 2;//7天的线，需要drawText

    public static final int TYPE_XAXIS_THIRD = 3;//最小刻度的线

    public static final int TYPE_XAXIS_SPECIAL = 4;//同时是月线以及7日分隔线

    public long timestamp;

    public boolean isSelected;

    public int type;

    public LocalDate localDate;

    public BarEntry() {
    }

    public BarEntry(float x, float y, long timestamp, int type){
        super(x, y);
        this.timestamp = timestamp;
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull BarEntry o) {
        return (int) (o.timestamp - timestamp);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarEntry barEntry = (BarEntry) o;
        return timestamp == barEntry.timestamp &&
                type == barEntry.type &&
                Objects.equals(localDate, barEntry.localDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, type, localDate);
    }
}
