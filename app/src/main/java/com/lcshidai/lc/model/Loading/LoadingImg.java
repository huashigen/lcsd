package com.lcshidai.lc.model.Loading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadingImg extends BaseData {
	LoadingAttach attach;

	public LoadingImg() {

	}

	public LoadingImg(String me) {

	}

	public LoadingAttach getAttach() {
		return attach;
	}

	@JsonProperty("attach")
	public void setAttach(LoadingAttach attach) {
		this.attach = attach;
	}

}