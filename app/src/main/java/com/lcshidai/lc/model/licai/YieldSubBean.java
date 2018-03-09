package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by RandyZhang on 16/9/28.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class YieldSubBean implements Serializable {
    private String invest_from;
    private String invest_to;
    private String min_buy_amount;
    private String annual_rate;
    private String rake_back_rate;

    public String getInvest_from() {
        return invest_from;
    }

    public void setInvest_from(String invest_from) {
        this.invest_from = invest_from;
    }

    public String getInvest_to() {
        return invest_to;
    }

    public void setInvest_to(String invest_to) {
        this.invest_to = invest_to;
    }

    public String getMin_buy_amount() {
        return min_buy_amount;
    }

    public void setMin_buy_amount(String min_buy_amount) {
        this.min_buy_amount = min_buy_amount;
    }

    public String getAnnual_rate() {
        return annual_rate;
    }

    public void setAnnual_rate(String annual_rate) {
        this.annual_rate = annual_rate;
    }

    public String getRake_back_rate() {
        return rake_back_rate;
    }

    public void setRake_back_rate(String rake_back_rate) {
        this.rake_back_rate = rake_back_rate;
    }
}
