package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FinanceItemBaseInfoMaterialItem extends BaseData {

	private String big;
	private String small;
	private String name;

	public FinanceItemBaseInfoMaterialItem() {

	}

	public FinanceItemBaseInfoMaterialItem(String me) {

	}

	public String getBig() {
		return big;
	}

	@JsonProperty("big")
	public void setBig(String big) {
		this.big = big;
	}

	public String getSmall() {
		return small;
	}

	@JsonProperty("small")
	public void setSmall(String small) {
		this.small = small;
	}

	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

}
