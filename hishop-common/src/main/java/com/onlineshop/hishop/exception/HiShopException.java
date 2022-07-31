package com.onlineshop.hishop.exception;


public class HiShopException extends RuntimeException {

    private String msg;

    public HiShopException(String msg){
        super(msg);
        this.msg=msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
