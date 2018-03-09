package com.lcshidai.lc.model.account;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lcshidai.lc.model.BaseData;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountSettingImg extends BaseData {
	private String url_s100; // 充值角标
	private String url_s300; // 提现角标
	private String url_s50; // 充值角标
	private String url_s700; // 提现角标
	private String url; // 提现角标

	public String getUrl_s100() {
		return url_s100;
	}

	@JsonProperty("url_s100")
	public void setUrl_s100(String url_s100) {
		this.url_s100 = url_s100;
	}

	public String getUrl_s300() {
		return url_s300;
	}

	@JsonProperty("url_s300")
	public void setUrl_s300(String url_s300) {
		this.url_s300 = url_s300;
	}

	public String getUrl_s50() {
		return url_s50;
	}

	@JsonProperty("url_s50")
	public void setUrl_s50(String url_s50) {
		this.url_s50 = url_s50;
	}

	public String getUrl_s700() {
		return url_s700;
	}

	@JsonProperty("url_s700")
	public void setUrl_s700(String url_s700) {
		this.url_s700 = url_s700;
	}

	public String getUrl() {
		return url;
	}

	@JsonProperty("url")
	public void setUrl(String url) {
		this.url = url;
	}

}
