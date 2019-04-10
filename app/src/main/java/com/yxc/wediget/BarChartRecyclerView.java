package com.yxc.wediget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.yxc.util.AttrsUtil;
import com.yxc.util.BarChartAttrs;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class BarChartRecyclerView extends RecyclerView {

    public BarChartAttrs mAttrs;

    public BarChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = AttrsUtil.getCustomerRecyclerAttrs(context, attrs);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= mAttrs.ratioVelocity;
        velocityY *= mAttrs.ratioVelocity;
        return super.fling(velocityX, velocityY);
    }

}
