package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenData implements Serializable{
    private String access_token;
    private String effective_time;

    public String getAccess_token() {
        return access_token;
    }

    @JsonProperty("access_token")
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getEffective_time() {
        return effective_time;
    }

    @JsonProperty("effective_time")
    public void setEffective_time(String effective_time) {
        this.effective_time = effective_time;
    }
}
