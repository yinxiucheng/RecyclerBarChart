package com.yxc.widgetlib.calendar.listener;

import org.joda.time.LocalDate;


public interface OnClickMonthViewListener {

    void onClickCurrentMonth(LocalDate localDate);

    void onClickLastMonth(LocalDate localDate);

    void onClickNextMonth(LocalDate localDate);

}
