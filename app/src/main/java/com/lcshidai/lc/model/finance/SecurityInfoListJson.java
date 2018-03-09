package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityInfoListJson extends BaseJson{

	private List<SecurityBaseInfoData> list;

 

	public List<SecurityBaseInfoData> getList() {
		return list;
	}

	@JsonProperty("data")
	public void setList(List<SecurityBaseInfoData> list) {
		this.list = list;
	}

	 
}
