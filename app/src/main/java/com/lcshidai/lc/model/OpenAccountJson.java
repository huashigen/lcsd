package com.lcshidai.lc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by RandyZhang on 16/7/7.
 */
public class OpenAccountJson {
    private String boolen;
    private String message;
    private OpenAccountData data;

    public String getBoolen() {
        return boolen;
    }

    @JsonProperty("boolen")
    public void setBoolen(String boolen) {
        this.boolen = boolen;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    public OpenAccountData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(OpenAccountData data) {
        this.data = data;
    }
}
