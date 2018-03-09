package com.lcshidai.lc.model.finance.borrower;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceProjectBorrowerRecordItem {
    public String custName;
    public String idNo;
    public String amount;
    public String mobile;
    public String startTime;
    public String endTime;

    public String getCustName() {
        return custName;
    }

    @JsonProperty("cust_name")
    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getIdNo() {
        return idNo;
    }

    @JsonProperty("id_no")
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getAmount() {
        return amount;
    }

    @JsonProperty("contract_amount")
    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile() {
        return mobile;
    }

    @JsonProperty("mobile_no")
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStartTime() {
        return startTime;
    }

    @JsonProperty("start_date")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @JsonProperty("end_date")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
