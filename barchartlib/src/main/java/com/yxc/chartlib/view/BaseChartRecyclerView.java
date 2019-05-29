package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yxc
 * @since  2019-05-10
 *
 */
public class BaseChartRecyclerView extends RecyclerView {

    public OnChartTouchListener onChartTouchListener;

    public BaseChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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


    public void setOnChartTouchListener(OnChartTouchListener onChartTouchListener) {
        this.onChartTouchListener = onChartTouchListener;
    }

    public interface OnChartTouchListener {

        void onChartGestureStart(MotionEvent e);

        void onChartGestureEnd(MotionEvent e);

        void onChartGestureMovingOn(MotionEvent e);
    }



}
