package com.yxc.barchart;

import android.content.Context;
import android.graphics.Color;

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

    public XAxis(Context context, int displayNumbers) {
        this.mContext = context;
        this.displayNumbers = displayNumbers;
        txtColor = Color.BLACK;
        txtSize = DisplayUtil.dip2px(14);

        barEntryTypeFirstColor = ColorUtil.getResourcesColor(mContext, R.color.black);
        barEntryTypeSecondColor = ColorUtil.getResourcesColor(mContext, R.color.black_80_transparent);
        barEntryTypeThirdColor = ColorUtil.getResourcesColor(mContext, R.color.black_30_transparent);
    }

}
