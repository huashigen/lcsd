package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundDetailInfoJson extends BaseJson {

    private FundDetailInfoData data;

    public FundDetailInfoData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(FundDetailInfoData data) {
        this.data = data;
    }
}
