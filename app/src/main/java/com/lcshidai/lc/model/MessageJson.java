package com.lcshidai.lc.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageJson extends BaseJson{
	private List<MessageData> data;

	public List<MessageData> getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(List<MessageData> data) {
		this.data = data;
	}

}
