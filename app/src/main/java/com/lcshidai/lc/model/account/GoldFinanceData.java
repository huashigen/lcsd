package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GoldFinanceData extends BaseData {
	private String myTyj;//
	private String myYield;//
	private String bonus_stay_total;//

	public String getMyTyj() {
		return myTyj;
	}

	@JsonProperty("myTyj")
	public void setMyTyj(String myTyj) {
		this.myTyj = myTyj;
	}

	public String getMyYield() {
		return myYield;
	}

	@JsonProperty("myYield")
	public void setMyYield(String myYield) {
		this.myYield = myYield;
	}

	public String getBonus_stay_total() {
		return bonus_stay_total;
	}

	@JsonProperty("bonus_stay_total")
	public void setBonus_stay_total(String bonus_stay_total) {
		this.bonus_stay_total = bonus_stay_total;
	}

}
