package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 投资记录排名Item
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvestRecordTopRankItem {
    private int ranking;
    private FinanceProjectInvestRecordItem data;
    private String ranking_img;

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public FinanceProjectInvestRecordItem getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(FinanceProjectInvestRecordItem data) {
        this.data = data;
    }

    public String getRanking_img() {
        return ranking_img;
    }

    public void setRanking_img(String ranking_img) {
        this.ranking_img = ranking_img;
    }

}
