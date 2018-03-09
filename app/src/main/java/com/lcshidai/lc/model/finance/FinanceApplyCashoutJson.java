package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceApplyCashoutJson extends BaseJson{
	
	private String cashoutId ;
	private String remark ;
	
	
	public String getCashoutId() {
		return cashoutId;
	}
	
	@JsonProperty("cashoutId")
	public void setCashoutId(String cashoutId) {
		this.cashoutId = cashoutId;
	}
	public String getRemark() {
		return remark;
	}
	
	@JsonProperty("remark")
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

}
