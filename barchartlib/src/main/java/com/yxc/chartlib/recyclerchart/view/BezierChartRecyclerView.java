package com.yxc.chartlib.recyclerchart.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.recyclerchart.attrs.BezierChartAttrs;
import com.yxc.chartlib.recyclerchart.attrs.ChartAttrsUtil;
import com.yxc.commonlib.util.DisplayUtil;

/**
 * @author yxc
 * @since 2019/4/10
 *
 */
public class BezierChartRecyclerView extends BaseChartRecyclerView {

    public BezierChartAttrs mAttrs;

    public BezierChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = ChartAttrsUtil.getBezierChartAttrs(context, attrs);
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
