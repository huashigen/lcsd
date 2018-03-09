package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSharkData extends BaseData {
	private String num; //
	private String url; //

	public String getNum() {
		return num;
	}

	@JsonProperty("num")
	public void setNum(String num) {
		this.num = num;
	}

	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

}
