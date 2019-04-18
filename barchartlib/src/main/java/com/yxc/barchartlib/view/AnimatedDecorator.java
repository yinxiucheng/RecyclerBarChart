package com.yxc.barchartlib.view;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

/**
 * @author yxc
 * @date 2019/4/18
 */
public class AnimatedDecorator extends RecyclerView.ItemDecoration {


    HashMap<Integer, CustomAnimatedDecorator> mAnimatorMap;


    public AnimatedDecorator(HashMap<Integer, CustomAnimatedDecorator> map) {
        this.mAnimatorMap = map;
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        boolean mustInvalidate = false;
        if (parent != null && parent.getChildCount() > 0) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(child);
                CustomAnimatedDecorator mDrawable = mAnimatorMap.get(position);
                if (position != RecyclerView.NO_POSITION) {
                    mustInvalidate = true;
                    drawView(canvas, mDrawable, child);
                }
            }
            if (mustInvalidate) parent.invalidate();
        }
    }


    private void drawView(Canvas canvas, AnimatedDecoratorDrawable drawable, View child) {
        canvas.save();
        canvas.translate(child.getLeft(), child.getTop());
        drawable.draw(canvas);
        canvas.restore();
    }
}
