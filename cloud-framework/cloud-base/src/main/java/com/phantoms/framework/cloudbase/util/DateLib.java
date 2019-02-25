package com.phantoms.framework.cloudbase.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateLib {

    private static Logger logger = LoggerFactory.getLogger(DateLib.class);
    private static String defaultDatePattern = "yyyy-MM-dd HH:mm:ss";
    public static String formatDatetime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern()
    {
        return defaultDatePattern;
    }

    public static Date parse(String strDate, String pattern)
    {
        try {
            return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(
                    pattern).parse(strDate);
        } catch (ParseException e) {
            logger.error("parse date string fail" + e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param millis long
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String convertDateTimeMillis(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }

    /**
     *
     * @param millis long
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String convertDateTimeMin(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(calendar.getTime());
    }
    /**
     *
     * @param millis long
     * @return yyyy-MM-dd
     */
    public static String convertDateTimeMillisToDay(long millis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
    public static long convertDateTimeMillisToLong(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

        return Long.valueOf(df.format(date));
    }

    public static long convertDateTimeToLong(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

        return Long.valueOf(df.format(date));
    }

    public static long convertStringToDateLong(long source){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = null;
        try {
            date = sdf.parse(String.valueOf(source));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date.getTime()/1000;
    }

    /**
     * 计算两个日期相隔的天数
     * @param firstDate
     * @param lastDate
     * @return
     */
    public static int daysOfTwo(Date firstDate, Date lastDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(firstDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(lastDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

        return day2 - day1;

    }
    public static void main(String[] args){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now.getTime());
        calendar.add(Calendar.DATE, -2);

        System.out.println(now.getTime());
        System.out.println(calendar.getTime().getTime());
    }
}
