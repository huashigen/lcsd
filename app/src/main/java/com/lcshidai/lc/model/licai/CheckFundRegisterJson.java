package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckFundRegisterJson extends BaseJson {

    private CheckFundRegisterData data;

    public CheckFundRegisterData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(CheckFundRegisterData data) {
        this.data = data;
    }
}
