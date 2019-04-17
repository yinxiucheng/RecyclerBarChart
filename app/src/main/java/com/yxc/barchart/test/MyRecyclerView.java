package com.yxc.barchart.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * @author yxc
 * @date 2019/4/17
 */
public class MyRecyclerView extends RecyclerView {

    OnTouchInterceptListener listener;

    public void setOnTouchInterceptListener(OnTouchInterceptListener listener){
        this.listener = listener;
    }

    interface  OnTouchInterceptListener{
        void onIntercept(boolean intercept);
    }

    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        Toast.makeText(getContext(),"MyRecyclerView invoke", Toast.LENGTH_LONG).show();

        int  action = e.getActionMasked();
        switch (action){

            case MotionEvent.ACTION_DOWN:{

            }

            case MotionEvent.ACTION_MOVE:{
                if (null != listener){
                    listener.onIntercept(true);
                }
            }

            case MotionEvent.ACTION_UP:{

            }

        }

        return super.onTouchEvent(e);
    }


}
