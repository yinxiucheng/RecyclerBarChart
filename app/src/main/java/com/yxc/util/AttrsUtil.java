package com.yxc.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yxc.barchart.R;

public class AttrsUtil {

    public static Attrs getAttrs(Context context, AttributeSet attributeSet) {

        Attrs attrs = new Attrs();

        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.NCalendar);

        attrs.solarTextColor = ta.getColor(R.styleable.NCalendar_solarTextColor, context.getResources().getColor(R.color.solarTextColor));
        attrs.todaySolarTextColor = ta.getColor(R.styleable.NCalendar_todaySolarTextColor, context.getResources().getColor(R.color.todaySolarTextColor));
        attrs.todaySolarSelectTextColor = ta.getColor(R.styleable.NCalendar_todaySolarSelectTextColor, context.getResources().getColor(R.color.white));
        attrs.lunarTextColor = ta.getColor(R.styleable.NCalendar_lunarTextColor, context.getResources().getColor(R.color.lunarTextColor));
        attrs.solarHolidayTextColor = ta.getColor(R.styleable.NCalendar_solarHolidayTextColor, context.getResources().getColor(R.color.solarHolidayTextColor));
        attrs.lunarHolidayTextColor = ta.getColor(R.styleable.NCalendar_lunarHolidayTextColor, context.getResources().getColor(R.color.lunarHolidayTextColor));
        attrs.solarTermTextColor = ta.getColor(R.styleable.NCalendar_solarTermTextColor, context.getResources().getColor(R.color.solarTermTextColor));

        attrs.selectCircleColor = ta.getColor(R.styleable.NCalendar_selectCircleColor, context.getResources().getColor(R.color.selectCircleColor));
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
        attrs.pointColor = ta.getColor(R.styleable.NCalendar_pointColor, context.getResources().getColor(R.color.pointColor));
        attrs.hollowCircleColor = ta.getColor(R.styleable.NCalendar_hollowCircleColor, context.getResources().getColor(R.color.hollowCircleColor));
        attrs.todayWeekBgColor = ta.getColor(R.styleable.NCalendar_todayWeekBgColor, context.getResources().getColor(R.color.todayWeekBgColor));
        attrs.hollowCircleStroke = ta.getDimension(R.styleable.NCalendar_hollowCircleStroke, DisplayUtil.dp2px(context, 1));
        attrs.monthCalendarHeight = (int) ta.getDimension(R.styleable.NCalendar_calendarHeight, DisplayUtil.dp2px(context, 300));
        attrs.duration = ta.getInt(R.styleable.NCalendar_duration, 240);
        attrs.isShowHoliday = ta.getBoolean(R.styleable.NCalendar_isShowHoliday, false);
        attrs.isWeekHold = ta.getBoolean(R.styleable.NCalendar_isWeekHold, false);
        attrs.holidayColor = ta.getColor(R.styleable.NCalendar_holidayColor, context.getResources().getColor(R.color.holidayColor));
        attrs.workdayColor = ta.getColor(R.styleable.NCalendar_workdayColor, context.getResources().getColor(R.color.workdayColor));
        attrs.bgCalendarColor = ta.getColor(R.styleable.NCalendar_bgCalendarColor, context.getResources().getColor(R.color.transparent));
        attrs.bgChildColor = ta.getColor(R.styleable.NCalendar_bgChildColor, context.getResources().getColor(R.color.white));
        attrs.firstDayOfWeek = ta.getInt(R.styleable.NCalendar_firstDayOfWeek, Attrs.SUNDAY);
        attrs.pointLocation = ta.getInt(R.styleable.NCalendar_pointLocation, Attrs.UP);
        attrs.defaultCalendar = ta.getInt(R.styleable.NCalendar_defaultCalendar, Attrs.MONTH);
        attrs.holidayLocation = ta.getInt(R.styleable.NCalendar_holidayLocation, Attrs.TOP_RIGHT);
        attrs.alphaColor = ta.getInt(R.styleable.NCalendar_alphaColor, 90);
        attrs.disabledAlphaColor = ta.getInt(R.styleable.NCalendar_disabledAlphaColor, 50);
        attrs.weekBarTextColor = ta.getColor(R.styleable.NCalendar_weekBarTextColor, Color.GRAY);
        attrs.weekBarTextSize = ta.getDimension(R.styleable.NCalendar_weekBarTextSize, DisplayUtil.sp2px(context, 12));
        String startString = ta.getString(R.styleable.NCalendar_startDate);
        String endString = ta.getString(R.styleable.NCalendar_endDate);
        attrs.disabledString = ta.getString(R.styleable.NCalendar_disabledString);
        attrs.startDateString = TextUtils.isEmpty(startString) ? "1901-01-01" : startString;
        attrs.endDateString = TextUtils.isEmpty(endString) ? "2099-12-31" : endString;

        ta.recycle();

        return attrs;
    }

}
