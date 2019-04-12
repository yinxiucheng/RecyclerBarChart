package com.yxc.barchartlib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

import com.yxc.barchartlib.util.AttrsUtil;
import com.yxc.barchartlib.util.BarChartAttrs;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class BarChartRecyclerView extends RecyclerView {

    public BarChartAttrs mAttrs;

    public BarChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = AttrsUtil.getCustomerRecyclerAttrs(context, attrs);
        int slop = ViewConfigurationCompat.getScaledPagingTouchSlop(ViewConfiguration.get(context));
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= mAttrs.ratioVelocity;
        velocityY *= mAttrs.ratioVelocity;
        return super.fling(velocityX, velocityY);
    }

}
