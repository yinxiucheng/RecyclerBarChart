package com.yxc.chartlib.entrys;

import com.yxc.chartlib.entrys.model.SleepItemTime;

/**
 * @author yxc
 * @since 2019/4/6
 */
public class SleepItemEntry extends BarEntry {


    public SleepItemTime sleepItemTime;


    public SleepItemEntry() {
    }

    public SleepItemEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
    }

}
