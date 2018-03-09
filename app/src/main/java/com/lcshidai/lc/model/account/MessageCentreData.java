package com.lcshidai.lc.model.account;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.Page;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCentreData {
	private Page pagedata;
	private List<MessageCentreInfo> infodata;

	public MessageCentreData() {

	}

	public MessageCentreData(String me) {

	}
	
	public Page getPagedata() {
		return pagedata;
	}
	@JsonProperty("page")
	public void setPagedata(Page pagedata) {
		this.pagedata = pagedata;
	}

	public List<MessageCentreInfo> getInfodata() {
		return infodata;
	}

	@JsonProperty("data")
	public void setInfodata(List<MessageCentreInfo> infodata) {
		this.infodata = infodata;
	}

}
