package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessTokenJson extends BaseJson {

    private AccessTokenData data;

    public AccessTokenData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(AccessTokenData data) {
        this.data = data;
    }
}
