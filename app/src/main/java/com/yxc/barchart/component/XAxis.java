package com.yxc.barchart.component;

import android.content.Context;
import android.graphics.Color;

import com.yxc.util.DisplayUtil;
import com.yxc.barchart.R;
import com.yxc.util.ColorUtil;

/**
 * @author yxc
 * @date 2019/4/8
 */
public class XAxis {

    private Context mContext;
    public int displayNumbers;
    public int txtSize;
    public int txtColor;

    public int barEntryTypeFirstColor;
    public int barEntryTypeSecondColor;
    public int barEntryTypeThirdColor;

    public int lastVisiblePosition = -1;
    public int firstVisiblePosition = -1;

    public XAxis(Context context, int displayNumbers) {
        this.mContext = context;
        this.displayNumbers = displayNumbers;
        txtColor = Color.BLACK;
        txtSize = DisplayUtil.dip2px(12);

        barEntryTypeFirstColor = ColorUtil.getResourcesColor(mContext, R.color.black);
        barEntryTypeSecondColor = ColorUtil.getResourcesColor(mContext, R.color.black_80_transparent);
        barEntryTypeThirdColor = ColorUtil.getResourcesColor(mContext, R.color.black_30_transparent);
    }

}
