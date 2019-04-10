package com.yxc.wediget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author yxc
 * @date 2019/4/10
 */
public class CustomerRecyclerView extends RecyclerView {

    float scale = 0.5f;

    public CustomerRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        velocityX *= scale;
        velocityY *= scale;
        return super.fling(velocityX, velocityY);
    }
}
