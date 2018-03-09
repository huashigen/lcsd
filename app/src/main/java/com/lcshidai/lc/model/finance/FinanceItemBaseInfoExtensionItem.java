package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoExtensionItem extends BaseData {

	private String k;
	private String v;
	private List<FinanceItemBaseInfoExtensionVInItem> vList;

	public FinanceItemBaseInfoExtensionItem() {

	}

	public FinanceItemBaseInfoExtensionItem(String me) {

	}

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

	public void setV(String v) {
		this.v = v;
	}

	public List<FinanceItemBaseInfoExtensionVInItem> getvList() {
		return vList;
	}

	@JsonProperty("v")
	public void setvList(List<FinanceItemBaseInfoExtensionVInItem> vList) {
		this.vList = vList;
	}

}
