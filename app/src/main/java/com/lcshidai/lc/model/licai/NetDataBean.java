package com.lcshidai.lc.model.licai;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/9/28.
 */
public class NetDataBean implements Serializable {
    private String date;
    private String value;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

