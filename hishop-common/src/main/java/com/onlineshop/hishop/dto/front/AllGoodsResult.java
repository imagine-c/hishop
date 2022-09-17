package com.onlineshop.hishop.dto.front;

import java.io.Serializable;
import java.util.List;

/**
 * @author imagine
 */
public class AllGoodsResult implements Serializable {

    private int total;

    private List<?> data;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
