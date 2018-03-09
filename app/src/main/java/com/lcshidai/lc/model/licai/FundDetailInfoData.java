package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundDetailInfoData implements Serializable {
    private FundDetailInfo info;

    public FundDetailInfo getInfo() {
        return info;
    }

    @JsonProperty("info")
    public void setInfo(FundDetailInfo info) {
        this.info = info;
    }
}
