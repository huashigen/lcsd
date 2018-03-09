package com.lcshidai.lc.model.finance;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchConditionData extends BaseData {
	private List<ConditionItemData> data;
	private String name;

	public List<ConditionItemData> getData() {
		return data;
	}

	public void setData(List<ConditionItemData> data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
