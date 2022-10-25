package com.yxc.chartlib.recyclerchart.entrys;

import java.util.ArrayList;
import java.util.List;

public class EcgEntry extends BarEntry{
    public List<Float> values = new ArrayList<>();

    public EcgEntry(int i, float value, long timestamp, int type) {
        super(i, value, timestamp, type);
    }
}
