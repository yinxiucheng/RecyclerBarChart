package com.yxc.barchartlib.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author yxc
 * @date 2019/4/18
 */
public class AnimatedDecorator extends RecyclerView.ItemDecoration {

    public enum Side {
        TOP,
        BOTTOM
    }

    AnimatedDecoratorDrawable mDrawable;
    Side mSide;

    public AnimatedDecorator(AnimatedDecoratorDrawable drawable, Side side) {
        this.mDrawable = drawable;
        this.mSide = side;
    }

//    @Override
//    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
//                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
//
//        int position = parent.getChildAdapterPosition(view);
//        if (mSide == Side.TOP) {
//            outRect.set(0, 0, 0, 0);
//        } else {
//            outRect.set(0, 0, 0, mDrawable.height);
//        }
//    }


    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        boolean mustInvalidate = false;
        if (parent != null && parent.getChildCount() > 0) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                View child = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(child);
                if (position != RecyclerView.NO_POSITION) {
                    mustInvalidate = true;
                    drawView(canvas, mDrawable, child);
                }
            }
//            if (mustInvalidate) parent.invalidate();
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
