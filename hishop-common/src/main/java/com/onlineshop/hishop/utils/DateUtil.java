package com.onlineshop.hishop.utils;

public class DateUtil {
    public static int date(String str,String type){
        return Integer.parseInt(str.split(type)[0]);
    }

    public static String type(String str){
        return str.substring(str.length()-1,str.length());
    }

}
