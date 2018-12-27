package com.zhilong.springcloud.utils;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    static Calendar calendar = Calendar.getInstance();

    public static int getCurrentDayOfMonth(){
        setCalendarInstanceCurrentTime();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHourOfDay(){
        setCalendarInstanceCurrentTime();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static Date getSharpClock(){
        setCalendarInstanceCurrentTime();
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    public static Date addHourDate(Date date,int hour){
        setCalendarInstanceCurrentTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,hour);
        return calendar.getTime();
    }

    public static Date addHour2CurrentTime(int hour){
        setCalendarInstanceCurrentTime();
        calendar.add(Calendar.HOUR_OF_DAY,hour);
        return calendar.getTime();
    }

    private static void setCalendarInstanceCurrentTime() {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.setTime(new Date());
    }

    private static void setCalendarInstanceCurrentTime(Date date) {
        if (calendar == null) {
            calendar = Calendar.getInstance();
        }
        calendar.setTime(date);
    }

}
