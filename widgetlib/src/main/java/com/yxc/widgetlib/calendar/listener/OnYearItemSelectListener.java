package com.yxc.widgetlib.calendar.listener;


import com.yxc.widgetlib.calendar.view.MonthCalendar;

/**
 * @author yxc
 * @date 2019/3/18
 */
public interface OnYearItemSelectListener {
    void onSelect(MonthCalendar yearCalendar, int oldPosition, int newPosition);

}
