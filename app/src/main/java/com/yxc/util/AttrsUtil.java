package com.yxc.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.yxc.barchart.R;

public class AttrsUtil {

    public static BarChartAttrs getCustomerRecyclerAttrs(Context context, AttributeSet attributeSet) {
        BarChartAttrs attrs = new BarChartAttrs();
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BarChartRecyclerView);

        attrs.barBorderColor = ta.getColor(R.styleable.BarChartRecyclerView_barBorderColor, ColorUtil.getResourcesColor(context, R.color.black_80_transparent));
        attrs.barChartColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartColor, ColorUtil.getResourcesColor(context, R.color.pink));
        attrs.barChartEdgeColor = ta.getColor(R.styleable.BarChartRecyclerView_barChartEdgeColor, ColorUtil.getResourcesColor(context, R.color.black_20_transparent));

        attrs.barBorderWidth = ta.getDimension(R.styleable.BarChartRecyclerView_barBorderWidth, DisplayUtil.dip2px(0.5f));
        attrs.contentPaddingBottom = ta.getDimension(R.styleable.BarChartRecyclerView_contentPaddingBottom, DisplayUtil.dip2px(15));
        attrs.maxYAxisPaddingTop = ta.getDimension(R.styleable.BarChartRecyclerView_maxYAxisPaddingTop, DisplayUtil.dip2px(10));
        attrs.recyclerPaddingLeft = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingLeft, DisplayUtil.dip2px(2));
        attrs.recyclerPaddingRight = ta.getDimension(R.styleable.BarChartRecyclerView_recyclerPaddingRight, DisplayUtil.dip2px(3));

        attrs.enableBarBorder = ta.getBoolean(R.styleable.BarChartRecyclerView_enableBarBorder, true);
        attrs.enableCharValueDisplay = ta.getBoolean(R.styleable.BarChartRecyclerView_enableCharValueDisplay, true);
        attrs.enableLeftYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableLeftYAxisLabel, true);
        attrs.enableRightYAxisLabel = ta.getBoolean(R.styleable.BarChartRecyclerView_enableRightYAxisLabel, true);
        attrs.enableXAxisGridLine = ta.getBoolean(R.styleable.BarChartRecyclerView_enableXAxisGridLine, true);
        attrs.enableYAxisZero = ta.getBoolean(R.styleable.BarChartRecyclerView_enableYAxisZero, true);
        attrs.enableScrollToScale = ta.getBoolean(R.styleable.BarChartRecyclerView_enableScrollToScale, true);

        attrs.barSpace = ta.getFloat(R.styleable.BarChartRecyclerView_barSpace, 0.5f);
        attrs.ratioVelocity = ta.getFloat(R.styleable.BarChartRecyclerView_ratioVelocity, 0.5f);

        ta.recycle();
        return attrs;
    }

}
