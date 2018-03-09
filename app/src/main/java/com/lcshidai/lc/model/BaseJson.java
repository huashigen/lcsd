package com.lcshidai.lc.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseJson {

    private String message;
    private String boolen;
    private String logined;
    private String down;
    private String url;
    private String status;


    public String getLogined() {
        return logined;
    }

    @JsonProperty("logined")
    public void setLogined(String logined) {
        this.logined = logined;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setErrorMsg(String message) {
        this.message = message;
    }

    public String getBoolen() {
        return boolen;
    }

    @JsonProperty("boolen")
    public void setBoolen(String boolen) {
        this.boolen = boolen;
    }

    public String getDown() {
        return down;
    }

    @JsonProperty("down")
    public void setDown(String down) {
        this.down = down;
    }

    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }
}
