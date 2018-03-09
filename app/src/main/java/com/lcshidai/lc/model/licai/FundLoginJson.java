package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundLoginJson extends BaseJson {

    private FundLoginData data;

    public FundLoginData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(FundLoginData data) {
        this.data = data;
    }
}
