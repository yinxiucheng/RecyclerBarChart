package com.yxc.barchartlib.entrys;

import com.yxc.barchartlib.view.CustomAnimatedDecorator;

/**
 * @author yxc
 * @date 2019/4/6
 */
public class BarEntry extends BaseBarEntry {

    public CustomAnimatedDecorator animatedDecorator;

    public BarEntry(float x, float y, long timestamp, int type){
        super(x, y, timestamp, type);
    }

    public void setAnimatedDecorator(CustomAnimatedDecorator animatedDecorator) {
        this.animatedDecorator = animatedDecorator;
    }


}
