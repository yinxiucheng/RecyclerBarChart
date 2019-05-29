package com.yxc.chartlib.component;

import com.yxc.chartlib.entrys.BarEntry;

/**
 * @author yxc
 * @since  2019/4/9
 *
 */
public class DistanceCompare<T extends BarEntry> {

    public int distanceLeft;
    public int distanceRight;
    public int position;
    public T barEntry;

    public DistanceCompare(int distanceLeft, int distanceRight){
        this.distanceLeft = distanceLeft;
        this.distanceRight = distanceRight;
    }

    //月线靠近左边
    public boolean isNearLeft(){
        return distanceLeft < distanceRight;
    }

    //月线靠近左边
    public boolean isNearRight(){
        return distanceLeft > distanceRight;
    }

    @Override
    public String toString() {
        return "DistanceCompare{" +
                "distanceLeft=" + distanceLeft +
                ", distanceRight=" + distanceRight +
                ", position=" + position +
                '}';
    }


    public void setPosition(int position) {
        this.position = position;
    }

    public void setBarEntry(T barEntry) {
        this.barEntry = barEntry;
    }

}
