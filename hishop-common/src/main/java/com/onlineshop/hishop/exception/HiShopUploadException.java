package com.onlineshop.hishop.exception;

/**
 * @author imagine
 */
public class HiShopUploadException extends RuntimeException {

    private String msg;

    public HiShopUploadException(String msg){
        super(msg);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
