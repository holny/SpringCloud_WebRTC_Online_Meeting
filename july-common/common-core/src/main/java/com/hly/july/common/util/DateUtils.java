package com.hly.july.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Java 8 的时间工具类
 * @author Linyuan Hou
 * @date 2021/5/11 18:01
 */
public class DateUtils {

    /**
     * 默认使用系统当前时区
     */
    public static final ZoneId ZONE = ZoneId.of("Asia/Shanghai");

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String DATE_FORMAT_DS = "yyyyMMdd";

    public static final String DATE_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FORMAT_CHN = "yyyy年MM月dd日 HH:mm:ss";

    public static final String DATE_FORMAT_CHN_SHORT = "yyyy年MM月dd日";

    public static final String TIME_FORMAT = "yyyyMMddHHmmss";

    public static final String REGEX = "\\:|\\-|\\s";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取当前时间
     *
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

        LocalDateTime now = LocalDateTime.now();

        return now.format(dateTimeFormatter);
    }

    /**
     * 获取当前时间Date
     *
     * @param
     * @return
     */
    public static Date getCurrentDateTime() {

        LocalDateTime now = LocalDateTime.now();

        return Date.from(now.atZone(ZONE).toInstant());

    }


    /**
     * 获取年
     *
     * @return 年
     */
    public static int getYear() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.YEAR);
    }

    /**
     * 获取月份
     *
     * @return 月份
     */
    public static int getMonth() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.MONTH_OF_YEAR);
    }

    /**
     * 获取某月的第几天
     *
     * @return 几号
     */
    public static int getMonthOfDay() {
        LocalTime localTime = LocalTime.now();
        return localTime.get(ChronoField.DAY_OF_MONTH);
    }

    /**
     * 格式化日期为字符串
     *
     * @param date date
     * @param pattern 格式
     * @return 日期字符串
     */
    public static String format(Date date, String pattern){

        Instant instant = date.toInstant();

        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZONE);

        return localDateTime.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 解析字符串日期为Date
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return Date
     */
    public static Date parse(String dateStr, String pattern) {

        LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
        Instant instant = localDateTime.atZone(ZONE).toInstant();
        return Date.from(instant);
    }

    /**
     * 为Date增加分钟,减传负数
     *
     * @param date        日期
     * @param plusMinutes 要增加的分钟数
     * @return 新的日期
     */
    public static Date addMinutes(Date date, Long plusMinutes) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZONE);
        LocalDateTime newDateTime = dateTime.plusMinutes(plusMinutes);
        return Date.from(newDateTime.atZone(ZONE).toInstant());
    }

    /**
     * 增加时间
     *
     * @param date date
     * @param hour 要增加的小时数
     * @return new date
     */
    public static Date addHour(Date date, Long hour) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(date.toInstant(), ZONE);
        LocalDateTime localDateTime = dateTime.plusHours(hour);
        return Date.from(localDateTime.atZone(ZONE).toInstant());
    }

    /**
     * 减月份
     *
     * @param monthsToSubtract 月份
     * @return Date
     */
    public static Date minusMonths(long monthsToSubtract){
        LocalDate localDate = LocalDate.now().minusMonths(monthsToSubtract);

        return localDate2Date(localDate);
    }

    /**
     * LocalDate类型转为Date
     *
     * @param localDate LocalDate object
     * @return Date object
     */
    public static Date localDate2Date(LocalDate localDate) {

        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZONE);

        return Date.from(zonedDateTime.toInstant());
    }

    /**
     * LocalDateTime类型转为Date
     *
     * @param localDateTime LocalDateTime object
     * @return Date object
     */
    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZONE).toInstant());
    }


    /**
     * 获取昨日时间
     *
     * @param format
     * @return
     */
    public static String getYesterday(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate yesterday = nowDate.minusDays(1);
        return yesterday.format(dateTimeFormatter);

    }

    /**
     * 获取上周的时间
     *
     * @param format
     * @return
     */
    public static String getLastWeek(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastWeek = nowDate.minusWeeks(1);
        return lastWeek.format(dateTimeFormatter);
    }

    /**
     * 获取上个月的时间
     *
     * @param format
     * @return
     */
    public static String getLastMonth(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastMonth = nowDate.minusMonths(1);
        return lastMonth.format(dateTimeFormatter);

    }

    /**
     * 获取去年的时间
     *
     * @param format
     * @return
     */
    public static String getLastYear(String format) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate lastYear = nowDate.minusYears(1);
        return lastYear.format(dateTimeFormatter);
    }

    /**
     * 获取前多少天的日期
     *
     * @param format
     * @param num
     * @return
     */
    public static String getBeforeSomeDay(String format, int num) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate nowDate = LocalDate.now();
        LocalDate beforeDay = nowDate.minusDays(num);
        return beforeDay.format(dateTimeFormatter);
    }

    /**
     * 获取指定时间的前多少天
     *
     * @param format
     * @param date
     * @param num
     * @return
     */
    public static String getBeforeDayOfDate(String format, String date, int num) {

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDate beforeDay = localDate.minusDays(num);
        return beforeDay.format(dateTimeFormatter);

    }

    /**
     * 获取当天的开始时间  yyyy-MM-dd 00:00:00
     *
     * @param format
     * @return
     */
    public static String getDayStartTime(String format, String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime toDayStart = LocalDateTime.of(localDate, LocalTime.MIN);
        return toDayStart.format(FORMATTER);
    }

    /**
     * 获取当天的结束时间 yyyy-MM-dd 23:59:59
     *
     * @param format
     * @return
     */
    public static String getDayEndTime(String format, String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
        LocalDate localDate = LocalDate.parse(date, dateTimeFormatter);
        LocalDateTime toDayStart = LocalDateTime.of(localDate, LocalTime.MAX);
        return toDayStart.format(FORMATTER);
    }

    /**
     * 获取两个时间之间的间隔天数
     *
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return
     */
    public static long getRangeCountOfDate(String startDate, String endDate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS);
        LocalDate startLocalDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, dateTimeFormatter);
        long count = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        return count;

    }

    /**
     * 后期两个时间之间的所有日期 【包含开始时间和结束时间】
     *
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return
     */
    public static List<String> getRangeOfDate(String startDate, String endDate) {
        List<String> range = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT_DS);
        LocalDate startLocalDate = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate endLocalDate = LocalDate.parse(endDate, dateTimeFormatter);
        long count = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        if (count < 0) {
            return range;
        }

        range = Stream.iterate(startLocalDate, d -> d.plusDays(1)).limit(count + 1).map(
                s -> s.format(dateTimeFormatter)).collect(Collectors.toList());
        return range;
    }

    public static void main(String[] args) {
        System.out.println(getRangeOfDate("20191010", "20191020"));
    }

}