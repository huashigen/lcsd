package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;
import com.lcshidai.lc.model.Page;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceProjectInvestRecordData extends BaseData {
    private Page page; //
    private List<FinanceProjectInvestRecordItem> list;
    private String remaining_amount;

    private List<InvestRecordTopRankItem> top_ranks;

    public Page getPage() {
        return page;
    }

    @JsonProperty("page")
    public void setPage(Page page) {
        this.page = page;
    }

    public List<FinanceProjectInvestRecordItem> getList() {
        return list;
    }

    @JsonProperty("list")
    public void setList(List<FinanceProjectInvestRecordItem> list) {
        this.list = list;
    }

    public String getRemaining_amount() {
        return remaining_amount;
    }

    @JsonProperty("remaining_amount")
    public void setRemaining_amount(String remaining_amount) {
        this.remaining_amount = remaining_amount;
    }

    public List<InvestRecordTopRankItem> getTop_ranks() {
        return top_ranks;
    }

    @JsonProperty("top_ranks")
    public void setTop_ranks(List<InvestRecordTopRankItem> top_ranks) {
        this.top_ranks = top_ranks;
    }

}
