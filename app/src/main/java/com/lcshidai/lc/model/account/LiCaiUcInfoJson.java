package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

/**
 * Created by RandyZhang on 2016/10/11.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LiCaiUcInfoJson extends BaseJson{
    LicaiUcInfoData data;

    public LicaiUcInfoData getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(LicaiUcInfoData data) {
        this.data = data;
    }
}
