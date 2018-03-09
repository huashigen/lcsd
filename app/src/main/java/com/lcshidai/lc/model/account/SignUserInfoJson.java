package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 16/7/7.
 */
public class SignUserInfoJson extends BaseJson{
    private SignUserInfoData data;

    public SignUserInfoData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(SignUserInfoData data) {
        this.data = data;
    }
}
