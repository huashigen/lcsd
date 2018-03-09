package com.lcshidai.lc.model.Loading;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoadingData extends BaseData {
	LoadingImg img;

	public LoadingData() {

	}

	public LoadingData(String me) {

	}

	public LoadingImg getImg() {
		return img;
	}

	@JsonProperty("img")
	public void setImg(LoadingImg img) {
		this.img = img;
	}

}
