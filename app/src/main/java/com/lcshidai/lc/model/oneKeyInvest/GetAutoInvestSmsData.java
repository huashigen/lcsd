package com.lcshidai.lc.model.oneKeyInvest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetAutoInvestSmsData extends BaseData {
    private int is_auto_bid;
    private int is_ecrow;
    private int open_again;

    public int getIs_auto_bid() {
        return is_auto_bid;
    }

    @JsonProperty("is_auto_bid")
    public void setIs_auto_bid(int is_auto_bid) {
        this.is_auto_bid = is_auto_bid;
    }

    public int getIs_ecrow() {
        return is_ecrow;
    }

    @JsonProperty("is_ecrow")
    public void setIs_ecrow(int is_ecrow) {
        this.is_ecrow = is_ecrow;
    }

    public int getOpen_again() {
        return open_again;
    }

    @JsonProperty("open_again")
    public void setOpen_again(int open_again) {
        this.open_again = open_again;
    }
}
