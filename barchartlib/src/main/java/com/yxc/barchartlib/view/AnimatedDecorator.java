package com.yxc.barchartlib.view;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yxc.barchartlib.util.DisplayUtil;

import java.util.HashMap;

/**
 * @author yxc
 * @date 2019/4/18
 */
public class AnimatedDecorator extends RecyclerView.ItemDecoration {

    public enum Side {
        TOP,
        BOTTOM
    }

//    CustomAnimatedDecorator mDrawable;
    Side mSide;
    HashMap<Integer, CustomAnimatedDecorator> mAnimatorMap;


    public AnimatedDecorator(HashMap<Integer, CustomAnimatedDecorator> map, Side side) {
        this.mAnimatorMap = map;
//        this.mDrawable = new CustomAnimatedDecorator(height, DisplayUtil.dip2px(30));
        this.mSide = side;
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        boolean mustInvalidate = false;
        if (parent != null && parent.getChildCount() > 0) {
            int height = parent.getHeight();
            for (int i = 0; i < parent.getChildCount(); i++) {

//                mDrawable.currentTop.setValue(height/2);
//                mDrawable.currentTop.end = height/2;
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
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
        canvas.save();
//        if (mSide == Side.TOP) {
//            canvas.translate(child.getLeft(), (child.getTop() - params.topMargin - drawable.height));
//        } else {
//            canvas.translate(child.getLeft(), (child.getBottom() + params.bottomMargin));
//        }
        canvas.translate(child.getLeft(), child.getTop());
        drawable.draw(canvas);
        canvas.restore();
    }
}
