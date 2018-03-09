package com.lcshidai.lc.model.account;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileCheckJson extends BaseJson implements Serializable {
    private MobileCheckData data;

    public MobileCheckData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(MobileCheckData data) {
        this.data = data;
    }

}
