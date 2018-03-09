package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 2016/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundTypeListJson extends BaseJson {
    FundTypeListData data;

    public FundTypeListData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(FundTypeListData data) {
        this.data = data;
    }
}
