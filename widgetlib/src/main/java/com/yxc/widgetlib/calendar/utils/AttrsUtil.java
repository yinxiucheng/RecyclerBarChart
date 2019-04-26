package com.yxc.widgetlib.calendar.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yxc.commonlib.util.ColorUtil;
import com.yxc.commonlib.util.DisplayUtil;
import com.yxc.widgetlib.R;


/**
 * Created by necer on 2018/11/28.
 */
public class AttrsUtil {


    public static CalendarAttrs getAttrs(Context context, AttributeSet attributeSet) {
        CalendarAttrs attrs = new CalendarAttrs();

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar);

        attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, ColorUtil.getResourcesColor(context, R.color.solarTextColor));
        attrs.todaySolarTextColor = ta.getColor(R.styleable.NCalendar_todaySolarTextColor, ColorUtil.getResourcesColor(context, R.color.todaySolarTextColor));
        attrs.todaySolarSelectTextColor = ta.getColor(R.styleable.NCalendar_todaySolarSelectTextColor, ColorUtil.getResourcesColor(context, R.color.white));
        attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, ColorUtil.getResourcesColor(context, R.color.lunarTextColor));
        attrs.solarHolidayTextColor = ta.getColor(R.styleable.NCalendar_solarHolidayTextColor, ColorUtil.getResourcesColor(context, R.color.solarHolidayTextColor));
        attrs.lunarHolidayTextColor = ta.getColor(R.styleable.NCalendar_lunarHolidayTextColor, ColorUtil.getResourcesColor(context, R.color.lunarHolidayTextColor));
        attrs.solarTermTextColor = ta.getColor(R.styleable.NCalendar_solarTermTextColor, ColorUtil.getResourcesColor(context, R.color.solarTermTextColor));

        attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, ColorUtil.getResourcesColor(context, R.color.selectCircleColor));
        attrs.solarTextSize = ta.getDimension(R.styleable.NCalendar_solarTextSize, DisplayUtil.sp2px(context, 18));
        attrs.lunarTextSize = ta.getDimension(R.styleable.NCalendar_lunarTextSize, DisplayUtil.sp2px(context, 10));
        attrs.lunarDistance = ta.getDimension(R.styleable.NCalendar_lunarDistance, DisplayUtil.sp2px(context, 15));
        attrs.holidayDistance = ta.getDimension(R.styleable.NCalendar_holidayDistance, DisplayUtil.sp2px(context, 15));
        attrs.holidayTextSize = ta.getDimension(R.styleable.NCalendar_holidayTextSize, DisplayUtil.sp2px(context, 10));
        attrs.selectCircleRadius = ta.getDimension(R.styleable.NCalendar_selectCircleRadius, DisplayUtil.dp2px(context, 22));
        attrs.isShowLunar = ta.getBoolean(R.styleable.NCalendar_isShowLunar, false);
        attrs.isDefaultSelect = ta.getBoolean(R.styleable.NCalendar_isDefaultSelect, true);
        attrs.pointSize = ta.getDimension(R.styleable.NCalendar_pointSize, DisplayUtil.dp2px(context, 2));
        attrs.pointDistance = ta.getDimension(R.styleable.NCalendar_pointDistance, DisplayUtil.dp2px(context, 18));
        attrs.pointColor = ta.getColor(R.styleable.NCalendar_pointColor, ColorUtil.getResourcesColor(context, R.color.pointColor));
        attrs.hollowCircleColor = ta.getColor(R.styleable.NCalendar_hollowCircleColor, ColorUtil.getResourcesColor(context, R.color.hollowCircleColor));
        attrs.todayWeekBgColor = ta.getColor(R.styleable.NCalendar_todayWeekBgColor, ColorUtil.getResourcesColor(context, R.color.todayWeekBgColor));
        attrs.hollowCircleStroke = ta.getDimension(R.styleable.NCalendar_hollowCircleStroke, DisplayUtil.dp2px(context, 1));
        attrs.monthCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, DisplayUtil.dp2px(context, 300));
        attrs.duration = ta.getInt(R.styleable.NCalendar_duration, 240);
        attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, false);
        attrs.isWeekHold = ta.getBoolean(R.styleable.NCalendar_isWeekHold, false);
        attrs.holidayColor = ta.getColor(R.styleable.NCalendar_holidayColor, ColorUtil.getResourcesColor(context, R.color.holidayColor));
        attrs.workdayColor = ta.getColor(R.styleable.NCalendar_workdayColor, ColorUtil.getResourcesColor(context, R.color.workdayColor));
        attrs.bgCalendarColor = ta.getColor(R.styleable.NCalendar_bgCalendarColor, ColorUtil.getResourcesColor(context, R.color.transparent));
        attrs.bgChildColor = ta.getColor(R.styleable.NCalendar_bgChildColor, ColorUtil.getResourcesColor(context, R.color.white));
        attrs.firstDayOfWeek = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, CalendarAttrs.SUNDAY);
        attrs.pointLocation = ta.getInt(R.styleable.NCalendar_pointLocation, CalendarAttrs.UP);
        attrs.defaultCalendar = ta.getInt(R.styleable.NCalendar_defaultCalendar, CalendarAttrs.MONTH);
        attrs.holidayLocation = ta.getInt(R.styleable.NCalendar_holidayLocation, CalendarAttrs.TOP_RIGHT);

        attrs.alphaColor = ta.getInt(R.styleable.NCalendar_alphaColor, 0xA0);
        attrs.disabledAlphaColor = ta.getInt(R.styleable.NCalendar_disabledAlphaColor, 50);
        attrs.calendarBarTextColor = ta.getColor(R.styleable.NCalendar_calendarBarTextColor, ColorUtil.getResourcesColor(context, R.color.white));
        attrs.calendarBarTextSize = ta.getDimension(R.styleable.NCalendar_calendarBarTextSize, DisplayUtil.sp2px(context, 12));

        String startString = ta.getString(R.styleable.NCalendar_startDate);
        String endString = ta.getString(R.styleable.NCalendar_endDate);
        attrs.disabledString = ta.getString(R.styleable.NCalendar_disabledString);


        attrs.startDateString = TextUtils.isEmpty(startString) ? "1901-01-01" : startString;
        attrs.endDateString = TextUtils.isEmpty(endString) ? "2099-12-31" : endString;

        ta.recycle();

        return attrs;

    }

}
