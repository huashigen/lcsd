package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RandyZhang on 16/9/28.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class YieldBean implements Serializable {
    private String time_limit;
    /**
     * invest_from : 100
     * invest_to : 300
     * min_buy_amount : 100
     * annual_rate : 8.00
     * rake_back_rate : 0.8
     */

    private List<YieldSubBean> yield_sub;

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
    }

    public List<YieldSubBean> getYield_sub() {
        return yield_sub;
    }

    public void setYield_sub(List<YieldSubBean> yield_sub) {
        this.yield_sub = yield_sub;
    }

}

