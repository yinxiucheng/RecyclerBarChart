package com.yxc.chartlib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yxc.chartlib.util.AttrsUtil;
import com.yxc.chartlib.util.BarChartAttrs;
import com.yxc.chartlib.util.DisplayUtil;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class BarChartRecyclerView extends RecyclerView {

    public BarChartAttrs mAttrs;

    public OnChartTouchListener onChartTouchListener;

    public BarChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = AttrsUtil.getCustomerRecyclerAttrs(context, attrs);
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

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (onChartTouchListener != null){
                onChartTouchListener.onChartGestureStart(e);
            }
        } else if (e.getAction() == MotionEvent.ACTION_UP
                || e.getAction() == MotionEvent.ACTION_CANCEL) {
            if (onChartTouchListener != null) {
                onChartTouchListener.onChartGestureEnd(e);
            }
        } else if (e.getActionMasked() == MotionEvent.ACTION_MOVE){
            if (onChartTouchListener != null) {
                onChartTouchListener.onChartGestureMovingOn(e);
            }
        }
        return super.onTouchEvent(e);
    }

    public interface OnChartTouchListener {

        void onChartGestureStart(MotionEvent e);

        void onChartGestureEnd(MotionEvent e);

        void onChartGestureMovingOn(MotionEvent e);
    }

    public void setOnChartTouchListener(OnChartTouchListener onChartTouchListener) {
        this.onChartTouchListener = onChartTouchListener;
    }

}
