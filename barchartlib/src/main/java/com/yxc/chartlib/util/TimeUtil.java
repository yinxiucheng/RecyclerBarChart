package com.yxc.chartlib.util;

import com.yxc.chartlib.entrys.BarEntry;
import com.yxc.chartlib.component.DistanceCompare;
import com.yxc.chartlib.formatter.Hour12;

import org.joda.time.Days;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yxc
 * @date 2019/4/7
 */
public class TimeUtil {

    public static final long TIME_DAY = 24 * 60 * 60;

    public static final long TIME_HOUR = 1 * 60 * 60;
    //一位小数
    public static final int ONE_LENGTH_DECIMAL = 1;
    //两位小数
    public static final int TWO_LENGTH_DECIMAL = 2;
    //三位小数
    public static final int THREE_LENGTH_DECIMAL = 3;

    public static final int NUM_DAY_OF_WEEK = 7;

    public static final int NUM_HOUR_OF_DAY = 24;

    public static final int NUM_MONTH_OF_YEAR = 12;

    //周的第一天
    public static final int SUNDAY = 300;//周的第一天 周日
    public static final int MONDAY = 301;//周的第一天 周一

    //是否同月
    public static boolean isEqualsMonth(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear() && date1.getMonthOfYear() == date2.getMonthOfYear();
    }

    /**
     * 第一个是不是第二个的上一个月,只在此处有效
     *
     * @param currentMonth
     * @param lastMonth
     * @return
     */
    public static boolean isLastMonth(LocalDate currentMonth, LocalDate lastMonth) {
        LocalDate date = lastMonth.plusMonths(1);
        return date.getMonthOfYear() == currentMonth.getMonthOfYear();
    }


    public static boolean isLastMonthOfTheYear(LocalDate localDate) {
        return localDate.getMonthOfYear() == 12;
    }


    /**
     * 第一个是不是第二个的下一个月，只在此处有效
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNextMonth(LocalDate date1, LocalDate date2) {
        LocalDate date = date2.plusMonths(1);
        return date1.getMonthOfYear() == date.getMonthOfYear();
    }


    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getIntervalMonths(LocalDate date1, LocalDate date2) {
        date1 = date1.withDayOfMonth(1);
        date2 = date2.withDayOfMonth(1);

        return Months.monthsBetween(date1, date2).getMonths();
    }

    /**
     * 获得两个日期距离几周
     *
     * @param date1
     * @param date2
     * @param type  一周
     * @return
     */
    public static int getIntervalWeek(LocalDate date1, LocalDate date2, int type) {

        if (type == MONDAY) {
            date1 = getMonFirstDayOfWeek(date1);
            date2 = getMonFirstDayOfWeek(date2);
        } else {
            date1 = getSunFirstDayOfWeek(date1);
            date2 = getSunFirstDayOfWeek(date2);
        }

        return Weeks.weeksBetween(date1, date2).getWeeks();

    }

