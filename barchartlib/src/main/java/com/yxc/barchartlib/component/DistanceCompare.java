package com.yxc.barchartlib.component;

import com.yxc.barchartlib.entrys.BarEntry;

/**
 * @author yxc
 * @date 2019/4/9
 */
public class DistanceCompare {

    public int distanceLeft;
    public int distanceRight;
    public int position;
    public BarEntry barEntry;

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

    public void setBarEntry(BarEntry barEntry) {
        this.barEntry = barEntry;
    }

}
