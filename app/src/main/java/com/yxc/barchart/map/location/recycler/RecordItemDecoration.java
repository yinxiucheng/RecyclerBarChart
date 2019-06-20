package com.yxc.barchart.map.location.recycler;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author yxc
 * @date 2019-06-20
 */
public class RecordItemDecoration extends RecyclerView.ItemDecoration {

    Paint mLinePaint;

    public RecordItemDecoration(){
        initPaint();
    }

    private void initPaint(){
        mLinePaint = new Paint();
        mLinePaint.reset();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);
        mLinePaint.setColor(Color.GRAY);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(canvas, parent, state);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            canvas.drawLine(parent.getLeft(), child.getBottom(), parent.getRight(), child.getBottom(), mLinePaint);
        }
    }
}
