package com.yxc.barchart.test;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author yxc
 * @date 2019/4/17
 */
public class SecondViewGroup extends FrameLayout {

    public SecondViewGroup(Context context) {
        super(context);
    }

    public SecondViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SecondViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SecondViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("onTouchEvent", "SecondViewGroup invoke ");
        return super.onTouchEvent(event);
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        return false;
//    }

}
