package com.yxc.chartlib.barchart;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

import com.yxc.chartlib.attrs.BarChartAttrs;

/**
 * @author yxc
 * @date 2019/4/11
 */
public class SpeedRatioLinearLayoutManager extends LinearLayoutManager {

    private BarChartAttrs mAttrs;

    private double ratioSpeed;

    public SpeedRatioLinearLayoutManager(Context context, BarChartAttrs attrs) {
        super(context);
        this.mAttrs = attrs;
        setOrientation(mAttrs.layoutManagerOrientation);
        setReverseLayout(mAttrs.layoutManagerReverseLayout);
        ratioSpeed = mAttrs.ratioSpeed;
    }

    public SpeedRatioLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public SpeedRatioLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
