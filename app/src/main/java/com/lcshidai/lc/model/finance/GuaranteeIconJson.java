package com.lcshidai.lc.model.finance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GuaranteeIconJson extends BaseJson {

	private List<GuaranteeIconData> data;

	public List<GuaranteeIconData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<GuaranteeIconData> data) {
		this.data = data;
	}
	
}