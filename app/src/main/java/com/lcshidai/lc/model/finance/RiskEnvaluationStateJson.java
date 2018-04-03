package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskEnvaluationStateJson extends BaseJson {

    private String data;
    private String message;

    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }
}
