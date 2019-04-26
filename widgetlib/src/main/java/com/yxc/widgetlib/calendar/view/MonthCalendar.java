package com.yxc.widgetlib.calendar.view;

import org.joda.time.LocalDate;

/**
 * @author yxc
 * @date 2019/3/8
 *
 */
public class MonthCalendar {

    public LocalDate localDate;

    public boolean isFuture;//未来的月，灰色不可点击

    public boolean isCurrent;//当前月

    public boolean isSelected;//选中月

    public MonthCalendar(){}

    public MonthCalendar(LocalDate localDate) {

        this.localDate = localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public boolean isFuture() {
        return isFuture;
    }

    public void setFuture(boolean future) {
        isFuture = future;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
