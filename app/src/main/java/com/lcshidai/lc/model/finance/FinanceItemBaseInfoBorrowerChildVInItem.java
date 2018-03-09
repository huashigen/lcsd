package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoBorrowerChildVInItem extends BaseData {

	private String k;
	private String v;

	public String getK() {
		return k;
	}

	@JsonProperty("k")
	public void setK(String k) {
		this.k = k;
	}

	public String getV() {
		return v;
	}

	@JsonProperty("v")
	public void setV(String v) {
		this.v = v;
	}

	 
 

}
