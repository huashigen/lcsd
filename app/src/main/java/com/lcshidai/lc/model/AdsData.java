package com.lcshidai.lc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AdsData extends BaseData {
	private String id;
	private String img_url;
	private String link_url;

	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	public String getImg_url() {
		return img_url;
	}

	@JsonProperty("img_url")
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getLink_url() {
		return link_url;
	}

	@JsonProperty("link_url")
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}

}
