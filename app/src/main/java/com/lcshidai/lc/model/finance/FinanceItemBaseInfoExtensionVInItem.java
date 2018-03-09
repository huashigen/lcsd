package com.lcshidai.lc.model.finance;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoExtensionVInItem extends BaseData {

	private String k;
	private String v;
	public ArrayList<FinanceItemBaseInfoExtensionVInItem> valueArray = new ArrayList<FinanceItemBaseInfoExtensionVInItem>();
	public ArrayList<String> vStr = new ArrayList<String>();

	public FinanceItemBaseInfoExtensionVInItem() {

	}

	public FinanceItemBaseInfoExtensionVInItem(String me) {

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

	@JsonProperty("v")
	public void setV(String v) {
		this.v = v;
	}

	public ArrayList<FinanceItemBaseInfoExtensionVInItem> getValueArray() {
		return valueArray;
	}

	public void setValueArray(
			ArrayList<FinanceItemBaseInfoExtensionVInItem> valueArray) {
		this.valueArray = valueArray;
	}

	public ArrayList<String> getvStr() {
		return vStr;
	}

	public void setvStr(ArrayList<String> vStr) {
		this.vStr = vStr;
	}

}
