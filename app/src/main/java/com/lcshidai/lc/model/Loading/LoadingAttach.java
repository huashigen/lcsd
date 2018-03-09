package com.lcshidai.lc.model.Loading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadingAttach extends BaseData {
	private String url; // 提现角标

	public LoadingAttach() {

	}

	public LoadingAttach(String me) {

	}

	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

}