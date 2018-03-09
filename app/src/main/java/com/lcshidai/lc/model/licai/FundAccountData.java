package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/10/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundAccountData implements Serializable {
    private FundAccountInfo info;

    public FundAccountInfo getInfo() {
        return info;
    }

    @JsonProperty("info")
    public void setInfo(FundAccountInfo info) {
        this.info = info;
    }
}
