package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetEscrowRemindData extends BaseData {

    private int flag;
    private String info;
    private int is_allow_escrow;
    private String openEscrowUrl;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getIs_allow_escrow() {
        return is_allow_escrow;
    }

    public void setIs_allow_escrow(int is_allow_escrow) {
        this.is_allow_escrow = is_allow_escrow;
    }

    public String getOpenEscrowUrl() {
        return openEscrowUrl;
    }

    @JsonProperty("openEscrowUrl")
    public void setOpenEscrowUrl(String openEscrowUrl) {
        this.openEscrowUrl = openEscrowUrl;
    }
}
