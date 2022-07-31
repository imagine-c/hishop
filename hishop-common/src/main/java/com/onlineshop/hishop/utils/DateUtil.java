package com.onlineshop.hishop.utils;

public class DateUtil {
    public static int date(String str,String type){
        return Integer.parseInt(str.split(type)[0]);
    }

    public static String type(String str){
        return str.substring(str.length()-1,str.length());
    }

    public static void main(String[] args) {
        System.out.println(type("30h"));
        System.out.println(date("30h",type("30h")));
    }
}
