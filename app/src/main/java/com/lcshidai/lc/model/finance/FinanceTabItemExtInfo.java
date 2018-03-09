package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class FinanceTabItemExtInfo {
	private String big_icon;

	public String getBig_icon() {
		return big_icon;
	}

	@JsonProperty("big_icon")
	public void setBig_icon(String big_icon) {
		this.big_icon = big_icon;
	}
}
