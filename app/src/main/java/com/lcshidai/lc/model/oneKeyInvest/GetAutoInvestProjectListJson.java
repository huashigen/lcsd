package com.lcshidai.lc.model.oneKeyInvest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAutoInvestProjectListJson extends BaseJson {
    private List<CollectionProData> data;

    public List<CollectionProData> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<CollectionProData> data) {
        this.data = data;
    }

}
