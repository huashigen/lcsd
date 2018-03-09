package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/7/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignJson extends BaseJson{
    private SignData data;

    public SignData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(SignData data) {
        this.data = data;
    }
}
