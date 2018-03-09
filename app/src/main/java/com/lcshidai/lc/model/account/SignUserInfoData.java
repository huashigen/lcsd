package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

/**
 * Created by RandyZhang on 16/7/7.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUserInfoData extends BaseData {
    // 总积分
    private String totalScore;
    // 今日积分
    private String totayScore;

    public String getTotalScore() {
        return totalScore;
    }

    @JsonProperty("total_integral")
    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getTotayScore() {
        return totayScore;
    }

    @JsonProperty("today_integral")
    public void setTotayScore(String totayScore) {
        this.totayScore = totayScore;
    }
}
