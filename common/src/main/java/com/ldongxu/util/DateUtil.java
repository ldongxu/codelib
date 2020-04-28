package com.ldongxu.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author liudongxu06
 * @date 2017/11/6
 */
public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_TIME_PATTERN = "HH:mm:ss";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private DateUtil() {
    }

    /**
     * 当前时间，格式 yyyy-MM-dd HH:mm:ss
     *
     * @return 当前时间的标准形式字符串
     */
    public static String now() {
        return formatDateTime(new Date());
    }

    /**
     * 当前日期，格式 yyyy-MM-dd
     *
     * @return 当前日期的标准形式字符串
     */
    public static String today() {
        return formatDate(new Date());
    }

    /**
     * 根据特定格式格式化日期
     *
     * @param date    被格式化的日期
     * @param pattern 格式
     * @return 格式化后的字符串
     */
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 格式 yyyy-MM-dd
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatDate(Date date) {
        return new SimpleDateFormat(DEFAULT_DATE_PATTERN).format(date);
    }

    /**
     * 格式HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatTime(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DEFAULT_TIME_PATTERN);
        return timeFormat.format(date);
    }

    /**
     * 格式 yyyy-MM-dd HH:mm:ss
     *
     * @param date 被格式化的日期
     * @return 格式化后的日期
     */
    public static String formatDateTime(Date date) {
        return new SimpleDateFormat(DEFAULT_DATETIME_PATTERN).format(date);
    }

    /**
     * 将特定格式的日期转换为Date对象
     *
     * @param dateString 特定格式的日期
     * @param format     格式，例如yyyy-MM-dd
     * @return 日期对象
     */
    public static Date parse(String dateString, String format) {
        try {
            return (new SimpleDateFormat(format)).parse(dateString);
        } catch (ParseException e) {
            logger.error("Parse " + dateString + " with format " + format + " error!", e);
        }
        return null;
    }

    /**
     * 格式yyyy-MM-dd HH:mm:ss
     *
     * @param dateString 标准形式的时间字符串
     * @return 日期对象
     */
    public static Date parseDateTime(String dateString) {
        try {
            return new SimpleDateFormat(DEFAULT_DATETIME_PATTERN).parse(dateString);
        } catch (ParseException e) {
            logger.error("Parse " + dateString + " with format " + DEFAULT_DATETIME_PATTERN + " error!", e);
        }
        return null;
    }

    /**
     * 格式yyyy-MM-dd
     *
     * @param dateString 标准形式的日期字符串
     * @return 日期对象
     */
    public static Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat(DEFAULT_DATE_PATTERN).parse(dateString);
        } catch (ParseException e) {
            logger.error("Parse " + dateString + " with format " + DEFAULT_DATE_PATTERN + " error!", e);
        }
        return null;
    }

    /**
     * 格式HH:mm:ss
     *
     * @param timeString 标准形式的日期字符串
     * @return 日期对象
     */
    public static Date parseTime(String timeString) {
        try {
            return new SimpleDateFormat(DEFAULT_TIME_PATTERN).parse(timeString);
        } catch (ParseException e) {
            logger.error("Parse " + timeString + " with format " + DEFAULT_TIME_PATTERN + " error!", e);
        }
        return null;
    }

    /**
     * 格式：<br>
     * 1、yyyy-MM-dd HH:mm:ss<br>
     * 2、yyyy-MM-dd<br>
     * 3、HH:mm:ss>
     *
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date parse(String dateStr) {
        int length = dateStr.length();
        try {
            if (length == DEFAULT_DATETIME_PATTERN.length()) {
                return parseDateTime(dateStr);
            } else if (length == DEFAULT_DATE_PATTERN.length()) {
                return parseDate(dateStr);
            } else if (length == DEFAULT_TIME_PATTERN.length()) {
                return parseTime(dateStr);
            }
        } catch (Exception e) {
            logger.error("Parse " + dateStr + " with format normal error!", e);
        }
        return null;
    }

    /**
     * 获取指定日期偏移指定时间后的时间
     *
     * @param date          基准日期
     * @param calendarField 偏移的粒度大小（小时、天、月等）使用Calendar中的常数
     * @param offsite       偏移量，正数为向后偏移，负数为向前偏移
     * @return 偏移后的日期
     */
    public static Date offsiteDate(Date date, int calendarField, int offsite) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendarField, offsite);
        return cal.getTime();
    }

    /**
     * 昨天
     *
     * @return 昨天
     */
    public static Date yesterday() {
        return offsiteDate(new Date(), Calendar.DAY_OF_YEAR, -1);
    }

    /**
     * 上周
     *
     * @return 上周
     */
    public static Date lastWeek() {
        return offsiteDate(new Date(), Calendar.WEEK_OF_YEAR, -1);
    }

    /**
     * 上个月
     *
     * @return 上个月
     */
    public static Date lastMouth() {
        return offsiteDate(new Date(), Calendar.MONTH, -1);
    }
}
