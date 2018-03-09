package com.lcshidai.lc.model.more;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseJson;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InviteJson extends BaseJson {
	private InviteData data;


	public InviteData getData() {
		return data;
	}

	@JsonProperty("data")
	public void setData(InviteData data) {
		this.data = data;
	}


	
}
