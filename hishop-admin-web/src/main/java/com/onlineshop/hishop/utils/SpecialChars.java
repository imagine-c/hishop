package com.onlineshop.hishop.utils;

public class SpecialChars {

    public String HtmlSpecialChars(String str){
        str = str.replaceAll("&", "&amp;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("\"", "&quot;");
        return str;
    }
}
