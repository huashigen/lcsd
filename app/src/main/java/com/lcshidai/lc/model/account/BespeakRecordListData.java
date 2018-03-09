package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.Page;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BespeakRecordListData {
	private Page page; //
	private List<BespeakRecordListItem> list;

	public BespeakRecordListData() {

	}

	public BespeakRecordListData(String me) {

	}
	
	public Page getPage() {
		return page;
	}

	@JsonProperty("page_info")
	public void setPage(Page page) {
		this.page = page;
	}

	public List<BespeakRecordListItem> getList() {
		return list;
	}

	@JsonProperty("list")
	public void setList(List<BespeakRecordListItem> list) {
		this.list = list;
	}

}
