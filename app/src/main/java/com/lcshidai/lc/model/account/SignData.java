package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

/**
 * Created by RandyZhang on 16/7/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignData extends BaseData {
    private String score;
    private String info;
    private int flag;

    public String getScore() {
        return score;
    }

    @JsonProperty("score")
    public void setScore(String score) {
        this.score = score;
    }

    public String getInfo() {
        return info;
    }

    @JsonProperty("info")
    public void setInfo(String info) {
        this.info = info;
    }

    public int getFlag() {
        return flag;
    }

    @JsonProperty("flag")
    public void setFlag(int flag) {
        this.flag = flag;
    }
}
