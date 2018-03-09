package com.lcshidai.lc.model.oneKeyInvest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAutoInvestSmsJson extends BaseJson {
    private GetAutoInvestSmsData data;

    public GetAutoInvestSmsData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(GetAutoInvestSmsData data) {
        this.data = data;
    }

}
