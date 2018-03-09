package com.lcshidai.lc.model.finance;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoFundInfoVInItem extends BaseData {

	private String k;
//	private String v;
	public ArrayList<FinanceItemBaseInfoFundInfoChildVInItem> v = new ArrayList<FinanceItemBaseInfoFundInfoChildVInItem>();
 
	public FinanceItemBaseInfoFundInfoVInItem() {

	}

	public FinanceItemBaseInfoFundInfoVInItem(String me) {

	}

	public String getK() {
		return k;
	}
	@JsonProperty("k")
	public void setK(String k) {
		this.k = k;
	}

	public ArrayList<FinanceItemBaseInfoFundInfoChildVInItem> getV() {
		return v;
	}
	@JsonProperty("v")
	public void setV(ArrayList<FinanceItemBaseInfoFundInfoChildVInItem> v) {
		this.v = v;
	}
 

	 
}
