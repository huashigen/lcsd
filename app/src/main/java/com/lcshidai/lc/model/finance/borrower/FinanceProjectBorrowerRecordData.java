package com.lcshidai.lc.model.finance.borrower;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;
import com.lcshidai.lc.model.Page;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceProjectBorrowerRecordData extends BaseData {
    private Page page; //
    private List<FinanceProjectBorrowerRecordItem> list;
    private String totalMoney;

    public String getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(String total) {
        this.total = total;
    }

    private String total;

    public Page getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Page page) {
        this.page = page;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    @JsonProperty("total_money")
    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public List<FinanceProjectBorrowerRecordItem> getList() {
        return list;
    }

    @JsonProperty("list")
    public void setList(List<FinanceProjectBorrowerRecordItem> list) {
        this.list = list;
    }

}
