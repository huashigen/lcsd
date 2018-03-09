package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundLoginData implements Serializable {
    private String user_token;
    private FundLoginInfo info;

    public String getUser_token() {
        return user_token;
    }

    @JsonProperty("user_token")
    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public FundLoginInfo getInfo() {
        return info;
    }

    @JsonProperty("info")
    public void setInfo(FundLoginInfo info) {
        this.info = info;
    }
}
