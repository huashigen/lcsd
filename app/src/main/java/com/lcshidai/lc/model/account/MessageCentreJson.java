package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCentreJson extends BaseJson {
	private MessageCentreData data;

	public MessageCentreData getData() {
		return data;
	}

	@JsonIgnore
	@JsonProperty("data")
	public void setData(MessageCentreData data) {
		this.data = data;
	}

}
