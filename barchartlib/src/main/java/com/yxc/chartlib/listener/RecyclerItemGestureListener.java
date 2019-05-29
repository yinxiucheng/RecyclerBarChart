package com.yxc.chartlib.listener;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;

import com.yxc.chartlib.barchart.SpeedRatioLayoutManager;
import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.view.BaseChartRecyclerView;
import com.yxc.chartlib.view.BaseChartRecyclerView.OnChartTouchListener;

/**
 * @author yxc
 * @since 2019/4/23
 */
public class RecyclerItemGestureListener implements RecyclerView.OnItemTouchListener {

    private OnItemGestureListener mListener;

    private boolean isLongPressing;

    private BarEntry selectBarEntry;

    private GestureDetector mGestureDetector;

    private SpeedRatioLayoutManager layoutManager;

    private RecyclerView.Adapter mAdapter;

    public RecyclerItemGestureListener(Context context, final BaseChartRecyclerView parent, final OnItemGestureListener listener) {
        mListener = listener;
        layoutManager = (SpeedRatioLayoutManager) parent.getLayoutManager();
        mAdapter =  parent.getAdapter();
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
                        if (barEntry.getY() <= 0 ) {
                            if (null != selectBarEntry){
                                selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
                            }
                            selectBarEntry = null;
                        }else if (!barEntry.equals(selectBarEntry)) {
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
                        mListener.onItemSelected(barEntry, position);
                        mListener.onItemClick(child, position);

                        if (null != mAdapter){
                            mAdapter.notifyItemChanged(position, false);
                        }
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
                        if (barEntry.getY() <= 0 ) {
                            return;
                        }else if (!barEntry.equals(selectBarEntry)) {
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
                        mListener.onItemSelected(barEntry, position);
                    }
                }
            }

            @Override
            public boolean onDown(MotionEvent e) {
                Log.d("OnItemTouch", " onDown: " + System.currentTimeMillis()/1000);
                return super.onDown(e);
            }
        });

        OnChartTouchListener onChartTouchListener = new OnChartTouchListener() {
            @Override
            public void onChartGestureStart(MotionEvent e) {

            }

            @Override
            public void onChartGestureEnd(MotionEvent e) {
                Log.d("OnItemTouch", " onChartGestureEnd： " + System.currentTimeMillis()/1000);
                isLongPressing = false;
                if (null != layoutManager) {//控制RecyclerView的滑动
                    layoutManager.resetRatioSpeed();
                }
            }

            @Override
            public void onChartGestureMovingOn(MotionEvent e) {
                Log.d("OnItemTouch", " onChartGestureMovingOn： " + System.currentTimeMillis()/1000);
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
                    int position = parent.getChildAdapterPosition(child);
                    if (position != RecyclerView.NO_POSITION){
                        BarEntry barEntry = (BarEntry) child.getTag();
                        if (barEntry.getY() <= 0 ) {
                            return;
                        }

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
                        mListener.onItemSelected(barEntry, position);
                    }
                } else {
                    //when is not longPress, normal condition reset the selected BarEntry
                    if (null != selectBarEntry && selectBarEntry.isSelected == BarEntry.TYPE_LONG_PRESS_SELECTED && isLongPressing) {
                        selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
                        selectBarEntry = null;
                        Log.d("OnItemTouch", " onItemSelected 释放 在 onChartGestureMovingOn： " + System.currentTimeMillis()/1000);
                        mListener.onItemSelected(null, -1);
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
                    if (null != selectBarEntry && selectBarEntry.isSelected != BarEntry.TYPE_UNSELECTED && !isLongPressing) {
                        if (Math.abs(dx) > 4) {
                            selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
                            selectBarEntry = null;
                            Log.d("OnItemTouch", " onItemSelected 释放 在 onScrolled： " + System.currentTimeMillis()/1000);
                            mListener.onItemSelected(null, -1);
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

    public void resetSelectedBarEntry() {
        if (null != selectBarEntry){
            selectBarEntry.isSelected = BarEntry.TYPE_UNSELECTED;
            selectBarEntry = null;
        }
    }

    public interface OnItemGestureListener {

        void onItemClick(View view, int position);

        void onLongItemClick(View view, int position);

        void onItemSelected(BarEntry barEntry, int position);

        void onScrollStateChanged(RecyclerView recyclerView, int newState);

        void onScrolled(RecyclerView recyclerView, int dx, int dy);
    }


}
