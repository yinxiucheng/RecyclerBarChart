package com.yxc.chartlib.sleepchart;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yxc.chartlib.attrs.SleepChartAttrs;
import com.yxc.chartlib.formatter.DefaultHighLightMarkValueFormatter;
import com.yxc.chartlib.formatter.ValueFormatter;
import com.yxc.chartlib.render.SleepChartRender;

/**
 * @author yxc
 * @date 2019/4/6
 * <p>
 * 这个ItemDecoration 是睡眠专用的
 */
public class SleepChartItemDecoration extends RecyclerView.ItemDecoration {
    private int mOrientation;
    private SleepChartAttrs mBarChartAttrs;
    private SleepChartRender sleepChartRender;

    private ValueFormatter mHighLightValueFormatter;
    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    public SleepChartItemDecoration(SleepChartAttrs barChartAttrs) {
        this.mOrientation = barChartAttrs.layoutManagerOrientation;
        this.mBarChartAttrs = barChartAttrs;
        this.mHighLightValueFormatter = new DefaultHighLightMarkValueFormatter(0);
        this.sleepChartRender = new SleepChartRender(mBarChartAttrs, mHighLightValueFormatter);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(canvas, parent, state);
        if (mOrientation == HORIZONTAL_LIST) {
            sleepChartRender.drawSleepChart(canvas, parent);//draw SleepChart
            sleepChartRender.drawHighLight(canvas, parent);
        } else if (mOrientation == VERTICAL_LIST) {//暂时不支持
            //drawHorizontalLine(c, parent, mXAxis);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    }

    public void setHighLightValueFormatter(ValueFormatter highLightValueFormatter){
        this.mHighLightValueFormatter = highLightValueFormatter;
        this.sleepChartRender.setHighLightValueFormatter(mHighLightValueFormatter);
    }
}
