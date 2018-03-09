package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MaterialInfoListJson extends BaseJson {

	private List<MaterialInfoData> list;

	public List<MaterialInfoData> getList() {
		return list;
	}

	@JsonProperty("data")
	public void setList(List<MaterialInfoData> list) {
		this.list = list;
	}

}
