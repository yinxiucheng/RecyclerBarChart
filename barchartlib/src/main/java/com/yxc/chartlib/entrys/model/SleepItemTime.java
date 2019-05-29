package com.yxc.chartlib.entrys.model;

import android.content.Context;

import com.yxc.chartlib.R;
import com.yxc.chartlib.attrs.SleepChartAttrs;
import com.yxc.commonlib.util.ColorUtil;

/**
 * @author yxc
 * @since 2019-05-09
 */
public class SleepItemTime {

    public static final int TYPE_DEEP_SLEEP = 3;//深睡

    public static final int TYPE_SLUMBER = 2;//潜睡

    public static final int TYPE_EYES_MOVE = 1;//快速眼动

    public static final int TYPE_WAKE = 0;//清醒

    public float durationTime; //这里是 浮点型的小数。h为单位的。

    public int sleepType;

    public long startTimestamp;//入睡时间点

    public long endTimestamp;//醒来时间点

    public SleepItemTime() {

    }

    public static int getSleepTypeColor(Context context, int sleepType) {
        switch (sleepType) {
            case TYPE_DEEP_SLEEP:
                return ColorUtil.getResourcesColor(context, R.color.deep_sleep_color);
            case TYPE_SLUMBER:
                return ColorUtil.getResourcesColor(context, R.color.slumber_color);
            case TYPE_EYES_MOVE:
                return ColorUtil.getResourcesColor(context, R.color.eye_move_color);
            case TYPE_WAKE:
                return ColorUtil.getResourcesColor(context, R.color.wake_color);
            default:
                return ColorUtil.getResourcesColor(context, R.color.deep_sleep_color);
        }
    }


    public int getSleepTypeColor(Context context) {
        switch (sleepType) {
            case TYPE_DEEP_SLEEP:
                return ColorUtil.getResourcesColor(context, R.color.deep_sleep_color);
            case TYPE_SLUMBER:
                return ColorUtil.getResourcesColor(context, R.color.slumber_color);
            case TYPE_EYES_MOVE:
                return ColorUtil.getResourcesColor(context, R.color.eye_move_color);
            case TYPE_WAKE:
                return ColorUtil.getResourcesColor(context, R.color.wake_color);
            default:
                return ColorUtil.getResourcesColor(context, R.color.deep_sleep_color);
        }
    }


    public String getSleepTypeName() {
        switch (sleepType) {
            case TYPE_DEEP_SLEEP:
                return "深睡";
            case TYPE_SLUMBER:
                return "浅睡";
            case TYPE_EYES_MOVE:
                return "快速眼动";
            case TYPE_WAKE:
                return "清醒";
            default:
                return "浅睡";
        }
    }

    public long getSleepTime() {
        return endTimestamp - startTimestamp;
    }


    public int getChartColor(SleepChartAttrs mChartAttrs) {
        switch (sleepType) {
            case TYPE_DEEP_SLEEP:
                return mChartAttrs.deepSleepColor;
            case TYPE_SLUMBER:
                return mChartAttrs.slumberColor;
            case TYPE_EYES_MOVE:
                return mChartAttrs.eyeMoveColor;
            case TYPE_WAKE:
                return mChartAttrs.weakColor;
            default:
                return mChartAttrs.deepSleepColor;
        }
    }

    public static int getSleepType(int position) {
        if (position == 0) {
            return TYPE_WAKE;
        } else if (position == 1) {
            return TYPE_EYES_MOVE;
        } else if (position == 2) {
            return TYPE_SLUMBER;
        } else {
            return TYPE_DEEP_SLEEP;
        }
    }
}
