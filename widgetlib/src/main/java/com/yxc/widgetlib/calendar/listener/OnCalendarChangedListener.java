package com.yxc.widgetlib.calendar.listener;


import com.yxc.widgetlib.calendar.entity.NDate;

public interface OnCalendarChangedListener {
    void onCalendarDateChanged(NDate date, boolean isClick);

    void onCalendarStateChanged(boolean isMonthSate);

}
