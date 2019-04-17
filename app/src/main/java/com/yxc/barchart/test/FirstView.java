package com.yxc.barchart.test;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author yxc
 * @date 2019/4/17
 */
public class FirstView extends View {

    public FirstView(Context context) {
        super(context);
    }

    public FirstView(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstView(Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("onTouchEvent", " FirstView invoke");
        return super.onTouchEvent(event);
    }
}
