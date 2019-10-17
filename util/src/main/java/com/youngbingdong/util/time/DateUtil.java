package com.youngbingdong.util.time;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * @author iba
 */
public final class DateUtil {

    /**
     * 默认日期格式，带日期和时间yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAUL_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 中划线分割日期yyyy-MM-dd
     */
    public static final String DEFAULT_NO_TIME_FROMAT = "yyyy-MM-dd";

    /**
     * 逗号分割日期yyyy.MM.dd
     */
    public static final String DOT_DATE_FROMAT = "yyyy.MM.dd";

    /**
     * 无分割日期yyyyMMdd
     */
    public static final String NO_SPLIT_DATE_FROMAT = "yyyyMMdd";

    /**
     * 无分割日期yyyyMM
     */
    public static final String NO_SPLIT_DATE_YM = "yyyyMM";

    /**
     * 斜线日期yyyy/MM/dd
     */
    public static final String SLASH_DATE_FROMAT = "yyyy/MM/dd";

    /**
     * 时间格式 HH:mm:ss
     */
    public static final String TIME_FROMAT = "HH:mm:ss";

    /**
     * 一日开始时间
     */
    public static final String START_DATE_TIME = "00:00:00";

    /**
     * 一日结束时间
     */
    public static final String END_DATE_TIME = "23:59:59";

    public static final String CRON_FORMAT = "ss mm HH dd MM ? yyyy";
    public static final String DEFAULT_NO_SECOND_FROMAT = "yyyy-MM-dd HH:mm";

    private DateUtil() {
    }

    /**
     * 获取yyyy-MM-dd格式字符串
     *
     * @param date
     * @returntr
     */
    public static String getMidLineDateStr(Date date) {
        return formatDate(date, DEFAULT_NO_TIME_FROMAT);
    }

    /**
     * 获取yyyy/MM/dd格式字符串
     *
     * @param date
     * @returntr
     */
    public static String getSlashDateStr(Date date) {
        return formatDate(date, SLASH_DATE_FROMAT);
    }

    /**
     * 获取yyyyMMdd格式字符串
     *
     * @param date
     * @returntr
     */
    public static String getNoSplitDateStr(Date date) {
        return formatDate(date, NO_SPLIT_DATE_FROMAT);
    }

    /**
     * 获取yyyy.MM.dd格式字符串
     *
     * @param date
     * @returntr
     */
    public static String getDotDateStr(Date date) {
        return formatDate(date, DOT_DATE_FROMAT);
    }

    /**
     * 获取当前时间点的指定格式字符串
     *
     * @param format 时间格式
     * @return 指定格式的字符串
     */
    public static String getNowStr(String format) {
        Date now = getCurDate();
        return formatDate(now, format);
    }

    /**
     * 获取当前时间的"yyyy-MM-dd HH:mm:ss"形式字符串
     *
     * @return
     */
    public static String getNowDefaultFormatStr() {
        return getNowStr(DEFAUL_FORMAT);
    }

    /**
     * 获取时间的指定格式字符串
     *
     * @param date
     * @param format 时间格式
     * @return
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 获取当前时间统一使用该方法，方便后期修改
     *
     * @return
     */
    public static Date getCurDate() {
        // TODO 分布式系统后修改该时间获取规则
        Date now = new Date();
        return now;
    }

    /**
     * 获取当前时间戳的string形式（当前时间戳只精确到秒）
     *
     * @return
     */
    public static String getNowTimeStampStr() {
        long time = getCurDate().getTime();
        return String.valueOf(time / 1000);
    }

    public static long getTimeStampStr(Date date) {
        return date.getTime() / 1000;
    }

    public static Long getCurrentTimeStamp() {
        long time = getCurDate().getTime();
        return time / 1000;
    }

