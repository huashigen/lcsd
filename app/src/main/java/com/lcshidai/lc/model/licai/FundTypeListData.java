package com.lcshidai.lc.model.licai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by RandyZhang on 2016/10/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FundTypeListData {
    List<String> fundTypeList;

    public List<String> getFundTypeList() {
        return fundTypeList;
    }

    @JsonProperty("fund_type_list")
    public void setFundTypeList(List<String> fundTypeList) {
        this.fundTypeList = fundTypeList;
    }
}