    public static int getIntervalDay(LocalDate date1, LocalDate date2) {
        return Days.daysBetween(date1, date2).getDays();

    }


    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return new LocalDate().equals(date);

    }

    public static List<LocalDate> getMonthLocalDateCalendar(LocalDate localDate) {
        return getMonthLocalDateCalendar(localDate, MONDAY);
    }


    public static List<LocalDate> getMonthLocalDateCalendar(LocalDate localDate, int type) {

        LocalDate lastMonthDate = localDate.plusMonths(-1);//上个月
        LocalDate nextMonthDate = localDate.plusMonths(1);//下个月

        int days = localDate.dayOfMonth().getMaximumValue();//当月天数
        int lastMonthDays = lastMonthDate.dayOfMonth().getMaximumValue();//上个月的天数
        int firstDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), 1).getDayOfWeek();//当月第一天周几
        int endDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), days).getDayOfWeek();//当月最后一天周几

        List<LocalDate> dateList = new ArrayList<>();


        //周一开始的
        if (type == MONDAY) {
            //周一开始的
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 2));
                dateList.add(date);
            }
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
            for (int i = 0; i < 7 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
        } else {
            //上个月
            if (firstDayOfWeek != 7) {
                for (int i = 0; i < firstDayOfWeek; i++) {
                    LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 1));
                    dateList.add(date);
                }
            }
            //当月
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0;
            }
            for (int i = 0; i < 6 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
        }

        //某些年的2月份28天，又正好日历只占4行
        if (dateList.size() == 28) {
            for (int i = 0; i < 7; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
        }
        return dateList;

    }


    //未来，明后天等
    public static boolean isAfterToday(LocalDate localDate) {
        LocalDate today = LocalDate.now();
        return localDate.isAfter(today);
    }


    public static boolean isSameWeekWithToday(LocalDate localDate) {
        int week = getDayWeek(localDate);
        return isThisYear(localDate) && week == getTodayWeek();
    }

    public static boolean isThisYear(LocalDate localDate) {
        return localDate.getYear() == LocalDate.now().getYear();
    }


    //获取今天坐在周
    public static int getTodayWeek() {
        return getDayWeek(LocalDate.now());
    }

    //获取LocalDate所在的周
    public static int getDayWeek(LocalDate localDate) {
        int selectDayWeek = localDate.getWeekOfWeekyear();
        return selectDayWeek;
    }

    public static int getWeekOfMonth(LocalDate mSelectDate) {
        List<LocalDate> dateList = getMonthLocalDateCalendar(mSelectDate);
        return getWeekOfMonth(dateList, mSelectDate);
    }

    //获取这个周在这个月中的第几周
    public static int getWeekOfMonth(List<LocalDate> dateList, LocalDate mSelectDate) {
        int weekOfMonth = -1;
        int i = dateList.indexOf(mSelectDate);
        if (i >= 0) {
            weekOfMonth = i % 7 == 0 ? i / 7 : i / 7 + 1;
        }
        return weekOfMonth;
    }

    //转化一周从周日开始
    public static LocalDate getSunFirstDayOfWeek(LocalDate date) {
        if (date.dayOfWeek().get() == 7) {
            return date;
        } else {
            return date.minusWeeks(1).withDayOfWeek(7);
        }
    }

    //转化一周从周一开始
    public static LocalDate getMonFirstDayOfWeek(LocalDate date) {
        return date.dayOfWeek().withMinimumValue();
    }

    //时间戳转成 LocalDate 13位
    public static LocalDate timestampToLocalDateInner(long timestamp) {
        LocalDate localDate = Instant.ofEpochMilli(timestamp).toDateTime().toLocalDate();
        return localDate;
    }

    public static LocalDate timestampToLocalDate(long timestamp) {
        return timestampToLocalDateInner(timestamp * 1000);
    }


    // 13位的
    public static long localDateToTimestampInner(LocalDate localDate) {
        long timestamp = localDate.toDateTimeAtCurrentTime().getMillis();
        return timestamp;
    }

    public static long localDateToTimestamp(LocalDate localDate) {
        return localDateToTimestampInner(localDate) / 1000;
    }

    /**
     * 时间取整，取到当天的 例如 2019-03-23 00：00：00 (13位)
     *
     * @param localDate
     * @return
     */
    public static long changZeroOfTheDayInner(LocalDate localDate) {
        long time = TimeUtil.localDateToTimestampInner(localDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(time);
        String dateStr = sdf.format(date);
        return Timestamp.valueOf(dateStr).getTime();
    }


    public static String getLocalDateStr(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date date = new Date(timestamp);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static long changZeroOfTheDay(LocalDate localDate) {
        return changZeroOfTheDayInner(localDate) / 1000;
    }

    public static String getDateStr(long timestamp, String patternStr) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patternStr);

        Date date = new Date(timestamp * 1000);
        String res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * @param current
     * @param compare
     * @param type(1,2,3:分别表示取1位、2位、3位)
     * @return
     */
    //todo 算法有问题
    public static String getPercentageStr(long current, long compare, int type) {
        if (compare <= 0) {
            return " -- ";
        }
        long distance = Math.abs(current - compare);
        float f;
        if (type == TWO_LENGTH_DECIMAL) {
            float temp = (distance * 100.0f) / compare;
            f = Math.round(temp);
        } else if (type == THREE_LENGTH_DECIMAL) {
            float temp = distance * 1000 / compare;
            f = Math.round(temp);
        } else {
            float temp = distance * 10 / compare;
            f = Math.round(temp);
        }
        return f + "%";
    }

    //获取百分数
    public static String getPercentageStr(long current, long compare) {
        return getPercentageStr(current, compare, ONE_LENGTH_DECIMAL);
    }

    //下周一 0点0时0分
    public static long getNextWeekMondayTime(LocalDate localDate) {
        LocalDate mondayLocalDate = getNextWeekMonday(localDate);
        long timestamp = changZeroOfTheDay(mondayLocalDate);
        return timestamp;
    }

    //获取下周一，用来请求周的参数
    public static LocalDate getNextWeekMonday(LocalDate localDate) {
        int week = localDate.getDayOfWeek();
        int distance = 7 - week + 1;
        return localDate.plusDays(distance);
    }

    //获取本周一
    public static LocalDate getWeekMonday(LocalDate localDate) {
        int week = localDate.getDayOfWeek();
        return localDate.minusDays(week - 1);
    }

    //获取明天的 00：00：00
    public static long getTomorrowTime() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        long timestamp = changZeroOfTheDay(tomorrow);
        return timestamp;
    }

    //获取本周一的 00：00：00
    public static long getWeekMondayTime(LocalDate localDate) {
        LocalDate mondayLocalDate = getWeekMonday(localDate);
        long timestamp = changZeroOfTheDay(mondayLocalDate);
        return timestamp;
    }

    //获取下个月1号 00：00：00
    public static long getNextMonthFirstDayOfTime(LocalDate localDate) {
        LocalDate tempLocalDate = localDate.plusMonths(1);
        return changZeroOfTheDay(getFirstDayOfMonth(tempLocalDate));
    }

    //获取下个月1号 00：00：00
    public static LocalDate getFirstDayOfNextMonth(LocalDate localDate) {
        long timestamp = getNextMonthFirstDayOfTime(localDate);
        return timestampToLocalDate(timestamp);
    }

    //获取上个月1号 00：00：00
    public static long getLastMonthFirstDayTime(long timestamp) {
        LocalDate localDate = timestampToLocalDate(timestamp);
        return getLastMonthFirstDayTime(localDate);
    }

    //获取上个月1号 00：00：00
    public static long getLastMonthFirstDayTime(LocalDate localDate) {
        LocalDate tempLocalDate = localDate.minusMonths(1);
        LocalDate firstDayMonth = TimeUtil.getFirstDayOfMonth(tempLocalDate);
        long resultTimestamp = TimeUtil.changZeroOfTheDay(firstDayMonth);
        return resultTimestamp;
    }

    //获取本月的1号 00：00：00
    public static LocalDate getFirstDayOfMonth(LocalDate localDate) {
        return timestampToLocalDate(getFirstDayOfMonthTime(localDate));
    }

    public static long getFirstDayOfMonthTime(LocalDate localDate) {
        int distance = localDate.getDayOfMonth();
        LocalDate firstDayOfMonth = localDate.minusDays(distance - 1);
        return changZeroOfTheDay(firstDayOfMonth);
    }

    //同一天
    public static boolean isSameLocalDate(LocalDate date, LocalDate compareDate) {
        return date.getYear() == compareDate.getYear() &&
                date.getMonthOfYear() == compareDate.getMonthOfYear() &&
                date.getDayOfMonth() == compareDate.getDayOfMonth();
    }

    //下一天
    public static boolean isNextDay(long timestamp) {
        long timestampMinusOneHour = timestamp - TIME_HOUR;
        LocalDate localDate1 = timestampToLocalDate(timestamp);
        LocalDate localDate2 = timestampToLocalDate(timestampMinusOneHour);
        return localDate1.getDayOfWeek() != localDate2.getDayOfWeek();
    }

    public static boolean isLastHourOfTheDay(long timestamp) {
        long timestampPlusOneHour = timestamp + TIME_HOUR;
        LocalDate localDate1 = timestampToLocalDate(timestamp);
        LocalDate localDate2 = timestampToLocalDate(timestampPlusOneHour);
        return localDate1.getDayOfWeek() != localDate2.getDayOfWeek();
    }

    //是否是上一周
    public static boolean isLastWeek(LocalDate localDate, LocalDate lastLocalDate) {
        LocalDate currentLocalDate = lastLocalDate.plusWeeks(1);
        return currentLocalDate.getWeekOfWeekyear() == localDate.getWeekOfWeekyear();
    }

    //需要跟上个月比较
    public static boolean isAnotherYear(LocalDate localDate) {
        LocalDate lastMonth = localDate.minusMonths(1);
        return localDate.getYear() != lastMonth.getYear();
    }

    public static boolean isMonday(LocalDate localDate) {
        return localDate.getDayOfWeek() == 1;
    }

    public static boolean isLastDayOfMonth(LocalDate localDate) {
        LocalDate temp = localDate.plusDays(1);
        return localDate.getMonthOfYear() != temp.getMonthOfYear();
    }


    public static boolean isFirstDayOfMonth(LocalDate localDate) {
        LocalDate temp = localDate.minusDays(1);
        return localDate.getMonthOfYear() != temp.getMonthOfYear();
    }

    public static String getWeekStr(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 7:
                return "周日";
            default:
                return "";
        }
    }

    public static int getHourOfTheDay(long timestamp) {
        LocalDate localDate = timestampToLocalDate(timestamp);
        long zeroHour = changZeroOfTheDay(localDate);
        long distance = timestamp - zeroHour;
        int time = (int) (distance / TIME_HOUR);
        return time;
    }

    public static Hour12 get12HourOfTheDay(long timestamp) {
        int time = getHourOfTheDay(timestamp);
        Hour12 hour12 = new Hour12();
        if (time == 0) {

        } else if (time < 12) {
            hour12.hour = time;
            hour12.isAnte = true;
        } else {
            hour12.hour = time % 12;
            hour12.isAnte = false;
        }
        if (hour12.hour == 0){
            hour12.hour = 12;
        }
        return hour12;
    }

    //返回固定的时间段格式
    public static String get12HourOfTheDayStr(long timestamp) {
        long start = timestamp;
        long end = timestamp + TimeUtil.TIME_HOUR;
        Hour12 hour12Start = get12HourOfTheDay(start);
        Hour12 hour12End = get12HourOfTheDay(end);
        return hour12Start.getHour12String() + "-" + hour12End.getHour12String();
    }


    public static boolean isSameYear(long timestamp, long timestampCompare) {
        LocalDate localDate = timestampToLocalDate(timestamp);
        LocalDate localDateCompare = timestampToLocalDate(timestampCompare);
        return localDate.getYear() == localDateCompare.getYear();
    }

    public static boolean isSameMonth(long timestamp, long timestampCompare) {
        LocalDate localDate = timestampToLocalDate(timestamp);
        LocalDate localDateCompare = timestampToLocalDate(timestampCompare);
        return localDate.getYear() == localDateCompare.getYear() && localDate.getMonthOfYear() == localDateCompare.getMonthOfYear();
    }

    public static boolean isTheSameDay(long timestamp, long timestampCompare) {
        LocalDate localDate = timestampToLocalDate(timestamp);
        LocalDate localDateCompare = timestampToLocalDate(timestampCompare);
        return isSameLocalDate(localDate, localDateCompare);
    }


    public static DistanceCompare createYearDistance(LocalDate localDate) {
        int month = localDate.getMonthOfYear();
        return new DistanceCompare(month, 12 - month);
    }

    public static DistanceCompare createDayDistance(BarEntry barEntry) {
        LocalDate localDate = barEntry.localDate;
        long tomorrowZeroTime = TimeUtil.changZeroOfTheDay(localDate.plusDays(1));//明天凌晨
        long todayZeroTime = TimeUtil.changZeroOfTheDay(localDate);//今天0点

        int distanceRight = (int) ((tomorrowZeroTime - barEntry.timestamp) / TimeUtil.TIME_HOUR); //右边的距离
        int distanceLeft = (int) ((barEntry.timestamp - todayZeroTime) / TimeUtil.TIME_HOUR);//左边的距离

        return new DistanceCompare(distanceLeft, distanceRight);
    }

    public static DistanceCompare createWeekDistance(LocalDate localDate) {
        int dayOfWeek = localDate.getDayOfWeek();
        return new DistanceCompare(dayOfWeek, 7 - dayOfWeek);
    }

    public static DistanceCompare createMonthDistance(LocalDate localDate) {
        LocalDate nextMonthFirstDay = TimeUtil.getFirstDayOfNextMonth(localDate);
        LocalDate lastMonthLastDay = TimeUtil.getFirstDayOfMonth(localDate).minusDays(1);

        int distanceRight = TimeUtil.getIntervalDay(localDate, nextMonthFirstDay); //右边的距离
        int distanceLeft = TimeUtil.getIntervalDay(lastMonthLastDay, localDate);
        return new DistanceCompare(distanceLeft, distanceRight);
    }

    public static boolean isFuture(LocalDate localDate) {
        return localDate.isAfter(LocalDate.now());
    }

    public static boolean isFuture(long timestamp) {
        return timestamp > System.currentTimeMillis() / 1000;
    }


    public static boolean isSunday(LocalDate localDateEntry) {
        return localDateEntry.getDayOfWeek() == 7;
    }

    //get the firstDay of the lastMonth of zhe Year (yyyy-12-01)
    public static LocalDate getLastMonthOfTheYear(LocalDate localDate) {
        int monthOfTheYear = localDate.getMonthOfYear();
        int distance = TimeUtil.NUM_MONTH_OF_YEAR - monthOfTheYear;
        LocalDate lastMonthOfYear = localDate.plusMonths(distance);
        LocalDate firstDayOfLastMonthYear = TimeUtil.getFirstDayOfMonth(lastMonthOfYear);
        return firstDayOfLastMonthYear;
    }


    public static LocalDate getLastDayOfThisMonth(LocalDate localDate) {
        LocalDate lastDayOfMonth = TimeUtil.getFirstDayOfNextMonth(localDate).minusDays(1);
        long timestamp = TimeUtil.changZeroOfTheDay(lastDayOfMonth);
        LocalDate lastDayOfMonthZero = TimeUtil.timestampToLocalDate(timestamp);
        return lastDayOfMonthZero;
    }

    public static LocalDate getLastDayOfThisWeek(LocalDate localDate) {
        int dayOfWeek = localDate.getDayOfWeek();
        int distance = TimeUtil.NUM_DAY_OF_WEEK - dayOfWeek;
        LocalDate lastDayOfWeek = localDate.plusDays(distance);
        return lastDayOfWeek;
    }

}
