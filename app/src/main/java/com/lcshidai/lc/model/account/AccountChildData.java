package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountChildData extends BaseData implements Serializable{
	private String money_invest;//
	private String will_profit_view;//  
	 

	 
	public String getMoney_invest() {
		return money_invest;
	}
	@JsonProperty("money_invest")
	public void setMoney_invest(String money_invest) {
		this.money_invest = money_invest;
	}

	public String getWill_profit_view() {
		return will_profit_view;
	}
	@JsonProperty("will_profit_view")
	public void setWill_profit_view(String will_profit_view) {
		this.will_profit_view = will_profit_view;
	}
	
}
