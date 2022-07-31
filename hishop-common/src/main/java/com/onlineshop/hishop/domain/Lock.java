package com.onlineshop.hishop.domain;

import java.io.Serializable;

public class Lock implements Serializable{
    private String name;
    private String value;

    public Lock(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
