package com.lcshidai.lc.model.finance;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoBorrowerVInItem extends BaseData {

	private String k;
//	private String v;
	public ArrayList<FinanceItemBaseInfoBorrowerChildVInItem> v = new ArrayList<FinanceItemBaseInfoBorrowerChildVInItem>();
 
	public FinanceItemBaseInfoBorrowerVInItem() {

	}

	public FinanceItemBaseInfoBorrowerVInItem(String me) {

	}

	public String getK() {
		return k;
	}
	@JsonProperty("k")
	public void setK(String k) {
		this.k = k;
	}

	public ArrayList<FinanceItemBaseInfoBorrowerChildVInItem> getV() {
		return v;
	}
	@JsonProperty("v")
	public void setV(ArrayList<FinanceItemBaseInfoBorrowerChildVInItem> v) {
		this.v = v;
	}
 

	 
}
