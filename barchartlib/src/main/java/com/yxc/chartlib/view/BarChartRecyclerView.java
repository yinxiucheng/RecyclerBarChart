package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.attrs.BarChartAttrs;
import com.yxc.chartlib.attrs.ChartAttrsUtil;
import com.yxc.commonlib.util.DisplayUtil;

/**
 * @author yxc
 * @since 2019/4/10
 */
public class BarChartRecyclerView extends BaseChartRecyclerView {

    public BarChartAttrs mAttrs;

    public BarChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = ChartAttrsUtil.getBarChartRecyclerAttrs(context, attrs);
        setRecyclerViewDefaultPadding();
    }

    private void setRecyclerViewDefaultPadding() {
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        if (mAttrs.enableRightYAxisLabel) {
            paddingRight = DisplayUtil.dip2px(36);
        }
        if (mAttrs.enableLeftYAxisLabel) {
            paddingLeft = DisplayUtil.dip2px(36);
        }
        setPadding(paddingLeft, getPaddingTop(), paddingRight, getPaddingBottom());
    }


    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= mAttrs.ratioVelocity;
        velocityY *= mAttrs.ratioVelocity;
        return super.fling(velocityX, velocityY);
    }

}
