package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by RandyZhang on 2016/10/28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundItemsBean implements Serializable {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
