package com.yxc.barchartlib.listener;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.yxc.barchartlib.entrys.BarEntry;
import com.yxc.barchartlib.view.BarChartAdapter;
import com.yxc.barchartlib.view.BarChartRecyclerView;
import com.yxc.barchartlib.view.BarChartRecyclerView.onChartTouchListener;
import com.yxc.barchartlib.view.SpeedRatioLinearLayoutManager;

/**
 * @author yxc
 * @since 2019/4/23
 */
public class RecyclerItemGestureListener implements RecyclerView.OnItemTouchListener {

    private OnItemGestureListener mListener;

    private boolean isLongPressing;

    private BarEntry selectBarEntry;

    private GestureDetector mGestureDetector;

    private SpeedRatioLinearLayoutManager layoutManager;

    private BarChartAdapter mAdapter;

    public RecyclerItemGestureListener(Context context, final BarChartRecyclerView parent, final OnItemGestureListener listener) {
        mListener = listener;
        layoutManager = (SpeedRatioLinearLayoutManager) parent.getLayoutManager();
        mAdapter = (BarChartAdapter) parent.getAdapter();
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                View child = parent.findChildViewUnder(x, y);
                float parentRight = parent.getWidth() - parent.getPaddingRight();
                if (child != null && mListener != null) {
                    float reservedWidth = child.getWidth() / 2.0f;
                    if (x < parent.getPaddingLeft() + reservedWidth || x > parentRight - reservedWidth) {
                        return false;
                    }
                    final int position = parent.getChildAdapterPosition(child);
                    if (position != RecyclerView.NO_POSITION) {
                        BarEntry barEntry = (BarEntry) child.getTag();
                        if (!barEntry.equals(selectBarEntry)) {
                            //重置原来的SelectBarEntry
                            if (null != selectBarEntry){
                                selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
                            }
                            selectBarEntry = barEntry;
                            barEntry.isSelected = BarEntry.TYPE_SINGLE_TAP_UP_SELECTED;
                        } else {
                            selectBarEntry = null;
                            barEntry.isSelected = BarEntry.TYPE_UNSELECTED;//再次被点击
                        }

                        if (null != mAdapter){
                            mAdapter.notifyItemChanged(position, false);
                        }

                        mListener.onItemClick(child, position);
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                View child = parent.findChildViewUnder(x, y);
                float parentRight = parent.getWidth() - parent.getPaddingRight();

                isLongPressing = true;
                if (null != layoutManager) {
                    layoutManager.setRatioSpeed(0);
                }
                if (child != null && mListener != null) {
                    float reservedWidth = child.getWidth() / 2.0f;
                    if (x < parent.getPaddingLeft() + reservedWidth || x > parentRight - reservedWidth) {
                        return;
                    }
                    final int position = parent.getChildAdapterPosition(child);
                    if (position != RecyclerView.NO_POSITION) {
                        BarEntry barEntry = (BarEntry) child.getTag();
                        if (!barEntry.equals(selectBarEntry)) {
                            //重置原来的SelectBarEntry
                            if (null != selectBarEntry){
                                selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
                            }
                            selectBarEntry = barEntry;
                            barEntry.isSelected = BarEntry.TYPE_LONG_PRESS_SELECTED;
                        } else {
                            selectBarEntry = null;
                            barEntry.isSelected = BarEntry.TYPE_UNSELECTED;//再次被点击
                        }

                        if (null != mAdapter){
                            mAdapter.notifyItemChanged(position, false);
                        }

                        mListener.onLongItemClick(child, position);
                        return;
                    }
                }
            }
        });

        BarChartRecyclerView.onChartTouchListener onChartTouchListener = new onChartTouchListener() {

            @Override
            public void onChartGestureStart(MotionEvent e) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent e) {
                isLongPressing = false;
                if (null != layoutManager) {
                    layoutManager.resetRatioSpeed();
                }
            }

            @Override
            public void onChartGestureMovingOn(MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                View child;
                child = parent.findChildViewUnder(x, y);
                float parentRight = parent.getWidth() - parent.getPaddingRight();
                if (child != null && isLongPressing) {
                    // longPress not action end, then moving the item is touched should be set selected
                    float reservedWidth = child.getWidth() / 2.0f;
                    //deal with the condition of the edge
                    if (x < parent.getPaddingLeft() + reservedWidth || x > parentRight - reservedWidth) {
                        return;
                    }
                    BarEntry barEntry = (BarEntry) child.getTag();
                    int position = parent.getChildAdapterPosition(child);
                    if (!barEntry.equals(selectBarEntry)) {
                        if (selectBarEntry != null){
                            selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
                        }
                        selectBarEntry = barEntry;
                        barEntry.isSelected = BarEntry.TYPE_LONG_PRESS_SELECTED;
                        if (null != mAdapter){
                            mAdapter.notifyItemChanged(position, false);
                        }
                    }
                } else {
                    //when is not longPress, normal condition reset the selected BarEntry
                    if (null != selectBarEntry && selectBarEntry.isSelected == BarEntry.TYPE_LONG_PRESS_SELECTED) {
                        selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
                        selectBarEntry = null;
                    }
                }
            }
        };
        parent.setOnChartTouchListener(onChartTouchListener);


        OnScrollListener scrollListener = new OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null != mListener) {
                    mListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (null != mListener) {
                    if (null != selectBarEntry && selectBarEntry.isSelected == 1) {
                        if (Math.abs(dx) > 2) {
                            selectBarEntry.isSelected = 0;
                            selectBarEntry = null;
                        }
                    }
                    mListener.onScrolled(recyclerView, dx, dy);
                }
            }
        };

        parent.addOnScrollListener(scrollListener);

    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView view, @NonNull MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(@NonNull RecyclerView parent, @NonNull MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public interface OnItemGestureListener {

        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);

        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }


}