    /**
     * 以指定的格式是解析时间
     *
     * @param format
     * @param date
     * @return
     */
    public static Date getStrDate(String format, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 以默认的时间格式解析时间字符串
     *
     * @param date
     * @return
     */
    public static Date getStrDateByDefault(String date) {
        if (isEmpty(date)) {
            return null;
        }
        return getStrDate(DEFAUL_FORMAT, date);
    }

    /**
     * yyyy-MM-dd 日期转化
     *
     * @param date
     * @return
     */
    public static Date getStrDateNoTime(String date) {
        if (isEmpty(date)) {
            return null;
        }
        return getStrDate(DEFAULT_NO_TIME_FROMAT, date);
    }

    /**
     * 获取时间的默认格式
     *
     * @param date
     * @return
     */
    public static String getDefaultFormatDate(Date date) {
        return formatDate(date, DEFAUL_FORMAT);
    }

    public static Date rollDate(Date curDate, char unit, int value) {
        Calendar c = Calendar.getInstance();
        c.setTime(curDate);

        switch (unit) {
            case 'M':
                c.add(Calendar.MINUTE, value);
                break;
            case 'H':
                c.add(Calendar.HOUR, value);
                break;
            case 'D':
                c.add(Calendar.DATE, value);
                break;
            default:
                break;
        }

        curDate = c.getTime();
        return curDate;
    }

    /**
     * 获取指定一天的最后时间
     *
     * @param date
     * @return
     */
    public static Date getDayLastTime(Date date) {
        if (null == date) {
            return null;
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        //目前数据库只精确到秒的, 毫秒会四舍五入，需要把毫秒设为0
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

    /**
     *
     */
    public static Date getBeginTimeOfDate(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        ca.set(Calendar.MILLISECOND, 0);
        return ca.getTime();
    }

    /**
     * 获取某年某月的第一天 的00:00:00
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfMonth(Date date) {
        Date dateFirst = setDays(date, 1);
        return getBeginTimeOfDate(dateFirst);
    }

    /**
     * 获取某年某月的最后一天 的23:59:59
     *
     * @param date
     * @return
     */
    public static Date getLastDayOfMonth(Date date) {
        Date nextMonthFirstDate = setDays(addMonths(date, 1), 1);
        Date curMonthlastDay = addDays(nextMonthFirstDate, -1);
        return getDayLastTime(curMonthlastDay);
    }

    /**
     * 获取某年的 年的最后时间
     *
     * @param date
     * @return
     */
    public static Date getYearStartTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        return getFirstDayOfMonth(c.getTime());
    }

    /**
     * 获取某年的 年的最后时间
     *
     * @param date
     * @return
     */
    public static Date getYearEndTime(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, Calendar.DECEMBER);
        return getLastDayOfMonth(c.getTime());
    }

    /**
     * 获取某年某月的第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfLastMonth(Date date) {
        Date lastMonthDate = addMonths(date, -1);
        return getBeginTimeOfDate(setDays(lastMonthDate, 1));
    }

    public static Date setDays(Date date, int amount) {
        return set(date, Calendar.DAY_OF_MONTH, amount);
    }

    private static Date set(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setLenient(false);
        c.setTime(date);
        c.set(calendarField, amount);
        return c.getTime();
    }

    /**
     * 获取今天是几号
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    /**
     * 获取今天是几月
     *
     * @param date
     * @return
     */
    public static int getMonthOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前时间是本月第几周
     *
     * @param date
     * @return
     */
    public static int getWeekOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_MONTH);
    }


    /**
     * 获取今天是第几季度
     *
     * @param date
     * @return
     */
    public static int getSeasonOfYear(Date date) {
        int month = getMonthOfYear(date);
        int season = 0;
        if (month > 0 && month < 4) {
            season = 1;
        } else if (month >= 4 && month < 7) {
            season = 2;
        } else if (month >= 7 && month < 10) {
            season = 3;
        } else {
            season = 4;
        }

        return season;
    }

    /**
     * 获取当前季度第一天
     *
     * @param date
     * @return
     */
    public static Date getStartTimeOfSeason(Date date) {
        int season = getSeasonOfYear(date);
        int month = Calendar.JANUARY;
        if (season == 1) {
            month = Calendar.JANUARY;
        } else if (month == 2) {
            month = Calendar.MAY;
        } else if (month == 3) {
            month = Calendar.AUGUST;
        } else if (month == 4) {
            month = Calendar.NOVEMBER;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, month);
        return getFirstDayOfMonth(c.getTime());
    }

    /**
     * 获取传入时间的周一
     *
     * @param date 当前时间
     * @return 返回传入时间当周星期一
     */
    public static Date getNowWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return getBeginTimeOfDate(cal.getTime());
    }

    /**
     * 获取当前星期几
     *
     * @param date 当前时间
     * @return "0-星期日", "1-星期一", "2-星期二", "3-星期三", "4-星期四", "5-星期五", "6-星期六
     */
    public static Integer getWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return w;
    }

    /**
     * 传入int获取对应的周几
     *
     * @param w 0-周日 1-周一 以此类推
     * @return
     */
    public static String getWeekDayStr(int w) {
        String weekDayStr = "";
        switch (w) {
            case 0:
                weekDayStr = "周日";
                break;

            case 1:
                weekDayStr = "周一";
                break;
            case 2:
                weekDayStr = "周二";
                break;
            case 3:
                weekDayStr = "周三";
                break;
            case 4:
                weekDayStr = "周四";
                break;
            case 5:
                weekDayStr = "周五";
                break;
            case 6:
                weekDayStr = "周六";
                break;
            default:
                break;
        }
        return weekDayStr;
    }

    public static int getDayDiff(Date d1, Date d2) {
        Long t1 = d1.getTime();
        Long t2 = d2.getTime();
        return (int) ((t1 - t2) / (24 * 60 * 60 * 1000));
    }

    public static int getDiff(Date d1, Date d2, char unit) {
        int diff = 0;
        switch (unit) {
            case 'M':
                diff = (int) (Math.abs(d1.getTime() - d2.getTime()) / (1000 * 60));
                break;
            case 'H':
                diff = (int) (Math.abs(d1.getTime() - d2.getTime()) / (1000 * 60 * 60));
                break;
            case 'D':
                diff = (int) (Math.abs(d1.getTime() - d2.getTime()) / (1000 * 60 * 60 * 24));
                break;
            default:
                throw new IllegalArgumentException();
        }

        return diff;
    }

    /**
     * 获取传入时间的周一的开始时间
     *
     * @param date 当前时间
     * @return
     */
    public static Date getWeekMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return getBeginTimeOfDate(cal.getTime());
    }


    /**
     * 获取传入时间的上周星期几的时间
     *
     * @param date
     * @param week Calendar的常量  星期一用  Calendar.MONDAY
     * @return
     */
    public static Date getLastWeekDay(Date date, int week) {
        Date a = addDays(date, -1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        cal.set(Calendar.DAY_OF_WEEK, week);

        return getBeginTimeOfDate(cal.getTime());
    }

    /**
     * 获取传入时间下周一
     *
     * @param date 当前时间
     * @return 返回下周一日期
     */
    public static Date getNextWeekMonday(Date date) {
        Date a = addDays(date, -1);
        Calendar cal = Calendar.getInstance();
        cal.setTime(a);
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return getBeginTimeOfDate(cal.getTime());
    }

    public static Date getNextDayStartTime(Date date) {
        Date a = addDays(date, 1);
        return getBeginTimeOfDate(a);
    }

    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    /**
     * 获得指定日期的偏移天数的23点59分59秒时间
     *
     * @param num 比较指定时间的偏移天数，例：－1表是指定日期的前一天，1表示指定日期的后一天。
     * @return
     */
    public static Date getOffsetDayEndDate(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, num);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal.getTime();
    }

    /**
     * 获得指定日期的偏移天数的23点59分59秒时间
     *
     * @param num 比较指定时间的偏移天数，例：－1表是指定日期的前一天，1表示指定日期的后一天。
     * @return
     */
    public static Date getOffsetDayStartDate(Date date, int num) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, num);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 查询条件的最小时间
     *
     * @return
     */
    public static Date getQryMinDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAUL_FORMAT);
        try {
            return sdf.parse("1970-01-01 00:00:00");
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 检查target时间是否在指定时间区间内
     *
     * @param begin
     * @param end
     * @param target
     * @return
     */
    public static Boolean checkDateSection(Date begin, Date end, Date target) {

        return formatDate(begin, "yyyyMMddHHmmss").compareTo(formatDate(target, "yyyyMMddHHmmss")) <= 0
                && formatDate(target, "yyyyMMddHHmmss").compareTo(formatDate(end, "yyyyMMddHHmmss")) <= 0;
    }

    /**
     * 查询条件的最大时间
     *
     * @return
     */
    public static Date getQryMaxDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAUL_FORMAT);
        try {
            return sdf.parse("2099-12-31 23:59:59");
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 得到几天后的时间
     */
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }


    public static Date getDifineDate(String dateStr, String format) {
        DateFormat dateFormat2 = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat2.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static boolean isFirstDayOfYear() {
        return "0101".equals(formatDate(new Date(), "MMdd"));
    }

    public static boolean timeOverlap(TimeInterval timeIntervalA, TimeInterval timeIntervalB) {
        return (timeIntervalA.getStartTime() >= timeIntervalB.getStartTime() && timeIntervalA.getStartTime() <= timeIntervalB.getEndTime())
                || (timeIntervalA.getEndTime() >= timeIntervalB.getStartTime() && timeIntervalA.getEndTime() <= timeIntervalB.getEndTime())
                || (timeIntervalA.getStartTime() <= timeIntervalB.getStartTime() && timeIntervalA.getEndTime() >= timeIntervalB.getEndTime());
    }

    public static class TimeInterval {
        private Date startTime;
        private Date endTime;

        public static TimeInterval of(Date startTime, Date endTime) {
            return new TimeInterval(startTime, endTime);
        }

        private TimeInterval(Date startTime, Date endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Long getStartTime() {
            return startTime.getTime();
        }

        public TimeInterval setStartTime(Date startTime) {
            this.startTime = startTime;
            return this;
        }

        public Long getEndTime() {
            return endTime.getTime();
        }

        public TimeInterval setEndTime(Date endTime) {
            this.endTime = endTime;
            return this;
        }
    }

    public static String parseToCron(Date date) {
        return FastDateFormat.getInstance(CRON_FORMAT).format(date);
    }

    /**
     * 两个日期相距多少天,不足一天按一天算
     *
     * @param date1
     * @param date2
     * @return
     * @throws ParseException
     */
    public static int daysBetween(Date date1, Date date2) throws ParseException {
        long oneDays = 1000 * 3600 * 24;
        long times = date1.getTime() - date2.getTime();
        if (times < oneDays) {
            times = oneDays;
        }
        Double between_days = times * 1.0 / oneDays;
        return ((Double) Math.ceil(between_days)).intValue();
    }

    /**
     * 获取当天及当天之后X天的具体某个时间
     *
     * @param hours
     * @param minutes
     * @param seconds
     * @return
     */
    public static Date getThatDaySomeTime(int hours, int minutes, int seconds, int num) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, num);
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);
        cal.set(Calendar.SECOND, seconds);
        return cal.getTime();
    }

    public static Long getDifferenceSeconds(Date begin, Date end) {
        return (end.getTime() - begin.getTime()) / 1000;
    }
}
