package com.yxc.chartlib.sleepchart;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.yxc.chartlib.attrs.SleepChartAttrs;

/**
 * @author yxc
 * @since  2019/4/11
 */
public class SleepLinearLayoutManager extends LinearLayoutManager {

    private SleepChartAttrs mAttrs;

    private double ratioSpeed;

    public SleepLinearLayoutManager(Context context, SleepChartAttrs attrs) {
        super(context);
        this.mAttrs = attrs;
        setOrientation(mAttrs.layoutManagerOrientation);
        setReverseLayout(mAttrs.layoutManagerReverseLayout);
        ratioSpeed = mAttrs.ratioSpeed;
    }

    public SleepLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SleepLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        //屏蔽之后无滑动效果，证明滑动的效果就是由这个函数实现
        int a = super.scrollHorizontallyBy((int) (ratioSpeed * dx), recycler, state);
        if (a == (int) (ratioSpeed * dx)) {
            return dx;
        }
        return a;
    }

    public double getRatioSpeed() {
        return ratioSpeed;
    }

    public void setRatioSpeed(double ratioSpeed) {
        this.ratioSpeed = ratioSpeed;
    }

    public void resetRatioSpeed(){
        this.ratioSpeed = mAttrs.ratioSpeed;
    }

}
