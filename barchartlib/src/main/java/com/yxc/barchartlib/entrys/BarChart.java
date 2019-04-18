package com.yxc.barchartlib.entrys;

import com.yxc.barchartlib.view.CustomAnimatedDecorator;

/**
 * @author yxc
 * @date 2019/4/18
 *
 */
public class BarChart {

    BarEntry mBayEntry;

    CustomAnimatedDecorator customAnimatedDecorator;

    public BarChart(BarEntry barEntry, CustomAnimatedDecorator customAnimatedDecorator){
        this.mBayEntry = barEntry;
        this.customAnimatedDecorator = customAnimatedDecorator;
    }

    public BarEntry getBayEntry() {
        return mBayEntry;
    }

    public void setBayEntry(BarEntry mBayEntry) {
        this.mBayEntry = mBayEntry;
    }

    public CustomAnimatedDecorator getCustomAnimatedDecorator() {
        return customAnimatedDecorator;
    }

    public void setCustomAnimatedDecorator(CustomAnimatedDecorator customAnimatedDecorator) {
        this.customAnimatedDecorator = customAnimatedDecorator;
    }

}
