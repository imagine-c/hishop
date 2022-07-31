package com.onlineshop.hishop.utils;

import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.*;

public class addDateMinut {

    public static Date addYear(Date date,int year){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(YEAR,year);
        return rightNow.getTime();
    }

    public static Date addMonth(Date date,int month){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(MONTH,month);
        return rightNow.getTime();
    }

    public static Date addDay(Date date,int day){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(DAY_OF_YEAR,day);
        return rightNow.getTime();
    }

    public static Date addHour(Date date,int hour){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(HOUR_OF_DAY,hour);
        return rightNow.getTime();
    }

    public static Date addMinute(Date date,int minute){
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(date);
        rightNow.add(MINUTE,minute);
        return rightNow.getTime();
    }
}
