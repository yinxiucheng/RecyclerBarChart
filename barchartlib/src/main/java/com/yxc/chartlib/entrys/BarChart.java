package com.yxc.chartlib.entrys;

import android.graphics.RectF;

import com.yxc.chartlib.view.CustomAnimatedDecorator;

import java.util.Objects;

/**
 * @author yxc
 * @date 2019/4/18
 *
 */
public class BarChart {

    public BarEntry mBayEntry;

    public RectF rectF;

    public boolean isSelected;

    public CustomAnimatedDecorator customAnimatedDecorator;

    public BarChart(BarEntry barEntry){
        this.mBayEntry = barEntry;
    }


    public BarEntry getBayEntry() {
        return mBayEntry;
    }

    public void setBayEntry(BarEntry mBayEntry) {
        this.mBayEntry = mBayEntry;
    }


    public RectF getRectF() {
        return rectF;
    }

    public void setRectF(RectF rectF) {
        this.rectF = rectF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BarChart barChart = (BarChart) o;
        return isSelected == barChart.isSelected &&
                Objects.equals(mBayEntry, barChart.mBayEntry) &&
                Objects.equals(rectF, barChart.rectF);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mBayEntry, rectF, isSelected);
    }


    public CustomAnimatedDecorator getCustomAnimatedDecorator() {
        return customAnimatedDecorator;
    }

    public void setCustomAnimatedDecorator(CustomAnimatedDecorator customAnimatedDecorator) {
        this.customAnimatedDecorator = customAnimatedDecorator;
    }
}
