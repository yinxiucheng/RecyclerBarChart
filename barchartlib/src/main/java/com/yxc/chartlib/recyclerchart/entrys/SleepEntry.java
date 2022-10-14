package com.yxc.chartlib.recyclerchart.entrys;

import com.yxc.chartlib.recyclerchart.entrys.model.SleepTime;

/**
 * @author yxc
 * @since 2019/4/6
 */
public class SleepEntry extends BarEntry {


    public SleepTime sleepTime;


    public SleepEntry() {
    }

    public SleepEntry(float x, float y, long timestamp, int type) {
        super(x, y, timestamp, type);
    }

}
