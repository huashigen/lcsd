package com.lcshidai.lc.model.finance.reward;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class NoUseModel {

	private String remain_money;
	private String benxi;
    private String total_rate;
	public String getRemain_money() {
		return remain_money;
	}
	
	@JsonProperty("remain_money")
	public void setRemain_money(String remain_money) {
		this.remain_money = remain_money;
	}
	public String getBenxi() {
		return benxi;
	}
	
	@JsonProperty("benxi")
	public void setBenxi(String benxi) {
		this.benxi = benxi;
	}
	public String getTotal_rate() {
		return total_rate;
	}
	
	@JsonProperty("total_rate")
	public void setTotal_rate(String total_rate) {
		this.total_rate = total_rate;
	}
	
    
    
}
