package com.yxc.chartlib.entrys;

import java.util.List;

/**
 * @author yxc
 * @date 2019-05-13
 */
public class YAxisMaxEntries<T extends BarEntry> {

    public YAxisMaxEntries(float yAxisMaximum, List<T> visibleEntries) {
        this.yAxisMaximum = yAxisMaximum;
        this.visibleEntries = visibleEntries;
    }

    public float yAxisMaximum;
    public List<T> visibleEntries;

}
