package com.yxc.chartlib.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yxc.chartlib.attrs.ChartAttrsUtil;
import com.yxc.chartlib.attrs.SleepChartAttrs;

/**
 * @author yxc
 * @since  2019/4/26
 */
public class SleepChartRecyclerView extends BaseChartRecyclerView {

    public SleepChartAttrs mAttrs;


    public SleepChartRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mAttrs = ChartAttrsUtil.getSleepChartAttrs(context, attrs);
    }

}
