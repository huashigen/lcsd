package com.lcshidai.lc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetHomePopJson extends BaseJson {
    private GetHomePopData data;

    public GetHomePopData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(GetHomePopData data) {
        this.data = data;
    }

}
