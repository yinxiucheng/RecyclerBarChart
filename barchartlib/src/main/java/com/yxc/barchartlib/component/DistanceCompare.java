package com.yxc.barchartlib.component;

/**
 * @author yxc
 * @date 2019/4/9
 */
public class DistanceCompare {

    public int distanceLeft;
    public int distanceRight;
    public int position;

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
}
