package com.yxc.barchart.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * @author yxc
 * @date 2019/4/17
 */
public class FirstViewGroup extends FrameLayout implements MyRecyclerView.OnTouchInterceptListener {

    private boolean mIntercept;

    public FirstViewGroup(Context context) {
        super(context);
    }

    public FirstViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FirstViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FirstViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mIntercept;
    }

    @Override
    public void onIntercept(boolean intercept) {
        mIntercept = intercept;
    }


}
