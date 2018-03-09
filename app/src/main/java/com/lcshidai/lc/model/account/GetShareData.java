package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetShareData extends BaseData {
	private String action; //
	private String img_url; //
	private String title; //
	private String content; //
	private String url; //
	private String btn_name; //

	public String getAction() {
		return action;
	}

	@JsonProperty("action")
	public void setAction(String action) {
		this.action = action;
	}

	public String getImg_url() {
		return img_url;
	}

	@JsonProperty("img_url")
	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	@JsonProperty("content")
	public void setContent(String content) {
		this.content = content;
	}

	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

	public String getBtn_name() {
		return btn_name;
	}

	@JsonProperty("btn_name")
	public void setBtn_name(String btn_name) {
		this.btn_name = btn_name;
	}

}
