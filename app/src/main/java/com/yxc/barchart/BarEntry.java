package com.yxc.barchart;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @date 2019/4/6
 */
public class BarEntry implements Comparable<BarEntry>{

    public static final int TYPE_FIRST = 1;

    public static final int TYPE_SECOND = 2;

    public static final int TYPE_THIRD = 3;

    public static final int TYPE_SPECIAL = 4;//同时是月线以及7日分隔线

    float value;
    long timestamp;
    int type;
    public LocalDate localDate;


    public float currentHeight = 0f;

    public BarEntry(){
    }

    public BarEntry(float value, long timestamp, int type) {
        this.value = value;
        this.timestamp = timestamp;
        this.type = type;
    }

    @Override
    public int compareTo(@NonNull BarEntry o) {
        return (int) (timestamp - o.timestamp);
    }


    public float getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(float currentHeight) {
        this.currentHeight = currentHeight;
    }

}
